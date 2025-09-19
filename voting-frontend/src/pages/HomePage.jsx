import React, { useEffect, useState } from "react";
import Layout from "./Layout";

export default function HomePage() {
  const [contests, setContests] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8000/api/contests")
      .then((res) => {
        if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
        return res.json();
      })
      .then(setContests)
      .catch((err) => console.error("Failed to fetch contests:", err));
  }, []);

  const sortedContests = [...contests].sort(
    (a, b) => new Date(a.startDateTime) - new Date(b.startDateTime)
  );

  return (
    <Layout>
      {/* Middle table */}
      <div className="flex-grow-1 m-3 text-center">
        <h3>Concursos</h3>
        <div className="table-responsive">
          <table className="table mt-3 mb-0">
            <thead>
              <tr>
                <th>Título</th>
                <th>Nome concorrente</th>
                <th>Resultado (€)</th>
                <th>Data</th>
              </tr>
            </thead>
            <tbody>
              {sortedContests.map((contest) => (
                <tr key={contest.id}>
                  <td>{contest.title}</td>
                  <td>{contest.contestContestants?.map(cc => cc.contestant?.name).join(", ") || "Nenhum"}</td>
                  <td>{contest.prize} €</td>
                  <td>{new Date(contest.startDateTime).toLocaleDateString()}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Betting Options & Stats */}
      <div className="col-12 col-lg-4">
        <div className="card bg-custom text-white text-center shadow-sm m-5">
          <div className="card-body">
            <h5 className="mb-3">Aposta hoje...</h5>
            <div className="d-flex flex-wrap justify-content-center gap-2">
              {[0, 200, 500, 1000, 3000, 10000, 50000].map((val) => (
                <button key={val} className="btn btn-outline-warning">{val.toLocaleString()}€</button>
              ))}
            </div>
          </div>
        </div>

        <div className="card bg-custom text-white text-center shadow-sm m-5">
          <div className="card-body">
            <h5>Estatísticas</h5>
            <p><strong>As minhas estatísticas:</strong></p>
            <div className="d-flex flex-column gap-2">
              <button className="btn btn-outline-warning">2,5 acertos por semana</button>
              <button className="btn btn-outline-warning">20 / 30 acertos no total</button>
            </div>
            <hr />
            <p><strong>Estatísticas gerais:</strong></p>
            <div className="d-flex flex-column gap-2">
              <button className="btn btn-outline-warning">200€ resultado mais frequente</button>
              <button className="btn btn-outline-warning">Mais alguma coisa</button>
            </div>
          </div>
        </div>
      </div>
    </Layout>
  );
}
