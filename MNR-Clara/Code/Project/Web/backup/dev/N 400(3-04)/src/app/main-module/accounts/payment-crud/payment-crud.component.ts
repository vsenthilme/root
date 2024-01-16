import { SelectionModel } from '@angular/cdk/collections';
import {
  Component,
  OnInit,
  ViewChild
} from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { DeleteComponent } from 'src/app/common-field/dialog_modules/delete/delete.component';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ExcelService } from 'src/app/common-service/excel.service';
import { AuthService } from 'src/app/core/core';
import { BillService } from '../bill/bill.service';
import { PaymentCrudNewComponent } from './payment-crud-new/payment-crud-new.component';
import {
  PaymentService
} from './payment.service';

@Component({
  selector: 'app-payment-crud',
  templateUrl: './payment-crud.component.html',
  styleUrls: ['./payment-crud.component.scss']
})
export class PaymentCrudComponent implements OnInit {

  screenid = 1188;
  displayedColumns: string[] = ['select', 'classId', 'clientId', 'matterNumber', 'invoiceNumber', 'paymentNumber', 'paymentAmount', 'paymentDate', 'paymentText'];

  public icon = 'expand_more';
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
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
  animal: string | undefined;
  id: string | undefined;

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
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
  checkboxLabel(row ? : any): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.quotationNo + 1}`;
  }
  clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }
  ELEMENT_DATA: any[] = [];
  dataSource = new MatTableDataSource < any > (this.ELEMENT_DATA);
  selection = new SelectionModel < any > (true, []);

  constructor(public dialog: MatDialog,
    private service: PaymentService, private router: Router,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private invoiceService: BillService,
    private fb: FormBuilder,
    private auth: AuthService) {}

  RA: any = {};
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);
    this.dropdownList();
    this.search();
  }
  deleteDialog() {
    if (this.selection.selected.length === 0) {
      this.toastr.error("Kindly select any Row", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    if (this.selection.selected[0].statusId == 43) {
      this.toastr.error("Closed Payment plan cannot be deleted.", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    const dialogRef = this.dialog.open(DeleteComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: {
        top: '6.5%'
      },
      data: this.selection.selected[0],
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0]);

      }
    });
  }
  deleterecord(id: any) {
    this.spin.show();

    this.sub.add(this.service.Delete(id.paymentId).subscribe((res) => {
      this.toastr.success(id.paymentId + " deleted successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });

      this.spin.hide(); //this.getAllListData();
      this.search();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  sub = new Subscription();
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }

  @ViewChild(MatSort)
  sort: MatSort;
  @ViewChild(MatPaginator)
  paginator: MatPaginator; // Pagination

  searhform = this.fb.group({
    clientId: [,],
    fromPaymentDate: [],
    fromPostingDate: [],
    invoiceNumber: [],
    matterNumber: [],
    paymentId: [],
    paymentNumber: [],
    toPaymentDate: [],
    toPostingDate: [],
    transactionType: [],
  });

  classList: any[] = [];
  clientList: any[] = [];
  matterList: any[] = [];
  paymentList: any[] = [];
  multiselectinvoicenoList: any[] = [];
  dropdownList() {
    this.cas.getalldropdownlist([this.cas.dropdownlist.matter.dropdown.url,
     ]).subscribe((results: any) => {
      results[0].classList.forEach((x: any) => {
        this.classList.push({ value: x.key, label: x.key + '-' + x.value });
      })
      results[0].clientNameList.forEach((x: any) => {
        this.clientList.push({ value: x.key, label: x.key + '-' + x.value });
      })
      results[0].matterList.forEach((x: any) => {
        this.matterList.push({ value: x.key, label: x.key + '-' + x.value });
      })

    }, (err) => {
      this.toastr.error(err, "");
    });

    this.sub.add(this.invoiceService.Search(this.searhform.getRawValue()).subscribe((res: any[]) => {
      let filterResult = res.filter(x => x.deletionIndicator == 0);
      filterResult.forEach((x: { invoiceNumber: string; }) => this.multiselectinvoicenoList.push({ value: x.invoiceNumber, label: x.invoiceNumber }))
      this.multiselectinvoicenoList = this.cas.removeDuplicatesFromArrayNew(this.multiselectinvoicenoList);
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));

  }


  search(){
    this.spin.show();
    this.searhform.controls.fromPaymentDate.patchValue(this.cs.day_callapiSearch(this.searhform.controls.fromPaymentDate.value));
    this.searhform.controls.toPaymentDate.patchValue(this.cs.day_callapiSearch(this.searhform.controls.toPaymentDate.value));

    this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((data: any) => {

      data.forEach(element => {
        this.paymentList.push({ value: element.paymentNumber, label: element.paymentNumber });
      });
      this.dataSource = new MatTableDataSource < any > (data);
      this.selection = new SelectionModel < any > (true, []);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
      this.spin.hide();
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    }));
  }
  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Payment Plan No": x.paymentPlanNumber,
        'Client Name': x.clientId,
        "Matter No  ": x.matterNumber,
        "Quote No ": x.quotationNo,
        'Start Date': x.paymentPlanStartDate,
        'Payment Plan Amount': this.cs.dateapi(x.paymentPlanTotalAmount),
        "No of Installments  ": x.noOfInstallment,
        "Installment  Amount  ": x.installmentAmount,
        "Status  ": x.statusIddes,
      });

    })
    this.excel.exportAsExcel(res, "Payment");
  }
  Clear() {
    this.reset();
  };



  openDialog(data: any = 'new'): void {
    if (data != 'New') {
      if (this.selection.selected.length === 0) {
        this.toastr.error("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
    }
    const dialogRef = this.dialog.open(PaymentCrudNewComponent, {
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.7%', },
      data: { pageflow: data, code: data != 'New' ? this.selection.selected[0].paymentId : null }
    });
    dialogRef.afterClosed().subscribe(result => {
      this.search();
    });
  }

  reset() {
    this.searhform.reset();
  }

}
