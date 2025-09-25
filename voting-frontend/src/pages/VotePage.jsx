// src/pages/VotePage.jsx
import { useEffect, useState } from 'react';

const OPTIONS = ["0€", "200€", "500€", "1000€", "3000€", "10000€", "50000€"];

function VotePage() {
  const [option, setOption] = useState('');
  const [submitted, setSubmitted] = useState(false);
  const [results, setResults] = useState([]);

  const submitVote = async () => {
    if (!option) return;
    await fetch('http://localhost:8000/api/vote', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json'},
      credentials: "include", 
      body: JSON.stringify({ option }),
    });
    setSubmitted(true);
  };

  const fetchResults = async () => {
    const res = await fetch('http://localhost:8000/api/vote/today');
    const data = await res.json();
    setResults(data);
  };

  useEffect(() => {
    if (submitted) fetchResults();
  }, [submitted]);

  return (
    <div style={{ padding: 20 }}>
      <h1>Daily Prediction</h1>
      {!submitted ? (
        <>
          <div>
            {OPTIONS.map((c) => (
              <label key={c} style={{ display: 'block' }}>
                <input
                  type="radio"
                  name="option"
                  value={c}
                  onChange={() => setOption(c)}
                />
                {c}
              </label>
            ))}
          </div>
          <button onClick={submitVote}>Submit</button>
        </>
      ) : (
        <>
          <h2>Today's Votes</h2>
          <ul>
            {results.map((r, i) => (
              <li key={i}>{r.username} voted: {r.option}</li>
            ))}
          </ul>
          <button onClick={() => setSubmitted(false)}>Vote Again</button>
        </>
      )}
    </div>
  );
}

export default VotePage;