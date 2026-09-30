import React, { useState, useEffect, useCallback, useMemo } from 'react';
import HeaderToolbar from './components/HeaderToolbar/HeaderToolbar';
import EmployeeList from './components/EmployeeList/EmployeeList';
import TimeSheetHeader from './components/TimeSheetHeader/TimeSheetHeader';
import CreditsBank from './components/CreditsBank/CreditsBank';
import TimeSheetGrid from './components/TimeSheetGrid/TimeSheetGrid';
import SearchModal from './components/SearchModal/SearchModal';
import { timesheetService } from './services/timesheetService';
import './index.css';

export default function App() {
  const [theme, setTheme] = useState('dark');
  const [employees, setEmployees] = useState([]);
  const [periods, setPeriods] = useState([]);
  const [lookups, setLookups] = useState({});
  const [selectedEmployeeId, setSelectedEmployeeId] = useState(null);
  const [selectedPeriodId, setSelectedPeriodId] = useState(null);
  const [timesheet, setTimesheet] = useState(null);
  const [loading, setLoading] = useState(true);
  const [isDirty, setIsDirty] = useState(false);
  const [isSearchOpen, setIsSearchOpen] = useState(false);

  // Filters
  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState('active');
  const [sheetFilter, setSheetFilter] = useState('ALL');

  // Notification Toast
  const [toastMessage, setToastMessage] = useState(null);

  const showToast = (msg) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(null), 3500);
  };

  // Check if timesheet is editable (only when status is Initial or 'I')
  const isEditable = useMemo(() => {
    if (!timesheet) return false;
    const s = String(timesheet.status || '').toLowerCase();
    return s === 'initial' || s === 'i';
  }, [timesheet]);

  // Toggle Theme
  const handleToggleTheme = () => {
    const next = theme === 'dark' ? 'light' : 'dark';
    setTheme(next);
    document.documentElement.setAttribute('data-theme', next);
  };

  // Initial Load: Employees, Periods, Lookups
  useEffect(() => {
    async function init() {
      try {
        setLoading(true);
        const [empList, periodList, lkps] = await Promise.all([
          timesheetService.getEmployees(),
          timesheetService.getPayPeriods(),
          timesheetService.getLookups(),
        ]);

        setEmployees(empList);
        setPeriods(periodList);
        setLookups(lkps);

        if (periodList.length > 0) {
          const defaultPeriod = periodList.find((p) => p.isDefault) || periodList[0];
          setSelectedPeriodId(defaultPeriod.id);
        }

        if (empList.length > 0) {
          setSelectedEmployeeId(empList[0].id);
        }
      } catch (err) {
        console.error('Error during initial load', err);
      } finally {
        setLoading(false);
      }
    }

    init();
  }, []);

  // Filtered employees list
  const filteredEmployees = useMemo(() => {
    return employees.filter((emp) => {
      // Active / Inactive
      if (statusFilter === 'active' && !emp.isActive) return false;
      if (statusFilter === 'inactive' && emp.isActive) return false;

      // Text search
      if (searchQuery.trim()) {
        const q = searchQuery.toLowerCase();
        const matchCode = emp.value.toLowerCase().includes(q);
        const matchName = emp.name.toLowerCase().includes(q);
        const matchDept = emp.department.toLowerCase().includes(q);
        if (!matchCode && !matchName && !matchDept) return false;
      }

      return true;
    });
  }, [employees, statusFilter, searchQuery]);

  // Load timesheet when selectedEmployeeId or selectedPeriodId changes
  useEffect(() => {
    if (!selectedEmployeeId || !selectedPeriodId) return;

    let isSubscribed = true;
    async function loadSheet() {
      try {
        const sheet = await timesheetService.getTimeSheet(selectedEmployeeId, selectedPeriodId);
        if (isSubscribed) {
          setTimesheet(sheet);
          setIsDirty(false);
        }
      } catch (err) {
        console.error('Error loading timesheet', err);
      }
    }

    loadSheet();
    return () => {
      isSubscribed = false;
    };
  }, [selectedEmployeeId, selectedPeriodId]);

  // Current Employee Object
  const currentEmployee = useMemo(() => {
    return employees.find((e) => e.id === selectedEmployeeId);
  }, [employees, selectedEmployeeId]);

  // Current Period Object
  const currentPeriod = useMemo(() => {
    return periods.find((p) => p.id === selectedPeriodId);
  }, [periods, selectedPeriodId]);

  // Current Index in filtered employees
  const currentEmpIndex = useMemo(() => {
    return filteredEmployees.findIndex((e) => e.id === selectedEmployeeId);
  }, [filteredEmployees, selectedEmployeeId]);

  // Navigation Handlers
  const handleMoveFirst = useCallback(() => {
    if (filteredEmployees.length > 0) {
      setSelectedEmployeeId(filteredEmployees[0].id);
    }
  }, [filteredEmployees]);

  const handleMovePrev = useCallback(() => {
    if (currentEmpIndex > 0) {
      setSelectedEmployeeId(filteredEmployees[currentEmpIndex - 1].id);
    }
  }, [filteredEmployees, currentEmpIndex]);

  const handleMoveNext = useCallback(() => {
    if (currentEmpIndex >= 0 && currentEmpIndex < filteredEmployees.length - 1) {
      setSelectedEmployeeId(filteredEmployees[currentEmpIndex + 1].id);
    }
  }, [filteredEmployees, currentEmpIndex]);

  const handleMoveLast = useCallback(() => {
    if (filteredEmployees.length > 0) {
      setSelectedEmployeeId(filteredEmployees[filteredEmployees.length - 1].id);
    }
  }, [filteredEmployees]);

  // Keyboard Shortcuts (Arrow navigation & Ctrl+S)
  useEffect(() => {
    function handleKeyDown(e) {
      // Ignore if inside an input or textarea
      if (['INPUT', 'SELECT', 'TEXTAREA'].includes(e.target.tagName)) return;

      if (e.key === 'ArrowUp') {
        e.preventDefault();
        handleMovePrev();
      } else if (e.key === 'ArrowDown') {
        e.preventDefault();
        handleMoveNext();
      } else if ((e.ctrlKey || e.metaKey) && e.key === 's') {
        e.preventDefault();
        if (isEditable && isDirty) {
          handleSave();
        }
      }
    }

    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [handleMovePrev, handleMoveNext, isEditable, isDirty]);

  // Save Handler
  const handleSave = async () => {
    if (!timesheet || !isEditable) return;
    try {
      const saved = await timesheetService.saveTimeSheet(timesheet);
      setTimesheet(saved);
      setIsDirty(false);
      showToast('✓ Feuille de temps enregistrée avec succès.');
    } catch (err) {
      showToast('❌ Erreur lors de l\'enregistrement.');
    }
  };

  // Validate / Calculate Handler
  const handleValidate = async () => {
    if (!timesheet) return;
    try {
      const validated = await timesheetService.validateTimeSheet(timesheet);
      setTimesheet(validated);
      setIsDirty(false);
      showToast(`✓ Feuille validée et calculée (Brut: ${validated.grossPay.toFixed(2)} $).`);
    } catch (err) {
      showToast('❌ Erreur lors de la validation.');
    }
  };

  // New Timesheet
  const handleNew = () => {
    if (isDirty && !window.confirm('Des modifications non enregistrées existent. Voulez-vous continuer ?')) {
      return;
    }
    const newSheet = {
      id: Date.now(),
      employeeId: selectedEmployeeId,
      periodId: selectedPeriodId,
      sheetNumber: `${currentPeriod?.name || '2026'}-${currentEmployee?.value.replace('EMP-', '')}-NEW`,
      sheetType: 'Regular',
      status: 'Initial',
      paymentType: currentEmployee?.paymentType || 'Deposit',
      grossPay: 0,
      netPay: 0,
      warnings: [],
      errors: [],
      creditsBank: timesheet?.creditsBank || [],
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
        { id: Date.now() + 20, benefitId: 701, name: 'BEN-VIE - Assurance-vie collective', amount: 15.0, periodName: currentPeriod?.name || '2026-P19', origin: 'Système' },
        { id: Date.now() + 21, benefitId: 703, name: 'BEN-SANTE - Contribution patronale assurance santé (QC)', amount: 95.0, periodName: currentPeriod?.name || '2026-P19', origin: 'Système' },
      ],
      accountingEntries: [
        { id: Date.now() + 30, region: '0100 - Région Est', account: '5110 - Salaires réguliers', activity: 'ACT-01 - 1000 Exploitation Standard', debit: 2600.0, credit: 0.0, isCorrection: false },
        { id: Date.now() + 31, region: '0100 - Région Est', account: '5120 - Charges sociales patronales', activity: 'ACT-01 - 1000 Exploitation Standard', debit: 253.38, credit: 0.0, isCorrection: false },
        { id: Date.now() + 32, region: '0100 - Région Est', account: '2120 - Retenues et charges à payer (Fédéral)', activity: 'Administration', debit: 0.0, credit: 471.24, isCorrection: false },
        { id: Date.now() + 33, region: '0100 - Région Est', account: '2125 - Retenues et charges à payer (Québec)', activity: 'Administration', debit: 0.0, credit: 521.98, isCorrection: false },
        { id: Date.now() + 34, region: '0100 - Région Est', account: '1010 - Banque de paie (Salaires nets)', activity: 'Trésorerie', debit: 0.0, credit: 1860.16, isCorrection: false },
      ],
    };
    setTimesheet(newSheet);
    setIsDirty(true);
    showToast('Nouvelle feuille de temps initialisée (Statut: Initiale).');
  };

  // Delete Timesheet
  const handleDelete = async () => {
    if (!timesheet) return;
    if (!isEditable) {
      alert('Seules les feuilles de temps au statut "Initiale" (I) peuvent être supprimées.');
      return;
    }
    if (window.confirm('Êtes-vous sûr de vouloir supprimer cette feuille de temps ?')) {
      await timesheetService.deleteTimeSheet(selectedEmployeeId, selectedPeriodId);
      const reloaded = await timesheetService.getTimeSheet(selectedEmployeeId, selectedPeriodId);
      setTimesheet(reloaded);
      setIsDirty(false);
      showToast('Feuille de temps supprimée.');
    }
  };

  // Export to Excel
  const handleExport = () => {
    if (!timesheet || !currentEmployee || !currentPeriod) return;
    timesheetService.exportToExcel(timesheet, currentEmployee, currentPeriod);
    showToast('Export Excel généré avec succès.');
  };

  // Detail Lines Update Handlers - guarded by isEditable
  const handleUpdateDetailLine = (lineId, updates) => {
    if (!isEditable) return;
    setTimesheet((prev) => {
      const nextDetails = prev.details.map((d) => (d.id === lineId ? { ...d, ...updates } : d));
      return { ...prev, details: nextDetails };
    });
    setIsDirty(true);
  };

  const handleAddDetailLine = () => {
    if (!isEditable) return;
    setTimesheet((prev) => {
      const newLine = {
        id: Date.now(),
        week: 1,
        assignmentId: lookups.assignments?.[0]?.id || 301,
        gainId: lookups.gains?.[0]?.id || 101,
        scheduleId: lookups.schedules?.[0]?.id || 401,
        activityId: lookups.activities?.[0]?.id || 501,
        originTime: 'Saisie manuelle',
        hours: [0, 0, 0, 0, 0, 0, 0],
        quantity: 0,
      };
      return { ...prev, details: [...prev.details, newLine] };
    });
    setIsDirty(true);
  };

  const handleDeleteDetailLine = (lineId) => {
    if (!isEditable) return;
    setTimesheet((prev) => ({
      ...prev,
      details: prev.details.filter((d) => d.id !== lineId),
    }));
    setIsDirty(true);
  };

  const handleDuplicateDetailLine = (lineId) => {
    if (!isEditable) return;
    setTimesheet((prev) => {
      const target = prev.details.find((d) => d.id === lineId);
      if (!target) return prev;
      const clone = {
        ...target,
        id: Date.now(),
      };
      return { ...prev, details: [...prev.details, clone] };
    });
    setIsDirty(true);
  };

  // Deductions Handlers - guarded by isEditable
  const handleUpdateDeduction = (id, updates) => {
    if (!isEditable) return;
    setTimesheet((prev) => ({
      ...prev,
      deductions: (prev.deductions || []).map((d) => (d.id === id ? { ...d, ...updates } : d)),
    }));
    setIsDirty(true);
  };

  const handleAddDeduction = () => {
    if (!isEditable) return;
    setTimesheet((prev) => {
      const firstLookup = lookups.deductions?.[0];
      const newDeduction = {
        id: Date.now(),
        deductionId: firstLookup?.id || 601,
        name: firstLookup?.name || 'Nouvelle déduction',
        salaryEligible: prev.grossPay || 0,
        hoursEligible: 80.0,
        employeePart: 0,
        employerPart: 0,
        accumulation: 0,
        retrieval: 0,
        origin: 'Manuel',
      };
      return {
        ...prev,
        deductions: [...(prev.deductions || []), newDeduction],
      };
    });
    setIsDirty(true);
  };

  const handleDeleteDeduction = (id) => {
    if (!isEditable) return;
    setTimesheet((prev) => ({
      ...prev,
      deductions: (prev.deductions || []).filter((d) => d.id !== id),
    }));
    setIsDirty(true);
  };

  // Taxable Benefits Handlers - guarded by isEditable
  const handleUpdateBenefit = (id, updates) => {
    if (!isEditable) return;
    setTimesheet((prev) => ({
      ...prev,
      taxableBenefits: (prev.taxableBenefits || []).map((b) => (b.id === id ? { ...b, ...updates } : b)),
    }));
    setIsDirty(true);
  };

  const handleAddBenefit = () => {
    if (!isEditable) return;
    setTimesheet((prev) => {
      const firstLookup = lookups.taxableBenefits?.[0];
      const newBenefit = {
        id: Date.now(),
        benefitId: firstLookup?.id || 701,
        name: firstLookup?.name || 'Nouvel avantage',
        amount: 0,
        periodName: currentPeriod?.name || '2026-P19',
        origin: 'Manuel',
      };
      return {
        ...prev,
        taxableBenefits: [...(prev.taxableBenefits || []), newBenefit],
      };
    });
    setIsDirty(true);
  };

  const handleDeleteBenefit = (id) => {
    if (!isEditable) return;
    setTimesheet((prev) => ({
      ...prev,
      taxableBenefits: (prev.taxableBenefits || []).filter((b) => b.id !== id),
    }));
    setIsDirty(true);
  };

  return (
    <div className="app-container">
      {/* Top Toolbar */}
      <HeaderToolbar
        onSave={handleSave}
        onNew={handleNew}
        onDelete={handleDelete}
        onSearch={() => setIsSearchOpen(true)}
        onValidate={handleValidate}
        onExport={handleExport}
        onMoveFirst={handleMoveFirst}
        onMovePrev={handleMovePrev}
        onMoveNext={handleMoveNext}
        onMoveLast={handleMoveLast}
        isDirty={isDirty}
        isEditable={isEditable}
        status={timesheet?.status}
        hasChat={timesheet?.hasChat}
        hasAttachment={timesheet?.hasAttachment}
        canNavigatePrev={currentEmpIndex > 0}
        canNavigateNext={currentEmpIndex >= 0 && currentEmpIndex < filteredEmployees.length - 1}
        theme={theme}
        onToggleTheme={handleToggleTheme}
      />

      {/* Main Content Body */}
      <div className="app-main-body">
        {/* Left Employee List Panel */}
        <EmployeeList
          employees={filteredEmployees}
          selectedEmployeeId={selectedEmployeeId}
          onSelectEmployee={(id) => {
            if (isDirty && !window.confirm('Modifications non enregistrées. Continuer ?')) return;
            setSelectedEmployeeId(id);
          }}
          searchQuery={searchQuery}
          onSearchChange={setSearchQuery}
          statusFilter={statusFilter}
          onStatusFilterChange={setStatusFilter}
          sheetFilter={sheetFilter}
          onSheetFilterChange={setSheetFilter}
          currentIndex={currentEmpIndex}
          totalCount={filteredEmployees.length}
        />

        {/* Right Content Area */}
        <main className="app-content-area">
          {/* Header Panel */}
          <TimeSheetHeader
            employee={currentEmployee}
            period={currentPeriod}
            timesheet={timesheet}
            periods={periods}
            isEditable={isEditable}
            onPeriodChange={(pid) => setSelectedPeriodId(pid)}
            onSheetTypeChange={(type) => {
              if (!isEditable) return;
              setTimesheet((prev) => ({ ...prev, sheetType: type }));
              setIsDirty(true);
            }}
            onPaymentTypeChange={(ptype) => {
              if (!isEditable) return;
              setTimesheet((prev) => ({ ...prev, paymentType: ptype }));
              setIsDirty(true);
            }}
          />

          {/* Credits & Accrual Banks Summary Bar */}
          <CreditsBank credits={timesheet?.creditsBank} />

          {/* Detail Tabs & Matrix Grid */}
          <TimeSheetGrid
            details={timesheet?.details || []}
            primes={timesheet?.primes || []}
            creditMovements={timesheet?.creditMovements || []}
            deductions={timesheet?.deductions || []}
            taxableBenefits={timesheet?.taxableBenefits || []}
            accountingEntries={timesheet?.accountingEntries || []}
            warnings={timesheet?.warnings || []}
            errors={timesheet?.errors || []}
            lookups={lookups}
            isEditable={isEditable}
            status={timesheet?.status}
            period={currentPeriod}
            onUpdateDetailLine={handleUpdateDetailLine}
            onAddDetailLine={handleAddDetailLine}
            onDeleteDetailLine={handleDeleteDetailLine}
            onDuplicateDetailLine={handleDuplicateDetailLine}
            onUpdateDeduction={handleUpdateDeduction}
            onAddDeduction={handleAddDeduction}
            onDeleteDeduction={handleDeleteDeduction}
            onUpdateBenefit={handleUpdateBenefit}
            onAddBenefit={handleAddBenefit}
            onDeleteBenefit={handleDeleteBenefit}
          />
        </main>
      </div>

      {/* Multi-criteria Search Modal */}
      <SearchModal
        isOpen={isSearchOpen}
        onClose={() => setIsSearchOpen(false)}
        employees={employees}
        onSelectEmployee={(id) => setSelectedEmployeeId(id)}
      />

      {/* Toast Notification */}
      {toastMessage && (
        <div
          style={{
            position: 'fixed',
            bottom: 24,
            right: 24,
            backgroundColor: '#1f2937',
            border: '1px solid #374151',
            boxShadow: 'var(--shadow-lg)',
            borderRadius: 8,
            padding: '10px 18px',
            color: '#f9fafb',
            fontSize: '0.85rem',
            fontWeight: 500,
            zIndex: 9999,
            display: 'flex',
            alignItems: 'center',
            gap: 8,
          }}
        >
          {toastMessage}
        </div>
      )}
    </div>
  );
}
