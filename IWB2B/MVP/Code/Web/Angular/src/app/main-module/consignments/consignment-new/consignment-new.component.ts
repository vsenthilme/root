import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { MenuItem } from 'primeng/api';
import { Table } from 'primeng/table';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
import { ConsignmentNewPopupComponent } from './consignment-new-popup/consignment-new-popup.component';

export interface  variant1 {
  code:  string;
  employeeName:  string;
  userName:  string;
  password:  string;
  userProfile:  string;
  accessType:  string;
  accessType1:  string;
  accessType2:  string;
  accessType3:  string;
} 
const ELEMENT_DATA1:  variant1[] = [
  {code:  '11020030',employeeName:  'Abdullah',userName:  '13132, Mubarak Al-kabir',password: 'P.O.Box: 15, Arabian Gulf Street, Safat 13001  Kuwait City',userProfile: 'Gift items',accessType: '5',
  accessType1: '5',accessType2: '11-02-2023', accessType3: 'New'}

];


@Component({
  selector: 'app-consignment-new',
  templateUrl: './consignment-new.component.html',
  styleUrls: ['./consignment-new.component.scss']
})
export class ConsignmentNewComponent implements OnInit {
  @ViewChild('consignmentNew') consignmentNew: Table | undefined;

  
  consignmentNew1: any;
  selectedconsignmentNew1 : variant1[];


  

  advanceFilterShow: boolean;

  constructor(public dialog: MatDialog, public spinner: NgxSpinnerService) { 
    
  }

  ngOnInit(): void {
  
    this.consignmentNew1= (ELEMENT_DATA1)
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

    const dialogRef = this.dialog.open(ConsignmentNewPopupComponent, {
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


