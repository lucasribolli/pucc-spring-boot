import React, {useEffect, useState} from "react";

import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link,
} from "react-router-dom";

import '../src/App.css';
import LoginScreen from "./pages/login/LoginScreen";
import ListScreen from "./pages/list/ListScreen";
import UserScreen from "./pages/user/UserScreen";


export default function App() {
  const [isLogged, setIsLogged] = useState(false);

  const logout = () => {
    localStorage.removeItem("token")
    window.location.replace("/")
  }

  useEffect(() => {
    const isAuth = localStorage.getItem("token");

    if (isAuth) {
      setIsLogged(true);
    } else {
      setIsLogged(false);
    }
  }, []);
  return (
    <Router>
      <div>
        <nav className="navbar">
          {
            isLogged ? (
              <>
                <Link className="navbar__btn" to="/reserves-list">Reservas</Link>
                <Link className="navbar__btn" to="/users">Usuários</Link>
                {/* eslint-disable-next-line */}
                <a className="navbar__btn" onClick={() => logout()}>Logout</a>
              </>
            ) : (
              <>
                <Link className="navbar__btn" to="/login">Login</Link>
              </>
            )
          }

        </nav>

        <div style={{
          display: 'flex',
          justifyContent: 'center',
        }}>
          <Switch>
            <Route exact path="/">
              <LoginScreen />
            </Route>
            <Route exact path="/login">
              <LoginScreen />
            </Route>
            <Route exact path="/reserves-list">
              <ListScreen />
            </Route>
            <Route exact path="/users">
              <UserScreen />
            </Route>
          </Switch>
        </div>
      </div>
    </Router>
  );
}
