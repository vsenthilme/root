import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NgxSpinner, NgxSpinnerService } from 'ngx-spinner';
import { MenuItem, MessageService } from 'primeng/api';
import { Table } from 'primeng/table';
import { Subscription } from 'rxjs';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ConsignmentLinesComponent } from '../../consignments/consignment-new/consignment-lines/consignment-lines.component';
import { CustomerService } from '../customer.service';
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
  selectedProducts : any[] = [];

  

  advanceFilterShow: boolean;

  constructor(public dialog: MatDialog, public spinner: NgxSpinnerService, public customerService: CustomerService,
    public cs: CommonService, private messageService: MessageService){ 
    
  }
  sub = new Subscription();
  ngOnInit(): void {
   this.getAll();
  }

  // applyFilterGlobal($event: any, stringVal: any) {
  //   this.userProfile!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  // }

    
  getAll(){
    this.spinner.show();
    this.sub.add(this.customerService.Getall().subscribe((res: any[]) => {
      this.products= (res);
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
      data: this.selectedProducts[0].customerId
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deleterecord(this.selectedProducts[0].customerId,this.selectedProducts[0].companyId, this.selectedProducts[0].languageId,);
      }
    });
  }

  deleterecord(customerId: any, companyId: any, languageId: any,) {
    this.spinner.show();
    this.sub.add(this.customerService.Delete(customerId, companyId, languageId, ).subscribe((res) => {
      this.messageService.add({key: 'br', severity:'success', summary:'Success', detail: customerId + " Deleted successfully"});
      this.spinner.hide();
      this.getAll();
    }, err => {
      this.cs.commonerror(err);
      this.spinner.hide();
    }));
  }

  onChange() {
    const choosen= this.selectedProducts[this.selectedProducts.length - 1];   
    this.selectedProducts.length = 0;
    this.selectedProducts.push(choosen);
  }


  
  openDialog(data: any = 'New'): void {
    if (this.selectedProducts.length === 0 && data != 'New') {
      this.messageService.add({key: 'br', severity:'warn', summary:'Warning', detail:'Kindly select any Row'});

      return;
    }
    const dialogRef = this.dialog.open(ProfileNewComponent, {
      disableClose: true,
     // width: '55%',
      maxWidth: '80%',
      data: { pageflow: data, code: data != 'New' ? this.selectedProducts[0] : null}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.getAll();
    });
  }
 downloadexcel( ) {
    var res: any = [];
    this.products.forEach(x => {
      res.push({
       "Customer ID":x.customerId,
       "Customer Name":x.customerName,
       "Cateogry":x.customerCategory,
       "Phone Number":x.phoneNo,
       "Status":x.status,
       "Created By":x.createdBy,
       "Created On":this.cs.dateapi(x.createdOn),
      
      });
  
    })
    this.cs.exportAsExcel(res, "Customer Profile");
  }


}
