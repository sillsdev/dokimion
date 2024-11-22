import React, { Component } from "react";
import { withRouter } from "react-router";
import qs from "qs";
import * as Utils from "../common/Utils";
import Backend from "../services/backend";
import TextField from '@material-ui/core/TextField';

import { LinkButtons, forgotButton } from './components';
import ControlledPopup from '../common/ControlledPopup';

class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      login: "",
      password: "",
      errorMessage:"",
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.onSessionChange = this.onSessionChange.bind(this);
  }

  onSessionChange(session) {
    this.props.onSessionChange(session);
  }

  handleChange(event) {
    // eslint-disable-next-line react/no-direct-mutation-state
    this.state[event.target.name] = event.target.value;
    this.setState(this.state);
  }

  handleSubmit(event) {
    Backend.post("user/login?login=" + this.state.login + "&password=" + this.state.password)
        .then(response => {
	  let responseClone = JSON.parse(JSON.stringify(response));
          this.onSessionChange(responseClone);
          var params = qs.parse(this.props.location.search.substring(1));
          var retpath = decodeURIComponent(params.retpath || "");
          var decodedReptath = decodeURI(retpath);
          if (decodedReptath === "") {
            decodedReptath = "/";
          }
          window.location = decodeURI(decodedReptath);
        }).catch(error => {
	    this.setState({errorMessage: "Unable to login: " + error.message});
        });
    event.preventDefault();
  }

  render() {
    return (
      <div className="text-center">
        <ControlledPopup popupMessage={this.state.errorMessage}/>
        <form className="form-signin" onSubmit={this.handleSubmit}>
          <h1 className="h3 mb-3 font-weight-normal">Please sign in</h1>
          <label for="login" className="sr-only">
            Login
          </label>
          <input
            type="text"
            id="login"
            name="login"
            className="form-control"
            placeholder="Login"
            required=""
            autofocus=""
            onChange={this.handleChange}
          />
          <label for="password" className="sr-only">
            Password
          </label>
          <input
            type="password"
            id="password"
            name="password"
            className="form-control"
            placeholder="Password"
            required=""
            onChange={this.handleChange}
          />
          <button className="btn btn-lg btn-primary btn-block" >
            Sign in
          </button>
        </form>
	<LinkButtons
	    buttonStyle={forgotButton}
	    buttonText="Forgot Password"
	    link="/forgot_password"
	/>
      </div>
    );
  }
}

export default withRouter(Login);
