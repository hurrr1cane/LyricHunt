import { React, useState } from "react";
import { useLoaderData, useNavigate } from "react-router-dom";
import Input from "../components/Input";
import Button from "../components/Button";
import Timer from "../components/Timer";
import { urlApi } from "../config";

import styles from "./GamePage.module.css";

export default function GamePage() {
  const data = useLoaderData();
  const navigate = useNavigate();

  const [lyrics, setLyrics] = useState(data.guess); // State to store lyrics

  const [wordToReveal, setWordToReveal] = useState(""); // State to store word to reveal

  const [songName, setSongName] = useState(""); // State to store song name

  const [hintCount, setHintCount] = useState(0); // State to store hint count

  async function surrenderHandler() {
    // Make a post request to urlApi/game/surrender/{sessionId}
    // If the request is successful, navigate to the LosePage
    // If the request is unsuccessful, show an error message

    const response = await fetch(`${urlApi}/game/surrender/${data.sessionId}`, {
      method: "POST",
    })
      .then((response) => response.json())
      .then((data) => {
        navigate("/game/end", {
          state: { data, hintCount: hintCount, time: currentTime },
        });
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  }

  async function hintHandler() {
    // Make a post request to ${urlApi}/game/hint/{sessionId}
    // If the request is successful, update the lyrics state
    // If the request is unsuccessful, show an error message

    const response = await fetch(`${urlApi}/game/hint/${data.sessionId}`, {
      method: "POST",
    })
      .then((response) => response.json())
      .then((data) => {
        setLyrics(data.guess);
      })
      .catch((error) => {
        console.error("Error:", error);
      });

    setHintCount(hintCount + 1);
  }

  async function revealHandler() {
    // Make a post request to ${urlApi}/game/reveal/{sessionId} with the word to reveal
    // If the request is successful, update the lyrics state

    const response = await fetch(`${urlApi}/game/reveal/${data.sessionId}`, {
      method: "POST",
      body: JSON.stringify({ word: wordToReveal }),
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setLyrics(data.guess);
      })
      .catch((error) => {
        console.error("Error:", error);
      });

    setWordToReveal("");
  }

  const handleKeyPressReveal = (event) => {
    // Check for 'enter' key press
    if (event.key === "Enter") {
      revealHandler();
    }
  };

  async function guessHandler() {
    const response = await fetch(`${urlApi}/game/guess/${data.sessionId}`, {
      method: "POST",
      body: JSON.stringify({ guess: songName }),
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setLyrics(data.guess);
        if (data.guessed) {
          // Redirect to WinPage
          navigate("/game/end", {
            state: { data, hintCount: hintCount, time: currentTime },
          });
        }
      })
      .catch((error) => {
        console.error("Error:", error);
      });

    setSongName("");
  }

  const guessOnChange = (event) => {
    setSongName(event.target.value);
  };

  const handleKeyPressGuess = (event) => {
    if (event.key === "Enter") {
      guessHandler();
    }
  };

  const wordOnChange = (event) => {
    setWordToReveal(event.target.value);
  };

  const [currentTime, setCurrentTime] = useState(0);

  const handleTimeUpdate = (time) => {
    setCurrentTime(time);
  };

  const formatTime = (totalSeconds) => {
    const minutes = Math.floor(totalSeconds / 60);
    const seconds = totalSeconds % 60;
    return `${minutes}:${seconds < 10 ? "0" : ""}${seconds}`;
  };

  return (
    <div class={styles.body}>
      <div className={styles.header}>
        <h1 className={styles.h1}>LyricHunt</h1>
      </div>

      <div className={styles.main}>
        <div className={`${styles.row} ${styles.center}`}>
          <h2>{data.artist} - </h2>
          <Input
            value={songName}
            className={styles.flex1}
            onKeyPress={handleKeyPressGuess}
            placeholder="Enter the song name"
            onChange={guessOnChange}
          />
          <Button onClick={guessHandler} className="small-button">
            Guess
          </Button>
        </div>
        <div className={styles.lyricsSection}>
          <div className={`${styles.row}`}>
            <Input
              onChange={wordOnChange}
              onKeyPress={handleKeyPressReveal}
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

          <div className={`${styles.row} ${styles.spaceBetween}`}>
            <div className={`${styles.flex1} ${styles.left}`}>
              <Button onClick={surrenderHandler}>Surrender</Button>
            </div>
            <div className={`${styles.flex1} ${styles.center}`}>
              <Timer onTimeUpdate={handleTimeUpdate} />
            </div>
            <div className={`${styles.flex1} ${styles.right}`}>
              <Button onClick={hintHandler}>Hint</Button>
            </div>
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
  //Make post request on ${urlApi}/game/start?artistName=Scorpions
  //If the request is successful, navigate to /game
  //If the request is unsuccessful, show an error message

  const artistName = params.artistName;
  const response = await fetch(
    `${urlApi}/game/start?artistName=${artistName}`,
    {
      method: "POST",
    }
  ).then((response) => {
    if (!response.ok) {
      throw new Error("Failed to start the game");
    }
    return response.json();
  });

  return response;
}
