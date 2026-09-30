import React, { useState, useMemo } from 'react';
import {
  CalendarDays,
  Plus,
  Trash2,
  Copy,
  Receipt,
  History,
  AlertTriangle,
  Lock,
  ShieldCheck,
  Award,
  BookOpen,
} from 'lucide-react';
import DeductionsTab from '../Tabs/DeductionsTab';
import TaxableBenefitsTab from '../Tabs/TaxableBenefitsTab';
import AccountingEntriesTab from '../Tabs/AccountingEntriesTab';
import { formatNumber } from '../../utils/formatters';
import './TimeSheetGrid.css';

const DAYS_HEADER = ['Dim', 'Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam'];

export default function TimeSheetGrid({
  details = [],
  primes = [],
  creditMovements = [],
  deductions = [],
  taxableBenefits = [],
  accountingEntries = [],
  warnings = [],
  errors = [],
  lookups = {},
  isEditable = true,
  status = 'Initial',
  onUpdateDetailLine,
  onAddDetailLine,
  onDeleteDetailLine,
  onDuplicateDetailLine,
  onUpdateDeduction,
  onAddDeduction,
  onDeleteDeduction,
  onUpdateBenefit,
  onAddBenefit,
  onDeleteBenefit,
  period,
}) {
  const [activeTab, setActiveTab] = useState('grid'); // 'grid' | 'primes' | 'movements' | 'deductions' | 'taxable' | 'accounting' | 'errors'
  const [selectedWeek, setSelectedWeek] = useState('ALL'); // 'ALL' | 1 | 2

  // Filter lines by selected week
  const filteredLines = useMemo(() => {
    if (selectedWeek === 'ALL') return details;
    return details.filter((d) => d.week === Number(selectedWeek));
  }, [details, selectedWeek]);

  // Compute daily totals and grand total
  const totals = useMemo(() => {
    const dayTotals = [0, 0, 0, 0, 0, 0, 0];
    let grandTotal = 0;

    filteredLines.forEach((line) => {
      line.hours.forEach((h, idx) => {
        const val = Number(h) || 0;
        dayTotals[idx] += val;
        grandTotal += val;
      });
    });

    return { dayTotals, grandTotal };
  }, [filteredLines]);

  // Generate date labels for headers if period dates exist
  const dayDates = useMemo(() => {
    if (!period || !period.startDate) return DAYS_HEADER.map(() => '');
    const start = new Date(period.startDate);
    return Array.from({ length: 7 }).map((_, i) => {
      const d = new Date(start);
      d.setDate(d.getDate() + i + (selectedWeek === 2 ? 7 : 0));
      return `${d.getDate()}/${d.getMonth() + 1}`;
    });
  }, [period, selectedWeek]);

  return (
    <div className="timesheet-detail-container">
      {/* Tabs Bar */}
      <div className="tabs-nav">
        <div className="tabs-list">
          <button
            className={`tab-btn ${activeTab === 'grid' ? 'active' : ''}`}
            onClick={() => setActiveTab('grid')}
          >
            <CalendarDays size={15} />
            <span>Feuille de Temps</span>
            <span className="tab-count-badge">{details.length}</span>
          </button>

          <button
            className={`tab-btn ${activeTab === 'primes' ? 'active' : ''}`}
            onClick={() => setActiveTab('primes')}
          >
            <Receipt size={15} />
            <span>Primes (106)</span>
            {primes.length > 0 && <span className="tab-count-badge">{primes.length}</span>}
          </button>

          <button
            className={`tab-btn ${activeTab === 'movements' ? 'active' : ''}`}
            onClick={() => setActiveTab('movements')}
          >
            <History size={15} />
            <span>Mouvements Banques</span>
            {creditMovements.length > 0 && (
              <span className="tab-count-badge">{creditMovements.length}</span>
            )}
          </button>

          <button
            className={`tab-btn ${activeTab === 'deductions' ? 'active' : ''}`}
            onClick={() => setActiveTab('deductions')}
          >
            <ShieldCheck size={15} />
            <span>Déductions</span>
            {deductions.length > 0 && (
              <span className="tab-count-badge">{deductions.length}</span>
            )}
          </button>

          <button
            className={`tab-btn ${activeTab === 'taxable' ? 'active' : ''}`}
            onClick={() => setActiveTab('taxable')}
          >
            <Award size={15} />
            <span>Avantages Imposables</span>
            {taxableBenefits.length > 0 && (
              <span className="tab-count-badge">{taxableBenefits.length}</span>
            )}
          </button>

          <button
            className={`tab-btn ${activeTab === 'accounting' ? 'active' : ''}`}
            onClick={() => setActiveTab('accounting')}
          >
            <BookOpen size={15} />
            <span>Écritures Comptables</span>
            {accountingEntries.length > 0 && (
              <span className="tab-count-badge">{accountingEntries.length}</span>
            )}
          </button>

          <button
            className={`tab-btn ${activeTab === 'errors' ? 'active' : ''}`}
            onClick={() => setActiveTab('errors')}
          >
            <AlertTriangle size={15} />
            <span>Erreurs & Alertes</span>
            {warnings.length + errors.length > 0 && (
              <span
                className="tab-count-badge"
                style={{
                  backgroundColor: errors.length > 0 ? 'rgba(239, 68, 68, 0.2)' : 'rgba(245, 158, 11, 0.2)',
                  color: errors.length > 0 ? '#f87171' : '#fbbf24',
                }}
              >
                {warnings.length + errors.length}
              </span>
            )}
          </button>
        </div>

        {/* Right controls for week filtering */}
        {activeTab === 'grid' && (
          <div className="tab-controls-right">
            <span style={{ fontSize: '0.72rem', color: 'var(--text-dim)' }}>Semaine :</span>
            <button
              className={`week-selector-btn ${selectedWeek === 'ALL' ? 'active' : ''}`}
              onClick={() => setSelectedWeek('ALL')}
            >
              Toutes
            </button>
            <button
              className={`week-selector-btn ${selectedWeek === 1 ? 'active' : ''}`}
              onClick={() => setSelectedWeek(1)}
            >
              Semaine 1
            </button>
            <button
              className={`week-selector-btn ${selectedWeek === 2 ? 'active' : ''}`}
              onClick={() => setSelectedWeek(2)}
            >
              Semaine 2
            </button>
          </div>
        )}
      </div>

      {/* Read-only notification banner when not in Initial status */}
      {!isEditable && (
        <div className="readonly-banner">
          <Lock size={14} color="#fbbf24" />
          <span>
            <strong>Feuille de temps verrouillée en lecture seule (Statut : {status}).</strong> Les heures et paramètres ne peuvent être modifiés que lorsque la feuille est au statut « Initiale » (I).
          </span>
        </div>
      )}

      {/* Main Grid View */}
      {activeTab === 'grid' && (
        <>
          <div className="grid-table-wrapper">
            <table className="timesheet-table">
              <thead>
                <tr>
                  <th className="col-week">Sem.</th>
                  <th className="col-select">Affectation / Poste</th>
                  <th className="col-select">Rubrique / Gain</th>
                  <th className="col-select" style={{ minWidth: 140 }}>Horaire</th>
                  <th className="col-select" style={{ minWidth: 140 }}>Activité</th>
                  {DAYS_HEADER.map((day, idx) => (
                    <th key={day} className="col-day col-day-th">
                      <span className="day-name">{day}</span>
                      <span className="day-date">{dayDates[idx]}</span>
                    </th>
                  ))}
                  <th className="col-total">Total</th>
                  {isEditable && <th className="col-actions">Actions</th>}
                </tr>
              </thead>
              <tbody>
                {filteredLines.map((line) => {
                  const lineTotal = line.hours.reduce((acc, h) => acc + (Number(h) || 0), 0);

                  return (
                    <tr key={line.id}>
                      {/* Week Select */}
                      <td className="col-week">
                        <select
                          value={line.week}
                          disabled={!isEditable}
                          onChange={(e) =>
                            onUpdateDetailLine(line.id, { week: Number(e.target.value) })
                          }
                          style={{
                            padding: '2px 4px',
                            fontSize: '0.75rem',
                            opacity: isEditable ? 1 : 0.75,
                            cursor: isEditable ? 'pointer' : 'default',
                          }}
                        >
                          <option value={1}>S1</option>
                          <option value={2}>S2</option>
                        </select>
                      </td>

                      {/* Assignment */}
                      <td>
                        <select
                          value={line.assignmentId}
                          disabled={!isEditable}
                          onChange={(e) =>
                            onUpdateDetailLine(line.id, { assignmentId: Number(e.target.value) })
                          }
                          style={{
                            width: '100%',
                            fontSize: '0.75rem',
                            opacity: isEditable ? 1 : 0.75,
                            cursor: isEditable ? 'pointer' : 'default',
                          }}
                        >
                          {lookups.assignments?.map((a) => (
                            <option key={a.id} value={a.id}>
                              {a.name}
                            </option>
                          ))}
                        </select>
                      </td>

                      {/* Gain */}
                      <td>
                        <select
                          value={line.gainId}
                          disabled={!isEditable}
                          onChange={(e) =>
                            onUpdateDetailLine(line.id, { gainId: Number(e.target.value) })
                          }
                          style={{
                            width: '100%',
                            fontSize: '0.75rem',
                            opacity: isEditable ? 1 : 0.75,
                            cursor: isEditable ? 'pointer' : 'default',
                          }}
                        >
                          {lookups.gains?.map((g) => (
                            <option key={g.id} value={g.id}>
                              {g.name}
                            </option>
                          ))}
                        </select>
                      </td>

                      {/* Schedule */}
                      <td>
                        <select
                          value={line.scheduleId}
                          disabled={!isEditable}
                          onChange={(e) =>
                            onUpdateDetailLine(line.id, { scheduleId: Number(e.target.value) })
                          }
                          style={{
                            width: '100%',
                            fontSize: '0.75rem',
                            opacity: isEditable ? 1 : 0.75,
                            cursor: isEditable ? 'pointer' : 'default',
                          }}
                        >
                          {lookups.schedules?.map((s) => (
                            <option key={s.id} value={s.id}>
                              {s.name}
                            </option>
                          ))}
                        </select>
                      </td>

                      {/* Activity */}
                      <td>
                        <select
                          value={line.activityId}
                          disabled={!isEditable}
                          onChange={(e) =>
                            onUpdateDetailLine(line.id, { activityId: Number(e.target.value) })
                          }
                          style={{
                            width: '100%',
                            fontSize: '0.75rem',
                            opacity: isEditable ? 1 : 0.75,
                            cursor: isEditable ? 'pointer' : 'default',
                          }}
                        >
                          {lookups.activities?.map((act) => (
                            <option key={act.id} value={act.id}>
                              {act.name}
                            </option>
                          ))}
                        </select>
                      </td>

                      {/* Day Hours (Dimanche à Samedi) */}
                      {line.hours.map((val, dayIdx) => (
                        <td key={dayIdx} className="col-day">
                          <input
                            type="number"
                            step="0.25"
                            min="0"
                            max="24"
                            readOnly={!isEditable}
                            className="day-input"
                            value={val === 0 ? '' : val}
                            placeholder="-"
                            onChange={(e) => {
                              if (!isEditable) return;
                              const newHours = [...line.hours];
                              newHours[dayIdx] = e.target.value === '' ? 0 : parseFloat(e.target.value) || 0;
                              onUpdateDetailLine(line.id, {
                                hours: newHours,
                                quantity: newHours.reduce((acc, h) => acc + h, 0),
                              });
                            }}
                          />
                        </td>
                      ))}

                      {/* Row Total */}
                      <td className="col-total">{formatNumber(lineTotal)} h</td>

                      {/* Row Actions (Duplicate, Delete) - Only if editable */}
                      {isEditable && (
                        <td className="col-actions">
                          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 4 }}>
                            <button
                              className="btn-line-action"
                              title="Dupliquer la ligne"
                              onClick={() => onDuplicateDetailLine(line.id)}
                            >
                              <Copy size={13} />
                            </button>
                            <button
                              className="btn-line-action"
                              title="Supprimer la ligne"
                              onClick={() => onDeleteDetailLine(line.id)}
                            >
                              <Trash2 size={13} />
                            </button>
                          </div>
                        </td>
                      )}
                    </tr>
                  );
                })}
              </tbody>

              {/* Totals Summary Row */}
              <tfoot>
                <tr className="totals-row">
                  <td colSpan={5} style={{ textAlign: 'right', paddingRight: 16 }}>
                    TOTAL GÉNÉRAL :
                  </td>
                  {totals.dayTotals.map((tot, idx) => (
                    <td key={idx} className="total-cell">
                      {tot > 0 ? `${formatNumber(tot)} h` : '-'}
                    </td>
                  ))}
                  <td className="total-cell grand-total-cell">
                    {formatNumber(totals.grandTotal)} h
                  </td>
                  {isEditable && <td></td>}
                </tr>
              </tfoot>
            </table>
          </div>

          {/* Bottom Table Action Bar */}
          <div className="grid-bottom-bar">
            {isEditable ? (
              <button className="btn-add-line" onClick={onAddDetailLine}>
                <Plus size={15} />
                <span>Ajouter une ligne</span>
              </button>
            ) : (
              <div style={{ display: 'flex', alignItems: 'center', gap: 6, color: 'var(--text-dim)', fontSize: '0.8rem' }}>
                <Lock size={14} />
                <span>Modification des lignes désactivée en mode consultation.</span>
              </div>
            )}
            <div style={{ fontSize: '0.75rem', color: 'var(--text-dim)' }}>
              {isEditable
                ? 'Astuce : Utilisez la touche Tab pour naviguer rapidement entre les jours.'
                : 'Passez la feuille au statut "Initiale" pour réactiver la saisie.'}
            </div>
          </div>
        </>
      )}

      {/* Primes Tab */}
      {activeTab === 'primes' && (
        <div style={{ padding: 20 }}>
          <div style={{ fontSize: '0.9rem', fontWeight: 600, marginBottom: 12 }}>
            Primes & Indemnités Déclarées (Gain 106)
          </div>
          {primes.length === 0 ? (
            <div style={{ color: 'var(--text-dim)', fontSize: '0.82rem' }}>
              Aucune prime spécifique enregistrée pour cette feuille de temps.
            </div>
          ) : (
            <table className="timesheet-table">
              <thead>
                <tr>
                  <th>Date</th>
                  <th>Code Prime</th>
                  <th>Description</th>
                  <th style={{ textAlign: 'right' }}>Montant ($)</th>
                </tr>
              </thead>
              <tbody>
                {primes.map((pr) => (
                  <tr key={pr.id}>
                    <td>{pr.date}</td>
                    <td style={{ fontFamily: 'var(--font-mono)' }}>{pr.code}</td>
                    <td>{pr.name}</td>
                    <td style={{ textAlign: 'right', fontWeight: 600, color: '#34d399' }}>
                      {formatNumber(pr.amount)} $
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}

      {/* Bank Movements Tab */}
      {activeTab === 'movements' && (
        <div style={{ padding: 20 }}>
          <div style={{ fontSize: '0.9rem', fontWeight: 600, marginBottom: 12 }}>
            Historique des mouvements de crédits (Période courante)
          </div>
          {creditMovements.length === 0 ? (
            <div style={{ color: 'var(--text-dim)', fontSize: '0.82rem' }}>
              Aucun mouvement de banque pour cette période.
            </div>
          ) : (
            <table className="timesheet-table">
              <thead>
                <tr>
                  <th>Date</th>
                  <th>Banque</th>
                  <th>Mouvement</th>
                  <th>Unité</th>
                  <th>Motif / Justification</th>
                </tr>
              </thead>
              <tbody>
                {creditMovements.map((m) => (
                  <tr key={m.id}>
                    <td>{m.date}</td>
                    <td>
                      <strong>{m.bankName}</strong> ({m.bankCode})
                    </td>
                    <td
                      style={{
                        color: m.variation < 0 ? '#f87171' : '#34d399',
                        fontWeight: 600,
                      }}
                    >
                      {m.variation > 0 ? `+${m.variation}` : m.variation}
                    </td>
                    <td>{m.uom}</td>
                    <td>{m.description}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}

      {/* Deductions Tab */}
      {activeTab === 'deductions' && (
        <DeductionsTab
          deductions={deductions}
          isEditable={isEditable}
          lookups={lookups}
          onUpdateDeduction={onUpdateDeduction}
          onAddDeduction={onAddDeduction}
          onDeleteDeduction={onDeleteDeduction}
        />
      )}

      {/* Taxable Benefits Tab */}
      {activeTab === 'taxable' && (
        <TaxableBenefitsTab
          benefits={taxableBenefits}
          isEditable={isEditable}
          lookups={lookups}
          onUpdateBenefit={onUpdateBenefit}
          onAddBenefit={onAddBenefit}
          onDeleteBenefit={onDeleteBenefit}
        />
      )}

      {/* Accounting Entries Tab */}
      {activeTab === 'accounting' && (
        <AccountingEntriesTab entries={accountingEntries} />
      )}

      {/* Errors & Alerts Tab */}
      {activeTab === 'errors' && (
        <div style={{ padding: 20 }}>
          <div style={{ fontSize: '0.9rem', fontWeight: 600, marginBottom: 12 }}>
            Journal des alertes et anomalies de validation
          </div>
          {warnings.length === 0 && errors.length === 0 ? (
            <div style={{ color: '#34d399', fontSize: '0.85rem' }}>
              ✓ Aucune anomalie détectée sur cette feuille de temps.
            </div>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
              {errors.map((err, i) => (
                <div
                  key={i}
                  style={{
                    padding: '8px 12px',
                    borderRadius: 6,
                    background: 'rgba(239, 68, 68, 0.1)',
                    border: '1px solid rgba(239, 68, 68, 0.3)',
                    color: '#f87171',
                    fontSize: '0.8rem',
                  }}
                >
                  <strong>Erreur :</strong> {err}
                </div>
              ))}
              {warnings.map((warn, i) => (
                <div
                  key={i}
                  style={{
                    padding: '8px 12px',
                    borderRadius: 6,
                    background: 'rgba(245, 158, 11, 0.1)',
                    border: '1px solid rgba(245, 158, 11, 0.3)',
                    color: '#fbbf24',
                    fontSize: '0.8rem',
                  }}
                >
                  <strong>Avertissement :</strong> {warn}
                </div>
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
}
