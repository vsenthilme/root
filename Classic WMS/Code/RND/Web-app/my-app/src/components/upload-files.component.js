import React, { Component } from "react";
import UploadService from "../services/upload-files.service";

export default class UploadFiles extends Component {
  constructor(props) {
    super(props);
    this.showOrders = this.showOrders.bind(this);

    this.state = {
      message: "",
    };
  }

  componentDidMount() {
  }

  showOrders() {
	  UploadService.getFiles().then((response) => {
      this.setState({
        message: response.data,
      });
    });
  }

  render() {
    const {
      message
    } = this.state;

    return (
      <div>
        <label className="btn btn-default">
          <h4>{message}</h4>
        </label>

        <button
          className="btn btn-success" 
          onClick={this.showOrders}
        >
          Display
        </button>      
      </div> 
    );
  }
}