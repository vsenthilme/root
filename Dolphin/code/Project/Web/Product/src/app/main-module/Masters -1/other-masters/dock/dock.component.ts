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
import { DockService } from './dock.service';

@Component({
  selector: 'app-dock',
  templateUrl: './dock.component.html',
  styleUrls: ['./dock.component.scss']
})
export class DockComponent implements OnInit {

 
  advanceFilterShow: boolean;
  @ViewChild('setupitemtype') setupitemtype: Table | undefined;
  itemtype: any[] = [];
  selecteditemtype : any[] = [];
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
    private service: DockService ) { }
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
      this.setupitemtype!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
    }
    
    ngOnInit(): void {
      this.getAll();
    }

    warehouseId = this.auth.warehouseId;
    /** Whether the number of selected elements matches the total number of rows. */
   
  openDialog(data: any = 'New'): void {
    if (data != 'New'){
      if (this.selecteditemtype.length == 0) {
        this.toastr.warning("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
    }
    let paramdata = "";
    paramdata = this.cs.encrypt({ pageflow: data, code: data != 'New' ? this.selecteditemtype[0].dockId : null,warehouseId: data != 'New' ? this.selecteditemtype[0].warehouseId : null,languageId: data != 'New' ? this.selecteditemtype[0].languageId : null, plantId: data != 'New' ? this.selecteditemtype[0].plantId : null,companyCodeId: data != 'New' ? this.selecteditemtype[0].companyCodeId : null,dockType: data != 'New' ? this.selecteditemtype[0].dockType : null});
    this.router.navigate(['/main/other-masters/dockNew/' + paramdata]);
  }
  
 
  getAll() {
   
      this.adminUser()
    
  }
  
  adminUser(){
    let obj: any = {};
     obj.companyCodeId = [this.auth.companyId];
     obj.plantId = [this.auth.plantId];
    obj.languageId = [this.auth.languageId];
    obj.warehouseId = [this.auth.warehouseId];
    this.spin.show();
    this.sub.add(this.service.search(obj).subscribe((res: any[]) => {
      console.log(res)
if(res){
   this.itemtype = res;

}
      this.spin.hide();
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
    }));
  }
 


  deleteDialog() {
    if (this.selecteditemtype.length === 0) {
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
      data: this.selecteditemtype[0].code,
    });
  
    dialogRef.afterClosed().subscribe(result => {
  
      if (result) {
        this.deleterecord(this.selecteditemtype[0].dockId,this.selecteditemtype[0].warehouseId,this.selecteditemtype[0].languageId,this.selecteditemtype[0].plantId,this.selecteditemtype[0].companyCodeId,this.selecteditemtype[0].dockType);
  
      }
    });
  }
  
  
  deleterecord(id: any,warehouseId:any,languageId:any,plantId:any,companyCodeId:any,dockType:any) {
    this.spin.show();
    this.sub.add(this.service.Delete(id,warehouseId,languageId,plantId,companyCodeId,dockType).subscribe((res) => {
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
    this.itemtype.forEach(x => {
      res.push({
      "Language ":x.languageId,
        "Company ":x.companyCodeId,
        "Plant":x.plantId,
        "Warehouse":x.warehouseId,
        "Dock Type":x.dockType,
        "Dock":x.dockId,
        "Description":x.description,
        "Dock Status":x.dockStatus,
        "Created By":x.createdBy,
       "Created One":this.cs.dateapi(x.createdOn),
      });
  
    })
    this.cs.exportAsExcel(res, "Dock");
  }
  onChange() {
    const choosen= this.selecteditemtype[this.selecteditemtype.length - 1];   
    this.selecteditemtype.length = 0;
    this.selecteditemtype.push(choosen);
  } 
  }
   
  
  
  





