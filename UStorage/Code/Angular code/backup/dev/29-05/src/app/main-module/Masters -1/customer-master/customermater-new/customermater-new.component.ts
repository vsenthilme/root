import {
  SelectionModel
} from "@angular/cdk/collections";
import {
  Component,
  OnInit
} from "@angular/core";
import {
  FormBuilder,
  FormControl,
  Validators
} from "@angular/forms";
import {
  MatTableDataSource
} from "@angular/material/table";
import {
  ActivatedRoute,
  Router
} from "@angular/router";
import {
  NgxSpinnerService
} from "ngx-spinner";
import {
  ToastrService
} from "ngx-toastr";
import {
  Location
} from '@angular/common';
import {
  Subscription
} from "rxjs";
import {
  CommonService
} from "src/app/common-service/common-service.service";
import {
  AuthService
} from "src/app/core/core";
import {
  CustomerService
} from "../customer.service";
import {
  CommonApiService
} from "src/app/common-service/common-api.service";
import {
  PaymentService
} from "src/app/main-module/operation/operation/payment/payment.service";
import {
  InvoiceService
} from "src/app/main-module/operation/operation/invoice/invoice.service";
import {
  HttpErrorResponse,
  HttpResponse
} from "@angular/common/http";
import {
  DomSanitizer
} from "@angular/platform-browser";
import { DeleteComponent } from "src/app/common-field/delete/delete.component";
import { MatDialog } from "@angular/material/dialog";
import { DownloadComponent } from "./download/download.component";


export interface PeriodicElement {
  usercode: string;
  name: string;
  admin: string;
  role: string;
  userid: string;
  password: string;
  status: string;
  email: string;
  phoneno: string;
  Agreement: string;
  created: string;
  processed: string;
  leadtime: string;
}

export interface PeriodicElement2 {
  usercode: string;
  name: string;
  admin: string;
  role: string;
  userid: string;
  password: string;
  status: string;
  email: string;
  phoneno: string;
  Agreement: string;
  created: string;
  processed: string;
  leadtime: string;
}
export interface PeriodicElement1 {
  usercode: string;
  name: string;
  admin: string;
  role: string;
  userid: string;
  password: string;
  status: string;
  email: string;
  phoneno: string;
  Agreement: string;
  created: string;
  processed: string;
  leadtime: string;
}

const ELEMENT_DATA: PeriodicElement[] = [{
  usercode: "test",
  name: 'test',
  admin: 'test',
  role: 'test',
  userid: 'test',
  password: 'test',
  status: 'test',
  email: 'test',
  phoneno: 'test',
  Agreement: 'test',
  created: 'test',
  processed: 'test',
  leadtime: 'test'
}, ];

const ELEMENT_DATA1: PeriodicElement1[] = [{
  usercode: "test",
  name: 'test',
  admin: 'test',
  role: 'test',
  userid: 'test',
  password: 'test',
  status: 'test',
  email: 'test',
  phoneno: 'test',
  Agreement: 'test',
  created: 'test',
  processed: 'test',
  leadtime: 'test'
}, ];
const ELEMENT_DATA2: PeriodicElement2[] = [{
  usercode: "test",
  name: 'test',
  admin: 'test',
  role: 'test',
  userid: 'test',
  password: 'test',
  status: 'test',
  email: 'test',
  phoneno: 'test',
  Agreement: 'test',
  created: 'test',
  processed: 'test',
  leadtime: 'test'
}, ];
@Component({
  selector: 'app-customermater-new',
  templateUrl: './customermater-new.component.html',
  styleUrls: ['./customermater-new.component.scss']
})
export class CustomermaterNewComponent implements OnInit {


  form = this.fb.group({
    address: [, Validators.required],
    authorizedCivilID: [],
    authorizedPerson: [],
    balanceToBePaid: [],
    billedAmount: [],
    billedAmountTillDate: [],
    civilId: [],
    createdBy: [],
    createdOn: [],
    currency: [],
    customerCode: [],
    customerGroup: [],
    customerName: [, Validators.required],
    deletionIndicator: [],
    email: [, ],
    faxNumber: [],
    isActive: [],
    mobileNumber: [, Validators.required],
    nationality: ['Kuwaiti'],
    paidAmountTillDate: [],
    paidDate: [],
    phoneNumber: [],
    preferedPaymentMode: [],
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
    seviceRendered: [],
    status: [],
    sbu: ['2000001', Validators.required],
    type: ['CUSTOMER', Validators.required],
    updatedBy: [],
    updatedOn: [],
    voucherNumber: [],
  });

