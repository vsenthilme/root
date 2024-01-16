import {
  Component,
  Inject,
  OnInit
} from '@angular/core';
import {
  FormBuilder,
  FormControl,
  Validators
} from '@angular/forms';
import {
  MatDialog,
  MatDialogRef,
  MAT_DIALOG_DATA
} from '@angular/material/dialog';
import {
  Router
} from '@angular/router';
import {
  NgxSpinnerService
} from 'ngx-spinner';
import {
  ToastrService
} from 'ngx-toastr';
import {
  Subscription
} from 'rxjs';
import {
  CommonApiService
} from 'src/app/common-service/common-api.service';
import {
  CommonService
} from 'src/app/common-service/common-service.service';
import {
  AuthService
} from 'src/app/core/core';
import {
  CustomerService
} from 'src/app/main-module/Masters -1/customer-master/customer.service';
import {
  AgreementService
} from '../../../operation/agreement/agreement.service';
import {
  InvoiceService
} from '../../../operation/invoice/invoice.service';
import {
  PaymentService
} from '../../../operation/payment/payment.service';
import {
  QuoteNewComponent
} from '../../quote/quote-new/quote-new.component';
import {
  InquiryService
} from '../inquiry.service';
import { CustomerDetailsComponent } from './customer-details/customer-details.component';
import {
  StorageavailablityComponent
} from './storageavailablity/storageavailablity.component';

@Component({
  selector: 'app-enquiry-new',
  templateUrl: './enquiry-new.component.html',
  styleUrls: ['./enquiry-new.component.scss']
})
export class EnquiryNewComponent implements OnInit {

  form = this.fb.group({
    createdBy: [],                                
    createdOn: [],
    copyTo: [],
    customerGroup: [],
    deletionIndicator: [],
    email: [, ],
    enquiryId: [],
    sbu: [],
    enquiryMobileNumber: [, Validators.required],
    enquiryName: [, Validators.required],
    enquiryRemarks: [],
    enquiryStatus: ['Open'],
    enquiryStoreSize: [, ],
    rentType:[],
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
    requirementDetail: [],
    requirementType: [],
    updatedBy: [],
    updatedOn: [],
    addressFrom: [],
    addressTo: [],
    numberOfTrips: [],
    packingCost: [],
    jobcardType: [],
  });


  constructor(
    public dialogRef: MatDialogRef < any > ,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private auth: AuthService,
    private fb: FormBuilder,
    public cs: CommonService,
    private router: Router,
    public service: InquiryService,
    private cas: CommonApiService,
    public dialog: MatDialog,
    private agreementService: AgreementService,
    private invoiceService: InvoiceService,
    private paymentService: PaymentService,

    private customerService: CustomerService,

  ) {}

  ngOnInit(): void {
    this.dropdownlist();
    this.form.controls.enquiryId.disable();
    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOn.disable();
    if (this.data.pageflow != 'New') {
      if (this.data.pageflow == 'Display')
        this.form.disable();
      this.fill();
    }
  }

