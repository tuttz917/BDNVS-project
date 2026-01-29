import { getAccessToken, setAccessToken } from './authService';

const API_BASE_URL = "http://localhost:8080/api/v1";

/**
 * Funcție helper pentru a obține un token nou folosind Cookie-ul de Refresh.
 * Browserul va trimite automat cookie-ul datorită credentials: 'include'.
 */
export const refreshAccessToken = async () => {
  try {
    const response = await fetch(`${API_BASE_URL}/refresh`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include' 
    });

    if (response.ok) {
      const data = await response.json();
      // Salvăm noul token
      localStorage.setItem('accessToken', data.acces_token);
      setAccessToken(data.acces_token);
      return data.acces_token;
    }
    return null;
  } catch (error) {
    console.error("Eroare critică la refresh:", error);
    return null;
  }
};

/**
 * Wrapper peste fetch care adaugă automat token-ul de acces și
 * reîncearcă cererea dacă token-ul a expirat.
 */
const authFetch = async (url, options = {}) => {
  let token = getAccessToken();
  
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers,
  };

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  // Prima încercare a cererii
  let response = await fetch(url, {
    ...options,
    headers,
    credentials: 'include' // Necesar pentru a trimite/primi cookies
  });

  // Dacă serverul răspunde cu 401, înseamnă că Access Token-ul e expirat sau invalid
  if (response.status === 401) {
    console.warn("Access Token expirat. Se încearcă refresh...");
    
    const newToken = await refreshAccessToken();

    if (newToken) {
      // Reîncercăm cererea cu noul token primit
      headers['Authorization'] = `Bearer ${newToken}`;
      response = await fetch(url, {
        ...options,
        headers,
        credentials: 'include'
      });
    } else {
      // Dacă refresh-ul eșuează (401 la refresh), sesiunea e complet expirată
      console.error("Sesiune expirată. Redirecționare la login.");
      // Opțional: window.location.href = '/login'; 
    }
  }

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || `Eroare: ${response.status}`);
  }

  return response;
};

// --- Autentificare ---

export const login = async (username, password) => {
  const response = await fetch(`${API_BASE_URL}/auth`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password }),
    credentials: 'include'
  });
  if (!response.ok) throw new Error("Credențiale invalide.");
  return response.json();
};

export const register = async (userData) => {
  const response = await fetch(`${API_BASE_URL}/register`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(userData),
    credentials: 'include' // Adăugat pentru a primi cookie-ul de refresh și la înregistrare
  });
  if (!response.ok) throw new Error("Eroare la înregistrare.");
  return response.json();
};

// --- Documente ---

export const fetchDocuments = async () => {
  const response = await authFetch(`${API_BASE_URL}/documents`);
  return response.json();
};

export const createDocument = async (name) => {
  const response = await authFetch(`${API_BASE_URL}/create-document`, {
    method: 'POST',
    body: JSON.stringify({ documentName: name })
  });
  return response.json();
};

export const getDocument = async (id) => {
  const response = await authFetch(`${API_BASE_URL}/document/${id}`);
  return response.text();
};

export const saveDocument = async (id) => {
  await authFetch(`${API_BASE_URL}/save-document/${id}`, {
    method: 'POST'
  });
};

export const deleteDocument = async (id) => {
  return await authFetch(`${API_BASE_URL}/delete-document/${id}`, {
    method: 'DELETE'
  });
};

// --- AI & Utilitare ---

const stripHtml = (html) => {
  const doc = new DOMParser().parseFromString(html, 'text/html');
  return doc.body.textContent || "";
};

export const callAiPipeline = async (endpoint, payload) => {
  const cleanPayload = { ...payload };
  if (cleanPayload.content) cleanPayload.content = stripHtml(cleanPayload.content);
  if (cleanPayload.context) cleanPayload.context = stripHtml(cleanPayload.context);
  if (cleanPayload.target) cleanPayload.target = stripHtml(cleanPayload.target);

  const response = await authFetch(`${API_BASE_URL}/${endpoint}`, {
    method: 'POST',
    body: JSON.stringify(cleanPayload)
  });
  return response.json();
};