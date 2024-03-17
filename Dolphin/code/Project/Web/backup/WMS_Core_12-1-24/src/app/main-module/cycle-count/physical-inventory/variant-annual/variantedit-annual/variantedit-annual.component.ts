import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { ActivatedRoute, Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { AssignuserPopupComponent } from "../../../prepetual-count/prepetual-confirm/assignuser-popup/assignuser-popup.component";
import { PhysicalInventoryService } from "../../physical-inventory.service";
import { Table } from "primeng/table";
@Component({
  selector: 'app-variantedit-annual',
  templateUrl: './variantedit-annual.component.html',
  styleUrls: ['./variantedit-annual.component.scss']
})
export class VarianteditAnnualComponent implements OnInit {

  perpetualvarianceConfirm: any[] = [];
  @ViewChild('perpetualvarianceConfirmTag') perpetualvarianceConfirmTag: Table | any;
  
  title1 = "Cycle count";
  title2 = "Prepetual Confirm";
  isShown: boolean = false; // hidden by default
  toggleShow() { this.isShown = !this.isShown; }
  animal: string | undefined;
  name: string | undefined;

  data: any = {};

  isShowDiv = false;
  public icon = 'expand_more';
  showFloatingButtons: any;
  toggle = true;

  disabled = false;
  step = 0;
  panelOpenState = false;

  
  cycleCountActions: any[] = [{ view: 'Recount', value: 'RECOUNT' }, { view: 'Skip', value: 'SKIP' }, { view: 'Write Off', value: 'WRITEOFF' }]

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(
    public dialog: MatDialog,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    public cs: CommonService,
    public auth: AuthService,
    public router: Router,
    private route: ActivatedRoute,
    public periodicService: PhysicalInventoryService
  ) { }


  @ViewChild(MatSort) sort: MatSort;
  
  filteredVariance: any[] = [];
  ngOnInit(): void {
    this.perpetualvarianceConfirm = [];
    this.data = this.cs.decrypt(this.route.snapshot.params.data);
    let obj: any = {};
    obj.companyCode = [this.auth.companyId];
    obj.languageId = [this.auth.languageId];
    obj.plantId = this.auth.plantId;
    obj.warehouseId = this.auth.warehouseId;
    obj.cycleCountNo = this.data.periodicHeaderData.cycleCountNo;

    this.periodicService.findPeriodicDLine(obj).subscribe(res => {
    if (this.data.type == "Edit" && res) {
      res.forEach(element => {
        if((element.countedQty - element.inventoryQuantity) != 0  && element.varianceQty != null ){
        if(element.statusId == 74 || element.statusId == 75){
          console.log(element.varianceQty)
         if(element.cycleCountAction == null){
          element.cycleCountAction = "RECOUNT";
         }
          this.filteredVariance.push(element)
        }
      }
      });
      this.perpetualvarianceConfirm = this.filteredVariance;
    }
    if (this.data.type =='Display' && res) {
      console.log(res)
      res.forEach(element => {
        if((element.countedQty - element.inventoryQuantity) != 0  && element.varianceQty != null ){
          this.filteredVariance.push(element);
      }
      });
      this.perpetualvarianceConfirm = this.filteredVariance;
    }
  })

  }

  confirm() {
    this.spin.show();
    this.perpetualvarianceConfirm.filter(x => x.companyCodeId = x.companyCode);
    this.periodicService.updatePeriodicLine(this.perpetualvarianceConfirm).subscribe(
      result => {
        this.spin.hide();
        this.toastr.success(
          "Stock count confirmed Successfully",
          "Notification"
        );
        this.router.navigate(['/main/cycle-count/variant-analysis-annual'])
      },
      error => {
        this.spin.hide();
        this.toastr.error(
          "Error",
          "Notification"
        );
      }
    );
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
    console.log('show:' + this.showFloatingButtons);
  }



  setStep(index: number) {
    this.step = index;
  }
  nextStep() {
    this.step++;
  }

  prevStep() {
    this.step--;
  }

  

  calculateVariance(element){
    element.varianceQty = element.countedQty - element.inventoryQuantity;
  }


  downloadexcel() {
    var res: any = [];
    this.perpetualvarianceConfirm.forEach(x => {
      res.push({
        "Actions ": x.cycleCountAction,
        "Product code ": x.itemCode,
        'Description': x.referenceField8,
        'Mfr Sku': x.referenceField9,
        "Section": x.referenceField10,
        "Bin Location": x.storageBin,
        "Pallet ID": x.packBarcodes,
        "Stock Type": x.stockTypeId,
        "Inventory Qty": x.inventoryQuantity,
        "Counted Qty": x.countedQty,
        "Variance": x.countedQty - x.inventoryQuantity,
        "User ID  ": x.cycleCounterId,
        "Status Id": x.statusId != null ? this.cs.getstatus_text(x.statusId) : ''

        // 'Created By': x.createdBy,
        // 'Date': this.cs.dateapi(x.createdOn),
      });

    })
    this.cs.exportAsExcel(res, "Periodic Variance Analysis");
  }
}

