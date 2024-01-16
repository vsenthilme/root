import { Component, OnInit } from '@angular/core';
import { FormControl, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { LAndECaseService } from '../l-and-ecase.service';
import { Location } from "@angular/common";
import { StatusService } from 'src/app/main-module/setting/configuration/status-id/status.service';

interface SelectItem {
  id: string;
  itemName: string;
}



@Component({
  selector: 'app-land-e-new',
  templateUrl: './land-e-new.component.html',
  styleUrls: ['./land-e-new.component.scss']
})
export class LandENewComponent implements OnInit {
  screenid: 1095 | undefined;
  input: any;
  public icon = 'expand_more';


  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email, Validators.pattern("[a-zA-Z0-9.-_]{1,}@[a-zA-Z.-]{2,}[.]{1}[a-zA-Z]{2,}")]);
  form = this.fb.group({
    adressLine1: [,],
    adressLine2: [,],
    advParty1CellPhone: [, [Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
    advParty1City: [,],
    advParty1CompanyName: [,],
    advParty1DirectTelephone: [, [Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
    advParty1Email: [,Validators.pattern("[a-zA-Z0-9.-_]{1,}@[a-zA-Z.-]{2,}[.]{1}[a-zA-Z]{2,}")],
    advParty1FaxNumber: [,],
    advParty1Name: [,],
    advParty1OfficeTelephone: [, [Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
    advParty1PostalZipCode: [,],
    advParty1State: [,],
    advParty1StreetAddress: [,],
    advParty2CellPhone: [, [Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
    advParty2City: [,],
    advParty2CompanyName: [,],
    advParty2DirectTelephone: [, [Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
    advParty2Email: [,Validators.pattern("[a-zA-Z0-9.-_]{1,}@[a-zA-Z.-]{2,}[.]{1}[a-zA-Z]{2,}")],
    advParty2FaxNumber: [,],
    advParty2Name: [,],
    advParty2OfficeTelephone: [, [Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
    advParty2PostalZipCode: [,],
    advParty2State: [,],
    advParty2StreetAddress: [,],
    agencyName: [,],
    agentEmail: [,],
    agentFaxNumber: [,],
    agentName: [,],
    agentOfficeTelephone: [, [Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
    agentPostalZipCode: [,],
    agentState: [,],
    agrentCity: [,],
    agrentStreetAddress: [,],
    assistantClerkEmail: [,Validators.pattern("[a-zA-Z0-9.-_]{1,}@[a-zA-Z.-]{2,}[.]{1}[a-zA-Z]{2,}")],
    assistantClerkName: [,],
    assistantClerkTelephone: [, [Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
    baliffCourtReporterEmail: [,Validators.pattern("[a-zA-Z0-9.-_]{1,}@[a-zA-Z.-]{2,}[.]{1}[a-zA-Z]{2,}")],
    baliffCourtReporterName: [,],
    baliffCourtReporterTelephone: [, [Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
    caseCategoryId: [, Validators.required],
  // caseCategoryIdFE: [, Validators.required],
    caseSubCategoryId:[, Validators.required],
   //caseSubCategoryIdFE: [, Validators.required],
    causeNo: [,],
    city: [,],
    classId: [1, Validators.required],
    clerkEmail: [,Validators.pattern("[a-zA-Z0-9.-_]{1,}@[a-zA-Z.-]{2,}[.]{1}[a-zA-Z]{2,}")],
    clerkName: [,],
    clerkTelephone: [, [Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
    clientId: [,  Validators.required],
   // clientIdFE: [, Validators.required],
    companyName: [,],
    contactNumber: [,],
    country: [,],
    courtCity: [,],
    courtNo: [,],
    courtState: [,],
    createdBy: [,],
    createdOn: [,],
    defendants: [,],
    deletionIndicator: [,],
    directTelephone: [, [Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
    email: [,Validators.pattern("[a-zA-Z0-9.-_]{1,}@[a-zA-Z.-]{2,}[.]{1}[a-zA-Z]{2,}")],
    emailId: [,Validators.pattern("[a-zA-Z0-9.-_]{1,}@[a-zA-Z.-]{2,}[.]{1}[a-zA-Z]{2,}")],
    fax: [,],
    faxNumber: [,],
    firstNameLastName: [,],
    homeNo: [,],
    id: [,],
    inquiryNumber: [,],
    intakeFormId: [,],
    intakeFormNumber: [,],
    judgeName: [,],
    languageId: [,],
    locationOfFile: [,],
    matterDescription: [, [Validators.required, Validators.pattern('[a-zA-Z0-9 \S]+')]],
    matterNumber: [,],
    notes: [,],
    notesDate: [,],
    officeTelephone: [, [Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
    plaintiffs: [,],
    postalZipCode: [,],
    potentialClientId: [,],
    reference: [,],
    referenceField1: [,],
    referenceField10: [,],
    referenceField2: [,],
    referenceField3: [,],
    referenceField4: [,],
    referenceField5: [,],
    referenceField6: [,],
    referenceField7: [,],
    referenceField8: [,],
    referenceField9: [,],
    schedule: [,],
    scheduleDate: [,],
    sectionCoorinatorEmail: [,Validators.pattern("[a-zA-Z0-9.-_]{1,}@[a-zA-Z.-]{2,}[.]{1}[a-zA-Z]{2,}")],
    sectionCoorinatorName: [,],
    sectionCoorinatorTelephone: [, [Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
    state: [,],
    statusId: [,],
    statusIddes: [,],
    streetAddress: [,],
    taskDate: [,],
    tasksToDo: [,],
    title: [,],
    transactionId: [,],
    typeOfMatter: [,],
    updatedBy: [,],
    updatedOn: [,],
    workNo: [,],
    zipCode: [,],
    pageflow: []

  });


  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  filterclienteList: any;
  filteredclientId: any;
  clientaddress: any;
  clientemail: any;
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
  newPage = true;


  isbtntext = true;
  constructor(private fb: FormBuilder, private serviceStatus: StatusService,
    private auth: AuthService,
    private service: LAndECaseService, private location: Location,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private route: ActivatedRoute, private router: Router,
    private cs: CommonService,) { }
  code: any;
  ngOnInit(): void {
    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.updatedOn.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.statusId.disable();
    this.form.controls.inquiryNumber.disable();
    this.form.controls.classId.disable();
    this.form.controls.statusIddes.disable();
    // this.form.controls.clientId.valueChanges.subscribe(x => {
    //   if (x)
    //     this.getclientinfo();
    // });
    // this.form.controls.classId.valueChanges.subscribe(x => {
    //   if (x)
    //     this.getclient_class(x);
    // });
    // this.form.controls.caseCategoryId.valueChanges.subscribe(x => {

    //   if (x)
    //     this.getsubcaseCategoryId(x);
    // });
    this.auth.isuserdata();
    this.dropdownlist();
    let code = this.route.snapshot.params.code;
    let js: any = {}
    js = this.cs.decrypt(code);
    console.log(js)
    if (js.pageflow != 'New') {
      this.newPage = false;
    //  js = this.cs.decrypt(code);
      this.filteredclientId = js.clientId
      this.code = js.code;
    }
    this.dropdownlist(js);
    this.form.controls.intakeFormNumber.disable();
    this.form.controls.potentialClientId.disable();
    this.form.controls.firstNameLastName.disable();
    // this.form.controls.title.disable();
  //  this.form.controls.email.disable();
    // this.form.controls.companyName.disable();
    this.form.controls.adressLine1.disable();
    this.form.controls.city.disable();
    this.form.controls.state.disable();
    this.form.controls.country.disable();
    this.form.controls.zipCode.disable();
    this.form.controls.homeNo.disable();
    this.form.controls.workNo.disable();
    this.form.controls.emailId.disable()
    this.form.controls.contactNumber.disable();
    this.form.controls.faxNumber.disable();
    if(this.filteredclientId){
      this.getclientinfo()
      }
  }
  apiClientfileter: any = { classId: 1 };
  // getclient_class(code: string) {
  //   this.spin.show();
  //   if (code == '3')
  //     this.apiClientfileter = {};
  //   else
  //     this.apiClientfileter = { classId: this.form.controls.classId.value };
  //   // this.form.controls.clientId.patchValue('');
  //   this.cas.getalldropdownlist([
  //     this.cas.dropdownlist.client.clientId.url,
  //   ]).subscribe((results) => {
  //     if (code == '3')
  //       this.clientIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.client.clientId.key);
  //     else
  //       this.clientIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.client.clientId.key, { classId: code });
  //   }, (err) => {
  //     this.toastr.error(err, "");
  //   });
  //   this.spin.hide();
  // }
  getsubcaseCategoryId(code: string) {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.caseSubcategoryId.url,
    ]).subscribe((results:any) => {



      this.caseSubCategoryIdList = results[0];
      this.multicasesubList= [];
          this.caseSubCategoryIdList.forEach((element:any) => {
            if(element.caseCategoryId == +code ){
              console.log(true)
             this.multicasesubList.push({value: element.caseSubcategoryId, label:  element.subCategory})
        
            }
          })
           console.log(this.multicasesubList)
       this.multiselectcasesubList = this.multicasesubList;
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();

  }
  getclientinfo() {
    if(this.filteredclientId){
    this.spin.show();
    this.sub.add(this.service.GetClient(this.filteredclientId).subscribe(res => {
      if (this.form.controls.firstNameLastName.value != res.firstNameLastName) {
        this.form.controls.firstNameLastName.patchValue(res.firstNameLastName);
        // this.form.controls.title.patchValue(res.firstNameLastName));
        this.form.controls.emailId.patchValue(res.emailId);
        this.clientaddress = res.addressLine1
        this.clientemail = res.emailId
        // this.form.controls.companyName.patchValue(res.firstNameLastName));
        this.form.controls.adressLine1.patchValue(res.addressLine1);
        this.clientaddress = res.addressLine1
        this.form.controls.city.patchValue(res.city);
        this.form.controls.state.patchValue(res.state);
        this.form.controls.country.patchValue(res.country);
        this.form.controls.zipCode.patchValue(res.zipCode);
        this.form.controls.homeNo.patchValue(res.homeNo);
        this.form.controls.workNo.patchValue(res.workNo);
        this.form.controls.contactNumber.patchValue(res.contactNumber);
        this.form.controls.faxNumber.patchValue(res.fax);
      }
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  else{
    this.spin.show();
    this.sub.add(this.service.GetClient(this.form.controls.clientId.value).subscribe(res => {
      if (this.form.controls.firstNameLastName.value != res.firstNameLastName) {
        this.form.patchValue(res, { emitEvent: false });
      }else{
        this.form.controls.adressLine1.patchValue(res.addressLine1);
        this.clientaddress = res.addressLine1
        this.clientemail = res.emailId
        this.form.controls.city.patchValue(res.city);
        this.form.controls.state.patchValue(res.state);
        this.form.controls.country.patchValue(res.country);
        this.form.controls.emailId.patchValue(res.emailId);
        this.form.controls.zipCode.patchValue(res.zipCode);
        this.form.controls.homeNo.patchValue(res.homeNo);
        this.form.controls.workNo.patchValue(res.workNo);
        this.form.controls.contactNumber.patchValue(res.contactNumber);
        this.form.controls.faxNumber.patchValue(res.fax);
      }
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  }

  clientIdList: any[] = [];
  caseSubCategoryIdList: any[] = [];
  caseCategoryIdList: any[] = [];
  classIdList: any[] = [];

  selectedItems3: SelectItem[] = [];
  multiselectcasesubList: any[] = [];
  multicasesubList: any[] = [];

  selectedItems2: SelectItem[] = [];
  multiselectcaseList: any[] = [];
  multicaseList: any[] = [];


  
  selectedItems: SelectItem[] = [];
  multiselectclientList: any[] = [];
  multiclientList: any[] = [];

  dropdownSettings = {
    singleSelection: true, 
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };
  referralList: any = [];



  dropdownlist(js?:any) {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.client.clientId.url,
      this.cas.dropdownlist.setup.caseCategoryId.url,
      this.cas.dropdownlist.setup.caseSubcategoryId.url,
      this.cas.dropdownlist.setup.classId.url,
      
    ]).subscribe((results) => {
      this.clientIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.client.clientId.key, { classId: 1 , statusId: 18});

      this.caseCategoryIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.caseCategoryId.key, { classId: 1 });
      this.caseCategoryIdList.forEach((x: { key: string; value: string; }) => this.multicaseList.push({value: x.key, label:  x.value}))
      this.multiselectcaseList = this.multicaseList;

      this.caseSubCategoryIdList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.caseSubcategoryId.key);
      this.caseSubCategoryIdList.forEach((x: { key: string; value: string; }) => this.multicasesubList.push({value: x.key, label:  x.value}))
   this.multiselectcasesubList = this.multicasesubList;

      this.classIdList = this.cas.foreachlist(results[3], this.cas.dropdownlist.setup.classId.key, { classId: [1, 2] });
      if(!this.newPage){
        this.fill(js);
      }
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.sub.add(this.service.GetClientdetails().subscribe(res => {
      this.referralList = res;
      this.filterclienteList = this.referralList.filter((element: { classId: any; }) => {
        return element.classId === 1;
      });
      this.filterclienteList.forEach((x: { clientId: string; firstNameLastName: string; }) => this.multiclientList.push({value: x.clientId, label: x.clientId + '-' + x.firstNameLastName}))
      this.multiselectclientList = this.multiclientList;
      
      this.spin.hide();
      
    })); 
    this.spin.hide();
  }
  btntext = "Save";
  pageflow = "New";
  isdisabled = this.form.controls.intakeFormNumber.value ? false : true;
  fill(data: any) {
    if (data.pageflow == 'New') {
      this.form.controls.clientId.patchValue(data.clientId);
      // this.multiclientList.forEach(element => {
      //   if(element.id == this.filteredclientId){
      //     this.form.controls.clientIdFE.patchValue([element]);
      //   }
      // });
    }
    if (data.pageflow != 'New') {
      this.pageflow = "Edit";
      this.btntext = 'Update';
      if (data.pageflow == 'Display') {
        this.form.disable();
        this.dropdownSettings.disabled=true;
        this.isbtntext = false;
      }
      this.form.controls.id.disable();

      this.spin.show();
      this.sub.add(this.service.Get(data.code).subscribe(res => {
        this.form.patchValue(res, { emitEvent: false });
        this.getclientinfo()
        this.form.controls.adressLine1.patchValue(this.clientaddress)
        this.form.controls.emailId.patchValue(this.clientemail)

        // this.multicaseList.forEach(element => {
        //   if(element.value == res.caseCategoryId){
        //     this.form.controls.caseCategoryIdFE.patchValue([element.value]);
        //   }
        // });

        // let casecatlist: any[]=[]
        // casecatlist.push(res.caseCategoryId);
        // console.log(casecatlist)
        // this.form.patchValue({caseCategoryId: casecatlist });

        // this.multicasesubList.forEach(element => {
        //   if(element.value == res.caseSubCategoryId){
        //     this.form.controls.caseSubCategoryId.patchValue(element.value);
        //   }
        // });
        // let casesublist: any[]=[]
        // casesublist.push(res.caseSubCategoryId);
        // console.log(casesublist)
        // this.form.patchValue({caseSubCategoryId: casesublist });

        // this.multiclientList.forEach(element => {
        //   if(element.id == res.clientId){
        //     this.form.controls.clientIdFE.patchValue([element]);
        //   }
        // });
        this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
        this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
        this.spin.hide();
        // this.getclient_class(this.form.controls.classId.value);
    //    this.getsubcaseCategoryId(res.caseCategoryId);
        this.getstatusdesId(this.form.controls.statusId.value);
        this.isdisabled = this.form.controls.intakeFormNumber.value ? false : true;
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }
  }
  getstatusdesId(code: string) {
    this.spin.show();
    this.sub.add(this.serviceStatus.Get(code).subscribe(res => {
      this.form.controls.statusIddes.patchValue(res.status);
      this.spin.hide();

    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));

  }
  submit() {
    console.log(this.form)
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

  

    if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
      this.form.patchValue({clientId: this.selectedItems[0].id });
    }

    this.form.removeControl('updatedOn');
    this.form.removeControl('createdOn');
    this.form.patchValue({ updatedby: this.auth.username });
    if (this.form.controls.id.value) {
      this.sub.add(this.service.Update(this.form.getRawValue(), this.form.controls.id.value).subscribe(res => {
        this.toastr.success(this.form.controls.id.value + " updated successfully!","Notification",{
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();
        this.location.back();



      }, err => {

        this.cs.commonerror(err);
        this.spin.hide();

      }));
    }
    else {
      this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
        this.toastr.success(res.id + " saved successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();
        this.location.back();

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
  open_Intake(type = 'Display') {

    let data: any = this.form.controls;
    let formname = this.cs.customerformname(data.intakeFormId.value);

    if (formname == '') {
      this.toastr.error(
        "Select from is invalid.",
        ""
      );

      this.cs.notifyOther(true);
      return;
    }
    this.cs.notifyOther(false);

    data.pageflow.patchValue(type);
    this.router.navigate(['/main/crm/' + formname + '/' + this.cs.encrypt(this.form.getRawValue())]);

  }

  onItemSelect(item:any){
    console.log(this.form.controls.caseCategoryId.value);
    console.log(this.selectedItems);
    this.getsubcaseCategoryId(this.form.controls.caseCategoryId.value);
}
onItemSelect1(item:any){
  if (item.id)
    this.getclientinfo();
    if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
      this.form.patchValue({clientId: this.selectedItems[0].id });
    }
}
}