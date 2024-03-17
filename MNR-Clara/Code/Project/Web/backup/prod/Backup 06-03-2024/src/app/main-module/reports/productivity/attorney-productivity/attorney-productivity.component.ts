import { SelectionModel } from '@angular/cdk/collections';
import { DatePipe } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
  import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ExcelService } from 'src/app/common-service/excel.service';
import { AuthService } from 'src/app/core/core';
import { PeriodicElement } from '../../accounting/ar-report/ar-report.component';
import { ReportServiceService } from '../../report-service.service';
import { FeesSharingComponent } from './fees-sharing/fees-sharing.component';

@Component({
  selector: 'app-attorney-productivity',
  templateUrl: './attorney-productivity.component.html',
  styleUrls: ['./attorney-productivity.component.scss']
})
export class AttorneyProductivityComponent implements OnInit {

  screenid = 1189;
    public icon = 'expand_more';
    isShowDiv = false;
    table = true;
    fullscreen = false;
    search = true;
    back = false;
    showFloatingButtons: any;
    toggle = true;
  submitted: boolean;
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
  
    sub = new Subscription();

  
    form = this.fb.group({
    classId:              [, Validators.required],
     assignedTimeKeeper:    [, ],
     caseCategoryId:        [, ],
     caseSubCategoryId:     [, ],
     fromDate:              [, ],
     fromFiledDateFE:              [, ],
     fromFiledDate:              [, ],
     toFiledDateFE:              [, ],
     toFiledDate:              [, ],
     fromDateFE:             [new Date("01/01/00 00:00:00"), ],
     matterNumber:          [, ],
     originatingTimeKeeper: [, ],
     responsibleTimeKeeper: [, ],
     toDate:                [, ],
     toDateFE:               [this.cs.todayapi(),],

    });
  
