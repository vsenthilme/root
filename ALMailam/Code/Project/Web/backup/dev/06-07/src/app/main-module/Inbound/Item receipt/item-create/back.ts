import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatTableDataSource } from "@angular/material/table";
import { ActivatedRoute, Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CrossDockComponent } from "../../preinbound/goodreceipt-create/cross-dock/cross-dock.component";
import { ItemReceiptService } from "../item-receipt.service";
import { AssignHEComponent } from "./assign-he/assign-he.component";
import { PackDetailsComponent } from "./pack-details/pack-details.component";
import { Packdetails1Component } from "./packdetails1/packdetails1.component";

import { Location } from "@angular/common";
import { GoodsReceiptService } from "../../Goods-receipt/goods-receipt.service";
import { AssignPickerComponent } from "src/app/main-module/Outbound/assignment/assignment-main/assign-picker/assign-picker.component";
import { DeleteComponent } from "src/app/common-field/delete/delete.component";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
@Component({
  selector: 'app-item-create',
  templateUrl: './item-create.component.html',
  styleUrls: ['./item-create.component.scss']
})
export class ItemCreateComponent implements OnInit {
  screenid: 1050 | undefined;
  displayedColumns: string[] = ['select',  'itemCode','itemDescription', 'acceptedQty', 'damageQty', 'varianceQty','barCodeId', 'cases', 'accQty','damQty'];
  displayedColumns2: string[] = ['status','itemCode',  'manufacturerPartNo', 'lineNo', 'itemDescription', 'orderQty', 'acceptedQty', 'damageQty', 'cases',  'batchSerialNumber','expiryDate','manufacturerDate' ];

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


  isShown: boolean = false; // hidden by default
  toggleShow() { this.isShown = !this.isShown; }
  animal: string | undefined;
  name: string | undefined;
  constructor(private fb: FormBuilder,
    private auth: AuthService,
    private service: ItemReceiptService, private location: Location,
    public toastr: ToastrService, private dialog: MatDialog, private grservice: GoodsReceiptService,
    // private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private route: ActivatedRoute, private router: Router,
    public cs: CommonService,) { }
  sub = new Subscription();
  dataSource = new MatTableDataSource<any>([]);
  dataSource2 = new MatTableDataSource<any>([]);
  selection = new SelectionModel<any>(true, []);

  assignhe(): void {
    const dialogRef = this.dialog.open(AssignHEComponent, {
      disableClose: true,
      width: '55%',
      maxWidth: '60%',
      position: { top: '9%', },
    });

    dialogRef.afterClosed().subscribe(result2 => {
      if (result2) {
        this.selection.selected.forEach((x: any) => {
          x.putAwayHandlingEquipment = result2;
        });
        this.submit();
      }
    });
  }
  pallet(element: any): void {
    if(element.acceptedQty == null || element.acceptedQty == ''){
      element.acceptedQty = 0;
    }
    let data: any[] = [];
    if (element.packBarcodes.length == 0) {
      this.spin.show();
      this.sub.add(this.service.packBarcode(element.acceptedQty, element.damageQty, element.warehouseId).subscribe(res => {
        element.packBarcodes = res;
        res.forEach((x: any) => {
          data.push({
            packBarcodes: x.barcode, Qty: x.quantityType == 'A' ? element.acceptedQty : element.damageQty, Type: x.quantityType == 'A' ? '' : 'D', warehouseId: element.warehouseId,
            nooflable: 1, quantityType: x.quantityType, itemCode: element.itemCode, invoiceNo: element.invoiceNo, itemDescription: element.itemDescription,
            manufacturerPartNo: element.manufacturerPartNo
          });
        })
        this.spin.hide();
        const dialogRef = this.dialog.open(Packdetails1Component, {
          disableClose: true,
          width: '55%',
          maxWidth: '80%',
          position: { top: '9%', },
          data: data
        });
        
      dialogRef.afterClosed().subscribe(result => {
        element.barCodeId = element.itemCode
      });
      }, err => {
        this.cs.commonerrorNew(err);
        this.spin.hide();
      }));

    }
    else {

      element.packBarcodes.forEach((x: any) => {
        data.push({
          packBarcodes: x.barcode, Qty: x.quantityType == 'A' ? element.acceptedQty : element.damageQty, Type: x.quantityType == 'A' ? '' : 'D', warehouseId: element.warehouseId,
          nooflable: 1, quantityType: x.quantityType, itemCode: element.itemCode, invoiceNo: element.invoiceNo, itemDescription: element.itemDescription
        });
      })


      const dialogRef = this.dialog.open(Packdetails1Component, {
        disableClose: true,
        width: '80%',
        maxWidth: '50%',
        data: data
      });

      dialogRef.afterClosed().subscribe(result => {
        element.barCodeId = element.itemCode
      });
    }
  }

