import React from 'react';
import { PiggyBank, Calendar, Clock, HeartPulse } from 'lucide-react';
import { formatNumber } from '../../utils/formatters';
import './CreditsBank.css';

const BANK_ICONS = {
  BV20B: Calendar,
  BV21: Clock,
  BV10: PiggyBank,
  BV05: HeartPulse,
};

export default function CreditsBank({ credits = [] }) {
  if (!credits || credits.length === 0) return null;

  return (
    <div className="credits-bank-container">
      {credits.map((c) => {
        const IconComponent = BANK_ICONS[c.code] || PiggyBank;
        const isNeg = c.periodVariation < 0;
        const isPos = c.periodVariation > 0;

        return (
          <div key={c.id || c.code} className="credit-card">
            {/* Header: Bank Name & Code */}
            <div className="card-header-line">
              <div className="bank-title">
                <IconComponent size={14} color="var(--primary-500)" />
                <span>{c.name}</span>
              </div>
              <span className="bank-code-pill">{c.code}</span>
            </div>

            {/* Values: Initial, Variation, New Balance */}
            <div className="card-values-row">
              <div className="value-col">
                <span className="val-label">Solde Début</span>
                <span className="val-number">
                  {formatNumber(c.startBalance)} {c.unit}
                </span>
              </div>

              <div className="value-col" style={{ textAlign: 'center' }}>
                <span className="val-label">Variation</span>
                <span
                  className={`val-number ${isNeg ? 'variation-neg' : ''} ${isPos ? 'variation-pos' : ''}`}
                >
                  {isPos ? `+${formatNumber(c.periodVariation)}` : formatNumber(c.periodVariation)}
                </span>
              </div>

              <div className="value-col" style={{ textAlign: 'right' }}>
                <span className="val-label">Nouveau Solde</span>
                <span className="val-number main">
                  {formatNumber(c.newBalance)} {c.unit}
                </span>
              </div>
            </div>
          </div>
        );
      })}
    </div>
  );
}
