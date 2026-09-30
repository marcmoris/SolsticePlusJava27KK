import React from 'react';
import {
  Clock,
  Save,
  Plus,
  Trash2,
  Search,
  FileSpreadsheet,
  CheckCircle2,
  ChevronLeft,
  ChevronRight,
  ChevronsLeft,
  ChevronsRight,
  MessageSquare,
  Paperclip,
  Sun,
  Moon,
  Lock,
} from 'lucide-react';
import './HeaderToolbar.css';

export default function HeaderToolbar({
  onSave,
  onNew,
  onDelete,
  onSearch,
  onValidate,
  onExport,
  onMoveFirst,
  onMovePrev,
  onMoveNext,
  onMoveLast,
  isDirty,
  isEditable = true,
  status = 'Initial',
  hasChat,
  hasAttachment,
  canNavigatePrev,
  canNavigateNext,
  theme,
  onToggleTheme,
}) {
  return (
    <header className="header-toolbar">
      {/* Brand & Title */}
      <div className="toolbar-brand">
        <div className="brand-icon">
          <Clock size={18} />
        </div>
        <div className="brand-title">
          <span>Solstice+</span>
          <span style={{ color: 'var(--text-dim)', fontWeight: 400 }}>/</span>
          <span>Feuilles de Temps</span>
          <span className="brand-badge">PRO</span>
        </div>

        {/* Read-only indicator badge */}
        {!isEditable && (
          <div
            className="indicator-badge"
            title={`Feuille au statut ${status} : Seules les feuilles au statut Initiale (I) peuvent être modifiées.`}
            style={{ color: '#fbbf24', borderColor: 'rgba(245, 158, 11, 0.3)' }}
          >
            <Lock size={12} />
            <span>Verrouillée ({status})</span>
          </div>
        )}
      </div>

      {/* Main Actions */}
      <div className="toolbar-actions">
        {/* Navigation Buttons (cmd_moveFirst, etc.) */}
        <button
          className="btn-toolbar btn-nav"
          title="Premier employé"
          disabled={!canNavigatePrev}
          onClick={onMoveFirst}
        >
          <ChevronsLeft size={16} />
        </button>
        <button
          className="btn-toolbar btn-nav"
          title="Employé précédent"
          disabled={!canNavigatePrev}
          onClick={onMovePrev}
        >
          <ChevronLeft size={16} />
        </button>
        <button
          className="btn-toolbar btn-nav"
          title="Employé suivant"
          disabled={!canNavigateNext}
          onClick={onMoveNext}
        >
          <ChevronRight size={16} />
        </button>
        <button
          className="btn-toolbar btn-nav"
          title="Dernier employé"
          disabled={!canNavigateNext}
          onClick={onMoveLast}
        >
          <ChevronsRight size={16} />
        </button>

        <div className="toolbar-divider" />

        {/* Search Modal Trigger */}
        <button className="btn-toolbar" onClick={onSearch} title="Rechercher (F3)">
          <Search size={15} />
          <span>Recherche</span>
        </button>

        {/* New Timesheet */}
        <button className="btn-toolbar" onClick={onNew} title="Créer une nouvelle feuille">
          <Plus size={15} />
          <span>Nouvelle</span>
        </button>

        {/* Save - only enabled if isEditable and isDirty */}
        <button
          className={`btn-toolbar ${isDirty && isEditable ? 'btn-primary' : ''}`}
          disabled={!isEditable || !isDirty}
          onClick={onSave}
          title={!isEditable ? 'Feuille verrouillée (non modifiable)' : 'Sauvegarder (Ctrl+S)'}
        >
          <Save size={15} />
          <span>Sauvegarder</span>
          {isDirty && isEditable && (
            <span style={{ width: 6, height: 6, borderRadius: '50%', background: '#fbbf24' }} />
          )}
        </button>

        {/* Validate / Calculate Process */}
        <button className="btn-toolbar" onClick={onValidate} title="Valider et calculer les totaux">
          <CheckCircle2 size={15} color="#34d399" />
          <span>Valider</span>
        </button>

        {/* Delete - only allowed if isEditable (Initial status) */}
        <button
          className="btn-toolbar btn-danger"
          disabled={!isEditable}
          onClick={onDelete}
          title={!isEditable ? 'Suppression possible uniquement pour une feuille au statut Initiale' : 'Supprimer la feuille'}
        >
          <Trash2 size={15} />
          <span>Supprimer</span>
        </button>

        <div className="toolbar-divider" />

        {/* Export to Excel (cmd_export) */}
        <button className="btn-toolbar" onClick={onExport} title="Exporter vers Excel (.xlsx)">
          <FileSpreadsheet size={15} color="#10b981" />
          <span>Export Excel</span>
        </button>

        <div className="toolbar-divider" />

        {/* Chat / Attachment indicators */}
        <button
          className="btn-toolbar btn-nav"
          title={hasChat ? 'Discussion active' : 'Pas de discussion'}
          style={{ opacity: hasChat ? 1 : 0.4 }}
        >
          <MessageSquare size={16} color={hasChat ? '#60a5fa' : 'currentColor'} />
        </button>
        <button
          className="btn-toolbar btn-nav"
          title={hasAttachment ? 'Pièces jointes disponibles' : 'Aucune pièce jointe'}
          style={{ opacity: hasAttachment ? 1 : 0.4 }}
        >
          <Paperclip size={16} color={hasAttachment ? '#a78bfa' : 'currentColor'} />
        </button>

        {/* Theme Toggle */}
        <button
          className="btn-toolbar btn-nav"
          onClick={onToggleTheme}
          title="Changer de thème (Sombre / Clair)"
        >
          {theme === 'dark' ? <Sun size={16} /> : <Moon size={16} />}
        </button>
      </div>
    </header>
  );
}
