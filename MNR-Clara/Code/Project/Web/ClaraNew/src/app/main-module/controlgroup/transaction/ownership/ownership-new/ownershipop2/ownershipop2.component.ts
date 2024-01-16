import {
  Component,
  OnInit,
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
  DialogExampleComponent
} from 'src/app/common-field/dialog-example/dialog-example.component';
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
  Subscription
} from 'rxjs';
import {
  SubgroupNewComponent
} from 'src/app/main-module/controlgroup/idmaster/subgroup/subgroup-new/subgroup-new.component';
import {
  MatTableDataSource
} from '@angular/material/table';
import {
  SelectionModel
} from '@angular/cdk/collections';

@Component({
  selector: 'app-ownershipop2',
  templateUrl: './ownershipop2.component.html',
  styleUrls: ['./ownershipop2.component.scss']
})
export class Ownershipop2Component implements OnInit {


  constructor(public dialog: MatDialog,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef6: MatDialogRef < DialogExampleComponent > ,
    private fb: FormBuilder,
    private auth: AuthService,
    private service: OwnershipService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private setupService: SetupServiceService,
    private route: ActivatedRoute, private router: Router,
    private cs: CommonService, ) {}
  form = this.fb.group({
    coOwnerId1: [],
    coOwnerId2: [],
    coOwnerId3: [],
    coOwnerId4: [],
    coOwnerId5: [],
    coOwnerName1: [],
    coOwnerName2: [],
    coOwnerName3: [],
    coOwnerName4: [],
    coOwnerName5: [],
    coOwnerPercentage1: [],
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
    groupTypeId: [, [Validators.required]],
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
    statusId: [1,],
    storeId: [],
    storeName: [],
    subGroupId: [, [Validators.required]],
    subGroupName: [],
    updatedBy: [],
    updatedOn: [],

  });
  ngOnInit(): void {
    this.fill();
  }
  sub = new Subscription();
  dropdownSelectcontrolgroupID: any[] = [];

  dropdownSelectsubgrouptypeID: any[] = [];
  // dropdownlist() {
  //   this.spin.show();
  //   this.cas.getalldropdownlist([
  //     this.cas.dropdownlist.cgsetup.controlgrouptype.url,
  //     this.cas.dropdownlist.cgsetup.subgrouptype.url,
  //   ]).subscribe((results) => {

  //     this.setupService.searchControlGroup({
  //       languageId: [this.form.controls.languageId.value],
  //       companyId: [this.form.controls.companyId.value],
  //       statusId: [0]
  //     }).subscribe(res => {
  //       this.dropdownSelectcontrolgroupID = [];
  //       res.forEach(element => {
  //         this.dropdownSelectcontrolgroupID.push({
  //           value: element.groupTypeId,
  //           label: element.groupTypeId + '-' + element.groupTypeName
  //         });
  //         let dropdownSelectcontrolgroupID = res.filter(x => x.dropdownSelectcontrolgroupID == 0);
  //       });

  //     });
  //     this.setupService.searchSubGroupType({
  //       languageId: [this.form.controls.languageId.value],
  //       companyId: [this.form.controls.companyId.value],
  //       statusId: [0],
  //       groupTypeId: [this.form.controls.groupTypeId.value]
  //     }).subscribe(res => {
  //       this.dropdownSelectsubgrouptypeID = [];
  //       res.forEach(element => {
  //         this.dropdownSelectsubgrouptypeID.push({
  //           value: element.subGroupTypeId,
  //           label: element.subGroupTypeId + '-' + element.subGroupTypeName
  //         });
  //       });
  //     });
  //     this.spin.hide();
  //     //console.log(this.dropdownSelectLanguageID);
  //   }, (err) => {
  //     this.toastr.error(err, "");
  //     this.spin.hide();
  //   });

  // }
  email = new FormControl('', [Validators.required, Validators.email]);
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
  ongrouptypeChange(value) {
    console.log(this.dropdownSelectsubgrouptypeID);
    this.form.controls.groupTypeName.patchValue(value.groupTypeName);
    this.setupService.searchSubGroupType({
      languageId: [this.data.languageId],
      companyId: [this.data.companyId],
      groupTypeId: [value.value],
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
  onsubgroupTypechange(value){
    this.form.controls.subGroupName.patchValue(value.subGroupName);
  }
  submitted = false;
  submit() {
    this.submitted = true;
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
    this.spin.show();
    this.form.removeControl('updatedOn');
    this.form.removeControl('createdOn');
    this.form.patchValue({
      updatedby: this.auth.username
    });
    this.form.controls.statusId.patchValue(1);
    this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
      this.toastr.success(res.requestId + " saved successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      this.spin.hide();
      this.dialogRef6.close();
      this.router.navigate(['/main/controlgroup/transaction/summary']);

    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();

    }));



  };
  ELEMENT_DATA: any[] = [];
  dataSource = new MatTableDataSource < any > (this.ELEMENT_DATA);
  selection = new SelectionModel < any > (true, []);
  calcdues: false;
  createSubgroup(data: any = 'new'): void {
    const dialogRef = this.dialog.open(SubgroupNewComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: {
        top: '6.5%'
      },
      data: {
        pageflow: data,
        code: data != 'New' ? this.selection.selected[0].subGroupTypeId : null,
        languageId: data != 'New' ? this.selection.selected[0].languageId : null,
        companyId: data != 'New' ? this.selection.selected[0].companyId : null,
        groupTypeId: data != 'New' ? this.selection.selected[0].groupTypeId : null,
        versionNumber: data != 'New' ? this.selection.selected[0].versionNumber : null
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      this.setupService.searchClientNew({
        languageId: [this.data.languageId],
        companyId: [this.data.companyId],
        coOwnerId1: this.data.coOwnerId1,
        coOwnerId2: this.data.coOwnerId2,
        coOwnerId3: this.data.coOwnerId3,
        coOwnerId4: this.data.coOwnerId4,
        coOwnerId5: this.data.coOwnerId5,
      }).subscribe(res => {
        this.dropdownSelectsubgrouptypeID = [];
        res.forEach(element => {
          this.dropdownSelectsubgrouptypeID.push({
            value: element.subGroupId,
            label: element.subGroupId + '-' + element.subGroupName
          });
          this.dropdownSelectsubgrouptypeID = this.cas.removeDuplicatesFromArray(this.dropdownSelectsubgrouptypeID);
        })
      });
    });

  }
  fill() {
    console.log(this.data);
    this.form.patchValue(this.data);
    this.setupService.searchClientNew({
      languageId: [this.data.languageId],
      companyId: [this.data.companyId],
      coOwnerId1: this.data.coOwnerId1,
      coOwnerId2: this.data.coOwnerId2,
      coOwnerId3: this.data.coOwnerId3,
      coOwnerId4: this.data.coOwnerId4,
      coOwnerId5: this.data.coOwnerId5,
    }).subscribe(res => {
      this.dropdownSelectcontrolgroupID = [];
      res.forEach(element => {
        if(element.groupTypeId != null){
          console.log(element);
          this.dropdownSelectcontrolgroupID.push({
            value: element.groupTypeId,
            label: element.groupTypeId + '-' + element.groupTypeName,
            groupTypeName: element.groupTypeName,
          });
        }
      })
      this.dropdownSelectcontrolgroupID = this.cas.removeDuplicatesFromArrayNew(this.dropdownSelectcontrolgroupID);
    });
  }
}
