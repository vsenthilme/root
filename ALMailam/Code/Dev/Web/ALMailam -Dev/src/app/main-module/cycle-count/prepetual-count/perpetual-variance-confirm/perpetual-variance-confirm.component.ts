import { Component, OnInit, ViewChild } from '@angular/core';
import { Location } from "@angular/common";
import { SelectionModel } from '@angular/cdk/collections';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { PrepetualCountService } from '../prepetual-count.service';
import { Table } from 'primeng/table';
import { AssignPickerComponent } from 'src/app/main-module/Outbound/assignment/assignment-main/assign-picker/assign-picker.component';
@Component({
  selector: 'app-perpetual-variance-confirm',
  templateUrl: './perpetual-variance-confirm.component.html',
  styleUrls: ['./perpetual-variance-confirm.component.scss']
})
export class PerpetualVarianceConfirmComponent implements OnInit {



  periodicvarianceConfirm: any[] = [];
  @ViewChild('periodicvarianceConfirmTag') periodicvarianceConfirmTag: Table | any;

  screenid: 1075 | undefined;
  title1 = "Cycle count";
  title2 = "Prepetual Confirm";
  isShown: boolean = false; // hidden by default
  toggleShow() { this.isShown = !this.isShown; }
  animal: string | undefined;
  name: string | undefined;
  constructor(private auth: AuthService, public dialog: MatDialog, private prepetualCountService: PrepetualCountService, public toastr: ToastrService, private spin: NgxSpinnerService, private location: Location,
    private route: ActivatedRoute, private router: Router,
    public cs: CommonService,) { }


  showAMS = false;

  checkAssignUser() {
    let asshignUser: any[] = []
    this.periodicvarianceConfirm.forEach(x => {
      if (x.cycleCountAction == 'CONFIRM') {
        x.statusId = 78;
      }
      if (x.cycleCountAction == 'RECOUNT' && x.firstCountedQty == null) {
        this.AssignUser(x);
        asshignUser.push(x)
      }
    })
    if (asshignUser.length == 0) {
      this.periodicvarianceConfirm.filter(x => x.firstCountedQty != null ? x.secondCountedQty = x.countedQty : x.secondCountedQty = null);
      this.confirmNew();
    }
  }

  AssignUser(line) {
    const dialogRef = this.dialog.open(AssignPickerComponent, {
      disableClose: true,
      width: '80%',
      maxWidth: '50%',
      position: { top: '9%', },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.periodicvarianceConfirm.forEach(element => {
          element.firstCountedQty = element.countedQty;
          if (element.cycleCountAction == 'RECOUNT') {
            element.cycleCounterId = result;
            element.statusId = 75;
          }
        });
        this.confirmNew();
      }
    });
  }

  confirmNew() {
    let code = this.route.snapshot.params.code;
    let js = this.cs.decrypt(code);

    this.prepetualCountService.varienAnalysisConfirm(this.code.cycleCountNo, this.periodicvarianceConfirm)
      .subscribe(result => {
        this.toastr.success("Prepetual details updated successfully", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.router.navigate(['/main/cycle-count/varianceConfirm']);
      },
        error => {
          console.log(error);
          this.toastr.error(error.error, "Error", {
            timeOut: 2000,
            progressBar: false,
          });
          this.spin.hide();

        });
  }
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
  code: any;
  pageflow: any;
  pageTitle: any = 'Perpetual Count';


  filteredVariance: any[] = []
  filteredAssignUser: any[] = []
  filterCountLines: any[] = []
  ngOnInit(): void {

    let code = this.route.snapshot.params.code;

    let js = this.cs.decrypt(code);
    this.code = js.code;

    this.pageflow = js.pageflow;
    this.filteredVariance = [];
    this.periodicvarianceConfirm = [];
    this.spin.show();
    this.prepetualCountService.get(this.code).subscribe(result => {
      this.code = result;
      this.code.perpetualLine.forEach(element => {
        if (element.inventoryQuantity == null) {
          element.inventoryQuantity = 0
        }
        if (element.statusId == 72) {
          element.countedQty = element.inventoryQuantity;
        }
        if (element.firstCountedQty != null) {
          element.cycleCountAction = 'CONFIRM';
        }
      });

      this.code.perpetualLine.forEach(element => {
        // if ((element.countedQty - element.inventoryQuantity) != 0 && element.varianceQty != null) {
        if (element.statusId == 74 || element.statusId == 75 || element.statusId == 78) {
          if (element.cycleCountAction == null) {
            element.cycleCountAction = "RECOUNT";
          }
          if ((element.countedQty - element.inventoryQuantity) != 0 && element.varianceQty != null && element.statusId != 78) {
            element.cycleCountAction = "RECOUNT";
          } else {
            element.cycleCountAction = "CONFIRM"
          }
          if (element.firstCountedQty != null) {
            element.cycleCountAction = "CONFIRM"
          }
          this.filteredVariance.push(element)
        }
        //}
      });
      if (this.filteredVariance.length == 0) {
        this.toastr.error("No Lines found", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.router.navigate(['/main/cycle-count/varianceConfirm']);
      }
      this.periodicvarianceConfirm = this.filteredVariance;
      this.pageTitle = this.pageflow;

    });
    this.spin.hide();
  };

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
  back() {
    this.location.back();
  }




  pushToAms() {
    this.spin.show();

    this.prepetualCountService.pushToAMS(this.code.cycleCountNo, this.periodicvarianceConfirm).subscribe(
      result => {
        this.spin.hide();
        this.toastr.success(
          "Stock count sent to AMS Successfully",
          "Notification"
        );

        // if (result) {
        //   this.prepetualCountService.updatePerpetualHeader(this.code.cycleCountNo, this.code.cycleCountTypeId, this.auth.warehouseId, this.code.movementTypeId, this.code.subMovementTypeId, {
        //     plantId: this.auth.plantId, languageId: this.auth.languageId,
        //     warehouseId: this.auth.warehouseId, cycleCountTypeId: this.code.cycleCountTypeId, companyCodeId: this.auth.companyId, cycleCountNo: this.code.cycleCountNo, movementTypeId: this.code.movementTypeId,
        //     subMovementTypeId: this.code.subMovementTypeId, referenceField1: true, perpetualLine: this.code.perpetualLine
        //   }).subscribe(res => {
            
        //     this.router.navigate(['/main/cycle-count/varianceConfirm']);

        //   });
        // }
        this.router.navigate(['/main/cycle-count/varianceConfirm']);
      },
      error => {
        this.spin.hide();
        this.cs.commonerrorNew(error);
      }
    );
  }

  downloadexcel() {
    var res: any = [];
    this.periodicvarianceConfirm.forEach(x => {
      res.push({
        "Actions ": x.cycleCountAction,
        "Product code ": x.itemCode,
        'Description': x.itemDesc,
        'Mfr Sku': x.manufacturerPartNo,
        "Section": x.storageSectionId,
        "Bin Location": x.storageBin,
        "Barcode Id": x.barcodeId,
        "Stock Type": x.stockTypeId,
        "Spl Stock Type": x.specialStockIndicator,
        "Inventory Qty": x.inventoryQuantity,
        "Counted Qty": x.countedQty,
        "Variance": x.countedQty - x.inventoryQuantity,
        "User ID  ": x.cycleCounterId,
        "Status Id": x.statusDescription

        // 'Created By': x.createdBy,
        // 'Date': this.cs.dateapi(x.createdOn),
      });

    })
    this.cs.exportAsExcel(res, "Perpetual  Stock Analysis");
  }


}

