import {
  Component,
  Inject,
  Injectable,
  OnInit,
  ViewChild
} from '@angular/core';
import {
  FormControl,
  FormBuilder,
  Validators,
  FormArray,
  AbstractControl
} from '@angular/forms';
import {
  NgxSpinnerService
} from 'ngx-spinner';
import {
  ToastrService
} from 'ngx-toastr';
import {
  BehaviorSubject,
  Observable,
  Subscription
} from 'rxjs';
import {
  SelectionModel
} from '@angular/cdk/collections';
import {
  DialogExampleComponent
} from 'src/app/common-field/dialog-example/dialog-example.component';
import {
  CommonApiService
} from 'src/app/common-service/common-api.service';
import {
  CommonService
} from 'src/app/common-service/common-service.service';
import {
  AuthService
} from 'src/app/core/core';
import {
  CaseCategoryElement
} from '../../../idmaster/language/language.service';
import {
  OwnershipService
} from '../ownership.service';
import {
  ActivatedRoute,
  Router
} from '@angular/router';
import {
  MatPaginator
} from '@angular/material/paginator';
import {
  MatSort
} from '@angular/material/sort';
import {
  map
} from 'highcharts';
import {
  SelectItem
} from 'primeng/api';
import {
  startWith
} from 'rxjs/operators';
import {
  SetupServiceService
} from 'src/app/common-service/setup-service.service';
import { MatTableDataSource } from '@angular/material/table';
import { OwnershippopComponent } from './ownershippop/ownershippop.component';
import { MatDialog } from '@angular/material/dialog';
import { Ownershipop2Component } from './ownershipop2/ownershipop2.component';
import { StoreNewComponent } from '../../../idmaster/store/store-new/store-new.component';
import { ClientNewComponent } from '../../../master/client/client-new/client-new.component';
import { SubgroupNewComponent } from '../../../idmaster/subgroup/subgroup-new/subgroup-new.component';
import { UploadFilesComponent } from './upload-files/upload-files.component';
import { HttpErrorResponse } from '@angular/common/http';
import { ConfirmComponent } from 'src/app/common-field/dialog_modules/confirm/confirm.component';
import { DomSanitizer } from '@angular/platform-browser';
import { Location } from '@angular/common';
import { ValidationService } from '../../validation/validation.service';


@Component({
  selector: 'app-ownership-new',
  templateUrl: './ownership-new.component.html',
  styleUrls: ['./ownership-new.component.scss']
})
@Injectable({
  providedIn: 'root'
})
export class OwnershipNewComponent implements OnInit {
  screenid: 1135 | undefined;
  displayedColumns = ['chooseFile', 'fileName', 'status'];
  displayedColumns2 = ['groupId','groupName'];
  dataSource1 = new BehaviorSubject<AbstractControl[]>([]);
  public icon = 'expand_more';
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  clientList: any;
  matterList: any;
  filtersmatter: any;



  addRow(d?: any, noUpdate?: boolean) {
    const row = this.fb.group({
      requestId: [],
      file: [],
      id: [0,],
      fileName: [],
      statusId: [],
      uploadedBy: [],
      download: [],
      uploadedOn: [],
    });
    if (d)
      row.patchValue(d);

    this.rows.push(row);
    if (!noUpdate) { this.updateView(); }
  }



  remove(element: any) {
  }



