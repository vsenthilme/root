import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { MatTableDataSource } from "@angular/material/table";
import { Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { SetupSelectionService } from "./setup-selection.service";
import { Storage, StorageList } from "src/app/models/storage";
import { Table } from "primeng/table";

export interface selection {
  no: string;
  lineno: string;
  partner: string;
  product: string;
  description: string;
  refdocno: string;
  
  }
  
  export class StorageListCls implements StorageList {
    constructor(public storage?: Storage[]) {
    }
  }

@Component({
  selector: 'app-setup-selection',
  templateUrl: './setup-selection.component.html',
  styleUrls: ['./setup-selection.component.scss']
})

export class SetupSelectionComponent implements OnInit {

  advanceFilterShow: boolean;
  @ViewChild('organization') organization: Table | undefined;
  products: any;
  selectedProducts : any[];


  advanceFilter(){
    this.advanceFilterShow = !this.advanceFilterShow;
  }

  storageListCls = new StorageListCls();
  constructor(private spinner: NgxSpinnerService,private router: Router, 
    public dialog: MatDialog, private setupSelectionService: SetupSelectionService) {}
    routeto(url: any, id: any) {
      sessionStorage.setItem('crrentmenu', id);
      this.router.navigate([url]);
    }

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

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

 

  applyFilterGlobal($event: any, stringVal: any) {
    this.organization!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  }

  ngOnInit(): void {
    /** spinner starts on init */
    this.spinner.show();

    setTimeout(() => {
      /** spinner ends after 5 seconds */
      this.spinner.hide();
    }, 500);

    this.getSetupMasterData();
 }

  displayedColumns: string[] = ['select','companyNo', 'plantCode', 'warehouseNo','floorNo','storageNo',  'createdBy','createdOn'];
  dataSource = new MatTableDataSource<Storage>();
  selection = new SelectionModel<Storage>(true, []);

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
  // checkboxLabel(row?:  Warehouse): string {
  //   if (!row) {
  //     return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
  //   }
  //   return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.no + 1}`;
  // }
  clearselection(row: any) {
    this.selection.clear();
    this.selection.toggle(row);
  }

  getSetupMasterData()
  {
    this.setupSelectionService.getSetupMasters().subscribe(
      result => {
        console.log(result);
        this.storageListCls.storage = result;

        this.products =  this.storageListCls.storage;
        this.dataSource = new MatTableDataSource(this.storageListCls.storage);
      },
      error => {
        console.log(error);
      }
    );
  }

  onEditClick()
  {
    sessionStorage.setItem('storageSection', JSON.stringify(this.selectedProducts[0]));
    this.routeto('/main/setup/company', 1001);
    
    // sessionStorage.setItem('storageSection', JSON.stringify(this.selection.selected[0]));
    // this.routeto('/main/setup/company', 1001);
  }

  onNewClick()
  {
    sessionStorage.removeItem('storageSection');
    this.routeto('/main/setup/company', 1001);
  }



  
}
