import React from 'react';
import { BookOpen, CheckCircle2, AlertCircle } from 'lucide-react';
import { formatCurrency } from '../../utils/formatters';

export default function AccountingEntriesTab({ entries = [] }) {
  const totalDebit = entries.reduce((acc, e) => acc + (Number(e.debit) || 0), 0);
  const totalCredit = entries.reduce((acc, e) => acc + (Number(e.credit) || 0), 0);
  const isBalanced = Math.abs(totalDebit - totalCredit) < 0.01;

  return (
    <div style={{ padding: '16px 20px', display: 'flex', flexDirection: 'column', gap: 14 }}>
      {/* Header Info */}
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <BookOpen size={18} color="var(--primary-500)" />
          <h3 style={{ fontSize: '0.95rem', fontWeight: 600, color: 'var(--text-main)' }}>
            Écritures Comptables & Répartition Grand Livre (GL)
          </h3>
          <span className="badge badge-initial" style={{ fontSize: '0.7rem' }}>
            {entries.length} ligne{entries.length > 1 ? 's' : ''}
          </span>
        </div>

        {/* Balance Status Indicator */}
        <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
          {isBalanced ? (
            <span
              style={{
                display: 'inline-flex',
                alignItems: 'center',
                gap: 4,
                padding: '4px 10px',
                borderRadius: 4,
                backgroundColor: 'rgba(16, 185, 129, 0.15)',
                color: '#34d399',
                fontSize: '0.75rem',
                fontWeight: 600,
              }}
            >
              <CheckCircle2 size={13} />
              Journal Équilibré (Débit = Crédit)
            </span>
          ) : (
            <span
              style={{
                display: 'inline-flex',
                alignItems: 'center',
                gap: 4,
                padding: '4px 10px',
                borderRadius: 4,
                backgroundColor: 'rgba(239, 68, 68, 0.15)',
                color: '#f87171',
                fontSize: '0.75rem',
                fontWeight: 600,
              }}
            >
              <AlertCircle size={13} />
              Écart de Balancement : {formatCurrency(Math.abs(totalDebit - totalCredit))}
            </span>
          )}
        </div>
      </div>

      {/* Table */}
      <div className="grid-table-wrapper" style={{ border: '1px solid var(--border-subtle)', borderRadius: 'var(--radius-md)' }}>
        <table className="timesheet-table">
          <thead>
            <tr>
              <th style={{ minWidth: 160 }}>Région / Territoire</th>
              <th style={{ minWidth: 240 }}>Compte Comptable</th>
              <th style={{ minWidth: 200 }}>Activité / Centre de Coûts</th>
              <th style={{ textAlign: 'right', minWidth: 130 }}>Débit ($)</th>
              <th style={{ textAlign: 'right', minWidth: 130 }}>Crédit ($)</th>
              <th style={{ width: 40, textAlign: 'center' }}>Corr.</th>
            </tr>
          </thead>
          <tbody>
            {entries.length === 0 ? (
              <tr>
                <td colSpan={6} style={{ textAlign: 'center', padding: '24px', color: 'var(--text-dim)' }}>
                  Aucune écriture comptable générée pour cette feuille. Les écritures sont calculées lors de la validation.
                </td>
              </tr>
            ) : (
              entries.map((entry) => (
                <tr key={entry.id}>
                  {/* Region */}
                  <td style={{ color: 'var(--text-muted)', fontSize: '0.78rem' }}>
                    {entry.region || '0100 - Région Est'}
                  </td>

                  {/* Account */}
                  <td style={{ fontWeight: 600, color: 'var(--text-main)' }}>
                    {entry.account}
                  </td>

                  {/* Activity */}
                  <td style={{ color: 'var(--text-muted)', fontSize: '0.78rem' }}>
                    {entry.activity || '-'}
                  </td>

                  {/* Debit */}
                  <td style={{ textAlign: 'right', fontFamily: 'var(--font-mono)', fontWeight: 600, color: entry.debit > 0 ? 'var(--text-main)' : 'var(--text-dim)' }}>
                    {entry.debit > 0 ? formatCurrency(entry.debit) : '-'}
                  </td>

                  {/* Credit */}
                  <td style={{ textAlign: 'right', fontFamily: 'var(--font-mono)', fontWeight: 600, color: entry.credit > 0 ? 'var(--text-main)' : 'var(--text-dim)' }}>
                    {entry.credit > 0 ? formatCurrency(entry.credit) : '-'}
                  </td>

                  {/* Correction Flag (*) */}
                  <td style={{ textAlign: 'center', color: '#fbbf24', fontWeight: 700 }}>
                    {entry.isCorrection ? '*' : ''}
                  </td>
                </tr>
              ))
            )}
          </tbody>

          {/* Totals Summary */}
          {entries.length > 0 && (
            <tfoot>
              <tr className="totals-row">
                <td colSpan={3} style={{ textAlign: 'right', paddingRight: 16 }}>
                  TOTAUX DES ÉCRITURES :
                </td>
                <td style={{ textAlign: 'right', fontFamily: 'var(--font-mono)', fontSize: '0.95rem', color: '#60a5fa' }}>
                  {formatCurrency(totalDebit)}
                </td>
                <td style={{ textAlign: 'right', fontFamily: 'var(--font-mono)', fontSize: '0.95rem', color: '#60a5fa' }}>
                  {formatCurrency(totalCredit)}
                </td>
                <td></td>
              </tr>
            </tfoot>
          )}
        </table>
      </div>
    </div>
  );
}
