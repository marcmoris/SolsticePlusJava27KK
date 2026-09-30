/**
 * Formats a date string or timestamp to YYYY-MM-DD
 */
export function formatDateISO(date) {
  if (!date) return '';
  const d = new Date(date);
  return d.toISOString().split('T')[0];
}

/**
 * Formats a date string to French display format (DD/MM/YYYY)
 */
export function formatDateDisplay(date) {
  if (!date) return '';
  const d = new Date(date);
  return d.toLocaleDateString('fr-CA', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
  });
}

/**
 * Returns the short day name in French (e.g. Dim, Lun, Mar, Mer, Jeu, Ven, Sam)
 */
export function getShortDayName(dayIndex) {
  const days = ['Dim', 'Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam'];
  return days[dayIndex % 7];
}

/**
 * Calculates days between two date strings
 */
export function getDaysBetween(start, end) {
  const d1 = new Date(start);
  const d2 = new Date(end);
  const diffTime = Math.abs(d2 - d1);
  return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
}
