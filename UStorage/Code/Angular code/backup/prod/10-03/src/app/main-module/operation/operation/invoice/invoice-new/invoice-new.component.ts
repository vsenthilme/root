import {
  SelectionModel
} from '@angular/cdk/collections';
import {
  Component,
  Inject,
  OnInit,
  ViewChild
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
  MatPaginator
} from '@angular/material/paginator';
import {
  MatSort
} from '@angular/material/sort';
import {
  MatTableDataSource
} from '@angular/material/table';
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
  InquiryService
} from '../../../crm/enquiry/inquiry.service';
import {
  QuoteService
} from '../../../crm/quote/quote.service';
import {
  AgreementService
} from '../../agreement/agreement.service';
import {
  PaymentNewComponent
} from '../../payment/payment-new/payment-new.component';
import {
  WorkorderService
} from '../../workorder/workorder.service';
import {
  CopyfromComponent
} from '../copyfrom/copyfrom.component';
import {
  InvoiceService
} from '../invoice.service';
export interface PeriodicElement {
  usercode: string;
  name: string;
  admin: string;
  role: string;
  userid: string;
  password: string;
  status: string;
  email: string;
}

const ELEMENT_DATA: PeriodicElement[] = [{
  usercode: "test",
  name: 'test',
  admin: 'test',
  role: 'test',
  userid: 'test',
  password: 'test',
  status: 'test',
  email: 'test'
}, ];

@Component({
  selector: 'app-invoice-new',
  templateUrl: './invoice-new.component.html',
  styleUrls: ['./invoice-new.component.scss']
})
export class InvoiceNewComponent implements OnInit {


  form = this.fb.group({
    documentNumber: [],
    createdBy: [],
    createdOn: [],
    copyTo: [],
    copyFrom: [],
    copyFromResult: [],
    customerId: [, Validators.required],
    deletionIndicator: [],
    documentEndDate: [],
    invoiceAmount: [, Validators.required],
    invoiceCurrency: ['KWD', Validators.required],
    invoiceDate: [new Date(), Validators.required],
    invoiceDiscount: [],
    invoiceDocumentStatus: ["Open", Validators.required],
    invoiceNumber: [],
    monthlyRent: [],
    quantity: [, ],
    referenceField1: [, ],
    referenceField10: [],
    referenceField2: [,Validators.required],
    referenceField3: [],
    referenceField4: [],
    referenceField5: [],
    referenceField6: [],
    referenceField7: [],
    referenceField8: [],
    referenceField9: [],
    remarks: [],
    sbu:[,Validators.required],
    documentStartDate: [, ],
    totalAfterDiscount: [],
    updatedBy: [],
    updatedOn: [],
    unit: ['Month', ],
  });

  constructor(
    public dialogRef: MatDialogRef < any > ,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService,
    private spin: NgxSpinnerService, public dialog: MatDialog,
    private auth: AuthService,
    private fb: FormBuilder,
    public cs: CommonService,
    private service: InvoiceService,
    private inquiryService: InquiryService,
    private quoteService: QuoteService,
    private agreementService: AgreementService,
    private workOrderService: WorkorderService,
    private cas: CommonApiService,
    private router: Router, ) {}

  sub = new Subscription();
  submitted = false;


