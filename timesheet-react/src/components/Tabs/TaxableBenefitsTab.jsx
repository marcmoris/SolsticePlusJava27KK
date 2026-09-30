import React from 'react';
import { Plus, Trash2, Award, Lock } from 'lucide-react';
import { formatCurrency } from '../../utils/formatters';

export default function TaxableBenefitsTab({
  benefits = [],
  isEditable = true,
  lookups = {},
  onUpdateBenefit,
  onAddBenefit,
  onDeleteBenefit,
}) {
  const totalAmount = benefits.reduce((acc, b) => acc + (Number(b.amount) || 0), 0);

  return (
    <div style={{ padding: '16px 20px', display: 'flex', flexDirection: 'column', gap: 14 }}>
      {/* Header Info */}
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <Award size={18} color="var(--primary-500)" />
          <h3 style={{ fontSize: '0.95rem', fontWeight: 600, color: 'var(--text-main)' }}>
            Avantages Imposables
          </h3>
          <span className="badge badge-initial" style={{ fontSize: '0.7rem' }}>
            {benefits.length} avantage{benefits.length > 1 ? 's' : ''}
          </span>
        </div>

        {isEditable ? (
          <button className="btn-add-line" onClick={onAddBenefit}>
            <Plus size={14} />
            <span>Ajouter un avantage</span>
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
              <th style={{ minWidth: 260 }}>Description de l'Avantage Imposable</th>
              <th style={{ textAlign: 'right', minWidth: 140 }}>Montant ($)</th>
              <th style={{ minWidth: 120 }}>Période</th>
              <th style={{ minWidth: 120 }}>Origine</th>
              {isEditable && <th style={{ width: 50, textAlign: 'center' }}>Action</th>}
            </tr>
          </thead>
          <tbody>
            {benefits.length === 0 ? (
              <tr>
                <td colSpan={isEditable ? 5 : 4} style={{ textAlign: 'center', padding: '24px', color: 'var(--text-dim)' }}>
                  Aucun avantage imposable déclaré pour cette feuille de temps.
                </td>
              </tr>
            ) : (
              benefits.map((b) => (
                <tr key={b.id}>
                  {/* Benefit selection */}
                  <td>
                    {isEditable ? (
                      <select
                        value={b.benefitId}
                        onChange={(e) => onUpdateBenefit(b.id, { benefitId: Number(e.target.value) })}
                        style={{ width: '100%', fontSize: '0.78rem' }}
                      >
                        {lookups.taxableBenefits?.map((lk) => (
                          <option key={lk.id} value={lk.id}>
                            {lk.name}
                          </option>
                        ))}
                      </select>
                    ) : (
                      <span style={{ fontWeight: 600, color: 'var(--text-main)' }}>
                        {lookups.taxableBenefits?.find((lk) => lk.id === b.benefitId)?.name || b.name || `BEN-${b.benefitId}`}
                      </span>
                    )}
                  </td>

                  {/* Amount */}
                  <td style={{ textAlign: 'right' }}>
                    {isEditable ? (
                      <input
                        type="number"
                        step="0.01"
                        className="day-input"
                        value={b.amount === 0 ? '' : b.amount}
                        placeholder="0.00"
                        onChange={(e) =>
                          onUpdateBenefit(b.id, {
                            amount: parseFloat(e.target.value) || 0,
                          })
                        }
                      />
                    ) : (
                      <span style={{ fontFamily: 'var(--font-mono)', fontWeight: 600, color: '#fbbf24' }}>
                        {formatCurrency(b.amount)}
                      </span>
                    )}
                  </td>

                  {/* Period */}
                  <td>
                    <span style={{ fontFamily: 'var(--font-mono)', fontSize: '0.78rem' }}>
                      {b.periodName || '2026-P19'}
                    </span>
                  </td>

                  {/* Origin */}
                  <td>
                    <span className="badge badge-initial" style={{ fontSize: '0.68rem' }}>
                      {b.origin || 'Système'}
                    </span>
                  </td>

                  {/* Delete Action */}
                  {isEditable && (
                    <td style={{ textAlign: 'center' }}>
                      <button
                        className="btn-line-action"
                        title="Supprimer l'avantage"
                        onClick={() => onDeleteBenefit(b.id)}
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
          {benefits.length > 0 && (
            <tfoot>
              <tr className="totals-row">
                <td style={{ textAlign: 'right', paddingRight: 16 }}>TOTAL AVANTAGES IMPOSABLES :</td>
                <td style={{ textAlign: 'right', fontFamily: 'var(--font-mono)', color: '#fbbf24', fontSize: '0.95rem' }}>
                  {formatCurrency(totalAmount)}
                </td>
                <td colSpan={isEditable ? 3 : 2}></td>
              </tr>
            </tfoot>
          )}
        </table>
      </div>
    </div>
  );
}
