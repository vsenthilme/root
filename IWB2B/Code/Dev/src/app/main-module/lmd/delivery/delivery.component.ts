
  
  import { Component, OnInit, ViewChild } from '@angular/core';
  import { MatDialog } from '@angular/material/dialog';
  import { Table } from 'primeng/table';
import { BulkAssignComponent } from './bulk-assign/bulk-assign.component';
import { ExcelAssignComponent } from './excel-assign/excel-assign.component';
import { PrintLabelComponent } from './print-label/print-label.component';
  
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
  } 
  const ELEMENT_DATA:  variant[] = [
    {organization:  'ASD00226',apiSubs:  'Asyad',partnerType:  '9809810',systemType:  'Ahmed',systemType1:  'Kuwait Kuwait',systemType4:  '+96895882803', systemType2:  '0',systemType3:  'PENDING', accessType:  'Medium', alerts:  '2', activehrs:  'Premium',  activehrs1:  '17-09-22', activehrs2:  '17-09-22',},
  
  ];
  
  @Component({
    selector: 'app-delivery',
    templateUrl: './delivery.component.html',
    styleUrls: ['./delivery.component.scss']
  })
  export class DeliveryComponent implements OnInit {
  
    
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

    bulkAssign(): void {

      const dialogRef = this.dialog.open(BulkAssignComponent, {
        disableClose: true,
        width: '50%',
        maxWidth: '80%',
        position: { top: '6.5%', },
      });
  
      dialogRef.afterClosed().subscribe(result => {
      });
    }

    excelAssign(): void {

      const dialogRef = this.dialog.open(ExcelAssignComponent, {
        disableClose: true,
        width: '50%',
        maxWidth: '80%',
        position: { top: '6.5%', },
      });
  
      dialogRef.afterClosed().subscribe(result => {
      });
    }
    printLabel(): void {

      const dialogRef = this.dialog.open(PrintLabelComponent, {
        disableClose: true,
        width: '50%',
        maxWidth: '80%',
        position: { top: '6.5%', },
      });
  
      dialogRef.afterClosed().subscribe(result => {
      });
    }
  }
  
  
