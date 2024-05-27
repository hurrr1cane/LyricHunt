import { React, useState } from "react";
import { useLoaderData } from "react-router-dom";
import Input from "../components/Input";
import Button from "../components/Button";

import styles from "./GamePage.module.css";

export default function GamePage() {
  const data = useLoaderData();


  const [lyrics, setLyrics] = useState(data.guess); // State to store lyrics

  const [wordToReveal, setWordToReveal] = useState(""); // State to store word to reveal

  async function revealHandler() {
    // Make a post request to http://localhost:8080/game/reveal/{sessionId} with the word to reveal
    // If the request is successful, update the lyrics state

    console.log(data.sessionId);

    const response = await fetch(
      `http://localhost:8080/game/reveal/${data.sessionId}`,
      {
        method: "POST",
        body: JSON.stringify({ word: wordToReveal }),
        headers: {
          "Content-Type": "application/json",
        },
      }
    )
      .then((response) => response.json())
      .then((data) => {
        setLyrics(data.guess);
      })
      .catch((error) => {
        console.error("Error:", error);
      });

    // Clear the input field
    setWordToReveal("");
  }

  const handleKeyPress = (event) => {
    // Check for 'enter' key press
    if (event.key === "Enter") {
      revealHandler();
    }
  }

  const wordOnChange = (event) => {
    setWordToReveal(event.target.value);
  }

  return (
    <div class={styles.body}>
      <div className={styles.header}>
        <h1 className={styles.h1}>LyricHunt</h1>
      </div>

      <div className={styles.main}>
        <div className={`${styles.row} ${styles.center}`}>
          <h2>{data.artist} - </h2>
          <Input className={styles.input} placeholder="Enter the song name" />
          <Button className="small-button">Guess</Button>
        </div>
        <div className={styles.lyricsSection}>
          <div className={`${styles.row}`}>
            <Input
              onChange={wordOnChange}
              onKeyPress={handleKeyPress}
              value={wordToReveal}
              className={styles.inputStart}
              placeholder="Enter the word to reveal"
            />
            <Button onClick={revealHandler} className="small-button">
              Reveal
            </Button>
          </div>
          <div>
            <h3 className={styles.h3}>Lyrics:</h3>
            <p className={styles.lyrics}>{formatLyrics(lyrics)}</p>
          </div>
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

  return lyrics
    .replace(/_/g, "█")
    .split("\n")
    .map((line, index) => {
      return (
        <p className={styles.pMargin} key={index}>
          {line}
        </p>
      );
    });
}

export async function loader({ request, params }) {
  //Make post request on http://localhost:8080/game/start?artistName=Scorpions
  //If the request is successful, navigate to /game
  //If the request is unsuccessful, show an error message

  const artistName = params.artistName;
  const response = await fetch(
    `http://localhost:8080/game/start?artistName=${artistName}`,
    {
      method: "POST",
    }
  );

  return response;
}
