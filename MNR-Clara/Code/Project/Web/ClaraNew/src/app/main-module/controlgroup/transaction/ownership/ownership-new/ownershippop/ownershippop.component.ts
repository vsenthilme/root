import {
  SelectionModel
} from '@angular/cdk/collections';
import {
  Component,
  OnInit,
  ViewChild,
  Inject
} from '@angular/core';
import {
  FormBuilder,
  FormControl,
  Validators
} from '@angular/forms';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef
} from '@angular/material/dialog';
import {
  MatPaginator
} from '@angular/material/paginator';
import {
  MatSort
} from '@angular/material/sort';
import {
  MatTableDataSource
} from '@angular/material/table';
import {
  ActivatedRoute,
  Router
} from '@angular/router';
import {
  NgxSpinnerService
} from 'ngx-spinner';
import {
  ToastrService
} from 'ngx-toastr';
import {
  SelectItem
} from 'primeng/api';
import {
  Observable,
  Subscription
} from 'rxjs';
import {
  CommonApiService
} from 'src/app/common-service/common-api.service';
import {
  CommonService
} from 'src/app/common-service/common-service.service';
import {
  SetupServiceService
} from 'src/app/common-service/setup-service.service';
import {
  AuthService
} from 'src/app/core/core';
import {
  OwnershipService
} from '../../ownership.service';
import {
  DialogExampleComponent
} from 'src/app/common-field/dialog-example/dialog-example.component';
import { ClientNewComponent } from 'src/app/main-module/controlgroup/master/client/client-new/client-new.component';

@Component({
  selector: 'app-ownershippop',
  templateUrl: './ownershippop.component.html',
  styleUrls: ['./ownershippop.component.scss']
})
export class OwnershippopComponent implements OnInit {
  screenid: 1135 | undefined;
  displayedColumns: string[] = ['sno', 'quantity', 'itemDescription', 'rateperHour'];
  public icon = 'expand_more';
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  clientList: any;
  matterList: any;
  filtersmatter: any;
  showAddButton: boolean = true; // Initially, the button is shown
  showFourthRow = false;
  showFifthRow = false;

  count = 0;
  toggleRows() {
    this.count = this.count + 1;
    if (this.count === 1) {
      this.showFourthRow = true;
      this.showFifthRow = false; // Ensure only one row is shown at a time
    } else if (this.count === 2) {
      this.showFourthRow = true;
      this.showFifthRow = true;
    } else {
      this.showFourthRow = false;
      this.showFifthRow = false;
      this.count = 0; // Reset the count
    }

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

  ELEMENT_DATA: any[] = [];
  dataSource = new MatTableDataSource < any > (this.ELEMENT_DATA);
  selection = new SelectionModel < any > (true, []);

  input: any;
  isbtntext = true;

  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);

