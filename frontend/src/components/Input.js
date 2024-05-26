import React from "react";
import styles from "./ComponentsStyles.module.css";

const Input = ({ value, onChange, className, ...props }) => {
  const handleChange = (event) => {
    // Ensure event.target exists before accessing its value
    if (event.target) {
      // Call the onChange function passed as props with the updated value
      onChange(event.target.value);
    }
  };

  return (
    <input
      onChange={handleChange}
      value={value}
      className={`${styles.input} ${className}`}
      type="text"
      {...props}
    />
  );
};

export default Input;
