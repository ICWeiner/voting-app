import { useState } from "react";
import { request, setAuthHeader } from "../helpers/axios_helper";

export default function AuthForm({ type, onSuccess }) {
  const [identifier, setIdentifier] = useState(""); // can be username OR email
  const [email, setEmail] = useState(""); // used only for register
  const [username, setUsername] = useState(""); // used only for register
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    const endpoint = `/auth/${type}`; // baseURL handled in helper

    const body =
      type === "register"
        ? { email, username, password }
        : { identifier, password };

    try {
      const res = await request("POST", endpoint, body);
      const data = res.data;

      // Axios doesn’t use res.ok — instead, errors throw automatically
      if (type === "login" && data.token) {
        // Save JWT to localStorage via helper
        setAuthHeader(data.token);
      }

      setMessage(`${type} successful!`);
      onSuccess && onSuccess(data);
    } catch (err) {
      console.error(err);
      if (err.response) {
        setMessage(err.response.data?.message || "Something went wrong");
      } else {
        setMessage("Network error");
      }
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
