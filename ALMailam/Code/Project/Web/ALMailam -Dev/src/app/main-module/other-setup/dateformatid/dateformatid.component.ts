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
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { DateformatidNewComponent } from './dateformatid-new/dateformatid-new.component';
import { DatefromatidService } from './datefromatid.service';

@Component({
  selector: 'app-dateformatid',
  templateUrl: './dateformatid.component.html',
  styleUrls: ['./dateformatid.component.scss']
})
export class DateformatidComponent implements OnInit {
   screenid=3111;
  advanceFilterShow: boolean;
  @ViewChild('Setupdate') Setupdate: Table | undefined;
  dates: any;
  selecteddates : any;

  displayedColumns: string[] = ['select','dateFormatId','dateFormat', 'createdBy','createdOn'];
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
    private auth: AuthService,
    private service: DatefromatidService) { }
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
      this.Setupdate!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
    }
    RA: any = {};
    ngOnInit(): void {
      this.RA = this.auth.getRoleAccess(this.screenid);
      this.getAll(

      );
    }

  
  
  
  
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
  console.log(this.selecteddates)
    if (data != 'New')
    if (this.selecteddates.length === 0) {
      this.toastr.warning("Kindly select any Row", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    const dialogRef = this.dialog.open(DateformatidNewComponent, {
      disableClose: true,
      width: '55%',
      maxWidth: '80%',
      data: { pageflow: data, code: data != 'New' ? this.selecteddates[0].dateFormatId : null,dateFormat: data != 'New' ? this.selecteddates[0].dateFormat : null,warehouseId: data != 'New' ? this.selecteddates[0].warehouseId : null,languageId: data != 'New' ? this.selecteddates[0].languageId : null,companyCodeId: data != 'New' ? this.selecteddates[0].companyCodeId : null,plantId: data != 'New' ? this.selecteddates[0].plantId : null}
    });
  
    dialogRef.afterClosed().subscribe(result => {
      this.getAll();
    });
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
    obj.languageId = [this.auth.languageId];
    obj.warehouseId = this.auth.warehouseId;
    this.spin.show();
    this.sub.add(this.service.search(obj).subscribe((res: any[]) => {
      console.log(res)
if(res){
   this.dates = res;

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
        this.dates=res;
       } this.spin.hide();
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
    }));
  }
  deleteDialog() {
    if (this.selecteddates.length === 0) {
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
      data: this.selecteddates[0].code,
    });
  
    dialogRef.afterClosed().subscribe(result => {
  
      if (result) {
        this.deleterecord(this.selecteddates[0].dateFormatId,this.selecteddates[0].warehouseId,this.selecteddates[0].languageId,this.selecteddates[0].companyCodeId,this.selecteddates[0].plantId);
  
      }
    });
  }
  
  
  deleterecord(id: any,warehouseId:any,languageId:any,companyCodeId:any,plantId:any) {
    this.spin.show();
    this.sub.add(this.service.Delete(id,warehouseId,languageId,companyCodeId,plantId).subscribe((res) => {
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
    this.dates.forEach(x => {
      res.push({
        "Language Id":x.languageId,
       "Company ":x.companyIdAndDescription,
       "Plant ":x.plantIdAndDescription,
       "Warehouse ":x.warehouseIdAndDescription,
        "DateFormat Id":x.dateFormatId,
       "Description":x.dateFormat,
       "Created By":x.createdBy,
       "Created On":this.cs.dateapi(x.createdOn),
      
      });
  
    })
    this.cs.exportAsExcel(res, "DateFormat ID");
  }
  onChange() {
    console.log(this.selecteddates.length)
    const choosen= this.selecteddates[this.selecteddates.length - 1];   
    this.selecteddates.length = 0;
    this.selecteddates.push(choosen);
  } 
  }
   
  
  
  




