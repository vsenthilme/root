import { Component, Injectable, OnInit, ViewChild } from '@angular/core';





import {
  defaultStyle
} from "../../../../config/customStylesNew";
import pdfMake from "pdfmake/build/pdfmake";
import {
  logo
} from "../../../../../assets/font/logo.js"
import pdfFontsNew from "../../../../../assets/font/vfs_fonts.js";
import {
  fonts1
} from "../../../../config/pdfFontsNew"
import { SummaryService } from '../summary/summary.service';
import { SelectionModel } from '@angular/cdk/collections';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router, ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { ConfirmComponent } from 'src/app/common-field/dialog_modules/confirm/confirm.component';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ExcelService } from 'src/app/common-service/excel.service';
import { AuthService } from 'src/app/core/core';
import { ApprovalService } from '../approval/approval.service';
import { OwnershipService } from '../ownership/ownership.service';
pdfMake.fonts = fonts1;
pdfMake.vfs = pdfFontsNew.pdfMake.vfs;


@Injectable({
  providedIn: 'root'
})

@Component({
  selector: 'app-admin-approval',
  templateUrl: './admin-approval.component.html',
  styleUrls: ['./admin-approval.component.scss']
})
export class AdminApprovalComponent implements OnInit {

  screenid = 1021;
  public icon = 'expand_more';
  sub = new Subscription();
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  ELEMENT_DATA: any[] = [];
  constructor(public dialog: MatDialog,
    private service: OwnershipService,
    private storePartnerListring: ApprovalService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private router: Router,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService,
    private summary: SummaryService,
    private route: ActivatedRoute) {}
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
  search() {
    this.spin.show();
    this.sub.add(this.service.search(this.searhform.getRawValue()).subscribe((res: any[]) => {
      console.log(res)
      this.dataSource = new MatTableDataSource < any > (res);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
      this.spin.hide();
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
    }));
  }
  displayedColumns  = ['select', 'action','languageId', 'companyId', 'requestId', 'storeId', 'createdBy', 'createdOn'];
  dataSource = new MatTableDataSource < any > (this.ELEMENT_DATA);
  selection = new SelectionModel < any > (true, []);

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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.requestId + 1}`;
  }
  RA: any = {};
  js: any = {}
  ngOnInit(): void {
    this.getAllListData();
  }

  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }

  @ViewChild(MatSort, {
    static: true
  })
  sort: MatSort;
  @ViewChild(MatPaginator, {
    static: true
  })
  paginator: MatPaginator; // Pagination

  multiselectyseridList: any[] = [];
  multiyseridList: any[] = [];
  multicasecatList: any[] = [];

  multiselectclassList: any[] = [];
  multilanguageList: any[] = [];
  multicompanyList: any[] = [];
  multigrouptypeList: any[] = [];
  multigroupidList: any[] = [];
  multirequestList: any[]=[];
  multistoreList: any[]=[];

  getall(excel: boolean = false) {

    let obj: any = {};
      obj.statusId = [4];
    this.spin.show();
    this.sub.add(this.service.search(obj).subscribe((res: any[]) => {
      console.log(res)
      this.dataSource = new MatTableDataSource < any > (res);
      this.dataSource.paginator = this.paginator;
      this.spin.hide();
      res.forEach((x: {
        languageId: string
      }) => this.multilanguageList.push({
        value: x.languageId,
        label: x.languageId
      }))
      this.multilanguageList = this.cas.removeDuplicatesFromArrayNew(this.multilanguageList);
      res.forEach((x: {
        companyId: string
      }) => this.multicompanyList.push({
        value: x.companyId,
        label: x.companyId
      }))
      this.multicompanyList = this.cas.removeDuplicatesFromArrayNew(this.multicompanyList);
      res.forEach((x: {
        requestId: string
      }) => this.multirequestList.push({
        value: x.requestId,
        label: x.requestId
      }))
      this.multirequestList = this.cas.removeDuplicatesFromArrayNew(this.multirequestList);
      res.forEach((x: {
        storeId: string,
        storeName: string
      }) => this.multistoreList.push({
        value: x.storeId,
        label: x.storeId + '-' + x.storeName
      }))
      this.multistoreList = this.cas.removeDuplicatesFromArrayNew(this.multistoreList);
      res.forEach((x: {
        createdBy: string;
      }) => this.multiyseridList.push({
        value: x.createdBy,
        label: x.createdBy
      }))
      this.multiselectyseridList = this.multiyseridList;
      this.multiselectyseridList = this.cas.removeDuplicatesFromArrayNew(this.multiyseridList);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
    }));
  }
  downloadexcel() {
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        'Language ID': x.languageId,
        'Company ID': x.companyId,
        "Request ID": x.requestId,
        "Store ID": x.storeId +'-'+x.storeName,
        'Created By': x.createdBy,
        'Created On': this.cs.dateapi(x.createdOn)
      });

    })
    this.excel.exportAsExcel(res, "Approval");
  }
  getAllListData() {
    this.getall();
  }
  searhform = this.fb.group({
    languageId: [],
    companyId: [],
    storeId: [],
    statusId:[[4]],
    requestId: [],
    createdBy: [],
    startCreatedOn:[],
    endCreatedOn:[],
  });



  clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }
  reset() {
    this.searhform.reset();
    this.searhform.controls.statusId.patchValue([4]);
  }


  Approve(element, action): void {
    const dialogRef = this.dialog.open(ConfirmComponent, {
      disableClose: true,
      width: '40%',
      maxWidth: '80%',
      position: {
        top: '6.5%'
      },
      data: {title: "Confirm", message:  action == 'Approve' ? "Are you sure you want to approve this record?" : "Are you sure you want to reject this record?"}
    });
    dialogRef.afterClosed().subscribe(result => {
      if(result && action == "Approve"){
        this.spin.show();
        element.statusId = 5;
        this.storePartnerListring.Create(element).subscribe(res =>{
          this.toastr.success(element.requestId + " approved successfully!", "Notification", {
            timeOut: 2000,
            progressBar: false,
          });
          this.service.Update(element, element.requestId, element.languageId, element.companyId).subscribe(res =>{

            this.summary.reportPdf().subscribe(summaryRes => {
            //  this.generatePdf(summaryRes);
              this.spin.hide();
            }, err => {
              this.cs.commonerror(err);
              this.spin.show();
            })

          }, err =>{
            this.cs.commonerrorNew(err);
            this.spin.hide();
          })
          
          this.router.navigate(['/main/controlgroup/transaction/summary'])
          this.spin.hide();
        }, err =>{
          this.cs.commonerrorNew(err);
          this.spin.hide();
        })
      
      }
      if(result && action == "Reject"){
        this.spin.show();
        element.statusId = 1;
        element.groupId = null;
        element.groupName = null;
        this.service.Update(element, element.requestId, element.languageId, element.companyId).subscribe(res =>{
          this.toastr.success(element.requestId + " rejected successfully!", "Notification", {
            timeOut: 2000,
            progressBar: false,
          });
        //  this.router.navigate(['/main/controlgroup/transaction/approval'])
        let paramdata = '';
        paramdata = this.cs.encrypt({ line: res});
        this.router.navigate(['/main/controlgroup/transaction/ownership/' +  paramdata])
          this.spin.hide();
        }, err =>{
          this.cs.commonerrorNew(err);
          this.spin.hide();
        })
      }
    });
    
  }


  groupId(element, pageflow: any): void {
    let paramdata = "";
    paramdata = this.cs.encrypt({line: element, pageflow: pageflow});
    this.router.navigate(['/main/controlgroup/transaction/proposal/' + paramdata]);
    
  }


  validate(element, pageflow){
    let paramdata = "";
    paramdata = this.cs.encrypt({ code: element, pageflow: pageflow });
    this.router.navigate(['/main/controlgroup/transaction/brotherSisterRemplate/' + paramdata]);
  }


  review(element){
    let paramdata1 = "";
    paramdata1 = this.cs.encrypt({code: element, line: element, pageflow: 'Display', pageflow1: 'Approve'});
    sessionStorage.setItem('controlGroupsSummary1', paramdata1);
    let paramdata = '';
    paramdata = this.cs.encrypt({ line: element, code: element, pageflow: 'Display', pageflow1: 'Approve',pageflow2:'Approval1' });
    this.router.navigate(['/main/controlgroup/transaction/proposal1/' +  paramdata])
  }

  generatePdf(element: any, lines: any) {
    var dd: any;

    let headerTable: any[] = [];
    let date = this.cs.todayapi();
    headerTable.push([{
      image: logo.headerLogo,
      fit: [100, 100],
      bold: true,
      fontSize: 10,
      border: [false, false, false, false],
      margin: [10, 0, 0]
    },
    // { text: '', bold: true, fontSize: 10, border: [false, false, false, false] },
    //    { text: 'Monty & Ramirez LLP \n 150 W Parker Road, 3rd Floor Houston, TX 77076',  alignment: 'center', fontSize: 10, border: [false, false, false, false] },
    {
      text: 'Check & Credit Card Request Form',
      bold: true,
      alignment: "center",
      fontSize: 13,
      margin: [10, 20, 10, 0],
      border: [false, false, false, false],
      color: '#666362'
    },
    {
      text: 'ddd',
      bold: true,
      alignment: "center",
      fontSize: 10,
      margin: [10, 20, 10, 0],
      border: [false, false, false, false],
      color: '#666362'
    },

    ]);

    dd = {
      pageSize: "A4",
      pageOrientation: "portrait",
      pageMargins: [40, 95, 40, 60],
      defaultStyle,
      header(): any {
        return [{
          columns: [{
            stack: [{
              image: logo.headerLogo,
              fit: [100, 100]
            }]
          },
          {
            stack: [{
              text: 'Request Summary',
              bold: true,
              // alignment: "center",
              fontSize: 13,
              margin: [10, 20, 10, 0],
              border: [false, false, false, false],
              color: '#666362'
            },],
            width: 300
          },
            // {
            //   stack: [{
            //     text: 'Run Date: ', // + '' +  (new Date().getDate()) +'-'+ (new Date().getMonth() + 1) + '-' + new Date().getFullYear() + '_' + this.cs.timeFormat(new Date()),
            //     alignment: 'left',
            //     fontSize: 7,
            //     color: '#666362'
            //   },

            //   ],
            //   width: 170
            // }
          ],
          margin: [40, 20]
        }]
      },
      footer(currentPage: number, pageCount: number): any {
        return [{
          text: date,
          bold: false,
          border: [false, false, false, false],
          alignment: 'right',
          fontSize: 9,
          margin: [30, 10, 0, 10]
        },
        {
          text: 'Page ' + currentPage + ' of ' + pageCount,
          style: 'header',
          alignment: 'center',
          bold: false,
          fontSize: 6,
          margin: [10, 5, -5, 0]
        }

          // { image: res.requirement == "Packing & Moving" ? workOrderLogo1.ulogistics1 : workOrderLogo1.ustorage, width: 570,   bold: false, margin: [10, -80, 0, 0], fontSize: 10, border: [false, false, false, false] },
        ]
      },
      content: ['\n'],
      //   defaultStyle,

    };


    let headerArray79: any[] = [];

    headerArray79.push([

      { text: 'Group Number', bold: true, fontSize: 10, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], border: [true, true, true, true] },
      { text: 'Name of the Group', bold: true, fontSize: 10, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], border: [true, true, true, true] }
    ]);

    element.groupIdList = element.groupIdList.sort((a, b) => (a.groupId > b.groupId) ? 1 : -1);

    element.groupIdList.forEach((data, i) => {
      headerArray79.push([
        { text: data.groupId, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], bold: false, fontSize: 10, border: [true, true, true, true], },
        { text: data.groupName, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], bold: false, fontSize: 10, border: [true, true, true, true] }
      ]);
      //   if ((i + 1) == res.quotationLine.length) {
      //   tableArray.push([
      //     { text: '', bold: false, fontSize: 10, border: [false, false, false, false],  },
      //     { text: '', bold: false, fontSize: 10, border: [false, false, false, false],  },
      //     { text: '', bold: false, fontSize: 10, border: [false, false, false, false],  },
      //     { text: '', bold: false, fontSize: 10, border: [false, false, false, false],  },
      //     { text: '', bold: false, fontSize: 10, border: [false, false, false, false],  },
      //     { text: 'Total:', borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], bold: true, fontSize: 10, border: [false, true, false, true],  },
      //     { text: res.quotationAmount != null ? '$ ' + this.decimalPipe.transform(res.quotationAmount, "1.2-2") : '$0.00', alignment: 'right', bold: true, fontSize: 10,  border: [false, true, true, true], borderColor: ['#ddd', '#ddd', '#ddd', '#ddd']},
      //   ]);
      // }
    });
    dd.content.push(
      // '\n',
      // {
      //   text: 'No of Stores',
      //   style: 'header',
      //   alignment: 'center',
      //   bold: true
      // },
      // '\n',
      {
        table: {
          headerRows: 1,
          widths: [80, '*'],
          body: headerArray79,
        },
        margin: [-3, 5, 0, 0]
      }
    )
    dd.content.push(
      {
        stack: [{
          text: '',
          alignment: 'center',
          fontSize: 12,
          lineHeight: 1.3,
          bold: true,
          pageBreak: 'after',
        }]
      })



    element.storeLists.forEach((data, i) => {
      dd.content.push({
        stack: [
          { text: data.groupId + '- ' + data.groupName, alignment: 'center', bold: true, fontSize: 12, margin: [0, 30, 0, -20] }
        ]
      }, '\n\n')
      let headerArray80: any[] = [];
      headerArray80.push([
        { text: 'Store Id', bold: true, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], fontSize: 10, border: [true, true, true, true], },
        { text: 'Store Name', bold: true, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], fontSize: 10, border: [true, true, true, true], },
        { text: '# of Full-time/Full-time equivalent Employees', bold: true, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], fontSize: 10, border: [true, true, true, true] }
      ]);
      data.stores.forEach((storeData, j) => {
        headerArray80.push([
          { text: storeData.storeId, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], bold: false, fontSize: 10, border: [true, true, true, true], },
          { text: storeData.storeName, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], bold: false, fontSize: 10, border: [true, true, true, true], },
          { text: '', borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], bold: false, fontSize: 10, border: [true, true, true, true] }
        ]);

        if ((j + 1) == data.stores.length) {
          headerArray80.push([
            { text: '', bold: false, fontSize: 10, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], },
            { text: 'Total Employees', bold: false, fontSize: 10, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], },
            { text: '', bold: false, fontSize: 10, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], }
          ]);
        }

      })
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [40,'*', '*'],
            body: headerArray80
          },

          margin: [-3, 0, 0, 0]
        })
      dd.content.push(
        {
          stack: [{
            text: '',
            alignment: 'center',
            fontSize: 12,
            lineHeight: 1.3,
            bold: true,
            pageBreak: 'after',
          }]
        })
    });

  //  pdfMake.createPdf(dd).download('Store_Group_Report');
    //pdfMake.createPdf(dd).open();
    const pdfDocGenerator = pdfMake.createPdf(dd);
    pdfDocGenerator.getBlob((blob) => {
      var file = new File([blob], lines.requestId + "_Store_Group_Report" + ".pdf"); //  + (new Date().getDate()) +'-'+ (new Date().getMonth() + 1) + '-' + new Date().getFullYear() + '_' + this.cs.timeFormat(new Date()) +  
      if (file) {
        this.spin.show();
       
        this.service.uploadfile(file, 'sharepoint/qb').subscribe((resp: any) => {
          lines.referenceField1 =  resp.file;
          this.service.Update(lines, lines.requestId,lines.languageId , lines.companyId).subscribe(res => {
              // this.toastr.success("Email sent successfully!", "Notification", {
              //   timeOut: 2000,
              //   progressBar: false,
              // });
          //this.location.back();
          this.spin.hide();
          })
        });
      }
    });

  }
}
