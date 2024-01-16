import {
  SelectionModel
} from '@angular/cdk/collections';
import {
  DatePipe
} from '@angular/common';
import {
  Component,
  Inject,
  OnInit,
  ViewChild
} from '@angular/core';
import {
  AbstractControl,
  FormArray,
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
import { Router } from '@angular/router';
import {
  NgxSpinnerService
} from 'ngx-spinner';
import {
  ToastrService
} from 'ngx-toastr';
import {
  BehaviorSubject,
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
  ConsumablesService
} from 'src/app/main-module/Masters -1/material-master/consumables/consumables.service';
import {
  FlRentService
} from 'src/app/main-module/Masters -1/material-master/fl-rent/fl-rent.service';
import {
  HandlingChargesService
} from 'src/app/main-module/Masters -1/material-master/handlingcharges/handling-charges.service';
import {
  TripsService
} from 'src/app/main-module/Masters -1/material-master/trips/trips.service';
import { PaymentNewComponent } from '../../payment/payment-new/payment-new.component';
import {
  WorkorderService
} from '../workorder.service';


export interface PeriodicElement {
  usercode: string,
    name: string,
    admin: string,
    role: string,
    userid: string,
    password: string,
    status: string,
    email: string,
}

const ELEMENT_DATA: PeriodicElement[] = [{
    usercode: "Moving",
    name: 'War',
    admin: 'test',
    role: 'test',
    userid: 'test',
    password: 'test',
    status: 'test',
    email: 'test'
  },
  {
    usercode: "Wrapping",
    name: 'War',
    admin: 'test',
    role: 'test',
    userid: 'test',
    password: 'test',
    status: 'test',
    email: 'test'
  },
  {
    usercode: "Dismantling and Assembling",
    name: 'War',
    admin: 'test',
    role: 'test',
    userid: 'test',
    password: 'test',
    status: 'test',
    email: 'test'
  },
  {
    usercode: "Box Size 45x45x45 cm(S) (with packing)",
    name: 'War',
    admin: 'test',
    role: 'test',
    userid: 'test',
    password: 'test',
    status: 'test',
    email: 'test'
  },
  {
    usercode: "Box Size 45x45x45 cm(S) (without packing)",
    name: 'War',
    admin: 'test',
    role: 'test',
    userid: 'test',
    password: 'test',
    status: 'test',
    email: 'test'
  },
  {
    usercode: "Box Size 45x45x45 cm(L) (with packing)",
    name: 'War',
    admin: 'test',
    role: 'test',
    userid: 'test',
    password: 'test',
    status: 'test',
    email: 'test'
  },
  {
    usercode: "Box Size 45x45x45 cm(L) (without packing)",
    name: 'War',
    admin: 'test',
    role: 'test',
    userid: 'test',
    password: 'test',
    status: 'test',
    email: 'test'
  },
  {
    usercode: "Corrugated Roll",
    name: 'War',
    admin: 'test',
    role: 'test',
    userid: 'test',
    password: 'test',
    status: 'test',
    email: 'test'
  },
  {
    usercode: "Bubble Wrap",
    name: 'War',
    admin: 'test',
    role: 'test',
    userid: 'test',
    password: 'test',
    status: 'test',
    email: 'test'
  },
  {
    usercode: "Nylon Roll",
    name: 'War',
    admin: 'test',
    role: 'test',
    userid: 'test',
    password: 'test',
    status: 'test',
    email: 'test'
  },
  {
    usercode: "Stretch Film",
    name: 'War',
    admin: 'test',
    role: 'test',
    userid: 'test',
    password: 'test',
    status: 'test',
    email: 'test'
  },
  {
    usercode: "Tape",
    name: 'War',
    admin: 'test',
    role: 'test',
    userid: 'test',
    password: 'test',
    status: 'test',
    email: 'test'
  },
  {
    usercode: "Other",
    name: 'War',
    admin: 'test',
    role: 'test',
    userid: 'test',
    password: 'test',
    status: 'test',
    email: 'test'
  },
];


@Component({
  selector: 'app-workorder-new',
  templateUrl: './workorder-new.component.html',
  styleUrls: ['./workorder-new.component.scss']
})
export class WorkorderNewComponent implements OnInit {



  ItemServices = this.fb.group({
    createdBy: [],
    createdOn: [],
    deletionIndicator: [],
    itemName: [],
    itemServiceName: [],
    itemServiceQuantity: [],
    itemServiceTotal: [],
    itemServiceUnitPrice: [],
    updatedBy: [],
    updatedOn: [],
    workOrderId: [],
    workOrderSbu:[],
  });

  rows: FormArray = this.fb.array([this.ItemServices]);

  form = this.fb.group({
    codeId: [],
    created: [],
    createdBy: [, Validators.required],
    createdOn: [],
    copyFrom: [],
    createdOn1: [],
    customerId: [, Validators.required],
    deletionIndicator: [],
    endDate: [],
    endTime: [],
    fromAddress: [],
    itemServices: this.rows,
    jobCard: [],
    jobCardType: [],
    leadTime: [],
    plannedHours: [],
    woProcessedBy: [, Validators.required],
    processedTime: [],
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
    remarks: [],
    startDate: [],
    startTime: [],
    status: ['Planned', ],
    updatedBy: [],
    updatedOn: [],
    workOrderDate:  [, Validators.required],
    workOrderId: [],
    workOrderNumber: [],
    workOrderSbu: ['2000002', Validators.required],
    toAddress: [],
  });

  itemNameList: any[] = [];
  constructor(
    public dialogRef: MatDialogRef < any > ,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService,
    private spin: NgxSpinnerService, public dialog: MatDialog,
    private auth: AuthService,
    private fb: FormBuilder,
    public cs: CommonService,
    private router: Router,
    private service: WorkorderService,
    private consumablesService: ConsumablesService,
    private handlingsService: HandlingChargesService,
    private flService: FlRentService,
    private tripService: TripsService,
    private cas: CommonApiService,
    public datePipe: DatePipe, ) {

    this.itemNameList = [{
        label: 'Consumables',
        value: 'Consumables'
      },
      {
        label: 'Handling Charges',
        value: 'Handling Charges'
      },
      {
        label: 'FL Rent',
        value: 'FL Rent'
      },
      {
        label: 'Trips',
        value: 'Trips'
      },
    ];
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

  sub = new Subscription();
  submitted = false;


  ngOnInit(): void {
    this.workOrderDropdownList();
    // this.addRow()
    this.dropdownlist();
    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOn.disable();
    if (this.data.pageflow != 'New' && this.data.pageflow != "Copy From Quote") {

      this.form.controls.workOrderId.disable();
      if (this.data.pageflow == 'Display')
        this.form.disable();
      this.fill();
    }

    this.form.controls.createdOn1.valueChanges.subscribe(x => {
      let leadTime = this.form.controls.endDate.value ? this.cs.getDataDiff(this.form.controls.createdOn1.value, this.form.controls.endDate.value) : '';
      this.form.controls.leadTime.patchValue(leadTime);
    })

    this.form.controls.endDate.valueChanges.subscribe(x => {
      let processedTime = this.form.controls.endDate.value ? this.cs.getDataDiff(this.form.controls.startDate.value, this.form.controls.endDate.value) : '';
      this.form.controls.processedTime.patchValue(processedTime);
    })
    //this.Qtycalculation()

    if (this.data.pageflow == "Copy From Quote") {
      this.form.controls.customerId.patchValue(this.data.customerName);  
      this.form.controls.fromAddress.patchValue(this.data.addressFrom);
      this.form.controls.toAddress.patchValue(this.data.addressTo);
      // this.form.controls.numberOfTrips.patchValue(this.data.numberOfTrips);
      // this.form.controls.packingCost.patchValue(this.data.packingCost);
      this.form.controls.jobCardType.patchValue(this.data.jobcardType);
    }

    // this.form.controls.copyFrom.valueChanges.subscribe(x => {
    //   this.copyTo();
    // })

  }

  customerIDList: any[] = [];
  processedByList: any[] = [];
  statusList: any[] = [];
  createdList: any[] = [];
  workOrderSbuList: any[] = [];

  dropdownlist() {
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.trans.customerID.url,
      this.cas.dropdownlist.setup.workorderprocessedby.url,
      this.cas.dropdownlist.setup.workorderstatus.url,
      this.cas.dropdownlist.setup.workordercreatedby.url,
      this.cas.dropdownlist.setup.sbu.url,
    ]).subscribe((results) => {
      this.customerIDList = this.cas.foreachlist2(results[0], this.cas.dropdownlist.trans.customerID.key);
      this.processedByList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.workorderprocessedby.key);
      this.statusList = this.cas.foreachlist1(results[2], this.cas.dropdownlist.setup.workorderstatus.key);
      this.createdList = this.cas.foreachlist(results[3], this.cas.dropdownlist.setup.workordercreatedby.key);
      this.workOrderSbuList = this.cas.foreachlist2(results[4], this.cas.dropdownlist.setup.sbu.key);
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }

  ELEMENT_DATA: any[] = [];
  displayedColumns: string[] = ['itemServiceName', 'itemServiceUnitPrice', 'itemServiceQuantity', 'itemServiceTotal', 'delete'];
  dataSource = new BehaviorSubject < AbstractControl[] > ([]);
  selection = new SelectionModel < any > (true, []);


  /** Whether the number of selected elements matches the total number of rows. */



  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;


  // ngAfterViewInit() {
  //   this.dataSource.paginator = this.paginator;
  //   this.dataSource.sort = this.sort;
  // }

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
    if (this.data.pageflow != 'New') {
      this.spin.show();
      this.sub.add(this.service.Get(this.data.code).subscribe(res => {
          this.form.patchValue(res, {
            emitEvent: false
          });

          this.tableArray = (res.itemServices)
          this.form.controls.createdOn1.patchValue(this.form.controls.workOrderDate.value);
          this.form.controls.endDate.patchValue(this.form.controls.endDate.value);

          this.form.controls.createdOn.patchValue(this.cs.dateapiutc0(this.form.controls.createdOn.value));
          this.form.controls.updatedOn.patchValue(this.cs.dateapiutc0(this.form.controls.updatedOn.value));
          
          //this.form.controls.updatedOn.patchValue(this.cs.dateapiutc0(this.form.controls.updatedOn.value));

          this.form.controls.workOrderDate.patchValue(this.cs.dateapi1(this.form.controls.workOrderDate.value));
          this.form.controls.endDate.patchValue(this.cs.dateapi1(this.form.controls.endDate.value));
          this.form.controls.startDate.patchValue(this.cs.dateapi1(this.form.controls.startDate.value));
          this.rows.clear();
          res.itemServices.forEach((d: any) => this.addRow(d, false));
          this.updateView();

          this.spin.hide();
        },
        err => {
          this.cs.commonerror(err);
          this.spin.hide();
        }
      ));
    } else this.ELEMENT_DATA.forEach((d: any) => this.addRow(d, false));
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
    //this.spin.show();

    if(this.form.controls.endTime.value == null){
      this.form.controls.endTime.patchValue('00:00');
    }
    if(this.form.controls.startTime.value == null){
      this.form.controls.startTime.patchValue('00:00');
    }
    
    let startTime = this.datePipe.transform(this.form.controls.startDate.value, "yyyy-MM-dd");
    let setStartDate = startTime != null ? (startTime + "T" + this.form.controls.startTime.value + ':00.000Z') : null;
    this.form.controls.startDate.patchValue(setStartDate);

    let endTime = this.datePipe.transform(this.form.controls.endDate.value, "yyyy-MM-dd");
    let setEndDate = endTime != null ? (endTime + "T" + this.form.controls.endTime.value + ':00.000Z') : null;
    this.form.controls.endDate.patchValue(setEndDate);

    let workOrder = this.datePipe.transform(this.form.controls.workOrderDate.value, "yyyy-MM-dd");
    let currentDateTime = this.datePipe.transform((new Date), 'HH:mm');
    let setWorkOrderDate = workOrder + "T" + currentDateTime + ':00.000Z';
  //  this.form.controls.workOrderDate.patchValue(setWorkOrderDate);
console.log(this.form.getRawValue())
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
        this.toastr.success(res.workOrderId + " Saved Successfully!", "Notification", {
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


  addRow(d ? : any, noUpdate ? : boolean) {


    const row = this.fb.group({
      createdBy: [],
      createdOn: [],
      deletionIndicator: [],
      itemName: [],
      itemServiceName: [],
      itemServiceQuantity: [],
      itemServiceTotal: [],
      itemServiceUnitPrice: [],
      updatedBy: [],
      updatedOn: [],
      workOrderId: []
    });
    if (d) {
      row.patchValue(d);
    }


    this.rows.push(row);
    if (!noUpdate) {
      this.updateView();
    }
  }

  updateView() {
    this.dataSource.next(this.rows.controls);
  }

  removerow(x: any) {
    this.rows.removeAt(x)
    // this.reset();
    this.dataSource.next(this.rows.controls);
  }


  lineData: any[] = [];



  selectedItemNameList: any[] = [];
  selectedConsumablesList: any[] = [];
  selectedhandlingChargesList: any[] = [];
  selectedpmTripList: any[] = [];
  selectedflRentList: any[] = [];

  ConsumablesList: any[] = [];
  handlingChargesList: any[] = [];
  flRentList: any[] = [];
  pmTripList: any[] = [];


  workOrderDropdownList() {
    this.spin.show();
    this.consumablesService.search({}).subscribe(res => {
      res.forEach((x: {
        description: string;unitPrice: string, referenceField1: string,
      }) => this.ConsumablesList.push({
        value: x.description,
        label: x.description,
        price: x.unitPrice,
        arabic: x.referenceField1
      }));
    });
    this.handlingsService.search({}).subscribe(res => {
      res.forEach((x: {
        description: string;unitPrice: string, referenceField1: string,
      }) => this.handlingChargesList.push({
        value: x.description,
        label: x.description,
        price: x.unitPrice,
        arabic: x.referenceField1
      }));
    });
    this.flService.search({}).subscribe(res => {
      res.forEach((x: {
        description: string;unitPrice: string, referenceField1: string,
      }) => this.flRentList.push({
        value: x.description,
        label: x.description,
        price: x.unitPrice,
        arabic: x.referenceField1
      }));
      this.spin.hide();
    });

          this.tripService.search({}).subscribe(res => {
            res.forEach((x: {
              description: string;unitPrice: string, referenceField1: string,
            }) => this.pmTripList.push({
              value: x.description,
              label: x.description,
              price: x.unitPrice,
              arabic: x.referenceField1
            }));

    })
  }


  tableArray: any[] = [];
  tableshow = false;
  execute() {
    console.log(this.tableArray)
    //this.tableArray = [];
    this.ConsumablesList.forEach(x => {
      this.selectedConsumablesList.forEach(y => {
        if (x.value == y) {
          this.tableArray.push({itemServiceName: x.value, itemServiceUnitPrice: x.price, itemName: x.arabic})
        }
      })
    });
    this.handlingChargesList.forEach(x => {
      this.selectedhandlingChargesList.forEach(y => {
        if (x.value == y) {
          this.tableArray.push({itemServiceName: x.value, itemServiceUnitPrice: x.price, itemName: x.arabic})
        }
      })
    })
    this.flRentList.forEach(x => {
      this.selectedflRentList.forEach(y => {
        if (x.value == y) {
          this.tableArray.push({itemServiceName: x.value, itemServiceUnitPrice: x.price, itemName: x.arabic})
          
        }
      })
    });
    this.pmTripList.forEach(x => {
      this.selectedpmTripList.forEach(y => {
        if (x.value == y) {
          this.tableArray.push({itemServiceName: x.value, itemServiceUnitPrice: x.price, itemName: x.arabic})
          
        }
      })
    });
    this.rows.clear();
    this.tableArray.forEach((d: any) => this.addRow(d, false));
      //   this.dataSource.paginator = this.paginator;
  //   this.dataSource.sort = this.sort;
    this.tableshow = true;
    console.log(this.tableArray)
  }

  inputChange(item){
    console.log(item)
    let total = ((item.get('itemServiceQuantity')?.value as number)*(item.get('itemServiceUnitPrice')?.value as number));
    item.get('itemServiceTotal')?.setValue(total);
  }
  inputChange1(item){
    console.log(item)
    let total = ((item.get('itemServiceQuantity')?.value as number)*(item.get('itemServiceUnitPrice')?.value as number));
    item.get('itemServiceTotal')?.setValue(total);
  }

 

    copyTo() {
      if (this.form.controls.copyFrom.value == "Payment") {
        this.dialogRef.close();
        const dialogRef = this.dialog.open(PaymentNewComponent, {
          disableClose: true,
          //  width: '55%',
          maxWidth: '80%',
          data: {
            pageflow: 'Copy From WorkOrder',
            jobCard: this.form.controls.jobCard.value,
            processedBy: this.form.controls.woProcessedBy.value,
            customerId: this.form.controls.customerId.value,
            workOrderId: this.form.controls.workOrderId.value,
            sbu: this.form.controls.workOrderSbu.value,
          }
        });
  
        dialogRef.afterClosed().subscribe(result => {
  
          this.router.navigate(["/main/operation/payment"]);
        });
      }
    }

}
