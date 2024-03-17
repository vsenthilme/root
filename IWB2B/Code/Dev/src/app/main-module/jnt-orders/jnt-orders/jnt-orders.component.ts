import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { JntOrdersService } from '../jnt-orders.service';
import { Table } from 'primeng/table';
import { MessageService } from 'primeng/api';
import { HttpErrorResponse } from '@angular/common/http';
import { DomSanitizer } from '@angular/platform-browser';
import { Paginator } from 'primeng/paginator';
import { FormBuilder } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { Calendar } from 'primeng/calendar';



@Component({
  selector: 'app-jnt-orders',
  templateUrl: './jnt-orders.component.html',
  styleUrls: ['./jnt-orders.component.scss']
})
export class JntOrdersComponent implements OnInit {

  @ViewChild('userProfile') userProfile: Table | undefined;
  jntOrders: any[] = [];
  selectedjntOrders: any[] = [];
  advanceFilterShow: boolean;

  constructor(public dialog: MatDialog, private service: JntOrdersService, private spin: NgxSpinnerService, private cs: CommonService,
    private messageService: MessageService, private sanitizer: DomSanitizer, private fb: FormBuilder, private datepipe: DatePipe) {

  }
  sub = new Subscription();
  startDate: any;
  ngOnInit(): void {

    let currentDate = new Date();
    let currentMonthStartDate = new Date();
    currentMonthStartDate.setDate(currentDate.getDate() - 31);

    this.searhform.controls.fullDate.patchValue([new Date(), new Date()]);
    this.searhform.controls.endDate.patchValue(new Date());
    this.searhform.controls.startDate.patchValue(new Date()); //currentMonthStartDate


  //  this.dropdown();
    this.getAll();
  }

  filterResult: any[] = [];
  multicustomer_code: any[] = [];
  multiscanType: any[] = [];
  multireference_number: any[] = [];


  dropdown() {
    this.spin.show();
    this.service.findConsignmentSpark({orderType: [1]}).subscribe(res => {
      res.forEach(x => {
          this.multicustomer_code.push({ value: x.customer_code, label: x.customer_code });
          this.multicustomer_code = this.cs.removeDuplicatesFromArrayNewstatus(this.multicustomer_code);

          this.multiscanType.push({ value: x.scanType, label: x.scanType == null ? 'Ready to Print' : x.scanType }); // == null  ? 'readyToPrint' : x.scanType
          this.multiscanType = this.cs.removeDuplicatesFromArrayNewstatus(this.multiscanType);

      })
      this.spin.hide();
    }, err => {
      this.spin.hide();
      this.cs.commonerror(err);
    });
  }

  getAll() {
    console.log(this.searhform.getRawValue())
    this.spin.show();
    this.jntOrders = [];
    this.service.findConsignmentSpark(this.searhform.getRawValue()).subscribe(res => {      //scanType: null
      // res.forEach(x => {
      //   x['printStatus'] = x.is_awb_printed == true && x.awb_3rd_Party ? 'Y' : x.is_awb_printed == null && x.awb_3rd_Party ? 'N' : '';

      //   if (x.scanType == null) {
      //     this.filterResult.push(x);
          
      //     this.multireference_number.push({ value: x.reference_number, label: x.reference_number });
      //     this.multireference_number = this.cs.removeDuplicatesFromArrayNewstatus(this.multireference_number);
      //   }
      // })
      // res.forEach(x => {
      //     this.multireference_number.push({ value: x.reference_number, label: x.reference_number });
      //     this.multireference_number = this.cs.removeDuplicatesFromArrayNewstatus(this.multireference_number);
      // })
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


  downloadExcel() {
    var res: any = [];
    this.jntOrders.forEach(x => {
      res.push({
        "Shipsy AWB No": x.reference_number,
        "Customer Code ": x.customer_code,
        "Hub Code ": 'JNT',
        "JNT Bill Code ": x.awb_3rd_Party,
        "Created On": this.cs.dateapi(x.created_at),
        "Event": x.scanType == null ? 'Ready to Print' : x.scanType,
        "Print Status": x.printStatus,
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
    window.location.reload();
    this.spin.hide();
  }


  onTableHeaderCheckboxToggle(E) {
    console.log(E);
    console.log(Paginator)
  }


  searhform = this.fb.group({
    reference_number: [],
    scanType: [[null], ],
    customer_code: [["BOQ"],],
    startDate: [],
    endDate: [],
    fullDate: [],
    orderType: [[1],],
    hubCode: [["JT"],],
  });

  @ViewChild('myStartCalendar') startCalendar: Calendar;

  filter(type: any) {
    // if (this.searhform.controls.customer_code.value.length <= 0) {
    //   this.messageService.add({key: 'br', severity:'info', summary:'Warning', detail:  "Kindly Select any one Customer Code"});
    //   return;
    // }
    // if (type == 'customer' || type == 'reset') {
    //   this.multireference_number = [];
    //   this.searhform.controls.reference_number.reset();
    // }

    if(type == 'date'){
      if (this.searhform.controls.fullDate.value[1]) { // If second date is selected
        this.startCalendar.overlayVisible=false;
    }
    if(this.searhform.controls.fullDate.value.length <= 0){
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
    this.searhform.controls.hubCode.patchValue(["JT"]);
    this.searhform.controls.customer_code.patchValue(["BOQ"]);
    this.filter('reset');
  }
}

