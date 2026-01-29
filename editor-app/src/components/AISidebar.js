import React, { useState } from 'react';
import { Search, PenTool, MessageSquare, AlertCircle, Globe, Zap } from 'lucide-react';

const AISidebar = ({ onCallPipeline, aiResponse, aiLoading, contextContent }) => {
  const [targetText, setTargetText] = useState('');

  return (
    <div className="sidebar">
      <h3>AI Assistant</h3>
      <div className="sidebar-section">
        <p className="label">Pipelines Globale:</p>
        <button onClick={() => onCallPipeline('grammar-check', { content: contextContent })} className="ai-btn">
            <PenTool size={16} /> Verificare Gramatică
        </button>
      </div>

      <hr className="divider"/>

      <div className="sidebar-section">
        <p className="label">Text Selectat / Target:</p>
        <textarea
            rows="3"
            placeholder="Scrie sau selectează un paragraf..."
            value={targetText}
            onChange={e => setTargetText(e.target.value)}
            className="sidebar-textarea"
        />
        <div className="sidebar-section" style={{ gap: '8px' }}>
             <button onClick={() => onCallPipeline('fact-check', { content: targetText })} className="ai-btn">
                <Search size={16} /> Fact-Check (Target)
            </button>
             <button onClick={() => onCallPipeline('source-provide', { content: targetText })} className="ai-btn">
                <Globe size={16} /> Source Provide
            </button>
             <button onClick={() => onCallPipeline('target-enhance', { context: contextContent, target: targetText })} className="ai-btn">
                <Zap size={16} /> Target Enhance
            </button>
        </div>

        <div className="btn-group" style={{marginTop: '10px'}}>
          <button onClick={() => onCallPipeline('argument-add', { context: contextContent, target: targetText })} className="tool-btn">
             <MessageSquare size={14} /> Argument (+)
          </button>
          <button onClick={() => onCallPipeline('argument-contradict', { context: contextContent, target: targetText })} className="tool-btn">
             <AlertCircle size={14} /> Argument (-)
          </button>
        </div>
      </div>

      <div className="terminal-output">
        <strong>Output AI:</strong>
        {aiLoading ? (
            <div className="loading-spinner">Se analizează...</div>
        ) : (
            <div className="json-output">
                {aiResponse ? JSON.stringify(aiResponse, null, 2) : "Așteptând comenzi..."}
            </div>
        )}
      </div>
    </div>
  );
};

export default AISidebar;
