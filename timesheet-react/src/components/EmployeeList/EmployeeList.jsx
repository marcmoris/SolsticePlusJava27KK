import React from 'react';
import { Search, X, Users, UserCheck } from 'lucide-react';
import './EmployeeList.css';

export default function EmployeeList({
  employees = [],
  selectedEmployeeId,
  onSelectEmployee,
  searchQuery,
  onSearchChange,
  statusFilter,
  onStatusFilterChange,
  sheetFilter,
  onSheetFilterChange,
  currentIndex = 0,
  totalCount = 0,
}) {
  return (
    <aside className="employee-list-panel">
      {/* Header with Search and Status Filter */}
      <div className="panel-header">
        {/* Search Field */}
        <div className="search-input-wrapper">
          <Search size={15} />
          <input
            type="text"
            className="search-input"
            placeholder="Rechercher nom, code..."
            value={searchQuery}
            onChange={(e) => onSearchChange(e.target.value)}
          />
          {searchQuery && (
            <button className="clear-search-btn" onClick={() => onSearchChange('')}>
              <X size={14} />
            </button>
          )}
        </div>

        {/* Active / Inactive Radios */}
        <div className="filter-row">
          <div className="radio-group">
            <label className="radio-label">
              <input
                type="radio"
                name="empStatus"
                checked={statusFilter === 'active'}
                onChange={() => onStatusFilterChange('active')}
              />
              <span>Actifs</span>
            </label>
            <label className="radio-label">
              <input
                type="radio"
                name="empStatus"
                checked={statusFilter === 'inactive'}
                onChange={() => onStatusFilterChange('inactive')}
              />
              <span>Inactifs</span>
            </label>
            <label className="radio-label">
              <input
                type="radio"
                name="empStatus"
                checked={statusFilter === 'all'}
                onChange={() => onStatusFilterChange('all')}
              />
              <span>Tous</span>
            </label>
          </div>
        </div>

        {/* Dropdown Filter for TimeSheet Status (cbFilter from VTimeSheetMain) */}
        <select
          className="status-dropdown"
          value={sheetFilter}
          onChange={(e) => onSheetFilterChange(e.target.value)}
        >
          <option value="ALL">Toutes les feuilles</option>
          <option value="Initial">Initiales</option>
          <option value="Calculated">Calculées</option>
          <option value="Validated">Validées</option>
          <option value="Approved">Approuvées</option>
          <option value="Issued">Émises</option>
          <option value="Error">En erreur</option>
          <option value="Transferred">Transférées</option>
          <option value="Cancelled">Annulées</option>
          <option disabled>──────────────</option>
          <option value="TYPE_Regular">Type: Régulière</option>
          <option value="TYPE_Adjustment">Type: Ajustement</option>
          <option value="TYPE_Complementary">Type: Complémentaire</option>
          <option value="TYPE_Advance">Type: Avance</option>
        </select>
      </div>

      {/* Employees Scroll List */}
      <div className="employees-scroll-list">
        {employees.length === 0 ? (
          <div className="empty-state">
            <Users size={28} />
            <span>Aucun employé trouvé</span>
          </div>
        ) : (
          employees.map((emp, index) => {
            const isSelected = emp.id === selectedEmployeeId;
            return (
              <div
                key={emp.id}
                className={`employee-card ${isSelected ? 'selected' : ''}`}
                onClick={() => onSelectEmployee(emp.id)}
              >
                <div className="card-top">
                  <span className="emp-code">{emp.value}</span>
                  {!emp.isActive && (
                    <span style={{ fontSize: '0.65rem', color: '#f87171' }}>Inactif</span>
                  )}
                </div>
                <div className="emp-name">{emp.name}</div>
                <div className="emp-dept">{emp.department}</div>
              </div>
            );
          })
        )}
      </div>

      {/* Footer Status Counter */}
      <footer className="panel-footer">
        <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
          <UserCheck size={14} color="var(--primary-500)" />
          <span>
            Position : <strong>{totalCount > 0 ? currentIndex + 1 : 0}</strong> / {totalCount}
          </span>
        </div>
      </footer>
    </aside>
  );
}
