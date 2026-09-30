require('dotenv').config();
const express = require('express');
const cors = require('cors');
const { query, testConnection, sql } = require('./db');

const app = express();
const PORT = process.env.PORT || 3001;

app.use(cors());
app.use(express.json());

// Request logger
app.use((req, res, next) => {
  const start = Date.now();
  res.on('finish', () => {
    const duration = Date.now() - start;
    console.log(`[${req.method}] ${req.originalUrl} - ${res.statusCode} (${duration}ms)`);
  });
  next();
});

// 1. Health check & DB connection status
app.get('/api/health', async (req, res) => {
  const dbStatus = await testConnection();
  res.json({
    status: dbStatus.success ? 'ok' : 'degraded',
    service: 'Solstice+ Timesheet API',
    database: dbStatus,
    timestamp: new Date().toISOString(),
  });
});

// 2. Pay Periods
app.get('/api/periods', async (req, res) => {
  try {
    const result = await query(`
      SELECT 
        CAST(P_Period_ID AS int) AS id,
        RTRIM(NAME) AS name,
        CAST(PERIODNO AS int) AS periodNo,
        CONVERT(varchar(10), STARTDATE, 120) AS startDate,
        CONVERT(varchar(10), ENDDATE, 120) AS endDate,
        CONVERT(varchar(10), PAYDATE, 120) AS payDate,
        CASE WHEN ISDEFAULT = 'Y' THEN 1 ELSE 0 END AS isDefault,
        RTRIM(PERIODSTATUS) AS status
      FROM P_Period
      WHERE ISACTIVE = 'Y'
      ORDER BY P_Period_ID DESC
    `);
    res.json(result.recordset);
  } catch (err) {
    console.error('Error in /api/periods:', err.message);
    res.status(500).json({ error: 'Erreur lors du chargement des périodes', details: err.message });
  }
});

// 3. Employees list with filters
app.get('/api/employees', async (req, res) => {
  try {
    const { status, search } = req.query;
    let sqlText = `
      SELECT TOP 200
        CAST(e.P_Employee_ID AS int) AS id,
        RTRIM(e.VALUE) AS value,
        RTRIM(e.NAME) AS name,
        RTRIM(ISNULL(e.FIRSTNAME, '')) AS firstName,
        RTRIM(ISNULL(e.SURNAME, '')) AS surname,
        CASE WHEN e.ISACTIVE = 'Y' THEN 1 ELSE 0 END AS isActive,
        CASE WHEN e.PAYMENTTYPE = 'C' THEN 'Chèque' ELSE 'Dépôt direct' END AS paymentType,
        RTRIM(ISNULL(d.NAME, 'Direction Générale')) AS department,
        RTRIM(ISNULL(jt.NAME, 'Employé standard')) AS jobTitle,
        RTRIM(ISNULL(jtype.NAME, 'Temps plein')) AS jobType,
        'Livret Régulier' AS booklet,
        'Solstice Corporation - Division Est' AS orgName,
        'Québec (QC)' AS region
      FROM P_Employee e
      LEFT JOIN P_Department d ON e.P_Department_ID = d.P_Department_ID
      LEFT JOIN P_Job_Title jt ON e.P_Job_Title_ID = jt.P_Job_Title_ID
      LEFT JOIN P_Job_Type jtype ON e.P_Job_Type_ID = jtype.P_Job_Type_ID
      WHERE 1=1
    `;

    const params = {};
    if (status === 'active') {
      sqlText += ` AND e.ISACTIVE = 'Y'`;
    } else if (status === 'inactive') {
      sqlText += ` AND e.ISACTIVE = 'N'`;
    }

    if (search && search.trim()) {
      sqlText += ` AND (e.VALUE LIKE @search OR e.NAME LIKE @search OR e.FIRSTNAME LIKE @search OR e.SURNAME LIKE @search)`;
      params.search = `%${search.trim()}%`;
    }

    sqlText += ` ORDER BY e.NAME ASC`;

    const result = await query(sqlText, params);
    res.json(result.recordset);
  } catch (err) {
    console.error('Error in /api/employees:', err.message);
    res.status(500).json({ error: 'Erreur lors du chargement des employés', details: err.message });
  }
});

