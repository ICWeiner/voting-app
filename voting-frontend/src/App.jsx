// src/App.jsx
import { useState } from "react";
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import VotePage from './pages/VotePage';
import LoginRegisterPage from "./pages/LoginRegisterPage";
import HomePage from "./pages/HomePage";
import CreateContestContestantForm from "./components/CreateContestContestantForm";
import EditContestContestantForm from "./components/EditContestContestantForm";
import ContestContestantDetail from "./components/ContestContestantDetail";
import Navbar from "./components/NavBar";

function NotFound() {
  return (
    <div style={{ padding: 20, textAlign: 'center' }}>
      <h1>404 - Page Not Found</h1>
      <p>The page you're looking for doesn't exist.</p>
    </div>
  );
}

function App() {
  
  return (
    <Router>
      <Navbar /> 
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/auth" element={<LoginRegisterPage/>} />
        <Route path="*" element={<NotFound />} />
        <Route path="/contest-contestants/create" element={<CreateContestContestantForm />} />
        <Route path="/contest-contestants/edit/:id" element={<EditContestContestantForm />} />
        <Route path="/contest-contestants/detail/:id" element={<ContestContestantDetail />} />
      </Routes>
    </Router>
  );
}

export default App;