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
  MatDialogRef,
  MAT_DIALOG_DATA
} from '@angular/material/dialog';
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
import { StorageunitService } from 'src/app/main-module/Masters -1/material-master/storage-unit/storageunit.service';
import { StorageTypeService } from 'src/app/main-module/setup/setup-masters/storageunit/storage-type/storage-type.service';
import {
  InquiryService
} from '../../../crm/enquiry/inquiry.service';
import {
  QuoteService
} from '../../../crm/quote/quote.service';
import {
  AgreementService
} from '../../agreement/agreement.service';
import { InvoiceService } from '../../invoice/invoice.service';
import {
  WorkorderService
} from '../../workorder/workorder.service';
import {
  PaymentService
} from '../payment.service';


@Component({
  selector: 'app-payment-new',
  templateUrl: './payment-new.component.html',
  styleUrls: ['./payment-new.component.scss']
})
export class PaymentNewComponent implements OnInit {

  role = this.auth.role;

  form = this.fb.group({
    contractNumber: [, Validators.required],
    createdBy: [],
    bank: [],
    createdOn: [],
    copyFrom: [],
    copyFromResult: [],
    customerName: [, Validators.required],
    deletionIndicator: [],
    documentType: [,Validators.required ],
    endDate: [],
    modeOfPayment: [, Validators.required],
    paidDate: [],
    paymentReference: [, ],
    phase: [, ],
    period: [, ],
    referenceField1: ['10001',],
    referenceField10: [],
    referenceField2: [],
    referenceField3: [],
    referenceField4: [],
    referenceField5: [],
    referenceField6: ['40001', Validators.required],
    referenceField7: [],
    referenceField8: [],
    referenceField9: [],
    remarks: [],
    sbu:[,Validators.required],
    serviceType: [],
    startDate: [,],
    status: [,],
    storeNumber: [, ],
    updatedBy: [],
    updatedOn: [],
    voucherAmount: [, Validators.required],
    voucherId: [, ],
    voucherDate: [new Date(), Validators.required],
    voucherStatus: [],
  });

  constructor(
    public dialogRef: MatDialogRef < any > ,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private auth: AuthService,
    private fb: FormBuilder,
    public cs: CommonService,
    private service: PaymentService,
    private cas: CommonApiService,
    private inquiryService: InquiryService,
    private storeService: StorageunitService,
    private quoteService: QuoteService,
    private agreementService: AgreementService,
    private invoiceService: InvoiceService,
    private workOrderService: WorkorderService, ) {}

  sub = new Subscription();
  submitted = false;


