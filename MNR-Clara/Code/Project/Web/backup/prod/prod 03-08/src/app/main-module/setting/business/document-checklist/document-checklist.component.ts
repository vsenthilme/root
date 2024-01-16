import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { documentTemplateService } from "../document-template/document-template.service";
import { ChecklistNewComponent } from "./checklist-new/checklist-new.component";

export interface dropdownelement {
  key: any;
  value: any;
  referenceField?: any;
}
interface SelectItem {
  id: string;
  itemName: string;
}

@Component({
  selector: 'app-document-checklist',
  templateUrl: './document-checklist.component.html',
  styleUrls: ['./document-checklist.component.scss']
})
export class DocumentChecklistComponent implements OnInit {

  screenid = 1128;
  public icon = 'expand_more';
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  sub = new Subscription();

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
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }
  constructor(
    public dialog: MatDialog,
    private service: documentTemplateService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService
  ) { }
  new(): void {

    const dialogRef = this.dialog.open(ChecklistNewComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { type: 'New' }
    });

    dialogRef.afterClosed().subscribe(result => {
      this.getAllCheckListDocuments();
    });
  }
  RA: any = {};
  //  StatusList: dropdownelement[] = [{ key: 'Active', value: 'Active' }, { key: 'Inactive', value: 'Inactive' }]
  ngOnInit(): void {
    this.RA = this.auth.getRoleAccess(this.screenid);
    this.getAllCheckListDocuments();


  }

  displayedColumns: string[] = ['select', 'classId', 'checkListNo', 'caseCategory', 'caseSub', 'statusId'];
  dataSource = new MatTableDataSource<any>();
  selection = new SelectionModel<any>(true, []);

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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.documenttype + 1}`;
  }
    clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }


  selectedItems2: SelectItem[] = [];
  multiselectstatusList: any[] = [];
  multistatusList: any[] = [];

  selectedItems5: any[] = [];
  multiselectcasecatList: any[] = [];
  multicasecatList: any[] = [];

  selectedItems8: any[] = [];
  multiselectcasesubList: any[] = [];
  multicasesubList: any[] = [];


  selectedItems3: SelectItem[] = [];
  multiselectclassList: any[] = [];
  multiclassList: any[] = [];

  selectedItems10: SelectItem[] = [];
  multiselectchecklistList: any[] = [];
  multichecklistList: any[] = [];

  dropdownSettings = {
    singleSelection: false,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };


  clientcatlist: any[] = [];
  quotationlist: any[] = [];
  userTypeList: any[] = [];


classIdList: any[] = [];
caseCategoryIdList: any[] = [];
caseSubCategoryIdList: any[] = [];
statuslist: any[] = [];
checklist: any[] = [];

  getAllCheckListDocuments() {
    // let classIdList: any[] = [];
    // let caseCategoryIdList: any[] = [];
    // let caseSubCategoryIdList: any[] = [];
    // let statuslist: any[] = [];
    // let checklist: any[] = [];


    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.classId.url,
      this.cas.dropdownlist.setup.caseCategoryId.url,
      this.cas.dropdownlist.setup.caseSubcategoryId.url,
      this.cas.dropdownlist.setup.statusId.url,
      this.cas.dropdownlist.setup.checklist.url,
    ]).subscribe((results) => {
      this.classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);
      this.classIdList.forEach((x: { key: string; value: string; }) => this.multiclassList.push({ value: x.key, label: x.key + '-' + x.value }))
      this.multiselectclassList = this.multiclassList;

      this.caseCategoryIdList = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.caseCategoryId.key);
      this.caseCategoryIdList.forEach((x: { key: string; value: string; }) => this.multicasecatList.push({ value: x.key, label: x.key + '-' + x.value }))
      this.multiselectcasecatList = this.multicasecatList;

      this.caseSubCategoryIdList = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.setup.caseSubcategoryId.key);
      this.caseSubCategoryIdList.forEach((x: { key: string; value: string; }) => this.multicasesubList.push({ value: x.key, label: x.key + '-' + x.value }))
      this.multiselectcasesubList = this.multicasesubList;


      this.statuslist = this.cas.foreachlist_searchpage(results[3], this.cas.dropdownlist.setup.statusId.key).filter(s => [18, 21].includes(s.key));
      this.statuslist.forEach((x: { key: string; value: string; }) => this.multistatusList.push({ value: x.key, label: x.key + '-' + x.value }))
      this.multiselectstatusList = this.multistatusList;

      this.checklist = this.cas.foreachlist_searchpage(results[4], this.cas.dropdownlist.setup.checklist.key);
      this.checklist.forEach((x: { key: string; value: string; }) => this.multichecklistList.push({ value: x.key, label: x.value }))
      this.multiselectchecklistList = this.multichecklistList;



      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.getChecklistDocument().subscribe((res: any[]) => {
        res.forEach((x) => {
          x['classIdName'] = this.classIdList.find(y => y.key == x.classId)?.value;
          x['caseCategoryIdName'] = this.caseCategoryIdList.find(y => y.key == x.caseCategoryId)?.value;
          x['caseSubCategoryIdName'] = this.caseSubCategoryIdList.find(y => y.key == x.caseSubCategoryId)?.value;
        })
        if (res.length > 0) {
          let grouped = this.groupByData(res, (data: any) => data.checkListNo);

          let checkListArray: any[] = [];
          let groupByCheckListArray: any[] = [];

          res.forEach((resData, i) => {
            checkListArray.push(resData.checkListNo);
          })
          let uniqueArray = this.removeDuplicateInArray(checkListArray);
          if (uniqueArray.length > 0) {
            uniqueArray.forEach(number => {
              groupByCheckListArray.push(grouped.get(number)[0])
            })
          }
          this.dataSource.data = groupByCheckListArray;
        }

        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();
  }

  groupByData(list: any[], keyGetter: any) {
    let map = new Map();
    list.forEach((item) => {
      let key = keyGetter(item);
      let collection = map.get(key);
      if (!collection) {
        map.set(key, [item]);
      } else {
        collection.push(item);
      }
    });
    return map;
  }

  removeDuplicateInArray(checkListArray: any[]) {
    let uniqueArray = checkListArray.filter((cehckListNo, index) => {
      const _thing = JSON.stringify(cehckListNo);
      return index === checkListArray.findIndex(obj => {
        return JSON.stringify(obj) === _thing;
      });
    });
    return uniqueArray;
  }
  editDocument() {
    const dialogRef = this.dialog.open(ChecklistNewComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { type: 'Edit', documentData: this.selection.selected[0] }
    });

    dialogRef.afterClosed().subscribe(result => {
      this.getAllCheckListDocuments();
    });
  }

  searhform = this.fb.group({
    classId: [],
    classIdFE: [],
    caseCategoryId: [],
    caseSubCategoryId: [],
    caseCategoryIdFE: [],
    caseSubCategoryIdFE: [],
    checkListNo: [],
    checkListNoFE: [],
    startCreatedOn: [],
    endCreatedOn: [],
    statusId: [],
    statusIdFE: [],
  });
  Clear() {
    this.reset();
  };

  search() {
    // this.searhform.controls.startQuotationDate.patchValue(this.cs.day_callapiSearch(this.searhform.controls.endCreatedOn.value));
    // this.searhform.controls.endQuotationDate.patchValue(this.cs.day_callapiSearch(this.searhform.controls.endCreatedOn.value));

    // if (this.selectedItems2 && this.selectedItems2.length > 0) {
    //   let multistatusList: any[] = []
    //   this.selectedItems2.forEach((a: any) => multistatusList.push(a.id))
    //   this.searhform.patchValue({ caseCategoryId: this.selectedItems2 });
    // }

    // if (this.selectedItems5 && this.selectedItems5.length > 0) {
    //   let multicasecatList: any[] = []
    //   this.selectedItems5.forEach((a: any) => multicasecatList.push(a.id))
    //   this.searhform.patchValue({ statusId: multicasecatList });
    // }

    // if (this.selectedItems8 && this.selectedItems8.length > 0) {
    //   let multicasesubList: any[] = []
    //   this.selectedItems8.forEach((a: any) => multicasesubList.push(a.id))
    //   this.searhform.patchValue({ caseSubCategoryId: multicasesubList });
    // }

    // if (this.selectedItems3 && this.selectedItems3.length > 0) {
    //   let multiclassList: any[] = []
    //   this.selectedItems3.forEach((a: any) => multiclassList.push(a.id))
    //   this.searhform.patchValue({ classId: multiclassList });
    // }
    // if (this.selectedItems10 && this.selectedItems10.length > 0) {
    //   let multichecklistList: any[] = []
    //   this.selectedItems10.forEach((a: any) => multichecklistList.push(a.id))
    //   this.searhform.patchValue({ checkListNo: multichecklistList });
    // }

//     this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url,
//     this.cas.dropdownlist.setup.statusId.url,
//     this.cas.dropdownlist.client.clientId.url,
//     this.cas.dropdownlist.setup.caseCategoryId.url,
//     this.cas.dropdownlist.setup.caseSubcategoryId.url,
//     ]).subscribe((results) => {
//       this.classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);
//     this.statuslist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key);
// //    this.clientIdlist = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.client.clientId.key);
//       this.caseCategoryIdList = this.cas.foreachlist_searchpage(results[3], this.cas.dropdownlist.setup.caseCategoryId.key);
//       this.caseSubCategoryIdList = this.cas.foreachlist_searchpage(results[4], this.cas.dropdownlist.setup.caseSubcategoryId.key);
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: any[]) => {
        res.forEach((x) => {
          x['classIdName'] = this.classIdList.find(y => y.key == x.classId)?.value;
          x['caseCategoryIdName'] = this.caseCategoryIdList.find(y => y.key == x.caseCategoryId)?.value;
          x['caseSubCategoryIdName'] = this.caseSubCategoryIdList.find(y => y.key == x.caseSubCategoryId)?.value;
        })
        if (res.length > 0) {
          let grouped = this.groupByData(res, (data: any) => data.checkListNo);

          let checkListArray: any[] = [];
          let groupByCheckListArray: any[] = [];
          res.forEach((resData, i) => {
            checkListArray.push(resData.checkListNo);
          })
          let uniqueArray = this.removeDuplicateInArray(checkListArray);
          if (uniqueArray.length > 0) {
            uniqueArray.forEach(number => {
              groupByCheckListArray.push(grouped.get(number)[0])
            })
          }
          console.log(groupByCheckListArray)
          this.dataSource.data = groupByCheckListArray;
        }
        //  this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
        this.selection = new SelectionModel<any>(true, []);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
        this.spin.hide();
      }, (err: any) => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    // }, (err) => {
    //   this.toastr.error(err, "");
    // });
    this.spin.hide();


  }

  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Class": x.classIdName,
        "Checklist No": x.checkListNo,
        'Case Category': x.caseCategoryIdName,
        "Case Sub Category": x.caseSubCategoryIdName,
        'Status': x.statusId,
      });

    })
    this.excel.exportAsExcel(res, "Document Checklist");
  }


  reset() {
    this.searhform.reset();
  }

  deleteChecklist() {
    if (this.selection.selected.length > 0) {
      this.sub.add(this.service.deleteChecklist(this.selection.selected[0]).subscribe(res => {
        this.toastr.success("Document deleted successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();
        this.getAllCheckListDocuments();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    } else {
      this.toastr.error("Please select the document to delete", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
    }

  }

}