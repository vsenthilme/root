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
import { Router } from '@angular/router';
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
  StorageunitService
} from 'src/app/main-module/Masters -1/material-master/storage-unit/storageunit.service';
import {
  AgreementNewComponent
} from '../../../operation/agreement/agreement-new/agreement-new.component';
import {
  AgreementService
} from '../../../operation/agreement/agreement.service';
import {
  InvoiceService
} from '../../../operation/invoice/invoice.service';
import {
  PaymentService
} from '../../../operation/payment/payment.service';
import { WorkorderNewComponent } from '../../../operation/workorder/workorder-new/workorder-new.component';
import { WorkorderService } from '../../../operation/workorder/workorder.service';
import {
  QuoteService
} from '../quote.service';


@Component({
  selector: 'app-quote-new',
  templateUrl: './quote-new.component.html',
  styleUrls: ['./quote-new.component.scss']
})
export class QuoteNewComponent implements OnInit {


  form = this.fb.group({
    quoteId: [],
    codeId: [],
    copyTo: [],
    enquiryReferenceNumber: [, Validators.required],
    customerName: [, Validators.required],
    mobileNumber: [, Validators.required],
    requirement: [],
    sbu: [],
    email: [],
    customerGroup: [],
    storeSize: [],
    rent: [],
    notes: [],
    documentStatus: [],
    requirementType: [],
    status: [],
    customerCode: [],
    deletionIndicator: [],
    referenceField1: [, ],
    referenceField2: [],
    referenceField3: [],
    referenceField4: [],
    referenceField5: [],
    referenceField6: [],
    referenceField7: [],
    referenceField8: [],
    referenceField9: [],
    referenceField10: [],
    createdBy: [],
    createdOn: [],
    updatedBy: [],
    updatedOn: [],
    addressFrom: [],
    addressTo: [],
    numberOfTrips: [],
    packingCost: [],
    jobcardType: [],
  });

  constructor(
    public dialogRef: MatDialogRef < QuoteNewComponent > ,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private auth: AuthService,
    private fb: FormBuilder,
    public cs: CommonService,
    public service: QuoteService,
    public dialog: MatDialog,
    private cas: CommonApiService,
    private agreementService: AgreementService,
    private invoiceService: InvoiceService,
    private paymentService: PaymentService,
    private router: Router,
    private customerService: CustomerService,
    private storageService: StorageunitService,
    private workOrderSerivce: WorkorderService ) {
    
    }


  sub = new Subscription();
  submitted = false;

