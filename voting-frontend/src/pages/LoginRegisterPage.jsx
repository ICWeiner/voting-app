import AuthForm from "../components/AuthForm";

export default function LoginRegisterPage() {
  const handleSuccess = (data) => {
    console.log("User authenticated:", data);
    // optionally redirect or store token
  };

  return (
    <div style={{ maxWidth: "400px", margin: "2rem auto" }}>
      <AuthForm type="register" onSuccess={handleSuccess} />
      <hr />
      <AuthForm type="login" onSuccess={handleSuccess} />
    </div>
  );
}
