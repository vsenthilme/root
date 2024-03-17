import {
  Component,
  OnInit
} from '@angular/core';
import {
  FormBuilder,
  FormControl,
  Validators
} from '@angular/forms';
import {
  ActivatedRoute,
  Router
} from '@angular/router';
import {
  NgxSpinnerService
} from 'ngx-spinner';
import {
  Subscription
} from 'rxjs';
import {
  CommonService
} from 'src/app/common-service/common-service.service';
import {
  MatterExpensesService
} from '../matter-expenses.service';
import {
  defaultStyle
} from "../../../../../config/customStylesNew";
import pdfMake from "pdfmake/build/pdfmake";
import {
  logo
} from "../../../../../../assets/font/logo.js"
import pdfFontsNew from "../../../../../../assets/font/vfs_fonts.js";
import {
  fonts1
} from "../../../../..//config/pdfFontsNew"
import {
  ToastrService
} from 'ngx-toastr';
import {
  ClientGeneralService
} from 'src/app/main-module/client/client-general/client-general.service';
import {
  AuthService
} from 'src/app/core/core';
import {
  DecimalPipe,
  Location
} from "@angular/common";
import {
  DatePipe
} from '@angular/common';
import {
  EmailComponent
} from '../email/email.component';
import {
  MatDialog
} from '@angular/material/dialog';
import { CommonApiService } from 'src/app/common-service/common-api.service';
pdfMake.fonts = fonts1;
pdfMake.vfs = pdfFontsNew.pdfMake.vfs;


@Component({
  selector: 'app-expenses-cheque',
  templateUrl: './expenses-cheque.component.html',
  styleUrls: ['./expenses-cheque.component.scss']
})
export class ExpensesChequeComponent implements OnInit {

  js: any = {};
  constructor(private fb: FormBuilder, private spin: NgxSpinnerService, private matter: MatterExpensesService, private route: ActivatedRoute,
    private router: Router, public dialog: MatDialog,   private cas: CommonApiService,
    private cs: CommonService, private toastr: ToastrService, private auth: AuthService, private location: Location, private decimalPipe: DecimalPipe,
    private service: MatterExpensesService, private clientService: ClientGeneralService, private datePipe: DatePipe) {}

  public mask = [/\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/]

  public mask1 = [/\d/, /\d/, /\d/, /\d/,  ' ', /\d/, /\d/, /\d/, /\d/,  ' ', /\d/, /\d/, /\d/, /\d/,  ' ', /\d/, /\d/, /\d/, /\d/]


  ngOnInit(): void {

    let code = this.route.snapshot.params.code;

    this.js = this.cs.decrypt(code);
    console.log(this.js);
    this.form.patchValue(this.js.data, {
      emitEvent: false
    });

    this.form.controls.requestDate.patchValue(this.form.controls.referenceField2.value);
    this.sub.add(this.clientService.Get(this.js.data.clientId).subscribe(res => {
      this.form.controls.clientName.patchValue(res.firstNameLastName);
      this.form.controls.matterDescription.patchValue(this.js.description);

      this.form.controls.billableToClient.patchValue('Billable');
      this.form.controls.userName.patchValue(this.auth.userfullName);
      this.form.controls.checkRequestStatus.patchValue(1);
      this.form.controls.clientName.disable();
      this.form.controls.matterNumber.disable();
      this.form.controls.matterDescription.disable();
      this.form.controls.userName.disable();
    }));

    this.service.Get(this.js.data.matterExpenseId).subscribe(getReult => {
      this.form.controls.corporateClient.patchValue(getReult.corporateClient);
    })
this.getAllDropDown();

  }
  sub = new Subscription();
  form = this.fb.group({
    billType: [],
    billableToClient: ['Billable', ],
    cardNotAccepted: [,Validators.required],
    caseCategoryId: [],
    caseInformationNo: [],
    caseSubCategoryId: [],
    classId: [],
    clientId: [],
    costPerItem: [],
    createdBy: [],
    createdOn: [],
    deletionIndicator: [],
    expenseAccountNumber: [],
    expenseAmount: [, Validators.required],
    expenseCode: [],
    expenseDescription: [],
    expenseType: [],
    languageId: [],
    matterExpenseId: [],
    matterNumber: [],
    matterDescription: [],
    numberofItems: [],
    payableTo: [],
    paymentMode: [, Validators.required],
    qbDepartment: [],
    rateUnit: [],
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
    corporateClient:[],
    isThisCorporateClient:[],
    hasTheClientPaid:[,Validators.required],
    requiredDate: [, Validators.required],
    screatedOn: [],
    doc: [],
    sreferenceField2: [],
    statusId: [],
    updatedBy: [],
    requestDate: [],
    updatedOn: [],
    userName: [this.auth.userfullName, ],
    vendorFax: [],
    checkRequestStatus: [],
    vendorMailingAddress: [],
    vendorNotes: [],
    vendorPhone: [],
    clientName: [],
    writeOff: [],
    physicalCard: [,Validators.required],


    nameOnCardOrCheck: [],
    typeOfCreditCard: [],
    creditCard: [],
    securityCode: [],
    expirationDate: [],
    checkRequestCreatedBy:[],
    checkRequestCreatedOn:[],
  });
  submitted = false;
  data: any[] = [];
  doc: any[] = [];



