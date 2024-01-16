
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Table } from 'primeng/table';
import * as FileSaver from 'file-saver';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
import { CreatesystemNewComponent } from './createsystem-new/createsystem-new.component';

export interface  variant {
  partnerType:  string;
  apiSubs:  string;
  organization:  string;
  systemType:  string;
  systemType1:  string;
  accessType:  string;
} 
const ELEMENT_DATA:  variant[] = [
  {organization:  '1000',apiSubs:  'Shipsy',partnerType:  'LMD',systemType:  'LMD API Services',systemType1:  'Full Access',accessType:  'Admin'},
  {organization:  '1001',apiSubs:  'MMD Vendor',partnerType:  'MMD',systemType:  'MMD API Services',systemType1:  'Full Access',accessType:  'Admin'},
  {organization:  '1002',apiSubs:  'FMD Vendor',partnerType:  'FMD ',systemType:  'FMD API Services',systemType1:  'Full Access',accessType:  'Admin'},
  {organization:  '1003',apiSubs:  'Ecomm Partner',partnerType:  'Ecomm',systemType:  'Ecomm API Services',systemType1:  'Full Access',accessType:  'Admin'},
  {organization:  '1004',apiSubs:  'Odoo',partnerType:  'Odoo',systemType:  'Odoo API Services',systemType1:  'Full Access',accessType:  'Admin'},
  {organization:  '1005',apiSubs:  'Classic WMS',partnerType:  'WMS',systemType:  'WMS API Services',systemType1:  'Full Access',accessType:  'Admin'},

];


@Component({
  selector: 'app-create-newsystem',
  templateUrl: './create-newsystem.component.html',
  styleUrls: ['./create-newsystem.component.scss']
})
export class CreateNewsystemComponent implements OnInit {
  
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

    const dialogRef = this.dialog.open(CreatesystemNewComponent, {
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

