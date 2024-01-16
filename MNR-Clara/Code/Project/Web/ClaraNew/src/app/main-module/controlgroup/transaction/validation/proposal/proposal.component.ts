import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { SetupServiceService } from 'src/app/common-service/setup-service.service';
import { AuthService } from 'src/app/core/core';
import { ControlgroupNewComponent } from '../../../master/controlgroup/controlgroup-new/controlgroup-new.component';
import { OwnershipService } from '../../ownership/ownership.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { ConfirmComponent } from 'src/app/common-field/dialog_modules/confirm/confirm.component';
import { ControlgroupService } from '../../../master/controlgroup/controlgroup.service';
import { ValidationService } from '../validation.service';
import { Location } from '@angular/common';
import { AdminApprovalComponent } from '../../admin-approval/admin-approval.component';
import { ApprovalService } from '../../approval/approval.service';
import { SummaryService } from '../../summary/summary.service';



const ELEMENT_DATA: any[] = [
  {
    "groupName": "Brother Sister #354",
    "groupId": "35",
    "stores": [
      {
        "storeName": "11-LMMM HOUSTON #11 LTD",
        "storeId": "114",
        "coOwnerName1": "Salvador Ortega",
        "coOwnerName2": "ROGA LLC",
        "coOwnerName3": "Miguel Cervantes",
        "coOwnerName4": "Salvador Romo",
        "coOwnerName5": "Magdalena Ortega",
        "coOwnerPercentage1": "40",
        "coOwnerPercentage2": "20",
        "coOwnerPercentage3": "10",
        "coOwnerPercentage4": "20",
        "coOwnerPercentage5": "10",
        "groupType": "2 - Name",
        "subGroupType": "Brothers & Sisters"
      },
      {
        "storeName": "3-ROMOS MEAT MARKET LTD",
        "storeId": "143",
        "coOwnerName1": "10012-Salvador Ortega",
        "coOwnerName2": "10014-ROGA LLC",
        "coOwnerPercentage1": "10",
        "coOwnerPercentage2": "20",
        "groupType": "2 - Name",
        "subGroupType": "43-Name1"
      },
      {
        "storeName": "4-ROMOS MEAT MARKET LTD",
        "storeId": "148",
        "coOwnerName1": "10012-Salvador Ortega",
        "coOwnerName2": "10014-ROGA LLC",
        "coOwnerPercentage1": "10",
        "coOwnerPercentage2": "20",
        "groupType": "2 - Name",
        "subGroupType": "43-Name1"
      },
      {
        "storeName": "2-ROMOS MEAT MARKET LTD",
        "storeId": "141",
        "coOwnerName1": "10012-Salvador Ortega",
        "coOwnerName2": "10014-ROGA LLC",
        "coOwnerPercentage1": "10",
        "coOwnerPercentage2": "20",
        "groupType": "2 - Name",
        "subGroupType": "43-Name1"
      }
    ]
  },
  {
    "groupName": "Group Name1",
    "groupId": "3",
    "stores": [
      {
        "storeName": "6-ROMOS MEAT MARKET LTD",
        "storeId": "123",
        "coOwnerName1": "10012-Salvador Ortega",
        "coOwnerName2": "10014-ROGA LLC",
        "coOwnerName3": "10017-Miguel Cervantes",
        "coOwnerName4": "10016-Salvador Romo",
        "coOwnerPercentage1": "30",
        "coOwnerPercentage2": "40",
        "coOwnerPercentage3": "30",
        "coOwnerPercentage4": "10",
        "groupType": "2 - Name",
        "subGroupType": "43-Name1"
      },
      {
        "storeName": "3-ROMOS MEAT MARKET LTD",
        "storeId": "137",
        "coOwnerName1": "10012-Salvador Ortega",
        "coOwnerName2": "10014-ROGA LLC",
        "coOwnerPercentage1": "30",
        "coOwnerPercentage2": "40",
        "groupType": "2 - Name",
        "subGroupType": "43-Name1"
      }
    ]
  }
]



@Component({
  selector: 'app-proposal',
  templateUrl: './proposal.component.html',
  styleUrls: ['./proposal.component.scss']
})
export class ProposalComponent implements OnInit {
  public icon = 'expand_more';
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  showsavebutton = true;
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

  constructor(
    private route: ActivatedRoute,
    public toastr: ToastrService,
    private cas: CommonApiService, public dialog: MatDialog, private storePartner: OwnershipService, private router: Router,
    private spin: NgxSpinnerService,
    private auth: AuthService, 
    private setupService: SetupServiceService,
    private cs: CommonService, private location: Location,
    private service: SetupServiceService,
    private existingGroupService: ControlgroupService,
    private validationService: ValidationService,
    private summary: SummaryService, private storePartnerListring: ApprovalService, private pdf: AdminApprovalComponent,
    private fb: FormBuilder
  ) { }

