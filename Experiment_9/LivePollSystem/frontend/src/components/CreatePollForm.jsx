import { useState } from "react";

function CreatePollForm({ onCreate }) {
  const [question, setQuestion] = useState("");
  const [options, setOptions] = useState(["", ""]);

  function updateOption(index, value) {
    setOptions((prev) => prev.map((item, i) => (i === index ? value : item)));
  }

  function addOption() {
    setOptions((prev) => [...prev, ""]);
  }

  function handleSubmit(event) {
    event.preventDefault();
    const cleaned = options.map((o) => o.trim()).filter(Boolean);
    if (!question.trim() || cleaned.length < 2) {
      return;
    }
    onCreate({ question: question.trim(), options: cleaned });
    setQuestion("");
    setOptions(["", ""]);
  }

  return (
    <form className="card form" onSubmit={handleSubmit}>
      <h2>Create Poll (Admin)</h2>
      <label>
        Question
        <input value={question} onChange={(e) => setQuestion(e.target.value)} placeholder="What should we deploy next?" />
      </label>

      {options.map((option, index) => (
        <label key={index}>
          Option {index + 1}
          <input value={option} onChange={(e) => updateOption(index, e.target.value)} />
        </label>
      ))}

      <div className="row">
        <button type="button" onClick={addOption}>Add Option</button>
        <button type="submit">Create Poll</button>
      </div>
    </form>
  );
}

export default CreatePollForm;
