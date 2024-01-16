import { SelectionModel } from '@angular/cdk/collections';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
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

import { AltpartNewComponent } from './altpart-new/altpart-new.component';
import { AltpartService } from './altpart.service';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
import { AnyARecord } from 'dns';

@Component({
  selector: 'app-altpart',
  templateUrl: './altpart.component.html',
  styleUrls: ['./altpart.component.scss']
})
export class AltpartComponent implements OnInit {
screenid=3147;
  advanceFilterShow: boolean;
  @ViewChild('Setupinvoice') Setupinvoice: Table | undefined;
  invoices: any;
  selectedinvoice : any;
 
  displayedColumns: string[] = ['select','doorId','doorDescription', 'createdBy','createdOn'];
  sub = new Subscription();
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  public icon = 'expand_more';

  ELEMENT_DATA: any[] = [];
  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
  selection = new SelectionModel<any>(true, []);
  
  constructor(public dialog: MatDialog,
   // private cas: CommonApiService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    public cs: CommonService,
   // private excel: ExcelService,
    private fb: FormBuilder,
    private router: Router,
    private auth: AuthService,
    private service: AltpartService) { }
    RA: any = {};
  ngOnInit(): void {
    this.RA = this.auth.getRoleAccess(this.screenid);
    this.getAll();
  }
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
    applyFilter(event: Event) {
      const filterValue = (event.target as HTMLInputElement).value;

  
      this.dataSource.filter = filterValue.trim().toLowerCase();
  
      if (this.dataSource.paginator) {
        this.dataSource.paginator.firstPage();
      }
    }
    applyFilterGlobal($event: any, stringVal: any) {
      this.Setupinvoice!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
    }
    // ngOnInit(): void {
    //   //this.getAll(

    //   );
    // }

  
  
  
  
    @ViewChild(MatSort, { static: true })
    sort!: MatSort;
    @ViewChild(MatPaginator, { static: true })
    paginator!: MatPaginator; // Pagination
   // Pagination
    warehouseId = this.auth.warehouseId;
  
  
  
  
  
  
    /** Whether the number of selected elements matches the total number of rows. */
    isAllSelected() {
      const numSelected = this.selection.selected.length;
      const numRows = this.dataSource.data.length;
      return numSelected === numRows;
    }
  
    /** Selects all rows if they are not all selected; otherwise clear selection. */
    masterToggle() {
      if (this.isAllSelected()) {
        this.selection.clear();
        return;
      }
  
      this.selection.select(...this.dataSource.data);
    }
  
    /** The label for the checkbox on the passed row */
    checkboxLabel(row?: any): string {
      if (!row) {
        return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
      }
      return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.handlingUnit + 1}`;
    }
  
  
  
  
  
  
    clearselection(row: any) {
  
      this.selection.clear();
      this.selection.toggle(row);
    }
  
    openDialog(data: any = 'New'): void {
      if (data != 'New'){
        if (this.selectedinvoice.length == 0) {
          this.toastr.warning("Kindly select any Row", "Notification", {
            timeOut: 2000,
            progressBar: false,
          });
          return;
        }
      }
      let paramdata = "";
      paramdata = this.cs.encrypt({ pageflow: data, code: data != 'New' ? this.selectedinvoice[0].altItemCode : null,warehouseId: data != 'New' ? this.selectedinvoice[0].warehouseId : null,languageId: data != 'New' ? this.selectedinvoice[0].languageId : null, plantId: data != 'New' ? this.selectedinvoice[0].plantId : null,companyCodeId: data != 'New' ? this.selectedinvoice[0].companyCodeId : null,uomId: data != 'New' ? this.selectedinvoice[0].uomId : null,itemCode: data != 'New' ? this.selectedinvoice[0].itemCode : null});
      this.router.navigate(['/main/masternew/altpartNew/' + paramdata]);
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
     this.invoices = res;
  
  }
        this.spin.hide();
      }, err => {
        this.cs.commonerrorNew(err);
        this.spin.hide();
      }));
    }
  

  
 
   downloadexcel() {
    var res: any = [];
    this.invoices.forEach(x => {
       res.push({
        "Language":x.languageId,
       "Company":x.companyCodeId,
        "Plant":x.plantId,
        "Warehouse":x.warehouseId,
        "Part No":x.itemCode,
        "Alt Part No":x.altItemCode,
        "Manfacturer":x.manufacturer,
        "Brand":x.brand,
       "Created By":x.createdBy,
       "Created On":this.cs.dateapi(x.createdOn),
      
       });
  
     })
     this.cs.exportAsExcel(res, "Alternate Part");
   }
  deleteDialog() {
    if (this.selectedinvoice.length === 0) {
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
      data: this.selectedinvoice[0].code,
    });
  
    dialogRef.afterClosed().subscribe(result => {
  
      if (result) {
        this.deleterecord(this.selectedinvoice[0].itemCode,this.selectedinvoice[0].warehouseId,this.selectedinvoice[0].languageId,this.selectedinvoice[0].plantId,this.selectedinvoice[0].companyCodeId);
  
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
 
  onChange() {
    console.log(this.selectedinvoice.length)
    const choosen= this.selectedinvoice[this.selectedinvoice.length - 1];   
    this.selectedinvoice.length = 0;
    this.selectedinvoice.push(choosen);
  }  
  }
   
  
  
  








