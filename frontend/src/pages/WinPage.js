import React from "react";
import { useLocation } from "react-router-dom";
import styles from "./WinPage.module.css";
import Button from "../components/Button";

export default function WinPage() {
  const location = useLocation();
  const { data } = location.state;

  const handlePlayAgain = () => {
    window.location.href = "/";
  };

  return (
    <div class={styles.body}>
      <div className={styles.header}>
        <h1 className={styles.h1}>LyricHunt</h1>
      </div>

      <div className={styles.main}>
        <h1 className={`${styles.center} ${styles.h1}`}>
          You finished the game!
        </h1>
        <h2 className={`${styles.center} ${styles.h2}`}>Your song was: </h2>

        <div className={`${styles.row} ${styles.center}`}>
          <h2>{data.song.artist} - </h2>
          <h2>{data.song.title}</h2>
        </div>
        <div className={styles.lyricsSection}>
          <div>
            <h3 className={styles.h3}>Lyrics:</h3>
            <p className={styles.lyrics}>{formatLyrics(data.song.lyrics)}</p>
          </div>
        </div>

        <div className={styles.center}>
          <Button className={styles.button} onClick={handlePlayAgain}>Play again</Button>
        </div>
      </div>
    </div>
  );
}

function formatLyrics(lyrics) {
  // Replace all "_" with "█"
  // Split the lyrics by \n
  // Separate them for different <p>

  if (!lyrics) return;

  return lyrics.split("\n").map((line, index) => {
    return (
      <p className={styles.pMargin} key={index}>
        {line}
      </p>
    );
  });
}
