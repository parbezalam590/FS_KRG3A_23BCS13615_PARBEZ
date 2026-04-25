import { useContext } from "react";
import { AuthContext } from "../context/AuthContext.jsx";
import { useNavigate } from "react-router-dom";

const Login = () => {
  const { isAuthenticated, login, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleClick = () => {
    if (isAuthenticated) {
      logout();
    } else {
      login();
      navigate("/dashboard");
    }
  };

  return (
    <div className="container">
      <div className="card">
        <h2>🔐 Login Page</h2>

        <p className="status">
          {isAuthenticated ? "Logged In" : "Logged Out"}
        </p>

        <button
          className={`btn ${isAuthenticated ? "logout" : "login"}`}
          onClick={handleClick}
        >
          {isAuthenticated ? "Logout" : "Login"}
        </button>
      </div>
    </div>
  );
};

export default Login;