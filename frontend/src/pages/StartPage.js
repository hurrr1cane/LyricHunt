import Button from "../components/Button";
import Input from "../components/Input";
import { ClimbingBoxLoader } from "react-spinners";
import { useNavigate, useNavigation } from "react-router-dom";
import { useState, useEffect } from "react";
import styles from "./StartPage.module.css";

export default function StartPage() {
  const navigate = useNavigate();
  const navigation = useNavigation();
  const [error, setError] = useState(false);
  const [artistName, setArtistName] = useState("");
  const [quote, setQuote] = useState("");

  useEffect(() => {
    if (navigation.state === "loading") {
      const id = setInterval(() => {
        setQuote(randomizeQuote());
      }, 2000);
      return () => clearInterval(id);
    }
  }, [navigation.state]);

  const randomizeQuote = () => {
    const quotes = [
      "Picking the best songs...",
      "Loading the lyrics...",
      "Getting ready...",
      "Almost there...",
      "If you are listening to vioria, you are sick",
      "Picking the nuclear codes...",
      "Breaking the pentagon...",
      "Hacking the mainframe...",
      "Getting the lyrics...",
      "Finding the best songs...",
      "Making the magic happen...",
      "Loading...",
    ];
    return quotes[Math.floor(Math.random() * quotes.length)];
  };

  const startGameHandler = () => {
    if (!artistName.trim()) {
      setError(true);
      setTimeout(() => setError(false), 2000);
      return;
    }
    navigate(`/game/${artistName}`);
  };

  const handleArtistNameChange = (event) => setArtistName(event.target.value);

  const handleKeyPress = (event) => {
    if (event.key === "Enter") {
      startGameHandler();
    }
  };

  if (navigation.state === "loading") {
    return (
      <div className={styles.main}>
        <ClimbingBoxLoader loading={true} color="#FFF" />
        <p>{quote}</p>
      </div>
    );
  }

  return (
    <div className={styles.main}>
      <h1 className={styles.h1}>LyricHunt</h1>
      <h3 className={styles.h3}>
        A game where you can test your memory on knowing the songs of your favorite artist.
      </h3>
      <p className={error ? `${styles.p} ${styles.error}` : styles.p}>
        Enter your artist's name
      </p>
      <Input
        placeholder="Artist's name"
        onKeyPress={handleKeyPress}
        value={artistName}
        onChange={handleArtistNameChange}
        className={error ? `${styles.input} ${styles.error}` : styles.input}
      />
      <Button onClick={startGameHandler} className={styles.button}>
        Start
      </Button>
    </div>
  );
}