  toEmailId: any;

  sendEmail() {
    const dialogRef = this.dialog.open(EmailComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: {
        top: '6.5%'
      },
      data: this.form.getRawValue(),
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.toEmailId = result;
        this.form.controls.checkRequestStatus.patchValue(2);
        this.submitForm(result);
      }
    });
  }

  submit() {
   if(this.form.controls.hasTheClientPaid.value == "No"){
    if(this.form.controls.isThisCorporateClient.value == null || this.form.controls.isThisCorporateClient.value == ""){
    this.toastr.error(
      "Please Fill all the Details to Continue",
      "Notification", {
      timeOut: 2000,
      progressBar: false,
    }
    );
  }
  if(this.form.controls.isThisCorporateClient.value == "No"){
    if (this.js.pageflow == 'Approve') {
      this.sendEmail();
    } else {
      this.submitForm('accounting@montyramirezlaw.com');  //checkrequest@montyramirezlaw.com //accounting@montyramirezlaw.com
    }
  }
  if(this.form.controls.isThisCorporateClient.value=="Yes"){
    if(this.form.controls.corporateClient.value == null){
    this.toastr.error(
      "Please Fill all the Details to Continue",
      "Notification", {
      timeOut: 2000,
      progressBar: false,
    }
    );
  }
  else{
  if (this.js.pageflow == 'Approve') {
    this.sendEmail();
  } else {
    this.submitForm('accounting@montyramirezlaw.com');  //checkrequest@montyramirezlaw.com //accounting@montyramirezlaw.com
  }
}
  }
 
    this.cs.notifyOther(true);
    return;
   }
   else{
    if (this.js.pageflow == 'Approve') {
      this.sendEmail();
    } else {
      this.submitForm('accounting@montyramirezlaw.com');  //checkrequest@montyramirezlaw.com //accounting@montyramirezlaw.com
    }
  }
 
  }
  originatingTimeList:any[]=[];
  legalAssistantList: any[] = [];
  ParalegalList: any[] = [];
