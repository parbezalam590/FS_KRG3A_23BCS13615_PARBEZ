import { useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function OAuthSuccessPage() {
  const { setSession } = useAuth();
  const navigate = useNavigate();
  const { search } = useLocation();

  useEffect(() => {
    const params = new URLSearchParams(search);
    const token = params.get("token");
    const role = params.get("role");
    const email = params.get("email");

    if (!token || !role || !email) {
      navigate("/login", { replace: true });
      return;
    }

    setSession(token, {
      email,
      name: "Google User",
      role
    });

    navigate(role === "ROLE_ADMIN" ? "/admin" : "/dashboard", { replace: true });
  }, [navigate, search, setSession]);

  return <p className="loading">Completing Google login...</p>;
}

export default OAuthSuccessPage;
