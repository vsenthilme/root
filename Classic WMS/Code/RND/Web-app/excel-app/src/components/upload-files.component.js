import React, { Component } from "react";
import {  Table } from "semantic-ui-react";
import UploadService from "../services/upload-files.service";

export default class UploadFiles extends Component {
  constructor(props) {
    super(props);
    this.showOrders = this.showOrders.bind(this);

    this.state = {
      message: "",
	  myObject: [],
	  tableRows: null,
    };
  }

  componentDidMount() {
  }

  showOrders() {
	  UploadService.getFiles().then((response) => {
      this.setState({
        myObject: response.data,
      });
    });
  }

  render() {
    const {
      message,
	  myObject,
    } = this.state;

    return (
      <div>
        <label className="btn btn-default">
          Received Orders
        </label>

        <button
          className="btn btn-success" 
          onClick={this.showOrders}
        >
          Display
        </button>

        <div className="alert alert-light" role="alert">
			<h4>{message}</h4>
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