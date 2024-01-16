
  import { Component, OnInit, ViewChild } from '@angular/core';
  import { MatDialog } from '@angular/material/dialog';
  import { Table } from 'primeng/table';
import { BulkArrivalComponent } from './Arrival/bulk-arrival/bulk-arrival.component';
import { UploadArrivalComponent } from './Arrival/upload-arrival/upload-arrival.component';
import { UploadCancelComponent } from './cancel/upload-cancel/upload-cancel.component';
import { BulkInscanComponent } from './Inscan/bulk-inscan/bulk-inscan.component';
import { UploadInscanComponent } from './Inscan/upload-inscan/upload-inscan.component';
import { NewComponent } from './new/new.component';
import { BulkOutscanComponent } from './outscan/bulk-outscan/bulk-outscan.component';
import { UploadoutscanComponent } from './outscan/uploadoutscan/uploadoutscan.component';
import { PrintComponent } from './print/print.component';
  
  export interface  variant {
    partnerType:  string;
    apiSubs:  string;
    organization:  string;
    systemType:  string;
    systemType1:  string;
    systemType2:  string;
    systemType3:  string;
    systemType4:  string;
    accessType:  string;
    alerts:  string;
    activehrs:  string;
    activehrs1:  string;
    activehrs2:  string;
    field1:  string;
    field2:  string;
    field3:  string;
    field4:  string;
    field5:  string;
    field6:  string;
    field7:  string;
  } 
  const ELEMENT_DATA:  variant[] = [
    {organization:  '',apiSubs:  '',partnerType:  '',systemType:  '',systemType1:  '',systemType4:  '', systemType2:  '',systemType3:  '', accessType:  '', alerts:  '', activehrs:  '',  activehrs1:  '', 
    activehrs2:  '', field1:  '', field2:  '', field3:  '', field4:  '',
    field5:  '', field6:  '', field7:  ''},
  
  ];
  


  @Component({
    selector: 'app-consignment',
    templateUrl: './consignment.component.html',
    styleUrls: ['./consignment.component.scss']
  })
  export class ConsignmentComponent implements OnInit {
    
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
    

  
    advanceFilter(){
      this.advanceFilterShow = !this.advanceFilterShow;
    }

    
    newConsignment(): void {

      const dialogRef = this.dialog.open(NewComponent, {
        disableClose: true,
        width: '50%',
        maxWidth: '80%',
        position: { top: '6.5%', },
      });
  
      dialogRef.afterClosed().subscribe(result => {
      });
    }
   

    print(data: any = 'New'): void {

      const dialogRef = this.dialog.open(PrintComponent, {
        disableClose: true,
        width: '50%',
        maxWidth: '80%',
        position: { top: '6.5%', },
        data: { pageflow: data,}
      });
  
      dialogRef.afterClosed().subscribe(result => {
      });
    }

    uploadExcel(): void {

      const dialogRef = this.dialog.open(UploadoutscanComponent, {
        disableClose: true,
        width: '50%',
        maxWidth: '80%',
        position: { top: '6.5%', },
        //data: { pageflow: data,}
      });
  
      dialogRef.afterClosed().subscribe(result => {
      });
    }

    bulkOutScan(): void {

      const dialogRef = this.dialog.open(BulkOutscanComponent, {
        disableClose: true,
        width: '50%',
        maxWidth: '80%',
        position: { top: '6.5%', },
        //data: { pageflow: data,}
      });
  
      dialogRef.afterClosed().subscribe(result => {
      });
    }

    uploadInscan(): void {

      const dialogRef = this.dialog.open(UploadInscanComponent, {
        disableClose: true,
        width: '50%',
        maxWidth: '80%',
        position: { top: '6.5%', },
        //data: { pageflow: data,}
      });
  
      dialogRef.afterClosed().subscribe(result => {
      });
    }
   bulkInscan(): void {

      const dialogRef = this.dialog.open(BulkInscanComponent, {
        disableClose: true,
        width: '50%',
        maxWidth: '80%',
        position: { top: '6.5%', },
        //data: { pageflow: data,}
      });
  
      dialogRef.afterClosed().subscribe(result => {
      });
    }

    uploadArrival(): void {

      const dialogRef = this.dialog.open(UploadArrivalComponent, {
        disableClose: true,
        width: '50%',
        maxWidth: '80%',
        position: { top: '6.5%', },
        //data: { pageflow: data,}
      });
  
      dialogRef.afterClosed().subscribe(result => {
      });
    }

    
    bulkArrival(): void {

      const dialogRef = this.dialog.open(BulkArrivalComponent, {
        disableClose: true,
        width: '50%',
        maxWidth: '80%',
        position: { top: '6.5%', },
        //data: { pageflow: data,}
      });
  
      dialogRef.afterClosed().subscribe(result => {
      });
    }

    uploadcancel(): void {

      const dialogRef = this.dialog.open(UploadCancelComponent, {
        disableClose: true,
        width: '50%',
        maxWidth: '80%',
        position: { top: '6.5%', },
        //data: { pageflow: data,}
      });
  
      dialogRef.afterClosed().subscribe(result => {
      });
    }
  }
  
  
