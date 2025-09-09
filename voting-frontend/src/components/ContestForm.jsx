import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function CreateContest() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    title: "",
    description: "",
    startDateTime: "",
    endDateTime: "",
    prize: 0,
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    fetch("http://localhost:8000/api/contests", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(formData),
    })
      .then((res) => res.json())
      .then(() => navigate("/"));
  };

  return (
    <div>
      <h1>Criar Novo Concurso</h1>
      <form onSubmit={handleSubmit}>
        <label>Título:</label>
        <input type="text" name="title" value={formData.title} onChange={handleChange} /><br />

        <label>Descrição:</label>
        <textarea name="description" value={formData.description} onChange={handleChange}/><br />

        <label>Data e hora de início:</label>        
        <input type="datetime-local" name="startDateTime" value={formData.startDateTime} onChange={handleChange} /><br />
        
        <label>Data e hora de fim:</label>    
        <input type="datetime-local" name="endDateTime" value={formData.endDateTime} onChange={handleChange} /><br />
        
        <label>Resultado final (€):</label> 
        <input type="number" name="prize" placeholder="Prize" value={formData.prize} onChange={handleChange}/><br />
        
        <button type="submit"> Criar Concurso</button>
      </form>
    </div>
  );
}