  selectedValue: any;
  showProposed = "1";
  groupIdSelected = false;
  groupIdSelected1 = false;

  form = this.fb.group({
    showProposed1: [1,],
  })

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
  js: any = {}
  result1: any[] = [];
  result3: any[] = [];
  Display: any[] = [];
  overwriteResult: any[] = [];
  overwriteGroupResult: any[] = [];



  ngOnInit(): void {
    let code = this.route.snapshot.params.code;
    this.js = this.cs.decrypt(code);
    this.findGroupId();
    this.js.line.groupId = null;
    this.js.line.groupIdName = null;

    this.dropdownlist();
  }


  result2: any[] = [];
  TableResult: any;
  findGroupId() {
    let searchObj: any = {};
    if (this.js.line.coOwnerId1 != null)
      searchObj.coOwnerId1 = this.js.line.coOwnerId1;

    if (this.js.line.coOwnerId2 != null)
      searchObj.coOwnerId2 = this.js.line.coOwnerId2;

    if (this.js.line.coOwnerId3 != null)
      searchObj.coOwnerId3 = this.js.line.coOwnerId3;

    if (this.js.line.coOwnerId4 != null)
      searchObj.coOwnerId4 = this.js.line.coOwnerId4;

    if (this.js.line.coOwnerId5 != null)
      searchObj.coOwnerId5 = this.js.line.coOwnerId5;
    if (this.js.line.coOwnerId6 != null)
      searchObj.coOwnerId6 = this.js.line.coOwnerId6;
    if (this.js.line.coOwnerId7 != null)
      searchObj.coOwnerId7 = this.js.line.coOwnerId7;
    if (this.js.line.coOwnerId8 != null)
      searchObj.coOwnerId8 = this.js.line.coOwnerId8;
    if (this.js.line.coOwnerId9 != null)
      searchObj.coOwnerId9 = this.js.line.coOwnerId9;
    if (this.js.line.coOwnerId10 != null)
      searchObj.coOwnerId10 = this.js.line.coOwnerId10;

    this.validationService.findGroup(searchObj).subscribe((res: any) => {
      if (res.likeMatchGroup.length > 0) {
        this.result1 = res.exactMatchGroups;
      }
      if (res.likeMatchGroup.length > 0) {
        this.result3 = res.likeMatchGroup;

        if (this.js.pageflow == 'Display') {
          this.Display = this.result3.filter(x => x.groupId == this.js.line.groupId);
        }
      }
      else {
        this.toastr.warning("No group id found for the selected co-owners!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
      }
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
    })
  }
  submit() {

    if (this.js.line.groupId == null || this.js.line.groupName == null) {
      this.toastr.error("Please assign group id to continue", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      this.cs.notifyOther(true);
      return;
    }
    this.spin.show();
    this.js.line.statusId = 3;
    this.storePartner.Update(this.js.line, this.js.line.requestId, this.js.line.languageId, this.js.line.companyId).subscribe(res => {
      this.toastr.success(this.js.line.requestId + " groupId assigned successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      //  this.router.navigate(['/main/controlgroup/transaction/proposed']);
      let paramdata = "";
      paramdata = this.cs.encrypt({ line: this.js.line });
      this.router.navigate(['/main/controlgroup/transaction/proposed/' + paramdata])
      this.spin.hide();
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
    })
  }

  openDialog(data: any = 'new'): void {
    this.showsavebutton = false;
    this.overwriteResult = [];
    this.overwriteGroupResult = [];
    this.showExistingGroup = false;
    const dialogRef = this.dialog.open(ControlgroupNewComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: {
        top: '6.5%'
      },
      data: {
        pageflow: 'New',
        pageflow1: data,
        code: data != 'TransactionCreate' ? this.js.line.groupId : null,
        languageId: data != 'TransactionCreate' ? this.js.line.languageId : null,
        companyId: data != 'TransactionCreate' ? this.js.line.companyId : null,
        groupTypeId: data != 'TransactionCreate' ? this.js.line.groupTypeId : null,
        versionNumber: data != 'TransactionCreate' ? this.js.line.versionNumber : null,
        element: this.js.line
      }
    });
    dialogRef.afterClosed().subscribe(result => {

      this.showsavebutton = true;
      if (result) {
        this.js.line.groupId = result.groupId;
        this.js.line.groupName = result.groupName;


        this.validationService.findGroup({ groupId: result.groupId }).subscribe((res: any) => {
          this.overwriteResult = res.likeMatchGroup;
          this.overwriteGroupResult.push(this.js.line);
          this.groupIdSelected1 = true;
        })

      }

    });
  }

  showExistingGroup = false;
  multiGroupList: any[] = [];
  existingGroup(): void {
    // const dialogRef = this.dialog.open(ExistingGroupComponent, {
    //   disableClose: true,
    //   width: '50%',
    //   maxWidth: '70%',
    //   position: { top: '6.5%' },
    //   data: this.js.line
    // });

    // dialogRef.afterClosed().subscribe(result => {
    //   this.js.line.groupId = result.groupId;
    //   this.js.line.groupName = result.groupIdName;
    // });

    this.showExistingGroup = true;

    let obj: any = {};
    obj.languageId = [this.js.line.languageId];
    obj.companyId = [this.js.line.companyId];
    obj.groupTypeId = [this.js.line.groupTypeId];
    obj.statusId = [0];
    this.existingGroupService.search(obj).subscribe(res => {
      res.forEach((x: { groupId: string; groupName: string; }) => this.multiGroupList.push({ value: x.groupId, label: x.groupId + '- ' + x.groupName, description: x.groupName }))
      this.multiGroupList = this.cas.removeDuplicatesFromArrayNew(this.multiGroupList);
    })
  }
  groupSelected(value) {
    this.overwriteResult = [];
    this.overwriteGroupResult = [];
    const dialogRef = this.dialog.open(ConfirmComponent, {
      disableClose: true,
      width: '40%',
      maxWidth: '60%',
      position: { top: '6.5%' },
      data: { title: "Confirm", message: "Are you sure you want to choose this groupId?" }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.js.line.groupIdName = value.description;
        this.js.line.groupName = value.description;
        this.js.line.groupId = value.value;


        this.validationService.findGroup({ groupId: value.value }).subscribe((res: any) => {
          this.overwriteResult = res.likeMatchGroup;
          this.overwriteGroupResult.push(this.js.line);
          this.groupIdSelected1 = true;
        })


        this.toastr.success(
          this.js.line.groupId + " Group ID selected successfully",
          "Notification", {
          timeOut: 2000,
          progressBar: false,
        }
        );
      }
    })

  }
  independent() {
    this.overwriteResult = [];
    this.overwriteGroupResult = [];
    this.showExistingGroup = false;
    const dialogRef = this.dialog.open(ConfirmComponent, {
      disableClose: true,
      width: '40%',
      maxWidth: '60%',
      position: { top: '6.5%' },
      data: { title: "Confirm", message: "Are you sure you want to choose independent?" }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.spin.show();
        this.service.searchControlGroup({ groupTypeId: ["3"] }).subscribe(res => {
          this.js.line.groupId = res[0].groupId;
          this.js.line.groupName = res[0].groupName;

          this.js.line.groupTypeId =  1003; //res[0].groupTypeId;
          this.js.line.groupTypeName = 'Independent';//res[0].groupTypeName;

          this.js.line.subGroupId = 2041;
          this.js.line.groupName = 'Independent';

          this.validationService.findGroup({ groupId: res[0].groupId }).subscribe((res: any) => {
            this.overwriteResult = res.likeMatchGroup;
            this.overwriteGroupResult.push(this.js.line);
            this.groupIdSelected1 = true;
          })

          this.toastr.success(
            this.js.line.groupId + "Group ID selected successfully",
            "Notification", {
            timeOut: 2000,
            progressBar: false,
          }
          );
          this.spin.hide();
        }, err => {
          this.spin.hide();
          this.cs.commonerror(err);
        })
      }
    });
  }

