import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { MatTableDataSource } from "@angular/material/table";
import { NotesComponent } from "../../../common-field/notes/notes.component";
import { DeleteComponent } from "src/app/common-field/dialog_modules/delete/delete.component";
import { Router, ActivatedRoute } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { Subscription } from "rxjs";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { ExcelService } from "src/app/common-service/excel.service";
import { IntakeElementList, IntakeService } from "./intake.service";
import { InquiresService } from "../inquiries/inquires.service";
import { FormBuilder } from "@angular/forms";
import { SpanishService } from "src/app/customerform/spanish/spanish.service";
import { FormService } from "src/app/customerform/form.service";
import { Spanish003Component } from "../intake-pdf/spanish003/spanish003.component";
import { DetailspopupComponent } from "../feedback-pdf/detailspopup/detailspopup.component";
import { EnglishFeedbackpdfComponent } from "../feedback-pdf/english-feedbackpdf/english-feedbackpdf.component";
import { SpanishFeedbackpdfComponent } from "../feedback-pdf/spanish-feedbackpdf/spanish-feedbackpdf.component";
import { FeedbackpdfService } from "../feedback-pdf/feedbackpdf.service";
import { EnglishN400Service } from "src/app/customerform/english-n400/english-n400.service";
import { EnglishService } from "src/app/customerform/english/english.service";
import { EnglishintakeComponent } from "../intake-pdf/englishintake/englishintake.component";
import { N400intakeComponent } from "../intake-pdf/n400intake/n400intake.component";

interface SelectItem {
  id: string;
  itemName: string;
}
@Component({
  selector: 'app-intake-snap-main',
  templateUrl: './intake-snap-main.component.html',
  styleUrls: ['./intake-snap-main.component.scss']
})
export class IntakeSnapMainComponent implements OnInit {
  screenid = 1073;
  public icon = 'expand_more';
  sub = new Subscription;
  isShowDiv = false;
  showFloatingButtons: any;
  ELEMENT_DATA: IntakeElementList[] = [];

  @ViewChild(MatSort, { static: true }) sort: MatSort = new MatSort();
  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination
  // Pagination
  // Pagination
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
  animal: string | undefined;
  name: string | undefined;
  constructor(private dialog: MatDialog,
    private spanishPdf: Spanish003Component,
    private cs: CommonService,
    private router: Router,
    private fb: FormBuilder,
    private excel: ExcelService,
    private auth: AuthService,
    private spanish: SpanishService,    private form: FormService,
    private inquiriesService: InquiresService,
    private service: IntakeService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService, public toastr: ToastrService,
    private englishfeedbackpdf:EnglishFeedbackpdfComponent,
    private spanishfeedbackpdf: SpanishFeedbackpdfComponent,
    private englishpdf: EnglishintakeComponent,
    private  englishpdfn400: N400intakeComponent,
    private english:EnglishService,
    private englishn400:EnglishN400Service,
    private feedBackService: FeedbackpdfService) { }


