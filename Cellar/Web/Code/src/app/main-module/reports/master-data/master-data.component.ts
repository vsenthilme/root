import { SelectionModel } from '@angular/cdk/collections';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { variant } from '../agreement-renew/agreement-renew.component';
import { ReportsService } from '../reports.service';

@Component({
  selector: 'app-master-data',
  templateUrl: './master-data.component.html',
  styleUrls: ['./master-data.component.scss']
})
export class MasterDataComponent implements OnInit {

 
  form = this.fb.group({
    codeId: [],
    inStock: [],
    itemCode: [,Validators.required],
    itemGroup: [,Validators.required],
    itemType: [],
    lastReceipt: [],
    status: [],
    warehouse: [],
});

  isShowDiv = false;
  table = true;
  fullscreen = false;
  search = true;
  back = false;
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
  displayedColumns: string[] = ['itemCode', 'description', 'itemGroup', 'saleUnitOfMeasure', 'unitPrice', 'inStock', 'openWo', 'lastReceipt'];
  sub = new Subscription();
  ELEMENT_DATA: variant[] = [];
  constructor(
    private router: Router,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cas: CommonApiService,
   private fb: FormBuilder,
   public cs: CommonService,
   private service: ReportsService,
    ) { }
  animal: string | undefined;
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
  ngOnInit(): void {
    this.dropdownlist();
   }

   itemGroupList : any[] = [];
   consumableList : any[] = [];

dropdownlist() {

  this.cas.getalldropdownlist([
    this.cas.dropdownlist.setup.itemgroup.url,
    this.cas.dropdownlist.trans.consumables.url,

  ]).subscribe((results) => {
    this.itemGroupList = this.cas.foreachlist1(results[0], this.cas.dropdownlist.setup.itemgroup.key);
    this.consumableList = this.cas.foreachlist2(results[1], this.cas.dropdownlist.trans.consumables.key);
  }, (err) => {
    this.toastr.error(err, "");
    this.spin.hide();
  });
}
  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
  selection = new SelectionModel<any>(true, []);


  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }
  @ViewChild(MatSort)
  sort!: MatSort;
  @ViewChild(MatPaginator)
  paginator!: MatPaginator; // Pagination
  // Pagination





  // filtersearch() {
  //   //  this.spin.show();
  //     this.table = true;
  //     this.search = true;
  //     this.fullscreen = true;


  // }
  togglesearch() {
    this.search = false;
    this.table = true;
    this.fullscreen = false;
    this.back = true;
  }
  backsearch() {
    this.table = true;
    this.search = true;
    this.fullscreen = true;
    this.back = false;
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
  checkboxLabel(row?: variant): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.warehouseno + 1}`;
  }



  clearselection(row: any) {

    this.selection.clear();
    this.selection.toggle(row);
  }

  onItemSelect(item: any) {
    console.log(item);
  }

  onSelectAll(items: any) {
    console.log(items);
  }

  submitted = false;
  filtersearch() {
    this.submitted = true;
    if (this.form.invalid) {
      this.toastr.error(
        "Please fill the required fields to continue",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );

      this.cs.notifyOther(true);
      return;
    }
    
    this.spin.show();
    this.sub.add(this.service.getMasterdataTime(this.form.getRawValue()).subscribe(res => {
      this.dataSource.data = res;
      this.spin.hide()
      this.dataSource.paginator = this.paginator;
       this.dataSource.sort = this.sort;
      this.spin.hide();

      this.spin.hide();
      this.table = true;
      this.search = false;
      this.back = true;
      this.fullscreen = false;
    },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
  }







  reset(){
    this.form.reset();
  }
  downloadexcel() {
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Item Code": x.itemCode,
        "Description": x.description,
        "Item Group": x.itemGroup,
        'UoM': x.saleUnitOfMeasure,
        'Price/Unit': x.unitPrice,
        'Instock': x.inStock,
        "Open WO": x.openWo,
        "Last Receipt":this.cs.dateapiutc0(x.lastReceipt),
        
       
       
      });

    })
    this.cs.exportAsExcel(res,"Master-Data");
  }
}

