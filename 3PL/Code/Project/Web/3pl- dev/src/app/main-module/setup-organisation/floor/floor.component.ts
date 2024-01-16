
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
import { FloorService } from './floor.service';
  
@Component({
  selector: 'app-floor',
  templateUrl: './floor.component.html',
  styleUrls: ['./floor.component.scss']
})
export class FloorComponent implements OnInit {
screenid=3005;
    advanceFilterShow: boolean;
    @ViewChild('setupFloor') setupFloor: Table | undefined;
    company: any[] = [];
    selectedFloor : any[] = [];
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
      private service: FloorService) { }
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
        this.setupFloor!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
      }
      RA: any = {};
      ngOnInit(): void {
        this.RA = this.auth.getRoleAccess(this.screenid);
        this.getAll();
      }
  
      warehouseId = this.auth.warehouseId;
      /** Whether the number of selected elements matches the total number of rows. */
     
    openDialog(data: any = 'New'): void {
      if (data != 'New'){
        if (this.selectedFloor.length == 0) {
          this.toastr.warning("Kindly select any Row", "Notification", {
            timeOut: 2000,
            progressBar: false,
          });
          return;
        }
      }
      let paramdata = "";
      paramdata = this.cs.encrypt({ pageflow: data,code: data != 'New' ? this.selectedFloor[0].floorId : null ,warehouseId: data != 'New' ? this.selectedFloor[0].warehouseId : null,companyId: data != 'New' ? this.selectedFloor[0].companyId : null,plantId: data != 'New' ? this.selectedFloor[0].plantId : null,languageId: data != 'New' ? this.selectedFloor[0].languageId : null});
      this.router.navigate(['/main/organisationsetup/floorNew/' + paramdata]);
    }
    
    getAll() {
    
        this.adminUser()
      
    }
    
    adminUser(){
      let obj: any = {};
      obj.companyId = this.auth.companyId;
      obj.plantId = this.auth.plantId;
      obj.languageId = this.auth.languageId;
    obj.warehouseId = this.auth.warehouseId;
    
    this.spin.show();
    this.sub.add(this.service.search(obj).subscribe((res: any[]) => {
      console.log(res)
      if(res){
        this.company=res;
    
      }this.spin.hide();
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
    }));
    }
   
    deleteDialog() {
      if (this.selectedFloor.length === 0) {
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
        data: this.selectedFloor[0].code,
      });
    
      dialogRef.afterClosed().subscribe(result => {
    
        if (result) {
          this.deleterecord(this.selectedFloor[0].floorId,this.selectedFloor[0].warehouseId,this.selectedFloor[0].companyId,this.selectedFloor[0].plantId,this.selectedFloor[0].languageId);
    
        }
      });
    }
    
    
    deleterecord(id: any, warehouseId: any,companyId:any,plantId:any,languageId:any) {
      this.spin.show();
      this.sub.add(this.service.Delete(id, warehouseId,companyId,plantId,languageId).subscribe((res) => {
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
      this.company.forEach(x => {
        res.push({
        "Language Id":x.languageId,
          "Company":x.companyIdAndDescription,
          "Plant":x.plantIdAndDescription,
          "Warehouse":x.warehouseIdAndDescription,
          "Description":x.description,
          "Created By":x.createdBy,
         "Created On":this.cs.dateapi(x.createdOn),
        });
    
      })
      this.cs.exportAsExcel(res, "Company");
    }
    onChange() {
      const choosen= this.selectedFloor[this.selectedFloor.length - 1];   
      this.selectedFloor.length = 0;
      this.selectedFloor.push(choosen);
    } 
    }
     
    
    
    
  