  ngOnInit(): void {
    this.dropdownlist();
    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOn.disable();
    if (this.data.pageflow != 'New' && this.data.pageflow != 'Copy From Invoice' && this.data.pageflow != 'Copy From Agreement' && this.data.pageflow != 'Copy From WorkOrder') {
      if (this.data.pageflow == 'Display')
        this.form.disable();
      this.form.controls.voucherId.disable();
      this.fill();

    }

    this.form.controls.copyFrom.valueChanges.subscribe(x => {
      this.copyFromdropdown(x)
    });

    this.form.controls.storeNumber.valueChanges.subscribe(x => {
      this.storeService.Get(this.form.controls.storeNumber.value).subscribe(res => {
        this.form.controls.phase.patchValue(res.phase)
      })
    })

    this.form.controls.contractNumber.valueChanges.subscribe(x => {
      this.storageunitList1 = []
      this.sub.add(this.agreementService.Get(x).subscribe(res => {
        res.storeNumbers.forEach(xx =>{
          this.storageunitList.forEach(agreement => {
            if(agreement.value == xx.storeNumber){
              this.storageunitList1.push(agreement);
              this.form.controls.storeNumber.patchValue(this.agreement)
            }
          });
        })
    }))
    });

    this.form.controls.modeOfPayment.valueChanges.subscribe(x => {
    if(x == "Cash"){
      this.form.controls.voucherStatus.patchValue("Approved");
    }else{
      this.form.controls.voucherStatus.patchValue("Pending Approval");
    }
    });

    this.form.controls.documentType.valueChanges.subscribe(x => {
      if (x == "Storage Services") {
    
      }  if (x == "Miscellaneous") {
        this.agreementList = [];
        this.sub.add(this.workOrderService.search({
          customerId: this.form.controls.customerName.value != null ? [this.form.controls.customerName.value]: [] 
        }).subscribe(res => {
          res.forEach((x: any) => {
       //     if(x.status == "Completed"){
              this.agreementList.push({
                value: x.workOrderId,
                label: x.workOrderId
              })
     //       }
          })
          if(this.data.pageflow == 'Copy From WorkOrder'){
            this.form.controls.contractNumber.patchValue(this.data.workOrderId);
          }
        }))
      }
    });
    
 

  

    this.form.controls.referenceField4.valueChanges.subscribe(x => {
    if(x == "Agreement"){
      this.agreementList = [];
      this.sub.add(this.agreementService.search({
        customerName: this.form.controls.customerName.value != null ? this.form.controls.customerName.value: [] 
      }).subscribe(res => {
        res.forEach((x: any) => {
          if(x.status != "Closed"){
            this.agreementList.push({
              value: x.agreementNumber,
              label: x.agreementNumber
            })
          }
        })

        if(this.data.pageflow == "Copy From Agreement"){
          this.form.controls.contractNumber.patchValue(this.data.agreementNumber);
        }

      }))
    }else{
      this.agreementList = [];
      this.sub.add(this.invoiceService.search({
        customerId: this.form.controls.customerName.value != null ? [this.form.controls.customerName.value]: [] 
      }).subscribe(res => {
        res.forEach((x: any) => {
          if(x.invoiceDocumentStatus == "Open"){
            this.agreementList.push({
              value: x.invoiceNumber,
              label: x.invoiceNumber
            })
          }
        })
        if(this.data.pageflow == "Copy From Invoice")
      this.form.controls.contractNumber.patchValue(this.data.invoiceNumber);

      }))
    }
    });

    this.form.controls.customerName.valueChanges.subscribe(x => {
      if (this.form.controls.documentType.value == "Storage Services") {
        if(x == "Agreement"){
          this.agreementList = [];
          this.sub.add(this.agreementService.search({
            customerName: this.form.controls.customerName.value != null ? this.form.controls.customerName.value: [] 
          }).subscribe(res => {
            res.forEach((x: any) => {
              if(x.status != "Closed"){
                this.agreementList.push({
                  value: x.agreementNumber,
                  label: x.agreementNumber
                })
              }
            })
          }))
        }
        if(x == "Invoice"){
          this.agreementList = [];
          this.sub.add(this.invoiceService.search({
            customerId: this.form.controls.customerName.value != null ? [this.form.controls.customerName.value]: [] 
          }).subscribe(res => {
            res.forEach((x: any) => {
              if(x.invoiceDocumentStatus == "Open"){
                this.agreementList.push({
                  value: x.invoiceNumber,
                  label: x.invoiceNumber
                })
              }
            })
          }))
        }
      } if (this.form.controls.documentType.value == "Miscellaneous") {
        this.agreementList = [];
        this.sub.add(this.workOrderService.search({
          customerId: [this.form.controls.customerName.value]
        }).subscribe(res => {
          res.forEach((x: any) => {
         //   if(x.status == "Completed"){
              this.agreementList.push({
                value: x.workOrderId,
                label: x.workOrderId
              })
          //  }
          })
        }))
      }
    })


    if (this.data.pageflow == "Copy From Agreement") {
      this.form.controls.customerName.patchValue(this.data.customerId);
      this.form.controls.documentType.patchValue('Storage Services');
      this.form.controls.referenceField4.patchValue('Agreement');
        this.form.controls.sbu.patchValue(this.data.sbu);
        this.form.controls.period.patchValue(this.data.rentPeriod);
        this.form.controls.voucherAmount.patchValue(this.data.rent);
    }

    if(this.data.pageflow == 'Copy From Invoice'){
      this.agreementList = [];
      this.form.controls.referenceField4.patchValue('Invoice');
      this.form.controls.documentType.patchValue('Storage Services');
      this.form.controls.voucherAmount.patchValue(this.data.totalAfterDiscount);
      this.form.controls.customerName.patchValue(this.data.customerId);
      this.form.controls.referenceField6.patchValue(this.data.currency);
      this.form.controls.sbu.patchValue(this.data.sbu);
      // this.form.controls.endDate.patchValue(this.data.endDate);
      // this.form.controls.startDate.patchValue(this.data.startDate);
      
      }

      if (this.data.pageflow == "Copy From WorkOrder") {
        this.form.controls.documentType.patchValue('Miscellaneous');
        // this.form.controls.referenceField2.patchValue(this.data.jobCard);
        //   this.form.controls.referenceField3.patchValue(this.data.processedBy);
          this.form.controls.contractNumber.patchValue(this.data.workOrderId);
            this.form.controls.customerName.patchValue(this.data.customerId);
            this.form.controls.sbu.patchValue(this.data.sbu);
      }

  }




