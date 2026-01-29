import React, { useState } from 'react';
import { FileText, Plus, X, Check, Trash2 } from 'lucide-react';

const DocumentList = ({ documents, onCreate, onSelect, onDelete }) => {
  const [isCreating, setIsCreating] = useState(false);
  const [newDocName, setNewDocName] = useState('');

  const handleCreateClick = () => {
    setIsCreating(true);
    setNewDocName('');
  };

  const handleConfirmCreate = () => {
    if (newDocName.trim()) {
      onCreate(newDocName);
      setIsCreating(false);
    }
  };

  const handleCancelCreate = () => {
    setIsCreating(false);
  };

  return (
    <div className="document-list-container">
      <div className="toolbar">
        <h1>Documentele Mele</h1>
        {!isCreating ? (
          <button onClick={handleCreateClick} className="primary-btn">
              <Plus size={16} style={{marginRight: '5px'}}/> Document Nou
          </button>
        ) : (
          <div className="create-doc-input-group" style={{ display: 'flex', gap: '10px', alignItems: 'center' }}>
            <input
              type="text"
              placeholder="Nume document..."
              value={newDocName}
              onChange={(e) => setNewDocName(e.target.value)}
              className="input-field"
              autoFocus
            />
            <button onClick={handleConfirmCreate} className="primary-btn" style={{ padding: '8px' }} title="Confirmă">
              <Check size={18} />
            </button>
            <button onClick={handleCancelCreate} className="tool-btn" style={{ padding: '8px', color: 'var(--danger-color)' }} title="Anulează">
              <X size={18} />
            </button>
          </div>
        )}
      </div>

      <div className="document-grid">
        {documents.map(doc => (
          <div key={doc.documentId} className="document-card-container" style={{ position: 'relative' }}>
            <div onClick={() => onSelect(doc.documentId)} className="document-card">
              <div className="doc-icon"><FileText size={40} /></div>
              <div className="doc-title">{doc.name || "Fără Nume"}</div>
              <div className="doc-meta">Click pentru a edita</div>
            </div>
            
            {/* Buton Ștergere */}
            <button 
              className="delete-document-btn"
              title="Șterge document"
              onClick={(e) => {
                e.stopPropagation(); // Previne declanșarea onSelect
                if (window.confirm(`Ești sigur că vrei să ștergi "${doc.name || 'acest document'}"?`)) {
                  onDelete(doc.documentId);
                }
              }}
              style={{
                position: 'absolute',
                top: '10px',
                right: '10px',
                background: 'rgba(255, 0, 0, 0.1)',
                border: 'none',
                borderRadius: '4px',
                padding: '5px',
                cursor: 'pointer',
                color: '#ff4d4d',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center'
              }}
            >
              <Trash2 size={16} />
            </button>
          </div>
        ))}
        {documents.length === 0 && !isCreating && (
          <div className="empty-state">Nu ai niciun document creat încă.</div>
        )}
      </div>
    </div>
  );
};

export default DocumentList;