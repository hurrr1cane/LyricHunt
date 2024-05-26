import React from "react";
import styles from "./ComponentsStyles.module.css";

const Input = ({ value, className, ...props }) => {
  return (
    <input
      value={value}
      className={`${styles.input} ${className}`}
      type="text"
      {...props}
    />
  );
};

export default Input;
