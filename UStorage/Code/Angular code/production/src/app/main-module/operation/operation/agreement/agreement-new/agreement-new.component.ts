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
import { InquiryService } from '../../../crm/enquiry/inquiry.service';
import { QuoteService } from '../../../crm/quote/quote.service';
import { InvoiceNewComponent } from '../../invoice/invoice-new/invoice-new.component';
import {
  InvoiceService
} from '../../invoice/invoice.service';
import { PaymentNewComponent } from '../../payment/payment-new/payment-new.component';
import {
  PaymentService
} from '../../payment/payment.service';
import {
  AgreementService
} from '../agreement.service';
import { StoreTableComponent } from '../store-table/store-table.component';


@Component({
  selector: 'app-agreement-new',
  templateUrl: './agreement-new.component.html',
  styleUrls: ['./agreement-new.component.scss']
})
export class AgreementNewComponent implements OnInit {

  form = this.fb.group({
    address: [, Validators.required],
    agreementDiscount: [0,],
    agreementNumber: [],
    agreementType: [, Validators.required],
    civilIDNumber: [, Validators.required],
    copyTo: [],
    createdBy: [],
    createdOn: [],
    customerName: [, Validators.required],
    deletionIndicator: [],
    deposit: [],
    email: [, ],
    endDate: [],
    faxNumber: [],
    insurance: [],
    itemsToBeStored: [],
    itemsToBeStored1: [],
    itemsToBeStored2: [],
    itemsToBeStored3: [],
    itemsToBeStored4: [],
    itemsToBeStored5: [],
    itemsToBeStored6: [],
    itemsToBeStored7: [],
    itemsToBeStored8: [],
    itemsToBeStored9: [],
    itemsToBeStored10: [],
    itemsToBeStored11: [],
    itemsToBeStored12: [],
    location: [, ],
    nationality: [, Validators.required],
    notes: [],
    others: [],
    paymentTerms: [, Validators.required],
    phoneNumber: [, Validators.required],
    quoteNumber: [, ],
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
    rent: [0, ],
    rentPeriod: [, Validators.required],
    secondaryNumber: [],
    sbu: ['2000001', Validators.required],
    status: ["Open"],
    size: [,],
    startDate: [, Validators.required],
    storeNumbers: [, Validators.required],
    totalAfterDiscount: [],
    totalRent: [0, Validators.required],
    updatedBy: [],
    updatedOn: [],
  });
  storeEdit: boolean;
  resultTable: any;
  storeNo: any;
  storeLines: any;


  constructor(
    public dialogRef: MatDialogRef < any > ,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private auth: AuthService,
    private fb: FormBuilder,
    public cs: CommonService,
    private service: AgreementService,
    private invoiceService: InvoiceService,
    private paymentService: PaymentService,
    private cas: CommonApiService,
    private customerService: CustomerService,
    private router: Router,
    private storageService: StorageunitService,
    private inquiryService: InquiryService,
    private quoteService: QuoteService,
    public dialog: MatDialog,
  ) {}

