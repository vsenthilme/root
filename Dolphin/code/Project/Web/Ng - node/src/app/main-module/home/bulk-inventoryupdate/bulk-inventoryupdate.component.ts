import { Component, OnInit, ViewChild } from '@angular/core';
import * as xlsx from 'xlsx'
import { NgxSpinnerComponent, NgxSpinnerModule, NgxSpinnerService } from 'ngx-spinner';
import { AuthService } from 'src/app/core/Auth/auth.service';
import { Subscription } from 'rxjs';
import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from "@angular/cdk/collections";
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
@Component({
  selector: 'app-bulk-inventoryupdate',
  templateUrl: './bulk-inventoryupdate.component.html',
  styleUrls: ['./bulk-inventoryupdate.component.scss']
})
export class BulkInventoryupdateComponent implements OnInit {

  constructor(private spin: NgxSpinnerService, public auth: AuthService) { }

  sub = new Subscription();
  ELEMENT_DATA: any[] = [];
  displayedColumns: string[] = ['select', 'itemCode', 'description',  'mfrPartNo', 'storageBin', 'storageSection','palletID','invQty','accocQty', 'total', 'stockType'];
  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
  selection = new SelectionModel<any>(true, []);


  isChecked = true;
  isShowDiv = false;
  table = false;
  userType = this.auth.userTypeId;
  div1Function() {
    this.table = !this.table;
  }
  showFloatingButtons: any;
  toggle = true;
  public icon = 'expand_more';
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


  disabled = false;
  step = 0;

  setStep(index: number) {
    this.step = index;
  }

  nextStep() {
    this.step++;
  }

  prevStep() {
    this.step--;
  }







  ngOnInit(): void {
  }

  arrayBuffer:any;
  file:File;

  data: any;
  incomingfile(event) 
    {
    this.file= event.target.files[0]; 
    }
    @ViewChild(MatSort, { static: true })
    sort!: MatSort;
    @ViewChild(MatPaginator, { static: true })
    paginator: MatPaginator; // Pagination
   Upload() {
    this.spin.show();
        let fileReader = new FileReader();
          fileReader.onload = (e) => {
              this.arrayBuffer = fileReader.result;
              var data = new Uint8Array(this.arrayBuffer);
              var arr = new Array();
              for(var i = 0; i != data.length; ++i) arr[i] = String.fromCharCode(data[i]);
              var bstr = arr.join("");
              var workbook = xlsx.read(bstr, {type:"binary"});
              var first_sheet_name = workbook.SheetNames[0];
              var worksheet = workbook.Sheets[first_sheet_name];
              this.data = (xlsx.utils.sheet_to_json(worksheet,{raw:true}));
              this.data.forEach(element => {
                element['companyCode'] =  this.auth.companyId;
                element['plantID'] =  this.auth.plantId;
                element['warehouseId'] =  this.auth.warehouseId;
                element['languageID'] =  this.auth.languageId;
              });
              console.log(this.data)
              this.dataSource = new MatTableDataSource<any>(this.data);
              this.selection = new SelectionModel<any>(true, []);
              this.dataSource.sort = this.sort;
              this.dataSource.paginator = this.paginator;
              this.spin.hide();

          }
          fileReader.readAsArrayBuffer(this.file);
  }

  onSelectAll(items: any) {
  }

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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.warehouseId + 1}`;
  }



  clearselection(row: any) {

    this.selection.clear();
    this.selection.toggle(row);
  }
}