partnerIdList:any[]=[];
multipartnerList:any[]=[];
corporationList:any[]=[];
multiMatterList:any[]=[];
multiclientList:any[]=[];
clientList:any[]=[];
filtersclient:any[]=[];
multiselectclientList:any[]=[];
getAllDropDown() {
  this.spin.show();
  this.sub.add(this.clientService.GetClientdetails().subscribe(res => {
    this.clientList = res;
    this.filtersclient = this.clientList.filter((element: {
      clientCategoryId: string;
    }) => {
      return element.clientCategoryId == '1';
    });
    this.filtersclient.forEach((x: {
      clientId: string;firstNameLastName: string;
    }) => this.multiclientList.push({
      value:  x.clientId + '-' + x.firstNameLastName,
      label: x.clientId + '-' + x.firstNameLastName
    }))
    this.multiselectclientList = this.multiclientList;

    this.spin.hide();
  },
  err => {
    this.cs.commonerror(err);
    this.spin.hide();
  }));
}

  submitForm(toEmailId) {
    this.spin.show();
    if (this.form.invalid) {
      this.toastr.error(
        "Please fill the required fields to continue",
        "Notification", {
          timeOut: 2000,
          progressBar: false,
        }
      );

      this.cs.notifyOther(true);
      this.spin.hide();
      return;
    }
    if(this.js.pageflow != 'Approve'){
      this.form.controls.checkRequestCreatedBy.patchValue(this.auth.userID);
    }
    this.form.controls.checkRequestCreatedOn.patchValue(new Date())
    this.sub.add(this.service.Update(this.form.getRawValue(), this.js.data.matterExpenseId).subscribe(res => {
      this.toastr.success(res.expenseCode + " updated successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      //   this.router.navigate(['/main/matters/case-management/expenses/' + this.route.snapshot.params.code]);
      this.spin.hide();
      this.generatePdf(res, toEmailId);
    }, err => {

      this.cs.commonerror(err);
      this.spin.hide();

    }));
  }
  email = new FormControl('', [Validators.required, Validators.email]);
  getErrorMessage() {
    if (this.email.hasError('required')) {
      return ' Field should not be blank';
    }
    return this.email.hasError('email') ? 'Not a valid email' : '';
  }
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  back() {
    this.location.back();
  }

  MaskCharacter(str, mask, n = 1) {
    return [...str].reduce((acc, x, i) =>
        (i < str.length - n) ? acc + mask : acc + x, '');
}

  generatePdf(element: any, toEmailId) {
    var dd: any;

    let headerTable: any[] = [];
    //let date = this.cs.todayapiNew();
    headerTable.push([{
        image: logo.headerLogo,
        fit: [100, 100],
        bold: true,
        fontSize: 12,
        border: [false, false, false, false],
        margin: [10, 0, 0]
      },
      // { text: '', bold: true, fontSize: 12, border: [false, false, false, false] },
      //    { text: 'Monty & Ramirez LLP \n 150 W Parker Road, 3rd Floor Houston, TX 77076',  alignment: 'center', fontSize: 12, border: [false, false, false, false] },
      {
        text: 'Check & Credit Card Request Form',
        bold: true,
        alignment: "center",
        fontSize: 13,
        margin: [10, 20, 10, 0],
        border: [false, false, false, false],
        color: '#666362'
      },
      {
        text: 'ddd',
        bold: true,
        alignment: "center",
        fontSize: 10,
        margin: [10, 20, 10, 0],
        border: [false, false, false, false],
        color: '#666362'
      },

    ]);

    dd = {
      pageSize: "A4",
      pageOrientation: "portrait",
      pageMargins: [40, 95, 40, 60],
      defaultStyle,
      header(): any {
        return [{
          columns: [{
              stack: [{
                image: logo.headerLogo,
                fit: [100, 100]
              }]
            },
            {
              stack: [{
                text: 'Check & Credit Card Request Form',
                bold: true,
                alignment: "center",
                fontSize: 13,
                margin: [10, 20, 10, 0],
                border: [false, false, false, false],
                color: '#666362'
              }, ],
              width: 300
            },
            {
              stack: [{
                  text: 'Run Date: ', // + '' +  (new Date().getDate()) +'-'+ (new Date().getMonth() + 1) + '-' + new Date().getFullYear() + '_' + this.cs.timeFormat(new Date()),
                  alignment: 'left',
                  fontSize: 7,
                  color: '#666362'
                },

              ],
              width: 170
            }
          ],
          margin: [40, 20]
        }]
      },
      footer(currentPage: number, pageCount: number): any {
        return [{
            text: '150 W.Parker Road | Third Floor | Houston,Texas 770706 | (281) 493-5529 phone | (281) 493-5529 fax | www.montyramirezlaw.com',
            bold: false,
            border: [false, false, false, false],
            alignment: 'left',
            fontSize: 9,
            margin: [30, 10, 0, 10]
          },
          {
            text: 'Page ' + currentPage + ' of ' + pageCount,
            style: 'header',
            alignment: 'center',
            bold: false,
            fontSize: 6,
            margin: [10, 5, -5, 0]
          }

          // { image: res.requirement == "Packing & Moving" ? workOrderLogo1.ulogistics1 : workOrderLogo1.ustorage, width: 570,   bold: false, margin: [10, -80, 0, 0], fontSize: 12, border: [false, false, false, false] },
        ]
      },
      content: ['\n'],
      //   defaultStyle,

    };
    // let headerArray4: any[] = [];
    // headerArray4.push([
    //   { text: 'Check & Credit Card Request Form', bold: true, border: [false, false, false, false], alignment: 'left', fontSize:13, color:'#267FA0'},


    // ]);
    // dd.content.push(
    //   {
    //     table: {
    //       headerRows: 1,
    //       widths: ['*'],
    //       body: headerArray4
    //     },
    //     margin: [-8, -10, 2, 0]
    //   }
    // )


    let headerArray79: any[] = [];
    headerArray79.push([{
        text: 'Request Date:',
        bold: true,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: this.datePipe.transform(element.referenceField2, 'MM-dd-yyyy'),
        bold: false,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: '',
        bold: false,
        border: [false, false, false, false]
      },
      {
        text: 'Required Date:',
        bold: true,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2],
        alignment: "left"
      },
      {
        text: this.datePipe.transform(element.requiredDate, 'MM-dd-yyyy'),
        bold: false,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2],
        alignment: "left"
      },

    ]);



    dd.content.push({
      table: {
        headerRows: 1,
        widths: [80, 60, '*', 80, 60],
        body: headerArray79
      },
      margin: [-3, 5, 10, 0]
    })
    if(this.js.pageflow == 'Approve' && this.auth.userTypeId == 7){
    let headerArray97: any[] = [];
    headerArray97.push([{
        text: '',
        bold: false,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: 'Business Use Only:',
        bold: true,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: '',
        bold: true,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },



    ]);



    dd.content.push({
      table: {
        headerRows: 1,
        widths: [180, '*', '*'],
        body: headerArray97
      },
      margin: [-3, 5, 10, 0]
    })
    let headerArray94: any[] = [];
    headerArray94.push([{
        text: 'Name On Card/Check#: ',
        bold: true,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: element.nameOnCardOrCheck,
        bold: false,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },



    ]);
    headerArray94.push([{
        text: 'Type Of Credit Card',
        bold: true,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text:  element.typeOfCreditCard,
        bold: false,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },



    ]);
    headerArray94.push([{
        text: 'Credit Card',
        bold: true,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text:  element.creditCard,// this.MaskCharacter(element.creditCard, '#', 4)
        bold: false,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },



    ]);
    headerArray94.push([{
        text: 'Security Code #:',
        bold: true,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: element.securityCode,
        bold: false,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },



    ]);
    headerArray94.push([{
        text: 'Expiration Date:',
        bold: true,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: this.datePipe.transform(element.expirationDate, 'MM-dd-yyyy'),
        bold: false,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },



    ]);



    dd.content.push({
      table: {
        headerRows: 1,
        widths: ['*', '*'],
        body: headerArray94
      },
      margin: [-3, 5, 10, 0]
    })
  }
    let headerArray81: any[] = [];
    headerArray81.push([{
        text: 'Payment Method:',
        bold: true,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: element.paymentMode,
        bold: false,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: 'Amount:',
        bold: true,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: element.expenseAmount ? '$' + this.decimalPipe.transform(element.expenseAmount, "1.2-2") : '$ 0.00',
        bold: false,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [-27, 2, 2, 2]
      },
    ]);



    dd.content.push({
      table: {
        headerRows: 1,
        widths: [100, 90, 100, 100],
        body: headerArray81
      },
      margin: [-3, 5, 10, 0]
    })
    let headerArray82: any[] = [];
    headerArray82.push([{
        text: 'Cards Not Accepted:',
        bold: true,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: element.cardNotAccepted,
        bold: false,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },


    ]);



    dd.content.push({
      table: {
        headerRows: 1,
        widths: [100, 100],
        body: headerArray82
      },
      margin: [-3, 5, 10, 0]
    })

    let headerArray83: any[] = [];
    headerArray83.push([{
        text: 'Requesting Physical Card: ',
        bold: true,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: element.physicalCard,
        bold: false,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [-20, 2, 2, 2]
      },


    ]);



    dd.content.push({
      table: {
        headerRows: 1,
        widths: [160, 20],
        body: headerArray83
      },
      margin: [-3, 5, 10, 0]
    })
    let headerArray1001: any[] = [];
    headerArray1001.push([{
        text: 'Has the client paid? ',
        bold: true,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: element.hasTheClientPaid,
        bold: false,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [-20, 2, 2, 2]
      },


    ]);



    dd.content.push({
      table: {
        headerRows: 1,
        widths: [160, 20],
        body: headerArray1001
      },
      margin: [-3, 5, 10, 0]
    })
    if(element.hasTheClientPaid=="No"){
    let headerArray1002: any[] = [];
    headerArray1002.push([{
        text: 'Is this a Corporate Client?',
        bold: true,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: element.isThisCorporateClient,
        bold: false,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [-20, 2, 2, 2]
      },


    ]);



    dd.content.push({
      table: {
        headerRows: 1,
        widths: [160, 20],
        body: headerArray1002
      },
      margin: [-3, 5, 10, 0]
    })
  }