  public myModel = ''
  //public mask = [/\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/]
  public mask = [/\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/]
  ngOnInit(): void {
    this.dropdownlist();
    this.form.controls.quoteId.disable();
    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOn.disable();
    if (this.data.pageflow != 'New' && this.data.pageflow != "Copy From Inquiry") {
      if (this.data.pageflow == 'Display')
        this.form.disable();
      this.fill();
    }

    if (this.data.pageflow == "Copy From Inquiry") {

      console.log(this.data)
      this.form.controls.enquiryReferenceNumber.patchValue(this.data.enquiryReferenceNumber);
      this.form.controls.customerName.patchValue(this.data.customerName);
      this.form.controls.mobileNumber.patchValue(this.data.mobileNumber);
      this.form.controls.requirement.patchValue(this.data.requirement);
      this.form.controls.customerGroup.patchValue(this.data.customerGroup);
      this.form.controls.storeSize.patchValue(this.data.storeSize);
      this.form.controls.email.patchValue(this.data.email);
      this.form.controls.referenceField1.patchValue(this.data.rentType);

      this.form.controls.addressFrom.patchValue(this.data.addressFrom);
      this.form.controls.addressTo.patchValue(this.data.addressTo);
      this.form.controls.numberOfTrips.patchValue(this.data.numberOfTrips);
      this.form.controls.packingCost.patchValue(this.data.packingCost);
      this.form.controls.jobcardType.patchValue(this.data.jobcardType);


      if (this.form.controls.storeSize.value && this.form.controls.referenceField1.value) {
        this.sub.add(this.storageService.search({
          priceMeterSquare: [this.data.storeSize]
        }).subscribe(res => {
          if (this.form.controls.referenceField1.value == "WEEKLY") {
            this.form.controls.rent.patchValue(res[0].weekly);
          }
          if (this.form.controls.referenceField1.value == "MONTHLY") {
            this.form.controls.rent.patchValue(res[0].monthly);
          }
          if (this.form.controls.referenceField1.value == "QUARTERLY") {
            this.form.controls.rent.patchValue(res[0].quarterly);
          }
          if (this.form.controls.referenceField1.value == "HALFYEARLY") {
            this.form.controls.rent.patchValue(res[0].halfYearly);
          }
          if (this.form.controls.referenceField1.value == "YEARLY") {
            this.form.controls.rent.patchValue(res[0].yearly);
          }
        }));
      }
    }


    this.form.controls.customerName.valueChanges.subscribe(x => {
      this.sub.add(this.customerService.Get(this.form.controls.customerName.value).subscribe(res => {
        this.form.controls.customerGroup.patchValue(res.customerGroup);
        this.form.controls.mobileNumber.patchValue(res.mobileNumber);
        //    this.form.controls.secondaryNumber.patchValue(res.mobileNumber);
        this.form.controls.email.patchValue(res.email);
      }));
    });



    this.form.controls.storeSize.valueChanges.subscribe(x => {
      this.sub.add(this.storageService.search({
        priceMeterSquare: [x]
      }).subscribe(res => {
        console.log(res.length)
        if (res.length >= 1) {
          if (this.form.controls.referenceField1.value == "WEEKLY") {
            this.form.controls.rent.patchValue(res[0].weekly);
          }
          if (this.form.controls.referenceField1.value == "MONTHLY") {
            this.form.controls.rent.patchValue(res[0].monthly);
          }
          if (this.form.controls.referenceField1.value == "QUARTERLY") {
            this.form.controls.rent.patchValue(res[0].quarterly);
          }
          if (this.form.controls.referenceField1.value == "HALFYEARLY") {
            this.form.controls.rent.patchValue(res[0].halfYearly);
          }
          if (this.form.controls.referenceField1.value == "YEARLY") {
            this.form.controls.rent.patchValue(res[0].yearly);
          }
        } else {
          this.form.controls.rent.patchValue(0);
        }
      }));
    });

    this.form.controls.referenceField1.valueChanges.subscribe(x => {
      this.sub.add(this.storageService.search({
        priceMeterSquare: [this.form.controls.storeSize.value]
      }).subscribe(res => {
        console.log(res.length)
        if (res.length >= 1) {
          if (this.form.controls.referenceField1.value == "WEEKLY") {
            this.form.controls.rent.patchValue(res[0].weekly);
          }
          if (this.form.controls.referenceField1.value == "MONTHLY") {
            this.form.controls.rent.patchValue(res[0].monthly);
          }
          if (this.form.controls.referenceField1.value == "QUARTERLY") {
            this.form.controls.rent.patchValue(res[0].quarterly);
          }
          if (this.form.controls.referenceField1.value == "HALFYEARLY") {
            this.form.controls.rent.patchValue(res[0].halfYearly);
          }
          if (this.form.controls.referenceField1.value == "YEARLY") {
            this.form.controls.rent.patchValue(res[0].yearly);
          }
        } else {
          this.form.controls.rent.patchValue(0);
        }
      }));
    });
  }


  requirementTypeList: any[] = [];
  customerTypeList: any[] = [];
  storeSizeList: any[] = [];
  customerIDList: any[] = [];
  workOrderSbuList: any[] = [];

