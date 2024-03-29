



  

  import { SelectionModel } from '@angular/cdk/collections';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router, ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { NumberingseriesNewComponent } from './numberingseries-new/numberingseries-new.component';
import { NumeringseriesService } from './numeringseries.service';


export interface PeriodicElement {
  usercode: string;
  name: string;
  admin: string;
  userid: string;
  role: string;
}

const ELEMENT_DATA: PeriodicElement[] = [
  {usercode: 'US', name: 'STU', admin: 'Storage Unit', userid: '0001', role: '9999'},
];


@Component({
  selector: 'app-numbering-series',
  templateUrl: './numbering-series.component.html',
  styleUrls: ['./numbering-series.component.scss']
})
export class NumberingSeriesComponent implements OnInit {


  constructor(private router: Router,
    private fb: FormBuilder,
    public toastr: ToastrService,
    private cs: CommonService,
    private spin: NgxSpinnerService,
    private auth: AuthService,
    public dialog: MatDialog,
    private route: ActivatedRoute,
    private service: NumeringseriesService 
  ) { }
  ngOnInit() {
    this.getAll();
  }
  
  sub = new Subscription();
  ELEMENT_DATA: any[] = [];



  //#region common
  // for header title
  title1 = "Organisation Setup";
  title2 = "Company";





  isShowDiv = false;
  public icon = 'expand_more';
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
    console.log('show:' + this.showFloatingButtons);
  }


  displayedColumns: string[] = ['select', 'description', 'numberRangeCode', 'codeId','documentName','numberRangeFrom','numberRangeTo'];
  dataSource = new MatTableDataSource<any>(ELEMENT_DATA);
  selection = new SelectionModel<any>(true, []);
  

  /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  toggleAllRows() {
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.usercode + 1}`;
  }

  clearselection(row: any) {
    this.selection.clear();
    this.selection.toggle(row);
  }


  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }


  openDialog(data: any = 'New'): void {
    if (data != 'New')
    if (this.selection.selected.length === 0) {
      this.toastr.warning("Kindly select any Row", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    const dialogRef = this.dialog.open(NumberingseriesNewComponent, {
      disableClose: true,
      width: '55%',
      maxWidth: '80%',
      position: { top: '9%', },
      data: { pageflow: data, code: data != 'New' ? this.selection.selected[0].numberRangeCode : null}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.getAll();
    });
  }

  getAll() {
    this.spin.show();
    this.sub.add(this.service.Getall().subscribe((res: any[]) => {
      console.log(res)
      this.dataSource = new MatTableDataSource<any>(res);
      this.selection = new SelectionModel<any>(true, []);
      this.dataSource.sort = this.sort;
     this.dataSource.paginator = this.paginator;
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  
  deleteDialog() {
    if (this.selection.selected.length === 0) {
      this.toastr.warning("Kindly select any Row", "Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    const dialogRef = this.dialog.open(DeleteComponent, {
      disableClose: true,
      width: '40%',
      maxWidth: '80%',
      position: { top: '9%', },
      data: this.selection.selected[0].code,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].numberRangeCode);

      }
    });
  }


  deleterecord(id: any) {
    this.spin.show();
    this.sub.add(this.service.Delete(id).subscribe((res) => {
      this.toastr.success(id + " Deleted successfully.","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.spin.hide();
      this.getAll();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
   applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
  
  downloadexcel() {
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
      "SBU Code" : x.description,
      "Code": x.numberRangeCode,
      "Object": x.numberRangeObject,
      "TO": x.numberRangeFrom,
      "From": x.numberRangeTo
      });

    })
    this.cs.exportAsExcel(res, "NumberRange");
  }
}
