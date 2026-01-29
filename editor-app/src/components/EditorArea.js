import React, { useEffect, useRef } from 'react';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { getAccessToken } from '../services/authService';
import { ArrowLeft, Save } from 'lucide-react';

const EditorArea = ({ docId, content, setContent, onSave, onBack }) => {
  const stompClient = useRef(null);

  useEffect(() => {
    if (!docId) return;

    const socket = new SockJS('http://localhost:8080/editor-ws');
    stompClient.current = new Client({
      webSocketFactory: () => socket,
      connectHeaders: { 'Authorization': `Bearer ${getAccessToken()}` },
      onConnect: () => console.log("WebSocket Conectat pentru documentul:", docId),
      onStompError: (frame) => console.error("Eroare STOMP:", frame)
    });

    stompClient.current.activate();

    return () => {
      if (stompClient.current) stompClient.current.deactivate();
    };
  }, [docId]);

  const handleChange = (html) => {
    setContent(html);
    if (stompClient.current && stompClient.current.connected) {
      stompClient.current.publish({
        destination: `/app/update/${docId}`,
        body: html
      });
    }
  };

  return (
    <div className="editor-wrapper">
      <div className="editor-header">
        <button onClick={onBack} className="tool-btn back-btn">
             <ArrowLeft size={16} /> Înapoi
        </button>
        <button onClick={onSave} className="primary-btn save-btn">
             <Save size={16} /> Salvează în Baza de Date
        </button>
      </div>
      <div className="editor-container">
        <ReactQuill
            theme="snow"
            value={content}
            onChange={handleChange}
            className="quill-editor"
        />
      </div>
    </div>
  );
};

export default EditorArea;
