/**
 * Configuration de l'API Backend Solstice+
 * 
 * Peut être surchargé au moment du build ou à l'exécution par la variable VITE_API_URL.
 * Par défaut : http://localhost:3001/api
 */
export const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:3001/api';
