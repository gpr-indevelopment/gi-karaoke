import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import React from "react";
import QueueView from "./components/QueueView/QueueView";
import Admin from "./components/Admin/Admin";

function App() {
  return (
    <Router>
      <Switch>
        <Route exact path="/">
          <QueueView/>
        </Route>
        <Route exact path="/admin">
          <Admin/>
        </Route>
      </Switch>
    </Router>
  )
}

export default App;
