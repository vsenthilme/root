import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Table } from 'primeng/table';
import { Location } from "@angular/common";
import { Subscription, forkJoin, of } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { ContainerReceiptService } from '../../Container-receipt/container-receipt.service';
import { AssignInvoiceComponent } from '../preinbound-new/assign-invoice/assign-invoice.component';
import { PreinboundeditpopupComponent } from '../preinbound-new/preinboundeditpopup/preinboundeditpopup.component';
import { PreinboundService } from '../preinbound.service';
import { MatTableDataSource, MatTable } from '@angular/material/table';
import { catchError } from 'rxjs/operators';
import { BusinessPartnerService } from 'src/app/main-module/Masters -1/other-masters/business-partner/business-partner.service';
import { MasterService } from 'src/app/shared/master.service';
import { PreinboundaddlinesComponent } from '../preinbound-upload/preinboundaddlines/preinboundaddlines.component';
import { B2borderlinesComponent } from './b2borderlines/b2borderlines.component';

@Component({
  selector: 'app-b2border',
  templateUrl: './b2border.component.html',
  styleUrls: ['./b2border.component.scss']
})
export class B2borderComponent implements OnInit {
  inventory: any[] = [];
  selectedbinner : any[] = [];
    email = new FormControl('', [Validators.required, Validators.email, Validators.pattern("[a-zA-Z0-9.-_]{1,}@[a-zA-Z.-]{2,}[.]{1}[a-zA-Z]{2,}")]);
    displayedColumns: string[] = ['containerNumber', 'expectedDate', 'expectedQty', 'invoiceNumber', 'lineReference', 'manufacturerName',
      'manufacturerCode', 'packQty', 'sku', 'skuDescription', 'supplierCode',
      'supplierPartNumber', 'uom', 'delete'];
    ELEMENT_DATA: any[] = [];
    dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
    screenid: 1037 | undefined;
  
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
    }
  
    sub = new Subscription();
  
    b2bTransferInHeader = this.fb.group({
      branchCode: [this.auth.plantId],
  companyCode: [this.auth.companyId],
  isCompleted: [],
  middlewareId: [],
  middlewareTable: [],
  sourceBranchCode: [],
  sourceCompanyCode: [],
  transferOrderDate: [],
  transferOrderNumber: [],
  updatedOn: [],
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
    panelOpenState = false;
    constructor(
      private service: PreinboundService,
      public toastr: ToastrService,
      private spin: NgxSpinnerService,
      private bonService: BusinessPartnerService,
      private masterService: MasterService,
      private auth: AuthService,
      private dialog: MatDialog,
      private fb: FormBuilder,
      private router: Router,
      private cs: CommonService,
    ) { }
  
    ngOnInit(): void {
    
      this.dropdownfill();
    this.b2bTransferInHeader.get('wareHouseId')?.disable();
    this.b2bTransferInHeader.get('branchCode')?.disable();
    this.b2bTransferInHeader.get('companyCode')?.disable();
    }
  
    multiWarehouseList: any[] = [];
    multiPartnerList: any[] = [];
  
    dropdownfill() {
      this.spin.show();
      forkJoin({
        warehouse: this.masterService.getWarehouseMasterDetails().pipe(catchError(err => of(err))),
      })
        .subscribe(({ warehouse }) => {
          warehouse.forEach(x => this.multiWarehouseList.push({ value: x.warehouseId, label: x.warehouseId }));
          //this.b2bTransferInHeader.controls.wareHouseId.patchValue(this.auth.warehouseId)
        })
      this.spin.hide();
    }
  
    submit() {
  
      let obj: any = {};
      let b2bTransferInHeader: any = {};
      b2bTransferInHeader.transferOrderNumber = this.b2bTransferInHeader.controls.transferOrderNumber.value;
      b2bTransferInHeader.companyCode=this.auth.companyId;
      b2bTransferInHeader.sourceBranchCode=this.b2bTransferInHeader.controls.sourceBranchCode.value;
      b2bTransferInHeader.sourceCompanyCode=this.b2bTransferInHeader.controls.sourceCompanyCode.value;
      b2bTransferInHeader.transferOrderDate=this.cs.uploadDate(this.b2bTransferInHeader.controls.transferOrderDate.value);
      b2bTransferInHeader.branchCode=this.auth.plantId,
      obj.b2bTransferInHeader = b2bTransferInHeader;
      obj.b2bTransferLine = this.inventory;
  
      this.sub.add(this.service.createB2BOrder(obj).subscribe(res => {
        if (res) {
          this.toastr.success("Order created successfully!", "Notification");
          this.router.navigate(['/main/inbound/preinbound']);
          this.spin.hide();
        }
      }, err => {
        this.cs.commonerror(err);
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
    table!: MatTable<any>;
  
    add() {
      const dialogRef = this.dialog.open(B2borderlinesComponent, {
        disableClose: true,
        width: '50%',
        maxWidth: '80%',
      });
  
      dialogRef.afterClosed().subscribe(result => {
       console.log(result.length);
       if(result){
        this.inventory
       
        if (result.length > 0) {
          this.inventory.push(result);
        }
  
        this.inventory.push(result);
      }
       this.inventory=[...this.inventory];
      });
    }
    
    removeRow(index: any) {
      const rowNo = this.inventory.indexOf(index);
      if (rowNo == -1) {
        this.inventory.splice(index, 1);
      }
      else {
        this.inventory.splice(rowNo, 1);
      }
      this.inventory=[...this.inventory];
    }
    openDialog(data: any,rowIndex): void {
      const dialogRef = this.dialog.open(PreinboundaddlinesComponent, {
        disableClose: true,
        width: '50%',
        maxWidth: '80%',
        data: this.inventory[rowIndex],
      });
    
      dialogRef.afterClosed().subscribe(result => {
        if(result){
          this.inventory.splice(rowIndex,0);
          this.inventory.splice(rowIndex, 1, result);
          console.log(result);
        //this.form.patchValue(result);
        this.inventory = [...this.inventory]
    
    }});
    }
  
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  