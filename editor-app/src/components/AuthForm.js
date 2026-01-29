import React, { useState } from 'react';
import { login, register } from '../services/api';
import { setAccessToken } from '../services/authService';

const AuthForm = ({ onLoginSuccess }) => {
  const [isLoginView, setIsLoginView] = useState(true);
  const [authData, setAuthData] = useState({
    username: '', password: '', firstName: '', lastName: '', email: '', confirmPassword: ''
  });
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      let data;
      if (isLoginView) {
        data = await login(authData.username, authData.password);
      } else {
        // Validare simplă pentru confirmare parolă pe frontend
        if (authData.password !== authData.confirmPassword) {
          throw new Error("Parolele nu coincid!");
        }

        data = await register({
          firstName: authData.firstName,
          lastName: authData.lastName,
          email: authData.email,
          userName: authData.username,
          password: authData.password,
          confirmPassword: authData.confirmPassword
        });
      }

      // Backend-ul returnează: { "acces_token": "..." }
      if (data && data.acces_token) {
        setAccessToken(data.acces_token);
        onLoginSuccess();
      } else {
        throw new Error("Răspuns invalid de la server.");
      }
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-wrapper">
      <div className="auth-card">
        <h2>{isLoginView ? 'Login' : 'Register'}</h2>
        
        {error && <div className="error-message" style={{ color: 'red', marginBottom: '10px' }}>{error}</div>}
        
        <form onSubmit={handleSubmit} className="auth-form">
          {!isLoginView && (
            <>
              <input 
                placeholder="Prenume" 
                className="input-field" 
                required
                onChange={e => setAuthData({...authData, firstName: e.target.value})} 
              />
              <input 
                placeholder="Nume" 
                className="input-field" 
                required
                onChange={e => setAuthData({...authData, lastName: e.target.value})} 
              />
              <input 
                placeholder="Email" 
                type="email"
                className="input-field" 
                required
                onChange={e => setAuthData({...authData, email: e.target.value})} 
              />
            </>
          )}
          
          <input 
            placeholder="Username" 
            className="input-field" 
            required
            onChange={e => setAuthData({...authData, username: e.target.value})} 
          />
          
          <input 
            placeholder="Parolă" 
            type="password" 
            className="input-field" 
            required
            onChange={e => setAuthData({...authData, password: e.target.value})} 
          />
          
          {!isLoginView && (
            <input 
              placeholder="Confirmă Parolă" 
              type="password" 
              className="input-field" 
              required
              onChange={e => setAuthData({...authData, confirmPassword: e.target.value})} 
            />
          )}

          <button 
            type="submit" 
            className="primary-btn full-width" 
            disabled={loading}
          >
            {loading ? 'Se procesează...' : (isLoginView ? 'Intră' : 'Creează cont')}
          </button>
        </form>

        <button 
          onClick={() => {
            setIsLoginView(!isLoginView);
            setError(null);
          }} 
          className="switch-btn"
          style={{ marginTop: '15px', background: 'none', border: 'none', cursor: 'pointer', color: '#007bff' }}
        >
          {isLoginView ? 'Nu ai cont? Creează unul' : 'Ai deja cont? Intră în cont'}
        </button>
      </div>
    </div>
  );
};

export default AuthForm;