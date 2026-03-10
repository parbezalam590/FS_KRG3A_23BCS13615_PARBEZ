import { useState } from "react";

function useForm(initialValues) {

  //state to hold form values 
  const [values, setValues] = useState(initialValues);

  //FNCN to handle input changes
  function handleChange(e) {

    const { name, value } = e.target;

    //here i m updating the values state by spreading the existing values
    setValues({
      ...values,
      [name]: value
    });
  }

// fnc to reset form 
  function resetForm() {
    setValues(initialValues);
  }

  return { values, handleChange, resetForm };
}

export default useForm;