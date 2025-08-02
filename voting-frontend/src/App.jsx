import { useEffect, useState } from 'react';

const CHOICES = ["0€", "200€", "500€", "1000€", "3000€", "10000€", "50000€" ];

function App() {
  const [username, setUsername] = useState('');
  const [choice, setChoice] = useState('');
  const [submitted, setSubmitted] = useState(false);
  const [results, setResults] = useState([]);

  const submitVote = async () => {
    if (!username || !choice) return;
    await fetch('http://localhost:8000/api/vote', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, choice }),
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
          <input
            placeholder="Your name"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <div>
            {CHOICES.map((c) => (
              <label key={c}>
                <input
                  type="radio"
                  name="choice"
                  value={c}
                  onChange={() => setChoice(c)}
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
              <li key={i}>{r.username} voted: {r.choice}</li>
            ))}
          </ul>
          <button onClick={() => setSubmitted(false)}>Vote Again</button>
        </>
      )}
    </div>
  );
}

export default App;