  dropdownlist() {
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.requirementtype.url,
      this.cas.dropdownlist.setup.customertype.url,
      this.cas.dropdownlist.setup.storenumbersize.url,
      this.cas.dropdownlist.trans.customerID.url,
      this.cas.dropdownlist.setup.sbu.url,
    ]).subscribe((results) => {
      this.requirementTypeList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.requirementtype.key);
      this.customerTypeList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.customertype.key);
      this.storeSizeList = this.cas.foreachlist1(results[2], this.cas.dropdownlist.setup.storenumbersize.key);
      this.workOrderSbuList = this.cas.foreachlist1(results[4], this.cas.dropdownlist.setup.sbu.key);
      this.customerIDList = this.cas.foreachlist2(results[3], this.cas.dropdownlist.trans.customerID.key);
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }

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

    if (this.form.controls.requirement.value != 'Packing & Moving' && this.form.controls.requirement.value && !this.form.controls.referenceField1.value) {
      this.toastr.error(
        "Please fill rent type fields to continue",
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
    this.form.controls.customerCode.patchValue(this.form.controls.customerName.value)
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
        this.toastr.success(res.quoteId + " Saved Successfully!", "Notification", {
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
    if (this.form.controls.copyTo.value == "Agreement") {
      this.sub.add(this.customerService.search({
        customerCode: [this.form.controls.customerName.value]
      }).subscribe(res => {
        if (res.length >= 1) {
          if (res[0].type != "LEAD") {
            this.dialogRef.close();
            const dialogRef = this.dialog.open(AgreementNewComponent, {
              disableClose: true,
              //     width: '55%',
              maxWidth: '80%',
              data: {
                pageflow: 'Copy From Quote',
                quoteNumber: this.form.controls.quoteId.value,
                customerName: this.form.controls.customerName.value,
                size: this.form.controls.storeSize.value,
                rent: this.form.controls.rent.value,
              }
            });

            dialogRef.afterClosed().subscribe(result => {
              
            this.router.navigate(["/main/operation/agreement"]);
            });
          } else {
            this.toastr.error(
              "Please change lead to customer to create agreement",
              "Notification", {
                timeOut: 2000,
                progressBar: false,
              }
            );

            this.cs.notifyOther(true);
            return;
          }
        }

      }))

    }
    if (this.form.controls.copyTo.value == "Work Order") {
      // this.sub.add(this.customerService.search({
      //   customerId: [this.form.controls.customerName.value]
      // }).subscribe(res => {
            this.dialogRef.close();
            const dialogRef1 = this.dialog.open(WorkorderNewComponent, {
              disableClose: true,
              //     width: '55%',
              maxWidth: '80%',
              data: {
                pageflow: 'Copy From Quote',
                customerName: this.form.controls.customerName.value,    
                addressFrom: this.form.controls.addressFrom.value,
                addressTo: this.form.controls.addressTo.value,
                numberOfTrips: this.form.controls.numberOfTrips.value,
                packingCost: this.form.controls.packingCost.value,
                jobcardType: this.form.controls.jobcardType.value,
              }
            });

            dialogRef1.afterClosed().subscribe(result => {
              this.router.navigate(["/main/operation/workorder"]);
            });
           
     
     

 //     }))

    }
    // if(this.form.controls.copyTo.value == "Invoice") {
    //   this.sub.add(this.invoiceService.Create({
    //     customerId: this.form.controls.customerName.value,

    //   }).subscribe(res => {
    //     this.toastr.success(res.invoiceNumber  + " Invoice created Successfully!","Notification",{
    //       timeOut: 2000,
    //       progressBar: false,
    //     });
    //     this.spin.hide();
    //  //   this.dialogRef.close();

    //   }, err => {
    //     this.cs.commonerror(err);
    //     this.spin.hide();

    //   }));
    // }
    // if(this.form.controls.copyTo.value == "Payment"){
    //   this.sub.add(this.paymentService.Create({
    //     customerName: this.form.controls.customerName.value,
    //     updatedBy: this.form.controls.updatedBy.value,

    //   }).subscribe(res => {
    //     this.toastr.success(res.voucherId  + " Payment created Successfully!","Notification",{
    //       timeOut: 2000,
    //       progressBar: false,
    //     });
    //     this.spin.hide();
    // //    this.dialogRef.close();

    //   }, err => {
    //     this.cs.commonerror(err);
    //     this.spin.hide();

    //   }));
    // }

  }

}
