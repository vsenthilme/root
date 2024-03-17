import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Table } from 'primeng/table';
import { UserprofileNewComponent } from './userprofile-new/userprofile-new.component';
import * as FileSaver from 'file-saver';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';

export interface  variant {
  partner:  string;
  profileType:  string;
  userName:  string;
} 
const ELEMENT_DATA:  variant[] = [
  {userName:  'IWE_ADMIN',profileType:  'Admin',partner:  'LMD'},
  {userName:  'IWE_OPR',profileType:  'Operations',partner:  'LMD'},
  {userName:  'IWE_REP',profileType:  'Report User',partner:  'LMD'}

];


@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss'],
  
})
export class UserProfileComponent implements OnInit {
  
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

    const dialogRef = this.dialog.open(UserprofileNewComponent, {
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
