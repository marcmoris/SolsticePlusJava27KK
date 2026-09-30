import React, { useState, useMemo } from 'react';
import { Search, X, Check } from 'lucide-react';
import './SearchModal.css';

export default function SearchModal({
  isOpen,
  onClose,
  employees = [],
  onSelectEmployee,
}) {
  const [empCode, setEmpCode] = useState('');
  const [lastName, setLastName] = useState('');
  const [firstName, setFirstName] = useState('');
  const [department, setDepartment] = useState('');

  const filteredResults = useMemo(() => {
    return employees.filter((emp) => {
      if (empCode && !emp.value.toLowerCase().includes(empCode.toLowerCase())) return false;
      if (lastName && !emp.lastName.toLowerCase().includes(lastName.toLowerCase())) return false;
      if (firstName && !emp.firstName.toLowerCase().includes(firstName.toLowerCase())) return false;
      if (department && !emp.department.toLowerCase().includes(department.toLowerCase())) return false;
      return true;
    });
  }, [employees, empCode, lastName, firstName, department]);

  if (!isOpen) return null;

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="search-modal-container" onClick={(e) => e.stopPropagation()}>
        {/* Header */}
        <div className="modal-header">
          <div className="modal-title">
            <Search size={18} color="var(--primary-500)" />
            <span>Recherche Multicritère d'Employé</span>
          </div>
          <button className="modal-close-btn" onClick={onClose}>
            <X size={18} />
          </button>
        </div>

        {/* Body */}
        <div className="modal-body">
          {/* Criteria Inputs */}
          <div className="search-form-grid">
            <div className="form-field">
              <label className="form-label">Numéro / Matricule</label>
              <input
                type="text"
                placeholder="ex: EMP-01042"
                value={empCode}
                onChange={(e) => setEmpCode(e.target.value)}
                autoFocus
              />
            </div>

            <div className="form-field">
              <label className="form-label">Nom</label>
              <input
                type="text"
                placeholder="ex: Tremblay"
                value={lastName}
                onChange={(e) => setLastName(e.target.value)}
              />
            </div>

            <div className="form-field">
              <label className="form-label">Prénom</label>
              <input
                type="text"
                placeholder="ex: Jean"
                value={firstName}
                onChange={(e) => setFirstName(e.target.value)}
              />
            </div>

            <div className="form-field">
              <label className="form-label">Département</label>
              <input
                type="text"
                placeholder="ex: Production, TI..."
                value={department}
                onChange={(e) => setDepartment(e.target.value)}
              />
            </div>
          </div>

          {/* Results List */}
          <div>
            <div style={{ fontSize: '0.75rem', color: 'var(--text-dim)', marginBottom: 6 }}>
              Résultats trouvés : <strong>{filteredResults.length}</strong>
            </div>

            <div className="search-results-table">
              {filteredResults.length === 0 ? (
                <div style={{ padding: 20, textAlign: 'center', color: 'var(--text-dim)', fontSize: '0.82rem' }}>
                  Aucun employé ne correspond aux critères.
                </div>
              ) : (
                filteredResults.map((emp) => (
                  <div
                    key={emp.id}
                    className="result-row"
                    onClick={() => {
                      onSelectEmployee(emp.id);
                      onClose();
                    }}
                  >
                    <span className="result-code">{emp.value}</span>
                    <span className="result-name">{emp.name}</span>
                    <span className="result-dept">{emp.department}</span>
                  </div>
                ))
              )}
            </div>
          </div>
        </div>

        {/* Footer */}
        <div className="modal-footer">
          <button
            className="btn-toolbar"
            onClick={() => {
              setEmpCode('');
              setLastName('');
              setFirstName('');
              setDepartment('');
            }}
          >
            Réinitialiser
          </button>
          <button className="btn-toolbar btn-primary" onClick={onClose}>
            <Check size={14} />
            <span>Fermer</span>
          </button>
        </div>
      </div>
    </div>
  );
}
