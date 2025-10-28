import React, { useState, useEffect } from "react";
import Layout from "../pages/Layout";
import { useNavigate, useParams } from "react-router-dom";

const EditContestContestantForm = () => {
  const navigate = useNavigate();
  const { id } = useParams();

  const [contest, setContest] = useState({
    title: "",
    description: "",
    startDateTime: "",
    endDateTime: "",
    prize: ""
  });

  const [contestant1, setContestant1] = useState({
    id: null,
    name: "",
    profession: "",
    age: "",
    studies: "",
    notes: ""
  });

  const [contestant2, setContestant2] = useState({
    id: null,
    name: "",
    profession: "",
    age: "",
    studies: "",
    notes: ""
  });

  const [allowCustomEndDate, setAllowCustomEndDate] = useState(false);

  const formatDateForInput = (date) => {
    const d = new Date(date);
    const pad = (num) => String(num).padStart(2, "0");
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`;
  };

  useEffect(() => {
    const fetchContest = async () => {
      try {
        const res = await fetch(`http://localhost:8000/api/contests/${id}`);
        if (!res.ok) throw new Error("Failed to fetch contest");
        const data = await res.json();

        setContest({
          title: data.title || "",
          description: data.description || "",
          startDateTime: data.startDateTime ? formatDateForInput(data.startDateTime) : "",
          endDateTime: data.endDateTime ? formatDateForInput(data.endDateTime) : "",
          prize: data.prize != null ? data.prize : ""
        });

        const cc = data.contestContestants || [];
        const main = cc.find(c => !c.isSuperJoker);
        const superJoker = cc.find(c => c.isSuperJoker);

        if (main) setContestant1({
          id: main.contestant.id,
          name: main.contestant.name || "",
          profession: main.contestant.profession || "",
          age: main.contestant.age != null ? main.contestant.age : "",
          studies: main.contestant.studies || "",
          notes: main.contestant.notes || ""
        });

        if (superJoker) setContestant2({
          id: superJoker.contestant.id,
          name: superJoker.contestant.name || "",
          profession: superJoker.contestant.profession || "",
          age: superJoker.contestant.age != null ? superJoker.contestant.age : "",
          studies: superJoker.contestant.studies || "",
          notes: superJoker.contestant.notes || ""
        });
      } catch (err) {
        console.error(err);
        alert("Erro ao carregar concurso: " + err.message);
      }
    };

    fetchContest();
  }, [id]);

  const handleContestChange = (e) => setContest({ ...contest, [e.target.name]: e.target.value });
  const handleContestant1Change = (e) => setContestant1({ ...contestant1, [e.target.name]: e.target.value });
  const handleContestant2Change = (e) => setContestant2({ ...contestant2, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await fetch(`http://localhost:8000/api/contests/edit/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ ...contest, prize: contest.prize !== "" ? Number(contest.prize) : null })
      });

      if (contestant1.id) {
        await fetch(`http://localhost:8000/api/contestants/edit/${contestant1.id}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            name: contestant1.name || null,
            profession: contestant1.profession || null,
            age: contestant1.age !== "" ? Number(contestant1.age) : null,
            studies: contestant1.studies !== "" ? contestant1.studies : null,
            notes: contestant1.notes || null
          })
        });
      }

      if (contestant2.id) {
        await fetch(`http://localhost:8000/api/contestants/edit/${contestant2.id}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            name: contestant2.name || null,
            profession: contestant2.profession || null,
            age: contestant2.age !== "" ? Number(contestant2.age) : null,
            studies: contestant2.studies !== "" ? contestant2.studies : null,
            notes: contestant2.notes || null
          })
        });
      }

      alert("Concurso atualizado com sucesso!");
      navigate("/");
    } catch (err) {
      console.error(err);
      alert("Erro ao atualizar concurso: " + err.message);
    }
  };

  return (
    <Layout>
      <div className="container my-4">
        <form onSubmit={handleSubmit}>
          <h2 className="text-center mb-4 text-custom">Editar Concurso</h2>

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
                <select name="prize" className="form-select" value={contest.prize} onChange={handleContestChange}>
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

          {/* Contestants */}
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
            <button type="submit" className="btn btn-custom border-warning">Atualizar Concurso</button>
          </div>
        </form>
      </div>
    </Layout>
  );
};

export default EditContestContestantForm;
