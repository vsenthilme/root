import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { Table } from 'primeng/table';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
import { SetupNewComponent } from './setup-new/setup-new.component';


export interface  variant2 {
  code:  string;
  employeeName:  string;
  userName:  string;
  password:  string;
  userProfile:  string;
  accessType:  string;
  accessType1:  string;
} 
const ELEMENT_DATA2:  variant2[] = [
  {code:  '11020030',employeeName:  'Diamond',userName:  '23',password: '33',userProfile: '3445',accessType: 'Active',
  accessType1: 'Admin'}

];

@Component({
  selector: 'app-loyalty-setup',
  templateUrl: './loyalty-setup.component.html',
  styleUrls: ['./loyalty-setup.component.scss']
})
export class LoyaltySetupComponent implements OnInit {

  @ViewChild('userProfile') userProfile: Table | undefined;
  products: any;
  selectedProducts : variant2[];

  

  advanceFilterShow: boolean;

  constructor(public dialog: MatDialog, public spinner: NgxSpinnerService) { 
    
  }
    
  loyatySetup1: any;
  selectedloyatySetup1 : variant2[];

  ngOnInit(): void {
  
    this.loyatySetup1= (ELEMENT_DATA2)
    this.spinner.show();

    setTimeout(() => {
      /** spinner ends after 5 seconds */
      this.spinner.hide();
    }, 1000);
  }

  new(): void {

    const dialogRef = this.dialog.open(SetupNewComponent, {
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

