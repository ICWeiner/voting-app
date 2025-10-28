import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Layout from "../pages/Layout";

export default function ContestContestantDetail() {
  const { id } = useParams(); // contest id
  const navigate = useNavigate();
  const [contest, setContest] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchContest = async () => {
      try {
        const res = await fetch(`http://localhost:8000/api/contests/${id}`);
        if (!res.ok) throw new Error("Erro ao carregar concurso");
        const data = await res.json();
        setContest(data);
      } catch (err) {
        console.error(err);
        alert("Falha ao carregar concurso: " + err.message);
      } finally {
        setLoading(false);
      }
    };
    fetchContest();
  }, [id]);

  const handleDelete = async () => {
    if (!window.confirm("Tem certeza que quer eliminar este concurso e os concorrentes relacionados?")) return;
    try {
      const res = await fetch(`http://localhost:8000/api/contests/delete/${id}`, {
        method: "DELETE"
      });
      if (!res.ok) throw new Error("Erro ao eliminar concurso");
      alert("Concurso eliminado com sucesso!");
      navigate("/");
    } catch (err) {
      console.error(err);
      alert("Falha ao eliminar concurso: " + err.message);
    }
  };

  if (loading) {
    return (
      <Layout>
        <div className="container my-4 text-center text-custom">
          <p>A carregar...</p>
        </div>
      </Layout>
    );
  }

  if (!contest) {
    return (
      <Layout>
        <div className="container my-4 text-center text-danger">
          <p>Concurso não encontrado</p>
        </div>
      </Layout>
    );
  }

  const { title, description, startDateTime, endDateTime, prize, contestContestants } = contest;

  const formatDateTime = (date) => {
    const d = new Date(date);
    return d.toLocaleString([], { dateStyle: "short", timeStyle: "short" });
  };

  const main = contestContestants?.find(c => !c.isSuperJoker);
  const superJoker = contestContestants?.find(c => c.isSuperJoker);

  return (
    <Layout>
      <div className="container my-4">
          <h2 className="text-center mb-4 text-custom">Detalhes do Concurso</h2>

        {/* Contest info */}
        <div className="card shadow-sm border-warning mb-4">
          <div className="card-header bg-custom text-white fw-bold">Informação do Concurso</div>
          <div className="card-body bg-light text-custom">
            <p><strong>Título:</strong> {title}</p>
            <p><strong>Descrição:</strong> {description}</p>
            <p><strong>Início:</strong> {formatDateTime(startDateTime)}</p>
            <p><strong>Fim:</strong> {formatDateTime(endDateTime)}</p>
            <p><strong>Prémio:</strong> {prize != null ? `${prize} €` : "---"}</p>
          </div>
        </div>

        {/* Contestants */}
        <div className="row">
          {main && (
            <div className="col-md-6 mb-4">
              <div className="card shadow-sm border-warning">
                <div className="card-header bg-custom text-white fw-bold">Concorrente Principal</div>
                <div className="card-body bg-light text-custom">
                  <p><strong>Nome:</strong> {main.contestant?.name}</p>
                  <p><strong>Profissão:</strong> {main.contestant?.profession || "---"}</p>
                  <p><strong>Idade:</strong> {main.contestant?.age || "---"}</p>
                  <p><strong>Estudos:</strong> {main.contestant?.studies || "---"}</p>
                  <p><strong>Notas:</strong> {main.contestant?.notes || "---"}</p>
                </div>
              </div>
            </div>
          )}
          {superJoker && (
            <div className="col-md-6 mb-4">
              <div className="card shadow-sm border-warning">
                <div className="card-header bg-custom text-white fw-bold">Concorrente Super Joker</div>
                <div className="card-body bg-light text-custom">
                  <p><strong>Nome:</strong> {superJoker.contestant?.name}</p>
                  <p><strong>Profissão:</strong> {superJoker.contestant?.profession || "---"}</p>
                  <p><strong>Idade:</strong> {superJoker.contestant?.age || "---"}</p>
                  <p><strong>Estudos:</strong> {superJoker.contestant?.studies || "---"}</p>
                  <p><strong>Notas:</strong> {superJoker.contestant?.notes || "---"}</p>
                </div>
              </div>
            </div>
          )}
        </div>

        {/* Actions */}
        <div className="text-center mt-4">
          <button className="btn btn-warning me-2 text-custom" onClick={() => navigate(`/contest-contestants/edit/${id}`)}>Editar</button>
          <button className="btn btn-danger me-2" onClick={handleDelete}>Eliminar</button>
          <button className="btn btn-custom" onClick={() => navigate("/")}>Voltar</button>
        </div>
      </div>
    </Layout>
  );
}
