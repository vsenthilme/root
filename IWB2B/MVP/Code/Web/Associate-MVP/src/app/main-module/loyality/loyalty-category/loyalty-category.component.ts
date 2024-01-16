import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { Table } from 'primeng/table';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
import { CategoryNewComponent } from './category-new/category-new.component';

export interface  variant2 {
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
const ELEMENT_DATA2:  variant2[] = [
  {code:  '11020030',employeeName:  'Diamond',userName:  '23',password: '33',userProfile: '3445',accessType: 'Percentage',
  accessType1: 'Active',accessType2: 'Admin', accessType3: '15-02-2023'}

];


@Component({
  selector: 'app-loyalty-category',
  templateUrl: './loyalty-category.component.html',
  styleUrls: ['./loyalty-category.component.scss']
})
export class LoyaltyCategoryComponent implements OnInit {

  @ViewChild('loyatyCategory') loyatyCategory: Table | undefined;
  products: any;
  selectedProducts : variant2[];

  

  advanceFilterShow: boolean;

  constructor(public dialog: MatDialog, public spinner: NgxSpinnerService) { 
    
  }
    
  loyatyCategory1: any;
  selectedloyatyCategory1 : variant2[];

  ngOnInit(): void {
  
    this.loyatyCategory1= (ELEMENT_DATA2)
    this.spinner.show();

    setTimeout(() => {
      /** spinner ends after 5 seconds */
      this.spinner.hide();
    }, 1000);
  }

  
  new(): void {

    const dialogRef = this.dialog.open(CategoryNewComponent, {
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
