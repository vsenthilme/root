import { SelectionModel } from "@angular/cdk/collections";
import { Component, Inject, OnInit } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { MatTableDataSource } from "@angular/material/table";
import { Router } from "@angular/router";
import { IDropdownSettings } from "ng-multiselect-dropdown";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { forkJoin, of, Subscription } from "rxjs";
import { catchError } from "rxjs/operators";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { ReportsService } from "src/app/main-module/reports/reports.service";
import { InhouseTransferService } from "../../inhouse-transfer.service";

interface SelectItem {
  id: number;
  itemName: string;
}


export interface sku {
  no: string;
  lineno: string;
  supcode: string;
  one: string;
  receipt: string;
}
const ELEMENT_DATA2: sku[] = [
  { no: "readonly", lineno: 'readonly', supcode: 'readonly', one: 'readonly', receipt: 'readonly', },
  { no: "readonly", lineno: 'readonly', supcode: 'readonly', one: 'readonly', receipt: 'readonly', },
  { no: "readonly", lineno: 'readonly', supcode: 'readonly', one: 'readonly', receipt: 'readonly', },
  { no: "readonly", lineno: 'readonly', supcode: 'readonly', one: 'readonly', receipt: 'readonly', },
  { no: "readonly", lineno: 'readonly', supcode: 'readonly', one: 'readonly', receipt: 'readonly', },
  { no: "readonly", lineno: 'readonly', supcode: 'readonly', one: 'readonly', receipt: 'readonly', },
  { no: "readonly", lineno: 'readonly', supcode: 'readonly', one: 'readonly', receipt: 'readonly', },
  { no: "readonly", lineno: 'readonly', supcode: 'readonly', one: 'readonly', receipt: 'readonly', },
  { no: "readonly", lineno: 'readonly', supcode: 'readonly', one: 'readonly', receipt: 'readonly', },
  { no: "readonly", lineno: 'readonly', supcode: 'readonly', one: 'readonly', receipt: 'readonly', },
];
@Component({
  selector: 'app-stock-popup',
  templateUrl: './stock-popup.component.html',
  styleUrls: ['./stock-popup.component.scss']
})
export class StockPopupComponent implements OnInit {

