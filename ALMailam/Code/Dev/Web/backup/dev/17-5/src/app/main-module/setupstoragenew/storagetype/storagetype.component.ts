import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Table } from 'primeng/table';
import { Subscription } from 'rxjs';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { StoragetypeService } from './storagetype.service';

@Component({
  selector: 'app-storagetype',
  templateUrl: './storagetype.component.html',
  styleUrls: ['./storagetype.component.scss']
})
export class StoragetypeComponent implements OnInit {

  advanceFilterShow: boolean;
  @ViewChild('setupstoragetype') setupstoragetype: Table | undefined;
  storagetype: any[] = [];
  selectedstoragetype : any[] = [];
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
    private service:  StoragetypeService) { }
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
      this.setupstoragetype!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
    }
    
    ngOnInit(): void {
      this.getAll();
    }

    warehouseId = this.auth.warehouseId;
    /** Whether the number of selected elements matches the total number of rows. */
   
  openDialog(data: any = 'New'): void {
    if (data != 'New'){
      if (this.selectedstoragetype.length == 0) {
        this.toastr.warning("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
    }
    let paramdata = "";
    paramdata = this.cs.encrypt({ pageflow: data, code: data != 'New' ? this.selectedstoragetype[0].storageTypeId : null,warehouseId: data != 'New' ? this.selectedstoragetype[0].warehouseId : null,storageClassId: data != 'New' ? this.selectedstoragetype[0].storageClassId : null,languageId: data != 'New' ? this.selectedstoragetype[0].languageId : null,plantId: data != 'New' ? this.selectedstoragetype[0].plantId : null,companyId: data != 'New' ? this.selectedstoragetype[0].companyId : null});
    this.router.navigate(['/main/productstorage/storagetypeNew/' + paramdata]);
  }
  getAll() {
    if(this.auth.userTypeId == 1){
      this.superAdmin()
    }else{
      this.adminUser()
    }
  }
  
  adminUser(){
    let obj: any = {};
    obj.companyCodeId = this.auth.companyId;
    obj.plantId = this.auth.plantId;
    obj.languageId = this.auth.languageId;
    obj.warehouseId = this.auth.warehouseId;
  
  this.spin.show();
  this.sub.add(this.service.search(obj).subscribe((res: any[]) => {
    console.log(res)
    if(res){
      this.storagetype=res;
  
    }this.spin.hide();
  }, err => {
    this.cs.commonerrorNew(err);
    this.spin.hide();
  }));
  }
  superAdmin(){
    this.spin.show();
    this.sub.add(this.service.Getall().subscribe((res: any[]) => {
      if(res){
        this.storagetype=res;
       } this.spin.hide();
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
    }));
  }
  
  deleteDialog() {
    if (this.selectedstoragetype.length === 0) {
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
      data: this.selectedstoragetype[0].code,
    });
  
    dialogRef.afterClosed().subscribe(result => {
  
      if (result) {
        this.deleterecord(this.selectedstoragetype[0].storageTypeId,this.selectedstoragetype[0].warehouseId,this.selectedstoragetype[0].storageClassId,this.selectedstoragetype[0].plantId,this.selectedstoragetype[0].languageId,this.selectedstoragetype[0].companyId);
  
      }
    });
  }
  
  
  deleterecord(id: any,warehouseId:any,storageClassId:any,plantId:any,languageId:any,companyId:any) {
    this.spin.show();
    this.sub.add(this.service.Delete(id,warehouseId,storageClassId,plantId,languageId,companyId).subscribe((res) => {
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
    this.storagetype.forEach(x => {
      res.push({
      "Language Id":x.languageId,
        "Storage Class Id":x.storageclassId,
        "Description":x.description,
        "Company ID":x.companyId,
        "Created By":x.createdBy,
       "Created One":this.cs.dateapi(x.createdOn),
      });
  
    })
    this.cs.exportAsExcel(res, "Storage Type");
  }
  onChange() {
    const choosen= this.selectedstoragetype[this.selectedstoragetype.length - 1];   
    this.selectedstoragetype.length = 0;
    this.selectedstoragetype.push(choosen);
  } 
  }
   
  
  
  





