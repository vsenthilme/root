



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
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { StorageunitService } from './storageunit.service';


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
}

const ELEMENT_DATA: PeriodicElement[] = [
  {usercode: "test", name: 'test', admin: 'test', role: 'test', userid: 'test', password: 'test', status: 'test', email: 'test', phoneno: 'test',Agreement: 'test'},
];

@Component({
  selector: 'app-storage-unit',
  templateUrl: './storage-unit.component.html',
  styleUrls: ['./storage-unit.component.scss']
})
export class StorageUnitComponent implements OnInit {




  constructor(private router: Router,
    private fb: FormBuilder,
    public toastr: ToastrService,
    public cs: CommonService,
    private spin: NgxSpinnerService,
    private auth: AuthService,
    public dialog: MatDialog,
    private route: ActivatedRoute,
    private service: StorageunitService,
    private cas: CommonApiService,
  ) { }
  ngOnInit() {
    this.search();
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

  displayedColumns: string[] = ['select', 'itemCode', 'codeId', 'description', 'phase', 'doorType','storeSizeMeterSquare', 'priceMeterSquare', 'weekly', 'monthly', 'halfYearly', 'yearly', 'availability', 'linkedAgreement','createdBy','createdOn'];
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
  checkboxLabel(row?: any): string {
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


  materialMaster(data: any = 'new'): void {
  let paramdata = "";
  paramdata = this.cs.encrypt({ pageflow: data, code: data != 'New' ? this.selection.selected[0].itemCode : null });
  this.router.navigate(['/main/materialmasters/storageunitNew/' + paramdata]);
}
// this.router.navigate(['/main/materialmasters/storageunitNew']);}

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
    data: this.selection.selected[0].itemCode,
  });

  dialogRef.afterClosed().subscribe(result => {

    if (result) {
      this.deleterecord(this.selection.selected[0].itemCode);

    }
  });
}

applyFilter(event: Event) {
  const filterValue = (event.target as HTMLInputElement).value;
  this.dataSource.filter = filterValue.trim().toLowerCase();
}

deleterecord(id: any) {
  this.spin.show();
  this.sub.add(this.service.Delete(id).subscribe((res) => {
    this.toastr.success(id + " Deleted successfully.","Notification",{
      timeOut: 2000,
      progressBar: false,
    });
    this.spin.hide();
    this.search();
  }, err => {
    this.cs.commonerror(err);
    this.spin.hide();
  }));
}

multiselectitemCodeList: any[] = [];
multiselectdescriptionList: any[] = [];
multiselectitemGroupList: any[] = [];
multiselectsaleUnitOfMeasureList: any[] = [];
multiselectpurchaseUnitOfMeasureList: any[] = [];
multiselectinStockList: any[] = [];
multiselectOpenWoList: any[] =[];
multiselectlastReceiptList: any[] = [];

searhform = this.fb.group({
  availability: [],
  bin: [],
  doorType: [],
  isActive: [],
  itemCode: [],
  itemGroup: [],
  itemType: [],
  storageType: [],
});

downloadexcel() {
  var res: any = [];
  this.dataSource.data.forEach(x => {
    res.push({
      "Storage ID": x.itemCode,
      "Phase":x.phase,
      "Store No": x.codeId,
      "Description": x.description,
      "Door Type ": x.doorType,
      'Storage Size	': x.storeSizeMeterSquare,
      'Price Per M2	': x.priceMeterSquare,
      'Weekly': x.weekly,
      "Monthly": x.monthly,
      "HALFYEARLY": x.halfYearly,
      "Yearly": x.yearly,
      "Availability":x.availability,
      "Curr.Agreement": x.linkedAgreement,
      "Created On":this.cs.dateapiutc0(x.createdOn),
      "Created By":x.createdBy,
    
    });

  })
  this.cs.exportAsExcel(res, "Storage-unit");
}
search(){
  this.service.search(this.searhform.value).subscribe(res => {
    this.spin.hide();
    res.forEach((x) => this.multiselectitemCodeList.push({value: x.itemCode, label: x.itemCode}));
    this.multiselectitemCodeList = this.cs.removeDuplicatesFromArrayNew(this.multiselectitemCodeList);
    
    res.forEach((x) => this.multiselectitemGroupList.push({value: x.itemGroup, label: x.itemGroup}));
    this.multiselectitemGroupList = this.cs.removeDuplicatesFromArrayNew(this.multiselectitemGroupList);

    this.dataSource = new MatTableDataSource<any>(res);
    this.selection = new SelectionModel<any>(true, []);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }, err => {

    this.cs.commonerror(err);
    this.spin.hide();

  });
}

resetSearch(){
  this.searhform.reset();
}

}

