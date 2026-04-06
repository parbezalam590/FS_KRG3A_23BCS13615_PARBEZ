import { useEffect, useState } from "react";
import Header from "../components/Header";
import PollCard from "../components/PollCard";
import { apiRequest } from "../api/client";
import { useAuth } from "../context/AuthContext";

function DashboardPage() {
  const { token, user } = useAuth();
  const [polls, setPolls] = useState([]);
  const [error, setError] = useState("");

  async function loadPolls() {
    try {
      setError("");
      const data = await apiRequest("/api/polls", { method: "GET" }, token);
      setPolls(data);
    } catch (err) {
      setError(err.message);
    }
  }

  async function handleVote(pollId, optionId) {
    try {
      const updated = await apiRequest(
        `/api/polls/${pollId}/vote`,
        {
          method: "POST",
          body: JSON.stringify({ optionId })
        },
        token
      );

      setPolls((prev) => prev.map((poll) => (poll.id === updated.id ? updated : poll)));
    } catch (err) {
      setError(err.message);
    }
  }

  useEffect(() => {
    loadPolls();
  }, []);

  return (
    <main className="page-wrap">
      <Header />

      <section className="card intro">
        <h2>Hello, {user?.email}</h2>
        <p>
          You are authenticated as <strong>{user?.role}</strong>. Security filter chain validates your JWT token for every API request.
        </p>
        <button onClick={loadPolls}>Refresh Polls</button>
      </section>

      {error ? <p className="error">{error}</p> : null}

      <section className="grid">
        {polls.map((poll) => (
          <PollCard key={poll.id} poll={poll} onVote={handleVote} disableVote={user?.role !== "ROLE_USER"} />
        ))}
      </section>
    </main>
  );
}

export default DashboardPage;