  isbtntexts = false;
  isbtntextr = false;
  form = this.fb.group({
    coOwnerId1: [,[Validators.required]],
    coOwnerId2: [],
    coOwnerId3: [],
    coOwnerId4: [],
    coOwnerId5: [],
    coOwnerName1: [,[Validators.required]],
    coOwnerName2: [],
    coOwnerName3: [],
    coOwnerName4: [],
    coOwnerName5: [],
    coOwnerPercentage1: [,[Validators.required]],
    coOwnerPercentage2: [],
    coOwnerPercentage3: [],
    coOwnerPercentage4: [],
    coOwnerPercentage5: [],
    companyId: [],
    createdBy: [],
    createdOn: [],
    deletionIndicator: [],
    groupId: [],
    groupName: [],
    groupTypeId: [],
    groupTypeName: [],
    languageId: [],
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
    requestId: [],
    statusId: [],
    storeId: [],
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
  onDelete(row) {
    // if clientName1 enable the specific rows of it.
    if (row === "clientName4") {
      this.showFourthRow = false;
      this.form.controls.coOwnerId4.reset();
      this.form.controls.coOwnerId4.patchValue(0);
      this.form.controls.coOwnerName4.patchValue("");
      this.form.controls.coOwnerPercentage4.patchValue(0);

    }
    if (row === "clientName1") {
      this.showFourthRow = false;
      this.form.controls.coOwnerId1.reset();
      this.form.controls.coOwnerId1.patchValue(0);
      this.form.controls.coOwnerName1.patchValue("");
      this.form.controls.coOwnerPercentage1.patchValue(0);

    }
    if (row === "clientName2") {
      this.showFourthRow = false;
      this.form.controls.coOwnerId2.reset();
      this.form.controls.coOwnerId2.patchValue(0);
      this.form.controls.coOwnerName2.patchValue("");
      this.form.controls.coOwnerPercentage2.patchValue(0);

    }
    if (row === "clientName3") {
      this.showFourthRow = false;
      this.form.controls.coOwnerId3.reset();
      this.form.controls.coOwnerId3.patchValue(0);
      this.form.controls.coOwnerName3.patchValue("");
      this.form.controls.coOwnerPercentage3.patchValue(0);

    }
    if (row === "clientName5") {
      this.showFourthRow = false;
      this.form.controls.coOwnerId5.reset();
      this.form.controls.coOwnerId5.patchValue(0);
      this.form.controls.coOwnerName5.patchValue("");
      this.form.controls.coOwnerPercentage5.patchValue(0);
    }
  }
  onChange(row, event) {
    // if clientName1 enable the specific rows of it.
    if (row == "clientName1") {
      if (event.checked == true) {
        this.form.disable();
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
        this.form.disable();
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
        this.form.disable();
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
        this.form.disable();
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
  calculateTotal(): number {
    const percentage1 = +this.form.controls.coOwnerPercentage1.value || 0;
    const percentage2 = +this.form.controls.coOwnerPercentage2.value || 0;
    const percentage3 = +this.form.controls.coOwnerPercentage3.value || 0;
    const percentage4 = +this.form.controls.coOwnerPercentage4.value || 0;
    const percentage5 = +this.form.controls.coOwnerPercentage5.value || 0;
    let total = percentage1 + percentage2 + percentage3 + percentage4 + percentage5;
    if (total > 100) {
      this.toastr.error("Total Percentage of Share exceeds 100!Kindly Check","Notification", {
        timeOut: 2000,
        progressBar: false,
      })
    }
    if (total < 100 && total>0) {
      this.toastr.error("Total should not be less than 100!Kindly Check","Notification", {
        timeOut: 2000,
        progressBar: false,
      })
    }
    
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

  constructor(private fb: FormBuilder,
    public dialogRef5: MatDialogRef < DialogExampleComponent > ,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialog: MatDialog,
    private auth: AuthService,
    private service: OwnershipService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private setupService: SetupServiceService,
    private route: ActivatedRoute, private router: Router,
    private cs: CommonService, ) {}
  js: any = {}
  ngOnInit(): void {
    this.form.controls.referenceField10.disable();
    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOn.disable();
    console.log(this.dropdownSelectclientID);
    this.dropdownlist();
    if (this.data.pageflow != 'New') {
      if (this.data.pageflow == 'Display')
        this.form.disable();
      this.fill();
      if (this.data.pageflow == "Edit" && this.data.pageflow == "Display") {
        this.showFifthRow = true;
        this.showFourthRow = true;
      }
    }
    this.spin.hide();
  }
  fill() {
    this.spin.show();
    console.log(this.data);
    this.sub.add(this.service.Get(this.data.code, this.data.languageId, this.data.companyId).subscribe(res => {
      this.form.patchValue(res, {
        emitEvent: false
      });
      this.dropdownlist();
      this.showFifthRow = true;
      this.showFourthRow = true;
      this.spin.hide();
     }, err => {
       this.cs.commonerror(err);
       this.spin.hide();
     }));
   
  }
  selectedItems2: SelectItem[] = [];
  dropdownSelectcompanyID: any[] = [];
  dropdownSelectcountryID: any[] = [];
  dropdownSelectcontroltypeID: any[] = [];
  dropdownSelectclientID: any[] = [];
  dropdownSelectgroupID: any[] = [];
  dropdownSelectcontrolgroupID: any[] = [];
  dropdownSelectclientID1: any[] = [];
  dropdownSelectclientID2: any[] = [];
  dropdownSelectclientID3: any[] = [];


  onClientChange1(value) {
    console.log(value);
      this.form.controls.coOwnerName1.patchValue(value.clientName);
    let selectedvalue = this.form.controls.coOwnerId1.value;
    this.dropdownSelectclientID2 = this.dropdownSelectclientID.filter(
      option => option.value !== selectedvalue
    );
    console.log(this.dropdownSelectclientID2);
  }
  onClientChange2(value) {
    console.log(value);
    this.form.controls.coOwnerName2.patchValue(value.clientName);
    let selectedvalue2 = this.form.controls.coOwnerId2.value;
    this.dropdownSelectclientID3 = this.dropdownSelectclientID2.filter(
      option => option.value !== selectedvalue2
    );
    console.log(this.dropdownSelectclientID3)
  }
  dropdownSelectclientID4: any[] = [];
  onClientChange3(value) {
    console.log(value);
      this.form.controls.coOwnerName3.patchValue(value.clientName);
    let selectedvalue3 = this.form.controls.coOwnerId3.value;
    this.dropdownSelectclientID4 = this.dropdownSelectclientID3.filter(
      option => option.value !== selectedvalue3
    );
  }
  dropdownSelectclientID5: any[] = [];
  onClientChange4(value) {
    console.log(value);
      this.form.controls.coOwnerName4.patchValue(value.clientName);
    let selectedvalue4 = this.form.controls.coOwnerId4.value;
    console.log(selectedvalue4);
    this.dropdownSelectclientID5 = this.dropdownSelectclientID4.filter(
      option => option.value !== selectedvalue4
    );
  }
  onClientChange5(value) {
    console.log(value);
      this.form.controls.coOwnerName5.patchValue(value.clientName);
  }

  languageIdList: any[] = [];
  companyList: any[] = [];
  controltypeList: any[] = [];
  dropdownSelectstoreID: any[] = [];
  storeList: any[] = [];
  groupList: any[] = [];
  clientList2: any[] = [];
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.cgsetup.client.url,
      //this.cas.dropdownlist.cgsetup.client1.url,
    ]).subscribe((results) => {
      this.setupService.searchClient({
        languageId: [this.data.languageId],
        companyId: [this.data.companyId],
        statusId: [0],
      
      }).subscribe(res => {
        console.log(res);
        this.dropdownSelectclientID = [];
        res.forEach(element => {
          this.dropdownSelectclientID.push({
            value: element.clientId,
            label: element.clientId + '-' + element.clientName,
            clientName:element.clientName,
          });
        });
        this.dropdownSelectclientID2 = [];
        res.forEach(element => {
          this.dropdownSelectclientID2.push({
            value: element.clientId,
            label: element.clientId + '-' + element.clientName,
            clientName:element.clientName,
          });
        });
        this.dropdownSelectclientID3 = [];
        res.forEach(element => {
          this.dropdownSelectclientID3.push({
            value: element.clientId,
            label: element.clientId + '-' + element.clientName,
            clientName:element.clientName,
          });
        });
        this.dropdownSelectclientID4 = [];
        res.forEach(element => {
          this.dropdownSelectclientID4.push({
            value: element.clientId,
            label: element.clientId + '-' + element.clientName,
            clientName:element.clientName,
          });
        });
        this.dropdownSelectclientID5 = [];
        res.forEach(element => {
          this.dropdownSelectclientID5.push({
            value: element.clientId,
            label: element.clientId + '-' + element.clientName,
            clientName:element.clientName,
          });
        });
      });
     // this.clientList2 = this.cas.foreachlist(results[1], this.cas.dropdownlist.cgsetup.client1.key);
      this.clientList2.forEach((x: {
        key: string;value: string;
      }) => this.dropdownSelectclientID1.push({
        value: x.key,
        label: x.value
      }))
      this.dropdownSelectclientID1 = this.cs.removeDuplicateInArray(this.dropdownSelectclientID1);
      this.spin.hide();
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });

    this.spin.hide();
  }
  calcdues:false;
  createClient(data: any = 'new'): void {
  const dialogRef = this.dialog.open(ClientNewComponent, {
    disableClose: true,
    width: '50%',
    maxWidth: '80%',
    position: { top: '6.5%' },
    data: { pageflow: data, code: data != 'New' ? this.selection.selected[0].clientId : null,languageId: data != 'New' ? this.selection.selected[0].languageId : null,companyId: data != 'New' ? this.selection.selected[0].companyId : null }
  });

    dialogRef.afterClosed().subscribe(result => {
      this.setupService.searchClient({
        languageId: [this.data.languageId],
        companyId: [this.data.companyId],
        statusId: [0],
      
      }).subscribe(res => {
        console.log(res);
        this.dropdownSelectclientID = [];
        res.forEach(element => {
          this.dropdownSelectclientID.push({
            value: element.clientId,
            label: element.clientId + '-' + element.clientName,
            clientName:element.clientName,
          });
        });
      });

    });

  }
  list: any = []
  path = "New";
  btntext = "Save";
  //data: any;
  submit3() {
    if((this.form.controls.coOwnerId1.value == null )){
      this.toastr.error("Kindly Fill a CO-Owner Details","Notification", {
        timeOut: 2000,
        progressBar: false,
      }
    
    );
    
    }
    if((this.form.controls.coOwnerPercentage1.value == null )){
      this.toastr.error("Kindly Fill a CO-Owner Percentage Details","Notification", {
        timeOut: 2000,
        progressBar: false,
      }
    
    );
    
    }
    
    if(this.form.controls.coOwnerId1.value !=null && this.form.controls.coOwnerPercentage1.value !=null){
    let obj1: any = {};
    obj1.data = this.form.value;
    obj1.data.languageId = this.data.languageId;
    obj1.data.companyId = this.data.companyId;
    obj1.data.storeId = this.data.storeId;
    obj1.data.storeName = this.data.storeName;
    this.dialogRef5.close(obj1);
  }
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
}
