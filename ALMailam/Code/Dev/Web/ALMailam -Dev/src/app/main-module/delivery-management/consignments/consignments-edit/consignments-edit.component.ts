import { SelectionModel } from '@angular/cdk/collections';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Table } from 'primeng/table';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';

import { variant } from 'src/app/main-module/cycle-count/variant-analysis/variant-analysis.component';
import { DeliveryService } from '../../delivery.service';

@Component({
  selector: 'app-consignments-edit',
  templateUrl: './consignments-edit.component.html',
  styleUrls: ['./consignments-edit.component.scss']
})
export class ConsignmentsEditComponent implements OnInit {


  advanceFilterShow: boolean;
  @ViewChild('Setupinvoice') Setupinvoice: Table | undefined;
  invoice: any;
  selectedinvoice: any;
  form = this.fb.group({

  });
  isShowDiv = false;
  table = true;
  fullscreen = false;
  search = true;
  step = 0;
  back = false;
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


  showFiller = false;
  sub = new Subscription();
  ELEMENT_DATA: variant[] = [];
  constructor(
    private router: Router, public toastr: ToastrService, private spin: NgxSpinnerService, private fb: FormBuilder,
    private cs: CommonService, private route: ActivatedRoute, private deliverService: DeliveryService, private auth: AuthService,
  ) { }
  data: any;
  ngOnInit(): void {
    this.data = this.cs.decrypt(this.route.snapshot.params.code);
    this.getOutboundLines()
  }

  getOutboundLines() {
    this.invoice = [];
    console.log(this.data)
    this.spin.show();
    this.deliverService.searchLinev2({refDocNumber: this.data.selectedRefDocNumber, warehouseId: [this.auth .warehouseId], companyCodeId: [this.auth.companyId], plantId: [this.auth.plantId], languageId: [this.auth.languageId] }).subscribe(res => {
      this.invoice = res;
      this.selectedinvoice = res;
      this.spin.hide();
    }, err => {
      this.spin.hide();
      this.cs.commonerrorNew(err);
    })
  }

  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }
  reset() {
    this.form.reset();
  }
  onChange() {
    const choosen = this.selectedinvoice[this.selectedinvoice.length - 1];
    this.selectedinvoice.length = 0;
    this.selectedinvoice.push(choosen);
  }
  invoiceNumber: any;
  manifeast() {
    this.spin.show();
    this.data.code.statusId = 89;
    this.sub.add(this.deliverService.Create(this.data.code[0]).subscribe(
      (res) => {
        this.selectedinvoice.forEach(x => {
          x.deliveryNo = res.deliveryNo
          x.invoiceNumber = this.data.code.salesInvoiceNumber
          x.statusId = 89;
        })

        this.sub.add(this.deliverService.CreateLine(this.selectedinvoice).subscribe(
          (result) => {
            if (result) {
              this.toastr.success(result.deliveryNo + "Manifest Saved Successfully!", "Notification", {
                timeOut: 2000,
                progressBar: false,
              });
            }
            this.spin.hide();
            this.router.navigate(['/main/delivery/consignment']);
          },
          (err) => {
            this.cs.commonerrorNew(err);
            this.spin.hide();
          }
        ));
      },
      (err) => {
        this.cs.commonerrorNew(err);
        this.spin.hide();
      }
    ));


  }


}



