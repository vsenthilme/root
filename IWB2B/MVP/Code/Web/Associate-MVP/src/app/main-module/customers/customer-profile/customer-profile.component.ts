import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NgxSpinner, NgxSpinnerService } from 'ngx-spinner';
import { MenuItem } from 'primeng/api';
import { Table } from 'primeng/table';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
import { ProfileNewComponent } from './profile-new/profile-new.component';


export interface  variant {
  code:  string;
  partnerName:  string;
  employeeName:  string;
  userName:  string;
  password:  string;
  userProfile:  string;
  accessType:  string;
} 
const ELEMENT_DATA:  variant[] = [
  {code:  '10001',partnerName:  'Ashraf',employeeName:  'Associtate',userName:  '965-3221-3222',password: 'Active',userProfile: 'ADMIN',accessType: '15-02-2023'},
  
  {code:  '10002',partnerName:  'Amir',employeeName:  'Associtate',userName:  '965-3221-3222',password: 'Active',userProfile: 'ADMIN',accessType: '15-02-2023'},

  {code:  '10003',partnerName:  'Mohamed',employeeName:  'Associtate',userName:  '965-3221-3222',password: 'Active',userProfile: 'ADMIN',accessType: '15-02-2023'},

];

@Component({
  selector: 'app-customer-profile',
  templateUrl: './customer-profile.component.html',
  styleUrls: ['./customer-profile.component.scss']
})
export class CustomerProfileComponent implements OnInit {
  @ViewChild('userProfile') userProfile: Table | undefined;
  products: any;
  selectedProducts : variant[];

  

  advanceFilterShow: boolean;

  constructor(public dialog: MatDialog, public spinner: NgxSpinnerService) { 
    
  }

  ngOnInit(): void {
    this.products= (ELEMENT_DATA)
    this.spinner.show();

    setTimeout(() => {
      /** spinner ends after 5 seconds */
      this.spinner.hide();
    }, 1000);
    
  }

  // applyFilterGlobal($event: any, stringVal: any) {
  //   this.userProfile!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  // }

    

  new(): void {

    const dialogRef = this.dialog.open(ProfileNewComponent, {
      disableClose: true,
      width: '70%',
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

}