  groupIdSelectedRadio(value, type) {
    if (type == 'exact') {
      this.result2 = this.result1.filter(x => x.groupId == value);
      this.js.line.groupId = this.result2[0].groupId;
      this.js.line.groupName = this.result2[0].groupName;

      let groupNAme = this.result2[0].stores[0].groupType.split('-');
      let subGroupNAme = this.result2[0].stores[0].subGroupType.split('-');
      
      this.js.line.groupTypeName = groupNAme[1];
      this.js.line.groupTypeId = this.result2[0].stores[0].groupTypeId;
      this.js.line.subGroupId = this.result2[0].stores[0].subGroupTypeId;
      this.js.line.subGroupName = subGroupNAme[1];

      this.groupIdSelected = true;
    }
    if (type == 'like') {
      this.result2 = this.result3.filter(x => x.groupId == value);
      this.js.line.groupId = this.result2[0].groupId;
      this.js.line.groupName = this.result2[0].groupName;

      let groupNAme = this.result2[0].stores[0].groupType.split('-');
      let subGroupNAme = this.result2[0].stores[0].subGroupType.split('-');

      this.js.line.groupTypeName = groupNAme[1];
      this.js.line.groupTypeId = this.result2[0].stores[0].groupTypeId;
      this.js.line.subGroupId = this.result2[0].stores[0].subGroupTypeId;
      this.js.line.subGroupName = subGroupNAme[1];
      this.groupIdSelected = true;
    }
  }

