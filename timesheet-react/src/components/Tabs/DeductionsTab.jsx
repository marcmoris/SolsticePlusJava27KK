import React from 'react';
import { Plus, Trash2, ShieldCheck, Lock } from 'lucide-react';
import { formatCurrency, formatNumber } from '../../utils/formatters';

export default function DeductionsTab({
  deductions = [],
  isEditable = true,
  lookups = {},
  onUpdateDeduction,
  onAddDeduction,
  onDeleteDeduction,
}) {
  // Calculate totals
  const totalEmployee = deductions.reduce((acc, d) => acc + (Number(d.employeePart) || 0), 0);
  const totalEmployer = deductions.reduce((acc, d) => acc + (Number(d.employerPart) || 0), 0);
  const totalAccumulation = deductions.reduce((acc, d) => acc + (Number(d.accumulation) || 0), 0);
  const totalRetrieval = deductions.reduce((acc, d) => acc + (Number(d.retrieval) || 0), 0);

  return (
    <div style={{ padding: '16px 20px', display: 'flex', flexDirection: 'column', gap: 14 }}>
      {/* Header Info */}
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <ShieldCheck size={18} color="var(--primary-500)" />
          <h3 style={{ fontSize: '0.95rem', fontWeight: 600, color: 'var(--text-main)' }}>
            Déductions & Retenues à la Source
          </h3>
          <span className="badge badge-initial" style={{ fontSize: '0.7rem' }}>
            {deductions.length} retenue{deductions.length > 1 ? 's' : ''}
          </span>
        </div>

        {isEditable ? (
          <button className="btn-add-line" onClick={onAddDeduction}>
            <Plus size={14} />
            <span>Ajouter une déduction</span>
          </button>
        ) : (
          <div style={{ display: 'flex', alignItems: 'center', gap: 6, color: 'var(--text-dim)', fontSize: '0.78rem' }}>
            <Lock size={13} />
            <span>Mode consultation (non modifiable)</span>
          </div>
        )}
      </div>

      {/* Table */}
      <div className="grid-table-wrapper" style={{ border: '1px solid var(--border-subtle)', borderRadius: 'var(--radius-md)' }}>
        <table className="timesheet-table">
          <thead>
            <tr>
              <th style={{ minWidth: 200 }}>Déduction / Retenue</th>
              <th style={{ textAlign: 'right', minWidth: 120 }}>Salaire Admissible</th>
              <th style={{ textAlign: 'right', minWidth: 100 }}>Heures Adm.</th>
              <th style={{ textAlign: 'right', minWidth: 110 }}>Part Employé ($)</th>
              <th style={{ textAlign: 'right', minWidth: 110 }}>Part Employeur ($)</th>
              <th style={{ textAlign: 'right', minWidth: 100 }}>Accumulation</th>
              <th style={{ textAlign: 'right', minWidth: 100 }}>Récupération</th>
              <th style={{ minWidth: 100 }}>Origine</th>
              {isEditable && <th style={{ width: 50, textAlign: 'center' }}>Action</th>}
            </tr>
          </thead>
          <tbody>
            {deductions.length === 0 ? (
              <tr>
                <td colSpan={isEditable ? 9 : 8} style={{ textAlign: 'center', padding: '24px', color: 'var(--text-dim)' }}>
                  Aucune déduction enregistrée pour cette paie.
                </td>
              </tr>
            ) : (
              deductions.map((d) => (
                <tr key={d.id}>
                  {/* Deduction name / dropdown */}
                  <td>
                    {isEditable ? (
                      <select
                        value={d.deductionId}
                        onChange={(e) => onUpdateDeduction(d.id, { deductionId: Number(e.target.value) })}
                        style={{ width: '100%', fontSize: '0.78rem' }}
                      >
                        {lookups.deductions?.map((lk) => (
                          <option key={lk.id} value={lk.id}>
                            {lk.name}
                          </option>
                        ))}
                      </select>
                    ) : (
                      <span style={{ fontWeight: 600, color: 'var(--text-main)' }}>
                        {lookups.deductions?.find((lk) => lk.id === d.deductionId)?.name || d.name || `DED-${d.deductionId}`}
                      </span>
                    )}
                  </td>

                  {/* Salary Eligible */}
                  <td style={{ textAlign: 'right', fontFamily: 'var(--font-mono)' }}>
                    {formatCurrency(d.salaryEligible)}
                  </td>

                  {/* Hours Eligible */}
                  <td style={{ textAlign: 'right', fontFamily: 'var(--font-mono)' }}>
                    {formatNumber(d.hoursEligible)} h
                  </td>

                  {/* Employee Part */}
                  <td style={{ textAlign: 'right' }}>
                    {isEditable ? (
                      <input
                        type="number"
                        step="0.01"
                        className="day-input"
                        value={d.employeePart === 0 ? '' : d.employeePart}
                        placeholder="0.00"
                        onChange={(e) =>
                          onUpdateDeduction(d.id, {
                            employeePart: parseFloat(e.target.value) || 0,
                          })
                        }
                      />
                    ) : (
                      <span style={{ fontFamily: 'var(--font-mono)', fontWeight: 600, color: '#f87171' }}>
                        {formatCurrency(d.employeePart)}
                      </span>
                    )}
                  </td>

                  {/* Employer Part */}
                  <td style={{ textAlign: 'right' }}>
                    {isEditable ? (
                      <input
                        type="number"
                        step="0.01"
                        className="day-input"
                        value={d.employerPart === 0 ? '' : d.employerPart}
                        placeholder="0.00"
                        onChange={(e) =>
                          onUpdateDeduction(d.id, {
                            employerPart: parseFloat(e.target.value) || 0,
                          })
                        }
                      />
                    ) : (
                      <span style={{ fontFamily: 'var(--font-mono)', fontWeight: 600, color: '#60a5fa' }}>
                        {formatCurrency(d.employerPart)}
                      </span>
                    )}
                  </td>

                  {/* Accumulation */}
                  <td style={{ textAlign: 'right', fontFamily: 'var(--font-mono)', color: 'var(--text-muted)' }}>
                    {formatCurrency(d.accumulation || 0)}
                  </td>

                  {/* Retrieval */}
                  <td style={{ textAlign: 'right', fontFamily: 'var(--font-mono)', color: 'var(--text-muted)' }}>
                    {formatCurrency(d.retrieval || 0)}
                  </td>

                  {/* Origine */}
                  <td>
                    <span className="badge badge-initial" style={{ fontSize: '0.68rem' }}>
                      {d.origin || 'Automatique'}
                    </span>
                  </td>

                  {/* Delete Action */}
                  {isEditable && (
                    <td style={{ textAlign: 'center' }}>
                      <button
                        className="btn-line-action"
                        title="Supprimer la déduction"
                        onClick={() => onDeleteDeduction(d.id)}
                      >
                        <Trash2 size={13} />
                      </button>
                    </td>
                  )}
                </tr>
              ))
            )}
          </tbody>

          {/* Totals Summary */}
          {deductions.length > 0 && (
            <tfoot>
              <tr className="totals-row">
                <td style={{ textAlign: 'right', paddingRight: 16 }}>TOTAL :</td>
                <td style={{ textAlign: 'right' }}>-</td>
                <td style={{ textAlign: 'right' }}>-</td>
                <td style={{ textAlign: 'right', fontFamily: 'var(--font-mono)', color: '#f87171' }}>
                  {formatCurrency(totalEmployee)}
                </td>
                <td style={{ textAlign: 'right', fontFamily: 'var(--font-mono)', color: '#60a5fa' }}>
                  {formatCurrency(totalEmployer)}
                </td>
                <td style={{ textAlign: 'right', fontFamily: 'var(--font-mono)' }}>
                  {formatCurrency(totalAccumulation)}
                </td>
                <td style={{ textAlign: 'right', fontFamily: 'var(--font-mono)' }}>
                  {formatCurrency(totalRetrieval)}
                </td>
                <td colSpan={isEditable ? 2 : 1}></td>
              </tr>
            </tfoot>
          )}
        </table>
      </div>
    </div>
  );
}
