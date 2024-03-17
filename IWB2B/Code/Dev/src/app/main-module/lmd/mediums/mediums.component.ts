

  import { Component, OnInit, ViewChild } from '@angular/core';
  import { MatDialog } from '@angular/material/dialog';
  import { Table } from 'primeng/table';
  
  export interface  variant {
    partnerType:  string;
    apiSubs:  string;
    organization:  string;
    systemType:  string;
  } 
  const ELEMENT_DATA:  variant[] = [
    {organization:  '',apiSubs:  '',partnerType:  '',systemType:  '',},
  ];
  
  export interface  pickup {
    pickupId:  string;
    status:  string;
    pickupPincode:  string;
    loadType:  string;
    pickupTime:  string;
  } 
  const ELEMENT_DATA1:  pickup[] = [
    {pickupId:  '',status:  '',pickupPincode:  '',loadType:  '',pickupTime: ''},
  ];

  export interface  delivery {
    consignmentNo:  string;
    status:  string;
    destinationCode:  string;
    deliveryTime:  string;
    receipt:  string;
  } 
  const ELEMENT_DATA2:  delivery[] = [
    {consignmentNo:  '',status:  '',destinationCode:  '',deliveryTime:  '',receipt: ''},
  ];

  @Component({
    selector: 'app-mediums',
    templateUrl: './mediums.component.html',
    styleUrls: ['./mediums.component.scss']
  })
  export class MediumsComponent implements OnInit {
  
  
    
    @ViewChild('userProfile') userProfile: Table | undefined;
    products: any;
    pickup: any;
    selectedProducts : variant[];
    advanceFilterShow: boolean;
  
    constructor(public dialog: MatDialog,) { 
      
    }
  
    ngOnInit(): void {
      this.products= (ELEMENT_DATA)
      this.pickup= (ELEMENT_DATA1)
    }
  
    applyFilterGlobal($event: any, stringVal: any) {
      this.userProfile!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
    }
    

  
    advanceFilter(){
      this.advanceFilterShow = !this.advanceFilterShow;
    }



  }
  
  
