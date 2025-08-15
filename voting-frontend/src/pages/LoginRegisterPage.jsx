import AuthForm from "../components/AuthForm";

export default function LoginRegisterPage( { setToken } ) {
  const handleSuccess = (data) => {
    console.log("User authenticated:", data);
    setToken(data.token);
    // optionally redirect
  };

  return (
    <div style={{ maxWidth: "400px", margin: "2rem auto" }}>
      <AuthForm type="register" onSuccess={handleSuccess} />
      <hr />
      <AuthForm type="login" onSuccess={handleSuccess} />
    </div>
  );
}
