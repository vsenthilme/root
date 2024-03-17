import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Table } from 'primeng/table';
import * as FileSaver from 'file-saver';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
import { UserCreationNewComponent } from './user-creation-new/user-creation-new.component';

export interface  variant {
  code:  string;
  partnerName:  string;
  employeeName:  string;
  userName:  string;
  password:  string;
  userProfile:  string;
  accessType:  string;
  systemType:  string;
  userActive:  string;
} 
const ELEMENT_DATA:  variant[] = [
  {code:  '10001',partnerName:  'IWE',employeeName:  'IWE ADMIN',userName:  'IWE_ADMIN',password: '******',userProfile: 'ADMIN',accessType: 'Full Access',
  systemType: 'IWE',userActive: 'Yes',},
  
  {code:  '10002',partnerName:  'Asyd',employeeName:  'ASYAD ADMIN',userName:  'AS_ADMIN',password: '******',userProfile: 'ADMIN',accessType: 'Full Access',
  systemType: 'LMD',userActive: 'Yes',},

  {code:  '10003',partnerName:  'Naquel',employeeName:  'NAQUEL ADMIN',userName:  'NAQUEL_ADMIN',password: '******',userProfile: 'ADMIN',accessType: 'Full Access',
  systemType: 'LMD',userActive: 'Yes',},

];



@Component({
  selector: 'app-user-creation',
  templateUrl: './user-creation.component.html',
  styleUrls: ['./user-creation.component.scss']
})
export class UserCreationComponent implements OnInit {
  
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

    const dialogRef = this.dialog.open(UserCreationNewComponent, {
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