  //screenid: 1043 | undefined;
  isShowDiv = false;
  public icon = 'expand_more';
  showFloatingButtons: any;
  toggle = true;
  pageflowupdated: any;
  civilIdResult: any;
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
  constructor(private fb: FormBuilder,
    private auth: AuthService,
    public toastr: ToastrService,
    // private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private location: Location,
    private sanitizer: DomSanitizer,
    private route: ActivatedRoute, private router: Router,
    private cs: CommonService,
    public dialog: MatDialog,
    private service: CustomerService,
    private paymentService: PaymentService,
    private invoiceService: InvoiceService,
    private cas: CommonApiService, ) {}

  displayedColumns: string[] = ['fileDescription', 'uploadedBy', 'uploadedOn', 'Actions'];
  dataSource = new MatTableDataSource < any > (ELEMENT_DATA);

  displayedColumns1: string[] = ['select', 'usercode', 'name', 'admin', 'role', 'userid', 'password', 'status', 'email'];
  dataSource1 = new MatTableDataSource < PeriodicElement1 > (ELEMENT_DATA1);
  selection = new SelectionModel < PeriodicElement1 > (true, []);


  displayedColumns2: string[] = ['documentType', 'documentDate', 'documentNumber', 'total', 'status', 'notes'];
  dataSource2 = new MatTableDataSource < any > (ELEMENT_DATA2);

  /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    if (this.isAllSelected()) {
      this.selection.clear();
      return;
    }