  proposedOverwrite(value) {
    this.result2 = [];
    this.overwriteGroupResult = [];
    this.groupIdSelected = false;
    this.groupIdSelected1 = false;
  }
  backsearch() {
    this.groupIdSelected = false;
    this.groupIdSelected1 = false;
    this.showExistingGroup = false;

  }

  back() {
    this.location.back();
  }



  ApproveAdmin(element, action): void {
    const dialogRef = this.dialog.open(ConfirmComponent, {
      disableClose: true,
      width: '40%',
      maxWidth: '80%',
      position: {
        top: '6.5%'
      },
      data: { title: "Confirm", message: action == 'Approve' ? "Are you sure you want to approve this record?" : "Are you sure you want to reject this record?" }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result && action == "Approve") {
        this.spin.show();
        element.statusId = 5;
        this.storePartnerListring.Create(element).subscribe(res => {
          this.toastr.success(element.requestId + " approved successfully!", "Notification", {
            timeOut: 2000,
            progressBar: false,
          });
          this.storePartner.Update(element, element.requestId, element.languageId, element.companyId).subscribe(res => {

            this.summary.reportPdf().subscribe(summaryRes => {
              this.pdf.generatePdf(summaryRes, element);
              this.spin.hide();
            }, err => {
              this.cs.commonerror(err);
              this.spin.show();
            })

          }, err => {
            this.cs.commonerrorNew(err);
            this.spin.hide();
          })

          this.router.navigate(['/main/controlgroup/transaction/approvalAdmin'])
          this.spin.hide();
        }, err => {
          this.cs.commonerrorNew(err);
          this.spin.hide();
        })

      }
      if (result && action == "Reject") {
        this.spin.show();
        element.statusId = 1;
        element.groupId = null;
        element.groupName = null;
        this.storePartner.Update(element, element.requestId, element.languageId, element.companyId).subscribe(res => {
          this.toastr.success(element.requestId + " rejected successfully!", "Notification", {
            timeOut: 2000,
            progressBar: false,
          });
          //  this.router.navigate(['/main/controlgroup/transaction/approval'])
          let paramdata = '';
          paramdata = this.cs.encrypt({ line: res });
          this.router.navigate(['/main/controlgroup/transaction/ownership/' + paramdata])
          this.spin.hide();
        }, err => {
          this.cs.commonerrorNew(err);
          this.spin.hide();
        })
      }
    });

  }

  subgroupList: any[] = [];
  groupList: any[] = [];
  dropdownSelectgroupTypeID: any[] = [];
  dropdownSelectsubgroupID: any[] = [];

  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.cgsetup.controlgrouptype.url,
      this.cas.dropdownlist.cgsetup.subgrouptype.url,
    ]).subscribe((results) => {
      this.groupList = this.cas.foreachlist(results[0], this.cas.dropdownlist.cgsetup.controlgrouptype.key);
      this.groupList.forEach((x: {
        key: string; value: string;
      }) => this.dropdownSelectgroupTypeID.push({
        value: x.key,
        label: x.value,
        description: x.value,
      }))
      this.subgroupList = this.cas.foreachlist(results[1], this.cas.dropdownlist.cgsetup.subgrouptype.key);
      this.subgroupList.forEach((x: {
        key: string; value: string;
      }) => this.dropdownSelectsubgroupID.push({
        value: x.key,
        label: x.value
      }))
      this.spin.hide();
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }


  onGroupChange(value) {
    this.setupService.searchSubGroupType({
      languageId: [this.form.controls.languageId.value],
      companyId: [this.form.controls.companyId.value],
      groupTypeId: [value.value],
      statusId: [0]
    }).subscribe(res => {
      this.dropdownSelectsubgroupID = [];
      res.forEach(element => {
        this.dropdownSelectsubgroupID.push({
          value: element.subGroupTypeId,
          label: element.subGroupTypeId + '-' + element.subGroupTypeName
        });
      });
    });

  }

}



// findResult(result, clientLength, storeName1) {
//   // let ownerCount  = Object.keys(clientLength).length;
//   let ownerCount = clientLength;
//   let ownerArray: any[] = [];

//   for (let i = 0; i < ownerCount; i++) {
//     var data = result[0]['coOwnerName' + (i + 1)];
//     ownerArray.push({ ownerName: data });
//   }

//   ownerArray.forEach((res, i) => {
//     var j = 1;
//     result.forEach(res1 => {
//       res['coOwnerPercentage' + (j++)] = res1[storeName1 + (i + 1)];
//       res['groupTypeName'] = res1['groupTypeId'] + ' - ' + res1['groupTypeName'];
//     })
//   });
//   console.log(ownerArray)
//   this.TableResult = ownerArray


// }