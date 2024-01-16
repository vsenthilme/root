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
import { Router } from '@angular/router';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { PricelistNewComponent } from '../pricelist/pricelist-new/pricelist-new.component';
import { PricelistassignmentNewComponent } from './pricelistassignment-new/pricelistassignment-new.component';
import { PricelistassignmentService } from './pricelistassignment.service';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';

@Component({
  selector: 'app-pricelistassignment',
  templateUrl: './pricelistassignment.component.html',
  styleUrls: ['./pricelistassignment.component.scss']
})
export class PricelistassignmentComponent implements OnInit {
  screenid=3203;
  advanceFilterShow: boolean;
  @ViewChild('Setuppricelistass') Setuppricelistass: Table | undefined;
  pricelistassignemnt: any;
  selectedpricelistas : any;
 
 
  
 
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
    private service: PricelistassignmentService ) { }
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
      this.Setuppricelistass!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
    }
    
    ngOnInit(): void {
      this.getAll();
    }

   
    /** Whether the number of selected elements matches the total number of rows. */
   
  openDialog(data: any = 'New'): void {
    if (data != 'New'){
      if (this.selectedpricelistas.length == 0) {
        this.toastr.warning("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
    }
    let paramdata = "";
    paramdata = this.cs.encrypt({ pageflow: data, code: data != 'New' ? this.selectedpricelistas[0].priceListId : null,warehouseId: data != 'New' ? this.selectedpricelistas[0].warehouseId : null,languageId: data != 'New' ? this.selectedpricelistas[0].languageId : null, plantId: data != 'New' ? this.selectedpricelistas[0].plantId : null,companyCodeId: data != 'New' ? this.selectedpricelistas[0].companyCodeId : null,partnerCode: data != 'New' ? this.selectedpricelistas[0].partnerCode : null,});
    this.router.navigate(['/main/threePLmaster/pricelistassignNew/' + paramdata]);
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
   this.pricelistassignemnt = res;

}
      this.spin.hide();
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
    }));
  }
  
  deleteDialog() {
    if (this.pricelistassignemnt.length === 0) {
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
      data: this.selectedpricelistas[0].code,
    });
  
    dialogRef.afterClosed().subscribe(result => {
  
      if (result) {
        this.deleterecord(this.selectedpricelistas[0].priceListId,this.selectedpricelistas[0].warehouseId,this.selectedpricelistas[0].languageId,this.selectedpricelistas[0].plantId,this.selectedpricelistas[0].companyCodeId,this.selectedpricelistas[0].partnerCode);
  
      }
    });
  }
  
  
  deleterecord(id: any,warehouseId:any,languageId:any,plantId:any,companyCodeId:any,partnerCode:any) {
    this.spin.show();
    this.sub.add(this.service.Delete(id,warehouseId,languageId,plantId,companyCodeId,partnerCode).subscribe((res) => {
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
    this.pricelistassignemnt.forEach(x => {
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
    const choosen= this.selectedpricelistas[this.selectedpricelistas.length - 1];   
    this.selectedpricelistas.length = 0;
    this.selectedpricelistas.push(choosen);
  } 
  }
   
  
  
  




