import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { DocumentTemplateService } from "../document-template.service";

@Component({
  selector: 'app-documents',
  templateUrl: './documents.component.html',
  styleUrls: ['./documents.component.scss']
})
export class DocumentsComponent implements OnInit {

  displayedColumns: string[] = ['lineno', 'updatedOn', 'product', 'no',];
  dataSource = new MatTableDataSource<any>();
  selection = new SelectionModel<any>(true, []);
  sub = new Subscription();


  constructor(
    private service: DocumentTemplateService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private auth: AuthService
  ) { }

  ngOnInit(): void {
    this.getAllCheckListDocuments();
  }
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  getAllCheckListDocuments() {
    let classIdList: any[] = [];
    let caseCategoryIdList: any[] = [];
    let caseSubCategoryIdList: any[] = [];

    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.classId.url,
      this.cas.dropdownlist.setup.caseCategoryId.url,
      this.cas.dropdownlist.setup.caseSubcategoryId.url,

    ]).subscribe((results) => {
      classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);
      caseCategoryIdList = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.caseCategoryId.key);
      caseSubCategoryIdList = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.setup.caseSubcategoryId.key);
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.searchMatterDocListHeader({ clientId: [this.auth.clientId] }).subscribe((res: any[]) => {
        res.forEach((x) => {
          x.classId = classIdList.find(y => y.key == x.classId)?.value;
          x.caseCategoryId = caseCategoryIdList.find(y => y.key == x.caseCategoryId)?.value;
          x.caseSubCategoryId = caseSubCategoryIdList.find(y => y.key == x.caseSubCategoryId)?.value;
        })
        this.dataSource.data = res;
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

}

