import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { MessageService } from 'primeng/api';
import { Table } from 'primeng/table';
import { Subscription } from 'rxjs';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
import { CommonService } from 'src/app/common-service/common-service.service';
import { CategoryNewComponent } from '../loyalty-category/category-new/category-new.component';
import { LoyaltyService } from '../loyalty.service';
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

  constructor(public dialog: MatDialog, public spinner: NgxSpinnerService, public cs: CommonService, private messageService: MessageService,
    private service: LoyaltyService){ 
    
  }
    
  loyatySetup1: any;
  selectedloyatySetup1 : any[] = [];



  ngOnInit(): void {
  
    this.getAll();
  }
  sub = new Subscription();
  
  getAll(){
    this.spinner.show();
    this.sub.add(this.service.SetupGetall().subscribe((res: any[]) => {
      this.loyatySetup1= (res);
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
      data: this.selectedloyatySetup1[0].loyaltyId
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deleterecord(this.selectedloyatySetup1[0].loyaltyId,this.selectedloyatySetup1[0].companyId, this.selectedloyatySetup1[0].languageId, this.selectedloyatySetup1[0].categoryId);
      }
    });
  }

  deleterecord(loyaltyId: any, companyId: any, languageId: any, categoryId: any) {
    this.spinner.show();
    this.sub.add(this.service.SetupDelete(loyaltyId, companyId, languageId, categoryId,).subscribe((res) => {
      this.messageService.add({key: 'br', severity:'success', summary:'Success', detail: loyaltyId + " Deleted successfully"});
      this.spinner.hide();
      this.getAll();
    }, err => {
      this.cs.commonerror(err);
      this.spinner.hide();
    }));
  }

  onChange() {
    const choosen= this.selectedloyatySetup1[this.selectedloyatySetup1.length - 1];   
    this.selectedloyatySetup1.length = 0;
    this.selectedloyatySetup1.push(choosen);
  }


  
  openDialog(data: any = 'New'): void {
    if (this.selectedloyatySetup1.length === 0 && data != 'New') {
      console.log(2)
      this.messageService.add({key: 'br', severity:'warn', summary:'Warning', detail:'Kindly select any Row'});

      return;
    }
    const dialogRef = this.dialog.open(SetupNewComponent, {
      disableClose: true,
     // width: '55%',
      maxWidth: '80%',
      data: { pageflow: data, code: data != 'New' ? this.selectedloyatySetup1[0] : null}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.getAll();
    });
  }
  downloadexcel( ) {
    var res: any = [];
    this.loyatySetup1.forEach(x => {
      res.push({
       "Loyalty ID":x.loyaltyId,
       "Category":x.categoryId,
       "From Delivery Charge":x.transactionValueFrom,
       "To Delivery Charge":x.transactionValueTo,
       "Loyalty Points":x.loyaltyPoint,
       "Status":x.status,
       "Created By":x.createdBy,
       "Created On":this.cs.dateapi(x.createdOn),
      
      });
  
    })
    this.cs.exportAsExcel(res, "Loyalty Setup");
  }

}

