// Timer.js
import React, { useState, useEffect } from "react";

const Timer = ({ onTimeUpdate }) => {
  const [secondsElapsed, setSecondsElapsed] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      setSecondsElapsed((prevSecondsElapsed) => {
        const newSecondsElapsed = prevSecondsElapsed + 1;
        onTimeUpdate(newSecondsElapsed); // Update parent with new time
        return newSecondsElapsed;
      });
    }, 1000);

    return () => clearInterval(interval);
  }, [onTimeUpdate]);

  const formatTime = (totalSeconds) => {
    const minutes = Math.floor(totalSeconds / 60);
    const seconds = totalSeconds % 60;
    return `${minutes}:${seconds < 10 ? "0" : ""}${seconds}`;
  };

  return <p>{formatTime(secondsElapsed)}</p>;
};

export default Timer;
