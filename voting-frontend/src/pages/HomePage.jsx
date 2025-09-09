import React, { useEffect, useState } from "react";

export default function HomePage() {
  const [contests, setContests] = useState([]);
  //const [contestants, setContestants] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8000/api/contests")
      .then((res) => res.json())
      .then(setContests);
  }, []);

  const sortedContests = [...contests].sort((a, b) => 
    new Date(a.startDateTime) + new Date(b.startDateTime)
  );

  return (
    <div>
      <h1>Joker</h1>
      <section>
        <div>
          <h2>Concursos</h2>
          <a href="/create_contest">Adicionar Concurso</a>
        </div>
        <ul>
          {sortedContests.map((c) => (
            <li key={c.contestId || c.title + Math.random()}>
              <b>{c.title}</b> {" "}
              Description: {c.description} <b/>
              Start: {new Date(c.startDateTime).toLocaleString()} | 
              End: {new Date(c.endDateTime).toLocaleString()} {" "}
              Prize: {c.prize}€
            </li>
          ))}
        </ul>
      </section>
    </div>
  );
}
