import { SelectionModel } from "@angular/cdk/collections";
import { HttpErrorResponse } from "@angular/common/http";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { DomSanitizer } from "@angular/platform-browser";
import { Router, ActivatedRoute } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DeleteComponent } from "src/app/common-field/dialog_modules/delete/delete.component";
import { NotesComponent } from "src/app/common-field/notes/notes.component";
import { CommonApiService, dropdownelement } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { FormService } from "src/app/customerform/form.service";
import { AgreementPopupNewComponent } from "src/app/main-module/crm/potential/agreement-popup-new/agreement-popup-new.component";
import { DisplayComponent } from "../display/display.component";
import { PotentialElement, PotentialService } from "../potential.service";

interface SelectItem {
  id: string;
  itemName: string;
}


@Component({
  selector: 'app-potential-snal',
  templateUrl: './potential-snal.component.html',
  styleUrls: ['./potential-snal.component.scss']
})
export class PotentialSnalComponent implements OnInit {

  screenid = 1075;
  public icon = 'expand_more';
  sub = new Subscription;
  ELEMENT_DATA: PotentialElement[] = [];
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination
  status = '';
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  filterintakeFormId: any;
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
  animal: string;
  name: string;
  constructor(private dialog: MatDialog, private sanitizer: DomSanitizer,
    private cs: CommonService,
    private router: Router,
    private excel: ExcelService,
    private fservice: FormService,
    private auth: AuthService,
    private service: PotentialService, private fb: FormBuilder,
    private cas: CommonApiService,
    private spin: NgxSpinnerService, public toastr: ToastrService,) { }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
  openNotes(data: any): void {

    const dialogRef = this.dialog.open(NotesComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      data: data
    });