  file: File;
  onFilechange(event: any, element: any) {
    console.log(this.file);
    const dialogRef = this.dialog.open(ConfirmComponent, {
      disableClose: true,
      width: '40%',
      maxWidth: '80%',
      position: {
        top: '6.5%'
      },
      data: { title: "Confirm", message: "Are you sure you want to upload this " + event.target.files[0].name + " ?" }
    });
    dialogRef.afterClosed().subscribe(result => {
      this.file = event.target.files[0];

      this.spin.show();
      this.service.uploadfile(this.file, 'sharepoint/qb').subscribe((resp: any) => {
        this.toastr.success("File uploaded successfully.", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        element.controls.fileName.patchValue(resp.file)
        element.controls.statusId.patchValue(1)
        //element.controls.file.patchValue()

        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      })
    })
  }


  fileUrldownload: any;
  docurl: any;
  async download(element) {
    this.spin.show()
    const blob = await this.service.download(element.controls.fileName.value)
      .catch((err: HttpErrorResponse) => {
        this.cs.commonerror(err);
      });
    this.spin.hide();
    if (blob) {
      const blobOb = new Blob([blob], {
        type: "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
      });
      this.fileUrldownload = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blobOb));
      this.docurl = window.URL.createObjectURL(blob);
      const a = document.createElement('a')
      a.href = this.docurl
      a.download = element.controls.fileName.value;
      a.click();
      URL.revokeObjectURL(this.docurl);

    }
    this.spin.hide();
  }

  delete(element, index) {
    if (this.js.pageflow != "New") {
      this.service.Delete1(element.controls.requestId.value, element.controls.id.value).subscribe(res => {
        this.toastr.success("File deleted successfully.", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.removerow(index)
      })
    }
    if (this.js.pageflow == "New") {
      this.removerow(index)
    }
  }


  removerow(x: any) {
    this.rows.removeAt(x)
    this.reset();
    this.dataSource1.next(this.rows.controls);
  }

  reset() {
    let i = 0;

    this.rows.value.forEach((ro: any) => { ro.id = ++i });
    this.rows.patchValue(this.rows.value);
  }
  FormLine = this.fb.group({
    requestId: [],
    file: [],
    id: [],
    fileName: [],
    statusId: [],
    uploadedBy: [],
    uploadedOn: [],
  });

  rows: FormArray = this.fb.array([this.FormLine]);

  form1 = this.fb.group({
    formLine: this.rows,
  });
  form2 = this.fb.group({
    groupId:[],
    groupName:[],
  });
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


  myControl = new FormControl();
  options: string[] = ['10008-01', '10009-01', '10010-01', '10011-01', '10012-01', '10013-01', '10014-01', '10015-01', '10016-01', '10017-01', '10018-01'];
  filteredOptions: Observable<string[]>;


  ELEMENT_DATA: any[] = [];
  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
  selection = new SelectionModel<any>(true, []);
  dataSource3 = new MatTableDataSource<any>(this.ELEMENT_DATA);
  
  selectedIndex = 0;
  input: any;
  isbtntext = true;

  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);

  isbtntexts = false;
  isbtntextr = false;

  form = this.fb.group({
    coOwnerId1: [, [Validators.required]],
    coOwnerId2: [],
    coOwnerId3: [],
    coOwnerId4: [],
    coOwnerId5: [],
    coOwnerId6: [],
    coOwnerId7: [],
    coOwnerId8: [],
    coOwnerId9: [],
    coOwnerId10: [],
    coOwnerName1: [, [Validators.required]],
    coOwnerName2: [],
    coOwnerName3: [],
    coOwnerName4: [],
    coOwnerName5: [],
    coOwnerName6: [],
    coOwnerName7: [],
    coOwnerName8: [],
    coOwnerName9: [],
    coOwnerName10: [],
    coOwnerPercentage1: [, [Validators.required]],
    coOwnerPercentage2: [],
    coOwnerPercentage3: [],
    coOwnerPercentage4: [],
    coOwnerPercentage5: [],
    coOwnerPercentage6: [],
    coOwnerPercentage7: [],
    coOwnerPercentage8: [],
    coOwnerPercentage9: [],
    coOwnerPercentage10: [],
    companyId: ["1000", [Validators.required]],
    createdBy: [],
    createdOn: [],
    createdOnFE: [],
    updatedOnFE: [],
    deletionIndicator: [],
    groupId: [],
    groupName: [],
    groupTypeId: [],
    groupTypeName: [],
    languageId: ["EN", [Validators.required]],
    referenceField1: [],
    referenceField10: [],
    referenceField2: [],
    referenceField3: [],
    referenceField4: [],
    referenceField5: [],
    referenceField6: [],
    referenceField7: [],
    referenceField8: [],
    referenceField9: [],
    relationShipId1: [],
    relationShipId2: [],
    relationShipId3: [],
    relationShipId4: [],
    relationShipId5: [],
    relationShipId6: [],
    relationShipId7: [],
    relationShipName1: [],
    relationShipName2: [],
    relationShipName3: [],
    relationShipName4: [],
    relationShipName5: [],
    relationShipName6: [],
    relationShipName7: [],
    relationShipName8: [],
    relationShipName9: [],
    relationShipName10: [],
    relationShipId8: [],
    relationShipId9: [],
    relationShipId10: [],
    requestId: [],
    statusId: [,],
    storeId: [, [Validators.required]],
    storeName: [],
    subGroupId: [],
    subGroupName: [],
    updatedBy: [],
    updatedOn: [],

  });
  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {

    if (control.includes('.')) {
      const controls = this.form.get(control);
      return controls ? controls.hasError(error) : false && this.submitted;

    }
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  getErrorMessage() {
    if (this.email.hasError('required')) {
      return ' Field should not be blank';
    }
    return this.email.hasError('email') ? 'Not a valid email' : '';
  }

  onChange(row, event) {
    if ((this.js.pageflow == "New")) {
      // if clientName1 enable the specific rows of it.
      if (row == "clientName1") {
        if (event.checked == true) {
          this.form.controls.coOwnerId1.enable();
          this.form.controls.coOwnerName1.enable();
          this.form.controls.coOwnerPercentage1.enable();
        } else {
          this.form.controls.coOwnerId1.disable();
          this.form.controls.coOwnerName1.disable();
          this.form.controls.coOwnerPercentage1.disable();
        }
      }
      // if clientName2 enable the specific rows of it.
      if (row == "clientName2") {
        if (event.checked == true) {
          this.form.controls.coOwnerId2.enable();
          this.form.controls.coOwnerName2.enable();
          this.form.controls.coOwnerPercentage2.enable();
        } else {
          this.form.controls.coOwnerId2.disable();
          this.form.controls.coOwnerName2.disable();
          this.form.controls.coOwnerPercentage2.disable();
        }
      }
      if (row == "clientName3") {
        if (event.checked == true) {
          this.form.controls.coOwnerId3.enable();
          this.form.controls.coOwnerName3.enable();
          this.form.controls.coOwnerPercentage3.enable();
        } else {
          this.form.controls.coOwnerId3.disable();
          this.form.controls.coOwnerName3.disable();
          this.form.controls.coOwnerPercentage3.disable();
        }
      }
      if (row == "clientName4") {
        if (event.checked == true) {
          this.form.controls.coOwnerId4.enable();
          this.form.controls.coOwnerName4.enable();
          this.form.controls.coOwnerPercentage4.enable();
        } else {
          this.form.controls.coOwnerId4.disable();
          this.form.controls.coOwnerName4.disable();
          this.form.controls.coOwnerPercentage4.disable();
        }
      }
    }
  }
  calculateTotal() {
    const percentage1 = +this.form.controls.coOwnerPercentage1.value || 0;
    const percentage2 = +this.form.controls.coOwnerPercentage2.value || 0;
    const percentage3 = +this.form.controls.coOwnerPercentage3.value || 0;
    const percentage4 = +this.form.controls.coOwnerPercentage4.value || 0;
    const percentage5 = +this.form.controls.coOwnerPercentage5.value || 0;
    const percentage6 = +this.form.controls.coOwnerPercentage6.value || 0;
    const percentage7 = +this.form.controls.coOwnerPercentage7.value || 0;
    const percentage8 = +this.form.controls.coOwnerPercentage8.value || 0;
    const percentage9 = +this.form.controls.coOwnerPercentage9.value || 0;
    const percentage10 = +this.form.controls.coOwnerPercentage10.value || 0;

    let total = percentage1 + percentage2 + percentage3 + percentage4 + percentage5 + percentage6 + percentage7 + percentage8 + percentage9 + percentage10;

    return total;
  }

  disabled = false;
  step = 0;

  setStep(index: number) {
    this.step = index;
  }

  nextStep() {
    this.step++;
  }

  prevStep() {
    this.step--;
  }

  panelOpenState = false;
  panelOpenState1 = false;
  panelOpenState2 = false;
  panelOpenState6 = false;
  constructor(private fb: FormBuilder,
    public dialog: MatDialog,
    private auth: AuthService,
    private service: OwnershipService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private setupService: SetupServiceService,
    private route: ActivatedRoute, private router: Router,
    private sanitizer: DomSanitizer,
    private location: Location,
    private cs: CommonService,

    private ValidationService: ValidationService) { }
  js: any = {}
  ngOnInit(): void {
console.log(this.dropdownSelectstoreID);
    let code = this.route.snapshot.params.code;
    this.js = this.cs.decrypt(code);
    this.form.controls.createdBy.disable();
    this.form.controls.createdOnFE.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOnFE.disable();
    this.form.controls.languageId.patchValue(this.form.controls.languageId.value);
    this.form.controls.companyId.patchValue(this.form.controls.companyId.value);
    this.form.controls.companyId.disable();
console.log(this.dropdownSelectstoreID);
    this.form.controls.languageId.disable();
    this.form.controls.coOwnerName1.disable();
    this.form.controls.coOwnerName2.disable();
    this.form.controls.coOwnerName3.disable();
    this.form.controls.coOwnerName4.disable();
    this.form.controls.coOwnerName5.disable();
    this.form.controls.coOwnerName6.disable();
    this.form.controls.coOwnerName7.disable();
    this.form.controls.coOwnerName8.disable();
    this.form.controls.coOwnerName9.disable();
    this.form.controls.coOwnerName10.disable();
    this.form.controls.relationShipName1.disable();
    this.form.controls.relationShipName2.disable();
    this.form.controls.relationShipName3.disable();
    this.form.controls.relationShipName4.disable();
    this.form.controls.relationShipName5.disable();
    this.form.controls.relationShipName6.disable();
    this.form.controls.relationShipName7.disable();
    this.form.controls.relationShipName8.disable();
    this.form.controls.relationShipName9.disable();
    this.form.controls.relationShipName10.disable();

    this.dropdownlist();

    if (this.js.pageflow == 'New') {
      this.toastr.warning("Kindly select a Store", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
    }
    if (this.js.pageflow !== 'New') {
      if (this.js.pageflow === 'Display') {
        this.form.disable();
        this.fill();
      }

      this.fill();
      this.form.controls.languageId.disable();
      this.form.controls.companyId.disable();

      this.form.controls.languageId.disable();
      this.form.controls.companyId.disable();

    }
    this.updateView();
  }
  updateView() {
    this.dataSource1.next(this.rows.controls);
  }
  statusIdList: any[] = [];
  clientIdList: any[] = [];
  caseSubCategoryIdList: any[] = [];
  caseCategoryIdList: any[] = [];
  clientIdCorporationList: any[] = [];
  apiClientfileter: any = {
    clientId: null
  };
  glList: any[] = [];


  selectedItems3: SelectItem[] = [];
  multiselectclientList: any[] = [];
  multiclientList: any[] = [];


  selectedItems4: SelectItem[] = [];
  multiselectcorporationList: any[] = [];
  multicorporationList: any[] = [];


  selectedItems2: SelectItem[] = [];
  multiselectmatterList: any[] = [];
  multimatterList: any[] = [];
  dropdownSelectcompanyID: any[] = [];
  dropdownSelectLanguageID: any[] = [];
  dropdownSelectcountryID: any[] = [];
  dropdownSelectcontroltypeID: any[] = [];
  dropdownSelectclientID: any[] = [];
  dropdownSelectgroupID: any[] = [];
  dropdownSelectcontrolgroupID: any[] = [];
  dropdownSelectclientID1: any[] = [];
  dropdownSelectclientID6: any[] = [];

  calcdues: false;
  createStore(data: any = 'new'): void {
    const dialogRef = this.dialog.open(StoreNewComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { pageflow: 'New', pageflow1: 'TransactionCreate', code: data != 'TransactionCreate' ? this.selection.selected[0].storeId : null, languageId: data != 'TransactionCreate' ? this.selection.selected[0].languageId : null, companyId: data != 'TransactionCreate' ? this.selection.selected[0].companyId : null, countryId: data != 'TransactionCreate' ? this.selection.selected[0].countryId : null, element: this.form.getRawValue() }
    });

    dialogRef.afterClosed().subscribe(result => {
      this.setupService.searchStore({
        languageId: [this.form.controls.languageId.value],
        companyId: [this.form.controls.companyId.value],
        status: [0],
      }).subscribe(res => {
        this.dropdownSelectstoreID = [];
          res.forEach(element => {
            this.dropdownSelectstoreID.push({
              value: element.storeId,
              label: element.storeId + '-' + element.storeName,
              storeName: element.storeName,
              groupTypeId:element.groupTypeId,
              subGroupId:element.subGroupTypeId,
              groupTypeName:element.groupTypeName,
              subGroupTypeName:element.subGroupTypeName,
            });
        });
      });

    });

  }


  createSubgroup(data: any = 'new'): void {
    console.log(this.form.getRawValue())
    const dialogRef = this.dialog.open(SubgroupNewComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: {
        top: '6.5%'
      },
      data: {
        pageflow: data,
        pageflow1: 'TransactionCreate',

        code: data != 'New' ? this.selection.selected[0].subGroupTypeId : null,
        languageId: data != 'New' ? this.selection.selected[0].languageId : null,
        companyId: data != 'New' ? this.selection.selected[0].companyId : null,
        groupTypeId: data != 'New' ? this.selection.selected[0].groupTypeId : null,
        versionNumber: data != 'New' ? this.selection.selected[0].versionNumber : null,
        element: this.form.getRawValue(),
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      this.spin.hide();
      this.setupService.searchSubGroupType({
        languageId: [this.form.controls.languageId.value],
        companyId: [this.form.controls.companyId.value],
        groupTypeId: [this.form.controls.groupTypeId.value],
        status: [0],
      }).subscribe(res => {
        this.dropdownSelectsubgrouptypeID = [];
        res.forEach(element => {
          this.dropdownSelectsubgrouptypeID.push({
            value: element.subGroupTypeId,
            label: element.subGroupTypeId + '-' + element.subGroupTypeName,
            subGroupName: element.subGroupTypeName,
          });

          this.dropdownSelectsubgrouptypeID = this.cas.removeDuplicatesFromArrayNew(this.dropdownSelectsubgrouptypeID);
        })

      });
      if (this.js.pageflow != 'New') {
        this.form.controls.subGroupId.patchValue(this.js.subGroupTypeId);
      }
    });

  }
  dropdownSelectsubgrouptypeID: any[] = [];
  // ongrouptypeChange(value) {
  //   this.form.controls.groupTypeName.patchValue(value.groupTypeName);
  //   this.setupService.searchSubGroupType({
  //     languageId: [this.form.controls.languageId.value],
  //     companyId: [this.form.controls.companyId.value],
  //     groupTypeId: [value.value],
  //   }).subscribe(res => {
  //     this.dropdownSelectsubgrouptypeID = [];
  //     res.forEach(element => {
  //       this.dropdownSelectsubgrouptypeID.push({
  //         value: element.subGroupTypeId,
  //         label: element.subGroupTypeId + '-' + element.subGroupTypeName,
  //         subGroupName: element.subGroupTypeName,
  //       });

  //     });
  //     if (this.js.pageflow !== 'New') {
  //       this.form.controls.subGroupId.patchValue(this.js.subGroupTypeId);
  //     }
  //   });
  //   this.dropdownSelectsubgrouptypeID = this.cas.removeDuplicatesFromArray(this.dropdownSelectsubgrouptypeID);
  // }
  onsubgroupTypechange(value) {
    this.form.controls.subGroupName.patchValue(value.subGroupName);
  }
  openDialog5(data: any = 'new', element: any = null): void {
    const dialogRef6 = this.dialog.open(Ownershipop2Component, {
      disableClose: true,
      width: '55%',
      maxWidth: '80%',
      position: {
        top: '6.5%'
      },
      data: this.form.getRawValue()
    });

    dialogRef6.afterClosed().subscribe(result => {


    });

  }
  dropdownSelectRealtionship1:any[]=[];
  dropdownSelectRealtionship2:any[]=[];
  dropdownSelectRealtionship3:any[]=[];
  dropdownSelectRealtionship4:any[]=[];
  dropdownSelectRealtionship5:any[]=[];
  dropdownSelectRealtionship6:any[]=[];
  dropdownSelectRealtionship7:any[]=[];
  dropdownSelectRealtionship8:any[]=[];
  dropdownSelectRealtionship9:any[]=[];
  dropdownSelectRealtionship10:any[]=[];
  dropdownSelectclientID2: any[] = [];
  dropdownSelectclientID3: any[] = [];
  dropdownSelectclientID9: any[] = [];
  onClientChange1(value) {
    this.form.controls.coOwnerName1.patchValue(value.clientName);
    this.dropdownSelectclientID9 = this.dropdownSelectclientID1;
    let selectedvalue = this.form.controls.coOwnerId1.value;
    let selectedvalue2 = this.form.controls.coOwnerId2.value;
    let selectedvalue3 = this.form.controls.coOwnerId3.value;
    let selectedvalue4 = this.form.controls.coOwnerId4.value;
    let selectedvalue5 = this.form.controls.coOwnerId5.value;
    this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
      option => option.value !== selectedvalue
    );
    if ((selectedvalue == this.form.controls.coOwnerId2.value)) {
      this.form.controls.coOwnerId2.reset();
      this.form.controls.relationShipId2.reset();
      this.form.controls.relationShipName2.reset();
      this.form.controls.coOwnerPercentage2.reset();
      this.form.controls.coOwnerName2.reset();
    }
    if ((selectedvalue == this.form.controls.coOwnerId3.value)) {
      this.form.controls.coOwnerId3.reset();
      this.form.controls.relationShipId3.reset();
      this.form.controls.relationShipName3.reset();
      this.form.controls.coOwnerPercentage3.reset();
      this.form.controls.coOwnerName3.reset();
    }
    if ((selectedvalue == this.form.controls.coOwnerId4.value)) {
      this.form.controls.coOwnerId4.reset();
      this.form.controls.relationShipId4.reset();
      this.form.controls.relationShipName4.reset();
      this.form.controls.coOwnerPercentage4.reset();
      this.form.controls.coOwnerName4.reset();
    }
    if ((selectedvalue == this.form.controls.coOwnerId5.value)) {
      this.form.controls.coOwnerId5.reset();
      this.form.controls.relationShipId5.reset();
      this.form.controls.relationShipName5.reset();
      this.form.controls.coOwnerPercentage5.reset();
      this.form.controls.coOwnerName5.reset();
    }

    if ((selectedvalue == this.form.controls.coOwnerId6.value)) {
      this.form.controls.coOwnerId6.reset();
      this.form.controls.relationShipId6.reset();
      this.form.controls.relationShipName6.reset();
      this.form.controls.coOwnerPercentage6.reset();
      this.form.controls.coOwnerName6.reset();
    }
    if ((selectedvalue == this.form.controls.coOwnerId7.value)) {
      this.form.controls.coOwnerId7.reset();
      this.form.controls.relationShipId7.reset();
      this.form.controls.relationShipName7.reset();
      this.form.controls.coOwnerPercentage7.reset();
      this.form.controls.coOwnerName7.reset();
    }
    if ((selectedvalue == this.form.controls.coOwnerId8.value)) {
      this.form.controls.coOwnerId8.reset();
      this.form.controls.relationShipId8.reset();
      this.form.controls.relationShipName8.reset();
      this.form.controls.coOwnerPercentage8.reset();
      this.form.controls.coOwnerName8.reset();
    }
    if ((selectedvalue == this.form.controls.coOwnerId9.value)) {
      this.form.controls.coOwnerId9.reset();
      this.form.controls.relationShipId9.reset();
      this.form.controls.relationShipName9.reset();
      this.form.controls.coOwnerPercentage9.reset();
      this.form.controls.coOwnerName9.reset();
    }
    if ((selectedvalue == this.form.controls.coOwnerId10.value)) {
      this.form.controls.coOwnerId10.reset();
      this.form.controls.relationShipId10.reset();
      this.form.controls.relationShipName10.reset();
      this.form.controls.coOwnerPercentage10.reset();
      this.form.controls.coOwnerName10.reset();
    }
    this.setupService.searchgroupmapping({  languageId: [this.form.controls.languageId.value],
      companyId: [this.form.controls.companyId.value],
      clientId: [this.form.controls.coOwnerId1.value],}).subscribe(res => {
        if(res.length > 0){
          this.dropdownSelectRealtionship1 = [];
          res.forEach(element => {
              this.dropdownSelectRealtionship1.push({value: element.relationship, label: element.relationship + '-' + element.relationshipDescription,  description: element.relationshipDescription});
            })
            this.dropdownSelectRealtionship1=this.cas.removeDuplicatesFromArrayNew(this.dropdownSelectRealtionship1);
          }
          if(res.length == 0){
            this.dropdownSelectRealtionship1=this.dropdownSelectRealtionship;
          }
          });
  }

  onClientChange2(value) {
    this.form.controls.coOwnerName2.patchValue(value.clientName);
    let selectedvalue2 = this.form.controls.coOwnerId2.value;
    this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
      option => option.value !== selectedvalue2
    );
    if ((selectedvalue2 == this.form.controls.coOwnerId1.value)) {
      this.form.controls.coOwnerId1.reset();
      this.form.controls.relationShipId1.reset();
      this.form.controls.relationShipName1.reset();
      this.form.controls.coOwnerPercentage1.reset();
      this.form.controls.coOwnerName1.reset();
    }
    if ((selectedvalue2 == this.form.controls.coOwnerId3.value)) {
      this.form.controls.coOwnerId3.reset();
      this.form.controls.relationShipId3.reset();
      this.form.controls.relationShipName3.reset();
      this.form.controls.coOwnerPercentage3.reset();
      this.form.controls.coOwnerName3.reset();
    }
    if ((selectedvalue2 == this.form.controls.coOwnerId4.value)) {
      this.form.controls.coOwnerId4.reset();
      this.form.controls.relationShipId4.reset();
      this.form.controls.relationShipName4.reset();
      this.form.controls.coOwnerPercentage4.reset();
      this.form.controls.coOwnerName4.reset();
    }
    if ((selectedvalue2 == this.form.controls.coOwnerId5.value)) {
      this.form.controls.coOwnerId5.reset();
      this.form.controls.relationShipId5.reset();
      this.form.controls.relationShipName5.reset();
      this.form.controls.coOwnerPercentage5.reset();
      this.form.controls.coOwnerName5.reset();
    }
    if ((selectedvalue2 == this.form.controls.coOwnerId6.value)) {
      this.form.controls.coOwnerId6.reset();
      this.form.controls.relationShipId6.reset();
      this.form.controls.relationShipName6.reset();
      this.form.controls.coOwnerPercentage6.reset();
      this.form.controls.coOwnerName6.reset();
    }
    if ((selectedvalue2 == this.form.controls.coOwnerId7.value)) {
      this.form.controls.relationShipId7.reset();
      this.form.controls.relationShipName7.reset();
      this.form.controls.coOwnerId7.reset();
      this.form.controls.coOwnerPercentage7.reset();
      this.form.controls.coOwnerName7.reset();
    }
    if ((selectedvalue2 == this.form.controls.coOwnerId8.value)) {
      this.form.controls.coOwnerId8.reset();
      this.form.controls.relationShipId8.reset();
      this.form.controls.relationShipName8.reset();
      this.form.controls.coOwnerPercentage8.reset();
      this.form.controls.coOwnerName8.reset();
    }
    if ((selectedvalue2 == this.form.controls.coOwnerId9.value)) {
      this.form.controls.coOwnerId9.reset();
      this.form.controls.relationShipId9.reset();
      this.form.controls.relationShipName9.reset();
      this.form.controls.coOwnerPercentage9.reset();
      this.form.controls.coOwnerName9.reset();
    }
    if ((selectedvalue2 == this.form.controls.coOwnerId10.value)) {
      this.form.controls.coOwnerId10.reset();
      this.form.controls.relationShipId10.reset();
      this.form.controls.relationShipName10.reset();
      this.form.controls.coOwnerPercentage10.reset();
      this.form.controls.coOwnerName10.reset();
    }
    this.setupService.searchgroupmapping({  languageId: [this.form.controls.languageId.value],
      companyId: [this.form.controls.companyId.value],
      clientId: [this.form.controls.coOwnerId2.value],}).subscribe(res => {
        if(res.length > 0){
          this.dropdownSelectRealtionship2 = [];
          res.forEach(element => {
              this.dropdownSelectRealtionship2.push({value: element.relationship, label: element.relationship + '-' + element.relationshipDescription,  description: element.relationshipDescription});
            })
            this.dropdownSelectRealtionship2=this.cas.removeDuplicatesFromArrayNew(this.dropdownSelectRealtionship2);
          }
          if(res.length == 0){
            this.dropdownSelectRealtionship2=this.dropdownSelectRealtionship;
          }
          });
  }
  dropdownSelectclientID4: any[] = [];
  onClientChange3(value) {
    this.form.controls.coOwnerName3.patchValue(value.clientName);
    let selectedvalue3 = this.form.controls.coOwnerId3.value;
    this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
      option => option.value !== selectedvalue3
    );
    if ((selectedvalue3 == this.form.controls.coOwnerId1.value)) {
      this.form.controls.coOwnerId1.reset();
      this.form.controls.relationShipId1.reset();
      this.form.controls.relationShipName1.reset();
      this.form.controls.coOwnerPercentage1.reset();
      this.form.controls.coOwnerName1.reset();
    }
    if ((selectedvalue3 == this.form.controls.coOwnerId2.value)) {
      this.form.controls.coOwnerId2.reset();
      this.form.controls.relationShipId2.reset();
      this.form.controls.relationShipName2.reset();
      this.form.controls.coOwnerPercentage2.reset();
      this.form.controls.coOwnerName2.reset();
    }
    if ((selectedvalue3 == this.form.controls.coOwnerId4.value)) {
      this.form.controls.coOwnerId4.reset();
      this.form.controls.relationShipId4.reset();
      this.form.controls.relationShipName4.reset();
      this.form.controls.coOwnerPercentage4.reset();
      this.form.controls.coOwnerName4.reset();
    }
    if ((selectedvalue3 == this.form.controls.coOwnerId5.value)) {
      this.form.controls.coOwnerId5.reset();
      this.form.controls.relationShipId5.reset();
      this.form.controls.relationShipName5.reset();
      this.form.controls.coOwnerPercentage5.reset();
      this.form.controls.coOwnerName5.reset();
    }

    if ((selectedvalue3 == this.form.controls.coOwnerId6.value)) {
      this.form.controls.coOwnerId6.reset();
      this.form.controls.relationShipId6.reset();
      this.form.controls.relationShipName6.reset();
      this.form.controls.coOwnerPercentage6.reset();
      this.form.controls.coOwnerName6.reset();
    }
    if ((selectedvalue3 == this.form.controls.coOwnerId7.value)) {
      this.form.controls.coOwnerId7.reset();
      this.form.controls.relationShipId7.reset();
      this.form.controls.relationShipName7.reset();
      this.form.controls.coOwnerPercentage7.reset();
      this.form.controls.coOwnerName7.reset();
    }
    if ((selectedvalue3 == this.form.controls.coOwnerId8.value)) {
      this.form.controls.coOwnerId8.reset();
      this.form.controls.relationShipId8.reset();
      this.form.controls.relationShipName8.reset();
      this.form.controls.coOwnerPercentage8.reset();
      this.form.controls.coOwnerName8.reset();
    }
    if ((selectedvalue3 == this.form.controls.coOwnerId9.value)) {
      this.form.controls.coOwnerId9.reset();
      this.form.controls.relationShipId9.reset();
      this.form.controls.relationShipName9.reset();
      this.form.controls.coOwnerPercentage9.reset();
      this.form.controls.coOwnerName9.reset();
    }
    if ((selectedvalue3 == this.form.controls.coOwnerId10.value)) {
      this.form.controls.coOwnerId10.reset();
      this.form.controls.relationShipId10.reset();
      this.form.controls.relationShipName10.reset();
      this.form.controls.coOwnerPercentage10.reset();
      this.form.controls.coOwnerName10.reset();
    }
    this.setupService.searchgroupmapping({  languageId: [this.form.controls.languageId.value],
      companyId: [this.form.controls.companyId.value],
      clientId: [this.form.controls.coOwnerId3.value],}).subscribe(res => {
        if(res.length > 0){
        this.dropdownSelectRealtionship3 = [];
        res.forEach(element => {
            this.dropdownSelectRealtionship3.push({value: element.relationship, label: element.relationship + '-' + element.relationshipDescription,  description: element.relationshipDescription});
          })
          this.dropdownSelectRealtionship3=this.cas.removeDuplicatesFromArrayNew(this.dropdownSelectRealtionship3);
        }
        if(res.length == 0){
          this.dropdownSelectRealtionship3=this.dropdownSelectRealtionship;
        }
        });
  }
  data: any;
  SelectclientID5: any[] = [];
  dropdownSelectclientID5: any[] = [];
  dropdownSelectclientID7: any[] = [];
  onClientChange4(value) {
    this.form.controls.coOwnerName4.patchValue(value.clientName);
    let selectedvalue4 = this.form.controls.coOwnerId4.value;
    this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
      option => option.value !== selectedvalue4
    );
    if ((selectedvalue4 == this.form.controls.coOwnerId1.value)) {
      this.form.controls.coOwnerId1.reset();
      this.form.controls.relationShipId1.reset();
      this.form.controls.relationShipName1.reset();
      this.form.controls.coOwnerPercentage1.reset();
      this.form.controls.coOwnerName1.reset();
    }
    if ((selectedvalue4 == this.form.controls.coOwnerId2.value)) {
      this.form.controls.coOwnerId2.reset();
      this.form.controls.relationShipId2.reset();
      this.form.controls.relationShipName2.reset();
      this.form.controls.coOwnerPercentage2.reset();
      this.form.controls.coOwnerName2.reset();
    }
    if ((selectedvalue4 == this.form.controls.coOwnerId3.value)) {
      this.form.controls.coOwnerId3.reset();
      this.form.controls.relationShipId3.reset();
      this.form.controls.relationShipName3.reset();
      this.form.controls.coOwnerPercentage3.reset();
      this.form.controls.coOwnerName3.reset();
    }
    if ((selectedvalue4 == this.form.controls.coOwnerId5.value)) {
      this.form.controls.coOwnerId5.reset();
      this.form.controls.relationShipId5.reset();
      this.form.controls.relationShipName5.reset();
      this.form.controls.coOwnerPercentage5.reset();
      this.form.controls.coOwnerName5.reset();
    }
    if ((selectedvalue4 == this.form.controls.coOwnerId6.value)) {
      this.form.controls.coOwnerId6.reset();
      this.form.controls.relationShipId6.reset();
      this.form.controls.relationShipName6.reset();
      this.form.controls.coOwnerPercentage6.reset();
      this.form.controls.coOwnerName6.reset();
    }
    if ((selectedvalue4 == this.form.controls.coOwnerId7.value)) {
      this.form.controls.coOwnerId7.reset();
      this.form.controls.relationShipId7.reset();
      this.form.controls.relationShipName7.reset();
      this.form.controls.coOwnerPercentage7.reset();
      this.form.controls.coOwnerName7.reset();
    }
    if ((selectedvalue4 == this.form.controls.coOwnerId8.value)) {
      this.form.controls.coOwnerId8.reset();
      this.form.controls.relationShipId8.reset();
      this.form.controls.relationShipName8.reset();
      this.form.controls.coOwnerPercentage8.reset();
      this.form.controls.coOwnerName8.reset();
    }
    if ((selectedvalue4 == this.form.controls.coOwnerId9.value)) {
      this.form.controls.coOwnerId9.reset();
      this.form.controls.relationShipId9.reset();
      this.form.controls.relationShipName9.reset();
      this.form.controls.coOwnerPercentage9.reset();
      this.form.controls.coOwnerName9.reset();
    }
    if ((selectedvalue4 == this.form.controls.coOwnerId10.value)) {
      this.form.controls.coOwnerId10.reset();
      this.form.controls.relationShipId10.reset();
      this.form.controls.relationShipName10.reset();
      this.form.controls.coOwnerPercentage10.reset();
      this.form.controls.coOwnerName10.reset();
    }
    this.setupService.searchgroupmapping({  languageId: [this.form.controls.languageId.value],
      companyId: [this.form.controls.companyId.value],
      clientId: [this.form.controls.coOwnerId4.value],}).subscribe(res => {
        if(res.length > 0){
          this.dropdownSelectRealtionship4 = [];
          res.forEach(element => {
              this.dropdownSelectRealtionship4.push({value: element.relationship, label: element.relationship + '-' + element.relationshipDescription,  description: element.relationshipDescription});
            })
            this.dropdownSelectRealtionship4=this.cas.removeDuplicatesFromArrayNew(this.dropdownSelectRealtionship4);
          }
          if(res.length == 0){
            this.dropdownSelectRealtionship4=this.dropdownSelectRealtionship;
          }
          });
      
  }
  onClientChange5(value) {
    this.form.controls.coOwnerName5.patchValue(value.clientName);
    let selectedvalue5 = this.form.controls.coOwnerId5.value;

    this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
      option => option.value !== selectedvalue5
    );
    this.form.controls.coOwnerName5.patchValue(value.clientName);
    if ((selectedvalue5 == this.form.controls.coOwnerId1.value)) {
      this.form.controls.coOwnerId1.reset();
      this.form.controls.relationShipId1.reset();
      this.form.controls.relationShipName1.reset();
      this.form.controls.coOwnerPercentage1.reset();
      this.form.controls.coOwnerName1.reset();
    }
    if ((selectedvalue5 == this.form.controls.coOwnerId2.value)) {
      this.form.controls.coOwnerId2.reset();
      this.form.controls.relationShipId2.reset();
      this.form.controls.relationShipName2.reset();
      this.form.controls.coOwnerPercentage2.reset();
      this.form.controls.coOwnerName2.reset();
    }
    if ((selectedvalue5 == this.form.controls.coOwnerId3.value)) {
      this.form.controls.coOwnerId3.reset();
      this.form.controls.relationShipId3.reset();
      this.form.controls.relationShipName3.reset();
      this.form.controls.coOwnerPercentage3.reset();
      this.form.controls.coOwnerName3.reset();
    }
    if ((selectedvalue5 == this.form.controls.coOwnerId4.value)) {
      this.form.controls.coOwnerId4.reset();
      this.form.controls.relationShipId4.reset();
      this.form.controls.relationShipName4.reset();
      this.form.controls.coOwnerPercentage4.reset();
      this.form.controls.coOwnerName4.reset();
    }
    if ((selectedvalue5 == this.form.controls.coOwnerId6.value)) {
      this.form.controls.coOwnerId6.reset();
      this.form.controls.relationShipId6.reset();
      this.form.controls.relationShipName6.reset();
      this.form.controls.coOwnerPercentage6.reset();
      this.form.controls.coOwnerName6.reset();
    }
    if ((selectedvalue5 == this.form.controls.coOwnerId7.value)) {
      this.form.controls.coOwnerId7.reset();
      this.form.controls.relationShipId7.reset();
      this.form.controls.relationShipName7.reset();
      this.form.controls.coOwnerPercentage7.reset();
      this.form.controls.coOwnerName7.reset();
    }
    if ((selectedvalue5 == this.form.controls.coOwnerId8.value)) {
      this.form.controls.coOwnerId8.reset();
      this.form.controls.relationShipId8.reset();
      this.form.controls.relationShipName8.reset();
      this.form.controls.coOwnerPercentage8.reset();
      this.form.controls.coOwnerName8.reset();
    }
    if ((selectedvalue5 == this.form.controls.coOwnerId9.value)) {
      this.form.controls.coOwnerId9.reset();
      this.form.controls.relationShipId9.reset();
      this.form.controls.relationShipName9.reset();
      this.form.controls.coOwnerPercentage9.reset();
      this.form.controls.coOwnerName9.reset();
    }
    if ((selectedvalue5 == this.form.controls.coOwnerId10.value)) {
      this.form.controls.coOwnerId10.reset();
      this.form.controls.relationShipId10.reset();
      this.form.controls.relationShipName10.reset();
      this.form.controls.coOwnerPercentage10.reset();
      this.form.controls.coOwnerName10.reset();
    }
    this.setupService.searchgroupmapping({  languageId: [this.form.controls.languageId.value],
      companyId: [this.form.controls.companyId.value],
      clientId: [this.form.controls.coOwnerId5.value],}).subscribe(res => {
        if(res.length > 0){
          this.dropdownSelectRealtionship5 = [];
          res.forEach(element => {
              this.dropdownSelectRealtionship5.push({value: element.relationship, label: element.relationship + '-' + element.relationshipDescription,  description: element.relationshipDescription});
            })
            this.dropdownSelectRealtionship5=this.cas.removeDuplicatesFromArrayNew(this.dropdownSelectRealtionship5);
          }
          if(res.length == 0){
            this.dropdownSelectRealtionship5=this.dropdownSelectRealtionship;
          }
          });
  }
  onClientChange6(value) {
    this.form.controls.coOwnerName6.patchValue(value.clientName);
    let selectedvalue6 = this.form.controls.coOwnerId6.value;

    this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
      option => option.value !== selectedvalue6
    );


    if ((selectedvalue6 == this.form.controls.coOwnerId1.value)) {
      this.form.controls.coOwnerId1.reset();
      this.form.controls.relationShipId1.reset();
      this.form.controls.relationShipName1.reset();
      this.form.controls.coOwnerPercentage1.reset();
      this.form.controls.coOwnerName1.reset();
    }
    if ((selectedvalue6 == this.form.controls.coOwnerId2.value)) {
      this.form.controls.coOwnerId2.reset();
      this.form.controls.relationShipId2.reset();
      this.form.controls.relationShipName2.reset();
      this.form.controls.coOwnerPercentage2.reset();
      this.form.controls.coOwnerName2.reset();
    }
    if ((selectedvalue6 == this.form.controls.coOwnerId3.value)) {
      this.form.controls.coOwnerId3.reset();
      this.form.controls.relationShipId3.reset();
      this.form.controls.relationShipName3.reset();
      this.form.controls.coOwnerPercentage3.reset();
      this.form.controls.coOwnerName3.reset();
    }
    if ((selectedvalue6 == this.form.controls.coOwnerId4.value)) {
      this.form.controls.coOwnerId4.reset();
      this.form.controls.relationShipId4.reset();
      this.form.controls.relationShipName4.reset();
      this.form.controls.coOwnerPercentage4.reset();
      this.form.controls.coOwnerName4.reset();
    }
    if ((selectedvalue6 == this.form.controls.coOwnerId5.value)) {
      this.form.controls.coOwnerId5.reset();
      this.form.controls.relationShipId5.reset();
      this.form.controls.relationShipName5.reset();
      this.form.controls.coOwnerPercentage5.reset();
      this.form.controls.coOwnerName5.reset();
    }

    if ((selectedvalue6 == this.form.controls.coOwnerId7.value)) {
      this.form.controls.coOwnerId7.reset();
      this.form.controls.relationShipId7.reset();
      this.form.controls.relationShipName7.reset();
      this.form.controls.coOwnerPercentage7.reset();
      this.form.controls.coOwnerName7.reset();
    }
    if ((selectedvalue6 == this.form.controls.coOwnerId8.value)) {
      this.form.controls.coOwnerId8.reset();
      this.form.controls.relationShipId8.reset();
      this.form.controls.relationShipName8.reset();
      this.form.controls.coOwnerPercentage8.reset();
      this.form.controls.coOwnerName8.reset();
    }
    if ((selectedvalue6 == this.form.controls.coOwnerId9.value)) {
      this.form.controls.coOwnerId9.reset();
      this.form.controls.relationShipId9.reset();
      this.form.controls.relationShipName9.reset();
      this.form.controls.coOwnerPercentage9.reset();
      this.form.controls.coOwnerName9.reset();
    }
    if ((selectedvalue6 == this.form.controls.coOwnerId10.value)) {
      this.form.controls.coOwnerId10.reset();
      this.form.controls.relationShipId10.reset();
      this.form.controls.relationShipName10.reset();
      this.form.controls.coOwnerPercentage10.reset();
      this.form.controls.coOwnerName10.reset();
    }
    this.setupService.searchgroupmapping({  languageId: [this.form.controls.languageId.value],
      companyId: [this.form.controls.companyId.value],
      clientId: [this.form.controls.coOwnerId6.value],}).subscribe(res => {
        if(res.length > 0){
          this.dropdownSelectRealtionship6 = [];
          res.forEach(element => {
              this.dropdownSelectRealtionship6.push({value: element.relationship, label: element.relationship + '-' + element.relationshipDescription,  description: element.relationshipDescription});
            })
            this.dropdownSelectRealtionship6=this.cas.removeDuplicatesFromArrayNew(this.dropdownSelectRealtionship6);
          }
          if(res.length == 0){
            this.dropdownSelectRealtionship6=this.dropdownSelectRealtionship;
          }
          });
  }
  onClientChange7(value) {
    this.form.controls.coOwnerName7.patchValue(value.clientName);
    let selectedvalue7 = this.form.controls.coOwnerId7.value;

    this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
      option => option.value !== selectedvalue7
    );


    if ((selectedvalue7 == this.form.controls.coOwnerId1.value)) {
      this.form.controls.coOwnerId1.reset();
      this.form.controls.relationShipId1.reset();
      this.form.controls.relationShipName1.reset();
      this.form.controls.coOwnerPercentage1.reset();
      this.form.controls.coOwnerName1.reset();
    }
    if ((selectedvalue7 == this.form.controls.coOwnerId2.value)) {
      this.form.controls.coOwnerId2.reset();
      this.form.controls.relationShipId2.reset();
      this.form.controls.relationShipName2.reset();
      this.form.controls.coOwnerPercentage2.reset();
      this.form.controls.coOwnerName2.reset();
    }
    if ((selectedvalue7 == this.form.controls.coOwnerId3.value)) {
      this.form.controls.coOwnerId3.reset();
      this.form.controls.relationShipId3.reset();
      this.form.controls.relationShipName3.reset();
      this.form.controls.coOwnerPercentage3.reset();
      this.form.controls.coOwnerName3.reset();
    }
    if ((selectedvalue7 == this.form.controls.coOwnerId4.value)) {
      this.form.controls.coOwnerId4.reset();
      this.form.controls.relationShipId4.reset();
      this.form.controls.relationShipName4.reset();
      this.form.controls.coOwnerPercentage4.reset();
      this.form.controls.coOwnerName4.reset();
    }
    if ((selectedvalue7 == this.form.controls.coOwnerId5.value)) {
      this.form.controls.coOwnerId5.reset();
      this.form.controls.relationShipId5.reset();
      this.form.controls.relationShipName5.reset();
      this.form.controls.coOwnerPercentage5.reset();
      this.form.controls.coOwnerName5.reset();
    }
    if ((selectedvalue7 == this.form.controls.coOwnerId6.value)) {
      this.form.controls.coOwnerId6.reset();
      this.form.controls.relationShipId6.reset();
      this.form.controls.relationShipName6.reset();
      this.form.controls.coOwnerPercentage6.reset();
      this.form.controls.coOwnerName6.reset();
    }

    if ((selectedvalue7 == this.form.controls.coOwnerId8.value)) {
      this.form.controls.coOwnerId8.reset();
      this.form.controls.relationShipId8.reset();
      this.form.controls.relationShipName8.reset();
      this.form.controls.coOwnerPercentage8.reset();
      this.form.controls.coOwnerName8.reset();
    }
    if ((selectedvalue7 == this.form.controls.coOwnerId9.value)) {
      this.form.controls.coOwnerId9.reset();
      this.form.controls.relationShipId9.reset();
      this.form.controls.relationShipName9.reset();
      this.form.controls.coOwnerPercentage9.reset();
      this.form.controls.coOwnerName9.reset();
    }
    if ((selectedvalue7 == this.form.controls.coOwnerId10.value)) {
      this.form.controls.coOwnerId10.reset();
      this.form.controls.relationShipId10.reset();
      this.form.controls.relationShipName10.reset();
      this.form.controls.coOwnerPercentage10.reset();
      this.form.controls.coOwnerName10.reset();
    }
    this.setupService.searchgroupmapping({  languageId: [this.form.controls.languageId.value],
      companyId: [this.form.controls.companyId.value],
      clientId: [this.form.controls.coOwnerId7.value],}).subscribe(res => {
        if(res.length > 0){
          this.dropdownSelectRealtionship7 = [];
          res.forEach(element => {
              this.dropdownSelectRealtionship7.push({value: element.relationship, label: element.relationship + '-' + element.relationshipDescription,  description: element.relationshipDescription});
            })
            this.dropdownSelectRealtionship7=this.cas.removeDuplicatesFromArrayNew(this.dropdownSelectRealtionship7);
          }
          if(res.length == 0){
            this.dropdownSelectRealtionship7=this.dropdownSelectRealtionship;
          }
          });
  }
  dropdownSelectclientID81: any[] = [];
  onClientChange8(value) {
    this.form.controls.coOwnerName8.patchValue(value.clientName);
    let selectedvalue8 = this.form.controls.coOwnerId8.value;

    this.dropdownSelectclientID9 = this.dropdownSelectclientID81.filter(
      option => option.value !== selectedvalue8
    );


    if ((selectedvalue8 == this.form.controls.coOwnerId1.value)) {
      this.form.controls.coOwnerId1.reset();
      this.form.controls.relationShipId1.reset();
      this.form.controls.relationShipName1.reset();
      this.form.controls.coOwnerPercentage1.reset();
      this.form.controls.coOwnerName1.reset();
    }
    if ((selectedvalue8 == this.form.controls.coOwnerId2.value)) {
      this.form.controls.coOwnerId2.reset();
      this.form.controls.relationShipId2.reset();
      this.form.controls.relationShipName2.reset();
      this.form.controls.coOwnerPercentage2.reset();
      this.form.controls.coOwnerName2.reset();
    }
    if ((selectedvalue8 == this.form.controls.coOwnerId3.value)) {
      this.form.controls.coOwnerId3.reset();
      this.form.controls.relationShipId3.reset();
      this.form.controls.relationShipName3.reset();
      this.form.controls.coOwnerPercentage3.reset();
      this.form.controls.coOwnerName3.reset();
    }
    if ((selectedvalue8 == this.form.controls.coOwnerId4.value)) {
      this.form.controls.coOwnerId4.reset();
      this.form.controls.relationShipId4.reset();
      this.form.controls.relationShipName4.reset();
      this.form.controls.coOwnerPercentage4.reset();
      this.form.controls.coOwnerName4.reset();
    }
    if ((selectedvalue8 == this.form.controls.coOwnerId5.value)) {
      this.form.controls.coOwnerId5.reset();
      this.form.controls.relationShipId5.reset();
      this.form.controls.relationShipName5.reset();
      this.form.controls.coOwnerPercentage5.reset();
      this.form.controls.coOwnerName5.reset();
    }
    if ((selectedvalue8 == this.form.controls.coOwnerId6.value)) {
      this.form.controls.coOwnerId6.reset();
      this.form.controls.relationShipId6.reset();
      this.form.controls.relationShipName6.reset();
      this.form.controls.coOwnerPercentage6.reset();
      this.form.controls.coOwnerName6.reset();
    }
    if ((selectedvalue8 == this.form.controls.coOwnerId7.value)) {
      this.form.controls.coOwnerId7.reset();
      this.form.controls.relationShipId7.reset();
      this.form.controls.relationShipName7.reset();
      this.form.controls.coOwnerPercentage7.reset();
      this.form.controls.coOwnerName7.reset();
    }
    if ((selectedvalue8 == this.form.controls.coOwnerId9.value)) {
      this.form.controls.coOwnerId9.reset();
      this.form.controls.relationShipId9.reset();
      this.form.controls.relationShipName9.reset();
      this.form.controls.coOwnerPercentage9.reset();
      this.form.controls.coOwnerName9.reset();
    }
    if ((selectedvalue8 == this.form.controls.coOwnerId10.value)) {
      this.form.controls.coOwnerId10.reset();
      this.form.controls.relationShipId10.reset();
      this.form.controls.relationShipName10.reset();
      this.form.controls.coOwnerPercentage10.reset();
      this.form.controls.coOwnerName10.reset();
    }
    this.setupService.searchgroupmapping({  languageId: [this.form.controls.languageId.value],
      companyId: [this.form.controls.companyId.value],
      clientId: [this.form.controls.coOwnerId8.value],}).subscribe(res => {
        if(res.length > 0){
          this.dropdownSelectRealtionship8 = [];
          res.forEach(element => {
              this.dropdownSelectRealtionship8.push({value: element.relationship, label: element.relationship + '-' + element.relationshipDescription,  description: element.relationshipDescription});
            })
            this.dropdownSelectRealtionship8=this.cas.removeDuplicatesFromArrayNew(this.dropdownSelectRealtionship8);
          }
          if(res.length == 0){
            this.dropdownSelectRealtionship8=this.dropdownSelectRealtionship;
          }
          });
  }
  dropdownSelectclientID10: any[] = [];
  onClientChange9(value) {
    this.form.controls.coOwnerName9.patchValue(value.clientName);
    let selectedvalue9 = this.form.controls.coOwnerId9.value;

    this.dropdownSelectclientID10 = this.dropdownSelectclientID9.filter(
      option => option.value !== selectedvalue9
    );


    if ((selectedvalue9 == this.form.controls.coOwnerId1.value)) {
      this.form.controls.coOwnerId1.reset();
      this.form.controls.relationShipId1.reset();
      this.form.controls.relationShipName1.reset();
      this.form.controls.coOwnerPercentage1.reset();
      this.form.controls.coOwnerName1.reset();
    }
    if ((selectedvalue9 == this.form.controls.coOwnerId2.value)) {
      this.form.controls.coOwnerId2.reset();
      this.form.controls.relationShipId2.reset();
      this.form.controls.relationShipName2.reset();
      this.form.controls.coOwnerPercentage2.reset();
      this.form.controls.coOwnerName2.reset();
    }
    if ((selectedvalue9 == this.form.controls.coOwnerId3.value)) {
      this.form.controls.coOwnerId3.reset();
      this.form.controls.relationShipId3.reset();
      this.form.controls.relationShipName3.reset();
      this.form.controls.coOwnerPercentage3.reset();
      this.form.controls.coOwnerName3.reset();
    }
    if ((selectedvalue9 == this.form.controls.coOwnerId4.value)) {
      this.form.controls.coOwnerId4.reset();
      this.form.controls.relationShipId4.reset();
      this.form.controls.relationShipName4.reset();
      this.form.controls.coOwnerPercentage4.reset();
      this.form.controls.coOwnerName4.reset();
    }
    if ((selectedvalue9 == this.form.controls.coOwnerId5.value)) {
      this.form.controls.coOwnerId5.reset();
      this.form.controls.relationShipId5.reset();
      this.form.controls.relationShipName5.reset();
      this.form.controls.coOwnerPercentage5.reset();
      this.form.controls.coOwnerName5.reset();
    }
    if ((selectedvalue9 == this.form.controls.coOwnerId6.value)) {
      this.form.controls.coOwnerId6.reset();
      this.form.controls.relationShipId6.reset();
      this.form.controls.relationShipName6.reset();
      this.form.controls.coOwnerPercentage6.reset();
      this.form.controls.coOwnerName6.reset();
    }
    if ((selectedvalue9 == this.form.controls.coOwnerId7.value)) {
      this.form.controls.coOwnerId7.reset();
      this.form.controls.relationShipId7.reset();
      this.form.controls.relationShipName7.reset();
      this.form.controls.coOwnerPercentage7.reset();
      this.form.controls.coOwnerName7.reset();
    }
    if ((selectedvalue9 == this.form.controls.coOwnerId8.value)) {
      this.form.controls.coOwnerId8.reset();
      this.form.controls.relationShipId8.reset();
      this.form.controls.relationShipName8.reset();
      this.form.controls.coOwnerPercentage8.reset();
      this.form.controls.coOwnerName8.reset();
    }
    if ((selectedvalue9 == this.form.controls.coOwnerId10.value)) {
      this.form.controls.coOwnerId10.reset();
      this.form.controls.relationShipId10.reset();
      this.form.controls.relationShipName10.reset();
      this.form.controls.coOwnerPercentage10.reset();
      this.form.controls.coOwnerName10.reset();
    }
    this.setupService.searchgroupmapping({  languageId: [this.form.controls.languageId.value],
      companyId: [this.form.controls.companyId.value],
      clientId: [this.form.controls.coOwnerId9.value],}).subscribe(res => {
        if(res.length > 0){
          this.dropdownSelectRealtionship9 = [];
          res.forEach(element => {
              this.dropdownSelectRealtionship9.push({value: element.relationship, label: element.relationship + '-' + element.relationshipDescription,  description: element.relationshipDescription});
            })
            this.dropdownSelectRealtionship9=this.cas.removeDuplicatesFromArrayNew(this.dropdownSelectRealtionship9);
          }
          if(res.length == 0){
            this.dropdownSelectRealtionship9=this.dropdownSelectRealtionship;
          }
          });
  }
  onClientChange10(value) {
    this.form.controls.coOwnerName10.patchValue(value.clientName);
    let selectedvalue10 = this.form.controls.coOwnerId10.value;


    if ((selectedvalue10 == this.form.controls.coOwnerId1.value)) {
      this.form.controls.coOwnerId1.reset();
      this.form.controls.relationShipId1.reset();
      this.form.controls.relationShipName1.reset();
      this.form.controls.coOwnerPercentage1.reset();
      this.form.controls.coOwnerName1.reset();
    }
    if ((selectedvalue10 == this.form.controls.coOwnerId2.value)) {
      this.form.controls.coOwnerId2.reset();
      this.form.controls.relationShipId2.reset();
      this.form.controls.relationShipName2.reset();
      this.form.controls.coOwnerPercentage2.reset();
      this.form.controls.coOwnerName2.reset();
    }
    if ((selectedvalue10 == this.form.controls.coOwnerId3.value)) {
      this.form.controls.coOwnerId3.reset();
      this.form.controls.relationShipId3.reset();
      this.form.controls.relationShipName3.reset();
      this.form.controls.coOwnerPercentage3.reset();
      this.form.controls.coOwnerName3.reset();
    }
    if ((selectedvalue10 == this.form.controls.coOwnerId4.value)) {
      this.form.controls.coOwnerId4.reset();
      this.form.controls.relationShipId4.reset();
      this.form.controls.relationShipName4.reset();
      this.form.controls.coOwnerPercentage4.reset();
      this.form.controls.coOwnerName4.reset();
    }
    if ((selectedvalue10 == this.form.controls.coOwnerId5.value)) {
      this.form.controls.coOwnerId5.reset();
      this.form.controls.relationShipId5.reset();
      this.form.controls.relationShipName5.reset();
      this.form.controls.coOwnerPercentage5.reset();
      this.form.controls.coOwnerName5.reset();
    }
    if ((selectedvalue10 == this.form.controls.coOwnerId6.value)) {
      this.form.controls.coOwnerId6.reset();
      this.form.controls.relationShipId6.reset();
      this.form.controls.relationShipName6.reset();
      this.form.controls.coOwnerPercentage6.reset();
      this.form.controls.coOwnerName6.reset();
    }
    if ((selectedvalue10 == this.form.controls.coOwnerId7.value)) {
      this.form.controls.coOwnerId7.reset();
      this.form.controls.relationShipId7.reset();
      this.form.controls.relationShipName7.reset();
      this.form.controls.coOwnerPercentage7.reset();
      this.form.controls.coOwnerName7.reset();
    }
    if ((selectedvalue10 == this.form.controls.coOwnerId8.value)) {
      this.form.controls.coOwnerId8.reset();
      this.form.controls.relationShipId8.reset();
      this.form.controls.relationShipName8.reset();
      this.form.controls.coOwnerPercentage8.reset();
      this.form.controls.coOwnerName8.reset();
    }
    if ((selectedvalue10 == this.form.controls.coOwnerId9.value)) {
      this.form.controls.coOwnerId10.reset();
      this.form.controls.relationShipId10.reset();
      this.form.controls.relationShipName10.reset();
      this.form.controls.coOwnerPercentage10.reset();
      this.form.controls.coOwnerName10.reset();
    }
    this.setupService.searchgroupmapping({  languageId: [this.form.controls.languageId.value],
      companyId: [this.form.controls.companyId.value],
      clientId: [this.form.controls.coOwnerId10.value],}).subscribe(res => {
        if(res.length > 0){
          this.dropdownSelectRealtionship10 = [];
          res.forEach(element => {
              this.dropdownSelectRealtionship10.push({value: element.relationship, label: element.relationship + '-' + element.relationshipDescription,  description: element.relationshipDescription});
            })
            this.dropdownSelectRealtionship10=this.cas.removeDuplicatesFromArrayNew(this.dropdownSelectRealtionship10);
          }
          if(res.length == 0){
            this.dropdownSelectRealtionship10=this.dropdownSelectRealtionship;
          }
          });
  }
  OnrealtionShipChange1(value) {
    this.form.controls.relationShipName1.patchValue(value.description);
  }
  OnrealtionShipChange2(value) {
    this.form.controls.relationShipName2.patchValue(value.description);
  }
  OnrealtionShipChange3(value) {
    this.form.controls.relationShipName3.patchValue(value.description);
  }
  OnrealtionShipChange4(value) {
    this.form.controls.relationShipName4.patchValue(value.description);
  }
  OnrealtionShipChange5(value) {
    this.form.controls.relationShipName5.patchValue(value.description);
  }
  OnrealtionShipChange6(value) {
    this.form.controls.relationShipName6.patchValue(value.description);
  }
  OnrealtionShipChange7(value) {
    this.form.controls.relationShipName7.patchValue(value.description);
  }
  OnrealtionShipChange8(value) {
    this.form.controls.relationShipName8.patchValue(value.description);
  }
  OnrealtionShipChange9(value) {
    this.form.controls.relationShipName9.patchValue(value.description);
  }
  OnrealtionShipChange10(value) {
    this.form.controls.relationShipName10.patchValue(value.description);
  }

  //    if((selectedvalue6 == this.form.controls.coOwnerId1.value)){
  //     this.form.controls.coOwnerId1.reset();
  //     this.form.controls.coOwnerPercentage1.reset();
  //   this.form.controls.coOwnerName1.reset();
  //    }
  //    if((selectedvalue6 == this.form.controls.coOwnerId2.value)){
  //      this.form.controls.coOwnerId2.reset();
  //     this.form.controls.coOwnerPercentage2.reset();
  //   this.form.controls.coOwnerName2.reset();
  //       }
  //    if((selectedvalue6 == this.form.controls.coOwnerId3.value)){
  //      this.form.controls.coOwnerId3.reset();
  //      this.form.controls.coOwnerPercentage3.reset();
  //   this.form.controls.coOwnerName3.reset();
  //     }  
  //     if((selectedvalue6 == this.form.controls.coOwnerId4.value)){
  //      this.form.controls.coOwnerId4.reset();
  //      this.form.controls.coOwnerPercentage4.reset();
  //   this.form.controls.coOwnerName4.reset();
  //   }  
  //   if((selectedvalue6 == this.form.controls.coOwnerId5.value)){
  //     this.form.controls.coOwnerId5.reset();
  //     this.form.controls.coOwnerPercentage5.reset();
  //  this.form.controls.coOwnerName5.reset();
  //  } 
  showClient() {
    this.setupService.searchClientNew({
      languageId: [this.form.controls.languageId.value],
      companyId: [this.form.controls.companyId.value],
     

    }).subscribe(res => {
      res=this.cas.removeDuplicatesFromArrayList(res, 'groupId')
    this.dataSource3=res;
   
    });
  
  }
  
  onstoreChange(value, data: any = 'New') {
    if (this.js.pageflow == 'TransEdit') {
      let storeId2 = this.js.code;
      this.js.pageflow = "New";
      this.form.controls.storeId.patchValue(storeId2);

    }
   
    this.form.controls.groupTypeId.patchValue(value.groupTypeId);
    this.form.controls.subGroupId.patchValue(value.subGroupId);
    this.form.controls.subGroupName.patchValue(value.subGroupTypeName);
    this.form.controls.groupTypeName.patchValue(value.groupTypeName);
    this.form.controls.storeName.patchValue(value.storeName);
    
    console.log(this.form.controls.groupTypeId.value);
    console.log(this.form.controls.subGroupId.value);
    let store1 = this.form.controls.storeId.value;
    let storeName1 = this.form.controls.storeName.value;
    let groupTypeId1=this.form.controls.groupTypeId.value;
    let subGroupId2=this.form.controls.subGroupId.value;
    let groupTypeName1=this.form.controls.groupTypeName.value;
    let subGroupTypeName1=this.form.controls.subGroupName.value;
console.log(value);
    this.setupService.searchClientNew({
      languageId: [this.form.controls.languageId.value],
      companyId: [this.form.controls.companyId.value],
      storeId: [this.form.controls.storeId.value],

    }).subscribe(res => {
      if (res.length > 0) {
        this.showsecondRow = true;
        this.showthirdRow = true;
        this.showFourthRow = true;
        this.showFifthRow = true;
        this.showSixthRow = true;
        this.showSeventhRow = true;
        this.showEigthRow = true;
        this.showNinthRow = true;
        this.showTenthRow = true;
        this.js.pageflow = "Edit"
        this.form.controls.storeId.patchValue(this.form.controls.storeId.value);
        this.form.patchValue(res[0], {
          emitEvent: false
        });

        this.form.controls.createdOnFE.patchValue(this.cs.dateNewFormat1(this.form.controls.createdOn.value));
        this.form.controls.updatedOnFE.patchValue(this.cs.dateNewFormat1(this.form.controls.updatedOn.value));
        this.form.controls.storeId.patchValue(store1);
      
        this.form.controls.storeName.patchValue(storeName1);
        if(this.js.pageflow == "TransEdit"){
        this.setupService.searchSubGroupType({
          languageId: [this.form.controls.languageId.value],
          companyId: [this.form.controls.companyId.value],
          groupTypeId: [this.form.controls.groupTypeId.value],
        }).subscribe(res => {
          this.dropdownSelectsubgrouptypeID = [];
          res.forEach(element => {
            this.dropdownSelectsubgrouptypeID.push({
              value: element.subGroupTypeId,
              label: element.subGroupTypeId + '-' + element.subGroupTypeName,
              subGroupName: element.subGroupTypeName,
            });
          
          });
        })
      }
        if (this.form.controls.coOwnerPercentage1.value == 0) {
          this.form.controls.coOwnerPercentage1.patchValue(null);
        }
        if (this.form.controls.coOwnerPercentage2.value == 0) {
          this.form.controls.coOwnerPercentage2.patchValue(null);
        }
        if (this.form.controls.coOwnerPercentage3.value == 0) {
          this.form.controls.coOwnerPercentage3.patchValue(null);
        }
        if (this.form.controls.coOwnerPercentage4.value == 0) {
          this.form.controls.coOwnerPercentage4.patchValue(null);
        }
        if (this.form.controls.coOwnerPercentage5.value == 0) {
          this.form.controls.coOwnerPercentage5.patchValue(null);

        }
        if (this.form.controls.coOwnerPercentage6.value == 0) {
          this.form.controls.coOwnerPercentage6.patchValue(null);
        }
        if (this.form.controls.coOwnerPercentage7.value == 0) {
          this.form.controls.coOwnerPercentage7.patchValue(null);
        }
        if (this.form.controls.coOwnerPercentage8.value == 0) {
          this.form.controls.coOwnerPercentage8.patchValue(null);
        }
        if (this.form.controls.coOwnerPercentage9.value == 0) {
          this.form.controls.coOwnerPercentage9.patchValue(null);
        }
        if (this.form.controls.coOwnerPercentage10.value == 0) {
          this.form.controls.coOwnerPercentage10.patchValue(null);

        }
      }

      if (res.length == 0) {
        this.setupService.searchClient({
          languageId: [this.form.controls.languageId.value],
          companyId: [this.form.controls.companyId.value],
          storeId: [this.form.controls.storeId.value],

        }).subscribe(res => {
          if (res != null) {
            this.js.pageflow = "New";
            this.form.reset();
            this.form.controls.groupTypeId.patchValue(groupTypeId1);
            this.form.controls.subGroupId.patchValue(subGroupId2);
            this.form.controls.groupTypeName.patchValue(groupTypeName1);
            this.form.controls.subGroupName.patchValue(subGroupTypeName1);
          }
          if (res != null) {
            this.dropdownSelectclientID = [];
            res.forEach(element => {
              this.dropdownSelectclientID.push({
                value: element.clientId,
                label: element.clientId + '-' + element.clientName,
                clientName: element.clientName,
              });
              this.form.controls.languageId.patchValue(this.auth.languageId);
              this.form.controls.companyId.patchValue("1000")
              this.form.controls.storeId.patchValue(store1);
              this.form.controls.storeName.patchValue(storeName1);
            
            });

          }
        })
      }
    })
    this.panelOpenState1 = !this.panelOpenState1;
  }

  dropdownSelectRealtionship: any[] = [];
  languageIdList: any[] = [];
  companyList: any[] = [];
  controltypeList: any[] = [];
  dropdownSelectstoreID: any[] = [];
  storeList: any[] = [];
  groupList: any[] = [];
  clientList2: any[] = [];
  dropdownlist() {

    this.cas.getalldropdownlist([
      this.cas.dropdownlist.cgsetup.language.url,

    ]).subscribe((results) => {
      this.languageIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.cgsetup.language.key);
      this.languageIdList.forEach((x: {
        key: string; value: string;
      }) => this.dropdownSelectLanguageID.push({
        value: x.key,
        label: x.value
      }));
      this.dropdownSelectLanguageID = this.cas.removeDuplicatesFromArrayNew(this.dropdownSelectLanguageID);
      this.setupService.searchCompany({
        languageId: [this.form.controls.languageId.value]
      }).subscribe(res => {
        this.dropdownSelectcompanyID = [];
        res.forEach(element => {
          this.dropdownSelectcompanyID.push({
            value: element.companyId,
            label: element.companyId + '-' + element.description
          });
        });
      }, (err) => {
        this.spin.hide();
        this.cs.commonerror(err);
      });
      this.dropdownSelectLanguageID = this.cas.removeDuplicatesFromArrayNew(this.dropdownSelectLanguageID);
      this.setupService.searchrelationship({
        languageId: ["EN"],
        companyId: ["1000"],
      }).subscribe(res => {
        this.dropdownSelectRealtionship = [];
        res.forEach(element => {
          this.dropdownSelectRealtionship.push({
            value: element.relationShipId,
            label: element.relationShipId + '-' + element.description,
            description: element.description
          });
          
        });
        this.dropdownSelectRealtionship1 = [];
        res.forEach(element => {
          this.dropdownSelectRealtionship1.push({
            value: element.relationShipId,
            label: element.relationShipId + '-' + element.description,
            description: element.description
          });
          
        });
        this.dropdownSelectRealtionship2 = [];
        res.forEach(element => {
          this.dropdownSelectRealtionship2.push({
            value: element.relationShipId,
            label: element.relationShipId + '-' + element.description,
            description: element.description
          });
          
        });
        this.dropdownSelectRealtionship3 = [];
        res.forEach(element => {
          this.dropdownSelectRealtionship3.push({
            value: element.relationShipId,
            label: element.relationShipId + '-' + element.description,
            description: element.description
          });
          
        });
        this.dropdownSelectRealtionship4 = [];
        res.forEach(element => {
          this.dropdownSelectRealtionship4.push({
            value: element.relationShipId,
            label: element.relationShipId + '-' + element.description,
            description: element.description
          });
          
        });
        this.dropdownSelectRealtionship5 = [];
        res.forEach(element => {
          this.dropdownSelectRealtionship5.push({
            value: element.relationShipId,
            label: element.relationShipId + '-' + element.description,
            description: element.description
          });
          
        });
        this.dropdownSelectRealtionship6 = [];
        res.forEach(element => {
          this.dropdownSelectRealtionship6.push({
            value: element.relationShipId,
            label: element.relationShipId + '-' + element.description,
            description: element.description
          });
          
        });
        this.dropdownSelectRealtionship7 = [];
        res.forEach(element => {
          this.dropdownSelectRealtionship7.push({
            value: element.relationShipId,
            label: element.relationShipId + '-' + element.description,
            description: element.description
          });
          
        });
        this.dropdownSelectRealtionship8 = [];
        res.forEach(element => {
          this.dropdownSelectRealtionship8.push({
            value: element.relationShipId,
            label: element.relationShipId + '-' + element.description,
            description: element.description
          });
          
        });
        this.dropdownSelectRealtionship9 = [];
        res.forEach(element => {
          this.dropdownSelectRealtionship9.push({
            value: element.relationShipId,
            label: element.relationShipId + '-' + element.description,
            description: element.description
          });
          
        });
        this.dropdownSelectRealtionship10 = [];
        res.forEach(element => {
          this.dropdownSelectRealtionship10.push({
            value: element.relationShipId,
            label: element.relationShipId + '-' + element.description,
            description: element.description
          });
          
        });
      }, (err) => {
        this.spin.hide();
        this.cs.commonerror(err);
      });
      this.spin.show();
      this.setupService.searchStore({
        languageId: [this.auth.languageId],
        companyId: ["1000"],
        status: [0],

      }).subscribe(res => {
        this.dropdownSelectstoreID = [];
        res.forEach(element => {
          this.dropdownSelectstoreID.push({
            value: element.storeId,
              label: element.storeId + '-' + element.storeName,
              storeName: element.storeName,
              groupTypeId:element.groupTypeId,
              subGroupId:element.subGroupTypeId,
              groupTypeName:element.groupTypeName,
              subGroupTypeName:element.subGroupTypeName,
          });
        });
        if (this.js.pageflow !== 'New') {
          let storeIdAsInt: number = parseInt(this.js.storeId);
          this.form.controls.storeId.patchValue(storeIdAsInt);
        }

        if (this.js.pageflow == 'TransEdit') {

          let storeIdAsInt: number = parseInt(this.js.code);
          this.form.controls.storeId.patchValue(storeIdAsInt);

          this.setupService.searchClientNew({
            languageId: [this.form.controls.languageId.value],
            companyId: [this.form.controls.companyId.value],
            storeId: [this.form.controls.storeId.value],

          }).subscribe(res => {
            if (res.length > 0) {
              this.showsecondRow = true;
              this.showthirdRow = true;
              this.showFourthRow = true;
              this.showFifthRow = true;
              this.showSixthRow = true;
              this.showSeventhRow = true;
              this.showEigthRow = true;
              this.showNinthRow = true;
              this.showTenthRow = true;
              this.js.pageflow = "New"
              this.form.patchValue(res[0], {
                emitEvent: false
              });
              let storeIdAsInt: number = parseInt(this.js.code);
              this.form.controls.storeId.patchValue(storeIdAsInt);

             
              this.form.controls.createdOnFE.patchValue(this.cs.dateNewFormat1(this.form.controls.createdOn.value));
              this.form.controls.updatedOnFE.patchValue(this.cs.dateNewFormat1(this.form.controls.updatedOn.value));

             
              let storeId2 = this.form.controls.storeId.value;

              this.setupService.searchSubGroupType({
                languageId: [this.form.controls.languageId.value],
                companyId: [this.form.controls.companyId.value],
                groupTypeId: [this.form.controls.groupTypeId.value],
              }).subscribe(res => {
                this.dropdownSelectsubgrouptypeID = [];
                res.forEach(element => {
                  this.dropdownSelectsubgrouptypeID.push({
                    value: element.subGroupTypeId,
                    label: element.subGroupTypeId + '-' + element.subGroupTypeName,
                    subGroupName: element.subGroupTypeName,
                  });
                  // if (this.js.pageflow !='New') {
                  // this.form.controls.storeId.patchValue(this.form.controls.storeId.value);
                  //   this.form.controls.subGroupId.patchValue(this.form.controls.subGroupId.value);

                  // }
                });
              })
            }
          }

          )
          //this.form.controls.storeId.disable();
        }


        this.spin.hide();
        //    if(this.js.pageflow == "TransEdit"){
        //    if(this.js.pageflow == "New" ){
        //     let storeId2=this.js.code;
        //     console.log(storeId2);
        //     this.form.controls.storeId.patchValue(storeId2);
        //       console.log(this.form.controls.storeId.value);
        //   }
        // }
      }, (err) => {
        this.spin.hide();
        this.cs.commonerror(err);
      });
      this.setupService.searchClient({
        languageId: [this.form.controls.languageId.value],
        companyId: [this.form.controls.companyId.value],
        statusId: [0]
      }).subscribe(res => {
        this.dropdownSelectclientID = [];
        res.forEach(element => {
          this.dropdownSelectclientID.push({
            value: element.clientId,
            label: element.clientId + '-' + element.clientName,
            clientName: element.clientName,
          });
        });
        this.dropdownSelectclientID2 = [];
        res.forEach(element => {
          this.dropdownSelectclientID2.push({
            value: element.clientId,
            label: element.clientId + '-' + element.clientName,
            clientName: element.clientName,
          });
        });
        this.dropdownSelectclientID3 = [];
        res.forEach(element => {
          this.dropdownSelectclientID3.push({
            value: element.clientId,
            label: element.clientId + '-' + element.clientName,
            clientName: element.clientName,
          });
        });
        this.dropdownSelectclientID4 = [];
        res.forEach(element => {
          this.dropdownSelectclientID4.push({
            value: element.clientId,
            label: element.clientId + '-' + element.clientName,
            clientName: element.clientName,
          });
        });
        this.dropdownSelectclientID5 = [];
        res.forEach(element => {
          this.dropdownSelectclientID5.push({
            value: element.clientId,
            label: element.clientId + '-' + element.clientName,
            clientName: element.clientName,
          });
        });
        this.dropdownSelectclientID6 = [];
        res.forEach(element => {
          this.dropdownSelectclientID6.push({
            value: element.clientId,
            label: element.clientId + '-' + element.clientName,
            clientName: element.clientName,
          });
        });
        this.dropdownSelectclientID7 = [];
        res.forEach(element => {
          this.dropdownSelectclientID7.push({
            value: element.clientId,
            label: element.clientId + '-' + element.clientName,
            clientName: element.clientName,
          });
        });
        this.dropdownSelectclientID81 = [];
        res.forEach(element => {
          this.dropdownSelectclientID81.push({
            value: element.clientId,
            label: element.clientId + '-' + element.clientName,
            clientName: element.clientName,
          });
        });
        this.dropdownSelectclientID9 = [];
        res.forEach(element => {
          this.dropdownSelectclientID9.push({
            value: element.clientId,
            label: element.clientId + '-' + element.clientName,
            clientName: element.clientName,
          });
        });
        this.dropdownSelectclientID10 = [];
        res.forEach(element => {
          this.dropdownSelectclientID10.push({
            value: element.clientId,
            label: element.clientId + '-' + element.clientName,
            clientName: element.clientName,
          });
        });
      });
      // this.setupService.searchClientNew({
      //   languageId: [this.form.controls.languageId.value],
      //   companyId: [this.form.controls.companyId.value],
      //   storeId: [this.form.controls.storeId.value],
      // }).subscribe(res => {
      //   this.dropdownSelectcontrolgroupID = [];
      //   res.forEach(element => {
      //     if (element.groupTypeId != null) {
      //       this.dropdownSelectcontrolgroupID.push({
      //         value: element.groupTypeId,
      //         label: element.groupTypeId + '-' + element.groupTypeName,
      //         groupTypeName: element.groupTypeName,
      //       });
      //     }
      //     else {
      //       this.setupService.searchControlType({
      //         languageId: [this.form.controls.languageId.value],
      //         companyId: [this.form.controls.companyId.value],
      //         statusId: [0],
      //       }).subscribe(res => {
      //         this.dropdownSelectcontrolgroupID = [];
      //         res.forEach(element => {
      //           this.dropdownSelectcontrolgroupID.push({
      //             value: element.groupTypeId,
      //             label: element.groupTypeId + '-' + element.groupTypeName,
      //             groupTypeName: element.groupTypeName,
      //           });
      //           // this.dropdownSelectcontrolgroupID = this.cas.removeDuplicatesFromArrayNew(this.dropdownSelectcontrolgroupID);
      //         });
      //       }),

      //         this.dropdownSelectcontrolgroupID = this.cas.removeDuplicatesFromArrayNew(this.dropdownSelectcontrolgroupID);
      //     }
      //   });
      //   if (this.js.pageflow !== 'New') {
      //     this.form.controls.groupTypeId.patchValue(this.js.groupTypeId);
      //   }
      // });
      // this.setupService.searchSubGroupType({
      //   languageId: [this.form.controls.languageId.value],
      //   companyId: [this.form.controls.companyId.value],
      //   groupTypeId: [this.form.controls.groupTypeId.value],
      // }).subscribe(res => {
      //   this.dropdownSelectsubgrouptypeID = [];
      //   res.forEach(element => {
      //     this.dropdownSelectsubgrouptypeID.push({
      //       value: element.subGroupTypeId,
      //       label: element.subGroupTypeId + '-' + element.subGroupTypeName,
      //       subGroupName: element.subGroupTypeName,
      //     });

      //   });
      //   if (this.js.pageflow != 'New') {
      //     this.form.controls.subGroupId.patchValue(this.js.subGroupId);
      //   }
      // });
      this.clientList2.forEach((x: {
        key: string; value: string;
      }) => this.dropdownSelectclientID1.push({
        value: x.key,
        label: x.value
      }))
      this.dropdownSelectclientID1 = this.cs.removeDuplicateInArray(this.dropdownSelectclientID1);
    }, (err) => {
      this.toastr.error(err, "");
    });

  }
  list: any = []
  path = "New";
  btntext = "Save";
  showfirstRow = true;

  count = 0;
  toggleRows() {
    if (this.js.pageflow == "New") {
      this.count = this.count + 1;
      if (this.count === 1) {
        this.showsecondRow = true;
        this.showFourthRow = false;
        this.showFifthRow = false;
        this.showthirdRow = false;// Ensure only one row is shown at a time
      } else if (this.count === 2) {
        this.showsecondRow = true;
        this.showFourthRow = false;
        this.showFifthRow = false;
        this.showthirdRow = true;
      }
      else if (this.count == 3) {
        this.showsecondRow = true;
        this.showFourthRow = true;
        this.showFifthRow = false;
        this.showthirdRow = true;
      }
      else if (this.count == 4) {
        this.showsecondRow = true;
        this.showFourthRow = true;
        this.showFifthRow = true;
        this.showthirdRow = true;
      }
      else if (this.count == 5) {
        this.showsecondRow = true;
        this.showFourthRow = true;
        this.showFifthRow = true;
        this.showthirdRow = true;
        this.showSixthRow = true;
      }
      else if (this.count == 6) {
        this.showsecondRow = true;
        this.showFourthRow = true;
        this.showFifthRow = true;
        this.showthirdRow = true;
        this.showSixthRow = true;
        this.showSeventhRow = true;
      }
      else if (this.count == 7) {
        this.showsecondRow = true;
        this.showFourthRow = true;
        this.showFifthRow = true;
        this.showthirdRow = true;
        this.showSixthRow = true;
        this.showSeventhRow = true;
        this.showEigthRow = true;
      }
      else if (this.count == 8) {
        this.showsecondRow = true;
        this.showFourthRow = true;
        this.showFifthRow = true;
        this.showthirdRow = true;
        this.showSixthRow = true;
        this.showSeventhRow = true;
        this.showEigthRow = true;
        this.showNinthRow = true;
      }
      else if (this.count == 9) {
        this.showsecondRow = true;
        this.showFourthRow = true;
        this.showFifthRow = true;
        this.showthirdRow = true;
        this.showSixthRow = true;
        this.showSeventhRow = true;
        this.showEigthRow = true;
        this.showNinthRow = true;
        this.showTenthRow = true;
        this.showTenthRow = true;
      }
    }
  }
  createClient(data: any = 'new'): void {
    const dialogRef = this.dialog.open(ClientNewComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { pageflow: data, code: data != 'New' ? this.selection.selected[0].clientId : null, languageId: data != 'New' ? this.selection.selected[0].languageId : null, companyId: data != 'New' ? this.selection.selected[0].companyId : null }
    });

    dialogRef.afterClosed().subscribe(result => {
      this.setupService.searchClient({
        languageId: [this.form.controls.languageId.value],
        companyId: [this.form.controls.companyId.value],
        statusId: [0],

      }).subscribe(res => {
        if (this.form.controls.coOwnerId1.value == null || this.form.controls.coOwnerId1.value != null) {
          this.dropdownSelectclientID = [];
          res.forEach(element => {
            this.dropdownSelectclientID.push({
              value: element.clientId,
              label: element.clientId + '-' + element.clientName,
              clientName: element.clientName,
            });
          });
        }
        if (this.form.controls.coOwnerId2.value == null || this.form.controls.coOwnerId2.value != null) {
          this.dropdownSelectclientID2 = [];
          res.forEach(element => {
            this.dropdownSelectclientID2.push({
              value: element.clientId,
              label: element.clientId + '-' + element.clientName,
              clientName: element.clientName,
            });
          });
        }
        if (this.form.controls.coOwnerId3.value == null || this.form.controls.coOwnerId3.value != null) {
          this.dropdownSelectclientID3 = [];
          res.forEach(element => {
            this.dropdownSelectclientID3.push({
              value: element.clientId,
              label: element.clientId + '-' + element.clientName,
              clientName: element.clientName,
            });
          });
        }
        if (this.form.controls.coOwnerId4.value == null || this.form.controls.coOwnerId4.value != null) {
          this.dropdownSelectclientID4 = [];
          res.forEach(element => {
            this.dropdownSelectclientID4.push({
              value: element.clientId,
              label: element.clientId + '-' + element.clientName,
              clientName: element.clientName,
            });
          });
        }
        if (this.form.controls.coOwnerId5.value == null || this.form.controls.coOwnerId5.value != null) {
          this.dropdownSelectclientID5 = [];
          res.forEach(element => {
            this.dropdownSelectclientID5.push({
              value: element.clientId,
              label: element.clientId + '-' + element.clientName,
              clientName: element.clientName,
            });
          });
        }
        if (this.form.controls.coOwnerId6.value == null || this.form.controls.coOwnerId6.value != null) {
          this.dropdownSelectclientID6 = [];
          res.forEach(element => {
            this.dropdownSelectclientID6.push({
              value: element.clientId,
              label: element.clientId + '-' + element.clientName,
              clientName: element.clientName,
            });
          });
        }
        if (this.form.controls.coOwnerId7.value == null || this.form.controls.coOwnerId7.value != null) {
          this.dropdownSelectclientID7 = [];
          res.forEach(element => {
            this.dropdownSelectclientID7.push({
              value: element.clientId,
              label: element.clientId + '-' + element.clientName,
              clientName: element.clientName,
            });
          });
        }
        if (this.form.controls.coOwnerId8.value == null || this.form.controls.coOwnerId8.value != null) {
          this.dropdownSelectclientID81 = [];
          res.forEach(element => {
            this.dropdownSelectclientID81.push({
              value: element.clientId,
              label: element.clientId + '-' + element.clientName,
              clientName: element.clientName,
            });
          });
        }
        if (this.form.controls.coOwnerId9.value == null || this.form.controls.coOwnerId9.value != null) {
          this.dropdownSelectclientID9 = [];
          res.forEach(element => {
            this.dropdownSelectclientID9.push({
              value: element.clientId,
              label: element.clientId + '-' + element.clientName,
              clientName: element.clientName,
            });
          });
        }
        if (this.form.controls.coOwnerId10.value == null || this.form.controls.coOwnerId10.value != null) {
          this.dropdownSelectclientID10 = [];
          res.forEach(element => {
            this.dropdownSelectclientID10.push({
              value: element.clientId,
              label: element.clientId + '-' + element.clientName,
              clientName: element.clientName,
            });
          });
        }
      });

    });

  }

  submit() {
    this.submitted = true;
    console.log(this.form);
    const percentage1 = +this.form.controls.coOwnerPercentage1.value || 0;
    const percentage2 = +this.form.controls.coOwnerPercentage2.value || 0;
    const percentage3 = +this.form.controls.coOwnerPercentage3.value || 0;
    const percentage4 = +this.form.controls.coOwnerPercentage4.value || 0;
    const percentage5 = +this.form.controls.coOwnerPercentage5.value || 0;
    const percentage6 = +this.form.controls.coOwnerPercentage6.value || 0;
    const percentage7 = +this.form.controls.coOwnerPercentage7.value || 0;
    const percentage8 = +this.form.controls.coOwnerPercentage8.value || 0;
    const percentage9 = +this.form.controls.coOwnerPercentage9.value || 0;
    const percentage10 = +this.form.controls.coOwnerPercentage10.value || 0;

    let total = percentage1 + percentage2 + percentage3 + percentage4 + percentage5 + percentage6 + percentage7 + percentage8 + percentage9 + percentage10;
    if (total > 100) {
      this.toastr.error(
        "Total Exceeds 100 !,Kindly recheck the Percentages",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );

      this.cs.notifyOther(true);
      return;

    }
    console.log(this.FormLine.value);

    if (total < 100) {
      this.toastr.error(
        "Total is less than 100 !,Kindly recheck the Percentages",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );

      this.cs.notifyOther(true);
      return;
    }


    if (this.form.controls.storeId.value == "") {
      this.toastr.error(
        "Please Select StoreId field to continue",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );

      this.cs.notifyOther(true);
      return;
    }
    if ((this.form.controls.coOwnerId1.value == null) && (this.form.controls.coOwnerPercentage1.value != null)) {
      this.toastr.error(
        "Please fill the Co-Owner1 field to continue",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );

      this.cs.notifyOther(true);
      return;
    }
    if ((this.form.controls.coOwnerId1.value == null) || (this.form.controls.coOwnerPercentage1.value == null)) {
      this.toastr.error(
        "Please fill the Co-Owner1 field to continue",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );

      this.cs.notifyOther(true);
      return;
    }
    if (this.js.pageflow == "New") {
      if ((this.form.controls.coOwnerId2.value == null) && (this.form.controls.coOwnerPercentage2.value != null) && (this.form.controls.coOwnerPercentage2.value != 0)) {
        this.toastr.error(
          "Please fill the Co-Owner2 field to continue",
          "Notification", {
          timeOut: 2000,
          progressBar: false,
        }
        );

        this.cs.notifyOther(true);
        return;
      }

      if ((this.form.controls.coOwnerId3.value == null) && (this.form.controls.coOwnerPercentage3.value != null) && (this.form.controls.coOwnerPercentage3.value != 0)) {
        this.toastr.error(
          "Please fill the Co-Owner3 field to continue",
          "Notification", {
          timeOut: 2000,
          progressBar: false,
        }
        );

        this.cs.notifyOther(true);
        return;
      }
      if ((this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerPercentage4.value != null) && (this.form.controls.coOwnerPercentage4.value != 0)) {
        this.toastr.error(
          "Please fill the Co-Owner4 field to continue",
          "Notification", {
          timeOut: 2000,
          progressBar: false,
        }
        );

        this.cs.notifyOther(true);
        return;
      }
      if ((this.form.controls.coOwnerId7.value == null) && (this.form.controls.coOwnerPercentage7.value != null) && (this.form.controls.coOwnerPercentage7.value != 0)) {
        this.toastr.error(
          "Please fill the Co-Owner7 field to continue",
          "Notification", {
          timeOut: 2000,
          progressBar: false,
        }
        );

        this.cs.notifyOther(true);
        return;
      }
      if ((this.form.controls.coOwnerId8.value == null) && (this.form.controls.coOwnerPercentage8.value != null) && (this.form.controls.coOwnerPercentage8.value != 0)) {
        this.toastr.error(
          "Please fill the Co-Owner8 field to continue",
          "Notification", {
          timeOut: 2000,
          progressBar: false,
        }
        );

        this.cs.notifyOther(true);
        return;
      }
      if ((this.form.controls.coOwnerId9.value == null) && (this.form.controls.coOwnerPercentage9.value != null) && (this.form.controls.coOwnerPercentage9.value != 0)) {
        this.toastr.error(
          "Please fill the Co-Owner9 field to continue",
          "Notification", {
          timeOut: 2000,
          progressBar: false,
        }
        );

        this.cs.notifyOther(true);
        return;
      }
      if ((this.form.controls.coOwnerId10.value == null) && (this.form.controls.coOwnerPercentage10.value != null) && (this.form.controls.coOwnerPercentage10.value != 0)) {
        this.toastr.error(
          "Please fill the Co-Owner10 field to continue",
          "Notification", {
          timeOut: 2000,
          progressBar: false,
        }
        );

        this.cs.notifyOther(true);
        return;
      }
      if ((this.form.controls.coOwnerId6.value == null) && (this.form.controls.coOwnerPercentage6.value != null) && (this.form.controls.coOwnerPercentage6.value != 0)) {
        this.toastr.error(
          "Please fill the Co-Owner6 field to continue",
          "Notification", {
          timeOut: 2000,
          progressBar: false,
        }
        );

        this.cs.notifyOther(true);
        return;
      }
      if ((this.form.controls.coOwnerId5.value == null) && (this.form.controls.coOwnerPercentage5.value != null) && (this.form.controls.coOwnerPercentage5.value != 0)) {
        this.toastr.error(
          "Please fill the Co-Owner5 field to continue",
          "Notification", {
          timeOut: 2000,
          progressBar: false,
        }
        );


        this.cs.notifyOther(true);
        return;
      }
    }
    if (this.form.invalid) {
      this.toastr.error(
        "Please fill the required fields to continue",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );

      this.cs.notifyOther(true);
      return;
    }
    this.cs.notifyOther(false);
    this.form.removeControl('updatedOn');
    this.form.removeControl('createdOn');
    this.form.patchValue({
      updatedby: this.auth.username
    });
    this.form.controls.statusId.patchValue(1);
    if (this.js.code && this.js.pageflow != "New") {
      if (this.js.pageflow == "Edit") {
        if (this.form.controls.coOwnerName4.value == "") {
          this.form.controls.coOwnerId4.patchValue(null);
          this.form.controls.coOwnerPercentage4.patchValue(null);
        }
        if (this.form.controls.coOwnerName3.value == "") {
          this.form.controls.coOwnerPercentage3.patchValue(null);
          this.form.controls.coOwnerId3.patchValue(null);

        }
        if (this.form.controls.coOwnerName5.value == "") {
          this.form.controls.coOwnerPercentage5.patchValue(null);
          this.form.controls.coOwnerId5.patchValue(null);

        }
        if (this.form.controls.coOwnerName2.value == "") {
          this.form.controls.coOwnerPercentage2.patchValue(null);
          this.form.controls.coOwnerId2.patchValue(null);

        }

        if (this.form.controls.coOwnerId3.value == 0) {
          this.form.controls.coOwnerId3.patchValue(null);

        }
        if (this.form.controls.coOwnerId5.value == 0) {
          this.form.controls.coOwnerId5.patchValue(null);

        }
        if (this.form.controls.coOwnerId2.value == "") {
          this.form.controls.coOwnerId2.patchValue(null);

        }
        if (this.form.controls.coOwnerId4.value == "") {
          this.form.controls.coOwnerId4.patchValue(null);

        } if (this.form.controls.coOwnerId6.value == "") {
          this.form.controls.coOwnerId6.patchValue(null);
          this.form.controls.coOwnerPercentage6.patchValue(null);
        }
        if (this.form.controls.coOwnerId7.value == "") {
          this.form.controls.coOwnerId7.patchValue(null);
          this.form.controls.coOwnerPercentage7.patchValue(null);
        }
        if (this.form.controls.coOwnerId8.value == "") {
          this.form.controls.coOwnerId8.patchValue(null);
          this.form.controls.coOwnerPercentage8.patchValue(null);
        }
        if (this.form.controls.coOwnerId9.value == "") {
          this.form.controls.coOwnerId9.patchValue(null);
          this.form.controls.coOwnerPercentage9.patchValue(null);
        }
        if (this.form.controls.coOwnerId10.value == "") {
          this.form.controls.coOwnerId10.patchValue(null);
          this.form.controls.coOwnerPercentage10.patchValue(null);
        }
      }
      if (this.js.pageflow != "New") {
        this.spin.show();
        this.sub.add(this.service.Update(this.form.getRawValue(), this.js.code, this.js.languageId, this.js.companyId).subscribe(res => {
          this.toastr.success(res.requestId + " Request Id updated successfully!", "Notification", {
            timeOut: 2000,
            progressBar: false,
          });
          this.rows.value.forEach(x => {
            x.requestId = res.requestId;
          })
          this.sub.add(this.service.search1({ requestId: [this.form.controls.requestId.value] }).subscribe(ress => {
            if (res.length > 0) {
              this.service.Update1(this.rows.value, this.js.code).subscribe(resp => {
                let paramdata = '';
                paramdata = this.cs.encrypt({ line: res });
                this.router.navigate(['/main/controlgroup/transaction/ownership/' + paramdata])
                this.spin.hide();
              }, err => {
                this.spin.hide();
                this.cs.commonerror(err);
              })
            } else {
              this.service.Create1(this.rows.value).subscribe(resp => {
                let paramdata1 = "";
                paramdata1 = this.cs.encrypt({ line: res });
                sessionStorage.setItem('controlGroupsSummary', paramdata1);
                let paramdata = '';
                paramdata = this.cs.encrypt({ line: res });
                this.router.navigate(['/main/controlgroup/transaction/ownership/' + paramdata])
                this.spin.hide();
              }, err => {
                this.spin.hide();
                this.cs.commonerror(err);
              })
            }
          }, err => {
            this.spin.hide();
            this.cs.commonerror(err);

          }));

        }, err => {
          this.spin.hide();
          this.cs.commonerror(err);

        }));


      }
    }
    else {
      this.spin.show();
      this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
        this.toastr.success(res.requestId + " Request Id saved successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.rows.value.forEach(x => {
          x.requestId = res.requestId;
        })
        this.service.Create1(this.rows.value).subscribe(resp => {
          let paramdata1 = "";
          paramdata1 = this.cs.encrypt({ line: res });
          sessionStorage.setItem('controlGroupsSummary', paramdata1);
          let paramdata = '';
          paramdata = this.cs.encrypt({ line: res });
          this.router.navigate(['/main/controlgroup/transaction/ownership/' + paramdata])
          this.spin.hide();
        }, err => {
          this.spin.hide();
          this.cs.commonerror(err);
        })
      }, err => {
        this.spin.hide();
        this.cs.commonerror(err);

      }));


    }


  };
  showsecondRow = false;
  showthirdRow = false;
  showFourthRow = false;
  showFifthRow = false;
  showSixthRow = false;
  showSeventhRow = false;
  showEigthRow = false;
  showNinthRow = false;
  showTenthRow = false;
  dropdownSelectclientID8: any[] = [];
  onDelete(row) {
    // if clientName1 enable the specific rows of it.
    if (row === "clientName4") {
      // this.showsecondRow = false;
      this.dropdownSelectclientID8 = this.dropdownSelectclientID4;
      this.form.controls.coOwnerId4.reset();
      //this.form.controls.coOwnerId2.patchValue(0);
      this.form.controls.coOwnerName4.patchValue(this.form.controls.coOwnerName5.value);

      this.form.controls.coOwnerId4.patchValue(this.form.controls.coOwnerId5.value);
      this.form.controls.coOwnerPercentage4.patchValue(this.form.controls.coOwnerPercentage5.value);
      this.form.controls.coOwnerId5.patchValue(this.form.controls.coOwnerId6.value);
      this.form.controls.coOwnerPercentage5.patchValue(this.form.controls.coOwnerPercentage6.value);
      this.form.controls.coOwnerName5.patchValue(this.form.controls.coOwnerName6.value);
      this.form.controls.coOwnerId6.patchValue(this.form.controls.coOwnerId7.value);
      this.form.controls.coOwnerPercentage6.patchValue(this.form.controls.coOwnerPercentage7.value);
      this.form.controls.coOwnerName6.patchValue(this.form.controls.coOwnerName7.value);
      this.form.controls.coOwnerId7.patchValue(this.form.controls.coOwnerId8.value);
      this.form.controls.coOwnerPercentage7.patchValue(this.form.controls.coOwnerPercentage8.value);
      this.form.controls.coOwnerName7.patchValue(this.form.controls.coOwnerName8.value);
      this.form.controls.coOwnerId8.patchValue(this.form.controls.coOwnerId9.value);
      this.form.controls.coOwnerPercentage8.patchValue(this.form.controls.coOwnerPercentage9.value);
      this.form.controls.coOwnerName8.patchValue(this.form.controls.coOwnerName9.value);
      this.form.controls.coOwnerId9.patchValue(this.form.controls.coOwnerId10.value);
      this.form.controls.coOwnerPercentage9.patchValue(this.form.controls.coOwnerPercentage10.value);
      this.form.controls.coOwnerName9.patchValue(this.form.controls.coOwnerName10.value);
      this.form.controls.coOwnerId10.patchValue("");
      this.form.controls.coOwnerPercentage10.patchValue("");
      this.form.controls.coOwnerName10.patchValue("");
      let selectedvalue = this.form.controls.coOwnerId1.value;
      let selectedvalue4 = this.form.controls.coOwnerId4.value;
      let selectedvalue2 = this.form.controls.coOwnerId2.value;
      let selectedvalue3 = this.form.controls.coOwnerId3.value;
      let selectedvalue5 = this.form.controls.coOwnerId5.value;
      let selectedvalue6 = this.form.controls.coOwnerId6.value;
      let selectedvalue7 = this.form.controls.coOwnerId7.value;
      let selectedvalue8 = this.form.controls.coOwnerId8.value;
      let selectedvalue9 = this.form.controls.coOwnerId9.value;
      this.dropdownSelectclientID2 = this.dropdownSelectclientID3;
      this.dropdownSelectclientID3 = this.dropdownSelectclientID4;
      this.dropdownSelectclientID4 = this.dropdownSelectclientID5;
      this.dropdownSelectclientID5 = this.dropdownSelectclientID6;
      this.dropdownSelectclientID6 = this.dropdownSelectclientID7;
      this.dropdownSelectclientID7 = this.dropdownSelectclientID81;
      this.dropdownSelectclientID81 = this.dropdownSelectclientID9;
      this.dropdownSelectclientID9 = this.dropdownSelectclientID10;
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue4
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue2
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue3
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue5
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue6
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue7
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue8
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue9
      );

      if ((this.form.controls.coOwnerId2.value == null) && (this.form.controls.coOwnerId3.value == null) && (this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {

        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
      }
      if ((this.form.controls.coOwnerId3.value == null) && (this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
      }
      if ((this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
      }
      if ((this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
      }
      if ((this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
      }
      if ((this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );

      }
      if ((this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );

      }
      if ((this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );
        this.dropdownSelectclientID9 = this.dropdownSelectclientID81.filter(
          option => option.value !== selectedvalue8
        );

      }
      if ((this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );
        this.dropdownSelectclientID9 = this.dropdownSelectclientID81.filter(
          option => option.value !== selectedvalue8
        );
        this.dropdownSelectclientID10 = this.dropdownSelectclientID9.filter(
          option => option.value !== selectedvalue9
        );
      }
      this.form.controls.relationShipId4.patchValue(this.form.controls.relationShipId5.value);
      this.form.controls.relationShipId5.patchValue(this.form.controls.relationShipId6.value);
      this.form.controls.relationShipId6.patchValue(this.form.controls.relationShipId7.value);
      this.form.controls.relationShipId7.patchValue(this.form.controls.relationShipId8.value);
      this.form.controls.relationShipId8.patchValue(this.form.controls.relationShipId9.value);
      this.form.controls.relationShipId9.patchValue(this.form.controls.relationShipId10.value);
      this.form.controls.relationShipName4.patchValue(this.form.controls.relationShipName5.value);
      this.form.controls.relationShipName5.patchValue(this.form.controls.relationShipName6.value);
      this.form.controls.relationShipName6.patchValue(this.form.controls.relationShipName7.value);
      this.form.controls.relationShipName7.patchValue(this.form.controls.relationShipName8.value);
      this.form.controls.relationShipName8.patchValue(this.form.controls.relationShipName9.value);
      this.form.controls.relationShipName9.patchValue(this.form.controls.relationShipName10.value);
      this.form.controls.relationShipId10.patchValue("");
      this.form.controls.relationShipName10.patchValue("");
    }
    if (row === "clientName2") {

      this.form.controls.coOwnerId2.reset();
      // this.showsecondRow = false;
      this.dropdownSelectclientID8 = this.dropdownSelectclientID;
      //this.form.controls.coOwnerId2.patchValue(0);

      this.form.controls.coOwnerName2.patchValue(this.form.controls.coOwnerName3.value);
      this.form.controls.coOwnerId2.patchValue(this.form.controls.coOwnerId3.value);
      this.form.controls.coOwnerPercentage2.patchValue(this.form.controls.coOwnerPercentage3.value);
      this.form.controls.coOwnerName3.patchValue(this.form.controls.coOwnerName4.value);
      this.form.controls.coOwnerId3.patchValue(this.form.controls.coOwnerId4.value);
      this.form.controls.coOwnerPercentage3.patchValue(this.form.controls.coOwnerPercentage4.value);
      this.form.controls.coOwnerName4.patchValue(this.form.controls.coOwnerName5.value);
      this.form.controls.coOwnerId4.patchValue(this.form.controls.coOwnerId5.value);
      this.form.controls.coOwnerPercentage4.patchValue(this.form.controls.coOwnerPercentage5.value);
      this.form.controls.coOwnerId5.patchValue(this.form.controls.coOwnerId6.value);
      this.form.controls.coOwnerPercentage5.patchValue(this.form.controls.coOwnerPercentage6.value);
      this.form.controls.coOwnerName5.patchValue(this.form.controls.coOwnerName6.value);
      this.form.controls.coOwnerId6.patchValue(this.form.controls.coOwnerId7.value);
      this.form.controls.coOwnerPercentage6.patchValue(this.form.controls.coOwnerPercentage7.value);
      this.form.controls.coOwnerName6.patchValue(this.form.controls.coOwnerName7.value);
      this.form.controls.coOwnerId7.patchValue(this.form.controls.coOwnerId8.value);
      this.form.controls.coOwnerPercentage7.patchValue(this.form.controls.coOwnerPercentage8.value);
      this.form.controls.coOwnerName7.patchValue(this.form.controls.coOwnerName8.value);
      this.form.controls.coOwnerId8.patchValue(this.form.controls.coOwnerId9.value);
      this.form.controls.coOwnerPercentage8.patchValue(this.form.controls.coOwnerPercentage9.value);
      this.form.controls.coOwnerName8.patchValue(this.form.controls.coOwnerName9.value);
      this.form.controls.coOwnerId9.patchValue(this.form.controls.coOwnerId10.value);
      this.form.controls.coOwnerPercentage9.patchValue(this.form.controls.coOwnerPercentage10.value);
      this.form.controls.coOwnerName9.patchValue(this.form.controls.coOwnerName10.value);
      this.form.controls.coOwnerId10.patchValue("");
      this.form.controls.coOwnerPercentage10.patchValue("");
      this.form.controls.coOwnerName10.patchValue("");
      let selectedvalue = this.form.controls.coOwnerId1.value;
      let selectedvalue4 = this.form.controls.coOwnerId4.value;
      let selectedvalue2 = this.form.controls.coOwnerId2.value;
      let selectedvalue3 = this.form.controls.coOwnerId3.value;
      let selectedvalue5 = this.form.controls.coOwnerId5.value;
      let selectedvalue6 = this.form.controls.coOwnerId6.value;
      let selectedvalue7 = this.form.controls.coOwnerId7.value;
      let selectedvalue8 = this.form.controls.coOwnerId8.value;
      let selectedvalue9 = this.form.controls.coOwnerId9.value;
      this.dropdownSelectclientID2 = this.dropdownSelectclientID3;
      this.dropdownSelectclientID3 = this.dropdownSelectclientID4;
      this.dropdownSelectclientID4 = this.dropdownSelectclientID5;
      this.dropdownSelectclientID5 = this.dropdownSelectclientID6;
      this.dropdownSelectclientID6 = this.dropdownSelectclientID7;
      this.dropdownSelectclientID7 = this.dropdownSelectclientID81;
      this.dropdownSelectclientID81 = this.dropdownSelectclientID9;
      this.dropdownSelectclientID9 = this.dropdownSelectclientID10;
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue4
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue2
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue3
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue5
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue6
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue7
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue8
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue9
      );

      if ((this.form.controls.coOwnerId2.value == null) && (this.form.controls.coOwnerId3.value == null) && (this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {

        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
      }
      if ((this.form.controls.coOwnerId3.value == null) && (this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
      }
      if ((this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
      }
      if ((this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
      }
      if ((this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
      }
      if ((this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );

      }
      if ((this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );

      }
      if ((this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );
        this.dropdownSelectclientID9 = this.dropdownSelectclientID81.filter(
          option => option.value !== selectedvalue8
        );

      }
      if ((this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );
        this.dropdownSelectclientID9 = this.dropdownSelectclientID81.filter(
          option => option.value !== selectedvalue8
        );
        this.dropdownSelectclientID10 = this.dropdownSelectclientID9.filter(
          option => option.value !== selectedvalue9
        );
      }
      this.form.controls.relationShipId2.patchValue(this.form.controls.relationShipId3.value);
      this.form.controls.relationShipId3.patchValue(this.form.controls.relationShipId4.value);
      this.form.controls.relationShipId4.patchValue(this.form.controls.relationShipId5.value);
      this.form.controls.relationShipId5.patchValue(this.form.controls.relationShipId6.value);
      this.form.controls.relationShipId6.patchValue(this.form.controls.relationShipId7.value);
      this.form.controls.relationShipId7.patchValue(this.form.controls.relationShipId8.value);
      this.form.controls.relationShipId8.patchValue(this.form.controls.relationShipId9.value);
      this.form.controls.relationShipId9.patchValue(this.form.controls.relationShipId10.value);
      this.form.controls.relationShipName2.patchValue(this.form.controls.relationShipName3.value);
      this.form.controls.relationShipName3.patchValue(this.form.controls.relationShipName4.value);
      this.form.controls.relationShipName4.patchValue(this.form.controls.relationShipName5.value);
      this.form.controls.relationShipName5.patchValue(this.form.controls.relationShipName6.value);
      this.form.controls.relationShipName6.patchValue(this.form.controls.relationShipName7.value);
      this.form.controls.relationShipName7.patchValue(this.form.controls.relationShipName8.value);
      this.form.controls.relationShipName8.patchValue(this.form.controls.relationShipName9.value);
      this.form.controls.relationShipName9.patchValue(this.form.controls.relationShipName10.value);
      this.form.controls.relationShipId10.patchValue("");
      this.form.controls.relationShipName10.patchValue("");
    }
    if (row === "clientName3") {
      // this.showsecondRow = false;
      this.form.controls.coOwnerId3.reset();
      this.dropdownSelectclientID8 = this.dropdownSelectclientID3;
      //this.form.controls.coOwnerId2.patchValue(0)


      this.form.controls.coOwnerName3.patchValue(this.form.controls.coOwnerName4.value);
      this.form.controls.coOwnerId3.patchValue(this.form.controls.coOwnerId4.value);
      this.form.controls.coOwnerPercentage3.patchValue(this.form.controls.coOwnerPercentage4.value);
      this.form.controls.coOwnerName4.patchValue(this.form.controls.coOwnerName5.value);
      this.form.controls.coOwnerId4.patchValue(this.form.controls.coOwnerId5.value);
      this.form.controls.coOwnerPercentage4.patchValue(this.form.controls.coOwnerPercentage5.value);
      this.form.controls.coOwnerId5.patchValue(this.form.controls.coOwnerId6.value);
      this.form.controls.coOwnerPercentage5.patchValue(this.form.controls.coOwnerPercentage6.value);
      this.form.controls.coOwnerName5.patchValue(this.form.controls.coOwnerName6.value);
      this.form.controls.coOwnerId6.patchValue(this.form.controls.coOwnerId7.value);
      this.form.controls.coOwnerPercentage6.patchValue(this.form.controls.coOwnerPercentage7.value);
      this.form.controls.coOwnerName6.patchValue(this.form.controls.coOwnerName7.value);
      this.form.controls.coOwnerId7.patchValue(this.form.controls.coOwnerId8.value);
      this.form.controls.coOwnerPercentage7.patchValue(this.form.controls.coOwnerPercentage8.value);
      this.form.controls.coOwnerName7.patchValue(this.form.controls.coOwnerName8.value);
      this.form.controls.coOwnerId8.patchValue(this.form.controls.coOwnerId9.value);
      this.form.controls.coOwnerPercentage8.patchValue(this.form.controls.coOwnerPercentage9.value);
      this.form.controls.coOwnerName8.patchValue(this.form.controls.coOwnerName9.value);
      this.form.controls.coOwnerId9.patchValue(this.form.controls.coOwnerId10.value);
      this.form.controls.coOwnerPercentage9.patchValue(this.form.controls.coOwnerPercentage10.value);
      this.form.controls.coOwnerName9.patchValue(this.form.controls.coOwnerName10.value);
      this.form.controls.coOwnerId10.patchValue("");
      this.form.controls.coOwnerPercentage10.patchValue("");
      this.form.controls.coOwnerName10.patchValue("");
      let selectedvalue = this.form.controls.coOwnerId1.value;
      let selectedvalue4 = this.form.controls.coOwnerId4.value;
      let selectedvalue2 = this.form.controls.coOwnerId2.value;
      let selectedvalue3 = this.form.controls.coOwnerId3.value;
      let selectedvalue5 = this.form.controls.coOwnerId5.value;
      let selectedvalue6 = this.form.controls.coOwnerId6.value;
      let selectedvalue7 = this.form.controls.coOwnerId7.value;
      let selectedvalue8 = this.form.controls.coOwnerId8.value;
      let selectedvalue9 = this.form.controls.coOwnerId9.value;
      this.dropdownSelectclientID2 = this.dropdownSelectclientID3;
      this.dropdownSelectclientID3 = this.dropdownSelectclientID4;
      this.dropdownSelectclientID4 = this.dropdownSelectclientID5;
      this.dropdownSelectclientID5 = this.dropdownSelectclientID6;
      this.dropdownSelectclientID6 = this.dropdownSelectclientID7;
      this.dropdownSelectclientID7 = this.dropdownSelectclientID81;
      this.dropdownSelectclientID81 = this.dropdownSelectclientID9;
      this.dropdownSelectclientID9 = this.dropdownSelectclientID10;
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue4
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue2
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue3
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue5
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue6
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue7
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue8
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue9
      );

      if ((this.form.controls.coOwnerId2.value == null) && (this.form.controls.coOwnerId3.value == null) && (this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {

        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
      }
      if ((this.form.controls.coOwnerId3.value == null) && (this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
      }
      if ((this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
      }
      if ((this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
      }
      if ((this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
      }
      if ((this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );

      }
      if ((this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );

      }
      if ((this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );
        this.dropdownSelectclientID9 = this.dropdownSelectclientID81.filter(
          option => option.value !== selectedvalue8
        );

      }
      if ((this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );
        this.dropdownSelectclientID9 = this.dropdownSelectclientID81.filter(
          option => option.value !== selectedvalue8
        );
        this.dropdownSelectclientID10 = this.dropdownSelectclientID9.filter(
          option => option.value !== selectedvalue9
        );
      }
      this.form.controls.relationShipId3.patchValue(this.form.controls.relationShipId4.value);
      this.form.controls.relationShipId4.patchValue(this.form.controls.relationShipId5.value);
      this.form.controls.relationShipId5.patchValue(this.form.controls.relationShipId6.value);
      this.form.controls.relationShipId6.patchValue(this.form.controls.relationShipId7.value);
      this.form.controls.relationShipId7.patchValue(this.form.controls.relationShipId8.value);
      this.form.controls.relationShipId8.patchValue(this.form.controls.relationShipId9.value);
      this.form.controls.relationShipId9.patchValue(this.form.controls.relationShipId10.value);
      this.form.controls.relationShipName3.patchValue(this.form.controls.relationShipName4.value);
      this.form.controls.relationShipName4.patchValue(this.form.controls.relationShipName5.value);
      this.form.controls.relationShipName5.patchValue(this.form.controls.relationShipName6.value);
      this.form.controls.relationShipName6.patchValue(this.form.controls.relationShipName7.value);
      this.form.controls.relationShipName7.patchValue(this.form.controls.relationShipName8.value);
      this.form.controls.relationShipName8.patchValue(this.form.controls.relationShipName9.value);
      this.form.controls.relationShipName9.patchValue(this.form.controls.relationShipName10.value);
      this.form.controls.realtionShipId10.patchValue("");
      this.form.controls.realtionShipName10.patchValue("");
    }
    if (row === "clientName5") {
      // this.showFifthRow = false

      this.form.controls.coOwnerId5.patchValue(this.form.controls.coOwnerId6.value);
      this.form.controls.coOwnerPercentage5.patchValue(this.form.controls.coOwnerPercentage6.value);
      this.form.controls.coOwnerName5.patchValue(this.form.controls.coOwnerName6.value);
      this.form.controls.coOwnerId6.patchValue(this.form.controls.coOwnerId7.value);
      this.form.controls.coOwnerPercentage6.patchValue(this.form.controls.coOwnerPercentage7.value);
      this.form.controls.coOwnerName6.patchValue(this.form.controls.coOwnerName7.value);
      this.form.controls.coOwnerId7.patchValue(this.form.controls.coOwnerId8.value);
      this.form.controls.coOwnerPercentage7.patchValue(this.form.controls.coOwnerPercentage8.value);
      this.form.controls.coOwnerName7.patchValue(this.form.controls.coOwnerName8.value);
      this.form.controls.coOwnerId8.patchValue(this.form.controls.coOwnerId9.value);
      this.form.controls.coOwnerPercentage8.patchValue(this.form.controls.coOwnerPercentage9.value);
      this.form.controls.coOwnerName8.patchValue(this.form.controls.coOwnerName9.value);
      this.form.controls.coOwnerId9.patchValue(this.form.controls.coOwnerId10.value);
      this.form.controls.coOwnerPercentage9.patchValue(this.form.controls.coOwnerPercentage10.value);
      this.form.controls.coOwnerName9.patchValue(this.form.controls.coOwnerName10.value);
      this.form.controls.coOwnerId10.patchValue("");
      this.form.controls.coOwnerPercentage10.patchValue("");
      this.form.controls.coOwnerName10.patchValue("");
      let selectedvalue = this.form.controls.coOwnerId1.value;
      let selectedvalue4 = this.form.controls.coOwnerId4.value;
      let selectedvalue2 = this.form.controls.coOwnerId2.value;
      let selectedvalue3 = this.form.controls.coOwnerId3.value;
      let selectedvalue5 = this.form.controls.coOwnerId5.value;
      let selectedvalue6 = this.form.controls.coOwnerId6.value;
      let selectedvalue7 = this.form.controls.coOwnerId7.value;
      let selectedvalue8 = this.form.controls.coOwnerId8.value;
      let selectedvalue9 = this.form.controls.coOwnerId9.value;
      this.dropdownSelectclientID2 = this.dropdownSelectclientID3;
      this.dropdownSelectclientID3 = this.dropdownSelectclientID4;
      this.dropdownSelectclientID4 = this.dropdownSelectclientID5;
      this.dropdownSelectclientID5 = this.dropdownSelectclientID6;
      this.dropdownSelectclientID6 = this.dropdownSelectclientID7;
      this.dropdownSelectclientID7 = this.dropdownSelectclientID8;
      this.dropdownSelectclientID8 = this.dropdownSelectclientID9;
      this.dropdownSelectclientID9 = this.dropdownSelectclientID10;
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue4
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue2
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue3
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue5
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue6
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue7
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue8
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue9
      );

      if ((this.form.controls.coOwnerId2.value == null) && (this.form.controls.coOwnerId3.value == null) && (this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {

        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
      }
      if ((this.form.controls.coOwnerId3.value == null) && (this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
      }
      if ((this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
      }
      if ((this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
      }
      if ((this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
      }
      if ((this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );

      }
      if ((this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );

      }
      if ((this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );
        this.dropdownSelectclientID9 = this.dropdownSelectclientID81.filter(
          option => option.value !== selectedvalue8
        );

      }
      if ((this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );
        this.dropdownSelectclientID9 = this.dropdownSelectclientID81.filter(
          option => option.value !== selectedvalue8
        );
        this.dropdownSelectclientID10 = this.dropdownSelectclientID9.filter(
          option => option.value !== selectedvalue9
        );
      }
      if (this.js.pageflow == "New") {
        if ((this.form.controls.coOwnerName4.value != null) && (this.form.controls.coOwnerId4 == null)) {
          this.form.controls.coOwnerName4.value.patchValue("");
        } if ((this.form.controls.coOwnerName3.value != null) && (this.form.controls.coOwnerId3 == null)) {
          this.form.controls.coOwnerName3.value.patchValue("");
        }
        if ((this.form.controls.coOwnerName2.value != null) && (this.form.controls.coOwnerId2 == null)) {
          this.form.controls.coOwnerName2.value.patchValue("");
        }
        if ((this.form.controls.coOwnerName5.value != null) && (this.form.controls.coOwnerId5 == null)) {
          this.form.controls.coOwnerName5.value.patchValue("");
        }

      }
      this.form.controls.relationShipId5.patchValue(this.form.controls.relationShipId6.value);
      this.form.controls.relationShipId6.patchValue(this.form.controls.relationShipId7.value);
      this.form.controls.relationShipId7.patchValue(this.form.controls.relationShipId8.value);
      this.form.controls.relationShipId8.patchValue(this.form.controls.relationShipId9.value);
      this.form.controls.relationShipId9.patchValue(this.form.controls.relationShipId10.value);
      this.form.controls.relationShipName5.patchValue(this.form.controls.relationShipName6.value);
      this.form.controls.relationShipName6.patchValue(this.form.controls.relationShipName7.value);
      this.form.controls.relationShipName7.patchValue(this.form.controls.relationShipName8.value);
      this.form.controls.relationShipName8.patchValue(this.form.controls.relationShipName9.value);
      this.form.controls.relationShipName9.patchValue(this.form.controls.relationShipName10.value);
      this.form.controls.relationShipId10.patchValue("");
      this.form.controls.relationShipName10.patchValue("");
    }
    if (row === "clientName6") {
      // this.showFifthRow = false

      this.form.controls.coOwnerId6.patchValue(this.form.controls.coOwnerId7.value);
      this.form.controls.coOwnerPercentage6.patchValue(this.form.controls.coOwnerPercentage7.value);
      this.form.controls.coOwnerName6.patchValue(this.form.controls.coOwnerName7.value);
      this.form.controls.coOwnerId7.patchValue(this.form.controls.coOwnerId8.value);
      this.form.controls.coOwnerPercentage7.patchValue(this.form.controls.coOwnerPercentage8.value);
      this.form.controls.coOwnerName7.patchValue(this.form.controls.coOwnerName8.value);
      this.form.controls.coOwnerId8.patchValue(this.form.controls.coOwnerId9.value);
      this.form.controls.coOwnerPercentage8.patchValue(this.form.controls.coOwnerPercentage9.value);
      this.form.controls.coOwnerName8.patchValue(this.form.controls.coOwnerName9.value);
      this.form.controls.coOwnerId9.patchValue(this.form.controls.coOwnerId10.value);
      this.form.controls.coOwnerPercentage9.patchValue(this.form.controls.coOwnerPercentage10.value);
      this.form.controls.coOwnerName9.patchValue(this.form.controls.coOwnerName10.value);
      this.form.controls.coOwnerId10.patchValue("");
      this.form.controls.coOwnerPercentage10.patchValue("");
      this.form.controls.coOwnerName10.patchValue("");
      let selectedvalue = this.form.controls.coOwnerId1.value;
      let selectedvalue4 = this.form.controls.coOwnerId4.value;
      let selectedvalue2 = this.form.controls.coOwnerId2.value;
      let selectedvalue3 = this.form.controls.coOwnerId3.value;
      let selectedvalue5 = this.form.controls.coOwnerId5.value;
      let selectedvalue6 = this.form.controls.coOwnerId6.value;
      let selectedvalue7 = this.form.controls.coOwnerId7.value;
      let selectedvalue8 = this.form.controls.coOwnerId8.value;
      let selectedvalue9 = this.form.controls.coOwnerId9.value;
      this.dropdownSelectclientID2 = this.dropdownSelectclientID3;
      this.dropdownSelectclientID3 = this.dropdownSelectclientID4;
      this.dropdownSelectclientID4 = this.dropdownSelectclientID5;
      this.dropdownSelectclientID5 = this.dropdownSelectclientID6;
      this.dropdownSelectclientID6 = this.dropdownSelectclientID7;
      this.dropdownSelectclientID7 = this.dropdownSelectclientID8;
      this.dropdownSelectclientID8 = this.dropdownSelectclientID9;
      this.dropdownSelectclientID9 = this.dropdownSelectclientID10;
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue4
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue2
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue3
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue5
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue6
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue7
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue8
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue9
      );

      if ((this.form.controls.coOwnerId2.value == null) && (this.form.controls.coOwnerId3.value == null) && (this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {

        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
      }
      if ((this.form.controls.coOwnerId3.value == null) && (this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
      }
      if ((this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
      }
      if ((this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
      }
      if ((this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
      }
      if ((this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );

      }
      if ((this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );

      }
      if ((this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );
        this.dropdownSelectclientID9 = this.dropdownSelectclientID81.filter(
          option => option.value !== selectedvalue8
        );

      }
      if ((this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );
        this.dropdownSelectclientID9 = this.dropdownSelectclientID81.filter(
          option => option.value !== selectedvalue8
        );
        this.dropdownSelectclientID10 = this.dropdownSelectclientID9.filter(
          option => option.value !== selectedvalue9
        );
      }
      if (this.js.pageflow == "New") {
        if ((this.form.controls.coOwnerName4.value != null) && (this.form.controls.coOwnerId4 == null)) {
          this.form.controls.coOwnerName4.value.patchValue("");
        } if ((this.form.controls.coOwnerName3.value != null) && (this.form.controls.coOwnerId3 == null)) {
          this.form.controls.coOwnerName3.value.patchValue("");
        }
        if ((this.form.controls.coOwnerName2.value != null) && (this.form.controls.coOwnerId2 == null)) {
          this.form.controls.coOwnerName2.value.patchValue("");
        }
        if ((this.form.controls.coOwnerName5.value != null) && (this.form.controls.coOwnerId5 == null)) {
          this.form.controls.coOwnerName5.value.patchValue("");
        }

      }
      this.form.controls.relationShipId6.patchValue(this.form.controls.relationShipId7.value);
      this.form.controls.relationShipId7.patchValue(this.form.controls.relationShipId8.value);
      this.form.controls.relationShipId8.patchValue(this.form.controls.relationShipId9.value);
      this.form.controls.relationShipId9.patchValue(this.form.controls.relationShipId10.value);
      this.form.controls.relationShipName6.patchValue(this.form.controls.relationShipName7.value);
      this.form.controls.relationShipName7.patchValue(this.form.controls.relationShipName8.value);
      this.form.controls.relationShipName8.patchValue(this.form.controls.relationShipName9.value);
      this.form.controls.relationShipName9.patchValue(this.form.controls.relationShipName10.value);
      this.form.controls.relationShipId10.patchValue("");
      this.form.controls.relationShipName10.patchValue("");
    }
    if (row === "clientName10") {
      // this.showFifthRow = false;

      this.form.controls.relationShipId10.patchValue("");
      this.form.controls.relationShipName10.patchValue("");
      this.form.controls.coOwnerId10.patchValue("");
      this.form.controls.coOwnerPercentage10.patchValue("");
      this.form.controls.coOwnerName10.patchValue("");
      let selectedvalue = this.form.controls.coOwnerId1.value;
      let selectedvalue4 = this.form.controls.coOwnerId4.value;
      let selectedvalue2 = this.form.controls.coOwnerId2.value;
      let selectedvalue3 = this.form.controls.coOwnerId3.value;
      let selectedvalue5 = this.form.controls.coOwnerId5.value;
      let selectedvalue6 = this.form.controls.coOwnerId6.value;
      let selectedvalue7 = this.form.controls.coOwnerId7.value;
      let selectedvalue8 = this.form.controls.coOwnerId8.value;
      let selectedvalue9 = this.form.controls.coOwnerId9.value;
      this.dropdownSelectclientID2 = this.dropdownSelectclientID3;
      this.dropdownSelectclientID3 = this.dropdownSelectclientID4;
      this.dropdownSelectclientID4 = this.dropdownSelectclientID5;
      this.dropdownSelectclientID5 = this.dropdownSelectclientID6;
      this.dropdownSelectclientID6 = this.dropdownSelectclientID7;
      this.dropdownSelectclientID7 = this.dropdownSelectclientID81;
      this.dropdownSelectclientID81 = this.dropdownSelectclientID9;
      this.dropdownSelectclientID9 = this.dropdownSelectclientID10;
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue4
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue2
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue3
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue5
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue6
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue7
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue8
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue9
      );

      if ((this.form.controls.coOwnerId2.value == null) && (this.form.controls.coOwnerId3.value == null) && (this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {

        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
      }
      if ((this.form.controls.coOwnerId3.value == null) && (this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
      }
      if ((this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
      }
      if ((this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
      }
      if ((this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
      }
      if ((this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );

      }
      if ((this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );

      }
      if ((this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );
        this.dropdownSelectclientID9 = this.dropdownSelectclientID81.filter(
          option => option.value !== selectedvalue8
        );

      }
      if ((this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );
        this.dropdownSelectclientID9 = this.dropdownSelectclientID81.filter(
          option => option.value !== selectedvalue8
        );
        this.dropdownSelectclientID10 = this.dropdownSelectclientID9.filter(
          option => option.value !== selectedvalue9
        );
      }
      if (this.js.pageflow == "New") {
        if ((this.form.controls.coOwnerName4.value != null) && (this.form.controls.coOwnerId4 == null)) {
          this.form.controls.coOwnerName4.value.patchValue("");
        } if ((this.form.controls.coOwnerName3.value != null) && (this.form.controls.coOwnerId3 == null)) {
          this.form.controls.coOwnerName3.value.patchValue("");
        }
        if ((this.form.controls.coOwnerName2.value != null) && (this.form.controls.coOwnerId2 == null)) {
          this.form.controls.coOwnerName2.value.patchValue("");
        }
        if ((this.form.controls.coOwnerName5.value != null) && (this.form.controls.coOwnerId5 == null)) {
          this.form.controls.coOwnerName5.value.patchValue("");
        }

      }

    }
    if (row === "clientName9") {
      // this.showFifthRow = false;

      this.form.controls.coOwnerId9.patchValue(this.form.controls.coOwnerId10.value);
      this.form.controls.coOwnerPercentage9.patchValue(this.form.controls.coOwnerPercentage10.value);
      this.form.controls.coOwnerName9.patchValue(this.form.controls.coOwnerName10.value);
      this.form.controls.coOwnerId10.patchValue("");
      this.form.controls.coOwnerPercentage10.patchValue("");
      this.form.controls.coOwnerName10.patchValue("");
      let selectedvalue = this.form.controls.coOwnerId1.value;
      let selectedvalue4 = this.form.controls.coOwnerId4.value;
      let selectedvalue2 = this.form.controls.coOwnerId2.value;
      let selectedvalue3 = this.form.controls.coOwnerId3.value;
      let selectedvalue5 = this.form.controls.coOwnerId5.value;
      let selectedvalue6 = this.form.controls.coOwnerId6.value;
      let selectedvalue7 = this.form.controls.coOwnerId7.value;
      let selectedvalue8 = this.form.controls.coOwnerId8.value;
      let selectedvalue9 = this.form.controls.coOwnerId9.value;
      this.dropdownSelectclientID2 = this.dropdownSelectclientID3;
      this.dropdownSelectclientID3 = this.dropdownSelectclientID4;
      this.dropdownSelectclientID4 = this.dropdownSelectclientID5;
      this.dropdownSelectclientID5 = this.dropdownSelectclientID6;
      this.dropdownSelectclientID6 = this.dropdownSelectclientID7;
      this.dropdownSelectclientID7 = this.dropdownSelectclientID81;
      this.dropdownSelectclientID81 = this.dropdownSelectclientID9;
      this.dropdownSelectclientID9 = this.dropdownSelectclientID10;
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue4
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue2
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue3
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue5
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue6
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue7
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue8
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue9
      );

      if ((this.form.controls.coOwnerId2.value == null) && (this.form.controls.coOwnerId3.value == null) && (this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {

        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
      }
      if ((this.form.controls.coOwnerId3.value == null) && (this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
      }
      if ((this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
      }
      if ((this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
      }
      if ((this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
      }
      if ((this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );

      }
      if ((this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );

      }
      if ((this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );
        this.dropdownSelectclientID9 = this.dropdownSelectclientID81.filter(
          option => option.value !== selectedvalue8
        );

      }
      if ((this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );
        this.dropdownSelectclientID9 = this.dropdownSelectclientID81.filter(
          option => option.value !== selectedvalue8
        );
        this.dropdownSelectclientID10 = this.dropdownSelectclientID9.filter(
          option => option.value !== selectedvalue9
        );
      }
      if (this.js.pageflow == "New") {
        if ((this.form.controls.coOwnerName4.value != null) && (this.form.controls.coOwnerId4 == null)) {
          this.form.controls.coOwnerName4.value.patchValue("");
        } if ((this.form.controls.coOwnerName3.value != null) && (this.form.controls.coOwnerId3 == null)) {
          this.form.controls.coOwnerName3.value.patchValue("");
        }
        if ((this.form.controls.coOwnerName2.value != null) && (this.form.controls.coOwnerId2 == null)) {
          this.form.controls.coOwnerName2.value.patchValue("");
        }
        if ((this.form.controls.coOwnerName5.value != null) && (this.form.controls.coOwnerId5 == null)) {
          this.form.controls.coOwnerName5.value.patchValue("");
        }

      }
      this.form.controls.relationShipId9.patchValue(this.form.controls.relationShipId10.value);
      this.form.controls.relationShipName9.patchValue(this.form.controls.relationShipName10.value);
      this.form.controls.realtionShipId10.patchValue("");
      this.form.controls.realtionShipName10.patchValue("");
    }
    if (row === "clientName8") {
      // this.showFifthRow = false;



      this.form.controls.coOwnerId8.patchValue(this.form.controls.coOwnerId9.value);
      this.form.controls.coOwnerPercentage8.patchValue(this.form.controls.coOwnerPercentage9.value);
      this.form.controls.coOwnerName8.patchValue(this.form.controls.coOwnerName9.value);
      this.form.controls.coOwnerId9.patchValue(this.form.controls.coOwnerId10.value);
      this.form.controls.coOwnerPercentage9.patchValue(this.form.controls.coOwnerPercentage10.value);
      this.form.controls.coOwnerName9.patchValue(this.form.controls.coOwnerName10.value);
      this.form.controls.coOwnerId10.patchValue("");
      this.form.controls.coOwnerPercentage10.patchValue("");
      this.form.controls.coOwnerName10.patchValue("");
      let selectedvalue = this.form.controls.coOwnerId1.value;
      let selectedvalue4 = this.form.controls.coOwnerId4.value;
      let selectedvalue2 = this.form.controls.coOwnerId2.value;
      let selectedvalue3 = this.form.controls.coOwnerId3.value;
      let selectedvalue5 = this.form.controls.coOwnerId5.value;
      let selectedvalue6 = this.form.controls.coOwnerId6.value;
      let selectedvalue7 = this.form.controls.coOwnerId7.value;
      let selectedvalue8 = this.form.controls.coOwnerId8.value;
      let selectedvalue9 = this.form.controls.coOwnerId9.value;
      this.dropdownSelectclientID2 = this.dropdownSelectclientID3;
      this.dropdownSelectclientID3 = this.dropdownSelectclientID4;
      this.dropdownSelectclientID4 = this.dropdownSelectclientID5;
      this.dropdownSelectclientID5 = this.dropdownSelectclientID6;
      this.dropdownSelectclientID6 = this.dropdownSelectclientID7;
      this.dropdownSelectclientID7 = this.dropdownSelectclientID81;
      this.dropdownSelectclientID81 = this.dropdownSelectclientID9;
      this.dropdownSelectclientID9 = this.dropdownSelectclientID10;
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue4
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue2
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue3
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue5
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue6
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue7
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue8
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue9
      );

      if ((this.form.controls.coOwnerId2.value == null) && (this.form.controls.coOwnerId3.value == null) && (this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {

        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
      }
      if ((this.form.controls.coOwnerId3.value == null) && (this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
      }
      if ((this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
      }
      if ((this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
      }
      if ((this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
      }
      if ((this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );

      }
      if ((this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );

      }
      if ((this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );
        this.dropdownSelectclientID9 = this.dropdownSelectclientID81.filter(
          option => option.value !== selectedvalue8
        );

      }
      if ((this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );
        this.dropdownSelectclientID9 = this.dropdownSelectclientID81.filter(
          option => option.value !== selectedvalue8
        );
        this.dropdownSelectclientID10 = this.dropdownSelectclientID9.filter(
          option => option.value !== selectedvalue9
        );
      }
      if (this.js.pageflow == "New") {
        if ((this.form.controls.coOwnerName4.value != null) && (this.form.controls.coOwnerId4 == null)) {
          this.form.controls.coOwnerName4.value.patchValue("");
        } if ((this.form.controls.coOwnerName3.value != null) && (this.form.controls.coOwnerId3 == null)) {
          this.form.controls.coOwnerName3.value.patchValue("");
        }
        if ((this.form.controls.coOwnerName2.value != null) && (this.form.controls.coOwnerId2 == null)) {
          this.form.controls.coOwnerName2.value.patchValue("");
        }
        if ((this.form.controls.coOwnerName5.value != null) && (this.form.controls.coOwnerId5 == null)) {
          this.form.controls.coOwnerName5.value.patchValue("");
        }

      }
      this.form.controls.relationShipId8.patchValue(this.form.controls.relationShipId9.value);
      this.form.controls.relationShipId9.patchValue(this.form.controls.relationShipId10.value);
      this.form.controls.relationShipName8.patchValue(this.form.controls.relationShipName9.value);
      this.form.controls.relationShipName9.patchValue(this.form.controls.relationShipName10.value);
      this.form.controls.relationShipId10.patchValue("");
      this.form.controls.relationShipName10.patchValue("");
    }
    if (row === "clientName7") {
      // this.showFifthRow = false;


      this.form.controls.coOwnerId7.patchValue(this.form.controls.coOwnerId8.value);
      this.form.controls.coOwnerPercentage7.patchValue(this.form.controls.coOwnerPercentage8.value);
      this.form.controls.coOwnerName7.patchValue(this.form.controls.coOwnerName8.value);
      this.form.controls.coOwnerId8.patchValue(this.form.controls.coOwnerId9.value);
      this.form.controls.coOwnerPercentage8.patchValue(this.form.controls.coOwnerPercentage9.value);
      this.form.controls.coOwnerName8.patchValue(this.form.controls.coOwnerName9.value);
      this.form.controls.coOwnerId9.patchValue(this.form.controls.coOwnerId10.value);
      this.form.controls.coOwnerPercentage9.patchValue(this.form.controls.coOwnerPercentage10.value);
      this.form.controls.coOwnerName9.patchValue(this.form.controls.coOwnerName10.value);
      this.form.controls.coOwnerId10.patchValue("");
      this.form.controls.coOwnerPercentage10.patchValue("");
      this.form.controls.coOwnerName10.patchValue("");
      let selectedvalue = this.form.controls.coOwnerId1.value;
      let selectedvalue4 = this.form.controls.coOwnerId4.value;
      let selectedvalue2 = this.form.controls.coOwnerId2.value;
      let selectedvalue3 = this.form.controls.coOwnerId3.value;
      let selectedvalue5 = this.form.controls.coOwnerId5.value;
      let selectedvalue6 = this.form.controls.coOwnerId6.value;
      let selectedvalue7 = this.form.controls.coOwnerId7.value;
      let selectedvalue8 = this.form.controls.coOwnerId8.value;
      let selectedvalue9 = this.form.controls.coOwnerId9.value;
      this.dropdownSelectclientID2 = this.dropdownSelectclientID3;
      this.dropdownSelectclientID3 = this.dropdownSelectclientID4;
      this.dropdownSelectclientID4 = this.dropdownSelectclientID5;
      this.dropdownSelectclientID5 = this.dropdownSelectclientID6;
      this.dropdownSelectclientID6 = this.dropdownSelectclientID7;
      this.dropdownSelectclientID7 = this.dropdownSelectclientID81;
      this.dropdownSelectclientID81 = this.dropdownSelectclientID9;
      this.dropdownSelectclientID9 = this.dropdownSelectclientID10;
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue4
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue2
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue3
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue5
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue6
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue7
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue8
      );
      this.dropdownSelectclientID8 = this.dropdownSelectclientID8.filter(
        option => option.value !== selectedvalue9
      );

      if ((this.form.controls.coOwnerId2.value == null) && (this.form.controls.coOwnerId3.value == null) && (this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {

        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
      }
      if ((this.form.controls.coOwnerId3.value == null) && (this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
      }
      if ((this.form.controls.coOwnerId4.value == null) && (this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
      }
      if ((this.form.controls.coOwnerId5.value == "") && (this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
      }
      if ((this.form.controls.coOwnerId6.value == "") && (this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
      }
      if ((this.form.controls.coOwnerId7.value == "") && (this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );

      }
      if ((this.form.controls.coOwnerId8.value == "") && (this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );

      }
      if ((this.form.controls.coOwnerId9.value == "") && (this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );
        this.dropdownSelectclientID9 = this.dropdownSelectclientID81.filter(
          option => option.value !== selectedvalue8
        );

      }
      if ((this.form.controls.coOwnerId10.value == "")) {
        this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
          option => option.value !== selectedvalue);
        this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
          option => option.value !== selectedvalue2);
        this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
          option => option.value !== selectedvalue3);
        this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
          option => option.value !== selectedvalue4);
        this.dropdownSelectclientID6 = this.dropdownSelectclientID5.filter(
          option => option.value !== selectedvalue5);
        this.dropdownSelectclientID7 = this.dropdownSelectclientID6.filter(
          option => option.value !== selectedvalue6
        );
        this.dropdownSelectclientID81 = this.dropdownSelectclientID7.filter(
          option => option.value !== selectedvalue7
        );
        this.dropdownSelectclientID9 = this.dropdownSelectclientID81.filter(
          option => option.value !== selectedvalue8
        );
        this.dropdownSelectclientID10 = this.dropdownSelectclientID9.filter(
          option => option.value !== selectedvalue9
        );
      }
      if (this.js.pageflow == "New") {
        if ((this.form.controls.coOwnerName4.value != null) && (this.form.controls.coOwnerId4 == null)) {
          this.form.controls.coOwnerName4.value.patchValue("");
        } if ((this.form.controls.coOwnerName3.value != null) && (this.form.controls.coOwnerId3 == null)) {
          this.form.controls.coOwnerName3.value.patchValue("");
        }
        if ((this.form.controls.coOwnerName2.value != null) && (this.form.controls.coOwnerId2 == null)) {
          this.form.controls.coOwnerName2.value.patchValue("");
        }
        if ((this.form.controls.coOwnerName5.value != null) && (this.form.controls.coOwnerId5 == null)) {
          this.form.controls.coOwnerName5.value.patchValue("");
        }

      }
      this.form.controls.relationShipId7.patchValue(this.form.controls.relationShipId8.value);
      this.form.controls.relationShipId8.patchValue(this.form.controls.relationShipId9.value);
      this.form.controls.relationShipId9.patchValue(this.form.controls.relationShipId10.value);
      this.form.controls.relationShipName7.patchValue(this.form.controls.relationShipName8.value);
      this.form.controls.relationShipName8.patchValue(this.form.controls.relationShipName9.value);
      this.form.controls.relationShipName9.patchValue(this.form.controls.relationShipName10.value);
      this.form.controls.relationShipId10.patchValue("");
      this.form.controls.relationShipName10.patchValue("");
    }
  }
  open() {
    this.setupService.searchClientNew({
      languageId: [this.form.controls.languageId.value],
      companyId: [this.form.controls.companyId.value],
      storeId: [this.form.controls.storeId.value],
    }).subscribe(res => {
      if (res != null) {
        this.dropdownSelectcontrolgroupID = [];
        res.forEach(element => {
          if (element.groupTypeId != null) {
            this.dropdownSelectcontrolgroupID.push({
              value: element.groupTypeId,
              label: element.groupTypeId + '-' + element.groupTypeName,
              groupTypeName: element.groupTypeName,
            });
          }
        })
        this.dropdownSelectcontrolgroupID = this.cas.removeDuplicatesFromArrayNew(this.dropdownSelectcontrolgroupID);

      }
      if (res == "") {
        this.setupService.searchControlType({
          languageId: [this.form.controls.languageId.value],
          companyId: [this.form.controls.companyId.value],
          statusId: [0],
        }).subscribe(res => {
          this.dropdownSelectcontrolgroupID = [];
          res.forEach(element => {
            this.dropdownSelectcontrolgroupID.push({
              value: element.groupTypeId,
              label: element.groupTypeId + '-' + element.groupTypeName,
              groupTypeName: element.groupTypeName,
            });
            this.dropdownSelectcontrolgroupID = this.cas.removeDuplicatesFromArrayNew(this.dropdownSelectcontrolgroupID);
          });

          if (this.js.pageflow != 'New') {
            let GroupIdint: number = parseInt(this.js.groupTypeId);
            this.form.controls.groupTypeId.patchValue(GroupIdint);
          }
        })
      }
    });
  }
  fill() {
    this.showsecondRow = true;
    this.showthirdRow = true;
    this.showFifthRow = true;
    this.showFourthRow = true;
    this.showSixthRow = true;
    this.showSeventhRow = true;
    this.showEigthRow = true;
    this.showNinthRow = true;
    this.showTenthRow = true;

    if (this.js.pageflow == "Edit" || this.js.pageflow == "Display") {
      this.sub.add(this.service.Get(this.js.code, this.js.languageId, this.js.companyId).subscribe(res => {
        this.form.patchValue(res, {
          emitEvent: false
        });
        this.js.storeId = res.storeId;
        this.js.subGroupId = res.subGroupId;
        this.js.groupTypeId = res.groupTypeId;
        this.form.controls.createdOnFE.patchValue(this.cs.dateNewFormat1(this.form.controls.createdOn.value));
        this.form.controls.updatedOnFE.patchValue(this.cs.dateNewFormat1(this.form.controls.updatedOn.value));
        this.dropdownlist();
        console.log(this.rows.value);
        this.sub.add(this.service.Get1(this.form.controls.requestId.value).subscribe(res => {
          this.rows.clear();
          res.forEach((d: any) => this.addRow(d, false));
        }))
        if (this.js.pageflow == "Edit") {
          this.displayedColumns = ['chooseFile', 'fileName', 'status'];
        }
        if (this.js.pageflow == "Display") {
          this.displayedColumns = ['fileName', 'status'];
        }
      }, err => {
        this.cs.commonerror(err);
      }));
    }
    if (this.js.pageflow == "TransEdit") {
      this.sub.add(this.setupService.searchClientNew({
        languageId: [this.js.languageId],
        companyId: [this.js.companyId],
        storeId: [this.js.code],

      }).subscribe(res => {
        this.form.patchValue(res, {
          emitEvent: false
        })
      }))
      console.log(this.form.value);
    }
  }
  back() {
    this.location.back();
  }
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }
  @ViewChild(MatSort)
  sort: MatSort;
  @ViewChild(MatPaginator)
  paginator: MatPaginator; // Pagination



  uploadFiles: any;
  upload(): void {
    const dialogRef = this.dialog.open(UploadFilesComponent, {
      disableClose: true,
      width: '60%',
      maxWidth: '80%',
      data: this.js
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(result.lines);
      this.uploadFiles = null;
      this.uploadFiles = result.lines;
    })
  }



  findGroupType() {
    this.spin.show();
    let searchObj: any = {};
    if (this.form.controls.coOwnerId1.value != null)
      searchObj.coOwnerId1 = this.form.controls.coOwnerId1.value;

    if (this.form.controls.coOwnerId2.value != null)
      searchObj.coOwnerId2 = this.form.controls.coOwnerId2.value;

    if (this.form.controls.coOwnerId3.value != null)
      searchObj.coOwnerId3 = this.form.controls.coOwnerId3.value;

    if (this.form.controls.coOwnerId4.value != null)
      searchObj.coOwnerId4 = this.form.controls.coOwnerId4.value;

    if (this.form.controls.coOwnerId5.value != null)
      searchObj.coOwnerId5 = this.form.controls.coOwnerId5.value;
    if (this.form.controls.coOwnerId6.value != null)
      searchObj.coOwnerId6 = this.form.controls.coOwnerId6.value;
    if (this.form.controls.coOwnerId7.value != null)
      searchObj.coOwnerId7 = this.form.controls.coOwnerId7.value;
    if (this.form.controls.coOwnerId8.value != null)
      searchObj.coOwnerId8 = this.form.controls.coOwnerId8.value;
    if (this.form.controls.coOwnerId9.value != null)
      searchObj.coOwnerId9 = this.form.controls.coOwnerId9.value;
    if (this.form.controls.coOwnerId10.value != null)
      searchObj.coOwnerId10 = this.form.controls.coOwnerId10.value;

    this.dropdownSelectcontrolgroupID = [];
    this.dropdownSelectsubgrouptypeID = [];

    this.ValidationService.findGroupbackup(searchObj).subscribe((res: any) => {
      if (res.exactMatchGroups.length > 0) {
        res.exactMatchGroups.forEach(element => {
          element.stores.forEach(storeList => {
            this.dropdownSelectcontrolgroupID.push({
              value: storeList.groupTypeId,
              label: storeList.groupType,
              groupTypeName: storeList.groupType,
            });

            this.dropdownSelectsubgrouptypeID.push({
              value: storeList.subGroupTypeId,
              label: storeList.subGroupType,
              subGroupName: storeList.subGroupType,
            });
          });
        });

        this.dropdownSelectcontrolgroupID = this.cs.removeDuplicateInArray(this.dropdownSelectcontrolgroupID);
        this.dropdownSelectsubgrouptypeID = this.cs.removeDuplicateInArray(this.dropdownSelectsubgrouptypeID);

        if (this.dropdownSelectcontrolgroupID.length > 0)
          this.form.controls.groupTypeId.patchValue(this.dropdownSelectcontrolgroupID[0].value);

        if (this.dropdownSelectsubgrouptypeID.length > 0)
          this.form.controls.subGroupId.patchValue(this.dropdownSelectsubgrouptypeID[0].value);

        if (this.form.controls.groupTypeId.value)
          this.form.controls.groupTypeId.patchValue(this.form.controls.groupTypeId.value);

        if (this.form.controls.subGroupId.value)
          this.form.controls.subGroupId.patchValue(this.form.controls.subGroupId.value);

      }
      this.spin.hide();
    }, err => {
      this.spin.hide();
      this.cs.commonerrorNew(err);
    })
  }

  clearGroupType() {
    this.setupService.searchControlType({
      languageId: [this.form.controls.languageId.value],
      companyId: [this.form.controls.companyId.value],
      statusId: [0],
    }).subscribe(res => {
      this.dropdownSelectcontrolgroupID = [];
      res.forEach(element => {
        this.dropdownSelectcontrolgroupID.push({
          value: element.groupTypeId,
          label: element.groupTypeId + '-' + element.groupTypeName,
          groupTypeName: element.groupTypeName,
        });
      });
    }),

      this.dropdownSelectcontrolgroupID = this.cas.removeDuplicatesFromArrayNew(this.dropdownSelectcontrolgroupID);
  }

  clearSubGroupType() {
    this.setupService.searchSubGroupType({
      languageId: [this.form.controls.languageId.value],
      companyId: [this.form.controls.companyId.value],
      groupTypeId: [this.form.controls.groupTypeId.value],
    }).subscribe(res => {
      this.dropdownSelectsubgrouptypeID = [];
      res.forEach(element => {
        this.dropdownSelectsubgrouptypeID.push({
          value: element.subGroupTypeId,
          label: element.subGroupTypeId + '-' + element.subGroupTypeName,
          subGroupName: element.subGroupTypeName,
        });

      });
    });
  }
}
