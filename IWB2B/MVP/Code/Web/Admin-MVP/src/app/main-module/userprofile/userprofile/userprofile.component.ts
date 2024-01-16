import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { MessageService } from 'primeng/api';
import { Table } from 'primeng/table';
import { Subscription } from 'rxjs';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
import { CommonService } from 'src/app/common-service/common-service.service';
import { UserprofileNewComponent } from './userprofile-new/userprofile-new.component';
import { UserprofileService } from './userprofile.service';

@Component({
  selector: 'app-userprofile',
  templateUrl: './userprofile.component.html',
  styleUrls: ['./userprofile.component.scss']
})
export class UserprofileComponent implements OnInit {

  @ViewChild('Setupuser') Setupuser: Table | undefined;
  products: any;
  selectedProducts : any[] = [];

  

  advanceFilterShow: boolean;

  constructor(public dialog: MatDialog, public spinner: NgxSpinnerService, public customerService: UserprofileService,
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
      data: this.selectedProducts[0].id
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deleterecord(this.selectedProducts[0].id);
      }
    });
  }

  deleterecord(id: any,) {
    this.spinner.show();
    this.sub.add(this.customerService.Delete(id ).subscribe((res) => {
      this.messageService.add({key: 'br', severity:'success', summary:'Success', detail: id + " Deleted successfully"});
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
    const dialogRef = this.dialog.open(UserprofileNewComponent, {
      disableClose: true,
     // width: '55%',
      maxWidth: '80%',
      data: { pageflow: data, code: data != 'New' ? this.selectedProducts[0] : null}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.getAll();
    });
  }

  downloadexcel() {
    var res: any = [];
    this.products.forEach(x => {
      res.push({
       "User Name":x.username,
       "Email":x.email,
       "First Name":x.firstname,
       "Second Name":x.lastname,
       "Role":x.role,
       "Phone No":x.phoneNo,
       "Status":x.status,
       "Created By":x.createdBy,
       "Created One":this.cs.dateapi(x.createdOn),
      
      });
  
    })
    this.cs.exportAsExcel(res, "User Profile");
  }

}

