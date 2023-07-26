import { useEffect, useState } from 'react';

const useCountdownTimer = () => {
  const [timeInSeconds, setTimeInSeconds] = useState(0);

  useEffect(() => {
    if (timeInSeconds > 0) {
      const timerInterval = setInterval(() => {
        setTimeInSeconds((prevTime) => prevTime - 1);
      }, 1000);
      return () => {
        clearInterval(timerInterval);
      };
    }
  }, [timeInSeconds]);

  // Calculate minutes and seconds
  const minutes = Math.floor(timeInSeconds / 60);
  const seconds = timeInSeconds % 60;

  // Format the time with leading zeros
  const formattedTime = `${minutes.toString().padStart(2, '0')}:${seconds
    .toString()
    .padStart(2, '0')}`;

  return { timeInSeconds, setTimeInSeconds, formattedTime };
};

export default useCountdownTimer;
