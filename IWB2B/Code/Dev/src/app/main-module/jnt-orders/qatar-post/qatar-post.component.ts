import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DomSanitizer } from '@angular/platform-browser';
import { NgxSpinnerService } from 'ngx-spinner';
import { MessageService } from 'primeng/api';
import { Table } from 'primeng/table';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { JntOrdersService } from '../jnt-orders.service';
import { FormBuilder } from '@angular/forms';
import { Calendar } from 'primeng/calendar';

@Component({
  selector: 'app-qatar-post',
  templateUrl: './qatar-post.component.html',
  styleUrls: ['./qatar-post.component.scss']
})
export class QatarPostComponent implements OnInit {

  @ViewChild('userProfile') userProfile: Table | undefined;
  jntOrders: any[] = [];
  selectedjntOrders: any[] = [];
  advanceFilterShow: boolean;

  constructor(public dialog: MatDialog, private service: JntOrdersService, private spin: NgxSpinnerService, private cs: CommonService,
    private messageService: MessageService, private sanitizer: DomSanitizer, private fb: FormBuilder) {

  }
  sub = new Subscription();


  searhform = this.fb.group({
    reference_number: [],
    scanType: [[null],],
    // customer_code: [["BOQ"],],
    startDate: [],
    endDate: [],
    fullDate: [],
    orderType: [[1],],
    hubCode: [["QATARPOST"],],
  });

  ngOnInit(): void {
    let currentDate = new Date();
    let currentMonthStartDate = new Date();
    currentMonthStartDate.setDate(currentDate.getDate() - 31);

    this.searhform.controls.fullDate.patchValue([new Date(), new Date()]);
    this.searhform.controls.endDate.patchValue(new Date());
    this.searhform.controls.startDate.patchValue(new Date()); //currentMonthStartDate

    this.sparkCall();
  }

  filterResult: any[] = [];
  multicustomer_code: any[] = [];
  multiscanType: any[] = [];
  multireference_number: any[] = [];

  getAll() {
    this.spin.show();
    this.jntOrders = [];
    this.sub.add(this.service.getAllOrder('QATARPOST').subscribe((res: any[]) => {

      res.forEach(x => {
        x['printStatus'] = x.is_awb_printed == true && x.awb_3rd_Party ? 'Y' : x.is_awb_printed == null && x.awb_3rd_Party ? 'N' : '';


        if (x.orderType == "1") {
          this.filterResult.push(x);
        }

      })
      this.jntOrders = this.filterResult;
      this.selectedjntOrders = [];
      this.spin.hide();
    }, err => {
      this.spin.hide();
      this.cs.commonerror(err);
    }));

  }

  sparkCall() {
    console.log(this.searhform.getRawValue())
    this.spin.show();
    this.jntOrders = [];
    this.service.findQatarPost(this.searhform.getRawValue()).subscribe(res => {
      this.jntOrders = res;
      this.selectedjntOrders = [];
      this.spin.hide();
    }, err => {
      this.spin.hide();
      this.cs.commonerror(err);
    });

  }



  advanceFilter() {
    this.advanceFilterShow = !this.advanceFilterShow;
  }


  onChange() {
    const choosen = this.selectedjntOrders[this.selectedjntOrders.length - 1];
    this.selectedjntOrders.length = 0;
    this.selectedjntOrders.push(choosen);
  }


  applyFilterGlobal($event: any, stringVal: any) {
    this.userProfile!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  }

  genrateLabel(jntOrders) {
    // if (this.selectedjntOrders.length === 0) {
    //   this.messageService.add({key: 'br', severity:'info', summary:'Warning', detail:  "Kindly Select any row"});
    //   return;
    // }
    this.spin.show();
    this.service.getLabel(jntOrders.awb_3rd_Party).subscribe(res => {
      window.open(res.data, '_blank');
      this.spin.hide();
    }, err => {
      this.spin.hide();
      this.cs.commonerror(err);
    })
  }

