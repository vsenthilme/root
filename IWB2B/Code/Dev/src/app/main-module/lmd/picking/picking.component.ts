import { Component, OnInit, ViewChild } from '@angular/core';
  import { MatDialog } from '@angular/material/dialog';
  import { Table } from 'primeng/table';
import { PickupNewComponent } from './pickup-new/pickup-new.component';
import { UploadExcelComponent } from './upload-excel/upload-excel.component';
  
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
    selector: 'app-picking',
    templateUrl: './picking.component.html',
    styleUrls: ['./picking.component.scss']
  })
  export class PickingComponent implements OnInit {
  
    
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

    
    newPickup(): void {

      const dialogRef = this.dialog.open(PickupNewComponent, {
        disableClose: true,
        width: '80%',
        maxWidth: '80%',
        position: { top: '6.5%', },
      });
  
      dialogRef.afterClosed().subscribe(result => {
      });
    }
   

    excelAssign(): void {

      const dialogRef = this.dialog.open(UploadExcelComponent, {
        disableClose: true,
        width: '50%',
        maxWidth: '80%',
        position: { top: '6.5%', },
      });
  
      dialogRef.afterClosed().subscribe(result => {
      });
    }
  }
  
  