  isShowDiv = false;
  public icon = 'expand_more';
  showFloatingButtons: any;
  toggle = true;
  toggleFloat() {
    this.isShowDiv = !this.isShowDiv;
    this.toggle = !this.toggle;

    if (this.icon === 'expand_more') {
      this.icon = 'chevron_left';
    } else {
      this.icon = 'expand_more'
    }
    this.showFloatingButtons = !this.showFloatingButtons;
    console.log('show:' + this.showFloatingButtons);
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
  sourceItemCode: any;
  targetItemCode: any;
  sourceStockTypeId: any;
  targetStockTypeId: any;

  skulist: any[] = [];
  load(sourceItemCode: any) {

 

    
    this.targetItemCode= this.targetItemCode;
    console.log(this.targetItemCode)
    console.log(this.sourceItemCode)
    if (!this.sourceStockTypeId || !this.sourceItemCode) {
      this.toastr.error("Please fill the required fields", "Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    this.targetItemCode = this.sourceItemCode;
    this.targetStockTypeId = this.sourceStockTypeId as string == '1' ? '7' : '1';



    this.spin.show();
    this.sub.add(this.service.GetInventoryv2({
      warehouseId: [this.auth.warehouseId], itemCode: [this.sourceItemCode],
      stockTypeId: [this.sourceStockTypeId]
    }).subscribe(res => {
      console.log(res)
      this.spin.hide();

      this.dataSource = new MatTableDataSource<any>(res);

    }, err => {

      this.cs.commonerrorNew(err);
      this.spin.hide();

    }));
  }


      multiselectItemCodeList: any[] = [];
    itemCodeList: any[] = [];
    multiselectItemCodeList1: any[] = [];
  itemCodeList1: any[] = [];
    onItemType(searchKey) {
      let searchVal = searchKey?.filter;
      if (searchVal !== '' && searchVal !== null) {
        forkJoin({
          itemList: this.ReportsService.getItemCodeDropDown2(searchVal.trim(),this.auth.companyId,this.auth.plantId,this.auth.warehouseId,this.auth.languageId).pipe(catchError(err => of(err))),
        })
          .subscribe(({ itemList }) => {
            if (itemList != null && itemList.length > 0) {
              this.multiselectItemCodeList = [];
              this.itemCodeList = itemList;
              this.itemCodeList.forEach(x => this.multiselectItemCodeList.push({ value: x.itemCode, label: x.itemCode + ' - ' + x.description }))
            }
          });
      }
    }
    onItemType1(value) {
      let searchTarget = value?.filter;
      if (searchTarget !== '' && searchTarget !== null) {
        forkJoin({
          itemList1: this.ReportsService.getItemCodeDropDown2(searchTarget.trim(),this.auth.companyId,this.auth.plantId,this.auth.warehouseId,this.auth.languageId).pipe(catchError(err => of(err))),
        })
          .subscribe(({ itemList1 }) => {
            if (itemList1 != null && itemList1.length > 0) {
              this.multiselectItemCodeList1 = [];
              this.itemCodeList1 = itemList1;
              this.itemCodeList1.forEach(x => this.multiselectItemCodeList1.push({ value: x.itemCode, label: x.itemCode + ' - ' + x.description }))
            }
          });
      }
    }


  save() {
    this.spin.show();
    this.data.inhouseTransferLine = [];
    this.data.inhouseTransferLine.push({ sourceItemCode: this.sourceItemCode, targetItemCode: this.targetItemCode, targetStockTypeId: this.targetStockTypeId, sourceStockTypeId: this.sourceStockTypeId });
    this.sub.add(this.service.Create(this.data).subscribe(res => {
      this.spin.hide();
      this.toastr.success(res.transferNumber + ' Created successfully.',"",{
        timeOut: 2000,
        progressBar: false,
      })
   //   this.dialogRef.close(res);
   this.sourceStockTypeId = null;
   this.sourceItemCode = null;
   this.targetItemCode = null;
   this.targetStockTypeId = null;
   this.dataSource.data = [];
   this.data.inhouseTransferLine = [];

    }, err => {

      this.cs.commonerrorNew(err);
      this.spin.hide();

    }));
  }

  selectedItems: SelectItem[] = [];
  multiskulist :  SelectItem[] = [];
  multiselectskuList: SelectItem[] = [];

  dropdownSettings = {
    singleSelection: true, 
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2
  };

  ngOnInit(): void {
    // this.spin.show();
    // this.sub.add(this.service.GetAllSKU().subscribe(res => {
    //   this.spin.hide();
    //   this.skulist = res;
    //   this.skulist.forEach(x => this.multiskulist.push({id: x.itemCode, itemName: x.itemCode + (x.description == null ? '' : '- ' + x.description) }))
    //   this.multiselectskuList = this.multiskulist;
    // }, err => {
    //   this.cs.commonerrorNew(err);
    //   this.spin.hide();
    // }));
  }
  constructor(public dialogRef: MatDialogRef<StockPopupComponent>, private service: InhouseTransferService,
    public toastr: ToastrService, public dialog: MatDialog,
    private spin: NgxSpinnerService, private router: Router, @Inject(MAT_DIALOG_DATA) public data: any,
    private auth: AuthService,
    private fb: FormBuilder,
    private cs: CommonService,  private ReportsService: ReportsService) {}
  sub = new Subscription();



  displayedColumns: string[] = ['no', 'lineno', 'supcode',];
  dataSource = new MatTableDataSource<any>([]);
  selection = new SelectionModel<any>(true, []);

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
  checkboxLabel(row?: sku): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.no + 1}`;
  }
  clearselection(row: any) {
    this.selection.clear();
    this.selection.toggle(row);
  }

  onItemSelect(item: any) {
    this.sourceItemCode=this.selectedItems[0].id;
    this.load( this.sourceItemCode)
    console.log(item);

  }

OnItemDeSelect(item:any){
    console.log(item);
    console.log(this.selectedItems);
}
onSelectAll(items: any){
    console.log(items);
}
onDeSelectAll(items: any){
    console.log(items);
}
}