    displayedColumns: string[] = [
      'statusDescription',
      'classId',
      'clientId', 
      'clientName',
      'matterNumber',
      'matterDescription',
        'caseCategoryId', 
        'caseSubCategoryId', 
        'timeKeeperCode1',
        'feeSharingPercentage1',
        'feeSharingAmount1',
        'timeKeeperCode2',
        'feeSharingPercentage2',
        'feeSharingAmount2',
        'timeKeeperCode3',
        'feeSharingPercentage3',
        'feeSharingAmount3',
     //   'feeSharingAmount', 
        'originatingTimeKeeper', 
        'responsibleTimeKeeper', 
        'assignedTimeKeeper', 
        'legalAssistant', 
        'paraLegal', 
        'caseOpenDate', 
        'caseFiledDate', 
        'invoiceAmount', 
    ];
    dataSource = new MatTableDataSource<any>();
    selection = new SelectionModel<PeriodicElement>(true, []);
  
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
    checkboxLabel(row?: PeriodicElement): string {
      if (!row) {
        return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
      }
      return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.name + 1}`;
    }
    constructor(
      public dialog: MatDialog,
      private service: ReportServiceService,
      private cs: CommonService,
      private spin: NgxSpinnerService,
      private excel: ExcelService,
      private fb: FormBuilder,
      public toastr: ToastrService,
      public datepipe: DatePipe,
      private cas: CommonApiService,
      private auth: AuthService) { }
    RA: any = {};
    startDate: any;
    currentDate: Date;
      
    ngOnInit(): void {
  
      this.RA = this.auth.getRoleAccess(this.screenid);
      this.getAllDropDown();
      this.currentDate = new Date();
      let yesterdayDate = new Date();
      let currentMonthStartDate = new Date();
      yesterdayDate.setDate(this.currentDate.getDate() - 1);
      this.startDate = this.datepipe.transform(yesterdayDate, 'dd');
     currentMonthStartDate.setDate(this.currentDate.getDate() - this.startDate);
    this.form.controls.fromDateFE.patchValue(currentMonthStartDate);
    }
    ngAfterViewInit() {
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    }
    @ViewChild(MatPaginator) paginator: MatPaginator;
   @ViewChild(MatSort) sort: MatSort;
    applyFilter(event: Event) {
      const filterValue = (event.target as HTMLInputElement).value;
      this.dataSource.filter = filterValue.trim().toLowerCase();
  
      if (this.dataSource.paginator) {
        this.dataSource.paginator.firstPage();
      }
    }
    filtersearch() {
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
      console.log(this.form.controls.toDateFE.value)
      console.log(this.form.controls.toFiledDateFE.value)
      if (this.form.controls.toDateFE.value == null && this.form.controls.toFiledDateFE.value == null) {
        this.toastr.error(
          "Please fill either open date or filed date",
          "Notification", {
          timeOut: 2000,
          progressBar: false,
        }
        );
  
        this.cs.notifyOther(true);
        return;
      } 
      this.form.controls.fromDate.patchValue(this.cs.dateNewFormat1(this.form.controls.fromDateFE.value));
      this.form.controls.toDate.patchValue(this.cs.dateNewFormat1(this.form.controls.toDateFE.value));
      
      this.form.controls.fromFiledDate.patchValue(this.cs.dateNewFormat1(this.form.controls.fromFiledDateFE.value));
      this.form.controls.toFiledDate.patchValue(this.cs.dateNewFormat1(this.form.controls.toFiledDateFE.value));

      this.spin.show();
      this.sub.add(this.service.getAttorneyProduction(this.form.getRawValue()).subscribe(res => {
        this.dataSource.data = res;
        this.spin.hide()
        this.dataSource.paginator = this.paginator;
         this.dataSource.sort = this.sort;
        this.spin.hide();
        this.table = true;
        this.search = false;
        this.back = true;
      },
        err => {
          this.cs.commonerror(err);
          this.spin.hide();
        }));
    }
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
  
    multiSelectCaseCatList: any[] = [];
    multiSelectCaseSubCatList: any[] = [];
    userIdList: any[] = [];
    matterAssignmentIdList: any[] = [];
    originatingTimeList: any[] = [];
    assignedTimeList: any[] = [];
    respTimeList: any[] = [];
    multiSelectClassList: any[] = [];
    getAllDropDown() {
      this.cas.getalldropdownlist([
        this.cas.dropdownlist.matter.dropdown.url,
        this.cas.dropdownlist.matter.matterAssignment.url,
        this.cas.dropdownlist.setup.userId.url,
      ]).subscribe((results: any) => {
        results[0].classList.forEach((x: any) => {
          this.multiSelectClassList.push({ value: x.key, label: x.key + '-' + x.value });
        })

        results[0].caseCategoryList.forEach((x: any) => {
          this.multiSelectCaseCatList.push({ value: x.key, label: x.key + '-' + x.value });
        })
        results[0].subCaseCategoryList.forEach((x: any) => {
          this.multiSelectCaseSubCatList.push({ value: x.key, label: x.key + '-' + x.value });
        })
        this.matterAssignmentIdList = results[1];
        this.userIdList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.userId.key);
        this.userIdList.forEach(user => {
        for (let i = 0; i < this.matterAssignmentIdList.length; i++) {
          if (user.key == this.matterAssignmentIdList[i].originatingTimeKeeper) {
            this.originatingTimeList.push({ value: user.key, label: user.value });
            break;
          }
        }
        //mainAttorney
        for (let i = 0; i < this.matterAssignmentIdList.length; i++) {
          if (user.key == this.matterAssignmentIdList[i].responsibleTimeKeeper) {
            this.respTimeList.push({ value: user.key, label: user.value })
            break;
          }
        }
        //assignedTK
        for (let i = 0; i < this.matterAssignmentIdList.length; i++) {
          if (user.key == this.matterAssignmentIdList[i].assignedTimeKeeper) {
            this.assignedTimeList.push({ value: user.key, label: user.value })
            break;
          }
        }
      })
       

      }, (err) => {
        this.toastr.error(err, "");
      });
    }
  
  
    public errorHandling = (control: string, error: string = "required") => {
      return this.form.controls[control].hasError(error) && this.submitted;
    }
    
  email = new FormControl('', [Validators.required, Validators.email]);
    getErrorMessage() {
      if (this.email.hasError('required')) {
        return ' Field should not be blank';
      }
      return this.email.hasError('email') ? 'Not a valid email' : '';
    }
    reset() {
      this.form.reset();
    }
  
    downloadexcel() {
      // if (excel)
      var res: any = [];
      this.dataSource.data.forEach(x => {
        res.push({
          'Status': x.statusDescription,
          "Class ID": x.classId,
          'Client ID': x.clientId,
          'Client Name': x.clientName,
          'Matter No': x.matterNumber,
          'Matter ID': x.matterDescription,
          'Case Category': x.caseCategoryId,
          'Case Sub Category': x.caseSubCategoryId,
      //    'Flat Fee Amount ': x.flatFee,
          'Time Keeper 1': x.timeKeeperCode1,
          'Fees Sharing 1 %': x.feeSharingPercentage1,
          'Fees Amount 1': x.feeSharingAmount1,
          'Time Keeper 2': x.timeKeeperCode2,
          'Fees Sharing 2 %': x.feeSharingPercentage2,
          'Fees Amount 2': x.feeSharingAmount2,
          'Time Keeper 3': x.timeKeeperCode3,
          'Fees Sharing 3 %': x.feeSharingPercentage3,
          'Fees Amount 3': x.feeSharingAmount3,
          'Originating TK' : x.originatingTimeKeeper,
          'Responsible TK' : x.responsibleTimeKeeper,
          'Assigned TK' : x.assignedTimeKeeper,
          'Legal Assistant TK' : x.legalAssistant,
          'ParaLegal' : x.paraLegal,
          'Date Opened': this.cs.dateapi(x.caseOpenDate),
          'Date Filed': this.cs.dateapi(x.caseFiledDate),
          'Billed Fees' : x.invoiceAmount,
        });
  
      })
      this.excel.exportAsExcel(res, "Attorney Productivity");
    }



    openDialog(element): void {
      const dialogRef = this.dialog.open(FeesSharingComponent, {
        disableClose: true,
        width: '70%',
        maxWidth: '80%',
        position: { top: '6.5%' },
        data: element
      });
  
      dialogRef.afterClosed().subscribe(result => {
      });
    }
  
  }
  
  
  
  