if(element.isThisCorporateClient == 'Yes' && element.hasTheClientPaid == "No"){
    let headerArray1003: any[] = [];
    headerArray1003.push([{
        text: 'Corporate Client',
        bold: true,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: element.corporateClient,
        bold: false,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [-20, 2, 2, 2]
      },


    ]);



    dd.content.push({
      table: {
        headerRows: 1,
        widths: [160,250],
        body: headerArray1003
      },
      margin: [-3, 5, 10, 0]
    })
  }
    let headerArray851: any[] = [];
    headerArray851.push([{
        text: '',
        bold: false,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: 'Vendor Information',
        bold: true,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: '',
        bold: true,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },



    ]);



    dd.content.push({
      table: {
        headerRows: 1,
        widths: [180, '*', '*'],
        body: headerArray851
      },
      margin: [-3, 5, 10, 0]
    })

    let headerArray84: any[] = [];
    headerArray84.push([{
        text: 'Payable To (Vendor Name)',
        bold: true,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: element.payableTo,
        bold: false,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },



    ]);
    headerArray84.push([{
        text: 'Vendor Mailing Address',
        bold: true,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: element.vendorMailingAddress,
        bold: false,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },



    ]);
    headerArray84.push([{
        text: 'Phone Number',
        bold: true,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: element.vendorPhone,
        bold: false,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },



    ]);
    headerArray84.push([{
        text: 'Fax',
        bold: true,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: element.vendorFax,
        bold: false,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },



    ]);
    headerArray84.push([{
      text: 'Description',
      bold: true,
      fontSize: 10,
      border: [true, true, true, true],
      borderColor: ['#808080', '#808080', '#808080', '#808080'],
      margin: [2, 2, 2, 2]
    },
    {
      text: element.expenseDescription,
      bold: false,
      fontSize: 10,
      border: [true, true, true, true],
      borderColor: ['#808080', '#808080', '#808080', '#808080'],
      margin: [2, 2, 2, 2]
    },



  ]);


    dd.content.push({
      table: {
        headerRows: 1,
        widths: ['*', '*'],
        body: headerArray84
      },
      margin: [-3, 5, 10, 0]
    })
    let headerArray85: any[] = [];
    headerArray85.push([{
        text: '',
        bold: false,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: 'Client Information',
        bold: true,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: '',
        bold: true,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },



    ]);



    dd.content.push({
      table: {
        headerRows: 1,
        widths: [180, '*', '*'],
        body: headerArray85
      },
      margin: [-3, 5, 10, 0]
    })
    let headerArray86: any[] = [];
    headerArray86.push([{
        text: 'Client Name',
        bold: true,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: this.form.controls.clientId.value + ' - ' + this.form.controls.clientName.value,
        bold: false,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },



    ]);
    headerArray86.push([{
        text: 'Case or Matter',
        bold: true,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: this.form.controls.matterNumber.value + ' - ' + this.form.controls.matterDescription.value,
        bold: false,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },



    ]);
    headerArray86.push([{
        text: 'Billable To Client',
        bold: true,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: element.billableToClient,
        bold: false,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },



    ]);




    dd.content.push({
      table: {
        headerRows: 1,
        widths: ['*', '*'],
        body: headerArray86
      },
      margin: [-3, 5, 10, 0]
    })

    let headerArray88: any[] = [];
    headerArray88.push([{
        text: '',
        bold: false,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: 'Must Be Completed By Employee',
        bold: true,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: '',
        bold: true,
        fontSize: 10,
        border: [false, false, false, false],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },



    ]);



    dd.content.push({
      table: {
        headerRows: 1,
        widths: [150, 200, 100],
        body: headerArray88
      },
      margin: [-3, 5, 10, 0]
    })
    let headerArray89: any[] = [];
    headerArray89.push([{
        text: 'Name:',
        bold: true,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: this.form.controls.userName.value,
        bold: false,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },



    ]);
    headerArray89.push([{
        text: 'QB Dept:',
        bold: true,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },
      {
        text: element.qbDepartment,
        bold: false,
        fontSize: 10,
        border: [true, true, true, true],
        borderColor: ['#808080', '#808080', '#808080', '#808080'],
        margin: [2, 2, 2, 2]
      },



    ]);





    dd.content.push({
      table: {
        headerRows: 1,
        widths: ['*', '*'],
        body: headerArray89
      },
      margin: [-3, 5, 10, 0]
    })

    dd.content.push(

      {
        unbreakable: true,
        stack: [
          '\n',
          {
            text: 'Notes',
            style: 'header',
            alignment: 'left',
            bold: true,
            fontSize: 10,
          },
          '\n',
          {
            text: element.vendorNotes,
            style: 'header',
            alignment: 'left',
            fontSize: 10,
          },
        ]
      }

    )




    // dd.content.push(
    //   {
    //     stack: [{
    //       text: '',
    //       alignment: 'center',
    //       fontSize: 12,
    //       lineHeight: 1.3,
    //       bold: true,
    //       pageBreak: 'before',
    //     }]
    //   })
    // pdfMake.createPdf(dd).download('Cheque No : ' +  element.expenseCode);
    //pdfMake.createPdf(dd).open();
    const pdfDocGenerator = pdfMake.createPdf(dd);
    pdfDocGenerator.getBlob((blob) => {
      var file = new File([blob], this.form.controls.matterNumber.value + "_expense_check" + ".pdf"); //  + (new Date().getDate()) +'-'+ (new Date().getMonth() + 1) + '-' + new Date().getFullYear() + '_' + this.cs.timeFormat(new Date()) +  
      if (file) {
        this.spin.show();
        this.service.uploadfile(file, 'check/' + this.js.data.clientId + '/' + this.js.data.matterNumber, toEmailId, this.form.controls.checkRequestCreatedBy.value).subscribe((resp: any) => {
          this.service.Update({
            documentName: resp.file,
            statusId: this.js.data.statusId
          }, this.js.data.matterExpenseId).subscribe(res => {
            this.toastr.success("Email sent successfully!", "Notification", {
              timeOut: 2000,
              progressBar: false,
            });
          this.location.back();
          this.spin.hide();
          })
        }, err => {
          this.cs.commonerror(err);
          //this.spin.hide();
        });
      }
    });

  }
}