    this.selection.select(...this.dataSource.data);
  }
  public mask = [/\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/]
  toggleAllRows() {
    if (this.isAllSelected()) {
      this.selection.clear();
      return;
    }

    this.selection.select(...this.dataSource.data);
  }

  /** The label for the checkbox on the passed row */
  checkboxLabel(row ? : any): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.no + 1}`;
  }


  clearselection(row: any) {
    this.selection.clear();
    this.selection.toggle(row);
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

  js: any = {}
  ngOnInit(): void {
    this.dropdownlist();
    let code = this.route.snapshot.params.code;
    this.js = this.cs.decrypt(code);
    console.log(this.js)
    if (this.js.pageflow != 'New') {
      this.form.controls.customerCode.disable();
      if (this.js.pageflow == 'Display')
        this.form.disable();
      this.fill();
    }
    if (this.js.pageflow == "Enquiry Customer") {
      this.form.controls.customerName.patchValue(this.js.name);
      this.form.controls.email.patchValue(this.js.email);
      this.form.controls.mobileNumber.patchValue(this.js.phoneNumber);
    }
  }
  sub = new Subscription();
  submitted = false;

  nationalityList: any[] = [];
  paymentModeList: any[] = [];
  customerTypeList: any[] = [];
  serviceRenderedList: any[] = [];
  statusList: any[] = [];
  workOrderSbuList: any[] = [];
  dropdownlist() {
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.nationality.url,
      this.cas.dropdownlist.setup.paymentmode.url,
      this.cas.dropdownlist.setup.customertype.url,
      this.cas.dropdownlist.setup.sbu.url,
      // this.cas.dropdownlist.setup.servicerendered.url,
      //  this.cas.dropdownlist.setup.accountstatus.url,
    ]).subscribe((results) => {
      this.nationalityList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.nationality.key);
      this.paymentModeList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.paymentmode.key);
      this.customerTypeList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.customertype.key);
      //  this.serviceRenderedList = this.cas.foreachlist(results[3], this.cas.dropdownlist.setup.servicerendered.key);
      this.workOrderSbuList = this.cas.foreachlist2(results[3], this.cas.dropdownlist.setup.sbu.key);
      //  this.statusList = this.cas.foreachlist1(results[4], this.cas.dropdownlist.setup.accountstatus.key);
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }

  customerDetails: any[] = [];

  fill() {
    this.customerDetails = [];
    this.spin.show();
    this.sub.add(this.service.Get(this.js.code).subscribe(res => {
        this.form.patchValue(res, {
          emitEvent: false
        });
        // this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
        // this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
        this.spin.hide();

        this.sub.add(this.paymentService.search({
          customerName: this.form.controls.customerCode.value
        }).subscribe(res => {
          let paidAmount = 0
          if (res.length > 0) {
            res.forEach(element => {
              paidAmount = paidAmount + parseInt(element.voucherAmount);
            });

            this.form.controls.paidAmountTillDate.patchValue(paidAmount);
            res.sort((a, b) => (a.createdOn > b.createdOn) ? -1 : 1);
            this.form.controls.billedAmount.patchValue(res[0].voucherAmount);
            this.form.controls.voucherNumber.patchValue(res[0].voucherId);
            this.form.controls.preferedPaymentMode.patchValue(res[0].modeOfPayment);
            this.form.controls.paidDate.patchValue(res[0].createdOn);
          }

          this.sub.add(this.invoiceService.search({
            customerId: [this.form.controls.customerCode.value]
          }).subscribe(res => {
            let billedAmout = 0
            if (res.length > 0) {
              res.forEach(element => {
                billedAmout = billedAmout + parseInt(element.invoiceAmount);
              });
              this.form.controls.billedAmountTillDate.patchValue(billedAmout);
              let balanceAmount = billedAmout - paidAmount;
              this.form.controls.balanceToBePaid.patchValue(balanceAmount);

              if (this.form.controls.balanceToBePaid.value > 0) {

                this.form.controls.status.patchValue("Pending");
              }
            }
          }))
          this.sub.add(this.service.customerDetails({
            customerCode: this.form.controls.customerCode.value
          }).subscribe(res => {
            if (res) {
              if (res.inquiry) {
                res.inquiry.forEach(element => {
                  this.customerDetails.push(element)
                });
              }
              if (res.quote) {
                res.quote.forEach(element => {
                  this.customerDetails.push(element)
                });
              }
              if (res.agreement) {
                res.agreement.forEach(element => {
                  this.customerDetails.push(element)
                });
              }
              if (res.invoice) {
                res.invoice.forEach(element => {
                  this.customerDetails.push(element)
                });
              }
              if (res.payment) {
                res.payment.forEach(element => {
                  this.customerDetails.push(element)
                });
              }
              if (res.workorder) {
                res.workorder.forEach(element => {
                  this.customerDetails.push(element)
                });
              }
            }
            this.dataSource2 = new MatTableDataSource < any > (this.customerDetails);
          }))

          this.service.searchDocumentStorage({customerName : [this.form.controls.customerCode.value]}).subscribe(storageGetAll => {
              
            this.dataSource = new MatTableDataSource <any> (storageGetAll);
              })

        }));

      },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }
    ));
  }

  back() {
    this.location.back();

  }

  submit() {
    console.log('repeat3')
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

    // if (!this.form.controls.civilId.value && this.form.controls.type.value == "CUSTOMER") {
    //   this.toastr.error(
    //     "Please fill the civil Id number to continue",
    //     "Notification", {
    //       timeOut: 2000,
    //       progressBar: false,
    //     }
    //   );

    //   this.cs.notifyOther(true);
    //   return;
    // }

    // this.sub.add(this.service.search({
    //   civilId: [this.form.controls.civilId.value]
    // }).subscribe(res => {
    //   this.civilIdResult = res
    //   this.spin.hide();

    // }, err => {

    //   this.cs.commonerror(err);
    //   this.spin.hide();

    // }));

    // if(this.civilIdResult.length > 1 && this.js.pageflow != 'New'){
    //   this.toastr.error(
    //     "Civil ID already exists",
    //     "Notification", {
    //       timeOut: 2000,
    //       progressBar: false,
    //     }
    //   );

    //   this.cs.notifyOther(true);
    //   return;
    // }
    // else{
    //   if(this.civilIdResult.length > 0){
    //     this.toastr.error(
    //       "Civil ID already exists",
    //       "Notification", {
    //         timeOut: 2000,
    //         progressBar: false,
    //       }
    //     );

    //     this.cs.notifyOther(true);
    //     return;
    //   }
    // }


    if (this.js.code) {
      this.sub.add(this.service.Update(this.form.getRawValue(), this.js.code).subscribe(res => {
        this.toastr.success(this.js.code + " updated successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        if (this.js.pageflow == "Enquiry Customer") {
          this.router.navigate(['/main/crm/enquiry']);
        } else {
          this.router.navigate(['/main/customermasters/customerlist']);
        }
        this.spin.hide();

      }, err => {

        this.cs.commonerror(err);
        this.spin.hide();

      }));
    } else {
      this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
        console.log('repeat1')
        this.toastr.success(res.customerCode + " Saved Successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        //   setTimeout(() => {
        //     window.location.reload();
        // }, 1000);
        this.router.navigate(['/main/customermasters/customerlist']);


        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();

      }));
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

  file: File;
  onFilechange(event: any) {
    console.log(event)
    this.file = event.target.files[0]
  }

  upload() {
    if (this.file) {
      console.log(this.file)
      this.spin.show();
      this.service.uploadfile(this.file, 'document').subscribe((resp: any) => {
        this.toastr.success("Document uploaded successfully.", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });

        this.service.documentStorage({
          fileDescription: resp.file,
          customerName: this.form.controls.customerCode.value
        }).subscribe(res => {
          if (res) {
            this.service.searchDocumentStorage({customerName : [this.form.controls.customerCode.value]}).subscribe(storageGetAll => {
              
          this.dataSource = new MatTableDataSource <any> (storageGetAll);
            })
          }
        })
      })
      this.spin.hide();
    } else {
      this.toastr.warning("Please select a file first.", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
    }


  }
  docurl: any;
  fileUrldownload: any;
  async download(element) {
    const blob = await this.service.getfile1(window.encodeURIComponent(element.fileDescription), 'document')
      .catch((err: HttpErrorResponse) => {

        this.cs.commonerror(err);
     //   this.fileName = "";
      });
    this.spin.hide();

    if (blob) {
      const blobOb = new Blob([blob], {
        type: "application/vnd.openxmlformats-officedocument.wordprocessingml.document",


      });
      this.fileUrldownload = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blobOb));
      // http://remote.url.tld/path/to/document.doc&embedded=true
      this.docurl = window.URL.createObjectURL(blob);

console.log(this.fileUrldownload)
      // const url = window.URL.createObjectURL(blob); this.docurl = url + '.docx';
      this.openDialog(element)
    }
  }

  deleterecord(id: any) {
    this.spin.show();
    this.sub.add(this.service.DeleteDocumentStorage(id).subscribe((res) => {
      this.toastr.success(id + " Deleted successfully.","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.spin.hide();
      this.service.searchDocumentStorage({customerName : [this.form.controls.customerCode.value]}).subscribe(storageGetAll => {
              
        this.dataSource = new MatTableDataSource <any> (storageGetAll);
          })
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  deleteDialog(element) {
    const dialogRef = this.dialog.open(DeleteComponent, {
      disableClose: true,
      width: '40%',
      maxWidth: '80%',
      position: { top: '9%', },
    data: element.documentNumber,
    });
  
    dialogRef.afterClosed().subscribe(result => {
  
      if (result) {
        this.deleterecord(element.documentNumber);
  
      }
    });
  }

  download1(element){
    let download = element.fileDescription; let href = "fileUrldownload"
  }

  openDialog(element): void {
    // if (this.selection.selected.length === 0) {
    //   this.toastr.warning("Kindly select any Row", "Notification", {
    //     timeOut: 2000,
    //     progressBar: false,
    //   });
    //   return;
    // }
    const dialogRef = this.dialog.open(DownloadComponent, {
      disableClose: true,
      width: '40%',
      maxWidth: '80%',
      position: { top: '9%', },
      data: { url: this.fileUrldownload, fileName: element.fileDescription}
    });

    dialogRef.afterClosed().subscribe(result => {
     // this.search();
    });
  }

}
