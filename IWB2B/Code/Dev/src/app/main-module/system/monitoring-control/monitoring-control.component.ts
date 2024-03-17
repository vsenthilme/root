
  import { Component, OnInit, ViewChild } from '@angular/core';
  import { MatDialog } from '@angular/material/dialog';
  import { Table } from 'primeng/table';
  import * as FileSaver from 'file-saver';
  import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
import { MonitoringNewComponent } from './monitoring-new/monitoring-new.component';
  
  export interface  variant {
    partnerType:  string;
    apiSubs:  string;
    organization:  string;
    systemType:  string;
    systemType1:  string;
    systemType2:  string;
    accessType:  string;
  } 
  const ELEMENT_DATA:  variant[] = [
    {organization:  '1001',apiSubs:  'Shipsy',partnerType:  'Asyad',systemType:  'Shipsy',systemType1:  'LMD Services API', systemType2:  'Source To Target Estabilished ', accessType:  '1.33.00 PM'},
  
  ];
  
  
  @Component({
    selector: 'app-monitoring-control',
    templateUrl: './monitoring-control.component.html',
    styleUrls: ['./monitoring-control.component.scss']
  })
  export class MonitoringControlComponent implements OnInit {
  
    
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
  
      const dialogRef = this.dialog.open(MonitoringNewComponent, {
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
  
  