  sub = new Subscription();
  submitted = false;
  ngOnInit(): void {
    this.storeEdit = false;
    this.dropdownlist();
    this.form.controls.agreementNumber.disable();
    this.form.controls.totalAfterDiscount.disable();
    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOn.disable();
    this.form.controls.rent.disable();
    if (this.data.pageflow != 'New' && this.data.pageflow != 'Copy From Quote') {
      if (this.data.pageflow == 'Display')
        this.form.disable();
      this.fill();
    }
    if (this.data.pageflow == 'New') {
      this.form.controls.agreementDiscount.disable();
    }

    this.form.controls.agreementDiscount.valueChanges.subscribe(x => {
      if (x && this.form.controls.referenceField3.value == "Percentage") {
        this.percentageCalculation();
      } else {
        this.kwdCalculation()
      }
    });

    this.form.controls.referenceField3.valueChanges.subscribe(x => {
      this.form.controls.agreementDiscount.enable();
    });


    this.form.controls.totalRent.valueChanges.subscribe(x => {
      if (x && this.form.controls.referenceField3.value == "Percentage") {
        this.percentageCalculation();
      } else {
        this.kwdCalculation()
      }
    });


    this.form.controls.customerName.valueChanges.subscribe(x => {
      this.form.controls.referenceField2.patchValue('');
      this.sub.add(this.customerService.Get(this.form.controls.customerName.value).subscribe(res => {
        this.form.controls.civilIDNumber.patchValue(res.civilId);
        this.form.controls.nationality.patchValue(res.nationality);
        this.form.controls.email.patchValue(res.email);
        this.form.controls.address.patchValue(res.address);
        this.form.controls.phoneNumber.patchValue(res.mobileNumber);
        this.form.controls.secondaryNumber.patchValue(res.phoneNumber);
        this.form.controls.faxNumber.patchValue(res.faxNumber);
      }));
    });

    // this.form.controls.location.valueChanges.subscribe(x => {
    //   this.storageunitList = [];
    //   this.storageUnfiltered.forEach((result: any) => {
    //     if (x == result.phase && result.availability == "Vacant")
    //       this.storageunitList.push({
    //         value: result.itemCode,
    //         label: result.codeId
    //       });
    //   });
      
    // });
    // this.form.controls.storeNumber.valueChanges.subscribe(x => {
    //   this.sub.add(this.storageService.Get(x).subscribe(res => {
    //     this.form.controls.size.patchValue(res.roundedDimention)
    //   }));
    // });


    if (this.data.pageflow == "Copy From Quote") {
      this.form.controls.customerName.patchValue(this.data.customerName);
      this.form.controls.quoteNumber.patchValue(this.data.quoteNumber);
      this.form.controls.rent.patchValue(this.data.rent);
     
      this.form.controls.agreementType.patchValue('New');
      this.form.controls.referenceField2.patchValue('Quote');
      this.copyFromdropdown(this.form.controls.referenceField2.value);
      this.form.controls.size.patchValue(this.data.size);

      
    }

    this.form.controls.referenceField2.valueChanges.subscribe(x => {
     if(x == "Quote"){
      this.copyFromdropdown(x);
     }
    });

       this.form.controls.quoteNumber.valueChanges.subscribe(x => {
if(x != null){
  this.sub.add(this.quoteService.search({quoteId: [x]}).subscribe(res => {
    if(res.length >= 1 ){
     this.form.controls.size.patchValue(res[0].storeSize);
     this.form.controls.rentPeriod.patchValue(res[0].referenceField1);
     this.form.controls.rent.patchValue(res[0].rent);
     this.form.controls.totalRent.patchValue(res[0].rent);
     this.form.controls.totalAfterDiscount.patchValue(res[0].rent);
    }
       }));
}
    });


    this.form.controls.size.valueChanges.subscribe(x => {
      this.form.controls.storeNumbers.reset();
      this.storeNumbersFE = []
      if(x != null){
 //   this.findStorageNumber();
      }
    });

    this.form.controls.rentPeriod.valueChanges.subscribe(x => {
      if(x != null){
      //  this.findStorageNumber();
      }
      });

    // this.form.controls.rentPeriod.valueChanges.subscribe(x => {
    //   this.sub.add(this.storageService.Get(this.form.controls.storeNumber.value).subscribe(res => {
    //     if (x == "Weekly") {
    //       this.form.controls.rent.patchValue(res.weekly);
    //     }
    //     if (x == "Monthly") {
    //       this.form.controls.rent.patchValue(res.monthly);
    //     }
    //     if (x == "Quarterly") {
    //       this.form.controls.rent.patchValue(res.quarterly);
    //     }
    //     if (x == "HALFYEARLY") {
    //       this.form.controls.rent.patchValue(res.halfYearly);
    //     }
    //     if (x == "Yearly") {
    //       this.form.controls.rent.patchValue(res.yearly);
    //     }
    //     if (x == "Meter Sq") {
    //       let rent = (res.roundedDimention) * (res.priceMeterSquare)
    //       this.form.controls.rent.patchValue(rent);
    //     }
    //   }));
    // });
  }