// 4. Lookups dictionaries
app.get('/api/lookups', async (req, res) => {
  try {
    const [gainsRes, assignRes, schedRes, actRes, dedRes, benRes] = await Promise.all([
      query(`SELECT CAST(P_Gain_ID AS int) AS id, RTRIM(VALUE) AS code, RTRIM(NAME) AS name, ISNULL(GAINUNIT, 'H') AS unit, CASE WHEN GAINUNIT = 'M' THEN 1 ELSE 0 END AS isMonetary FROM P_Gain WHERE ISACTIVE = 'Y' ORDER BY VALUE`),
      query(`SELECT CAST(P_Assignment_ID AS int) AS id, RTRIM(VALUE) AS code, RTRIM(NAME) AS name FROM P_Assignment WHERE ISACTIVE = 'Y' ORDER BY VALUE`),
      query(`SELECT CAST(P_Schedule_ID AS int) AS id, CAST(P_Schedule_ID AS varchar) AS code, RTRIM(NAME) AS name FROM P_Schedule WHERE ISACTIVE = 'Y' ORDER BY NAME`),
      query(`SELECT CAST(C_Activity_ID AS int) AS id, RTRIM(VALUE) AS code, RTRIM(NAME) AS name FROM C_Activity WHERE ISACTIVE = 'Y' ORDER BY VALUE`),
      query(`SELECT CAST(P_Deduction_ID AS int) AS id, RTRIM(VALUE) AS code, RTRIM(NAME) AS name FROM P_Deduction WHERE ISACTIVE = 'Y' ORDER BY VALUE`),
      query(`SELECT CAST(P_Taxable_Benefit_ID AS int) AS id, RTRIM(VALUE) AS code, RTRIM(NAME) AS name FROM P_Taxable_Benefit WHERE ISACTIVE = 'Y' ORDER BY VALUE`),
    ]);

    const lookups = {
      gains: gainsRes.recordset,
      assignments: assignRes.recordset,
      schedules: schedRes.recordset,
      activities: actRes.recordset,
      deductions: dedRes.recordset,
      taxableBenefits: benRes.recordset,
      paymentTypes: [
        { id: 'Deposit', name: 'Dépôt direct' },
        { id: 'Cheque', name: 'Chèque imprimé' },
      ],
      sheetTypes: [
        { id: 'Regular', name: 'Régulière' },
        { id: 'Complementary', name: 'Complémentaire' },
        { id: 'Adjustment', name: 'Ajustement' },
        { id: 'Advance', name: 'Avance' },
        { id: 'ExpenseAccount', name: 'Compte de dépenses' },
        { id: 'SeparationPay', name: 'Indemnité départ' },
      ],
      timesheetStatuses: [
        { id: 'Initial', name: 'Initiale', color: 'initial' },
        { id: 'Calculated', name: 'Calculée', color: 'calculated' },
        { id: 'Approved', name: 'Approuvée', color: 'approved' },
        { id: 'Validated', name: 'Validée', color: 'validated' },
        { id: 'Issued', name: 'Émise', color: 'issued' },
        { id: 'Error', name: 'En erreur', color: 'error' },
      ],
    };

    res.json(lookups);
  } catch (err) {
    console.error('Error in /api/lookups:', err.message);
    res.status(500).json({ error: 'Erreur lors du chargement des dictionnaires', details: err.message });
  }
});

// Helper to map DB status code to UI label
function mapDbStatus(code) {
  if (!code) return 'Initial';
  const c = String(code).trim().toUpperCase();
  if (c === 'I') return 'Initial';
  if (c === 'C') return 'Calculated';
  if (c === 'A') return 'Approved';
  if (c === 'V') return 'Validated';
  if (c === 'E') return 'Issued';
  return 'Initial';
}

function mapUiStatusToDb(status) {
  if (!status) return 'I';
  const s = String(status).trim().toLowerCase();
  if (s === 'initial' || s === 'i') return 'I';
  if (s === 'calculated' || s === 'c') return 'C';
  if (s === 'approved' || s === 'a') return 'A';
  if (s === 'validated' || s === 'v') return 'V';
  if (s === 'issued' || s === 'e') return 'E';
  return 'I';
}

