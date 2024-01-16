import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { MessageService } from 'primeng/api';
import { Table } from 'primeng/table';
import { Subscription } from 'rxjs';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
import { CommonService } from 'src/app/common-service/common-service.service';
import { LoyaltyService } from '../loyalty.service';
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

  

  advanceFilterShow: boolean;

  constructor(public dialog: MatDialog, public spinner: NgxSpinnerService, public cs: CommonService, private messageService: MessageService,
   private service: LoyaltyService){ 
    
  }
    
  loyatyCategory1: any;
  selectedloyatyCategory1 : any[] = [];

  ngOnInit(): void {
  
    this.getAll();
  }
  sub = new Subscription();
  
  getAll(){
    this.spinner.show();
    this.sub.add(this.service.Getall().subscribe((res: any[]) => {
      this.loyatyCategory1= (res);
      this.spinner.hide();
    }
    , err => {
      this.cs.commonerror(err);
      this.spinner.hide();
    }));
  }


  delete(): void {

    const dialogRef = this.dialog.open(DeleteComponent, {
      disableClose: true,
      width: '35%',
      maxWidth: '80%',
      data: this.selectedloyatyCategory1[0].customerId
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deleterecord(this.selectedloyatyCategory1[0].rangeId);
      }
    });
  }

  deleterecord(rangeId: any) {
    this.spinner.show();
    this.sub.add(this.service.Delete(rangeId).subscribe((res) => {
      this.messageService.add({key: 'br', severity:'success', summary:'Success', detail: rangeId + " Deleted successfully"});
      this.spinner.hide();
      this.getAll();
    }, err => {
      this.cs.commonerror(err);
      this.spinner.hide();
    }));
  }

  onChange() {
    const choosen= this.selectedloyatyCategory1[this.selectedloyatyCategory1.length - 1];   
    this.selectedloyatyCategory1.length = 0;
    this.selectedloyatyCategory1.push(choosen);
  }


  
  openDialog(data: any = 'New'): void {
    if (this.selectedloyatyCategory1.length === 0 && data != 'New') {
      console.log(2)
      this.messageService.add({key: 'br', severity:'warn', summary:'Warning', detail:'Kindly select any Row'});

      return;
    }
    const dialogRef = this.dialog.open(CategoryNewComponent, {
      disableClose: true,
     // width: '55%',
      maxWidth: '80%',
      data: { pageflow: data, code: data != 'New' ? this.selectedloyatyCategory1[0] : null}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.getAll();
    });
  }
  downloadexcel( ) {
    var res: any = [];
    this.loyatyCategory1.forEach(x => {
      res.push({
       "Category ID":x.categoryId,
       "Category":x.category,
       "Layolty Points-From":x.pointsFrom,
       "Loyalty Points-To":x.pointsTo,
       "Credit Value":x.creditValuePoint,
       "Credit Unit":x.creditUnit,
       "Status":x.status,
       "Created By":x.createdBy,
       "Created On":this.cs.dateapi(x.createdOn),
      
      });
  
    })
    this.cs.exportAsExcel(res, "Loyalty Category");
  }

}
