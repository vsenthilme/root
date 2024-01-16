import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit } from "@angular/core";
import { DatePipe, Location } from '@angular/common';
import { FormControl, FormBuilder, Validators, } from "@angular/forms";
import { MatTableDataSource } from "@angular/material/table";
import { ActivatedRoute, Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { BillService } from "../bill.service";

@Component({
  selector: 'app-invoice-details',
  templateUrl: './invoice-details.component.html',
  styleUrls: ['./invoice-details.component.scss']
})
export class InvoiceDetailsComponent implements OnInit {
  //screenid: 1128 | undefined;
  displayedColumns: string[] = ['documenttype', 'approvaldate', 'expirationdate', 'remainder4','prebillamount',];
  input: any;
  isbtntext = true;
  public icon = 'expand_more';

  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);

  form = this.fb.group({
    languageId: [],
    classId: [],
    matterNumber: [],
    clientId: [],
    caseCategoryId: [],
    caseSubCategoryId: [],
    preBillBatchNumber: [],
    preBillNumber: [],
    preBillDate: [],
    invoiceNumber: [],
    invoiceFiscalYear: [],
    invoicePeriod: [],
    postingDate: [],
    referenceText: [],
    partnerAssigned: [],
    invoiceDate: [],
    totalBillableHours: [],
    invoiceAmount: [],
    currency: [],
    arAccountNumber: [],
    paymentPlanNumber: [],
    billStartDate: [],
    costCutDate: [],
    paymentCutDate: [],
    trustCutoffDate: [],
    totalPaidAmount: [],
    remainingBalance: [],
    statusId: [],
    deletionIndicator: [],
    referenceField1: [],
    referenceField2: [],
    referenceField3: [],
    referenceField4: [],
    referenceField5: [],
    referenceField6: [],
    referenceField7: [],
    referenceField8: [],
    referenceField9: [],
    referenceField10: [],
    addInvoiceLine: [],
    statusIddes: [],

  });
  todaydate = new Date();
  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  statusIdList: any[] = [];
  clientIdList: any[] = [];

  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  dataSource = new MatTableDataSource<any>([]);
  disabled = false;
  step = 0;
  btntext = "Save";
  pageflow = "New";
  panelOpenState = false;
  invoiceNumber: any;
  clientName: any;
  matterNumber: any;

  constructor(private fb: FormBuilder,
    private auth: AuthService,
    private service: BillService,
    public toastr: ToastrService,
    private location: Location,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private datePipe: DatePipe,
    private route: ActivatedRoute, private router: Router,
    private cs: CommonService,) { }

  ngOnInit(): void {

    this.auth.isuserdata();
    this.dropdownlist();
  }

  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.statusId.url,
      this.cas.dropdownlist.client.clientId.url,
    ]).subscribe((results) => {
      this.statusIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.statusId.key);
      this.clientIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.client.clientId.key);

      let code = this.route.snapshot.params.code;
      if (code != 'new') {
        let js = this.cs.decrypt(code);
        this.fill(js);
      }
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
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
  }

  fill(data: any) {
    this.pageflow = data.pageflow;

    if (data.pageflow != 'New') {
      this.btntext = "Update";
      if (data.pageflow == 'Display') {
        this.form.disable();
        this.isbtntext = false;
      }
      this.form.patchValue(data.code);
      this.form.controls.invoiceDate.patchValue(this.cs.day_callapiSearch(this.form.controls.invoiceDate.value));
      this.form.controls.postingDate.patchValue(this.cs.day_callapiSearch(this.form.controls.postingDate.value));
this.invoiceNumber = data.code.invoiceNumber;
      this.spin.show();
      this.sub.add(this.service.Get(data.code.invoiceNumber).subscribe(res => {
        let invoice = res.invoiceDate
        res.addInvoiceLine.forEach((x) => {
          x['clientname'] = this.clientIdList.find(y => y.key == x.clientId)?.value;
          x.statusId = this.statusIdList.find(y => y.key == x.statusId)?.value;
          this.clientName = x['clientname']
          this.matterNumber = x.matterNumber
          let splitApprovedDescription = x.statusId.split('-');
          x.statusIddes = splitApprovedDescription[1]
          x.invoiceDate = this.datePipe.transform(invoice, 'MM-dd-yyyy', "GMT00:00")
        })

        this.dataSource = new MatTableDataSource<any>(res.addInvoiceLine);
        console.log(this.dataSource.data)
        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }
  }

  // submit() {
  //   this.submitted = true; this.form.updateValueAndValidity;
  //   if (this.form.invalid) {
  //     this.toastr.error(
  //       "Please fill the required fields to continue",
  //       ""
  //     );
  //     this.cs.notifyOther(true);
  //     return;
  //   }
  //   if (this.form.controls.clientCategoryId.value == 3) {
  //     if (!this.form.controls.corporationClientId.value) {
  //       this.toastr.error(
  //         "Please fill corporation fields to continue",
  //         ""
  //       );
  //       return;
  //     }
  //   }

  //   if (this.form.controls.clientCategoryId.value == 4) {
  //     if (!this.form.controls.referenceField2.value) {
  //       this.toastr.error(
  //         "Please fill Petitioner fields to continue",
  //         ""
  //       );
  //       return;
  //     }
  //   }
  //   this.cs.notifyOther(false);
  //   this.spin.show();
  //   this.form.removeControl('updatedOn');
  //   this.form.removeControl('createdOn');
  //   this.form.patchValue({ updatedby: this.auth.username });
  //   if (this.form.controls.clientId.value) {
  //     this.sub.add(this.service.Update(this.form.getRawValue(), this.form.controls.clientId.value).subscribe(res => {
  //       this.toastr.success(this.form.controls.clientId.value + " updated successfully!", "Notification", {
  //         timeOut: 2000,
  //         progressBar: false,
  //       });
  //       this.spin.hide();
  //       this.router.navigate(['/main/client/clientlist']);
  //     }, err => {
  //       this.cs.commonerror(err);
  //       this.spin.hide();
  //     }));
  //   }
  //   else {
  //     this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
  //       this.toastr.success(res.clientId + " saved successfully!", "Notification", {
  //         timeOut: 2000,
  //         progressBar: false,
  //       });
  //       this.spin.hide();
  //       this.router.navigate(['/main/client/clientlist']);

  //     }, err => {
  //       this.cs.commonerror(err);
  //       this.spin.hide();

  //     }));
  //   }
  // };
  back() {
    this.location.back();
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

  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }
  }
}