// 5. Get TimeSheet for an employee and period
app.get('/api/timesheets/:employeeId/:periodId', async (req, res) => {
  const { employeeId, periodId } = req.params;

  try {
    // 1. Fetch header
    const sheetRes = await query(`
      SELECT TOP 1
        CAST(ts.P_Time_Sheet_ID AS int) AS id,
        CAST(ts.P_Employee_ID AS int) AS employeeId,
        CAST(ts.P_Period_ID AS int) AS periodId,
        RTRIM(ISNULL(ts.VALUE, '')) AS sheetNumber,
        RTRIM(ISNULL(ts.SHEETTYPE, 'Regular')) AS sheetType,
        RTRIM(ISNULL(ts.TIMESHEETSTATUS, 'I')) AS dbStatus,
        CASE WHEN ts.PAYMENTTYPE = 'C' THEN 'Cheque' ELSE 'Deposit' END AS paymentType,
        CASE WHEN ts.ISERROR = 'Y' THEN 1 ELSE 0 END AS isError,
        CASE WHEN ts.ISWARNING = 'Y' THEN 1 ELSE 0 END AS isWarning
      FROM P_Time_Sheet ts
      WHERE ts.P_Employee_ID = @empId AND ts.P_Period_ID = @periodId
    `, { empId: Number(employeeId), periodId: Number(periodId) });

    let sheet = sheetRes.recordset[0];

    // If no sheet exists in DB, construct a fresh initial structure
    if (!sheet) {
      const periodRes = await query(`SELECT RTRIM(NAME) AS name, CONVERT(varchar(10), STARTDATE, 120) AS startDate FROM P_Period WHERE P_Period_ID = @periodId`, { periodId: Number(periodId) });
      const empRes = await query(`SELECT RTRIM(VALUE) AS value, CASE WHEN PAYMENTTYPE = 'C' THEN 'Cheque' ELSE 'Deposit' END AS paymentType FROM P_Employee WHERE P_Employee_ID = @empId`, { empId: Number(employeeId) });

      const periodName = periodRes.recordset[0]?.name || '2026-P19';
      const empCode = empRes.recordset[0]?.value || '0000';
      const paymentType = empRes.recordset[0]?.paymentType || 'Deposit';

      sheet = {
        id: 0,
        employeeId: Number(employeeId),
        periodId: Number(periodId),
        sheetNumber: `${periodName}-${empCode}`,
        sheetType: 'Regular',
        status: 'Initial',
        paymentType,
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
            id: 1,
            week: 1,
            assignmentId: 1153884,
            gainId: 1003531,
            scheduleId: 1000000,
            activityId: 1000000,
            originTime: 'Saisie manuelle',
            hours: [0, 8.0, 8.0, 8.0, 8.0, 8.0, 0],
            quantity: 40.0,
          },
          {
            id: 2,
            week: 2,
            assignmentId: 1153884,
            gainId: 1003531,
            scheduleId: 1000000,
            activityId: 1000000,
            originTime: 'Saisie manuelle',
            hours: [0, 8.0, 8.0, 8.0, 8.0, 8.0, 0],
            quantity: 40.0,
          },
        ],
        primes: [],
        creditMovements: [],
        deductions: [],
        taxableBenefits: [],
        accountingEntries: [],
      };

      return res.json(sheet);
    }

    // 2. Fetch Detail Lines
    const detailsRes = await query(`
      SELECT 
        CAST(d.P_Time_Sheet_Detail_ID AS int) AS id,
        ISNULL(d.WEEKINDEX, 1) AS week,
        CAST(d.P_Assignment_ID AS int) AS assignmentId,
        CAST(d.P_Gain_ID AS int) AS gainId,
        CAST(d.P_Schedule_ID AS int) AS scheduleId,
        CAST(d.C_Activity_ID AS int) AS activityId,
        RTRIM(ISNULL(d.ORIGINTIME, 'Saisie manuelle')) AS originTime,
        DATEPART(dw, d.DAY) AS dayOfWeek,
        CAST(d.DAYQTY AS float) AS dayQty
      FROM P_Time_Sheet_Detail d
      WHERE d.P_Time_Sheet_ID = @sheetId
      ORDER BY d.WEEKINDEX, d.P_Assignment_ID, d.P_Gain_ID, d.DAY
    `, { sheetId: sheet.id });

    // Group rows into 7-day vector matrix
    const lineMap = new Map();
    detailsRes.recordset.forEach((row) => {
      const key = `${row.week}_${row.assignmentId}_${row.gainId}_${row.scheduleId}_${row.activityId}`;
      if (!lineMap.has(key)) {
        lineMap.set(key, {
          id: row.id,
          week: row.week,
          assignmentId: row.assignmentId,
          gainId: row.gainId,
          scheduleId: row.scheduleId,
          activityId: row.activityId,
          originTime: row.originTime,
          hours: [0, 0, 0, 0, 0, 0, 0],
          quantity: 0,
        });
      }

      const line = lineMap.get(key);
      const dayIdx = (row.dayOfWeek - 1 + 7) % 7;
      line.hours[dayIdx] = Number(row.dayQty) || 0;
      line.quantity += Number(row.dayQty) || 0;
    });

    const details = Array.from(lineMap.values());

    // 3. Fetch Deductions
    const dedRes = await query(`
      SELECT 
        CAST(pd.P_Payment_Deduction_ID AS int) AS id,
        CAST(pd.P_Deduction_ID AS int) AS deductionId,
        RTRIM(d.NAME) AS name,
        CAST(pd.SALARY_ELIGIBLE AS float) AS salaryEligible,
        CAST(pd.HOURS_ELIGIBLE AS float) AS hoursEligible,
        CAST(pd.EMPLOYEE_PART AS float) AS employeePart,
        CAST(pd.EMPLOYER_PART AS float) AS employerPart,
        CAST(ISNULL(pd.ACCUMULATIONAMOUNT, 0) AS float) AS accumulation,
        CAST(ISNULL(pd.RETRIEVALAMOUNT, 0) AS float) AS retrieval,
        RTRIM(ISNULL(pd.origine, 'Automatique')) AS origin
      FROM P_Payment_Deduction pd
      JOIN P_Deduction d ON pd.P_Deduction_ID = d.P_Deduction_ID
      WHERE pd.P_Employee_ID = @empId AND pd.P_Period_ID = @periodId
    `, { empId: Number(employeeId), periodId: Number(periodId) });

    // 4. Fetch Taxable Benefits
    const benRes = await query(`
      SELECT 
        CAST(ptb.P_Payment_Taxable_Benefit_ID AS int) AS id,
        CAST(ptb.P_Taxable_Benefit_ID AS int) AS benefitId,
        RTRIM(tb.NAME) AS name,
        CAST(ptb.TAXABLE_BENEFIT_AMOUNT AS float) AS amount,
        '2026-P19' AS periodName,
        RTRIM(ISNULL(ptb.origine, 'Système')) AS origin
      FROM P_Payment_Taxable_Benefit ptb
      JOIN P_Taxable_Benefit tb ON ptb.P_Taxable_Benefit_ID = tb.P_Taxable_Benefit_ID
      WHERE ptb.P_Employee_ID = @empId AND ptb.P_Period_ID = @periodId
    `, { empId: Number(employeeId), periodId: Number(periodId) });

    // 5. Fetch Credits Banks
    const creditsRes = await query(`
      SELECT 
        RTRIM(c.VALUE) AS id,
        RTRIM(c.VALUE) AS code,
        RTRIM(c.NAME) AS name,
        'H' AS unit,
        80.0 AS startBalance,
        0.0 AS periodVariation,
        80.0 AS newBalance
      FROM P_Credits c
      WHERE c.ISACTIVE = 'Y' AND c.IsDisplayedCredits = 'Y'
    `);

    // Compute gross & net pay estimate
    let totalHours = 0;
    details.forEach(d => {
      totalHours += d.hours.reduce((acc, h) => acc + (Number(h) || 0), 0);
    });
    const grossPay = Math.round(totalHours * 32.50 * 100) / 100;
    const netPay = Math.round(grossPay * 0.70 * 100) / 100;

    const fullSheet = {
      id: sheet.id,
      employeeId: Number(employeeId),
      periodId: Number(periodId),
      sheetNumber: sheet.sheetNumber,
      sheetType: sheet.sheetType,
      status: mapDbStatus(sheet.dbStatus),
      paymentType: sheet.paymentType,
      grossPay,
      netPay,
      hasChat: false,
      hasAttachment: false,
      warnings: sheet.isWarning ? ['Feuille marquée avec avertissement.'] : [],
      errors: sheet.isError ? ['Feuille en erreur de validation.'] : [],
      creditsBank: creditsRes.recordset.length > 0 ? creditsRes.recordset : [
        { id: 'BV20B', code: 'BV20B', name: 'Banque Vacances', unit: 'H', startBalance: 80.0, periodVariation: 0.0, newBalance: 80.0 },
        { id: 'BV21', code: 'BV21', name: 'Congés Fériés', unit: 'H', startBalance: 16.0, periodVariation: 0.0, newBalance: 16.0 },
      ],
      details: details.length > 0 ? details : [
        {
          id: 1,
          week: 1,
          assignmentId: 1153884,
          gainId: 1003531,
          scheduleId: 1000000,
          activityId: 1000000,
          originTime: 'Saisie manuelle',
          hours: [0, 8.0, 8.0, 8.0, 8.0, 8.0, 0],
          quantity: 40.0,
        },
      ],
      primes: [],
      creditMovements: [],
      deductions: dedRes.recordset,
      taxableBenefits: benRes.recordset,
      accountingEntries: [
        { id: 1, region: '0100 - Région Est', account: '5110 - Salaires réguliers', activity: 'Exploitation', debit: grossPay, credit: 0.0, isCorrection: false },
        { id: 2, region: '0100 - Région Est', account: '1010 - Banque de paie (Salaires nets)', activity: 'Trésorerie', debit: 0.0, credit: netPay, isCorrection: false },
      ],
    };

    res.json(fullSheet);
  } catch (err) {
    console.error(`Error in /api/timesheets/${employeeId}/${periodId}:`, err.message);
    res.status(500).json({ error: 'Erreur lors du chargement de la feuille de temps', details: err.message });
  }
});

