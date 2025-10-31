import React, { useState, useEffect } from "react";
import Layout from "../pages/Layout";
import { useNavigate } from "react-router-dom";


const CreateContestContestantForm = () => {
  const navigate = useNavigate();

  const getTodayAt21 = () => {
    const now = new Date();
    now.setHours(21, 0, 0, 0);
    const pad = (num) => String(num).padStart(2, "0");
    return `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())}T${pad(now.getHours())}:${pad(now.getMinutes())}`;
  };

  const [contest, setContest] = useState({
    title: "",
    description: "",
    startDateTime: getTodayAt21(), // default to today 21:00
    endDateTime: "",
    prize: ""
  });

  const [contestant1, setContestant1] = useState({
    name: "",
    profession: "",
    age: "",
    studies: "",
    notes: ""
  });

  const [contestant2, setContestant2] = useState({
    name: "",
    profession: "",
    age: "",
    studies: "",
    notes: ""
  });

  const [allowCustomEndDate, setAllowCustomEndDate] = useState(false);

  const formatDateForInput = (date) => {
    const pad = (num) => String(num).padStart(2, "0");
    return `${date.getFullYear()}-${pad(date.getMonth()+1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}`;
  };

  useEffect(() => {
    if (contest.startDateTime && !allowCustomEndDate) {
      const start = new Date(contest.startDateTime);
      const end = new Date(start.getTime() + 60 * 60 * 1000);
      setContest(prev => ({ ...prev, endDateTime: formatDateForInput(end) }));
    }
  }, [contest.startDateTime, allowCustomEndDate]);

  const handleContestChange = (e) => setContest({ ...contest, [e.target.name]: e.target.value });
  const handleContestant1Change = (e) => setContestant1({ ...contestant1, [e.target.name]: e.target.value });
  const handleContestant2Change = (e) => setContestant2({ ...contestant2, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const contestRes = await fetch("http://localhost:8000/contests/create", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ ...contest, prize: contest.prize !== "" ? Number(contest.prize) : null }),
      });
      const contestData = await contestRes.json();
      const contestId = contestData.id;

      const contestant1Res = await fetch("http://localhost:8000/contestants/create", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ ...contestant1, age: contestant1.age !== "" ? Number(contestant1.age) : null, studies: contestant1.studies || null }),
      });
      const contestant1Data = await contestant1Res.json();

      await fetch("http://localhost:8000/contest-contestants/create", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ contestId, contestantId: contestant1Data.id, isSuperJoker: false }),
      });

      const contestant2Res = await fetch("http://localhost:8000/contestants/create", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ ...contestant2, age: contestant2.age !== "" ? Number(contestant2.age) : null, studies: contestant2.studies || null }),
      });
      const contestant2Data = await contestant2Res.json();

      await fetch("http://localhost:8000/contest-contestants/create", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ contestId, contestantId: contestant2Data.id, isSuperJoker: true }),
      });

      console.log("Contest and contestants created successfully!"); //TODO improve error and success messages
      setContest({ title: "", description: "", startDateTime: "", endDateTime: "", prize: "" });
      setContestant1({ name: "", profession: "", age: "", studies: "", notes: "" });
      setContestant2({ name: "", profession: "", age: "", studies: "", notes: "" });
      setAllowCustomEndDate(false);
      navigate("/"); //TODO redirect to contest detail page?
    } catch (err) {
      console.error(err);
      alert("Something went wrong: " + err.message); //TODO improve error and success messages
    }
  };

  return (
    <Layout>
      <div className="container my-4">
        <form onSubmit={handleSubmit}>
          <h2 className="text-center mb-4 text-custom">Criar Concurso</h2>

          {/* Contest Info */}
          <div className="card shadow-sm mb-4 border-warning">
            <div className="card-header bg-custom text-white fw-bold">Informação do Concurso</div>
            <div className="card-body bg-light text-custom">
              <div className="mb-3">
                <label className="form-label">Título</label>
                <input type="text" name="title" className="form-control" value={contest.title} onChange={handleContestChange} />
              </div>
              <div className="mb-3">
                <label className="form-label">Descrição</label>
                <textarea name="description" className="form-control" value={contest.description} onChange={handleContestChange} />
              </div>
              <div className="row">
                <div className="mb-3 col-md-6">
                  <label className="form-label">Data e Hora de Início</label>
                  <input type="datetime-local" name="startDateTime" className="form-control" value={contest.startDateTime} onChange={handleContestChange} />
                </div>
                <div className="mb-3 col-md-6">
                  <label className="form-label">Data e Hora de Fim</label>
                  <input type="datetime-local" name="endDateTime" className="form-control" value={contest.endDateTime} onChange={handleContestChange} disabled={!allowCustomEndDate} />
                  <div className="form-check mt-2">
                    <input type="checkbox" className="form-check-input" checked={allowCustomEndDate} onChange={e => setAllowCustomEndDate(e.target.checked)} />
                    <label className="form-check-label">Definir manualmente</label>
                  </div>
                </div>
              </div>
              <div className="mb-3 col-md-4">
                <label className="form-label">Prémio (€)</label>
                <select
                  name="prize"
                  className="form-select"
                  value={contest.prize}
                  onChange={handleContestChange}
                >
                  <option value="">Selecione o prémio</option>
                  <option value={0}>0 €</option>
                  <option value={200}>200 €</option>
                  <option value={500}>500 €</option>
                  <option value={1000}>1.000 €</option>
                  <option value={3000}>3.000 €</option>
                  <option value={10000}>10.000 €</option>
                  <option value={50000}>50.000 €</option>
                </select>
                <small className="text-muted">Se não selecionar, o prémio será considerado desconhecido.</small>
              </div>
            </div>
          </div>

          <div className="row mb-4">
            {/* Contestant 1 */}
            <div className="col-md-6">
              <div className="card shadow-sm border-warning">
                <div className="card-header bg-custom text-white fw-bold">Concorrente 1 (Principal)</div>
                <div className="card-body bg-light text-custom">
                  <div className="mb-2"><label className="form-label">Nome</label><input type="text" name="name" className="form-control" value={contestant1.name} onChange={handleContestant1Change} /></div>
                  <div className="mb-2"><label className="form-label">Profissão</label><input type="text" name="profession" className="form-control" value={contestant1.profession} onChange={handleContestant1Change} /></div>
                  <div className="mb-2"><label className="form-label">Idade</label><input type="number" name="age" min={18} className="form-control" value={contestant1.age} onChange={handleContestant1Change} /></div>
                  <div className="mb-2"><label className="form-label">Estudos</label>
                    <select name="studies" className="form-select" value={contestant1.studies} onChange={handleContestant1Change}>
                      <option value="">Selecione o nível mais alto de estudos</option>
                      <option value="PHD">Doutoramento</option>
                      <option value="MASTER">Mestrado</option>
                      <option value="BACHELOR">Licenciatura</option>
                      <option value="SECONDARY">Ensino Secundário</option>
                      <option value="PRIMARY">Ensino Primário</option>
                      <option value="NONE">Nenhum</option>
                    </select>
                    <small className="text-muted">Se não selecionar, os estudos serão considerados desconhecidos.</small>
                  </div>
                  <div className="mb-2"><label className="form-label">Notas</label><textarea name="notes" className="form-control" value={contestant1.notes} onChange={handleContestant1Change} /></div>
                </div>
              </div>
            </div>

            {/* Contestant 2 */}
            <div className="col-md-6">
              <div className="card shadow-sm border-warning">
                <div className="card-header bg-custom text-white fw-bold">Concorrente 2 (Super Joker)</div>
                <div className="card-body bg-light text-custom">
                  <div className="mb-2"><label className="form-label">Nome</label><input type="text" name="name" className="form-control" value={contestant2.name} onChange={handleContestant2Change} /></div>
                  <div className="mb-2"><label className="form-label">Profissão</label><input type="text" name="profession" className="form-control" value={contestant2.profession} onChange={handleContestant2Change} /></div>
                  <div className="mb-2"><label className="form-label">Idade</label><input type="number" name="age" min={18} className="form-control" value={contestant2.age} onChange={handleContestant2Change} /></div>
                  <div className="mb-2"><label className="form-label">Estudos</label>
                    <select name="studies" className="form-select" value={contestant2.studies} onChange={handleContestant2Change}>
                      <option value="">Selecione o nível mais alto de estudos</option>
                      <option value="PHD">Doutoramento</option>
                      <option value="MASTER">Mestrado</option>
                      <option value="BACHELOR">Licenciatura</option>
                      <option value="SECONDARY">Ensino Secundário</option>
                      <option value="PRIMARY">Ensino Primário</option>
                      <option value="NONE">Nenhum</option>
                    </select>
                    <small className="text-muted">Se não selecionar, os estudos serão considerados desconhecidos.</small>
                  </div>
                  <div className="mb-2"><label className="form-label">Notas</label><textarea name="notes" className="form-control" value={contestant2.notes} onChange={handleContestant2Change} /></div>
                </div>
              </div>
            </div>
          </div>

          <div className="text-center">
            <button type="submit" className="btn btn-custom border-warning">Criar Concurso</button>
          </div>
        </form>
      </div>
    </Layout>
  );
};

export default CreateContestContestantForm;
