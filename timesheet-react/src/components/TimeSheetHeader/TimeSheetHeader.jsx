import React from 'react';
import { Calendar, CreditCard, Building2, Briefcase, FileText, CheckCircle2, Lock, Unlock } from 'lucide-react';
import { formatCurrency } from '../../utils/formatters';
import './TimeSheetHeader.css';

export default function TimeSheetHeader({
  employee,
  period,
  timesheet,
  periods = [],
  isEditable = true,
  onPeriodChange,
  onSheetTypeChange,
  onPaymentTypeChange,
}) {
  if (!employee) return null;

  const initials = employee.firstName && employee.lastName
    ? `${employee.firstName[0]}${employee.lastName[0]}`
    : 'EM';

  const status = timesheet?.status || 'Initial';

  return (
    <div className="timesheet-header-card">
      {/* Top Row: Employee Hero + Status + Period Selector */}
      <div className="header-top-row">
        {/* Employee Info */}
        <div className="employee-hero">
          <div className="avatar-badge">{initials}</div>
          <div className="hero-info">
            <div className="hero-name">{employee.name}</div>
            <div className="hero-meta">
              <span className="meta-pill">{employee.value}</span>
              <span>•</span>
              <span>{employee.department}</span>
            </div>
          </div>
        </div>

        {/* Selectors and Status */}
        <div className="header-selectors">
          {/* Pay Period Dropdown */}
          <div className="selector-item">
            <span className="selector-label">Période de Paie</span>
            <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
              <select
                value={period?.id || ''}
                onChange={(e) => onPeriodChange(Number(e.target.value))}
                style={{ fontWeight: 600, minWidth: 160 }}
              >
                {periods.map((p) => (
                  <option key={p.id} value={p.id}>
                    {p.name} ({p.startDate} au {p.endDate})
                  </option>
                ))}
              </select>
            </div>
          </div>

          {/* Timesheet Status Badge */}
          <div className="selector-item">
            <span className="selector-label">État Feuille</span>
            <div>
              <span className={`badge badge-${status.toLowerCase()}`}>
                {isEditable ? <Unlock size={12} /> : <Lock size={12} />}
                {status} {!isEditable && '(Lecture seule)'}
              </span>
            </div>
          </div>

          {/* Pay Summary (Gross & Net) */}
          <div className="pay-summary-box">
            <div className="pay-item">
              <span className="selector-label">Salaire Brut</span>
              <span className="val gross">{formatCurrency(timesheet?.grossPay || 0)}</span>
            </div>
            <div style={{ width: 1, height: 26, backgroundColor: 'var(--border-subtle)' }} />
            <div className="pay-item">
              <span className="selector-label">Salaire Net Est.</span>
              <span className="val">{formatCurrency(timesheet?.netPay || 0)}</span>
            </div>
          </div>
        </div>
      </div>

      {/* Metadata Detail Grid */}
      <div className="metadata-grid">
        <div className="meta-field">
          <span className="meta-field-label">Titre d'Emploi</span>
          <span className="meta-field-value">{employee.jobTitle || '-'}</span>
        </div>

        <div className="meta-field">
          <span className="meta-field-label">Statut d'Emploi</span>
          <span className="meta-field-value">{employee.jobType || '-'}</span>
        </div>

        <div className="meta-field">
          <span className="meta-field-label">Livret de Distribution</span>
          <span className="meta-field-value">{employee.booklet || '-'}</span>
        </div>

        <div className="meta-field">
          <span className="meta-field-label">Convention Collective</span>
          <span className="meta-field-value">{employee.convention || '-'}</span>
        </div>

        <div className="meta-field">
          <span className="meta-field-label">Type de Feuille</span>
          <select
            value={timesheet?.sheetType || 'Regular'}
            disabled={!isEditable}
            onChange={(e) => onSheetTypeChange(e.target.value)}
            style={{
              fontSize: '0.78rem',
              padding: '3px 6px',
              opacity: isEditable ? 1 : 0.7,
              cursor: isEditable ? 'pointer' : 'not-allowed',
            }}
          >
            <option value="Regular">Régulière</option>
            <option value="Adjustment">Ajustement</option>
            <option value="Complementary">Complémentaire</option>
            <option value="Advance">Avance</option>
            <option value="ExpenseAccount">Compte de dépenses</option>
          </select>
        </div>

        <div className="meta-field">
          <span className="meta-field-label">Mode de Paiement</span>
          <select
            value={timesheet?.paymentType || 'Deposit'}
            disabled={!isEditable}
            onChange={(e) => onPaymentTypeChange(e.target.value)}
            style={{
              fontSize: '0.78rem',
              padding: '3px 6px',
              opacity: isEditable ? 1 : 0.7,
              cursor: isEditable ? 'pointer' : 'not-allowed',
            }}
          >
            <option value="Deposit">Dépôt direct</option>
            <option value="Cheque">Chèque</option>
          </select>
        </div>

        <div className="meta-field">
          <span className="meta-field-label">Numéro Feuille</span>
          <span className="meta-field-value" style={{ fontFamily: 'var(--font-mono)' }}>
            {timesheet?.sheetNumber || '-'}
          </span>
        </div>

        <div className="meta-field">
          <span className="meta-field-label">Région de Taxation</span>
          <span className="meta-field-value">{employee.region || 'Québec (QC)'}</span>
        </div>
      </div>
    </div>
  );
}
