import { SelectionModel } from "@angular/cdk/collections";
import { HttpErrorResponse } from "@angular/common/http";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { DomSanitizer } from "@angular/platform-browser";
import { Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { InvoiceService } from "../../invoice/invoice.service";
import { MatterService } from "../../matters/matter.service";
import { DocumentTemplateService } from "../document-template.service";
import { DownloadConfirmComponent } from "../download-confirm/download-confirm.component";
import { UploadmatterComponent } from "./uploadmatter/uploadmatter.component";

// export interface ordermanagement {
//   lineno: string;
//   partner: string;
//   product: string;
//   date:string;
//   }

//   const ELEMENT_DATA: ordermanagement[] = [
//   { lineno: '300001 -01', partner: 'Aldi Arias Lopez - PERM', product: 'Pending',date: '02-02-2022',},
//   { lineno: '300001 -02', partner: 'Aldi Arias Lopez - PERM', product: 'In progress',date: '02-02-2022',},
//   { lineno: '300001 -03', partner: 'Aldi Arias Lopez - PERM', product: 'Sent',date: '02-02-2022',},
//   { lineno: '300001 -04', partner: 'Aldi Arias Lopez - PERM', product: 'Sent',date: '02-02-2022',},
//   { lineno: '300001 -04', partner: 'Aldi Arias Lopez - PERM', product: 'Sent',date: '02-02-2022',},
//   { lineno: '300001 -04', partner: 'Aldi Arias Lopez - PERM', product: 'Sent',date: '02-02-2022',},
//   { lineno: '300001 -04', partner: 'Aldi Arias Lopez - PERM', product: 'Sent',date: '02-02-2022',},

//   ];
@Component({
  selector: 'app-documentsfrom-mr',
  templateUrl: './documentsfrom-mr.component.html',
  styleUrls: ['./documentsfrom-mr.component.scss']
})
export class DocumentsfromMRComponent implements OnInit {

  sub = new Subscription();
  ELEMENT_DATA: any[] = [];
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  public icon = 'expand_more';
  constructor(public dialog: MatDialog,
    private service: DocumentTemplateService,
    private matterService: MatterService,
    // private cas: CommonApiService,
    public toastr: ToastrService,
    private router: Router,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    // private excel: ExcelService,
    private sanitizer: DomSanitizer,
    private fb: FormBuilder,
    public auth: AuthService) { } ///YETTOBEPROD
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
  upload() {
    console.log(this.matterList)
    const dialogRef = this.dialog.open(UploadmatterComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: this.matterList
    });

    dialogRef.afterClosed().subscribe(() => {
    //  window.location.reload();
    this.getAll();
    });
  }

  matterList: any[] = [];

  displayedColumns: string[] = ['lineno', 'partner', 'product','createdOn', 'uploadedBy', 'no'];
  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
  selection = new SelectionModel<any>(true, []);

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
   // this.getDropdown();
    this.getAll();

  }



  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }
  @ViewChild(MatSort, { static: true })
  sort!: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator!: MatPaginator; // Pagination
  // Pagination

  clientList: any;
  multiselectclientList: any[] = [];
  multiclientList: any[] = [];

  docList: any;
  multiselectdocList: any[] = [];
  multidocList: any[] = [];


  getDropdown() {
    this.sub.add(this.service.GetallDocName().subscribe(res => {
      this.docList = res;
      this.docList.forEach((x: { documentNumber: string; documentFileDescription: string; }) => this.multidocList.push({ id: x.documentNumber, itemName: x.documentFileDescription }))

    //  this.spin.hide();
    },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));

  }

  getAll() {
    this.spin.show();
    this.sub.add(this.service.findMatterDocument({clientId: [this.auth.clientId]}).subscribe((res: any[]) => {
      this.sub.add(this.matterService.filterMatter( {clientId: [this.auth.clientId] }).subscribe(matterListData => {
        this.clientList = matterListData; //.filter((x: any) => x.isUploaded != "U");
        this.clientList.forEach((x: { matterNumber: string; matterDescription: string; }) => this.multiclientList.push({ id: x.matterNumber, itemName: x.matterDescription }))
        let filteredData: any[] = [];
        res.forEach((x) => {
         // if(x.clientId == this.auth.clientId){
            x['matterName'] = this.multiclientList.find(y => y.id == x.matterNumber)?.itemName;
           // x.documentNo = this.multidocList.find(z => z.id == x.documentNo)?.itemName;
            filteredData.push(x);
        //  }
        })
        let result = filteredData.filter((x: any) => x.referenceField5 == "Client Portal" || x.referenceField2 == 'CLIENTPORTAL');
    
        this.matterList =result;
        this.dataSource = new MatTableDataSource<any>(result);
        this.selection = new SelectionModel<any>(true, []);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
        this.spin.hide();
      },
        err => {
          this.cs.commonerror(err);
          this.spin.hide();
        }));


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

  clearselection(row: any) {
    this.selection.clear();
    this.selection.toggle(row);
  }


  open_link(data: any): void {
    // if(!data.documentUrl.includes('.pdf') && !data.documentUrl.includes('.docx') && !data.documentUrl.includes('.dotx') && !data.documentUrl.includes('.doc')){
    //   console.log('png')
    //   this.fill(data);
    // }else{
    // this.router.navigate(['main/documents/documentpdf/' + this.cs.encrypt({ code: data.matterDocumentId,})]);
    // }
  //  this.fill(data);
    this.router.navigate(['main/documents/documentpdf/' + this.cs.encrypt({ code: data.matterDocumentId,})]);
  }


  fileUrldownload: any;
  docurl: any
  async fill(code: any) {
    console.log(code)
    this.spin.show();
    this.sub.add(this.service.GetmatterDocument(code.matterDocumentId).subscribe(async ress => {
       let locationfile = 'clientportal/' + ress.clientId + '/' + ress.matterNumber;
          let classId =  this.auth.classId;
          let fileName = !ress.documentUrl ? '' : ress.documentUrl;
          const blob = await this.service.getfile1(classId,  window.encodeURIComponent(fileName), locationfile)
          .catch((err: HttpErrorResponse) => {

            this.cs.commonerror(err);
            fileName = "";
          });
        this.spin.hide();

        if (blob) {
          const blobOb = new Blob([blob], {
            type: "application/vnd.openxmlformats-officedocument.wordprocessingml.document",

          });
          this.fileUrldownload = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blobOb));
          this.docurl = window.URL.createObjectURL(blob);
          this.downloadConfirm(code)
        }
   


  }, err => {
    this.cs.commonerror(err);
    this.spin.hide();
  }));


}


downloadConfirm(element): void {
  const dialogRef = this.dialog.open(DownloadConfirmComponent, {
    disableClose: true,
    width: '40%',
    maxWidth: '80%',
    position: { top: '9%', },
    data: { url: this.fileUrldownload, fileName: element.documentUrl}
  });

  dialogRef.afterClosed().subscribe(result => {
   // this.search();
  });
}

}



