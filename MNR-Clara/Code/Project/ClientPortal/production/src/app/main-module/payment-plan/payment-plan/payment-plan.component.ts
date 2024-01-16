import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { MatterPopupComponent } from "../../matters/matter-popup/matter-popup.component";
import { PaymentplanService } from "../paymentplan.service";



export interface ordermanagement {
  lineno: string;
  partner: string;
  product: string;
  status: string; 
  date:string;
  description:string;
  
  }
  
  const ELEMENT_DATA: ordermanagement[] = [
  { lineno: '300001 -01',status: 'Open', partner: '3223',date: '02-02-2022', product: 'Legal fees for filing I130',description: 'Aldi Arias Lopez - PERM'},
  { lineno: '300001 -02',status: 'Closed', partner: '3455',date: '02-02-2022', product: 'Legal fees for H1B ',description: 'Aldi Arias Lopez - PERM'},
  { lineno: '300001 -03',status: 'Open', partner: '5543',date: '02-02-2022', product: 'Legal fees for H1B ',description: 'Aldi Arias Lopez - PERM'},
  { lineno: '300001 -04',status: 'Closed', partner: '2345',date: '02-02-2022', product: 'Legal fess for N400 ',description: 'Aldi Arias Lopez - PERM'},
  
  ];
@Component({
  selector: 'app-payment-plan',
  templateUrl: './payment-plan.component.html',
  styleUrls: ['./payment-plan.component.scss']
})
export class PaymentPlanComponent implements OnInit {
  displayedColumns: string[] = ['matterNumber','paymentPlanNumber','paymentPlanTotalAmount','statusId','no',];
  sub = new Subscription();
  ELEMENT_DATA: any[] = [];
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  public icon = 'expand_more';
  constructor(public dialog: MatDialog,
    private service: PaymentplanService,
   // private cas: CommonApiService,
    public toastr: ToastrService,
    private router: Router, 
    private spin: NgxSpinnerService,
    private cs: CommonService,
   // private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService) { }
    routeto(url: any, id: any) {
      localStorage.setItem('crrentmenu', id);
      this.router.navigate([url]);
    }
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
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
  ngOnInit(): void {
   // this.auth.isuserdata();

    this.getAll();
  }

  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
  selection = new SelectionModel<any>(true, []);

 
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }
  statuslist: any[] = [];
  @ViewChild(MatSort, { static: true })
  sort!: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator!: MatPaginator; // Pagination
 // Pagination

 
 statusList: any;
 multiselectstatusList: any[] = [];
 multistatusList: any[] = [];
 
 getAll() {
  this.spin.show();

  
  this.sub.add(this.service.Getstatus().subscribe(res => {
    this.statusList = res;
    console.log(this.statusList)
    this.statusList.forEach((x: { statusId: string; status: string; }) => this.multistatusList.push({ id: x.statusId, itemName: x.status }))

    console.log(this.multistatusList)
    this.spin.hide();
  },
    err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));


  this.sub.add(this.service.Getall().subscribe((res: any[]) => {
    let filteredData: any[] = [];
    this.ELEMENT_DATA = res;
    this.ELEMENT_DATA.forEach((x) => {
      if(x.clientId == this.auth.clientId){
        x.statusId = this.multistatusList.find(y => y.id == x.statusId)?.itemName;
        filteredData.push(x);
      }
    })
    this.dataSource = new MatTableDataSource<any>(filteredData);
    this.selection = new SelectionModel<any>(true, []);
    this.dataSource.sort = this.sort;
   this.dataSource.paginator = this.paginator;
    this.spin.hide();
  }, err => {
    this.cs.commonerror(err);
    this.spin.hide();
  }));
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
  checkboxLabel(row?: any): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.matterNumber + 1}`;
  }



  matter() {
    const dialogRef = this.dialog.open(MatterPopupComponent, {
      disableClose: true,
    });

    dialogRef.afterClosed().subscribe();
  }


  clearselection(row: any) {

    this.selection.clear();
    this.selection.toggle(row);
  }

  openDialog(data: any = 'new'): void {

  //  if (data != 'New')
      // if (this.selection.selected.length === 0) {
      //   this.toastr.error("Kindly select any Row", "Notification", {
      //     timeOut: 2000,
      //     progressBar: false,
      //   });
      //   return;
      // }
//    if (this.selection.selected.length > 0) {
      // if (this.selection.selected[0].statusId == 43 && data == 'Edit') {
      //   this.toastr.error("Closed Payment plan cannot be edited.", "Notification", {
      //     timeOut: 2000,
      //     progressBar: false,
      //   });
      //   return;
      // }
      let paramdata = this.cs.encrypt({ code: data });
      console.log(paramdata)
      this.router.navigate(['/main/payment/payment-details/' + paramdata]);
 //   }

  }

}

