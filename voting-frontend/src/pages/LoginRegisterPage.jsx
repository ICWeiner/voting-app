import AuthForm from "../components/AuthForm";
import { setAuthHeader } from '../helpers/axios_helper';

export default function LoginRegisterPage() {
  const handleSuccess = (data) => {
    console.log("User authenticated:", data);

    if (data && data.token) {
      // Save the token locally (this also updates axios default headers)
      setAuthHeader(data.token);
    }

  };

  return (
    <div style={{ maxWidth: "400px", margin: "2rem auto" }}>
      <AuthForm type="register" onSuccess={handleSuccess} />
      <hr />
      <AuthForm type="login" onSuccess={handleSuccess} />
    </div>
  );
}