  requirementTypeList: any[] = [];
  customerTypeList: any[] = [];
  statusList: any[] = [];
  storageSizeList: any[] = [];
  workOrderSbuList: any[] = [];
  enquirystatuslist:any[] = [];
//hello
  dropdownlist() {
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.requirementtype.url,
      this.cas.dropdownlist.setup.customertype.url,
      //    this.cas.dropdownlist.setup.status.url,
      this.cas.dropdownlist.setup.storenumbersize.url,
      this.cas.dropdownlist.setup.sbu.url,
      this.cas.dropdownlist.setup.inquiryStatus.url,

    ]).subscribe((results) => {
      this.requirementTypeList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.customertype.key);
      this.customerTypeList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.customertype.key);
      //   this.statusList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.status.key);
      this.workOrderSbuList = this.cas.foreachlist1(results[3], this.cas.dropdownlist.setup.sbu.key);
      this.enquirystatuslist = this.cas.foreachlist(results[4], this.cas.dropdownlist.setup.inquiryStatus.key);
      this.storageSizeList = this.cas.foreachlist1(results[2], this.cas.dropdownlist.setup.storenumbersize.key);
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }


  public mask = [/\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/]

  email = new FormControl('', [Validators.required, Validators.email]);
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  getErrorMessage() {
    if (this.email.hasError('required')) {
      return ' Field should not be blank';
    }
    return this.email.hasError('email') ? 'Not a valid email' : '';
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

  sub = new Subscription();
  submitted = false;
  panelOpenState = false;

  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code).subscribe(res => {
        this.form.patchValue(res, {
          emitEvent: false
        });
        this.form.controls.createdOn.patchValue(this.cs.dateapiutc0(this.form.controls.createdOn.value));
        this.form.controls.updatedOn.patchValue(this.cs.dateapiutc0(this.form.controls.updatedOn.value));
        this.spin.hide();
      },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }
    ));
  }

  submit() {
    this.submitted = true;
    if (this.form.invalid) {
      this.toastr.error(
        "Please fill required fields to continue",
        "Notification", {
          timeOut: 2000,
          progressBar: false,
        }
      );

      this.cs.notifyOther(true);
      return;
    }

    this.cs.notifyOther(false);
    this.spin.show();

    if (this.data.code) {
      this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code).subscribe(res => {
        this.toastr.success(this.data.code + " updated successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();
        this.dialogRef.close();

      }, err => {

        this.cs.commonerror(err);
        this.spin.hide();

      }));
    } else {
      this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
        this.toastr.success(res.enquiryId + " Saved Successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();
        this.dialogRef.close();

      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();

      }));
    }

  }

  copyTo() {
    if (this.form.controls.copyTo.value == "Customer") {
      let paramdata = "";
      paramdata = this.cs.encrypt({
        pageflow: 'Enquiry Customer',
        name: this.form.controls.enquiryName.value,
        phoneNumber: this.form.controls.enquiryMobileNumber.value,
        email: this.form.controls.email.value,
      });
      this.dialogRef.close();
      this.router.navigate(['/main/customermasters/customermaster-new/' + paramdata]);
    }
    if (this.form.controls.copyTo.value == "Quote") {
      this.sub.add(this.customerService.search({
        mobileNumber: [this.form.controls.enquiryMobileNumber.value]
      }).subscribe(res => {
        if (res.length >= 1) {
          if (res.length == 1) {
            this.dialogRef.close();
            const dialogRef = this.dialog.open(QuoteNewComponent, {
              disableClose: true,
             // width: '55%',
              maxWidth: '80%',
              data: {
                pageflow: 'Copy From Inquiry',
                enquiryReferenceNumber: this.form.controls.enquiryId.value,
                customerName: res[0].customerCode,
                mobileNumber: this.form.controls.enquiryMobileNumber.value,
                requirement: this.form.controls.requirementDetail.value,
                customerGroup: this.form.controls.customerGroup.value,
                storeSize: this.form.controls.enquiryStoreSize.value,
                email: this.form.controls.email.value,
                rentType: this.form.controls.referenceField1.value,

                
                addressFrom: this.form.controls.addressFrom.value,
                addressTo: this.form.controls.addressTo.value,
                numberOfTrips: this.form.controls.numberOfTrips.value,
                packingCost: this.form.controls.packingCost.value,
                jobcardType: this.form.controls.jobcardType.value,
              }
            });

            dialogRef.afterClosed().subscribe(result => {
              this.router.navigate(["/main/crm/quote"]);
            });
          }
          if(res.length > 1){
            this.dialogRef.close();
            const dialogRef = this.dialog.open(CustomerDetailsComponent, {
              disableClose: true,
              width: '55%',
              maxWidth: '80%',
              data: {
                mobileNumber: this.form.controls.enquiryMobileNumber.value,
              }
            });

            dialogRef.afterClosed().subscribe(customerResult => {
             this.dialogRef.close();
            const dialogRef = this.dialog.open(QuoteNewComponent, {
              disableClose: true,
              //width: '55%',
              maxWidth: '80%',
              data: {
                pageflow: 'Copy From Inquiry',
                enquiryReferenceNumber: this.form.controls.enquiryId.value,
                customerName: customerResult,
                mobileNumber: this.form.controls.enquiryMobileNumber.value,
                requirement: this.form.controls.requirementDetail.value,
                customerGroup: this.form.controls.customerGroup.value,
                storeSize: this.form.controls.enquiryStoreSize.value,
                email: this.form.controls.email.value,
                rentType: this.form.controls.referenceField1.value,

                addressFrom: this.form.controls.addressFrom.value,
                addressTo: this.form.controls.addressTo.value,
                numberOfTrips: this.form.controls.numberOfTrips.value,
                packingCost: this.form.controls.packingCost.value,
                jobcardType: this.form.controls.jobcardType.value,
              }
            });

            dialogRef.afterClosed().subscribe(result => {
              this.router.navigate(["/main/crm/quote"]);
            });
            });
          }
        } else {
          this.toastr.error(
            "Please create customer to create quote",
            "Notification", {
              timeOut: 2000,
              progressBar: false,
            }
          );

          this.cs.notifyOther(true);
          return;
        }
      }));


    }


  }


  availablityCheck() {
    const dialogRef = this.dialog.open(StorageavailablityComponent, {
      disableClose: true,
      width: '55%',
      maxWidth: '80%',
      data: {
       // storeSize: this.form.controls.enquiryStoreSize.value,
      }
    });

    dialogRef.afterClosed().subscribe(result => {});

  }
}
