import Button from "../components/Button";
import Input from "../components/Input";

import { useNavigate } from "react-router-dom";
import { useState } from "react";
import styles from "./StartPage.module.css";

export default function StartPage() {
  const navigate = useNavigate();
  const [error, setError] = useState(false);
  const [artistName, setArtistName] = useState(""); // State to store artist name

  function startGameHandler() {
    if (artistName.trim().length === 0) {
      // Show error if artist name is empty
      setError(true);
      setTimeout(() => {
        setError(false);
      }, 2000);
      return;
    }

    navigate(`/game/${artistName}`); // Corrected navigation route
  }

  // Handler function to update artist name state
  const handleArtistNameChange = (event) => {
    setArtistName(event.target.value);
  };

  const handleKeyPress = (event) => {
    if (event.key === "Enter") {
      startGameHandler();
    }
  };

  return (
    <div className={styles.main}>
      <h1 className={styles.h1}>LyricHunt</h1>
      <h3 className={styles.h3}>
        A game where you can test your memory on knowing the songs of your
        favorite artist.
      </h3>
      <p className={error ? `${styles.p} ${styles.error}` : styles.p}>
        Enter your artist's name
      </p>
      {/* Pass value and onChange handler to Input component */}
      <Input
        placeholder="Artist's name"
        onKeyPress={handleKeyPress}
        value={artistName} // Set value
        onChange={handleArtistNameChange} // Pass handler function directly
        className={error ? `${styles.input} ${styles.error}` : styles.input}
      />
      <Button onClick={startGameHandler} className={styles.button}>
        Start
      </Button>
    </div>
  );
}
