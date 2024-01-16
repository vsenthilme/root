import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit } from "@angular/core";
import { FormControl, FormBuilder, Validators } from "@angular/forms";
import { MatTableDataSource } from "@angular/material/table";
import { ActivatedRoute, Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { TimekeeperService } from "src/app/main-module/setting/business/timekeeper/timekeeper.service";
import { GeneralMatterService } from "../../case-management/General/general-matter.service";
import { CaseAssignmentService } from "../case-assignment.service";

import { Location } from "@angular/common";


export interface PeriodicElement {
  type: string, typedes: string,
  code: string,
  name: string,
  total: string,
  assign: string[],
  assignL: string,
  amount: string,
}




@Component({
  selector: 'app-resouce-assignment',
  templateUrl: './resouce-assignment.component.html',
  styleUrls: ['./resouce-assignment.component.scss']
})
export class ResouceAssignmentComponent implements OnInit {


  screenid: 1119 | undefined;
  ELEMENT_DATA: PeriodicElement[] = [];

  displayedColumns: string[] = [ 'assign', 'typedes', 'code', 'name', 'amount', 'total',];
  dataSource = new MatTableDataSource<PeriodicElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<PeriodicElement>(true, []);
  public icon = 'expand_more';

  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);
  form = this.fb.group({
    assignedTimeKeeper: [],
    caseCategoryId: [],
    caseInformationNo: [],
    caseOpenedDate: [],
    caseSubCategoryId: [],
    classId: [, [Validators.required]],
    clientId: [, [Validators.required]],
    createdBy: [],
    createdOn: [],
    deletionIndicator: [0],
    languageId: [, [Validators.required]],
    legalAssistant: [],
    matterNumber: [, [Validators.required]],
    originatingTimeKeeper: [],
    partner: [, [Validators.required]],
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
    responsibleTimeKeeper: [],
    statusId: [],
    updatedBy: [],
    updatedOn: [],
    administrativeCost: [], flatFeeAmount: [], billingModeId: [],
  });


  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  getErrorMessage() {
    if (this.email.hasError('required')) {
      return ' Field should not be blank';
    }
    return this.email.hasError('email') ? 'Not a valid email' : '';
  }


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



  isbtntext = true;
  constructor(private fb: FormBuilder,
    private auth: AuthService,
    private service: CaseAssignmentService, private matterservice: GeneralMatterService,
    public toastr: ToastrService, private servicetimekeeper: TimekeeperService,
    private cas: CommonApiService, private location: Location,
    private spin: NgxSpinnerService,
    private route: ActivatedRoute, private router: Router,
    private cs: CommonService,) { }
  title: any;
  ngOnInit(): void {
    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.updatedOn.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.statusId.disable();

    this.form.controls.caseSubCategoryId.disable();
    this.form.controls.caseCategoryId.disable();

    this.form.controls.billingModeId.disable();
    this.form.controls.flatFeeAmount.disable();
    this.form.controls.administrativeCost.disable();
this.form.controls.clientId.disable();


//

    this.auth.isuserdata();
    this.fillmatter();


    this.dropdownlist();

this.form.controls.caseOpenedDate.disable();

    this.form.controls.matterNumber.disable();

  }
  timkeeperlist() {

    this.spin.show();
    this.sub.add(this.service.Getall().subscribe(data => {

      this.sub.add(this.matterservice.Getall().subscribe(datarate => {
        this.cas.getalldropdownlist([
          this.cas.dropdownlist.setup.userTypeId.url,
        ]).subscribe((results) => {
          let userTypeId = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.userTypeId.key);
          this.spin.hide();
          this.spin.show();


          this.sub.add(this.servicetimekeeper.Getall().subscribe(res => {
            console.log(this.form.controls.classId.value)
            console.log(res)
            let result1 = res.filter((x: any) => x.classId == this.form.controls.classId.value);
            console.log(result1)
           let result = result1.filter((y: any) => y.userTypeId == "1" || y.userTypeId == "2" || y.userTypeId == "3" || y.userTypeId == "4"  || y.userTypeId == "5");
            result.forEach((x: any) => {

              let listoftimekeeper = data;
              let filterkeeper = listoftimekeeper.filter(function (l: any) {
                return ((l.assignedTimeKeeper == x.timekeeperCode)

                  || (l.originatingTimeKeeper == x.timekeeperCode)
                  || (l.legalAssistant == x.timekeeperCode)
                  || (l.responsibleTimeKeeper == x.timekeeperCode))
              }
              )
              let materlist: any[] = [];
              filterkeeper.forEach((l: any) => materlist.push(l.matterNumber));
              var restamount = "0";
              if (materlist.length > 0) {
                let dataratef = this.cs.filterArray(datarate, { matterNumber: materlist });

                restamount = dataratef.map(bill => bill.flatFeeAmount + bill.administrativeCost).reduce((acc, bill) => bill + acc);
              }
              datarate.f
              this.ELEMENT_DATA.push({
                type: x.userTypeId,
                typedes: userTypeId.find(y => y.key == x.userTypeId)?.value,
                code: x.timekeeperCode,
                name: x.timekeeperName,
                total: filterkeeper.length,
                assign: [],
                assignL: '',
                amount: restamount,
              });
            })

            this.dataSource = new MatTableDataSource<PeriodicElement>(this.ELEMENT_DATA);
            this.selection = new SelectionModel<PeriodicElement>(true, []);
            this.spin.hide();
            this.referesgrid();
          }, err => {
            this.cs.commonerror(err);
            this.spin.hide();
          }));

        }, (err) => {
          this.toastr.error(err, "");
        });
        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }


  clientIdList: any[] = [];
  caseSubCategoryIdList: any[] = [];
  caseCategoryIdList: any[] = [];
  statusIdList: any[] = [];
  partnerList: any[] = [];
  billingModeIdList: any[] = [];
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.client.clientId.url,
    this.cas.dropdownlist.setup.caseCategoryId.url,
    this.cas.dropdownlist.setup.caseSubcategoryId.url,
    this.cas.dropdownlist.setup.statusId.url,
    this.cas.dropdownlist.setup.timeKeeperCode.url,
    this.cas.dropdownlist.setup.billingModeId.url,
    ]).subscribe((results) => {
      this.clientIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.client.clientId.key);
      this.caseCategoryIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.caseCategoryId.key);
      this.caseSubCategoryIdList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.caseSubcategoryId.key);
      this.statusIdList = this.cas.foreachlist(results[3], this.cas.dropdownlist.setup.statusId.key);
      this.partnerList = this.cas.foreachlist(results[4], this.cas.dropdownlist.setup.timeKeeperCode.key, { userTypeId: 1 });
      this.billingModeIdList = this.cas.foreachlist(results[5], this.cas.dropdownlist.setup.billingModeId.key);
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();
  }
  btntext = "Save";
  matterNumber: any;
  fillmatter() {
    let code = this.route.snapshot.params.code;
    let js = this.cs.decrypt(code);
    this.title = js.pageflow;
    this.matterNumber = js.code;
    this.spin.show();
    this.sub.add(this.matterservice.Get(js.code).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });
      this.form.controls.referenceField1.patchValue(null);
      this.form.controls.referenceField2.patchValue(null);
      this.form.controls.referenceField3.patchValue(null);
      this.form.controls.referenceField4.patchValue(null);
      this.form.controls.referenceField5.patchValue(null);
      this.form.controls.referenceField6.patchValue(null);
      this.form.controls.referenceField7.patchValue(null);
      this.form.controls.referenceField8.patchValue(null);
      this.form.controls.referenceField9.patchValue(null);
      this.form.controls.referenceField10.patchValue(null);
    if(res.billingModeId){
      this.form.controls.billingModeId.patchValue(+res.billingModeId)
    }
      console.log(this.form)
      this.fill(js);
      this.spin.hide();

    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));

  }
  fill(data: any) {

    if (data.pageflow != 'New') {
     // this.form.controls.partner.disable();
      this.title = data.code;
      this.btntext = 'Update';
      if (data.pageflow == 'Display') {
        this.form.disable();
        this.isbtntext = false;
      }

      this.sub.add(this.service.Get(data.code).subscribe(res => {
        this.form.patchValue(res, { emitEvent: false });

        this.spin.hide();
        this.timkeeperlist();
        this.referesgrid();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    } else {
      this.timkeeperlist();
      this.referesgrid();
    }

  }


  submit() {

    this.submitted = true;
    if (this.form.invalid) {
         this.toastr.error(
        "Please fill the required fields to continue",
        "Notification",{
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
    this.form.patchValue({ updatedby: this.auth.username });

    let code = this.route.snapshot.params.code;
    let js = this.cs.decrypt(code);

    if (js.pageflow != 'New') {
      this.sub.add(this.service.Update(this.form.getRawValue(), this.form.controls.matterNumber.value).subscribe(res => {
        this.toastr.success(this.form.controls.matterNumber.value + " updated successfully!","Notification",{
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();
        this.router.navigate(['/main/matters/case-assignment/caselist']);



      }, err => {

        this.cs.commonerror(err);
        this.spin.hide();

      }));
    }
    else {
      this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
        this.toastr.success(res.matterNumber + " saved successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();
        this.router.navigate(['/main/matters/case-assignment/caselist']);

      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();

      }));
    }
  };
  back() {
    this.location.back();
  }
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

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
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }

  assigntimekeeping(event: any, timekeeperCode: any) {
    if (event.source.value.length > 0) {
      if (event.source.value == '4')
        this.form.controls.legalAssistant.patchValue(timekeeperCode);
      else if (event.source.value == '5')
        this.form.controls.referenceField1.patchValue(timekeeperCode);
        else if (event.source.value == '6')
        this.form.controls.referenceField2.patchValue(timekeeperCode);
      else
        event.source.value.forEach((element: any) => {
          if (element == '1')
            this.form.controls.assignedTimeKeeper.patchValue(timekeeperCode);
          if (element == '2')
            this.form.controls.originatingTimeKeeper.patchValue(timekeeperCode);
          if (element == '3')
            this.form.controls.responsibleTimeKeeper.patchValue(timekeeperCode);

        });



      this.referesgrid();
    }
  }
  referesgrid() {

    this.ELEMENT_DATA.forEach(x => {
      x.assign = [];
      x.assignL = '';
      if (this.form.controls.assignedTimeKeeper.value == x.code)
        x.assign.push("1");
      if (this.form.controls.originatingTimeKeeper.value == x.code)
        x.assign.push("2");
      if (this.form.controls.responsibleTimeKeeper.value == x.code)
        x.assign.push("3");
      if (this.form.controls.legalAssistant.value == x.code)
        x.assignL = "4";
      if (this.form.controls.referenceField1.value == x.code)
        x.assignL = "5";
        //mugilan added for paralegal
        if (this.form.controls.referenceField2.value == x.code)
        x.assignL = "6";

    });


    this.dataSource = new MatTableDataSource<PeriodicElement>(this.ELEMENT_DATA);
    this.selection = new SelectionModel<PeriodicElement>(true, []);

  }
}