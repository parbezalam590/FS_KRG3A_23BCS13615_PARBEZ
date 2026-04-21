import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { apiRequest, getGoogleLoginUrl } from "../api/client";
import { useAuth } from "../context/AuthContext";

function LoginPage() {
  const navigate = useNavigate();
  const { setSession } = useAuth();
  const [mode, setMode] = useState("login");
  const [form, setForm] = useState({ name: "", email: "", password: "" });
  const [error, setError] = useState("");

  function updateField(key, value) {
    setForm((prev) => ({ ...prev, [key]: value }));
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setError("");

    try {
      const endpoint = mode === "login" ? "/api/auth/login" : "/api/auth/register";
      const payload = mode === "login"
        ? { email: form.email, password: form.password }
        : { name: form.name, email: form.email, password: form.password };

      const data = await apiRequest(endpoint, {
        method: "POST",
        body: JSON.stringify(payload)
      });

      setSession(data.token, {
        email: data.email,
        name: data.name,
        role: data.role
      });

      navigate(data.role === "ROLE_ADMIN" ? "/admin" : "/dashboard");
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <main className="login-page">
      <section className="hero">
        <h1>Secure LivePoll</h1>
        <p>
          Learn complete full-stack security flow with JWT, Google OAuth login, security filters,
          and role-based authorization.
        </p>
      </section>

      <section className="card form auth-card">
        <div className="row spread">
          <h2>{mode === "login" ? "Sign In" : "Create Account"}</h2>
          <button type="button" onClick={() => setMode(mode === "login" ? "register" : "login")}> 
            {mode === "login" ? "Need account?" : "Have account?"}
          </button>
        </div>

        <form onSubmit={handleSubmit}>
          {mode === "register" ? (
            <label>
              Name
              <input value={form.name} onChange={(e) => updateField("name", e.target.value)} required />
            </label>
          ) : null}

          <label>
            Email
            <input type="email" value={form.email} onChange={(e) => updateField("email", e.target.value)} required />
          </label>

          <label>
            Password
            <input type="password" value={form.password} onChange={(e) => updateField("password", e.target.value)} required />
          </label>

          <button type="submit">{mode === "login" ? "Login" : "Register"}</button>
        </form>

        <div className="divider">or</div>

        <a className="oauth-btn" href={getGoogleLoginUrl()}>
          Continue with Google OAuth
        </a>

        {error ? <p className="error">{error}</p> : null}

        <p className="hint">
          Demo user: user@livepoll.com / User@123 | Demo admin: admin@livepoll.com / Admin@123
        </p>
      </section>
    </main>
  );
}

export default LoginPage;
