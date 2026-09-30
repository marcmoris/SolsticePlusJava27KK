/**
 * Timesheet Service
 * Provides full API abstraction for Solstice+ TimeSheet Module.
 * Ready to be connected to real REST/GraphQL endpoints or operates seamlessly on local state/mock.
 */

import { API_BASE_URL } from '../config';
import { MOCK_EMPLOYEES, MOCK_PAY_PERIODS, MOCK_TIMESHEETS, MOCK_LOOKUPS } from '../data/mockData';
import * as XLSX from 'xlsx';

// Local storage key for persistent offline edits
const STORAGE_KEY = 'solstice_timesheets_db';

function loadStoredTimesheets() {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (raw) return JSON.parse(raw);
  } catch (e) {
    console.error('Error loading stored timesheets', e);
  }
  return { ...MOCK_TIMESHEETS };
}

function saveStoredTimesheets(data) {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(data));
  } catch (e) {
    console.error('Error saving timesheets to localStorage', e);
  }
}

let activeTimesheets = loadStoredTimesheets();

export const timesheetService = {
  /**
   * Retrieves list of employees with optional filters
   */
  async getEmployees({ status = 'active', search = '', timesheetFilter = null } = {}) {
    try {
      const url = new URL(`${API_BASE_URL}/employees`);
      if (status) url.searchParams.set('status', status);
      if (search && search.trim()) url.searchParams.set('search', search.trim());
      
      const res = await fetch(url.toString(), { signal: AbortSignal.timeout(3000) });
      if (res.ok) {
        const data = await res.json();
        if (Array.isArray(data) && data.length > 0) {
          return data;
        }
      }
    } catch (err) {
      console.warn('[Timesheet API] Impossible de contacter le serveur, bascule sur données de secours:', err.message);
    }

    // Fallback: mock data
    return MOCK_EMPLOYEES.filter((emp) => {
      if (status === 'active' && !emp.isActive) return false;
      if (status === 'inactive' && emp.isActive) return false;
      if (search.trim()) {
        const q = search.toLowerCase();
        const matchCode = emp.value.toLowerCase().includes(q);
        const matchName = emp.name.toLowerCase().includes(q);
        const matchDept = emp.department.toLowerCase().includes(q);
        if (!matchCode && !matchName && !matchDept) return false;
      }
      return true;
    });
  },

  /**
   * Retrieves all pay periods
   */
  async getPayPeriods() {
    try {
      const res = await fetch(`${API_BASE_URL}/periods`, { signal: AbortSignal.timeout(3000) });
      if (res.ok) {
        const data = await res.json();
        if (Array.isArray(data) && data.length > 0) {
          return data;
        }
      }
    } catch (err) {
      console.warn('[Timesheet API] Impossible de charger les périodes, secours local:', err.message);
    }
    return [...MOCK_PAY_PERIODS];
  },

  /**
   * Retrieves all lookup dictionaries (gains, assignments, schedules, activities, etc.)
   */
  async getLookups() {
    try {
      const res = await fetch(`${API_BASE_URL}/lookups`, { signal: AbortSignal.timeout(3000) });
      if (res.ok) {
        const data = await res.json();
        if (data && data.gains) {
          return data;
        }
      }
    } catch (err) {
      console.warn('[Timesheet API] Impossible de charger les dictionnaires, secours local:', err.message);
    }
    return { ...MOCK_LOOKUPS };
  },

  /**
   * Retrieves a timesheet for a specific employee and period
   */
  async getTimeSheet(employeeId, periodId) {
    try {
      const res = await fetch(`${API_BASE_URL}/timesheets/${employeeId}/${periodId}`, { signal: AbortSignal.timeout(3000) });
      if (res.ok) {
        const data = await res.json();
        if (data && data.sheetNumber) {
          return data;
        }
      }
    } catch (err) {
      console.warn('[Timesheet API] Erreur chargement feuille temps réelle, secours local:', err.message);
    }

    const key = `${employeeId}_${periodId}`;
    if (activeTimesheets[key]) {
      return JSON.parse(JSON.stringify(activeTimesheets[key]));
    }

    // Default empty timesheet if none exists
    const employee = MOCK_EMPLOYEES.find((e) => e.id === Number(employeeId));
    const period = MOCK_PAY_PERIODS.find((p) => p.id === Number(periodId));

    return {
      id: 0,
      employeeId: Number(employeeId),
      periodId: Number(periodId),
      sheetNumber: `${period ? period.name : '2026'}-${employee ? employee.value.replace('EMP-', '') : 'NEW'}`,
      sheetType: 'Regular',
      status: 'Initial',
      paymentType: employee ? employee.paymentType : 'Deposit',
      grossPay: 0,
      netPay: 0,
      hasChat: false,
      hasAttachment: false,
      warnings: [],
      errors: [],
      creditsBank: [
        { id: 'BV20B', code: 'BV20B', name: 'Banque Vacances', unit: 'H', startBalance: 80.0, periodVariation: 0.0, newBalance: 80.0 },
        { id: 'BV21', code: 'BV21', name: 'Congés Fériés', unit: 'H', startBalance: 16.0, periodVariation: 0.0, newBalance: 16.0 },
        { id: 'BV10', code: 'BV10', name: 'Banque Heures Supp', unit: 'H', startBalance: 0.0, periodVariation: 0.0, newBalance: 0.0 },
        { id: 'BV05', code: 'BV05', name: 'Congés Maladie', unit: 'J', startBalance: 5.0, periodVariation: 0.0, newBalance: 5.0 },
      ],
      details: [
        {
          id: Date.now(),
          week: 1,
          assignmentId: 301,
          gainId: 101,
          scheduleId: 401,
          activityId: 501,
          originTime: 'Saisie manuelle',
          hours: [0, 8.0, 8.0, 8.0, 8.0, 8.0, 0],
          quantity: 40.0,
        },
        {
          id: Date.now() + 1,
          week: 2,
          assignmentId: 301,
          gainId: 101,
          scheduleId: 401,
          activityId: 501,
          originTime: 'Saisie manuelle',
          hours: [0, 8.0, 8.0, 8.0, 8.0, 8.0, 0],
          quantity: 40.0,
        },
      ],
      primes: [],
      creditMovements: [],
      deductions: [
        { id: Date.now() + 10, deductionId: 601, name: 'RRQ - Régime de rentes du Québec', salaryEligible: 2600.0, hoursEligible: 80.0, employeePart: 140.40, employerPart: 140.40, accumulation: 1540.0, retrieval: 0.0, origin: 'Automatique' },
        { id: Date.now() + 11, deductionId: 602, name: 'RQAP - Régime d\'assurance parentale', salaryEligible: 2600.0, hoursEligible: 80.0, employeePart: 12.84, employerPart: 17.98, accumulation: 141.0, retrieval: 0.0, origin: 'Automatique' },
        { id: Date.now() + 12, deductionId: 603, name: 'IMP-FED - Impôt sur le revenu fédéral', salaryEligible: 2600.0, hoursEligible: 80.0, employeePart: 318.0, employerPart: 0.0, accumulation: 3500.0, retrieval: 0.0, origin: 'Automatique' },
        { id: Date.now() + 13, deductionId: 604, name: 'IMP-QC - Impôt sur le revenu Québec', salaryEligible: 2600.0, hoursEligible: 80.0, employeePart: 351.0, employerPart: 0.0, accumulation: 3860.0, retrieval: 0.0, origin: 'Automatique' },
        { id: Date.now() + 14, deductionId: 605, name: 'ASS-MED - Assurance santé collective', salaryEligible: 2600.0, hoursEligible: 80.0, employeePart: 65.0, employerPart: 95.0, accumulation: 715.0, retrieval: 0.0, origin: 'Barème fixe' },
      ],
      taxableBenefits: [
        { id: Date.now() + 20, benefitId: 701, name: 'BEN-VIE - Assurance-vie collective', amount: 15.0, periodName: period ? period.name : '2026-P19', origin: 'Système' },
        { id: Date.now() + 21, benefitId: 703, name: 'BEN-SANTE - Contribution patronale assurance santé (QC)', amount: 95.0, periodName: period ? period.name : '2026-P19', origin: 'Système' },
      ],
      accountingEntries: [
        { id: Date.now() + 30, region: '0100 - Région Est', account: '5110 - Salaires réguliers', activity: 'ACT-01 - 1000 Exploitation Standard', debit: 2600.0, credit: 0.0, isCorrection: false },
        { id: Date.now() + 31, region: '0100 - Région Est', account: '5120 - Charges sociales patronales', activity: 'ACT-01 - 1000 Exploitation Standard', debit: 253.38, credit: 0.0, isCorrection: false },
        { id: Date.now() + 32, region: '0100 - Région Est', account: '2120 - Retenues et charges à payer (Fédéral)', activity: 'Administration', debit: 0.0, credit: 471.24, isCorrection: false },
        { id: Date.now() + 33, region: '0100 - Région Est', account: '2125 - Retenues et charges à payer (Québec)', activity: 'Administration', debit: 0.0, credit: 521.98, isCorrection: false },
        { id: Date.now() + 34, region: '0100 - Région Est', account: '1010 - Banque de paie (Salaires nets)', activity: 'Trésorerie', debit: 0.0, credit: 1860.16, isCorrection: false },
      ],
    };
  },

  /**
   * Saves a timesheet
   */
  async saveTimeSheet(timesheet) {
    try {
      const res = await fetch(`${API_BASE_URL}/timesheets`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(timesheet),
        signal: AbortSignal.timeout(3000),
      });
      if (res.ok) {
        console.log('[Timesheet API] Feuille sauvegardée avec succès dans SQL Server');
      }
    } catch (err) {
      console.warn('[Timesheet API] Erreur sauvegarde SQL Server, enregistrement local:', err.message);
    }

    const key = `${timesheet.employeeId}_${timesheet.periodId}`;
    const updated = {
      ...timesheet,
      id: timesheet.id || Date.now(),
    };
    activeTimesheets[key] = updated;
    saveStoredTimesheets(activeTimesheets);
    return JSON.parse(JSON.stringify(updated));
  },

  /**
   * Deletes a timesheet
   */
  async deleteTimeSheet(employeeId, periodId) {
    try {
      const res = await fetch(`${API_BASE_URL}/timesheets/${employeeId}/${periodId}`, {
        method: 'DELETE',
        signal: AbortSignal.timeout(3000),
      });
      if (res.ok) {
        console.log('[Timesheet API] Feuille supprimée de SQL Server');
      }
    } catch (err) {
      console.warn('[Timesheet API] Erreur suppression SQL Server, suppression locale:', err.message);
    }

    const key = `${employeeId}_${periodId}`;
    if (activeTimesheets[key]) {
      delete activeTimesheets[key];
      saveStoredTimesheets(activeTimesheets);
      return true;
    }
    return false;
  },

  /**
   * Executes validation / calculation on a timesheet
   */
  async validateTimeSheet(timesheet) {
    await new Promise((r) => setTimeout(r, 150));
    let totalHours = 0;
    (timesheet.details || []).forEach((d) => {
      totalHours += d.hours.reduce((acc, h) => acc + (Number(h) || 0), 0);
    });

    const hourlyRate = 32.50; // default estimated rate
    const grossPay = totalHours * hourlyRate;
    
    // Recalculate deductions proportionally if present
    const rrqEmp = Math.round(grossPay * 0.054 * 100) / 100;
    const rqapEmp = Math.round(grossPay * 0.00494 * 100) / 100;
    const rqapPat = Math.round(grossPay * 0.00692 * 100) / 100;
    const fedTax = Math.round(grossPay * 0.125 * 100) / 100;
    const qcTax = Math.round(grossPay * 0.14 * 100) / 100;
    const totalDeductions = rrqEmp + rqapEmp + fedTax + qcTax;
    const netPay = Math.round((grossPay - totalDeductions) * 100) / 100;

    // Updated deductions
    const updatedDeductions = (timesheet.deductions && timesheet.deductions.length > 0)
      ? timesheet.deductions.map(d => {
          if (d.deductionId === 601) return { ...d, salaryEligible: grossPay, hoursEligible: totalHours, employeePart: rrqEmp, employerPart: rrqEmp };
          if (d.deductionId === 602) return { ...d, salaryEligible: grossPay, hoursEligible: totalHours, employeePart: rqapEmp, employerPart: rqapPat };
          if (d.deductionId === 603) return { ...d, salaryEligible: grossPay, hoursEligible: totalHours, employeePart: fedTax };
          if (d.deductionId === 604) return { ...d, salaryEligible: grossPay, hoursEligible: totalHours, employeePart: qcTax };
          return { ...d, salaryEligible: grossPay, hoursEligible: totalHours };
        })
      : [
          { id: Date.now() + 10, deductionId: 601, name: 'RRQ - Régime de rentes du Québec', salaryEligible: grossPay, hoursEligible: totalHours, employeePart: rrqEmp, employerPart: rrqEmp, accumulation: rrqEmp * 11, retrieval: 0.0, origin: 'Automatique' },
          { id: Date.now() + 11, deductionId: 602, name: 'RQAP - Régime d\'assurance parentale', salaryEligible: grossPay, hoursEligible: totalHours, employeePart: rqapEmp, employerPart: rqapPat, accumulation: rqapEmp * 11, retrieval: 0.0, origin: 'Automatique' },
          { id: Date.now() + 12, deductionId: 603, name: 'IMP-FED - Impôt fédéral (ARC)', salaryEligible: grossPay, hoursEligible: totalHours, employeePart: fedTax, employerPart: 0.0, accumulation: fedTax * 11, retrieval: 0.0, origin: 'Automatique' },
          { id: Date.now() + 13, deductionId: 604, name: 'IMP-QC - Impôt provincial (Revenu Québec)', salaryEligible: grossPay, hoursEligible: totalHours, employeePart: qcTax, employerPart: 0.0, accumulation: qcTax * 11, retrieval: 0.0, origin: 'Automatique' },
        ];

    // Balanced GL accounting entries
    const employerCharges = rrqEmp + rqapPat;
    const fedPayable = fedTax + rqapEmp + rqapPat;
    const qcPayable = qcTax + rrqEmp * 2;
    const netPayable = Math.round((grossPay + employerCharges - fedPayable - qcPayable) * 100) / 100;

    const updatedAccounting = [
      { id: Date.now() + 30, region: '0100 - Région Est', account: '5110 - Salaires réguliers', activity: 'ACT-01 - 1000 Exploitation Standard', debit: grossPay, credit: 0.0, isCorrection: false },
      { id: Date.now() + 31, region: '0100 - Région Est', account: '5120 - Charges sociales patronales', activity: 'ACT-01 - 1000 Exploitation Standard', debit: employerCharges, credit: 0.0, isCorrection: false },
      { id: Date.now() + 32, region: '0100 - Région Est', account: '2120 - Retenues et charges à payer (Fédéral)', activity: 'Administration', debit: 0.0, credit: fedPayable, isCorrection: false },
      { id: Date.now() + 33, region: '0100 - Région Est', account: '2125 - Retenues et charges à payer (Québec)', activity: 'Administration', debit: 0.0, credit: qcPayable, isCorrection: false },
      { id: Date.now() + 34, region: '0100 - Région Est', account: '1010 - Banque de paie (Salaires nets)', activity: 'Trésorerie', debit: 0.0, credit: netPayable, isCorrection: false },
    ];

    const validatedSheet = {
      ...timesheet,
      status: 'Validated',
      grossPay,
      netPay,
      deductions: updatedDeductions,
      accountingEntries: updatedAccounting,
      warnings: totalHours > 80 ? ['Total des heures supérieur à 80h sur la période.'] : [],
      errors: [],
    };

    return this.saveTimeSheet(validatedSheet);
  },

  /**
   * Exports the timesheet to an Excel (.xlsx) file in the browser
   */
  exportToExcel(timesheet, employee, period) {
    if (!timesheet || !employee || !period) return;

    // Header info rows
    const rows = [
      ['SOLSTICE+ - FEUILLE DE TEMPS PAIE'],
      [],
      ['Numéro Employé', employee.value, 'Nom Employé', employee.name],
      ['Période', period.name, 'Date Début', period.startDate, 'Date Fin', period.endDate, 'Date Paie', period.payDate],
      ['Titre d\'Emploi', employee.jobTitle, 'Statut d\'Emploi', employee.jobType],
      ['Département', employee.department, 'Livret', employee.booklet],
      ['Convention', employee.convention, 'Région', employee.region],
      ['Numéro Feuille', timesheet.sheetNumber, 'Type Feuille', timesheet.sheetType, 'Statut', timesheet.status],
      ['Mode Paiement', timesheet.paymentType, 'Salaire Brut', timesheet.grossPay, 'Salaire Net', timesheet.netPay],
      [],
      ['DÉTAIL DES HEURES ET GAINS'],
      ['Sem.', 'Affectation / Poste', 'Rubrique / Gain', 'Horaire', 'Activité', 'Dim', 'Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam', 'Total Heures'],
    ];

    // Detail rows
    (timesheet.details || []).forEach((d) => {
      const gain = MOCK_LOOKUPS.gains.find((g) => g.id === d.gainId)?.name || d.gainId;
      const assign = MOCK_LOOKUPS.assignments.find((a) => a.id === d.assignmentId)?.name || d.assignmentId;
      const sched = MOCK_LOOKUPS.schedules.find((s) => s.id === d.scheduleId)?.name || d.scheduleId;
      const act = MOCK_LOOKUPS.activities.find((a) => a.id === d.activityId)?.name || d.activityId;
      const lineTotal = d.hours.reduce((acc, val) => acc + (Number(val) || 0), 0);

      rows.push([
        `S${d.week}`,
        assign,
        gain,
        sched,
        act,
        d.hours[0] || 0,
        d.hours[1] || 0,
        d.hours[2] || 0,
        d.hours[3] || 0,
        d.hours[4] || 0,
        d.hours[5] || 0,
        d.hours[6] || 0,
        lineTotal,
      ]);
    });

    // Deductions section
    if (timesheet.deductions && timesheet.deductions.length > 0) {
      rows.push([]);
      rows.push(['DÉDUCTIONS ET RETENUES À LA SOURCE']);
      rows.push(['Déduction / Retenue', 'Salaire Admissible', 'Heures Adm.', 'Part Employé ($)', 'Part Employeur ($)', 'Accumulation', 'Récupération', 'Origine']);
      timesheet.deductions.forEach((ded) => {
        const name = MOCK_LOOKUPS.deductions.find((lk) => lk.id === ded.deductionId)?.name || ded.name || ded.deductionId;
        rows.push([
          name,
          ded.salaryEligible,
          ded.hoursEligible,
          ded.employeePart,
          ded.employerPart,
          ded.accumulation,
          ded.retrieval,
          ded.origin,
        ]);
      });
    }

    // Taxable benefits section
    if (timesheet.taxableBenefits && timesheet.taxableBenefits.length > 0) {
      rows.push([]);
      rows.push(['AVANTAGES IMPOSABLES']);
      rows.push(['Description de l\'Avantage', 'Montant ($)', 'Période', 'Origine']);
      timesheet.taxableBenefits.forEach((ben) => {
        const name = MOCK_LOOKUPS.taxableBenefits.find((lk) => lk.id === ben.benefitId)?.name || ben.name || ben.benefitId;
        rows.push([
          name,
          ben.amount,
          ben.periodName || period.name,
          ben.origin || 'Système',
        ]);
      });
    }

    // Accounting entries section
    if (timesheet.accountingEntries && timesheet.accountingEntries.length > 0) {
      rows.push([]);
      rows.push(['ÉCRITURES COMPTABLES ET RÉPARTITION GRAND LIVRE (GL)']);
      rows.push(['Région / Territoire', 'Compte Comptable', 'Activité / Centre Coûts', 'Débit ($)', 'Crédit ($)', 'Correction']);
      timesheet.accountingEntries.forEach((ae) => {
        rows.push([
          ae.region || '0100',
          ae.account,
          ae.activity || '-',
          ae.debit || 0,
          ae.credit || 0,
          ae.isCorrection ? '*' : '',
        ]);
      });
    }

    // Accrual bank balances
    if (timesheet.creditsBank && timesheet.creditsBank.length > 0) {
      rows.push([]);
      rows.push(['SOLDES DE BANQUES ET CRÉDITS']);
      rows.push(['Code Banque', 'Nom de la Banque', 'Unité', 'Solde Initial', 'Variation Période', 'Nouveau Solde']);

      timesheet.creditsBank.forEach((cb) => {
        rows.push([cb.code, cb.name, cb.unit, cb.startBalance, cb.periodVariation, cb.newBalance]);
      });
    }

    const worksheet = XLSX.utils.aoa_to_sheet(rows);
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, 'Feuille_De_Temps');

    const fileName = `Feuille_Temps_${employee.value}_${period.name}.xlsx`;
    XLSX.writeFile(workbook, fileName);
  },
};
