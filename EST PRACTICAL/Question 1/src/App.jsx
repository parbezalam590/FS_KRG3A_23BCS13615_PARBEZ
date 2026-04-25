import React from "react";
import UserContext from "./UserContext";
import Dashboard from "./components/Dashboard";

function App() {
  const userData = {
    username: "Arjun",
    isLoggedIn: true,
  };

  return (
    <UserContext.Provider value={userData}>
      <div style={styles.container}>
        <div style={styles.card}>
          <h1>App Component</h1>
          <Dashboard />
        </div>
      </div>
    </UserContext.Provider>
  );
}

const styles = {
  container: {
    height: "100vh",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    background: "#f0f2f5",
  },
  card: {
    padding: "30px",
    borderRadius: "12px",
    background: "#fff",
    boxShadow: "0 4px 15px rgba(0,0,0,0.1)",
    textAlign: "center",
    width: "300px",
  },
};

export default App;