  ngOnInit(): void {
    this.dropdownlist();
    this.form.controls.createdOn.disable();

    this.form.controls.invoiceNumber.disable();
    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOn.disable();
    this.form.controls.totalAfterDiscount.disable();
    if (this.data.pageflow != 'New' && this.data.pageflow != 'Copy From Agreement') {
      if (this.data.pageflow == 'Display')
        this.form.disable();
      this.fill();
    }
    if (this.data.pageflow == 'New') {
      this.form.controls.invoiceDiscount.disable();
    }
    this.form.controls.invoiceDiscount.valueChanges.subscribe(x => {
      if (x && this.form.controls.referenceField1.value == "Percentage") {
        this.percentageCalculation();
      } else {
        this.kwdCalculation()
      }
    });

    this.form.controls.invoiceAmount.valueChanges.subscribe(x => {
      if (x && this.form.controls.referenceField1.value == "Percentage") {
        this.percentageCalculation();
      } else {
        this.kwdCalculation()
      }
    });
    this.form.controls.referenceField1.valueChanges.subscribe(x => {
      this.form.controls.invoiceDiscount.enable();
    });

    // this.form.controls.copyFrom.valueChanges.subscribe(x => {
    //   this.copyFromdropdown(x)
    // });


    this.form.controls.referenceField2.valueChanges.subscribe(x => {
      this.copyFromdropdown(x);
    });

    this.form.controls.customerId.valueChanges.subscribe(x => {
      this.form.controls.referenceField2.patchValue(null);
    });



    this.form.controls.quantity.valueChanges.subscribe(x => {
      let agreementRent = this.form.controls.quantity.value as number * this.form.controls.monthlyRent.value as number;
      this.form.controls.invoiceAmount.patchValue(agreementRent);

      if (this.form.controls.invoiceDiscount.value && this.form.controls.referenceField1.value == "Percentage") {
        this.percentageCalculation();
      } else {
        this.kwdCalculation()
      }


      this.form.controls.copyTo.valueChanges.subscribe(x => {
        this.copyTo()
      });

    });



    if (this.data.pageflow == "Copy From Agreement") {
      this.sub.add(this.agreementService.Getall().subscribe((ress: any[]) => {
        ress.sort((a, b) => (a.createdOn > b.createdOn) ? -1 : 1);
        // ress.forEach((x: any) => {
        //   if (x.status == "Open") {
        //     this.copyFromList.push({
        //       value: x.quoteId,
        //       label: x.quoteId
        //     })
        //     console.log(this.copyFromList)
        //   }
        // })
        this.form.controls.customerId.patchValue(this.data.customerId);
      this.form.controls.referenceField2.patchValue('Agreement');
      this.form.controls.documentNumber.patchValue(this.data.agreementNumber);
      this.form.controls.referenceField5.patchValue(this.data.rentPeriod);
      
      this.form.controls.sbu.patchValue(this.data.sbu);
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }
  }

documentNo(){
  if (this.form.controls.referenceField2.value == "Agreement") {
    this.sub.add(this.agreementService.Get(this.form.controls.documentNumber.value).subscribe(res => {
      if (res) {
        this.form.controls.monthlyRent.patchValue((res.rent));
   //     this.form.controls.referenceField3.patchValue(res.location);
    //    this.form.controls.referenceField4.patchValue(res.storeNumbers.storeNumber[0]);
      //  this.form.controls.invoiceAmount.patchValue(res.totalRent);
     //   this.form.controls.invoiceDiscount.patchValue(res.agreementDiscount);
       // this.form.controls.totalAfterDiscount.patchValue(res.totalAfterDiscount);
        this.form.controls.referenceField1.patchValue(res.referenceField3);
         this.form.controls.documentStartDate.patchValue(this.cs.dateapi1(res.startDate));
         this.form.controls.documentEndDate.patchValue(this.cs.dateapi1(res.endDate));

        this.form.controls.sbu.patchValue(res.sbu);
        this.dataSource1 = new MatTableDataSource<any>(res.storeNumbers);
        this.form.controls.referenceField5.patchValue(res.rentPeriod);
      }
    }));
  }
  if(this.form.controls.referenceField2.value == "Work Order"){
    this.sub.add(this.workOrderService.Get(this.form.controls.documentNumber.value).subscribe(res => {
      this.dataSource = new MatTableDataSource<any>(res.itemServices);
      this.form.controls.invoiceAmount.patchValue(this.getTotal());
      this.form.controls.totalAfterDiscount.patchValue(this.getTotal());
      
      this.form.controls.sbu.patchValue(res.workOrderSbu);
    }));
  }
}
  customerIDList: any[] = [];
  statusIDList: any[] = [];
  currencyList: any[] = [];
  workOrderSbuList: any[] = [];

  dropdownlist() {
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.trans.customerID.url,
      this.cas.dropdownlist.setup.invoicedocumentstatus.url,
      this.cas.dropdownlist.setup.invoicecurrency.url,
      this.cas.dropdownlist.setup.sbu.url,
    ]).subscribe((results) => {
      this.customerIDList = this.cas.foreachlist2(results[0], this.cas.dropdownlist.trans.customerID.key);
      this.statusIDList = this.cas.foreachlist1(results[1], this.cas.dropdownlist.setup.invoicedocumentstatus.key);
      this.workOrderSbuList = this.cas.foreachlist2(results[3], this.cas.dropdownlist.setup.sbu.key);
      this.currencyList = this.cas.foreachlist2(results[2], this.cas.dropdownlist.setup.invoicecurrency.key);
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }


  percentageCalculation() {
    let discountAmount
    discountAmount = (this.form.controls.invoiceDiscount.value / 100) * this.form.controls.invoiceAmount.value
    let totalAmount = this.form.controls.invoiceAmount.value - discountAmount;
    this.form.controls.totalAfterDiscount.patchValue(totalAmount);

  }
  kwdCalculation() {
    let totalAmount = this.form.controls.invoiceAmount.value - this.form.controls.invoiceDiscount.value;
    this.form.controls.totalAfterDiscount.patchValue(totalAmount);

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

  displayedColumns: string[] = [ 'usercode', 'name', 'admin', 'role'];
  dataSource = new MatTableDataSource < any > (ELEMENT_DATA);
  selection = new SelectionModel < any > (true, []);

  displayedColumns1: string[] = [ 'storeNumber', 'size', 'location', 'rent'];
  dataSource1 = new MatTableDataSource < any > (ELEMENT_DATA);



  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;


  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
    this.dataSource1.paginator = this.paginator;
    this.dataSource1.sort = this.sort;
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

  new(): void {

    const dialogRef = this.dialog.open(CopyfromComponent, {
      disableClose: true,
      width: '55%',
      maxWidth: '80%',
      position: {
        top: '9%',
      },
    });

    dialogRef.afterClosed().subscribe(result => {});
  }

  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code).subscribe(res => {
        this.form.patchValue(res, {
          emitEvent: false
        });
        if (this.form.controls.referenceField2.value == "Inquiry") {
          this.sub.add(this.inquiryService.Getall().subscribe((ress: any[]) => {
            if (ress) {
              ress.sort((a, b) => (a.createdOn > b.createdOn) ? -1 : 1);
              ress.forEach((x: any) => {
                if (x.status == "Open") {
                  this.copyFromList.push({
                    value: x.enquiryId,
                    label: x.enquiryId
                  })
                }
              })
            }
            this.form.controls.documentNumber.patchValue(res.documentNumber)
   
          }, err => {
            this.cs.commonerror(err);
            this.spin.hide();
          }));
        }
        if (this.form.controls.referenceField2.value == "Quote") {
          this.sub.add(this.quoteService.Getall().subscribe((ress: any[]) => {
            if (ress) {
              ress.sort((a, b) => (a.createdOn > b.createdOn) ? -1 : 1);
              ress.forEach((x: any) => {
                if (x.status == "Open") {
                  this.copyFromList.push({
                    value: x.quoteId,
                    label: x.quoteId
                  })
                }
              })
            }
            this.form.controls.documentNumber.patchValue(res.documentNumber)
          }, err => {
            this.cs.commonerror(err);
            this.spin.hide();
          }));
        }
        if (this.form.controls.referenceField2.value == "Agreement") {
          this.sub.add(this.agreementService.Getall().subscribe((ress: any[]) => {
            ress.sort((a, b) => (a.createdOn > b.createdOn) ? -1 : 1);
            // ress.forEach((x: any) => this.copyFromList.push({
            //   value: x.agreementNumber,
            //   label: x.agreementNumber
            // }))
            ress.forEach((x: any) => {
              if (x.status != "Closed") {
                this.copyFromList.push({
                  value: x.agreementNumber,
                  label: x.agreementNumber
                })
              }
            })
            this.form.controls.documentNumber.patchValue(res.documentNumber)
             this.sub.add(this.agreementService.Get(this.form.controls.documentNumber.value).subscribe(res => {
               this.dataSource1 = new MatTableDataSource<any>(res.storeNumbers);
             }))
          }, err => {
            this.cs.commonerror(err);
            this.spin.hide();
          }));
        }
        if (this.form.controls.referenceField2.value == "Work Order") {
          this.sub.add(this.workOrderService.Getall().subscribe((ress: any[]) => {
            ress.sort((a, b) => (a.createdOn > b.createdOn) ? -1 : 1);
            ress.forEach((x: any) => {
                this.copyFromList.push({
                  value: x.workOrderId,
                  label: x.workOrderId
                })
            })
            this.form.controls.documentNumber.patchValue(res.documentNumber)
          }, err => {
            this.cs.commonerror(err);
            this.spin.hide();
          }));
        }
        this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
        this.form.controls.invoiceDate.patchValue(this.cs.dateapi1(this.form.controls.invoiceDate.value));
        this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
        this.form.controls.documentStartDate.patchValue(this.cs.dateapi1(this.form.controls.documentStartDate.value));
        this.form.controls.documentEndDate.patchValue(this.cs.dateapi1(this.form.controls.documentEndDate.value));
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
    
    this.form.controls.invoiceDate.patchValue(this.cs.day_callapiSearch(this.form.controls.invoiceDate.value));
    this.form.controls.documentStartDate.patchValue(this.cs.day_callapiSearch(this.form.controls.documentStartDate.value));
    this.form.controls.documentEndDate.patchValue(this.cs.day_callapiSearch(this.form.controls.documentEndDate.value));
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
        this.toastr.success(res.invoiceNumber + " Saved Successfully!", "Notification", {
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
    // if (x == "Inquiry") {
    //   this.sub.add(this.inquiryService.Getall().subscribe((res: any[]) => {
    //     res.forEach((x: any) => this.copyFromList.push({
    //       value: x.enquiryId,
    //       label: x.enquiryId
    //     }))
    //   }, err => {
    //     this.cs.commonerror(err);
    //     this.spin.hide();
    //   }));
    // }
    if (x == "Quote") {
      this.sub.add(this.quoteService.Getall().subscribe((res: any[]) => {
        res.sort((a, b) => (a.createdOn > b.createdOn) ? -1 : 1);
        res.forEach((x: any) => {
          if (x.status == "Open") {
            this.copyFromList.push({
              value: x.quoteId,
              label: x.quoteId
            })
          }
        })
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }
    if (x == "Agreement") {
      this.sub.add(this.agreementService.search({
        customerName: this.form.controls.customerId.value
      }).subscribe((res: any[]) => {
        res.sort((a, b) => (a.createdOn > b.createdOn) ? -1 : 1);
        res.forEach((x: any) => {
          if (x.status != "Closed") {
            this.copyFromList.push({
              value: x.agreementNumber,
              label: x.agreementNumber
            })
          }
        })

        if(this.data.pageflow == "Copy From Agreement"){
          this.form.controls.documentNumber.patchValue(this.data.agreementNumber);
        }
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }
    if (x == "Work Order") {
      this.sub.add(this.workOrderService.search({
        customerId: [this.form.controls.customerId.value]
      }).subscribe((res: any[]) => {
        res.sort((a, b) => (a.createdOn > b.createdOn) ? -1 : 1);
        res.forEach((x: any) => {
          if (x.status == "Completed" || x.status == "Assigned") {
            this.copyFromList.push({
              value: x.workOrderId,
              label: x.workOrderId
            })
          }
        })
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }
  }

  copyFrom() {
    if (this.form.controls.copyFrom.value == "Inquiry") {
      this.sub.add(this.inquiryService.Get(this.form.controls.copyFromResult.value).subscribe(res => {
          this.form.controls.documentNumber.patchValue(res.enquiryId)
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
          this.form.controls.documentNumber.patchValue(res.quoteId)
          this.spin.hide();
        },
        err => {
          this.cs.commonerror(err);
          this.spin.hide();
        }
      ));
    }
    if (this.form.controls.copyFrom.value == "Agreement") {
      this.sub.add(this.agreementService.Get(this.form.controls.copyFromResult.value).subscribe(res => {
          this.form.controls.documentNumber.patchValue(res.agreementNumber)
          this.spin.hide();
        },
        err => {
          this.cs.commonerror(err);
          this.spin.hide();
        }
      ));
    }

  }

  copyTo() {
    if (this.form.controls.copyFrom.value == "Payment") {
      this.dialogRef.close();
      const dialogRef = this.dialog.open(PaymentNewComponent, {
        disableClose: true,
        //  width: '55%',
        maxWidth: '80%',
        data: {
          pageflow: 'Copy From Invoice',
          customerId: this.form.controls.customerId.value,
          startDate: this.form.controls.documentStartDate.value,
          endDate: this.form.controls.documentEndDate.value,
          totalAfterDiscount: this.form.controls.totalAfterDiscount.value,
          invoiceNumber: this.form.controls.invoiceNumber.value,
          currency: this.form.controls.invoiceCurrency.value,
          sbu: this.form.controls.sbu.value,
        }
      });

      dialogRef.afterClosed().subscribe(result => {

        this.router.navigate(["/main/operation/payment"]);
      });
    }
  }

  getTotal(){    
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + Number(element.itemServiceTotal != null ? element.itemServiceTotal : 0);
    })
    return (Math.round(total * 100) / 100);
  }
  getQty(){    
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + Number(element.itemServiceQuantity != null ? element.itemServiceQuantity : 0);
    })
    return (Math.round(total * 100) / 100);
  }

  
  getTotalRent(){    
    let total = 0;
    this.dataSource1.data.forEach(element => {
      total = total + Number(element.rent != null ? parseInt(element.rent) : 0);
    })
    return (Math.round(total * 100) / 100);
  }

  rentCalculation(){
    this.service.rentCalculation({
      endDate: this.form.controls.documentEndDate.value,
      period: this.form.controls.referenceField5.value,
      rent: this.form.controls.monthlyRent.value,
      startDate:this.form.controls.documentStartDate.value,
    }).subscribe(res => {
      this.form.controls.invoiceAmount.patchValue(res.totalRent)
    })
  }
}
