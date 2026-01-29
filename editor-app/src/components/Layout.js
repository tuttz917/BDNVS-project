import React from 'react';
import { Moon, Sun } from 'lucide-react';

const Layout = ({ children, theme, toggleTheme }) => {
  return (
    <div className={`app-container ${theme === 'dark' ? 'dark-mode' : ''}`}>
      <header className="app-header">
        <div className="logo">Editor App</div>
        <button onClick={toggleTheme} className="theme-toggle" title="Toggle Theme">
          {theme === 'dark' ? <Sun size={20} /> : <Moon size={20} />}
        </button>
      </header>
      <main className="app-content">
        {children}
      </main>
    </div>
  );
};

export default Layout;