  barCodeChanged(element: any): void {
    console.log(element.barCodeId)
    if(!element.barCodeId){
     element.packBarcode = []
    }
    if(element.acceptedQty == null || element.acceptedQty == ''){
      element.acceptedQty = 0;
    }
    let data: any[] = [];
    if (element.packBarcodes.length == 0) {
      this.spin.show();
      this.sub.add(this.service.packBarcode(element.acceptedQty, element.damageQty, element.warehouseId).subscribe(res => {
        element.packBarcodes = res;
        res.forEach((x: any) => {
          data.push({
            packBarcodes: x.barcode, Qty: x.quantityType == 'A' ? element.acceptedQty : element.damageQty, Type: x.quantityType == 'A' ? '' : 'D', warehouseId: element.warehouseId,
            nooflable: 1, quantityType: x.quantityType, itemCode: element.itemCode, invoiceNo: element.invoiceNo, itemDescription: element.itemDescription,
            manufacturerPartNo: element.manufacturerPartNo
          });
        })
        this.spin.hide();
      }, err => {
        this.cs.commonerrorNew(err);
        this.spin.hide();
      }));

    }
    else {
      element.packBarcodes.forEach((x: any) => {
        data.push({
          packBarcodes: x.barcode, Qty: x.quantityType == 'A' ? element.acceptedQty : element.damageQty, Type: x.quantityType == 'A' ? '' : 'D', warehouseId: element.warehouseId,
          nooflable: 1, quantityType: x.quantityType, itemCode: element.itemCode, invoiceNo: element.invoiceNo, itemDescription: element.itemDescription
        });
      })
    }
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    const filterValue1 = (event.target as HTMLInputElement).value;
    this.dataSource2.filter = filterValue1.trim().toLowerCase();
  }
  email = new FormControl('', [Validators.required, Validators.email, Validators.pattern("[a-zA-Z0-9.-_]{1,}@[a-zA-Z.-]{2,}[.]{1}[a-zA-Z]{2,}")]);
  form = this.fb.group({
    companyCodeId: [],
    confirmedBy: [],
    confirmedOn: [],
    containerNo: [],
    containerReceiptNo: [],
    createdBy: [],
    createdOn: [],
    deletionIndicator: [],
    dockAllocationNo: [],
    inboundOrderTypeId: [],
    languageId: [],
    plantId: [],
    preInboundNo: [],
    refDocNumber: [],
    referenceField1: [],
    referenceField10: [],
    referenceField2: [],
    referenceField3: [],
    referenceField4: [],
    referenceField5: [],
    referenceField6: [],
    referenceField7: [],
    referenceField8: [],
    referenceField9: [],
    stagingNo: [],
    statusId: [],
    updatedBy: [],
    updatedOn: [],
    vechicleNo: [],
    warehouseId: [],
    goodsReceiptNo: [],
  });

  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  getErrorMessage() {
    if (this.email.hasError('required')) {
      return ' Field should not be blank';
    }
    return this.email.hasError('email') ? 'Not a valid email' : '';
  }



  isbtntext = true;

  code: any;
  ngOnInit(): void {
    this.form.disable();
    this.form.controls.containerNo.enable();
    let code = this.route.snapshot.params.code;
    if (code != 'new') {

      let js = this.cs.decrypt(code);
      this.fill(js);

      this.code = js.code;
    }
  }

