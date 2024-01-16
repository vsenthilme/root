

  import { Component, OnInit, ViewChild } from '@angular/core';
  import { MatDialog } from '@angular/material/dialog';
  import { Table } from 'primeng/table';
  import * as FileSaver from 'file-saver';
  import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
  
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
    {organization:  '10001',apiSubs:  'Shipsy',partnerType:  'Asyad',systemType:  'Shipsy',systemType1:  '0.03mins', systemType2:  '2', accessType:  '45mins'},
  
  ];
  

  @Component({
    selector: 'app-archives-log',
    templateUrl: './archives-log.component.html',
    styleUrls: ['./archives-log.component.scss']
  })
  export class ArchivesLogComponent implements OnInit {
  
    
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
  
  