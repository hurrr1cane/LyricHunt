import { createBrowserRouter, RouterProvider } from "react-router-dom";

import "./App.css";

import StartPage from "./pages/StartPage";
import GamePage from "./pages/GamePage";
import { loader as gameLoader } from "./pages/GamePage";


function App() {
  const router = createBrowserRouter([
    { path: "/", element: <StartPage /> },
    { path: "/game/:artistName", element: <GamePage />, loader: gameLoader },
  ]);

  return <RouterProvider router={router}></RouterProvider>;
}

export default App;
