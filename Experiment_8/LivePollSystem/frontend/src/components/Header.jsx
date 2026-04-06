import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function Header() {
  const { user, clearSession } = useAuth();
  const navigate = useNavigate();

  function handleLogout() {
    clearSession();
    navigate("/login");
  }

  return (
    <header className="topbar">
      <div>
        <h1>LivePoll Secure</h1>
        <p>Experiment 8: Spring Security + OAuth + RBAC</p>
      </div>
      <nav>
        <Link to="/dashboard">Dashboard</Link>
        {user?.role === "ROLE_ADMIN" ? <Link to="/admin">Admin</Link> : null}
        <button onClick={handleLogout}>Logout</button>
      </nav>
    </header>
  );
}

export default Header;
