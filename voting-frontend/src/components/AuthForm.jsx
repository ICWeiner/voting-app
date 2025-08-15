import { useState } from "react";

export default function AuthForm({ type, onSuccess }) {
  const [identifier, setIdentifier] = useState(""); // can be username OR email
  const [email, setEmail] = useState(""); // used only for register
  const [username, setUsername] = useState(""); // used only for register
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    const endpoint = `http://localhost:8000/auth/${type}`;

    let body;
    if (type === "register") {
      body = { email, username, password };
    } else {
      body = { identifier, password }; // backend expects "identifier"
    }

    try {
      const res = await fetch(endpoint, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body),
      });

      const data = await res.json();

      if (res.ok) {
        setMessage(`${type} successful!`);
        onSuccess && onSuccess(data);
      } else {
        setMessage(data.message || "Something went wrong");
      }
    } catch (err) {
      console.error(err);
      setMessage("Network error");
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ marginBottom: "1.5rem" }}>
      <h2>{type === "register" ? "Register" : "Login"}</h2>

      {type === "register" ? (
        <>
          <input
            type="text"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </>
      ) : (
        <input
          type="text"
          placeholder="Username or Email"
          value={identifier}
          onChange={(e) => setIdentifier(e.target.value)}
          required
        />
      )}

      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        required
      />
      <button type="submit">{type === "register" ? "Sign Up" : "Login"}</button>
      {message && <p>{message}</p>}
    </form>
  );
}
