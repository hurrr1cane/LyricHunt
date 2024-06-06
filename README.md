# LyricHunt

## Overview

LyricHunt is an interactive website that allows users to guess song lyrics in a fun and engaging way. Users can type the name of a song artist, and the app will retrieve a random song by that artist and display the lyrics with all words hidden. Users can then guess the lyrics by typing words, and the app will reveal all instances of the guessed word in the lyrics. The goal is to guess the song by revealing as many words as possible.

## Features

- **Artist Search**: Input an artist's name to fetch a random song.
- **Lyric Guessing Game**: Guess the lyrics by typing words to reveal all instances of the guessed word.
- **Song Identification**: Try to identify the song based on the revealed lyrics.

## Technologies Used

### Backend
- **Java Spring Boot**: For building the backend RESTful API.
- **MySQL**: Database for storing song and lyric data.
- **JDBC & JPA**: For database interaction and persistence.
- **Genius API**: For fetching song lyrics.

### Frontend
- **React**: For building the interactive user interface.

## Installation

To run LyricHunt on your local machine, follow these steps:

### Backend

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/lyrichunt.git
   ```

2. **Navigate to the Backend Directory**:
   ```bash
   cd lyrichunt/backend
   ```

3. **Configure MySQL Database**:
   Set up a MySQL database and update the database configuration in `application.properties`.

4. **Build and Run the Application**:
   Use your preferred IDE to build and run the Spring Boot application or use the command:
   ```bash
   ./mvnw spring-boot:run
   ```

### Frontend

1. **Navigate to the Frontend Directory**:
   ```bash
   cd lyrichunt/frontend
   ```

2. **Install Dependencies**:
   ```bash
   npm install
   ```

3. **Run the Application**:
   ```bash
   npm start
   ```

## Usage

1. **Launch the Application**:
   Open your browser and navigate to `http://localhost:3000` to view the frontend.

2. **Search for an Artist**:
   Enter the name of an artist to fetch a random song and its lyrics.

3. **Guess the Lyrics**:
   Start typing words you think might be in the lyrics. All instances of the guessed word will be revealed.

4. **Identify the Song**:
   Use the revealed lyrics to guess the name of the song.

## Contributing

Contributions are welcome! If you have suggestions or improvements, please create a pull request or open an issue.

## License

This project is licensed under the Apache 2.0 License. See the [LICENSE](LICENSE) file for details.

## Contact

If you have any questions or need further assistance, please contact:

- **Name**: Maksym Horak
- **Email**: maximgorak2005@gmail.com

Enjoy guessing and discovering songs with LyricHunt!