  clearselection(row: any) {
    this.selection.clear();
    this.selection.toggle(row);
  }


  btntext = "Save";
  pageflow = "New";

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true })
  sort: MatSort;
  
  fill(data: any) {

    if (data.pageflow != 'New') {
      this.pageflow = "Edit";
      this.btntext = 'Update';
      this.form.controls.preInboundNo.disable();
      this.form.controls.warehouseId.disable();
      if (data.pageflow == 'Display') {

        this.isbtntext = false;
      }
      this.form.patchValue(data.code);
      this.form.disable();
      this.spin.show();
      this.sub.add(this.grservice.searchLine({ stagingNo: [data.code.stagingNo], caseCode: [data.code.caseCode], }).subscribe(res => {

        res.forEach((x: any) => {
          // x.damageQty = 0; 
          // x.acceptedQty = x.orderQty;
           x.varianceQty = 0;
          x.putAwayHandlingEquipment = "";
          x.packBarcodes = [];
          x.stagingNo = this.form.controls.stagingNo.value;
          x.goodsReceiptNo = this.form.controls.goodsReceiptNo.value;

          // this.sub.add(this.service.searchline({itemCode: [x.itemCode], refDocNumber: [x.refDocNumber]}).subscribe(grLineRes => {
          //   let accTotal = 0;
          //   let damTotal = 0;
          //   grLineRes.forEach(element => {
          //     accTotal = accTotal + (element.acceptedQty != null ? element.acceptedQty : 0);
          //     damTotal = damTotal + (element.damageQty != null ? element.damageQty : 0);
          //   });
          //   x.accQty = accTotal;
          //   x.damQty = damTotal;

          // x.damageQty =  0; 
          // x.acceptedQty = (x.orderQty - (x.accQty + x.damQty)) > 0 ?  (x.orderQty - (x.accQty + x.damQty)) : 0;

          // }))
   
        });
        this.spin.hide();

        this.dataSource = new MatTableDataSource<any>(res);
      //  this.dataSource.filterPredicate = this.customFilterPredicate();
        this.dataSource.sort = this.sort;
        this.selection = new SelectionModel<any>(true, []);
      }, err => {
        this.cs.commonerrorNew(err);
        this.spin.hide();
      }));

      // this.sub.add(this.service.searchline({ caseCode: [data.code.caseCode], refDocNumber: [data.code.refDocNumber] }).subscribe(res => {


      //   this.spin.hide();

      //   this.dataSource2 = new MatTableDataSource<any>(res);
      //   this.dataSource2.filterPredicate = this.customFilterPredicate();
      //   this.selection = new SelectionModel<any>(true, []);
      // }, err => {
      //   this.cs.commonerrorNew(err);
      //   this.spin.hide();
      // }));




      this.form.patchValue(data.code, { emitEvent: false });

    }
  }

  submit() {

let data:any =[];
    this.dataSource.data.forEach((element: any)=> {
      if(element.packBarcodes.length>0){
data.push(element)
      }
     })
    

    
    this.spin.show();


    this.sub.add(this.service.CreateLine(data).subscribe(res => {
      this.toastr.success("GR No Saved Successfully!");
      this.spin.hide();
      this.location.back();

    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();

    }));



  };
  back() {
    this.location.back();
  }
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.lineno + 1}`;
  }




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

  panelOpenState = false;
  getVariance(element: any) {

    let num: number = (Number(element.damageQty) + Number(element.acceptedQty));
    element.varianceQty = Number(element.orderQty) as number - num;
  }

  delete() {
    if (this.selection.selected.length === 0) {
      this.toastr.error("Kindly select one row", "", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    if (this.selection.selected.length > 1) {
      this.toastr.error("Kindly select one row", "", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    const dialogRef = this.dialog.open(DeleteComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.7%', },
      // data: this.selection.selected[0],
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.spin.show();
        debugger
        this.sub.add(this.service.DeleteLine(this.selection.selected[0]).subscribe(res => {
          this.spin.hide();
          this.ngOnInit();
        }, err => {

          this.cs.commonerrorNew(err);
          this.spin.hide();

        }));
      }
    });

  }




}



