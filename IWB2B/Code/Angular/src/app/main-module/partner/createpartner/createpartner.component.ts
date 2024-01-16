
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Table } from 'primeng/table';
import * as FileSaver from 'file-saver';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
import { PartnerNewComponent } from './partner-new/partner-new.component';

export interface  variant {
  partnerType:  string;
  apiSubs:  string;
  organization:  string;
  systemType:  string;
  systemType1:  string;
  accessType:  string;
} 
const ELEMENT_DATA:  variant[] = [
  {organization:  '10001',apiSubs:  'Asyad',partnerType:  'Asyad Logistics',systemType:  'LMD API Services',systemType1:  'Full Access',accessType:  'Admin'},
  {organization:  '10002',apiSubs:  'Naquel',partnerType:  'Naquel Logistics',systemType:  'MMD API Services',systemType1:  'Full Access',accessType:  'Admin'},
  {organization:  '10003',apiSubs:  'Ecomm Partner',partnerType:  'Ecomm ',systemType:  'Ecomm API Services',systemType1:  'Full Access',accessType:  'Admin'},

];


@Component({
  selector: 'app-createpartner',
  templateUrl: './createpartner.component.html',
  styleUrls: ['./createpartner.component.scss']
})
export class CreatepartnerComponent implements OnInit {
  
  @ViewChild('userProfile') userProfile: Table | undefined;
  products: any;
  selectedProducts : variant[];
  advanceFilterShow: boolean;

  constructor(public dialog: MatDialog,) { 
    
  }

  ngOnInit(): void {
    console.log(ELEMENT_DATA)
    this.products= (ELEMENT_DATA)
    console.log( this.products) 
    
  }

  applyFilterGlobal($event: any, stringVal: any) {
    this.userProfile!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  }
  

  new(): void {

    const dialogRef = this.dialog.open(PartnerNewComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%', },
    });

    dialogRef.afterClosed().subscribe(result => {
    });
  }

  delete(): void {

    const dialogRef = this.dialog.open(DeleteComponent, {
      disableClose: true,
      width: '35%',
      maxWidth: '80%',
    });

    dialogRef.afterClosed().subscribe(result => {
    });
  }


  advanceFilter(){
    this.advanceFilterShow = !this.advanceFilterShow;
  }
  
}

