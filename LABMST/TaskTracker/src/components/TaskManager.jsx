import React, { useState } from "react";
import useForm from "../hooks/useForm";

function TaskManager() {

  // state fOR holdin tasks
  const [tasks, setTasks] = useState([]);

  //  custom hook to manage form inputs
  const { values, handleChange, resetForm } = useForm({
    title: "",
    priority: "Low"
  });

  // This fnc will run when form is submitted
  function handleSubmit(e) {

    e.preventDefault();  

    // add new task to tasks array
    setTasks([...tasks, values]);

    
    resetForm();
  }

  return (
    <div>

      <h2>Task Tracker</h2>
    
      <form onSubmit={handleSubmit}>

        <input
          type="text"
          name="title"
          placeholder="Enter Task"
          value={values.title}
          onChange={handleChange}
          required
        />

        <select
          name="priority"
          value={values.priority}
          onChange={handleChange}
        >
          <option>Low</option>
          <option>Medium</option>
          <option>High</option>
        </select>

        <button type="submit">
          Add Task
        </button>

      </form>

      <h3>Task List</h3>

      <ul>

        {tasks.map((task, index) => (

          <li key={index}>
            {task.title} | {task.priority}
          </li>

        ))}

      </ul>

    </div>
  );
}

export default TaskManager;