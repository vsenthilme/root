import { SelectionModel } from '@angular/cdk/collections';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Table } from 'primeng/table';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { PricelistNewComponent } from '../pricelist/pricelist-new/pricelist-new.component';
import { BillingNewComponent } from './billing-new/billing-new.component';
import { BillingService } from './billing.service';
import { Router } from '@angular/router';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
import { BinlocationService } from '../../masternew/binlocation/binlocation.service';

@Component({
  selector: 'app-billing',
  templateUrl: './billing.component.html',
  styleUrls: ['./billing.component.scss']
})
export class BillingComponent implements OnInit {

  advanceFilterShow: boolean;
  @ViewChild('Setupbilling') Setupbilling: Table | undefined;
  billing: any[] = [];
  selectedbilling : any[] = [];
  sub = new Subscription();
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  public icon = 'expand_more';

  ELEMENT_DATA: any[] = [];
  
  constructor(public dialog: MatDialog,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private router: Router,
    public cs: CommonService,
   // private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService,
    private service:  BillingService) { }
    toggleFloat() {

      this.isShowDiv = !this.isShowDiv;
      this.toggle = !this.toggle;
  
      if (this.icon === 'expand_more') {
        this.icon = 'chevron_left';
      } else {
        this.icon = 'expand_more'
      }
      this.showFloatingButtons = !this.showFloatingButtons;
  
    }
    showFiller = false;
    animal: string | undefined;
   
    applyFilterGlobal($event: any, stringVal: any) {
      this.Setupbilling!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
    }
    
    ngOnInit(): void {
      this.getAll();
    }

    warehouseId = this.auth.warehouseId;
    /** Whether the number of selected elements matches the total number of rows. */
   
  openDialog(data: any = 'New'): void {
    if (data != 'New'){
      if (this.selectedbilling.length == 0) {
        this.toastr.warning("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
    }
    let paramdata = "";
    paramdata = this.cs.encrypt({ pageflow: data, code: data != 'New' ? this.selectedbilling[0].partnerCode : null,warehouseId: data != 'New' ? this.selectedbilling[0].warehouseId : null,languageId: data != 'New' ? this.selectedbilling[0].languageId : null, plantId: data != 'New' ? this.selectedbilling[0].plantId : null,companyCodeId: data != 'New' ? this.selectedbilling[0].companyCodeId : null,moduleId: data != 'New' ? this.selectedbilling[0].moduleId : null});
    this.router.navigate(['/main/threePLmaster/billingNew/' + paramdata]);
  }
  
  getAll() {
  
      this.adminUser()
    
  }
  
  adminUser(){
     let obj: any = {};
      obj.companyCodeId = this.auth.companyId;
     obj.plantId = this.auth.plantId;
     obj.languageId = [this.auth.languageId];
     obj.warehouseId = this.auth.warehouseId;
    this.spin.show();
    this.sub.add(this.service.search(obj).subscribe((res: any[]) => {
      console.log(res)
if(res){
   this.billing = res;

}
      this.spin.hide();
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
    }));
  }

  deleteDialog() {
    if (this.selectedbilling.length === 0) {
      this.toastr.error("Kindly select any row", "Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    const dialogRef = this.dialog.open(DeleteComponent, {
      disableClose: true,
      width: '40%',
      maxWidth: '80%',
      position: { top: '9%', },
      data: this.selectedbilling[0].code,
    });
  
    dialogRef.afterClosed().subscribe(result => {
  
      if (result) {
        this.deleterecord(this.selectedbilling[0].partnerCode,this.selectedbilling[0].warehouseId,this.selectedbilling[0].languageId,this.selectedbilling[0].plantId,this.selectedbilling[0].companyCodeId,this.selectedbilling[0].moduleId);
  
      }
    });
  }
  
  
  deleterecord(id: any,warehouseId:any,languageId:any,plantId:any,companyCodeId:any,moduleId:any) {
    this.spin.show();
    this.sub.add(this.service.Delete(id,warehouseId,languageId,plantId,companyCodeId,moduleId).subscribe((res) => {
      this.toastr.success(id + " Deleted successfully.","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.spin.hide();
      this.getAll();
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
    }));
  }
  downloadexcel() {
    var res: any = [];
    this.billing.forEach(x => {
      res.push({
      "Product Code ":x.itemCode,
        "Item Group ":x.itemGroup,
        "Item Type":x.itemType,
        "Maximum Stock":x.maximumStock,
        "Minimum Stock":x.minimumStock,
        "Total Stock":x.totalStock,
        "Base UOM":x.uomId,
        "Created By":x.createdBy,
       "Created One":this.cs.dateapi(x.createdOn),
      });
  
    })
    this.cs.exportAsExcel(res, "Bin Location");
  }
  onChange() {
    const choosen= this.selectedbilling[this.selectedbilling.length - 1];   
    this.selectedbilling.length = 0;
    this.selectedbilling.push(choosen);
  } 
  }
   
  
  
  