  nationalityList: any[] = [];
  storageunitList: any[] = [];
  storageUnfiltered: any;
  storenumbersizeList: any[] = [];
  customerIDList: any[] = [];
  paymenttermList: any[] = [];
  phaseList: any[] = [];
  statusList: any[] = [];
  workOrderSbuList: any[] = [];
  dropdownlist() {
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.nationality.url,
      this.cas.dropdownlist.trans.storageunit.url,
      this.cas.dropdownlist.setup.storenumbersize.url,
      this.cas.dropdownlist.trans.customerID.url,
      this.cas.dropdownlist.setup.paymentterm.url,
      this.cas.dropdownlist.setup.phase.url,
      this.cas.dropdownlist.setup.status.url,
      this.cas.dropdownlist.setup.sbu.url,
      
    ]).subscribe((results) => {
      this.nationalityList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.nationality.key);
      this.storageUnfiltered = results[1];
      this.storageUnfiltered.forEach((x: any) => {
             if(x.availability == "Vacant"){
          this.storageunitList.push({
            value: x.itemCode,
            label: x.codeId + ' - ' +  x.storeSizeMeterSquare
          });
        }
      });
      this.storenumbersizeList = this.cas.foreachlist1(results[2], this.cas.dropdownlist.setup.storenumbersize.key);
      this.storenumbersizeList = this.storenumbersizeList.sort((a, b) => (parseFloat(a.value) > parseFloat(b.value)) ? 1 : -1);
      this.customerIDList = this.cas.foreachlist2(results[3], this.cas.dropdownlist.trans.customerID.key);
      this.paymenttermList = this.cas.foreachlist(results[4], this.cas.dropdownlist.setup.paymentterm.key);
      this.workOrderSbuList = this.cas.foreachlist2(results[7], this.cas.dropdownlist.setup.sbu.key);
      this.phaseList = this.cas.foreachlist(results[5], this.cas.dropdownlist.setup.paymentterm.key);
      this.statusList = this.cas.foreachlist1(results[6], this.cas.dropdownlist.setup.status.key).filter(x => ['Open','Renewed','Closed', 'Cancelled', 'Expired'].includes(x.value));


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
  public myModel = ''
  //public mask = [/\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/]
  public mask = [/\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/]


  percentageCalculation() {
    let discountAmount
    discountAmount = (this.form.controls.agreementDiscount.value / 100) * this.form.controls.totalRent.value
    console.log(discountAmount)
    let totalAmount = this.form.controls.totalRent.value - discountAmount;
    this.form.controls.totalAfterDiscount.patchValue(totalAmount);

  }
  kwdCalculation() {
    let totalAmount = this.form.controls.totalRent.value - this.form.controls.agreementDiscount.value;
    this.form.controls.totalAfterDiscount.patchValue(totalAmount);

  }


  storeIDChange(e){
    this.form.controls.storeNumbers.patchValue(e.value);
    console.log(this.form.controls.storeNumbers.value)
  }
  
  totalAfterDiscount = '';
  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code).subscribe(res => {
      this.resultTable = res.storeNumbers;
      this.storeNo = res.referenceField4;
        this.form.patchValue(res, {
          emitEvent: false
        });

        this.sub.add(this.quoteService.search({customerCode: [this.form.controls.customerName.value]}).subscribe((res: any[]) => {
          res.forEach((x: any) => {
            if(x.status == "Open"){
              this.copyFromList.push({
                value: x.quoteId,
                label: x.quoteId
              })
 
          this.form.controls.quoteNumber.patchValue(res[0].quoteId);
            }
          })
        }, err => {
          this.cs.commonerror(err);
          this.spin.hide();
        }));

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
storeNumbersFE: any[] = []
  submit() {
//     this.storeNumbersFE = [];
//     console.log(this.storeNumbersFE)
//     if(this.storeNumbersFE.length > 0){
//       console.log(this.storeNumbersFE)
//       this.form.controls.storeNumbers.patchValue(this.storeNumbersFE);
//     }
let any = this.cs.findInvalidControls(this.form);
 console.log(any)
 if(this.storeLines){
  this.form.controls.storeNumbers.patchValue(this.storeLines)
 }
    
 console.log(this.form)
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
    this.form.controls.startDate.patchValue(this.cs.day_callapiSearch(this.form.controls.startDate.value));
    this.form.controls.endDate.patchValue(this.cs.day_callapiSearch(this.form.controls.endDate.value));
    if (this.data.code && this.form.controls.agreementType.value == "New") {
      this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code).subscribe(res => {
if(this.form.controls.status.value == 'Closed' || this.form.controls.status.value == 'Cancelled' || this.form.controls.status.value == 'Expired'){
  this.form.controls.storeNumbers.value.forEach(element => {
    this.sub.add(this.storageService.Update({availability: 'Vacant'}, element.storeNumber).subscribe(storageResult => {
  
    }))
  });
}else{ 
  this.form.controls.storeNumbers.value.forEach(element => {
    this.sub.add(this.storageService.Update({availability: 'Occupied'}, element.storeNumber).subscribe(storageResult => {
  
    }))
  });
}
        
        this.toastr.success(this.data.code + " updated successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();
        this.dialogRef.close();
        
      //  return;
      }, err => {

        this.cs.commonerror(err);
        this.spin.hide();

      }));
    }
    else  {
      this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {

        if(this.form.controls.status.value == 'Closed' || this.form.controls.status.value == 'Cancelled' || this.form.controls.status.value == 'Expired'){
          this.form.controls.storeNumbers.value.forEach(element => {
            this.sub.add(this.storageService.Update({availability: 'Vacant'}, element.storeNumber).subscribe(storageResult => {
          
            }))
          });
        }else{
          this.form.controls.storeNumbers.value.forEach(element => {
            this.sub.add(this.storageService.Update({availability: 'Occupied'}, element.storeNumber).subscribe(storageResult => {
          
            }))
          });
        }

        this.toastr.success(res.agreementNumber + " Saved Successfully!", "Notification", {
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
    if (this.form.controls.copyTo.value == "Invoice") {

      this.dialogRef.close();
      const dialogRef = this.dialog.open(InvoiceNewComponent, {
        disableClose: true,
      //  width: '55%',
        maxWidth: '80%',
        data: { 
          pageflow: 'Copy From Agreement', 
          agreementNumber: this.form.controls.agreementNumber.value,
          customerId: this.form.controls.customerName.value,
          sbu: this.form.controls.sbu.value,
          rentPeriod: this.form.controls.rentPeriod.value,
      }
      });
    
      dialogRef.afterClosed().subscribe(result => {
        
        this.router.navigate(["/main/operation/invoice"]);
      });
    }
    if (this.form.controls.copyTo.value == "Payment") {
    
      this.dialogRef.close();
      const dialogRef = this.dialog.open(PaymentNewComponent, {
        disableClose: true,
      //  width: '55%',
        maxWidth: '80%',
        data: { 
          pageflow: 'Copy From Agreement', 
          agreementNumber: this.form.controls.agreementNumber.value,
          customerId: this.form.controls.customerName.value,
          sbu: this.form.controls.sbu.value,
          rent: this.form.controls.sbu.value,
          rentPeriod: this.form.controls.rentPeriod.value,
      }
      });
    
      dialogRef.afterClosed().subscribe(result => {
        
        this.router.navigate(["/main/operation/payment"]);
      });
    }

  }


  copyFromList: any[] = [];
  copyFromdropdown(x) {
    this.copyFromList = [];
    if (x == "Quote") {
      this.sub.add(this.quoteService.search({customerCode: [this.form.controls.customerName.value]}).subscribe((res: any[]) => {
        res.forEach((x: any) => {
          if(x.status == "Open"){
            this.copyFromList.push({
              value: x.quoteId,
              label: x.quoteId
            })
          }
        })
        if(this.data.pageflow == 'Copy From Quote'){
          this.form.controls.quoteNumber.patchValue(this.data.quoteNumber);
        }
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }
  }

  // findStorageNumber(){
  //   this.sub.add(this.storageService.search({storeSizeMeterSquare : [this.form.controls.size.value]}).subscribe(res => {
  //     this.storageunitList = [];
  //       res.forEach(element => {
  //       if(element.availability == "Vacant"){
  //         this.storageunitList.push({
  //           value: element.itemCode,
  //           label: element.codeId + ' - ' + element.storeSizeMeterSquare
  //         });
  //       }
  //       });
  //       if(res.length >= 1){
  //         console.log('store')
  //         if ( this.form.controls.rentPeriod.value == "WEEKLY") {
  //           this.form.controls.rent.patchValue(res[0].weekly);
  //         }
  //         if (this.form.controls.rentPeriod.value == "MONTHLY") {
  //           this.form.controls.rent.patchValue(res[0].monthly);
  //         }
  //         if (this.form.controls.rentPeriod.value == "QUARTERLY") {
  //           this.form.controls.rent.patchValue(res[0].quarterly);
  //         }
  //         if (this.form.controls.rentPeriod.value == "HALFYEARLY") {
  //           this.form.controls.rent.patchValue(res[0].halfYearly);
  //         }
  //         if (this.form.controls.rentPeriod.value == "YEARLY") {
  //           this.form.controls.rent.patchValue(res[0].yearly);
  //         }
  //       }else{
  //         this.form.controls.rent.patchValue(0);
  //       }
  //   }))
  // }


  enableStoreEdit(){
    this.storeEdit = !this.storeEdit
  }


  
  storeDetails() {

    if (!this.form.controls.rentPeriod.value) {
      this.toastr.error(
        "Please fill rent period fields to continue",
        "Notification", {
          timeOut: 2000,
          progressBar: false,
        }
      );

      this.cs.notifyOther(true);
      return;
    }

    const dialogRef = this.dialog.open(StoreTableComponent, {
      disableClose: true,
      width: '58%',
      maxWidth: '80%',
      data: {
       storeSize: this.resultTable, rentPeriod: this.form.controls.rentPeriod.value
      }
    });

    dialogRef.afterClosed().subscribe(result => {
        if(result){
          this.storeLines = result.data;
          this.resultTable = result.data;
          //if(result.storeChanged == true)
          this.form.controls.rent.patchValue(result.totalRent);
          this.rentCalculation();
        }
    });
  }


  rentCalculation(){
    this.service.rentCalculation({
      endDate: this.form.controls.endDate.value,
      period: this.form.controls.rentPeriod.value,
      rent: this.form.controls.rent.value,
      startDate:this.form.controls.startDate.value,
    }).subscribe(res => {
      this.form.controls.totalRent.patchValue(res.totalRent)
    })
  }
}
