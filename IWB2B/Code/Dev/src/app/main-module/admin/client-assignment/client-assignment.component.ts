

  import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Table } from 'primeng/table';
import * as FileSaver from 'file-saver';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
import { ClientAssignmentNewComponent } from './client-assignment-new/client-assignment-new.component';

export interface  variant {
  partnerType:  string;
  apiSubs:  string;
  organization:  string;
  systemType:  string;
  accessType:  string;
} 
const ELEMENT_DATA:  variant[] = [
  {organization:  'Asyad',apiSubs:  'LMD',partnerType:  'Shipsy',systemType:  'LMD',accessType:  'Full Access'},
  {organization:  'Naquel',apiSubs:  'LMD',partnerType:  'Shipsy',systemType:  'LMD',accessType:  'Full Access'},
  {organization:  'Ecomm Partner',apiSubs:  'ECOMM',partnerType:  'Ecomm',systemType:  'Ecomm',accessType:  'Full Access'},

];


@Component({
  selector: 'app-client-assignment',
  templateUrl: './client-assignment.component.html',
  styleUrls: ['./client-assignment.component.scss']
})
export class ClientAssignmentComponent implements OnInit {
  
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

    const dialogRef = this.dialog.open(ClientAssignmentNewComponent, {
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