// 6. Save or Update TimeSheet
app.post('/api/timesheets', async (req, res) => {
  const sheet = req.body;
  if (!sheet || !sheet.employeeId || !sheet.periodId) {
    return res.status(400).json({ error: 'Données de feuille de temps invalides' });
  }

  try {
    const dbStatus = mapUiStatusToDb(sheet.status);

    // Check if sheet exists
    const existing = await query(`
      SELECT P_Time_Sheet_ID FROM P_Time_Sheet 
      WHERE P_Employee_ID = @empId AND P_Period_ID = @periodId
    `, { empId: sheet.employeeId, periodId: sheet.periodId });

    let sheetId = existing.recordset[0]?.P_Time_Sheet_ID;

    if (!sheetId) {
      // Insert new header
      const insertRes = await query(`
        INSERT INTO P_Time_Sheet (
          AD_CLIENT_ID, AD_ORG_ID, ISACTIVE, CREATED, CREATEDBY, UPDATED, UPDATEDBY,
          VALUE, P_EMPLOYEE_ID, P_PERIOD_ID, SHEETTYPE, PAYMENTTYPE, TIMESHEETSTATUS
        ) VALUES (
          1000000, 1000000, 'Y', GETDATE(), 100, GETDATE(), 100,
          @sheetNumber, @empId, @periodId, @sheetType, @paymentType, @dbStatus
        );
        SELECT SCOPE_IDENTITY() AS newId;
      `, {
        sheetNumber: sheet.sheetNumber || 'NEW',
        empId: sheet.employeeId,
        periodId: sheet.periodId,
        sheetType: sheet.sheetType || 'Regular',
        paymentType: sheet.paymentType === 'Cheque' ? 'C' : 'D',
        dbStatus,
      });
      sheetId = insertRes.recordset[0]?.newId;
    } else {
      // Update header
      await query(`
        UPDATE P_Time_Sheet SET
          UPDATED = GETDATE(),
          SHEETTYPE = @sheetType,
          PAYMENTTYPE = @paymentType,
          TIMESHEETSTATUS = @dbStatus
        WHERE P_Time_Sheet_ID = @sheetId
      `, {
        sheetType: sheet.sheetType || 'Regular',
        paymentType: sheet.paymentType === 'Cheque' ? 'C' : 'D',
        dbStatus,
        sheetId,
      });

      // Clear existing details
      await query(`DELETE FROM P_Time_Sheet_Detail WHERE P_Time_Sheet_ID = @sheetId`, { sheetId });
    }

    // Insert new detail lines
    if (sheet.details && sheet.details.length > 0) {
      for (const line of sheet.details) {
        for (let dayIdx = 0; dayIdx < 7; dayIdx++) {
          const qty = Number(line.hours[dayIdx]) || 0;
          if (qty > 0) {
            await query(`
              INSERT INTO P_Time_Sheet_Detail (
                AD_CLIENT_ID, AD_ORG_ID, ISACTIVE, CREATED, CREATEDBY, UPDATED, UPDATEDBY,
                P_TIME_SHEET_ID, P_ASSIGNMENT_ID, P_GAIN_ID, P_SCHEDULE_ID, C_ACTIVITY_ID,
                WEEKINDEX, DAY, DAYQTY, ORIGINTIME
              ) VALUES (
                1000000, 1000000, 'Y', GETDATE(), 100, GETDATE(), 100,
                @sheetId, @assignmentId, @gainId, @scheduleId, @activityId,
                @weekIndex, DATEADD(day, @dayOffset, GETDATE()), @qty, @originTime
              )
            `, {
              sheetId,
              assignmentId: line.assignmentId || 1153884,
              gainId: line.gainId || 1003531,
              scheduleId: line.scheduleId || 1000000,
              activityId: line.activityId || 1000000,
              weekIndex: line.week || 1,
              dayOffset: dayIdx + (line.week === 2 ? 7 : 0),
              qty,
              originTime: line.originTime || 'Saisie manuelle',
            });
          }
        }
      }
    }

    res.json({ success: true, id: sheetId, message: 'Feuille de temps enregistrée avec succès' });
  } catch (err) {
    console.error('Error saving timesheet:', err.message);
    res.status(500).json({ error: "Erreur lors de l'enregistrement", details: err.message });
  }
});

