import { Component, OnInit, ViewChild } from '@angular/core';
import { ControlgroupService } from '../../../master/controlgroup/controlgroup.service';
import { AuthService } from 'src/app/core/core';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { ValidationService } from '../../validation/validation.service';
import { SelectionModel } from '@angular/cdk/collections';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { Location } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import { OwnershipService } from '../../ownership/ownership.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-merge',
  templateUrl: './merge.component.html',
  styleUrls: ['./merge.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({ height: '0px', minHeight: '0' })),
      state('expanded', style({ height: '*' })),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class MergeComponent implements OnInit {

  expandedElement: any | null;
  constructor(private existingGroupService: ControlgroupService, private auth: AuthService, private cas: CommonApiService, private spin: NgxSpinnerService,
    private groupService: ValidationService,
    private cs: CommonService, private route: ActivatedRoute,
    private location: Location, private toastr: ToastrService, private ownership: OwnershipService, private router: Router) { }

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

  multiGroupList: any[] = [];
  multiGroupList1: any[] = [];

  ELEMENT_DATA: any[] = [];
  displayedColumns: string[] = ['select', 'clientDoc', 'storeId', 'storeName',];
  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
  selection = new SelectionModel<any>(true, []);


  displayedColumns1: string[] = ['clientDoc', 'storeId', 'storeName',];
  dataSource1 = new MatTableDataSource<any>(this.ELEMENT_DATA);

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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.versionNumber + 1}`;
  }

  @ViewChild(MatSort, {
    static: true
  })
  sort: MatSort;
  @ViewChild(MatPaginator, {
    static: true
  })
  paginator: MatPaginator; // Pagination

  js: any = {}
  showTransfer = false;
  
  ngOnInit(): void {
    let code = this.route.snapshot.params.code;
    this.js = this.cs.decrypt(code);
    console.log(this.js)
    let obj: any = {};
    obj.languageId = [this.auth.languageId];
    obj.companyId = ['1000'];
    this.ownership.searchStorePartner(obj).subscribe(res => {
      res.forEach((x: { groupId: string; groupName: string; }) => this.multiGroupList.push({ value: x.groupId, label: x.groupId + '- ' + x.groupName, description: x.groupName }))
      this.multiGroupList = this.cas.removeDuplicatesFromArrayNew(this.multiGroupList);

      this.sourceGroup = this.js.code.groupId;  
      this.multiGroupList1 = this.multiGroupList.filter(
        option => option.value !== this.sourceGroup
      );

      this.dataSource = new MatTableDataSource<any>([this.js.code]);
      this.showTransfer = true;
    })
  }

  groupSelected1(value) {
    this.dataSource1.data = [];
    this.targetGroupName = value.description;

    let obj: any = {};
    obj.languageId = [this.auth.languageId];
    obj.groupId = [value.value];
    obj.companyId = ['1000']; //ff
    this.spin.show();
    this.ownership.searchStorePartner(obj).subscribe(res => {
      this.dataSource1 = new MatTableDataSource<any>(res);
      this.spin.hide();
    }, err => { 
      this.cs.commonerror(err);
      this.spin.hide();
    })
  }

  clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }

  showSave = false;
  merge() {

    if (this.selection.selected.length == 0) {
      this.toastr.error(
        "Please select the store to be transferred to continue",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );
      this.spin.hide();
      this.cs.notifyOther(true);
      return;
    }

    if (this.targetGroup == null || this.targetGroup == undefined) {
      this.toastr.error(
        "Please select the target the group to continue",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );
      this.spin.hide();
      this.cs.notifyOther(true);
      return;
    }

   // this.dataSource1 = new MatTableDataSource<any>(this.selection.selected);

   //this.dataSource1.data.push(this.selection.selected[0]);
   this.selection.selected[0].referenceField10 = true;
   console.log(this.selection.selected[0])
   this.dataSource1.data.splice(0, 0, this.selection.selected[0]);
   this.dataSource1._updateChangeSubscription();
   this.showSave = true;
  }

  back() {
    this.location.back();
  }

  targetGroupName: any;
  targetGroup: any;
  sourceGroup: any;
  submit() {
    this.spin.show();
    if (this.sourceGroup == null || this.sourceGroup == undefined) {
      this.toastr.error(
        "Please select the source the group to continue",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );
      this.spin.hide();
      this.cs.notifyOther(true);
      return;
    }
    if (this.targetGroup == null || this.targetGroup == undefined) {
      this.toastr.error(
        "Please select the target the group to continue",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );
      this.spin.hide();
      this.cs.notifyOther(true);
      return;
    }

    this.selection.selected.forEach(element => {
      element['groupId'] = this.targetGroup;
      element['groupName'] = this.targetGroupName;
      element['companyId'] = '1000';
      element['languageId'] = this.auth.languageId;
      element['statusId'] = 6;


      this.ownership.Create(element).subscribe(res => {
        this.toastr.success(res.requestId + "request saved successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();


        let paramdata1 = "";
        paramdata1 = this.cs.encrypt({ line: res, pageflow: 'transfer' });
        sessionStorage.setItem('controlGroupsSummary', paramdata1);

        let paramdata = "";
        paramdata = this.cs.encrypt({ line: res, pageflow: 'transfer' });
        this.router.navigate(['/main/controlgroup/transaction/proposed/' + paramdata]);

      })
    }, err => {
      this.spin.show();
      this.cs.commonerror(err);
    });
  }
  
  clientList: any[] = [];
  totalPercentage = 0;
  showClient(e) {
    this.totalPercentage = 0;
    this.clientList = [];
    this.clientList.push(e);
    let total = 0;
    total = total + (e.coOwnerPercentage1 != null ? e.coOwnerPercentage1 : 0) + (e.coOwnerPercentage2 != null ? e.coOwnerPercentage2 : 0) +
      (e.coOwnerPercentage3 != null ? e.coOwnerPercentage3 : 0) + (e.coOwnerPercentage4 != null ? e.coOwnerPercentage4 : 0) +
      (e.coOwnerPercentage5 != null ? e.coOwnerPercentage5 : 0) + (e.coOwnerPercentage6 != null ? e.coOwnerPercentage6 : 0)
      + (e.coOwnerPercentage7 != null ? e.coOwnerPercentage7 : 0) + (e.coOwnerPercentage8 != null ? e.coOwnerPercentage8 : 0)
      + (e.coOwnerPercentage9 != null ? e.coOwnerPercentage9 : 0) + (e.coOwnerPercentage10 != null ? e.coOwnerPercentage10 : 0);

    this.totalPercentage = total;
  }
}

