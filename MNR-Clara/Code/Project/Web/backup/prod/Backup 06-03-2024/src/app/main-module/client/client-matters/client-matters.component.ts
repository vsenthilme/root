import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { Router, ActivatedRoute } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DeleteComponent } from "src/app/common-field/dialog_modules/delete/delete.component";
import { CommonApiService, dropdownelement } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { GeneralMatterElement, GeneralMatterService } from "../../matters/case-management/General/general-matter.service";
import { ClientGeneralService } from "../client-general/client-general.service";

interface SelectItem {
  id: string;
  itemName: string;
}


@Component({
  selector: 'app-client-matters',
  templateUrl: './client-matters.component.html',
  styleUrls: ['./client-matters.component.scss']
})
export class ClientMattersComponent implements OnInit {
  screenid = 1088;
  public icon = 'expand_more';

  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  clientlist: any[] = [];
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
  checkboxLabel(row?: GeneralMatterElement): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.classId + 1}`;
  }
    clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }

  ELEMENT_DATA: GeneralMatterElement[] = [];


  displayedColumns: string[] = ['select', 'matterNumber', 'matterDescription', 'clientId', 'caseInformationNo', 'caseCategoryId', 'scaseOpenedDate', 'statusId',];
  dataSource = new MatTableDataSource<GeneralMatterElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<GeneralMatterElement>(true, []);

  constructor(public dialog: MatDialog,
    private service: GeneralMatterService, private router: Router, private route: ActivatedRoute,
    public toastr: ToastrService, private serviceClient: ClientGeneralService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService) { }
  ClientFilter: any;
  RA: any = {};
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);


    let code = this.route.snapshot.params.code;

    if (code) {
      let js = this.cs.decrypt(code).code;
      this.searhform.controls.clientId.patchValue([js]);
      this.searhform.controls.clientId.disable();
      this.isPagematter = false;
      this.ClientFilter = { clientId: js };
    }
    this.getAllListData();

  }
  deleteDialog() {
    if (this.selection.selected.length === 0) {
      this.toastr.error("Kindly select any Row", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    const dialogRef = this.dialog.open(DeleteComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: this.selection.selected[0].matterNumber,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].matterNumber);

      }
    });
  }
  deleterecord(id: any) {
    this.spin.show();
    this.sub.add(this.service.Delete(id).subscribe((res) => {
      this.toastr.success(id + " deleted successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });

      this.spin.hide(); //this.getAllListData();
      window.location.reload();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  openDialog(data: any = 'new'): void {
    if (data != 'New')
      if (this.selection.selected.length === 0) {
        this.toastr.error("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
    let paramdata = "";
    sessionStorage.removeItem('matter');
    if (this.selection.selected.length > 0) {
      paramdata = this.cs.encrypt({ code: this.selection.selected[0].matterNumber, pageflow: data });
      sessionStorage.setItem('matter', paramdata);
      this.router.navigate(['/main/matters/case-management/matter/' + paramdata]);
    }
    else {
      sessionStorage.setItem('matter', paramdata);

      paramdata = this.cs.encrypt({ pageflow: data });
      this.router.navigate(['/main/matters/case-management/matter/' + paramdata]);
    }

    // const dialogRef = this.dialog.open(CasecategoryDisplayComponent, {
    //   disableClose: true,
    //   width: '50%',
    //   maxWidth: '80%',
    //   position: { top: '6.5%' },
    //   data: { pageflow: data, code: data != 'New' ? this.selection.selected[0].clientId : null }
    // });

    // dialogRef.afterClosed().subscribe(result => {

    //   this.getAllListData();
    // });
  }
  sub = new Subscription();
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }

  @ViewChild(MatSort, { static: true })
  sort: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination
  caseCategoryIdList: any[] = [];
  statuslist: any[] = [];
  matterlist: any[] = [];
  caseinfolist: any[] = [];


  selectedItems2: SelectItem[] = [];
  multiselectstatusList: any[] = [];
  multistatusList: any[] = [];

  selectedItems3: SelectItem[] = [];
  multiselectcasecatList: any[] = [];
  multicasecatList: any[] = [];

  selectedItems4: SelectItem[] = [];
  multiselectmatterList: any[] = [];
  multimatterList: any[] = [];

  selectedItems5: SelectItem[] = [];
  multiselectcaseinfoList: any[] = [];
  multicaseinfoList: any[] = [];

  dropdownSettings = {
    singleSelection: false,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };


  getAllMatter() {
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.matter.dropdown.url,
    ]).subscribe((results: any) => {
      this.spin.show();
      results[0].matterList.forEach((x: any) => {
        this.multiselectmatterList.push({ value: x.key, label: x.key + '-' + x.value });
      })
      results[0].clientNameList.forEach((client: any) => {
        this.clientlist.push({ value: client.key, label: client.value });
      }) 
      results[0].caseCategoryList.forEach((casecat: any) => {
        this.multiselectcasecatList.push({ value: casecat.key, label: casecat.key + '-' + casecat.value });
      })
      this.spin.hide();
    }, (err) => {
      this.spin.hide();
      this.toastr.error(err, "");
    });
  }
  
  getall(excel: boolean = false) {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.matter.dropdown.url,
      this.cas.dropdownlist.caseinfo.caseinfoId.url,
      this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results: any) => {

      results[0].matterList.forEach((x: any) => {
        this.multiselectmatterList.push({ value: x.key, label: x.key + '-' + x.value });
      })
      results[0].clientNameList.forEach((client: any) => {
        this.clientlist.push({ value: client.key, label: client.value });
      }) 
      this.caseCategoryIdList = results[0].caseCategoryList
      results[0].caseCategoryList.forEach((casecat: any) => {
        this.multiselectcasecatList.push({ value: casecat.key, label: casecat.key + '-' + casecat.value });
      })

      this.statuslist = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.setup.statusId.key).filter(s => [26, 27, 28, 29, 30, 36].includes(s.key));
      console.log(this.statuslist)
      this.statuslist.forEach((x: { key: string; value: string; }) => this.multistatusList.push({ value: x.key, label: x.value }))
      this.multiselectstatusList = this.multistatusList;
      this.caseinfolist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.caseinfo.caseinfoId.key);
      this.caseinfolist.forEach((x: { key: string; value: string; }) => this.multicaseinfoList.push({ value: x.key, label: x.value }))
      this.multiselectcaseinfoList = this.multicaseinfoList;

      this.sub.add(this.service.SearchNew({clientId : [this.searhform.controls.clientId.value[0]]}).subscribe((res: GeneralMatterElement[]) => {
      console.log(this.caseCategoryIdList)
        res.forEach((x) => {
          // x.classId = this.classIdList.find(y => y.key == x.classId)?.value;
      //    x.statusId = this.statuslist.find(y => y.key == x.statusId)?.value;
         // x.caseCategoryId = this.caseCategoryIdList.find(y => y.key == x.caseCategoryId)?.value;
       //  x['clientname'] = this.clientlist.find(y => y.value == x.clientId)?.label;
        })


        this.ELEMENT_DATA = res;
        this.caseInformationList = [];

        const categories = this.ELEMENT_DATA.map(person => ({
          caseInformationNo: person.caseInformationNo,
        }));
        const distinctThings = categories.filter(
          (thing, i, arr) => arr.findIndex(t => t.caseInformationNo === thing.caseInformationNo) === i
        );
        distinctThings.forEach(x => {

          this.caseInformationList.push({ key: x.caseInformationNo, value: x.caseInformationNo });
        });

        if (excel)
          this.excel.exportAsExcel(this.ELEMENT_DATA.sort((a, b) => (a.matterNumber > b.matterNumber) ? -1 : 1));
        this.dataSource = new MatTableDataSource<GeneralMatterElement>(this.ELEMENT_DATA.sort((a, b) => (a.matterNumber > b.matterNumber) ? -1 : 1));
        this.selection = new SelectionModel<GeneralMatterElement>(true, []);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }, (err) => {
      this.toastr.error(err, "");
    });

  }
  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Matter ID": x.matterNumber,
        'Matter Description': x.matterDescription,
        "Client Name  ": x.clientId,
        "Case Info Sheet No ": x.caseInformationNo,
        'Case Category': x.caseCategoryId,
        "Status  ": x.statusId,
        'Case Opened Date': this.cs.dateapi(x.caseOpenedDate),
      });

    })
    this.excel.exportAsExcel(res, "Client Matters");
  }
  getAllListData() {
    this.getall();
  }

  isPagematter = true;
  caseInformationList: dropdownelement[] = [];
  searchStatusList = {
    statusId: [26, 27, 28, 29, 30, 36
    ]
  };
  searhform = this.fb.group({
    clientId: [],
    caseCategoryId: [],
    caseCategoryIdFE: [],
    caseInformationNo: [],
    caseInformationNoFE: [],
    caseOpenedDate1: [],
    caseOpenedDate2: [],
    matterDescription: [],
    matterNumber: [],
    matterNumberFE: [],
    statusId: [],
    statusIdFE: [],
  });
  Clear() {
    this.reset();


  };

  search() {
    this.searhform.controls.caseOpenedDate1.patchValue(this.cs.day_callapiSearch(this.searhform.controls.caseOpenedDate1.value));
    this.searhform.controls.caseOpenedDate2.patchValue(this.cs.day_callapiSearch(this.searhform.controls.caseOpenedDate2.value));

    if (this.selectedItems2 && this.selectedItems2.length > 0) {
      let multistatusList: any[] = []
      this.selectedItems2.forEach((a: any) => multistatusList.push(a.id))
      this.searhform.patchValue({ statusId: this.selectedItems2 });
    }

    if (this.selectedItems4 && this.selectedItems4.length > 0) {
      let multimatterList: any[] = []
      this.selectedItems4.forEach((a: any) => multimatterList.push(a.id))
      this.searhform.patchValue({ matterNumber: this.selectedItems4 });
    }

    if (this.selectedItems3 && this.selectedItems3.length > 0) {
      let multicasecatList: any[] = []
      this.selectedItems3.forEach((a: any) => multicasecatList.push(a.id))
      this.searhform.patchValue({ caseCategoryId: this.selectedItems3 });
    }

    if (this.selectedItems5 && this.selectedItems5.length > 0) {
      let multicaseinfoList: any[] = []
      this.selectedItems5.forEach((a: any) => multicaseinfoList.push(a.id))
      this.searhform.patchValue({ caseInformationNo: this.selectedItems5 });
    }

    this.spin.show();

    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.statusId.url,
      this.cas.dropdownlist.setup.caseCategoryId.url,
    ]).subscribe((results) => {
      // this.classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);
      this.statuslist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key);
      this.caseCategoryIdList = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.caseCategoryId.key);
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: GeneralMatterElement[]) => {
        res.forEach((x) => {
          // x.classId = this.classIdList.find(y => y.key == x.classId)?.value;
          x.statusId = this.statuslist.find(y => y.key == x.statusId)?.value;
          x.caseCategoryId = this.caseCategoryIdList.find(y => y.key == x.caseCategoryId)?.value;
        })

        this.ELEMENT_DATA = res;


        this.dataSource = new MatTableDataSource<GeneralMatterElement>(this.ELEMENT_DATA.sort((a, b) => (a.matterNumber > b.matterNumber) ? -1 : 1));
        this.selection = new SelectionModel<GeneralMatterElement>(true, []);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }, (err) => {
      this.toastr.error(err, "");
    });
  }
  reset() {
    this.searhform.reset();
    let code = this.route.snapshot.params.code;

    if (code) {
      let js = this.cs.decrypt(code).code;
      this.searhform.controls.clientId.patchValue([js]);
    }
  }

  newcaseinfosheet() {
    this.spin.show();
    this.sub.add(this.serviceClient.Get(this.searhform.controls.clientId.value).subscribe((res) => {
      let paramdata = "";
      if (res.classId == '2'){

        paramdata = this.cs.encrypt({ pageflow: 'New', clientId: res.clientId });
        console.log(paramdata)
        this.router.navigate(['/main/matters/case-info/immigrationnew/' + paramdata]);
      }

      else{
        paramdata =  this.cs.encrypt({ pageflow: 'New', clientId: res.clientId });
       
        this.router.navigate(['/main/matters/case-info/landenew/' + paramdata]);
      }

      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  goToMatter(type: any): void {
    if (this.selection.selected.length === 0) {
      this.toastr.error("Kindly select any Row", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    let paramdata = "";
    sessionStorage.removeItem('matter');
    if (this.selection.selected.length > 0) {
      paramdata = this.cs.encrypt({ code: this.selection.selected[0].matterNumber, code1: this.selection.selected[0].matterDescription, pageflow: type });
      sessionStorage.setItem('matter', paramdata);
      this.router.navigate(['/main/matters/case-management/matter/' + paramdata]);
    }
  }
}
