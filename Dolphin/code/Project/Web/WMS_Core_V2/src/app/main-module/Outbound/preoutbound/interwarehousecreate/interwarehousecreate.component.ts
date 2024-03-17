import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, Validators, FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource, MatTable } from '@angular/material/table';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription, forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { BusinessPartnerService } from 'src/app/main-module/Masters -1/other-masters/business-partner/business-partner.service';
import { MasterService } from 'src/app/shared/master.service';
import { PreoutboundService } from '../preoutbound.service';
import { InterwarehouselinesComponent } from './interwarehouselines/interwarehouselines.component';

@Component({
  selector: 'app-interwarehousecreate',
  templateUrl: './interwarehousecreate.component.html',
  styleUrls: ['./interwarehousecreate.component.scss']
})
export class InterwarehousecreateComponent implements OnInit {
  inventory: any[] = [];
  selectedbinner : any[] = [];
  email = new FormControl('', [Validators.required, Validators.email, Validators.pattern("[a-zA-Z0-9.-_]{1,}@[a-zA-Z.-]{2,}[.]{1}[a-zA-Z]{2,}")]);
  displayedColumns: string[] = ['no','lineReference', 'orderType','orderedQty','sku','skuDescription','uom', 'delete'];
  dataSource = new MatTableDataSource<any>([]);
  screenid: 1037 | undefined;

  sub = new Subscription();


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

 

  interWarehouseTransferOutHeader= this.fb.group({
    fromBranchCode: [this.auth.plantId],
  fromCompanyCode: [this.auth.companyId],
  requiredDeliveryDate: [],
  toBranchCode: [],
  toCompanyCode: [],
  transferOrderNumber: [],
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

    private service: PreoutboundService,
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
    this.interWarehouseTransferOutHeader.get('fromBranchCode')?.disable();
  this.interWarehouseTransferOutHeader.get('fromCompanyCode')?.disable();
  }

 multiWarehouseList: any[] = [];
  multiPartnerList: any[] = [];
  dropdownfill() {
    this.spin.show();
    forkJoin({
      warehouse: this.masterService.getWarehouseMasterDetails().pipe(catchError(err => of(err))),
      partner: this.bonService.search({warehouseId: [this.auth.warehouseId], businessPartnerType: [2]}).pipe(catchError(err => of(err))),
    })
    .subscribe(({ warehouse, partner }) => {
      warehouse.forEach(x => this.multiWarehouseList.push({ value: x.warehouseId, label: x.warehouseId }));
      partner.forEach(x => this.multiPartnerList.push({ value: x.partnerCode, label: x.partnerCode + ' - ' + x.partnerName }));
      this.interWarehouseTransferOutHeader.get('soHeader.wareHouseId')?.patchValue(this.auth.warehouseId)
    })
    this.spin.hide();
  }


  submit() {
    let obj: any = {};
    let interWarehouseTransferOutHeader: any = {};
    interWarehouseTransferOutHeader.fromBranchCode = this.auth.plantId;
    interWarehouseTransferOutHeader.fromCompanyCode = this.auth.companyId;
    interWarehouseTransferOutHeader.transferOrderNumber = this.interWarehouseTransferOutHeader.controls.transferOrderNumber.value;
    interWarehouseTransferOutHeader.toBranchCode = this.interWarehouseTransferOutHeader.controls.toBranchCode.value;
    interWarehouseTransferOutHeader.toCompanyCode = this.interWarehouseTransferOutHeader.controls.toCompanyCode.value;
  
    //soHeader.requiredDeliveryDate = this.soHeader.controls.requiredDeliveryDate.value;
    const originalDate = this.interWarehouseTransferOutHeader.controls.requiredDeliveryDate.value;
    interWarehouseTransferOutHeader.requiredDeliveryDate = this.cs.dateddMMYY(originalDate);
    
    obj.interWarehouseTransferOutHeader = interWarehouseTransferOutHeader;
   obj.interWarehouseTransferOutLine= this.inventory,

this.interWarehouseTransferOutHeader.controls.requiredDeliveryDate.patchValue(interWarehouseTransferOutHeader.requiredDeliveryDate);
    this.sub.add(this.service.createinterwarehouse(obj).subscribe(res => {
  if(res){
    this.toastr.success("Order created successfully!",   "Notification");
    this.router.navigate(['/main/outbound/preoutbound']);
    this.spin.hide();
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
  table!: MatTable<any>;
  add() {
    const dialogRef = this.dialog.open(InterwarehouselinesComponent, {
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
    const rowNo = this.dataSource.data.indexOf(index);
    if (rowNo == -1) {
      this.dataSource.data.splice(index, 1);
    }
    else {
      this.dataSource.data.splice(rowNo, 1);
    }
    this.dataSource._updateChangeSubscription();
  }  openDialog(data: any,rowIndex): void {
    const dialogRef = this.dialog.open(InterwarehouselinesComponent, {
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






