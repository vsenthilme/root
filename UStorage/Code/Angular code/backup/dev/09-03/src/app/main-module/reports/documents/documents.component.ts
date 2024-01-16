import {
  SelectionModel
} from '@angular/cdk/collections';
import { DatePipe } from '@angular/common';
import {
  Component,
  OnInit,
  ViewChild
} from '@angular/core';
import {
  FormBuilder, FormControl, Validators
} from '@angular/forms';
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
  variant
} from '../agreement-renew/agreement-renew.component';
import {
  ReportsService
} from '../reports.service';

@Component({
  selector: 'app-documents',
  templateUrl: './documents.component.html',
  styleUrls: ['./documents.component.scss']
})
export class DocumentsComponent implements OnInit {
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
  form = this.fb.group({
    customerCode: [[],],
    documentType: [[],],
    endDate: [this.cs.todayapi(), Validators.required],
    startDate: [, Validators.required],
  });

  isShowDiv = false;
  table = true;
  fullscreen = false;
  search = true;
  back = false;
  div1Function() {
    this.table = !this.table;
  }
  showFloatingButtons: any;
  toggle = true;
  public icon = 'expand_more';
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
  showFiller = false;
  documentList: any[] = []
  displayedColumns: string[] = ['documentType', 'documentNumber', 'documentDate', 'customerId','customerName',  'serviceType', 'storeNumber', 'amount', 'status'];
  sub = new Subscription();
  ELEMENT_DATA: variant[] = [];
  constructor(
    private router: Router,
    private spin: NgxSpinnerService,
    private cas: CommonApiService,
    public toastr: ToastrService,
    private fb: FormBuilder,
    public cs: CommonService,
    private service: ReportsService,
    public datepipe: DatePipe,
  ) {
    this.documentList = [{
        label: 'Agreement',
        value: 'agreement'
      },
      {
        label: 'Invoice',
        value: 'invoice'
      },
      {
        label: 'Payment',
        value: 'payment'
      },
      {
        label: 'Work Order',
        value: 'workorder'
      },
      {
        label: 'Quote',
        value: 'quotation'
      },
      {
        label: 'Inquiry',
        value: 'Inquiry'
      },


    ];
  }
  animal: string | undefined;
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
  RA: any = {};
  startDate: any;
  currentDate: Date;
  ngOnInit(): void {
    this.dropdownlist();
    this.currentDate = new Date();
    let yesterdayDate = new Date();
    let currentMonthStartDate = new Date();
    yesterdayDate.setDate(this.currentDate.getDate() - 1);
    this.startDate = this.datepipe.transform(yesterdayDate, 'dd');
   currentMonthStartDate.setDate(this.currentDate.getDate() - this.startDate);
  this.form.controls.startDate.patchValue(currentMonthStartDate);
  }

  CustomeridList: any[] = [];
  dropdownlist() {

    this.cas.getalldropdownlist([

      this.cas.dropdownlist.trans.customerID.url,
    ]).subscribe((results) => {

      this.CustomeridList = this.cas.foreachlist2(results[0], this.cas.dropdownlist.trans.customerID.key)
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }


  dataSource = new MatTableDataSource <any> (this.ELEMENT_DATA);
  selection = new SelectionModel <any> (true, []);



  @ViewChild(MatSort)
  sort!: MatSort;
  @ViewChild(MatPaginator)
  paginator!: MatPaginator; // Pagination
  // Pagination





  // filtersearch() {
  //   //  this.spin.show();
  //     this.table = true;
  //     this.search = true;
  //     this.fullscreen = true;


  // }
  togglesearch() {
    this.search = false;
    this.table = true;
    this.fullscreen = false;
    this.back = true;
  }
  backsearch() {
    this.table = true;
    this.search = true;
    this.fullscreen = true;
    this.back = false;
  }







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

  /** The label for the checkbox on the passed row */
  checkboxLabel(row ? : variant): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.warehouseno + 1}`;
  }



  clearselection(row: any) {

    this.selection.clear();
    this.selection.toggle(row);
  }

  onItemSelect(item: any) {
    console.log(item);
  }

  onSelectAll(items: any) {
    console.log(items);
  }
  submitted = false;

  dataTable: any[] = [];
  filtersearch() {
    this.dataTable = [];
    this.submitted = true;
    if (this.form.invalid) {
      this.toastr.error(
        "Please fill the required fields to continue",
        "Notification", {
          timeOut: 2000,
          progressBar: false,
        }
      );

      this.cs.notifyOther(true);
      return;
    }
    this.form.controls.endDate.patchValue(this.cs.endDate(this.form.controls.endDate.value));
    this.form.controls.startDate.patchValue(this.cs.startDate(this.form.controls.startDate.value));
console.log(this.form.controls.documentType)
    // if(this.form.controls.documentType){
    //   this.form.controls.documentType.patchValue(null);
    // }
    this.spin.show();
    this.sub.add(this.service.getAgreementTime(this.form.getRawValue()).subscribe(res => {

        res.enquiry.forEach(element => {
          if (res.enquiry.length > 0) {
            this.dataTable.push(element)
          }
        });
        res.quote.forEach(element => {
          if (res.quote.length > 0) {
            this.dataTable.push(element)
          }
        });
        res.agreement.forEach(element => {
          if (res.agreement.length > 0) {
            this.dataTable.push(element)
          }
        });
        res.invoice.forEach(element => {
          if (res.invoice.length > 0) {
            this.dataTable.push(element)
          }
        });
        res.payment.forEach(element => {
          if (res.payment.length > 0) {
            this.dataTable.push(element)
          }
        });
        res.workorder.forEach(element => {
          if (res.workorder.length > 0) {
            this.dataTable.push(element)
          }
        });
        this.dataTable = this.cs.removeDuplicatesFromArrayList(this.dataTable, 'documentNumber')
        console.log(this.dataTable)
        this.dataSource.data = this.dataTable;
        this.spin.hide()
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        this.spin.hide();

        this.spin.hide();
        this.table = true;
        this.search = false;
        this.fullscreen = false;
        this.back = true;
      },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
  }


  reset() {
    this.form.reset();
  }
  downloadexcel() {
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Document No": x.documentNumber,
        "Document Date":this.cs.dateapi(x.documentDate) ,
        "Customer Name": x.customerName,
        'Customer ID': x.customerId,
        'Service Type':x.serviceType ,
        'Storage ID':x.storeNumber,
        "Total amount": x.amount,
        "Status": x.status,
      });

    })
    this.cs.exportAsExcel(res,"DOCUMENTS");
  }
}