  modeofpaymentList: any[] = [];
  paymentperiodList: any[] = [];
  storageunitList: any[] = [];
  storageunitList1: any[] = [];
  agreementList: any[] = [];
  statusList: any[] = [];
  customerIDList: any[] = [];
  documentList: any[] = [];
  processedByList: any[] = [];
  employeeList: any[] = [];
  workOrderSbuList: any[] = [];
  currencyList: any[] = [];
  dropdownlist() {
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.modeofpayment.url,
      this.cas.dropdownlist.setup.paymentperiod.url,
      this.cas.dropdownlist.trans.storageunit.url,
      //   this.cas.dropdownlist.trans.agreement.url,
      this.cas.dropdownlist.setup.accountstatus.url,
      this.cas.dropdownlist.trans.customerID.url,
      this.cas.dropdownlist.setup.documentstatus.url,
      this.cas.dropdownlist.setup.workorderprocessedby.url,
      //this.cas.dropdownlist.setup.employee.url,
      this.cas.dropdownlist.setup.sbu.url,
      this.cas.dropdownlist.setup.invoicecurrency.url,
    ]).subscribe((results) => {
      this.modeofpaymentList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.modeofpayment.key);
      this.paymentperiodList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.paymentperiod.key);
      this.storageunitList = this.cas.foreachlist2(results[2], this.cas.dropdownlist.trans.storageunit.key);
      //  this.agreementList = this.cas.foreachlist1(results[3], this.cas.dropdownlist.trans.agreement.key);
      this.statusList = this.cas.foreachlist(results[3], this.cas.dropdownlist.setup.accountstatus.key);
      this.customerIDList = this.cas.foreachlist2(results[4], this.cas.dropdownlist.trans.customerID.key);
      this.documentList = this.cas.foreachlist2(results[5], this.cas.dropdownlist.setup.documentstatus.key);
      this.processedByList = this.cas.foreachlist(results[6], this.cas.dropdownlist.setup.workorderprocessedby.key);
     // this.employeeList = this.cas.foreachlist(results[7], this.cas.dropdownlist.setup.employee.key);
     this.workOrderSbuList = this.cas.foreachlist2(results[7], this.cas.dropdownlist.setup.sbu.key);
     this.currencyList = this.cas.foreachlist2(results[8], this.cas.dropdownlist.setup.invoicecurrency.key);
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }


  documentNoSelect(){
    if (this.form.controls.documentType.value == "Miscellaneous" && this.form.controls.contractNumber.value) {
      this.sub.add(this.workOrderService.Get(this.form.controls.contractNumber.value).subscribe(res => {
        this.form.controls.referenceField2.patchValue(res.jobCard);
        this.form.controls.referenceField3.patchValue(res.woProcessedBy);
        this.form.controls.sbu.patchValue(res.workOrderSbu);
        let total = 0;
        res.itemServices.forEach(element => {
          total = total + Number(element.itemServiceTotal != null ? element.itemServiceTotal : 0);
        });
        this.form.controls.voucherAmount.patchValue(total);
        console.log( total )
      }))

    }
    else{
            
      if(this.form.controls.referenceField4.value == "Agreement" && this.form.controls.contractNumber.value != null){
        this.sub.add(this.agreementService.Get(this.form.controls.contractNumber.value).subscribe(res => {
        this.form.controls.period.patchValue(res.rentPeriod);
        this.form.controls.sbu.patchValue(res.sbu);
        this.form.controls.endDate.patchValue(res.startDate);
        this.form.controls.startDate.patchValue(res.endDate);
        // this.storageunitList.forEach(element => {
        //   res.storeNumbers.forEach(agreement => {
        //     if(agreement.storeNumber == element.value){
        //       this.storageunitList = [];
        //       this.storageunitList.push(element)
        //     }
        //   });
        // });
      }))
      }else{
        this.sub.add(this.invoiceService.Get(this.form.controls.contractNumber.value).subscribe(res => {
          this.form.controls.period.patchValue(res.rentPeriod);
         // this.form.controls.customerName.patchValue(res.customerId);
         // this.form.controls.voucherAmount.patchValue(res.totalAfterDiscount);
           this.form.controls.endDate.patchValue(res.documentEndDate);
           this.form.controls.startDate.patchValue(res.documentStartDate);
          this.form.controls.sbu.patchValue(res.sbu);
        }))
      }
    }
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


  agreement: any;
  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code).subscribe(res => {
        this.form.patchValue(res, {
          emitEvent: false
        });
        this.agreement = res.storeNumber
        if(this.form.controls.documentType.value == "Miscellaneous"){
          this.agreementList = [];
          this.sub.add(this.workOrderService.Getall().subscribe(ress => {
            ress.forEach((x: any) => {
              if(x.status == "Completed"){
                this.agreementList.push({
                  value: x.workOrderId,
                  label: x.workOrderId
                })
              }
            })
            this.form.controls.contractNumber.patchValue(res.contractNumber);
          }))
        }

        
          if (this.form.controls.referenceField4.value == "Agreement") {
    
              this.agreementList = [];
              this.sub.add(this.agreementService.Getall().subscribe(ress => {
                ress.forEach((x: any) => {
                  if(x.status != "Closed"){
                    this.agreementList.push({
                      value: x.agreementNumber,
                      label: x.agreementNumber
                    })
                  }
                })
                this.form.controls.contractNumber.patchValue(res.contractNumber);


              }))
            }
            if (this.form.controls.referenceField4.value == "Invoice") {
              this.agreementList = [];
              this.sub.add(this.invoiceService.Getall().subscribe(ress => {
                ress.forEach((x: any) => {
                  if(x.invoiceDocumentStatus == "Open"){
                    this.agreementList.push({
                      value: x.invoiceNumber,
                      label: x.invoiceNumber
                    })
                  }
                })
                this.form.controls.contractNumber.patchValue(res.contractNumber);
              }))
            }
    
    
            this.form.controls.voucherDate.patchValue(this.cs.dateapi1(this.form.controls.voucherDate.value));

            this.form.controls.startDate.patchValue(this.cs.dateapi1(this.form.controls.startDate.value));
            this.form.controls.endDate.patchValue(this.cs.dateapi1(this.form.controls.endDate.value));
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
    console.log(this.form)
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

    if(this.form.controls.modeOfPayment.value != 'Cash' && this.form.controls.modeOfPayment.value && !this.form.controls.paymentReference.value){
      this.toastr.error(
        "Please fill payment reference fields to continue",
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
    this.form.controls.voucherDate.patchValue(this.cs.day_callapiSearch(this.form.controls.voucherDate.value));
    this.form.controls.startDate.patchValue(this.cs.day_callapiSearch(this.form.controls.startDate.value));
    this.form.controls.endDate.patchValue(this.cs.day_callapiSearch(this.form.controls.endDate.value));
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
        this.toastr.success(res.voucherId + " Saved Successfully!", "Notification", {
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


  copyFromList: any[] = [];

  copyFromdropdown(x) {
    this.copyFromList = [];
    if (x == "Enquiry") {
      this.sub.add(this.inquiryService.Getall().subscribe((res: any[]) => {
        res.forEach((x: any) => this.copyFromList.push({
          value: x.enquiryId,
          label: x.enquiryId
        }))
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }
    if (x == "Quote") {
      this.sub.add(this.quoteService.Getall().subscribe((res: any[]) => {
        res.forEach((x: any) => this.copyFromList.push({
          value: x.quoteId,
          label: x.quoteId
        }))
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }
    if (x == "Agreement") {
      this.sub.add(this.agreementService.Getall().subscribe((res: any[]) => {
        res.forEach((x: any) => this.copyFromList.push({
          value: x.agreementNumber,
          label: x.agreementNumber
        }))
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }
  }

  copyFrom() {
    if (this.form.controls.copyFrom.value == "Enquiry") {
      this.sub.add(this.inquiryService.Get(this.form.controls.copyFromResult.value).subscribe(res => {
          this.form.controls.contractNumber.patchValue(res.enquiryId)
          this.spin.hide();
        },
        err => {
          this.cs.commonerror(err);
          this.spin.hide();
        }
      ));
    }
    if (this.form.controls.copyFrom.value == "Quote") {
      this.sub.add(this.quoteService.Get(this.form.controls.copyFromResult.value).subscribe(res => {
          this.form.controls.contractNumber.patchValue(res.quoteId)
          this.spin.hide();
        },
        err => {
          this.cs.commonerror(err);
          this.spin.hide();
        }
      ));
    }
    if (this.form.controls.copyFrom.value == "Agreement" && this.form.controls.copyFromResult.value != null) {
      this.sub.add(this.agreementService.Get(this.form.controls.copyFromResult.value).subscribe(res => {
          this.form.controls.contractNumber.patchValue(res.agreementNumber)
          this.spin.hide();
        },
        err => {
          this.cs.commonerror(err);
          this.spin.hide();
        }
      ));
    }

  }


  rentCalculation(){
    this.service.rentCalculation({
      endDate: this.form.controls.endDate.value,
      period: this.form.controls.period.value,
      rent: this.form.controls.rent.value,
      startDate:this.form.controls.startDate.value,
    }).subscribe(res => {
      this.form.controls.totalRent.patchValue(res.totalRent)
    })
  }

}
