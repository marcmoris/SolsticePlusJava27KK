/**
 * Formats a numeric value to a standard 2-decimal currency or quantity
 */
export function formatNumber(val, decimals = 2) {
  if (val === null || val === undefined || isNaN(val)) return '0.00';
  return Number(val).toLocaleString('fr-CA', {
    minimumFractionDigits: decimals,
    maximumFractionDigits: decimals,
  });
}

/**
 * Formats a currency value with $ sign
 */
export function formatCurrency(val) {
  if (val === null || val === undefined || isNaN(val)) return '0,00 $';
  return Number(val).toLocaleString('fr-CA', {
    style: 'currency',
    currency: 'CAD',
  });
}

/**
 * Formats hours (e.g. 7.50 -> 7,50 h)
 */
export function formatHours(val) {
  if (val === null || val === undefined || isNaN(val) || Number(val) === 0) return '-';
  return `${formatNumber(val, 2)} h`;
}
