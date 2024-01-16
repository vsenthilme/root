import { ArrayDataSource } from '@angular/cdk/collections';
import { NestedTreeControl } from '@angular/cdk/tree';
import { DatePipe } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Table } from 'primeng/table';
import { AuthService } from 'src/app/core/core';

export interface  variant {
  code:  string;
  partnerName:  string;
  employeeName:  string;
  userName:  string;
  password:  string;
  userProfile:  string;
  accessType:  string;
} 
const ELEMENT_DATA:  variant[] = [
  {code:  '10001',partnerName:  'Ashraf',employeeName:  'Associtate',userName:  '965-3221-3222',password: 'Active',userProfile: 'ADMIN',accessType: '15-02-2023'},
  
  {code:  '10002',partnerName:  'Amir',employeeName:  'Associtate',userName:  '965-3221-3222',password: 'Active',userProfile: 'ADMIN',accessType: '15-02-2023'},

  {code:  '10003',partnerName:  'Mohamed',employeeName:  'Associtate',userName:  '965-3221-3222',password: 'Active',userProfile: 'ADMIN',accessType: '15-02-2023'},

];

export interface  variant1 {
  code:  string;
  employeeName:  string;
  userName:  string;
  password:  string;
  userProfile:  string;
  accessType:  string;
  accessType1:  string;
  accessType2:  string;
  accessType3:  string;
} 
const ELEMENT_DATA1:  variant1[] = [
  {code:  '11020030',employeeName:  'Abdullah',userName:  '13132, Mubarak Al-kabir',password: 'P.O.Box: 15, Arabian Gulf Street, Safat 13001  Kuwait City',userProfile: 'Gift items',accessType: '5',
  accessType1: '5',accessType2: '11-02-2023', accessType3: 'New'}

];


export interface  variant2 {
  code:  string;
  employeeName:  string;
  userName:  string;
  password:  string;
  userProfile:  string;
  accessType:  string;
  accessType1:  string;
  accessType2:  string;
  accessType3:  string;
} 
const ELEMENT_DATA2:  variant2[] = [
  {code:  '11020030',employeeName:  'Diamond',userName:  '23',password: '33',userProfile: '3445',accessType: 'Percentage',
  accessType1: 'Active',accessType2: 'Admin', accessType3: '15-02-2023'}

];

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})

export class DashboardComponent implements OnInit {
  @ViewChild('userProfile') userProfile: Table | undefined;
  @ViewChild('consignmentNew') consignmentNew: Table | undefined;
  @ViewChild('consignmentHistory') consignmentHistory: Table | undefined;
  products: any;
  selectedProducts : variant[];

  
  consignmentNew1: any;
  selectedconsignmentNew1 : variant[];

  
  consignmentHistory1: any;
  selectedconsignmentHistory1 : variant[];


  advanceFilterShow: boolean;

  constructor(public dialog: MatDialog,private spinner: NgxSpinnerService) { 
    
  }

  ngOnInit(): void {
    this.products= (ELEMENT_DATA)
    this.consignmentNew1= (ELEMENT_DATA1)
    this.consignmentHistory1= (ELEMENT_DATA2)
    

    this.spinner.show();

    setTimeout(() => {
      /** spinner ends after 5 seconds */
      this.spinner.hide();
    }, 1000);

  }

  // applyFilterGlobal($event: any, stringVal: any) {
  //   this.userProfile!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  // }
  
customerTable = true
consignmentTable = false
congignmentNewTable = false
  selectedDashBoard(selected){
if(selected == 'Customer'){
this.customerTable = true;
this.consignmentTable = false;
this.congignmentNewTable = false;
}
if(selected == 'Consignment'){
  this.customerTable = false;
  this.consignmentTable = true;
  this.congignmentNewTable = false;
  }
  if(selected == 'Consignment-New'){
    this.customerTable = false;
    this.consignmentTable = false;
    this.congignmentNewTable = true;
    }
  }

}
