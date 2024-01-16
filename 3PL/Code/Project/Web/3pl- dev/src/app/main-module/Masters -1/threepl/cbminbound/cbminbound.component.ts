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
import { PricelistassignmentNewComponent } from '../pricelistassignment/pricelistassignment-new/pricelistassignment-new.component';
import { CbminboundNewComponent } from './cbminbound-new/cbminbound-new.component';
import { CbminboundService } from './cbminbound.service';
import { Router } from '@angular/router';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';

@Component({
  selector: 'app-cbminbound',
  templateUrl: './cbminbound.component.html',
  styleUrls: ['./cbminbound.component.scss']
})
export class CbminboundComponent implements OnInit {
  screenid=3204;
  advanceFilterShow: boolean;
  @ViewChild('Setupcbminbound') Setupcbminbound: Table | undefined;
  cbminbound : any;
  selectedcbm : any;
 
 
  
 
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
    private service: CbminboundService ) { }
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
      this.Setupcbminbound!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
    }
    
    ngOnInit(): void {
      this.getAll();
    }

    warehouseId = this.auth.warehouseId;
    /** Whether the number of selected elements matches the total number of rows. */
   
  openDialog(data: any = 'New'): void {
    if (data != 'New'){
      if (this.selectedcbm.length == 0) {
        this.toastr.warning("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
    }
    let paramdata = "";
    paramdata = this.cs.encrypt({ pageflow: data, code: data != 'New' ? this.selectedcbm[0].itemCode : null,warehouseId: data != 'New' ? this.selectedcbm[0].warehouseId : null,languageId: data != 'New' ? this.selectedcbm[0].languageId : null, plantId: data != 'New' ? this.selectedcbm[0].plantId : null,companyCodeId: data != 'New' ? this.selectedcbm[0].companyCodeId : null,partnerCode: data != 'New' ? this.selectedcbm[0].partnerCode : null,});
    this.router.navigate(['/main/threePLmaster/cbmNew/' + paramdata]);
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
    this.sub.add(this.service.search({}).subscribe((res: any[]) => {
      console.log(res)
if(res){
   this.cbminbound = res;

}
      this.spin.hide();
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
    }));
  }
  superAdmin(){
    this.spin.show();
    this.sub.add(this.service.Getall().subscribe((res: any[]) => {
      if(res){
        this.cbminbound=res;
       } this.spin.hide();
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
    }));
  }
  deleteDialog() {
    if (this.cbminbound.length === 0) {
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
      data: this.selectedcbm[0].code,
    });
  
    dialogRef.afterClosed().subscribe(result => {
  
      if (result) {
        this.deleterecord(this.selectedcbm[0].itemCode,this.selectedcbm[0].warehouseId,this.selectedcbm[0].languageId,this.selectedcbm[0].plantId,this.selectedcbm[0].companyCodeId);
  
      }
    });
  }
  
  
  deleterecord(id: any,warehouseId:any,languageId:any,plantId:any,companyCodeId:any) {
    this.spin.show();
    this.sub.add(this.service.Delete(id,warehouseId,languageId,plantId,companyCodeId).subscribe((res) => {
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
    this.cbminbound.forEach(x => {
      res.push({
      "Product Code ":x.itemCode,
        "UOM ":x.uomId,
        "Length":x.length,
        "Width":x.width,
        "Height":x.height,
        "CBM":x.cbm,
        "Created By":x.createdBy,
       "Created One":this.cs.dateapi(x.createdOn),
       "Status":x.statusId =="0"?'Active':'Inactive',
      });
  
    })
    this.cs.exportAsExcel(res, "CBM Inbound");
  }
  onChange() {
    const choosen= this.selectedcbm[this.selectedcbm.length - 1];   
    this.selectedcbm.length = 0;
    this.selectedcbm.push(choosen);
  } 
  }
   
  
  
  




