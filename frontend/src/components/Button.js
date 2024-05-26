import styles from "./ComponentsStyles.module.css";

export default function Button({ children, onClick, ...props }) {
  return (
    <button onClick={onClick} className={styles.button + (props.className? " " + props.className : "")}>
      {children}
    </button>
  );
}