  openNotes(data: any): void {


    this.spin.show();
    this.sub.add(this.service.Get(data.inquiryNumber).subscribe((res) => {
      this.spin.hide();


      const dialogRef = this.dialog.open(NotesComponent, {
        disableClose: true,
        width: '50%',
        maxWidth: '80%',
        data: res.referenceField8
      });

      dialogRef.afterClosed().subscribe(result => {



      });
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));


  }
  open_new_update(data: any, type: any) {
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
  openDelete(data: any): void {

    const dialogRef = this.dialog.open(DeleteComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {


        this.spin.show();
        this.sub.add(this.service.Delete(data.intakeFormNumber, data.inquiryNumber).subscribe((res) => {
          this.spin.hide();

          this.sub.add(this.inquiriesService.Assign(data, data.inquiryNumber).subscribe(res => {
            this.spin.hide();
            this.getallationslist();
            this.toastr.success(data.intakeFormNumber + ' deleted successfully.', "Notification", {
              timeOut: 2000,
              progressBar: false,
            });

          }, err => {


            this.cs.commonerror(err);

          }));
        }, err => {
          this.cs.commonerror(err);
          this.spin.hide();
        }));

      }
    });

  }
  statuslist: any[] = [];

  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        'Class': x.classId,
        'Status': x.statusId_des,
        "Inquiry No": x.inquiryNumber,
        'Intake Form': x.intakeFormNumber,
        'Intake Form Desc': x.intakeFormId_des,
        "Inquiry Date  ": this.cs.dateapi(x.createdOn),
        "Intake Form No ": x.intakeFormNumber,
        'Email': x.email,
        "Sent/Filled Date  ": this.cs.dateapi(x.sentOn),
        'Received Date': this.cs.dateapi(x.receivedOn),
        'Approved Date': this.cs.dateapi(x.approvedOn),
      });

    })
    this.excel.exportAsExcel(res, "Intake");
  }

  selectedItems2: SelectItem[] = [];
  multiselectstatusList: any[] = [];
  multistatusList: any[] = [];

  
  selectedItems3: SelectItem[] = [];
  multiselectintakeIDList: any[] = [];
  multiintakeIDList: any[] = [];


  getallationslist() {


    let intakeFormIdList: any[] = [];
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.statusId.url,
    this.cas.dropdownlist.setup.intakeFormId.url,
    ]).subscribe((results) => {
      this.statuslist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key).filter(s => [5, 6, 7, 67, 8, 9, 10, 25].includes(s.key));    //PC rejected - is not showing up in the CRM Intake Form window  30/10/2023
      this.statuslist.forEach((x: { key: string; value: string; }) => this.multistatusList.push({value: x.key, label:  x.value}))
      this.multiselectstatusList = this.multistatusList;

      intakeFormIdList = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.intakeFormId.key);
      intakeFormIdList.forEach((x: { key: string; value: string; }) => this.multiintakeIDList.push({value: x.key, label:  x.key + '-' + x.value}))
      this.multiselectintakeIDList = this.multiintakeIDList;

      this.sub.add(this.service.Getall().subscribe((res: IntakeElementList[]) => {
        this.ELEMENT_DATA = res;
        this.ELEMENT_DATA.forEach((x) => {
          x.statusId_des = this.statuslist.find(y => y.key == x.statusId)?.value;
          x.intakeFormId_des = intakeFormIdList.find(y => y.key == x.intakeFormId)?.value;
          x['name'] =  this.handlingunitList.find(y => y.inquiryNumber == x.inquiryNumber)?.firstName;
          x['lastname'] =  this.handlingunitList.find(y => y.inquiryNumber == x.inquiryNumber)?.lastName;
        })

        this.dataSource = new MatTableDataSource<IntakeElementList>(this.ELEMENT_DATA.sort((a, b) => (a.inquiryNumber > b.inquiryNumber) ? -1 : 1));
        this.selection = new SelectionModel<IntakeElementList>(true, []);
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
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }



  RA: any = {};
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);
    this.dropdownlist();


    this.referral()
   
  }
  displayedColumns: string[] = ['select', 'action', 'statusId_des', 'intakeFormNumber', 'intakeFormId_des', 'inquiryNumber','document', 'clientDocument', 'feedback', 'name', 'createdOn', 'email', 'notes', 'sentOnString', 'receivedOnString', 'approvedOnString', 'createdOn'];
  dataSource = new MatTableDataSource<IntakeElementList>(this.ELEMENT_DATA);
  selection = new SelectionModel<IntakeElementList>(true, []);

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
  checkboxLabel(row?: IntakeElementList): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.intakeFormId + 1}`;
  }

    clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }


  selectedItems: SelectItem[] = [];
  selectedItems1: SelectItem[] = [];
  multiselectstorageList: any[] = [];
  multistorageList: any[] = [];
  multiselectinquiryList: any[] = [];
  multiinquiryList: any[] = [];

  dropdownSettings = {
    singleSelection: false,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2
  };
  handlingunitList: any = [];
  dropdownlist() {
    this.spin.show();
    this.sub.add(this.service.Getinquiry().subscribe(res => {
      this.handlingunitList = res;
      console.log(this.handlingunitList)
      this.handlingunitList.forEach((x: { inquiryNumber: any; }) => this.multiinquiryList.push({ value: x.inquiryNumber, label: x.inquiryNumber }))
      this.multiselectinquiryList = this.multiinquiryList;
      // const inquiryNumber = [...new Set(this.multiselectinquiryList.map(item => item.id))].filter(x => x != null);
      // this.searhform.controls.inquiryNumber.patchValue(inquiryNumber);
      this.getallationslist();
      this.spin.hide();

    },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
  }
  searchStatusList = {
    statusId: [5, 6, 7, 67, 8, 9, 10, 25]   //PC rejected - is not showing up in the CRM Intake Form window  30/10/2023
  };

  searhform = this.fb.group({
    eapprovedOn: [],
    email: [],
    ereceivedOn: [],
    esentOn: [],
    inquiryNumber: [],
    inquiryNumberFE: [],
    intakeFormId: [],
    intakeFormIdFE: [],
    intakeFormNumber: [],
    sapprovedOn: [],
    sreceivedOn: [],
    ssentOn: [],
    statusId: [],
    statusIdFE: [],

    startReceivedOn: [],
    endReceivedOn: [],
    startApprovedOn: [],
    startSentOn: [],
    endApprovedOn: [],
    endSentOn: [],

  });
  Clear() {
    this.reset();
  };
  search() {

    this.searhform.controls.startApprovedOn.patchValue(this.cs.dateNewFormat1(this.searhform.controls.sapprovedOn.value));
    this.searhform.controls.startReceivedOn.patchValue(this.cs.dateNewFormat1(this.searhform.controls.sreceivedOn.value));
    this.searhform.controls.startSentOn.patchValue(this.cs.dateNewFormat1(this.searhform.controls.ssentOn.value));
    this.searhform.controls.endApprovedOn.patchValue(this.cs.dateNewFormat1(this.searhform.controls.eapprovedOn.value));
    this.searhform.controls.endReceivedOn.patchValue(this.cs.dateNewFormat1(this.searhform.controls.ereceivedOn.value));
    this.searhform.controls.endSentOn.patchValue(this.cs.dateNewFormat1(this.searhform.controls.esentOn.value));
    // let inquiryNumber: any[] = [];
    // this.selectedItems.forEach(x => inquiryNumber.push(x.id))
    // this.searhform.patchValue({ inquiryNumber: inquiryNumber });

    // if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
    //   this.searhform.patchValue({inquiryNumber: this.selectedItems[0].id });
    // }

    if (this.selectedItems && this.selectedItems.length > 0){
      let multiinquiryList: any[]=[]
      this.selectedItems.forEach((a: any)=> multiinquiryList.push(a.id))
      this.searhform.patchValue({inquiryNumber: this.selectedItems });
    }

    if (this.selectedItems2 && this.selectedItems2.length > 0){
      let multistatusList: any[]=[]
      this.selectedItems2.forEach((a: any)=> multistatusList.push(a.id))
      this.searhform.patchValue({statusId: this.selectedItems2 });
    }

    if (this.selectedItems3 && this.selectedItems3.length > 0){
      let multiintakeIDList: any[]=[]
      this.selectedItems3.forEach((a: any)=> multiintakeIDList.push(a.id))
      this.searhform.patchValue({intakeFormId: this.selectedItems3 });
    }

    let intakeFormIdList: any[] = [];
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.statusId.url,
    this.cas.dropdownlist.setup.intakeFormId.url,
    ]).subscribe((results) => {
      this.statuslist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key);
      intakeFormIdList = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.intakeFormId.key);
      this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: IntakeElementList[]) => {
        this.ELEMENT_DATA = res;
        this.ELEMENT_DATA.forEach((x) => {
          x.statusId_des = this.statuslist.find(y => y.key == x.statusId)?.value;
          x.intakeFormId_des = intakeFormIdList.find(y => y.key == x.intakeFormId)?.value;
      
          x['name'] =  this.handlingunitList.find(y => y.inquiryNumber == x.inquiryNumber)?.firstName;
          x['lastname'] =  this.handlingunitList.find(y => y.inquiryNumber == x.inquiryNumber)?.lastName;
        })

        this.dataSource = new MatTableDataSource<IntakeElementList>(this.ELEMENT_DATA.sort((a, b) => (a.inquiryNumber > b.inquiryNumber) ? -1 : 1));
        this.selection = new SelectionModel<IntakeElementList>(true, []);
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

  reset() {
    this.searhform.reset();
  }

  onItemSelect(item: any) {
    console.log(item);
    console.log(this.selectedItems);
  }
  OnItemDeSelect(item: any) {
    console.log(item);
    console.log(this.selectedItems);
  }
  onSelectAll(items: any) {
    console.log(items);
  }
  onDeSelectAll(items: any) {
    console.log(items);
  }

  callIntakeApi(element, type){
    if(type == 'client'){
      if(element.intakeFormId == 3){
      this.spanish.Get(element.classId, element.inquiryNumber, element.intakeFormId, element.intakeFormNumber, element.languageId ).subscribe(res => {
        console.log(res)
        this.form.Get(element.intakeFormNumber).subscribe(intakeRes => {
        this.spanishPdf.generatePdf(res, intakeRes, this.multiselectreferralList)
        })
      })
    }
    if(element.intakeFormId == 2){
      this.english.Get(element.classId, element.inquiryNumber, element.intakeFormId, element.intakeFormNumber, element.languageId ).subscribe(res => {
        console.log(res)
        this.form.Get(element.intakeFormNumber).subscribe(intakeRes => {
        this.englishpdf.generatePdf(res, intakeRes, this.multiselectreferralList)
        })
      })
    }
      if(element.intakeFormId == 4){
        this.englishn400.Get(element.classId, element.inquiryNumber, element.intakeFormId, element.intakeFormNumber, element.languageId ).subscribe(res => {
          console.log(res)
          this.form.Get(element.intakeFormNumber).subscribe(intakeRes => {
          this.englishpdfn400.generatePdf(res, intakeRes, this.multiselectreferralList)
          })
        })
      }  
  }
    if(type == 'attorney'){
      if(element.intakeFormId == 3){
      this.spanish.GetAttorney(element.classId, element.inquiryNumber, element.intakeFormId, element.intakeFormNumber, element.languageId ).subscribe(res => {
        console.log(res)
        this.form.Get(element.intakeFormNumber).subscribe(intakeRes => {
        this.spanishPdf.generatePdf(res, intakeRes, this.multiselectreferralList)
        })
      })
      }
      if(element.intakeFormId == 2){
        this.english.GetAttorney(element.classId, element.inquiryNumber, element.intakeFormId, element.intakeFormNumber, element.languageId ).subscribe(res => {
          console.log(res)
          this.form.Get(element.intakeFormNumber).subscribe(intakeRes => {
          this.englishpdf.generatePdf(res, intakeRes, this.multiselectreferralList)
          })
        })

      } 
       if(element.intakeFormId == 4){
        this.englishn400.GetAttorney(element.classId, element.inquiryNumber, element.intakeFormId, element.intakeFormNumber, element.languageId ).subscribe(res => {
          console.log(res)
          this.form.Get(element.intakeFormNumber).subscribe(intakeRes => {
          this.englishpdfn400.generatePdf(res, intakeRes, this.multiselectreferralList)
          })
        })

    
   
  }
  }   
}


sendSurvey(data: any): void {

  let formname = this.cs.customerformname(data.intakeFormId);
  if (formname == '') {
    this.toastr.error(
      "Select from is invalid.",
      ""
    );
    
    this.cs.notifyOther(true);
    return;
  }
  this.cs.notifyOther(false)
    const dialogRef = this.dialog.open(DetailspopupComponent, {
      disableClose: true,
      width: '55%',
      maxWidth: '80%',
      data: {code: data}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.getallationslist();
    });

}


callFeedback(element){
  if(element.intakeFormId==3){
     this.feedBackService.GetFeedback(element.intakeFormNumber).subscribe(res => {
      console.log(res)
      if(res){
        this.spanishfeedbackpdf.generatePdf(res)
      }else{
        this.toastr.error('No data found', "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
      }
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    })
  }
  else{
    this.feedBackService.GetFeedback(element.intakeFormNumber).subscribe(res => {
      console.log(res)
      if(res){
        this.englishfeedbackpdf.generatePdf(res);
      }else{
        this.toastr.error('No data found', "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
      }
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    })
  }
    }


    referralList: any[] = [];
filterreferralList: any[] = [];
filterreferralList1: any[] = [];
multiselectreferralList: any[] = [];
multireferralList: any[] = [];
referral(){
  this.sub.add(this.english.getreferral().subscribe(res => {
    this.referralList = res;
    this.filterreferralList = this.referralList.filter((element: { languageId: any; }) => {
      return element.languageId === 'EN';

    });
    this.filterreferralList1 = this.filterreferralList;
    this.filterreferralList1.forEach((x: { referralId: string; referralDescription: string; }) => this.multireferralList.push({value: (x.referralId).toString(), label: x.referralId + '-' + x.referralDescription}))
    this.multiselectreferralList = this.multireferralList;
    this.spin.hide();
    
  }, 
  err => {
    this.cs.commonerror(err);
    this.spin.hide();
  }));
}
}

