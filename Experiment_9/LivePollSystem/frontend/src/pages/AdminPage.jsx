import { useEffect, useState } from "react";
import Header from "../components/Header";
import CreatePollForm from "../components/CreatePollForm";
import PollCard from "../components/PollCard";
import { apiRequest } from "../api/client";
import { useAuth } from "../context/AuthContext";

function AdminPage() {
  const { token } = useAuth();
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

  async function createPoll(payload) {
    try {
      setError("");
      await apiRequest(
        "/api/admin/polls",
        {
          method: "POST",
          body: JSON.stringify(payload)
        },
        token
      );
      await loadPolls();
    } catch (err) {
      setError(err.message);
    }
  }

  async function closePoll(pollId) {
    try {
      const updated = await apiRequest(`/api/admin/polls/${pollId}/close`, { method: "POST" }, token);
      setPolls((prev) => prev.map((poll) => (poll.id === updated.id ? updated : poll)));
    } catch (err) {
      setError(err.message);
    }
  }

  async function deletePoll(pollId) {
    try {
      await apiRequest(`/api/admin/polls/${pollId}`, { method: "DELETE" }, token);
      setPolls((prev) => prev.filter((poll) => poll.id !== pollId));
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

      <CreatePollForm onCreate={createPoll} />
      {error ? <p className="error">{error}</p> : null}

      <section className="grid">
        {polls.map((poll) => (
          <article key={poll.id} className="admin-poll">
            <PollCard poll={poll} onVote={() => {}} disableVote />
            <div className="row">
              {!poll.closed ? <button onClick={() => closePoll(poll.id)}>Close Poll</button> : null}
              <button className="danger" onClick={() => deletePoll(poll.id)}>Delete</button>
            </div>
          </article>
        ))}
      </section>
    </main>
  );
}

export default AdminPage;
