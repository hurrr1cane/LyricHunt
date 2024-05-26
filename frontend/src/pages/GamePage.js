import React from 'react'
import { useLoaderData } from 'react-router-dom';
import Input from '../components/Input';
import Button from '../components/Button';

import styles from './GamePage.module.css';


export default function GamePage() {


  const data = useLoaderData();

  console.log(data);

  return (
    <div className={styles.main}>
      <h1>LyricHunt</h1>

      <div>
        <div className='row'>
          <h2>{data.artist} - </h2>
          <Input placeholder="Enter the song name"/>
          <Button className='small-button'>Submit</Button>
        </div>
        <div className='row'>
          <Input placeholder="Enter the word to reveal"/>
          <Button className='small-button'>Reveal</Button>
        </div>
        <div>
          <h3>Lyrics:</h3>
          <p>{data.guess}</p>
        </div>
      </div>
    </div>
  )
}

export async function loader({request, params}) {
  //Make post request on http://localhost:8080/game/start?artistName=Scorpions
  //If the request is successful, navigate to /game
  //If the request is unsuccessful, show an error message
  
  const artistName = params.artistName;
  const response = await fetch(`http://localhost:8080/game/start?artistName=${artistName}`, {
    method: "POST"
  });

  return response;
}