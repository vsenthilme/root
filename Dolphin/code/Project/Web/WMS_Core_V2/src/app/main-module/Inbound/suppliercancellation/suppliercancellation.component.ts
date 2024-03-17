import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Table } from 'primeng/table';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { PickingService } from '../../Outbound/picking/picking.service';
import { PreinboundService } from '../preinbound/preinbound.service';

@Component({
  selector: 'app-suppliercancellation',
  templateUrl: './suppliercancellation.component.html',
  styleUrls: ['./suppliercancellation.component.scss']
})
export class SuppliercancellationComponent implements OnInit {
  screenid=3224;
  RA: any = {};
  picking: any[] = [];
  selectedpicker : any[] = [];
  @ViewChild('pickingTag') pickingTag: Table | any;
 
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

  

  title1 = "Outbound";
  title2 = "Order Management";
  constructor(private service: PreinboundService,
    public toastr: ToastrService, public dialog: MatDialog,
    private spin: NgxSpinnerService, private router: Router,
    public auth: AuthService,
    private fb: FormBuilder,
    public cs: CommonService,) { }
  sub = new Subscription();

  ngOnInit(): void {
    this.RA = this.auth.getRoleAccess(this.screenid);
    console.log(this.RA);
    this.search(true);
    this.getDropdown();

  }

  warehouseId = this.auth.warehouseId
 
  searhform = this.fb.group({
    companyCodeId: [[this.auth.companyId]],
  endConfirmedOn: [],
  endCreatedOn: [],
  inboundOrderTypeId: [],
  languageId: [[this.auth.languageId]],
  newContainerNo: [],
  newPreInboundNo: [],
  newRefDocNumber: [],
  newStatusId: [],
  oldContainerNo: [],
  oldPreInboundNo: [],
  oldRefDocNumber: [],
  oldStatusId: [],
  plantId: [[this.auth.plantId]],
  purchaseOrderNumber: [],
  startConfirmedOn: [],
  startCreatedOn: [],
  supplierInvoiceCancelHeaderId: [],
  warehouseId: [[this.auth.warehouseId]],
  });





  dropdownSettings = {
    singleSelection: false, 
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2
  };
 oldpicklistno:any[]=[];
 newpicklistno:any[]=[];
 confirmedby:any[]=[];
  getDropdown() {
    this.sub.add(this.service.searchSupplierinvoice({ warehouseId: [this.auth.warehouseId],companyCodeId:[this.auth.companyId],plantId:[this.auth.plantId],languageId:[this.auth.languageId] }).subscribe(res => {
      res.forEach((x: any) => this.oldpicklistno.push({ value: x.oldRefDocNumber, label: x.oldRefDocNumber }));
      res.forEach((x: any) => this.newpicklistno.push({ value: x.newRefDocNumber, label: x.newRefDocNumber }));
      res.forEach((x: any) => this.confirmedby.push({ value: x.createdBy, label: x.createdBy }));
    }))
  }
 

  search(ispageload = false) {
    if (!ispageload) {
    }
   this.searhform.controls.startCreatedOn.patchValue(this.cs.dateNewFormat1(this.searhform.controls.startCreatedOn.value));
  this.searhform.controls.endConfirmedOn.patchValue(this.cs.dateNewFormat1(this.searhform.controls.endCreatedOn.value));
    //this.searhform.controls.statusId.value.push(48);
    this.service.searchSupplierinvoice(this.searhform.value).subscribe(res => {
      console.log(res);
      this.picking = res;
      this.spin.hide();
    }, err => {

      this.cs.commonerrorNew(err);;
      this.spin.hide();

    });


  }
  onChange() {
    const choosen= this.selectedpicker[this.selectedpicker.length - 1];   
    this.selectedpicker.length = 0;
    this.selectedpicker.push(choosen);
  }
 
  
  reload() {
    window.location.reload();
  }
 

  openConfirm(data: any) {
    console.log(data);
    console.log(data.line);
    if(data.line.length == 0){
      this.toastr.error("No Lines Present", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
   
    let paramdata = this.cs.encrypt({ code: data, pageflow: 'Edit' });
    this.router.navigate(['/main/inbound/cancellation-lines/' + paramdata]);

  }


 

  
 


  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.picking.forEach(x => {
      res.push({
        "Branch":x.plantDescription,
        "Warehouse":x.warehouseDescription,
        "Old Order No":x.oldRefDocNumber,
        "New Order No":x.newRefDocNumber,
        "No of Old Order Lines":x.oldCountOfOrderLines,
        "No of New Order Lines":x.newCountOfOrderLines,
        "Order Type":x.referenceDocumentType,
        "Created By":x.createdBy,
        "Created On":this.cs.dateapiwithTime(x.createdOn),
        
        // 'Created By': x.createdBy,
        // 'Date': this.cs.dateapi(x.createdOn),
      });

    })
    this.cs.exportAsExcel(res, "Supplier Invoice Cancellation");
  }
  onItemSelect(item: any) {
    console.log(item);
  }
  
  OnItemDeSelect(item:any){
    console.log(item);
}
onSelectAll(items: any){
    console.log(items);
}
onDeSelectAll(items: any){
    console.log(items);
}

form = this.fb.group({
  assignedPickerId: [,],
});





}