  @ViewChild('myStartCalendar') startCalendar: Calendar;
  filter(type: any) {
    // if (this.searhform.controls.customer_code.value.length <= 0) {
    //   this.messageService.add({ key: 'br', severity: 'info', summary: 'Warning', detail: "Kindly Select any one Customer Code" });
    //   return;
    // }
    // if (type == 'customer' || type == 'reset') {
    //   this.multireference_number = [];
    //   this.searhform.controls.reference_number.reset();
    // }

    if (type == 'date') {
      if (this.searhform.controls.fullDate.value[1]) { // If second date is selected
        this.startCalendar.overlayVisible = false;
      }
      if (this.searhform.controls.fullDate.value.length <= 0) {
        this.cs.notifyOther(true);
        return;
      }
    }

    this.jntOrders = [];
    this.filterResult = [];
    this.spin.show();
    if (this.searhform.controls.fullDate.value != null) {
      this.searhform.controls.startDate.patchValue(this.cs.day_callapi_UTC(this.searhform.controls.fullDate.value[0]));
      this.searhform.controls.endDate.patchValue(this.cs.day_callapi_UTC(this.searhform.controls.fullDate.value[1]));
    }

    this.service.findConsignmentSpark(this.searhform.getRawValue()).subscribe(res => {

      res.forEach(x => {
        x['printStatus'] = x.is_awb_printed == true && x.awb_3rd_Party ? 'Y' : x.is_awb_printed == null && x.awb_3rd_Party ? 'N' : '';

        if (type == 'customer' || type == 'reset') {
          this.multireference_number.push({ value: x.reference_number, label: x.reference_number });
          this.multireference_number = this.cs.removeDuplicatesFromArrayNewstatus(this.multireference_number);
        }

        this.filterResult.push(x);
      })
      this.jntOrders = this.filterResult;
      this.selectedjntOrders = [];

      this.spin.hide();
    }, err => {
      this.spin.hide();
      this.cs.commonerror(err);
    })
  }

  reset() {
    this.searhform.reset();
    this.searhform.controls.orderType.patchValue([1]);
    this.searhform.controls.hubCode.patchValue(["QATARPOST"]);
   // this.searhform.controls.customer_code.patchValue(["BOQ"]);
    this.filter('reset');
  }

  downloadExcel() {
    var res: any = [];
    this.jntOrders.forEach(x => {
      res.push({
        "Shipsy AWB No": x.reference_number,
        "Customer Code ": x.customer_code,
        "Hub Code ": 'Qatar Post',
        "Integration Status ": x.qp_wh_status,
        "JNT Bill Code ": x.awb_3rd_Party,
        "Created On": this.cs.dateapi(x.created_at),
        "Event": x.item_action_name,
        "Event Time": this.cs.dateapi(x.action_time),
      });

    })
    this.cs.exportAsExcel(res, "JNT Orders");
  }


  docurl: any;
  fileUrldownload: any;


  async bulkLabel() {
    let selectedLabel: any[] = [];
    this.selectedjntOrders.forEach(x => {
      if (x.awb_3rd_Party != null) {
        selectedLabel.push(x.awb_3rd_Party);
      }
    })
    let obj: any = {};
    obj.billCodes = selectedLabel;
    this.spin.show()
    const blob = await this.service.getBulkLabel(obj)
      .catch((err: HttpErrorResponse) => {
        this.cs.commonerror(err);
      });
    this.spin.hide();
    if (blob) {
      const blobOb = new Blob([blob], {
        type: "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
      });
      this.fileUrldownload = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blobOb));
      // http://remote.url.tld/path/to/document.doc&embedded=true
      this.docurl = window.URL.createObjectURL(blob);
      const a = document.createElement('a')
      a.href = this.docurl
      a.download = 'JNTBillCode' + `_${new Date().getDate() + '-' + (new Date().getMonth() + 1) + '-' + new Date().getFullYear() + '_' + this.cs.timeFormat(new Date())}`;
      a.click();
      URL.revokeObjectURL(this.docurl);
    }
    // window.location.reload();
    this.spin.hide();
  }

  onTableHeaderCheckboxToggle(E) {
    console.log(E);
    //console.log(Paginator)
  }
}
