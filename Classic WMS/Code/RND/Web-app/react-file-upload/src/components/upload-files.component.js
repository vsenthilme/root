import React, { Component } from "react";
import {  Table } from "semantic-ui-react";
import UploadService from "../services/upload-files.service";

export default class UploadFiles extends Component {
  constructor(props) {
    super(props);
    this.selectFile = this.selectFile.bind(this);
    this.upload = this.upload.bind(this);

    this.state = {
      selectedFiles: undefined,
      currentFile: undefined,
      progress: 0,
      message: "",
	  myObject: [],
      fileInfos: [],
	  tableRows: null,
    };
  }

  componentDidMount() {
	UploadService.getFiles().then((response) => {
      this.setState({
        fileInfos: response.data,
      });
    });
  }

  selectFile(event) {
    this.setState({
      selectedFiles: event.target.files,
    });
  }

  upload() {
    let currentFile = this.state.selectedFiles[0];

    this.setState({
      progress: 0,
      currentFile: currentFile,
    });

	UploadService.upload(currentFile, (event) => {
      this.setState({
        progress: Math.round((100 * event.loaded) / event.total),
      });
    })
      .then((response) => {
		  this.setState({
			  message: response.data.message,
			  myObject: response.data.newOrders,
			});
	  }) 
      .catch((e) => {
        this.setState({
          progress: 0,
          message: e.message,
          currentFile: undefined,
        });
      });

    this.setState({
      selectedFiles: undefined,
    });
  }

  render() {
    const {
      selectedFiles,
      currentFile,
      progress,
      message,
	  myObject,
      fileInfos,
    } = this.state;

    return (
      <div>
        {currentFile && (
          <div className="progress">
            <div
              className="progress-bar progress-bar-info progress-bar-striped"
              role="progressbar"
              aria-valuenow={progress}
              aria-valuemin="0"
              aria-valuemax="100"
              style={{ width: progress + "%" }}
            >
              {progress}%
            </div>
          </div>
        )}

        <label className="btn btn-default">
          <input type="file" onChange={this.selectFile} />
        </label>

        <button
          className="btn btn-success"
          disabled={!selectedFiles}
          onClick={this.upload}
        >
          Post
        </button>

        <div className="alert alert-light" role="alert">
			<h5>
				{message && (
					<div> <b>Status Message from Courier: [Port:8085]</b></div>
				)}
            </h5>
			<h5>{message}</h5>
			<br/>

			<Table className="table table-responsive table-striped mt-3">	
				<tr className="bg-secondary text-white">
					<th> Shipper Code</th>
					<th> Shipment Order type</th>
					<th> Shipment Order No</th>
					<th> Delivery Type</th>
					<th> Status</th>
					<th> Consignement type</th>
					<th> Outbound Do Number</th>
					<th> Product</th>
					<th> Warehouse</th>
					<th> Requested Delivery date</th>
					<th> Delviery order date</th>
					<th> Quantity</th>
					<th> Unit of Measure</th>
					<th> Unit Price</th>
					<th> Currency</th>
					<th> Payment Mode</th>
					<th> COD Amount</th>
				</tr>
				
				{this.state.myObject &&
				this.state.myObject.map((order, index) => (
               
				<tr key={index}>
				  <td>{order.shipperCode}</td>
				  <td>{order.shipmentOrdertype}</td>
				  <td>{order.shipmentOrderNo}</td>
				  <td>{order.deliveryType}</td>
				  <td>{order.status}</td>
				  <td>{order.consignementType}</td>
				  <td>{order.outboundDeliveryOrderNo}</td>
				  <td>{order.itemCode}</td>
				  <td>{order.warehouseCode}</td>
				  <td>{order.requiredDeliveryDate}</td>
				  <td>{order.deliveryOrderDate}</td>
				  <td>{order.itemQuantity}</td>
				  <td>{order.orderedUnitOfMeasure}</td>
				  <td>{order.unitPrice}</td>
				  <td>{order.currency}</td>
				  <td>{order.paymentMode}</td>
				  <td>{order.codAmount}</td>
                </tr>
              ))}
			</Table>
        </div>
      </div> 
    );
  }
}