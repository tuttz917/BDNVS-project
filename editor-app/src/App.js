import React, { useState, useEffect } from 'react';
import { BrowserRouter, Routes, Route, Navigate, useNavigate, useParams } from 'react-router-dom';
import './App.css';
import { fetchDocuments, createDocument, getDocument, saveDocument, deleteDocument, callAiPipeline, refreshAccessToken } from './services/api';
import Layout from './components/Layout';
import AuthForm from './components/AuthForm';
import DocumentList from './components/DocumentList';
import EditorArea from './components/EditorArea';
import AISidebar from './components/AISidebar';

// Wrapper pentru Editor pentru a utiliza useParams și a încărca conținutul
const EditorPage = ({
  isAuthenticated,
  content,
  setContent,
  handleSaveToDb,
  handleCallAiPipeline,
  aiResponse,
  aiLoading,
  loadDocumentContent
}) => {
  const { id } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    if (isAuthenticated && id) {
      loadDocumentContent(id);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id, isAuthenticated]);

  if (!isAuthenticated) return <Navigate to="/login" />;

  return (
    <div className="workspace">
      <EditorArea
        docId={id}
        content={content}
        setContent={setContent}
        onSave={() => handleSaveToDb(id)}
        onBack={() => navigate('/')}
      />
      <AISidebar
        onCallPipeline={handleCallAiPipeline}
        aiResponse={aiResponse}
        aiLoading={aiLoading}
        contextContent={content}
      />
    </div>
  );
};

// Wrapper pentru lista de documente pentru a gestiona navigarea
const DocumentListWrapper = ({ documents, onCreate, onDelete }) => {
    const navigate = useNavigate();
    return (
        <DocumentList
            documents={documents}
            onCreate={(name) => onCreate(navigate, name)}
            onSelect={(id) => navigate(`/document/${id}`)}
            onDelete={onDelete}
        />
    );
}

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isAuthChecking, setIsAuthChecking] = useState(true);
  const [documents, setDocuments] = useState([]);
  const [content, setContent] = useState('');
  const [aiLoading, setAiLoading] = useState(false);
  const [aiResponse, setAiResponse] = useState(null);
  const [theme, setTheme] = useState('dark');

  const toggleTheme = () => {
    setTheme(prev => prev === 'light' ? 'dark' : 'light');
  };

  useEffect(() => {
    const initAuth = async () => {
      try {
        const token = await refreshAccessToken();
        if (token) {
          setIsAuthenticated(true);
        }
      } catch (error) {
        console.error("Auto-login failed:", error);
      } finally {
        setIsAuthChecking(false);
      }
    };
    initAuth();
  }, []);

  const loadDocuments = () => {
    fetchDocuments()
      .then(data => setDocuments(Array.isArray(data) ? data : []))
      .catch(err => console.error("Eroare incarcare documente:", err));
  };

  useEffect(() => {
    if (isAuthenticated) {
        loadDocuments();
    }
  }, [isAuthenticated]);

  const loadDocumentContent = (id) => {
      getDocument(id)
        .then(data => setContent(data))
        .catch(err => console.error("Eroare incarcare document:", err));
  };

  const handleCreateDocument = (navigate, name) => {
    createDocument(name)
        .then(doc => {
            setDocuments(prev => [...prev, doc]);
            navigate(`/document/${doc.documentId}`);
        })
        .catch(err => alert("Eroare creare document: " + err.message));
  };

  const handleDeleteDocument = (id) => {
    deleteDocument(id)
        .then(() => {
            // Eliminăm documentul din starea locală după succesul API
            setDocuments(prev => prev.filter(d => d.documentId !== id));
        })
        .catch(err => alert("Eroare la ștergerea documentului: " + err.message));
  };

  const handleSaveToDb = (id) => {
    saveDocument(id)
      .then(() => alert("Documentul a fost salvat permanent în baza de date!"))
      .catch(err => alert("Eroare la salvare: " + err.message));
  };

  const handleCallAiPipeline = (endpoint, payload) => {
    setAiLoading(true);
    setAiResponse(null);
    callAiPipeline(endpoint, payload)
      .then(data => {
        setAiResponse(data);
        setAiLoading(false);
      })
      .catch(() => {
        setAiLoading(false);
        setAiResponse({ error: "Eroare Pipeline AI." });
      });
  };

  if (isAuthChecking) {
    return <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>Se verifică autentificarea...</div>;
  }

  return (
    <BrowserRouter>
      <Layout theme={theme} toggleTheme={toggleTheme}>
        <Routes>
          <Route path="/login" element={
            !isAuthenticated ? <AuthForm onLoginSuccess={() => setIsAuthenticated(true)} /> : <Navigate to="/" />
          } />

          <Route path="/" element={
            isAuthenticated ? (
              <DocumentListWrapper
                documents={documents}
                onCreate={handleCreateDocument}
                onDelete={handleDeleteDocument}
              />
            ) : <Navigate to="/login" />
          } />

          <Route path="/document/:id" element={
            <EditorPage
                isAuthenticated={isAuthenticated}
                content={content}
                setContent={setContent}
                handleSaveToDb={handleSaveToDb}
                handleCallAiPipeline={handleCallAiPipeline}
                aiResponse={aiResponse}
                aiLoading={aiLoading}
                loadDocumentContent={loadDocumentContent}
            />
          } />
        </Routes>
      </Layout>
    </BrowserRouter>
  );
}

export default App;