
  import { SelectionModel } from '@angular/cdk/collections';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
//import { table } from 'console';
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { forkJoin, of, Subscription } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { DialogExampleComponent } from 'src/app/common-field/innerheader/dialog-example/dialog-example.component';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { PreinboundService } from 'src/app/main-module/Inbound/preinbound/preinbound.service';
import { BOMElement, BOMService } from 'src/app/main-module/Masters -1/other-masters/bom/bom.service';
import { BusinessPartnerService } from 'src/app/main-module/Masters -1/other-masters/business-partner/business-partner.service';
import { ReportsService } from 'src/app/main-module/reports/reports.service';
import { MasterService } from 'src/app/shared/master.service';

export interface PeriodicElement {

  position: number;
}


interface SelectItem {
  item_id: number;
  item_text: string;
}

// const ELEMENT_DATA: PeriodicElement[] = [
//   //{position: 1,},
// ];
@Component({
  selector: 'app-preinbound-upload',
  templateUrl: './preinbound-upload.component.html',
  styleUrls: ['./preinbound-upload.component.scss']
})
export class PreinboundUploadComponent implements OnInit {

  email = new FormControl('', [Validators.required, Validators.email, Validators.pattern("[a-zA-Z0-9.-_]{1,}@[a-zA-Z.-]{2,}[.]{1}[a-zA-Z]{2,}")]);
  displayedColumns: string[] = ['containerNumber', 'invoiceNumber','lineReference','sku', 'skuDescription',
  'manufacturerPartNo', 'expectedQty','uom','packQty',  'expectedDate','delete'];
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
  checkboxLabel(row?: any): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.no + 1}`;
  }
  clearselection(row: any) {
    this.selection.clear();
    this.selection.toggle(row);
  }

  screenid: 1037 | undefined;

  sub = new Subscription();

  //creation of Form
  AsnLine = this.fb.group({
    containerNumber: [],
    checkbox: [],
    expectedDate: [],
    expectedDateFE: [],
    expectedQty: [],
    invoiceNumber: [],
    lineReference: [],
    manufacturerName: [],
    manufacturerPartNo: [],
    packQty: [],
    sku: [],
    skuDescription: [],
    supplierCode: [],
    supplierPartNumber: [],
    uom: []
  });
  rows: FormArray = this.fb.array([this.AsnLine]);

  AsnHeader = this.fb.group({
    asnNumber: [],
    wareHouseId : [],
  });

  form = this.fb.group({
    asnHeader: this.AsnHeader,
    asnLine: this.rows,
  });
  submitted = false;
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

  isShowDiv = false;
  public icon = 'expand_more';
  showFloatingButtons: any;
  toggle = true;
  storecodeList: any;
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

  // statusList: any[] = [
  //   { key: "Active", value: 'Active' },
  //   { key: "InActive", value: 'InActive' }];



  panelOpenState = false;
  constructor(
    // public dialogRef: MatDialogRef<DialogExampleComponent>,
    // @Inject(MAT_DIALOG_DATA) public data: any, private dialog: MatDialog,
    private service: PreinboundService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private bonService: BusinessPartnerService,
    private masterService: MasterService,
    private auth: AuthService,
    private fb: FormBuilder,
    private cs: CommonService,
    private router: Router,
    private ReportsService: ReportsService
  ) { }
  ngOnInit(): void {
 this.dropdownfill();
    
  }
  multiWarehouseList: any[] = [];
  multiPartnerList: any[] = [];
  dropdownfill() {
    this.spin.show();
    forkJoin({
      warehouse: this.masterService.getWarehouseMasterDetails().pipe(catchError(err => of(err))),
    })
    .subscribe(({ warehouse}) => {
      warehouse.forEach(x => this.multiWarehouseList.push({ value: x.warehouseId, label: x.warehouseId }));
      this.form.get('asnHeader.wareHouseId')?.patchValue(this.auth.warehouseId)
    })
    this.spin.hide();
  }








  submit() {
    var arrayControl = this.form.get('asnLine') as FormArray
    arrayControl.controls.forEach((element, index) => {
      var arrayControl1 = element as FormGroup
      arrayControl1.controls.expectedDate.patchValue(this.cs.dateddMMYY(arrayControl1.controls.expectedDateFE.value))
    });
    this.sub.add(this.service.createAsnOrder(this.form.getRawValue()).subscribe(res => {
  if(res){
    this.toastr.success("Order created successfully!",   "Notification",{
      timeOut: 2000,
      progressBar: false,
    });
    this.spin.hide();
   // this.dialogRef.close();
    
    this.router.navigate(["/main/inbound/preinbound"]);
  }

    }, err => {

      this.cs.commonerrorNew(err);
      this.spin.hide();

    }));
  };

  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }

  elementdata: any;
  @ViewChild(MatTable)
  table!: MatTable<PeriodicElement>;
  add() {

    if (this.dataSource.data.length > 0) {
      this.addRow();
    }
    this.dataSource.data.push(this.dataSource.data.length);

    // this.elementdata.push(this.elementdata[0]);

    this.dataSource._updateChangeSubscription();


  }

  addRow() {
    const row = this.fb.group({
      containerNumber: [],
      checkbox: [],
      expectedDate: [],
      expectedDateFE: [],
      expectedQty: [],
      invoiceNumber: [],
      lineReference: [],
      manufacturerName: [],
      manufacturerPartNo: [],
      packQty: [],
      sku: [],
      skuDescription: [],
      supplierCode: [],
      supplierPartNumber: [],
      uom: []
    });
    this.rows.push(row);
    // if (!noUpdate) { this.updateView(); }
  }

  addRowOnEdit(bom: any) {
    const row = this.fb.group({
      containerNumber: [],
      checkbox: [],
      expectedDate: [],
      expectedDateFE: [],
      expectedQty: [],
      invoiceNumber: [],
      lineReference: [],
      manufacturerName: [],
      manufacturerPartNo: [],
      packQty: [],
      sku: [],
      skuDescription: [],
      supplierCode: [],
      supplierPartNumber: [],
      uom: []
    });
    this.rows.push(row);
    console.log(this.rows);
    // if (!noUpdate) { this.updateView(); }
  }

  removeRow(index: any) {
    this.rows.removeAt(index);
    const rowNo = this.dataSource.data.indexOf(index);
    console.log(rowNo);
    if(rowNo == -1)
    {
      this.dataSource.data.splice(index, 1);
    }
    else
    {
      this.dataSource.data.splice(rowNo, 1);
    }
    
    this.dataSource._updateChangeSubscription();
  }


  onItemSelect(item: any) {
    console.log(item);
  }

  onSelectAll(items: any) {
    console.log(items);
  }


  inputChange(item, index, row){

    //item.get('itemServiceTotal')?.setValue(total);
  }


  
  multiselectItemCodeList: any[] = [];
  itemCodeList: any[] = [];
  multiselectItemCodeList1: any[] = [];
itemCodeList1: any[] = [];
  onItemType(searchKey) {
    let searchVal = searchKey?.filter;
    if (searchVal !== '' && searchVal !== null) {
      forkJoin({
        itemList: this.ReportsService.getItemCodeDropDown(searchVal.trim()).pipe(catchError(err => of(err))),
      })
        .subscribe(({ itemList }) => {
          if (itemList != null && itemList.length > 0) {
            this.multiselectItemCodeList = [];
            this.itemCodeList = itemList;
            this.itemCodeList.forEach(x => this.multiselectItemCodeList.push({value: x.itemCode, label: x.itemCode, description: x.description}))
          }
        });
    }
  }

  selectedProduct(e, index){
    this.multiselectItemCodeList.forEach(x => {
      if(x.value == e.value){
        const skuDescription = this.rows.at(index) as FormGroup
        skuDescription.get('skuDescription')?.setValue(x.description); //handle according to your code
      }
    })
  }
  copy(){
    this.toastr.info("Select rows to paste the container no!",   "Notification",{
      timeOut: 2000,
      progressBar: false,
    });
  }
}




