import React from 'react'
import styles from './StartPage.module.css'
import Button from '../components/Button'

export default function ErrorPage() {
  return (
    <div className={styles.main}>
      <img src="/error.png" alt="Error"  />
      <p className={styles.h3}>So this is an error ;&#40;</p>
      <p className={styles.h3}>Please go back and try again</p>

      
      <Button className={styles.topMargin} onClick={() => window.location.href = "/"}>Go back</Button>
    </div>
  )
}