// 7. Delete a timesheet
app.delete('/api/timesheets/:employeeId/:periodId', async (req, res) => {
  const { employeeId, periodId } = req.params;
  try {
    const existing = await query(`
      SELECT P_Time_Sheet_ID, TIMESHEETSTATUS 
      FROM P_Time_Sheet 
      WHERE P_Employee_ID = @empId AND P_Period_ID = @periodId
    `, { empId: Number(employeeId), periodId: Number(periodId) });

    const sheet = existing.recordset[0];
    if (!sheet) {
      return res.status(404).json({ error: 'Feuille de temps introuvable' });
    }

    if (sheet.TIMESHEETSTATUS && sheet.TIMESHEETSTATUS.trim().toUpperCase() !== 'I') {
      return res.status(403).json({ error: 'Seules les feuilles de temps au statut "Initiale" peuvent être supprimées' });
    }

    await query(`DELETE FROM P_Time_Sheet_Detail WHERE P_Time_Sheet_ID = @sheetId`, { sheetId: sheet.P_Time_Sheet_ID });
    await query(`DELETE FROM P_Time_Sheet WHERE P_Time_Sheet_ID = @sheetId`, { sheetId: sheet.P_Time_Sheet_ID });

    res.json({ success: true, message: 'Feuille de temps supprimée' });
  } catch (err) {
    console.error('Error deleting timesheet:', err.message);
    res.status(500).json({ error: 'Erreur lors de la suppression', details: err.message });
  }
});

// 8. Validate timesheet
app.post('/api/timesheets/validate', async (req, res) => {
  const sheet = req.body;
  try {
    await query(`
      UPDATE P_Time_Sheet SET
        TIMESHEETSTATUS = 'V',
        UPDATED = GETDATE()
      WHERE P_Employee_ID = @empId AND P_Period_ID = @periodId
    `, { empId: sheet.employeeId, periodId: sheet.periodId });

    res.json({
      ...sheet,
      status: 'Validated',
      message: 'Feuille de temps validée avec succès.',
    });
  } catch (err) {
    console.error('Error validating timesheet:', err.message);
    res.status(500).json({ error: 'Erreur lors de la validation', details: err.message });
  }
});

// Start Server
app.listen(PORT, () => {
  console.log(`=======================================================`);
  console.log(`  Solstice+ Timesheet Backend API démarré sur :`);
  console.log(`  -> URL locale : http://localhost:${PORT}/api/health`);
  console.log(`=======================================================`);
});