    dialogRef.afterClosed().subscribe(result => {


    });
  }

  open_new_update(type: any) {

    if (this.selection.selected.length === 0) {
      this.toastr.error("Kindly select any Row", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }

    if (this.selection.selected[0].statusId == 17)
      type = "display";
    const dialogRef = this.dialog.open(DisplayComponent, {
      disableClose: true,
      width: '95%',
      maxWidth: '95%',
      position: { top: '6.5%' },
      data: { data: this.selection.selected[0], pageflow: type }
    });

    dialogRef.afterClosed().subscribe(result => {

      // this.getallationslist();
     window.location.reload();
    });
  }
  openDelete(): void {

    if (this.selection.selected.length === 0) {
      this.toastr.error("Kindly select any Row", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }

    if (this.selection.selected[0].statusId == 17 || this.selection.selected[0].statusId == 15) {

      this.toastr.error("Prospective Client can't be Deleted.", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    const dialogRef = this.dialog.open(DeleteComponent, {
      disableClose: true,
      width: '30%',
      maxWidth: '80%',
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.spin.show();
        this.sub.add(this.service.Delete(this.selection.selected[0].potentialClientId).subscribe(res => {

          this.spin.hide();
          this.spin.show();

          this.sub.add(this.fservice.Get(this.selection.selected[0].intakeFormNumber).subscribe(res => {

            this.spin.hide();
            this.spin.show();
            res.statusId = 8;
            this.sub.add(this.fservice.Update(this.selection.selected[0].intakeFormNumber, res).subscribe(res => {

              this.toastr.success(this.selection.selected[0].potentialClientId + ' deleted successfully.', "Notification", {
                timeOut: 2000,
                progressBar: false,
              });

              this.spin.hide();
              //this.getallationslist();
              window.location.reload();
            }, err => {
              this.cs.commonerror(err);
              this.spin.hide();
            }));
          }, err => {
            this.cs.commonerror(err);
            this.spin.hide();
          }));


        }, err => {

          this.cs.commonerror(err);
          this.spin.hide();
        }));
      }
    });
  }
  statuslist: any[] = [];
  InquiryNoList: any[] = [];
  potentialList: any[] = [];
  intakeFormIdList: any[] = [];
  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Prospective Client": x.potentialClientId,
        "Inquiry No": x.inquiryNumber,
        'Name': x.firstNameLastName,
        "Email  ": x.emailId,
        "Phone ": x.contactNumber,
        'Created By': x.createdBy,
        "Onboarding Status  ": x.statusId_des,
        'Created Date': this.cs.dateapi(x.createdOn),
      });

    })
    this.excel.exportAsExcel(res, "Prospective client");
  }


  assignAttorneyList: any[] = [];


  selectedItems: any[] = [];
  multiselectassignAttorneyList: any[] = [];
  multiassignAttorneyList: any[] = [];

  selectedItems2: SelectItem[] = [];
  multiselectstatusList: any[] = [];
  multistatusList: any[] = [];

  selectedItems3: SelectItem[] = [];
  multiselectintakeIDList: any[] = [];
  multiintakeIDList: any[] = [];

  selectedItems4: SelectItem[] = [];
  multiselectInquirynoList: any[] = [];
  multiInquirynoList: any[] = [];


  selectedItems5: SelectItem[] = [];
  multiselectpotentialClientList: any[] = [];
  multipotentialClientList: any[] = [];


  dropdownSettings = {
    singleSelection: false,
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };


  getallationslist() {

    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.statusId.url,
      this.cas.dropdownlist.crm.inquiryNumber.url,
      this.cas.dropdownlist.crm.potentialClientId.url,
      this.cas.dropdownlist.setup.intakeFormId.url,
    ]).subscribe((results) => {
      this.statuslist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key).filter(s => [11, 12, 15, 19, 20, 17].includes(s.key));
      this.statuslist.forEach((x: { key: string; value: string; }) => this.multistatusList.push({value: x.key, label:  x.value}))
      this.multiselectstatusList = this.multistatusList;

      // this.InquiryNoList = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.crm.inquiryNumber.key);
      // this.InquiryNoList.forEach((x: { key: string; value: string; }) => this.multiInquirynoList.push({value: x.key, label:  x.value}))
      // this.multiselectInquirynoList = this.multiInquirynoList;

      // this.potentialList = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.crm.potentialClientId.key);
      // this.potentialList.forEach((x: { key: string; value: string; }) => this.multipotentialClientList.push({value: x.key, label:  x.value}))
      // this.multiselectpotentialClientList = this.multipotentialClientList;

      this.intakeFormIdList = this.cas.foreachlist_searchpage(results[3], this.cas.dropdownlist.setup.intakeFormId.key, { intakeFormId: this.filterintakeFormId });
      this.intakeFormIdList.forEach((x: { key: string; value: string; }) => this.multiintakeIDList.push({value: x.key, label:  x.key + '-' + x.value}))
    
      

      this.sub.add(this.service.Getall().subscribe((res: any[]) => {
        let filterintakelist : any[] = [];
        this.multiintakeIDList.forEach(element => {
     for(let i=0; i<res.length; i++){
      if(element.value == res[i].intakeFormId){
        filterintakelist.push(element);
        break
      }
     }   
        });

        this.multiselectintakeIDList = filterintakelist;

        console.log(this.filterintakeFormId)
       // filterintakeFormId = res.intakeFormId;
     
       
let result = res.filter((x : any) => x.statusId != 17)

result.forEach((x: { potentialClientId: string;}) => this.multiselectpotentialClientList.push({value: x.potentialClientId, label: x.potentialClientId}))
this.multiselectpotentialClientList = this.cas.removeDuplicatesFromArrayNew(this.multiselectpotentialClientList);

result.forEach((x: { inquiryNumber: string;}) => this.multiselectInquirynoList.push({value: x.inquiryNumber, label: x.inquiryNumber}))
this.multiselectInquirynoList = this.cas.removeDuplicatesFromArrayNew(this.multiselectInquirynoList);


        this.ELEMENT_DATA = result;
        this.ELEMENT_DATA.forEach((x) => {
          x.statusId_des = this.statuslist.find(y => y.key == x.statusId)?.value;
        })


        this.assignedAttorneyList = [];

        const categories = this.ELEMENT_DATA.map(person => ({
          referenceField2: person.referenceField2,
        }));
        const distinctThings = categories.filter(
          (thing, i, arr) => arr.findIndex(t => t.referenceField2 === thing.referenceField2) === i
        );
        distinctThings.forEach(x => {

          this.assignedAttorneyList.push({ key: x.referenceField2, value: x.referenceField2 });
        });

        this.assignedAttorneyList.forEach((x: { key: string; value: string; }) => this.multiassignAttorneyList.push({value: x.key, label:  x.value}))
        this.multiselectassignAttorneyList = this.multiassignAttorneyList;

        this.dataSource = new MatTableDataSource<PotentialElement>(this.ELEMENT_DATA.sort((a, b) => (a.potentialClientId > b.potentialClientId) ? -1 : 1));
        this.selection = new SelectionModel<PotentialElement>(true, []);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
        this.spin.hide();

      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }, (err) => {
      this.spin.hide();
      this.toastr.error(err, "");
    });
  }


  open_Agreement(data: any): void {

    const dialogRef = this.dialog.open(AgreementPopupNewComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: data
    });

    dialogRef.afterClosed().subscribe(result => {


    });
  }
  open_link(data: any): void {
    this.router.navigate(['main/crm/agreementdocument/' + this.cs.encrypt({ agreementCode: data.agreementCode, potentialClientId: data.potentialClientId, pageflow: 'agreement' })]);

  }
  open_Upload(data: any): void {
    this.router.navigate(['main/crm/agreementdocument/' + this.cs.encrypt({ agreementCode: data.agreementCode, potentialClientId: data.potentialClientId, pageflow: 'upload' })]);

  }

  open_ClientCreation(data: any) {
    if (!this.selection.selected[0].clientCategoryId) {
      this.toastr.error("Client Category is mandatory for client creation.", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }

    this.spin.show();
    this.sub.add(this.service.ClientGeneral(data.potentialClientId).subscribe((res) => {
      console.log(res)
      this.toastr.success(res.clientId + ' Client created successfully.', "Notification", {
        timeOut: 2000,
        progressBar: false,
      });

      this.spin.hide();
      this.getallationslist();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }



  RA: any = {};
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);

    this.getallationslist();
  }
  displayedColumns: string[] = ['select', 'action', 'status', 'potentialClientId', 'inquiryNumber', 'firstNameLastName', 'emailId', 'contactNumber', 'intake', 'agreement', 'notes', 'createdBy', 'screatedOn',];
  dataSource = new MatTableDataSource<PotentialElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<PotentialElement>(true, []);

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
  checkboxLabel(row?: PotentialElement): string {
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

  open_Intake(data: any, type = 'Display') {
    let formname = this.cs.customerformname(data.intakeFormId);




    if (formname == '') {
      this.toastr.error(
        "Select from is invalid.",
        ""
      );

      this.cs.notifyOther(true);
      return;
    }
    this.cs.notifyOther(false);

    data.pageflow = type;
    this.router.navigate(['/main/crm/' + formname + '/' + this.cs.encrypt(data)]);

  }

  searchStatusList = {
    statusId: [11, 12, 15, 19, 20, 17]
  };
  assignedAttorneyList: dropdownelement[] = [];
  searhform = this.fb.group({

    assignedAttorney: [],
    assignedAttorneyFE:[],
    contactNumber: [],
    createdBy: [],
    econsultationDate: [],
    econsultationDateFE: [],
    ecreatedOn: [],
    ecreatedOnFE: [],
    emailId: [],
    firstNameLastName: [],
    inquiryNumber: [],
    inquiryNumberFE: [],
    intakeFormId: [],
    intakeFormIdFE: [],
    potentialClientId: [],
    potentialClientIdFE: [],
    sconsultationDate: [],
    sconsultationDateFE: [],
    screatedOn: [],
    screatedOnFE: [],
    statusId: [[17],],
    statusIdFE: [],
  });
  Clear() {
    this.reset();
  };
  search() {
    this.searhform.controls.screatedOn.patchValue(this.cs.dateNewFormat1(this.searhform.controls.screatedOnFE.value));
    this.searhform.controls.ecreatedOn.patchValue(this.cs.dateNewFormat1(this.searhform.controls.ecreatedOnFE.value));

    this.searhform.controls.econsultationDate.patchValue(this.cs.dateNewFormat1(this.searhform.controls.econsultationDateFE.value));
    this.searhform.controls.sconsultationDate.patchValue(this.cs.dateNewFormat1(this.searhform.controls.sconsultationDateFE.value));

    if (this.selectedItems2 && this.selectedItems2.length > 0){
      let multistatusList: any[]=[]
      this.selectedItems2.forEach((a: any)=> multistatusList.push(a.id))
      this.searhform.patchValue({statusId: this.selectedItems2 });
    }
    

    if (this.selectedItems && this.selectedItems.length > 0){
      let multiassignAttorneyList: any[]=[]
      this.selectedItems.forEach((a: any)=> multiassignAttorneyList.push(a.id))
      this.searhform.patchValue({assignedAttorney: this.selectedItems[0] });
    }
    
    if (this.selectedItems4 && this.selectedItems4.length > 0){
      let multiInquirynoList: any[]=[]
      this.selectedItems4.forEach((a: any)=> multiInquirynoList.push(a.id))
      this.searhform.patchValue({inquiryNumber: this.selectedItems4 });
    }

    if (this.selectedItems5 && this.selectedItems5.length > 0){
      let multipotentialClientList: any[]=[]
      this.selectedItems5.forEach((a: any)=> multipotentialClientList.push(a.id))
      this.searhform.patchValue({potentialClientId: this.selectedItems5 });
    }

    if (this.selectedItems3 && this.selectedItems3.length > 0){
      let multiintakeIDList: any[]=[]
      this.selectedItems3.forEach((a: any)=> multiintakeIDList.push(a.id))
      this.searhform.patchValue({intakeFormId: this.selectedItems3 });
    }

    // if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
    //   this.searhform.patchValue({assignedAttorney: this.selectedItems[0].id });
    // }

    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.statusId.url,

    ]).subscribe((results) => {
      this.statuslist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key);
      this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: PotentialElement[]) => {

      //  let result = res.filter((x : any) => x.statusId != 17)
      let result = res
        this.ELEMENT_DATA = result.filter(x => x.deletionIndicator == 0);
        this.ELEMENT_DATA.forEach((x) => {
          x.statusId_des = this.statuslist.find(y => y.key == x.statusId)?.value;
        })

        

        this.dataSource = new MatTableDataSource<PotentialElement>(this.ELEMENT_DATA.sort((a, b) => (a.potentialClientId > b.potentialClientId) ? -1 : 1));
        this.selection = new SelectionModel<PotentialElement>(true, []);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }, (err) => {
      this.spin.hide();
      this.toastr.error(err, "");
    });
  }
  open_Intake_agreement(data: any): void {
    this.router.navigate(['main/crm/agreementdocument/' + this.cs.encrypt({ agreementCode: data.agreementCode, potentialClientId: data.potentialClientId, pageflow: 'view' })]);

  }

  DocSign_check_update(obj: any) {
    this.spin.show();
    this.sub.add(this.service.docusignstatus(obj.potentialClientId).subscribe((res) => {
      this.spin.hide();
      if (res.status == "completed") {
        this.spin.show();
        this.sub.add(this.service.docuSignDownload(obj.potentialClientId).subscribe((res: any) => {
          this.spin.hide();
          this.getallationslist();
        }, err => {
          this.cs.commonerror(err);
          this.spin.hide();
        }));
      }

    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();

    }));
  }




  // checktableDokerSignStatus() {
  //   this.spin.show();
  //   this.sub.add(this.service.Getall().subscribe((resgetall: PotentialElement[]) => {
  //     this.spin.hide();
  //     this.spin.show();

  //     let checkstatus: PotentialElement[] = [];
  //     resgetall.forEach(x => {
  //       if (x.statusId == 12) {
  //         checkstatus.push(x);

  //       }
  //     });


  //     if (checkstatus.length > 0) {
  //       let url: string[] = [];
  //       checkstatus.forEach(x => url.push('/mnr-crm-service/agreement/docusign/envelope/status?potentialClientId=' + x.potentialClientId));
  //       this.cas.getalldropdownlist(url).subscribe((results) => {
  //         this.spin.hide();
  //         this.spin.show();

  //         let updatestatus: any[] = [];
  //         for (let i = 0; i < results.length; i++) {
  //           let response: any = results[i];
  //           if (response.status == "completed") {
  //             let updatelement = checkstatus[i];
  //             updatelement.statusId = 15;
  //             updatestatus.push({ obj: updatelement, url: '/mnr-crm-service/potentialClient/' + updatelement.potentialClientId });
  //           }
  //         }
  //         if (updatestatus.length > 0)
  //           this.cas.dockerSignStatusUpdatepotentialClientId(updatestatus).subscribe((results) => {
  //             this.spin.hide();

  //             this.getallationslist();

  //           }, (err) => {
  //             this.spin.hide();
  //             this.toastr.error(err, "");
  //           });
  //         else {
  //           this.spin.hide();

  //           this.getallationslist();

  //         }

  //       }, (err) => {
  //         this.spin.hide();
  //         this.toastr.error(err, "");
  //       });

  //     }
  //     else {
  //       this.spin.hide();

  //       this.getallationslist();

  //     }

  //   }, err => {
  //     this.cs.commonerror(err);
  //     this.spin.hide();
  //   }));
  // }
  reset() {
    this.searhform.reset();
  }
}