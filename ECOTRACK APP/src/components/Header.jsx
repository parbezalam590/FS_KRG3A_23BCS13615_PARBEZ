import { Link } from "react-router-dom";

const Header = ({ title }) => {
  return (
    <>
      <header
        style={{
          padding: "1rem",
          backgroundColor: "#2ecc71",
          color: "white",
          textAlign: "center",
        }}
      >
        <h2>{title}</h2>
        <nav>
          <Link
            to="/"
            style={{
              marginRight: "1rem",
              color: "white",
              textDecoration: "none",
            }}
          >
            Dashboard
          </Link>
          <Link
            to="/logs"
            style={{
              marginRight: "1rem",
              color: "white",
              textDecoration: "none",
            }}
          >
            Logs
          </Link>
          <Link
            to="/"
            style={{
              marginRight: "1rem",
              color: "white",
              textDecoration: "none",
            }}
          >
            Login
          </Link>
          <Link
            to="/"
            style={{
              marginRight: "1rem",
              color: "red",
              textDecoration: "none",
            }}
          >
            Logout
          </Link>
          
        </nav>
      </header>
    </>
  );
};

export default Header;
