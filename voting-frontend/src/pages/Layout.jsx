import React, { useState } from "react";
import "../css/Home.css";
import jokerLogo from "../../../docs/joker_logo.png";

export default function Layout({ children }) {
  const [showSidebar, setShowSidebar] = useState(false);

  return (
    <div className="d-flex flex-column flex-md-row min-vh-100">
      {/* Sidebar for md+ screens */}
      <div className="bg-custom text-white p-3 flex-shrink-0 text-center d-none d-md-block" style={{ minWidth: "200px", maxWidth: "250px" }}>
        <img className="joker-logo" src={jokerLogo} alt="Joker logo"/>
        <ul className="nav flex-column">
          <li className="nav-item"><a href="#" className="nav-link text-white">Concursos</a></li>
          <li className="nav-item"><a href="#" className="nav-link text-white">Histórico Apostas</a></li>
          <li className="nav-item"><a href="#" className="nav-link text-white">Apostas Comunidade</a></li>
          <li className="nav-item"><a href="#" className="nav-link text-white">Ranking</a></li>
          <li className="nav-item"><a href="#" className="nav-link text-white">Estatísticas</a></li>
          <hr className="bg-warning" />
          <li className="nav-item"><a href="#" className="nav-link text-white">Perfil</a></li>
          <li className="nav-item"><a href="#" className="nav-link text-white">Notificações</a></li>
          <li className="nav-item"><a href="#" className="nav-link text-white">Definições</a></li>
          <li className="nav-item"><a href="#" className="nav-link text-white">FAQ</a></li>
        </ul>
      </div>

      {/* Main content */}
      <div className="flex-grow-1 d-flex flex-column">
        {/* Top Navbar */}
        <nav className="navbar px-3 m-2 w-100 flex-wrap align-items-center">
          {/* Sidebar toggle button for mobile */}
          <button className="btn btn-outline-warning d-md-none mb-2" onClick={() => setShowSidebar(true)}>
            ☰
          </button>

          {/* Search bar */}
          <div className="d-flex flex-grow-1 me-3 bg-custom p-2 rounded mb-2 mb-md-0">
            <input className="form-control me-2" type="search" placeholder="Pesquisar..." />
            <button className="btn btn-outline-warning" type="submit">🔍</button>
          </div>

          {/* Icons on the right */}
          <div className="ms-auto d-flex gap-3 align-items-center">
            <span className="text-dark">❓</span>
            <span className="text-dark">🔔</span>
            <span className="text-dark">⚙️</span>
            <span className="text-dark">👤</span>
          </div>
        </nav>

        {/* Mobile Offcanvas Sidebar */}
        {showSidebar && (
          <div className="offcanvas-mobile bg-custom text-white p-3 position-fixed top-0 start-0 vh-100" style={{ width: "250px", zIndex: 1050 }}>
            <button className="btn btn-outline-warning mb-3" onClick={() => setShowSidebar(false)}>✕ Fechar</button>
            <img className="joker-logo" src={jokerLogo} alt="Joker logo"/>
            <ul className="nav flex-column">
              <li className="nav-item"><a href="#" className="nav-link text-white">Concursos</a></li>
              <li className="nav-item"><a href="#" className="nav-link text-white">Histórico Apostas</a></li>
              <li className="nav-item"><a href="#" className="nav-link text-white">Apostas Comunidade</a></li>
              <li className="nav-item"><a href="#" className="nav-link text-white">Ranking</a></li>
              <li className="nav-item"><a href="#" className="nav-link text-white">Estatísticas</a></li>
              <hr className="bg-warning" />
              <li className="nav-item"><a href="#" className="nav-link text-white">Perfil</a></li>
              <li className="nav-item"><a href="#" className="nav-link text-white">Notificações</a></li>
              <li className="nav-item"><a href="#" className="nav-link text-white">Definições</a></li>
              <li className="nav-item"><a href="#" className="nav-link text-white">FAQ</a></li>
            </ul>
          </div>
        )}

        {/* Page content */}
        <div className="d-flex flex-grow-1 flex-column flex-md-row px-0">
          {children}
        </div>

        {/* Footer */}
        <footer className="bg-custom text-center text-white py-2 mt-auto w-100">
          <small>Copyright © {(new Date().getFullYear())}</small>
        </footer>
      </div>
    </div>
  );
}
