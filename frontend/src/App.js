import { createBrowserRouter, RouterProvider } from "react-router-dom";


import StartPage from "./pages/StartPage";
import GamePage from "./pages/GamePage";
import WinPage from "./pages/WinPage";
import { loader as gameLoader } from "./pages/GamePage";
import ErrorPage from "./pages/ErrorPage";


function App() {
  const router = createBrowserRouter([
    { path: "/", element: <StartPage /> },
    { path: "/game/:artistName", element: <GamePage />, loader: gameLoader, errorElement: <ErrorPage />},
    { path: "/game/end", element: <WinPage /> },
  ]);

  return <RouterProvider router={router}></RouterProvider>;
}

export default App;
