// src/App.jsx
import { useState } from "react";
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import VotePage from './pages/VotePage';
import LoginRegisterPage from "./pages/LoginRegisterPage";

function NotFound() {
  return (
    <div style={{ padding: 20, textAlign: 'center' }}>
      <h1>404 - Page Not Found</h1>
      <p>The page you're looking for doesn't exist.</p>
    </div>
  );
}

function App() {
  const [token, setToken] = useState(null);
  
  return (
    <Router>
      <Routes>
        <Route path="/" element={<VotePage token={token} key={token}/>} />
        <Route path="/auth" element={<LoginRegisterPage setToken={setToken} />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
    </Router>
  );
}

export default App;