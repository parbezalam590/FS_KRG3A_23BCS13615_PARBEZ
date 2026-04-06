function PollCard({ poll, onVote, disableVote }) {
  return (
    <article className="poll-card">
      <div className="poll-card-head">
        <h3>{poll.question}</h3>
        <span className={poll.closed ? "status closed" : "status open"}>
          {poll.closed ? "Closed" : "Open"}
        </span>
      </div>

      <p className="meta">
        Created by {poll.createdBy} at {new Date(poll.createdAt).toLocaleString()}
      </p>

      <ul>
        {poll.options.map((option) => (
          <li key={option.id}>
            <div>
              <strong>{option.optionText}</strong>
              <small>{option.voteCount} votes</small>
            </div>
            {!poll.closed && !disableVote ? (
              <button onClick={() => onVote(poll.id, option.id)}>Vote</button>
            ) : null}
          </li>
        ))}
      </ul>
    </article>
  );
}

export default PollCard;
