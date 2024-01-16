import { Component, OnInit } from '@angular/core';
import { FormControl, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { ImmigirationService } from '../immigiration.service';
import { Location } from "@angular/common";
import { StatusService } from 'src/app/main-module/setting/configuration/status-id/status.service';
import { disable } from '@rxweb/reactive-form-validators';


interface SelectItem {
  id: string;
  itemName: string;
}

@Component({
  selector: 'app-immigration-new',
  templateUrl: './immigration-new.component.html',
  styleUrls: ['./immigration-new.component.scss']
})
export class ImmigrationNewComponent implements OnInit {
  screenid: 1097 | undefined;
  input: any;
  public icon = 'expand_more';


  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);
  form = this.fb.group({

    adressLine1: [,],
    adressLine2: [,],
    alienNumber: [,],
    alternateEmailId: [, [Validators.email]],
    alternateTelephone1: [,[Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
    caseCategoryId: [, Validators.required ],
  // caseCategoryIdFE: [, Validators.required ],
    caseSubCategoryId:  [, Validators.required],
    //caseSubCategoryIdFE: [, Validators.required],
    city: [,],
    classId: [2, Validators.required],
    clientId: [, Validators.required],
    //clientIdFE: [, Validators.required],
    companyName: [,],
    contactNumber: [,],
    country: [,],
    countryOfBirth: [,],
    createdBy: [,],
    createdOn: [,],
    dateOfBirth: [,],
    deletionIndicator: [,],
    emailId: [, [Validators.email ]],
    fax: [,],
    homeNo: [,],
    id: [,],
    inquiryNumber: [,],
    intakeFormId: [,],
    intakeFormNumber: [,],
    languageId: [,],
    locationOfFile: [,],
    matterDescription: [, Validators.required],
    matterNumber: [,],
    nameOfEr: [,],
    potentialClientId: [,],
    referenceField1: [,],
    referenceField10: [,],
    referenceField11: [,],
    referenceField12: [,],
    referenceField13: [,],
    referenceField14: [,],
    referenceField15: [,],
    referenceField16: [,],
    referenceField17: [,],
    referenceField18: [,],
    referenceField19: [,],
    referenceField2: [,],
    referenceField20: [,],
    referenceField3: [,],
    referenceField4: [,],
    referenceField5: [,],
    referenceField6: [,],
    referenceField7: [,],
    referenceField8: [,],
    referenceField9: [,],
    state: [,],
    statusId: [,],
    statusIddes: [,],
    title: [,],
    transactionId: [,],
    typeOfMatter: [,],
    updatedBy: [,],
    updatedOn: [,],
    workNo: [,],
    zipCode: [,],

    firstNameLastName: [],
    pageflow: []
  });




  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
   // return this.form.controls[control].hasError(error) && this.submitted;
  }
  filteredclientId: any;
  clientaddress: any;
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
  newPage = true;
  panelOpenState = false;
  isdisabled = this.form.controls.intakeFormNumber.value ? false : true;

  isbtntext = true;
  constructor(private fb: FormBuilder, private serviceStatus: StatusService,
    private auth: AuthService,
    private service: ImmigirationService,
    public toastr: ToastrService, private location: Location,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private route: ActivatedRoute, private router: Router,
    private cs: CommonService,) { }
  code: any;
  ngOnInit(): void {
    this.form.controls.updatedOn.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.statusId.disable();
    this.form.controls.statusIddes.disable();
    this.form.controls.inquiryNumber.disable();
    this.form.controls.classId.disable();

    // this.form.controls.clientId.valueChanges.subscribe(x => {

    //   if (x)
    //     this.getclientinfo();
    // });

    // this.form.controls.caseCategoryId.valueChanges.subscribe(x => {

    //   if (x)
    //     this.getsubcaseCategoryId(x);
    // });
    // this.form.controls.classId.valueChanges.subscribe(x => {
    //   if (x)
    //     this.getclient_class(x);
    // });


    this.auth.isuserdata();
this.dropdownlist();
    let code = this.route.snapshot.params.code;
    console.log(code)
    let js: any = {}
    if (code != 'new') {
      this.newPage = false;
      js = this.cs.decrypt(code);
      console.log(js)
      this.filteredclientId = js.clientId
      console.log(this.filteredclientId)
      this.code = js.code;
    }
    this.dropdownlist(js);
    this.form.controls.intakeFormNumber.disable();
    this.form.controls.potentialClientId.disable();
    this.form.controls.firstNameLastName.disable();
    // this.form.controls.title.disable();
    this.form.controls.emailId.disable();
    // this.form.controls.companyName.disable();
    this.form.controls.adressLine1.disable();
    this.form.controls.city.disable();
    this.form.controls.state.disable();
    this.form.controls.country.disable();
    this.form.controls.zipCode.disable();
    this.form.controls.homeNo.disable();
    this.form.controls.workNo.disable();
    this.form.controls.contactNumber.disable();
    this.form.controls.fax.disable();
    if(this.filteredclientId){
    this.getclientinfo()
    }
  }


  getclientinfo() {

 
 if(this.filteredclientId){

  this.spin.show();
  this.sub.add(this.service.GetClient(this.filteredclientId).subscribe(res => {
    if (this.form.controls.firstNameLastName.value != res.firstNameLastName) {
      console.log(res)
      this.form.controls.firstNameLastName.patchValue(res.firstNameLastName);
      // this.form.controls.title.patchValue(res.firstNameLastName));
      this.form.controls.emailId.patchValue(res.emailId);
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
      this.form.controls.fax.patchValue(res.fax);

    }
    this.spin.hide();
  }, err => {
    this.cs.commonerror(err);
    this.spin.hide();
  }));
 }else{
  this.spin.show();
  this.sub.add(this.service.GetClient(this.form.controls.clientId.value).subscribe(res => {
    if (this.form.controls.firstNameLastName.value != res.firstNameLastName) {
      this.form.controls.firstNameLastName.patchValue(res.firstNameLastName);
      // this.form.controls.title.patchValue(res.firstNameLastName));
      this.form.controls.emailId.patchValue(res.emailId);
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
      this.form.controls.fax.patchValue(res.fax);

    }
    this.spin.hide();
  }, err => {
    this.cs.commonerror(err);
    this.spin.hide();
  }));
 }
  }

  getsubcaseCategoryId(code: string) {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.caseSubcategoryId.url,
    ]).subscribe((results:any) => {
      this.caseSubCategoryIdList = results[0];
     this.multicasesubList= [];
         this.caseSubCategoryIdList.forEach((element:any) => {
           if(element.caseCategoryId == +code ){
            this.multicasesubList.push({value: element.caseSubcategoryId, label:  element.subCategory})
       
           }
         })
          
      this.multiselectcasesubList = this.multicasesubList;
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();

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
  // getclient_class(code: string) {
  //   this.spin.show();
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
  clientIdList: any[] = [];
  caseSubCategoryIdList: any[] = [];
  caseCategoryIdList: any[] = [];
  classIdList: any[] = [];

  selectedItems: SelectItem[] = [];
  multiselectcasesubList: any[] = [];
  multicasesubList: any[] = [];

  selectedItems2: SelectItem[] = [];
  multiselectcaseList: any[] = [];
  multicaseList: any[] = [];


  selectedItems3: SelectItem[] = [];
  multiselectclientList: any[] = [];
  multiclientList: any[] = [];


  dropdownSettings = {
    singleSelection: true,
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled : false
  };



  dropdownlist(js?:any) {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.client.clientId.url,
      this.cas.dropdownlist.setup.caseCategoryId.url,
  
      this.cas.dropdownlist.setup.classId.url,
      this.cas.dropdownlist.setup.caseSubcategoryId.url,
    ]).subscribe((results) => {
      this.clientIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.client.clientId.key, { classId: 2 , statusId: 18});
     
      this.clientIdList.forEach((x: { key: string; value: string; }) => this.multiclientList.push({value: x.key, label: x.value}))
      this.multiselectclientList = this.multiclientList;
      this.caseCategoryIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.caseCategoryId.key, { classId: 2 });
      this.caseCategoryIdList.forEach((x: { key: string; value: string; }) => this.multicaseList.push({value: x.key, label:  x.value}))
      this.multiselectcaseList = this.multicaseList;




      this.caseSubCategoryIdList = this.cas.foreachlist(results[3], this.cas.dropdownlist.setup.caseSubcategoryId.key);
      this.caseSubCategoryIdList.forEach((x: { key: string; value: string; }) => this.multicasesubList.push({value: x.key, label:  x.value}))
   this.multiselectcasesubList = this.multicasesubList;

      this.classIdList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.classId.key, { classId: [1, 2] });

      if(!this.newPage){
        this.fill(js);
      }

    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();
  }
  btntext = "Save";
  pageflow = "New";
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
      this.btntext = 'Update';
      this.pageflow = "Edit";
      if (data.pageflow == 'Display') {
        this.form.disable();
        this.isbtntext = false;
      }
      this.form.controls.id.disable();

      this.spin.show();
      this.sub.add(this.service.Get(data.code).subscribe(res => {
    
      //  this.getsubcaseCategoryId(this.form.controls.caseCategoryId.value);
        this.form.patchValue(res, { emitEvent: false });
        this.getclientinfo()
    this.form.controls.adressLine1.patchValue(this.clientaddress)
        // this.multicaseList.forEach(element => {
        //   if(element.id == res.caseCategoryId){
        //     this.form.controls.caseCategoryIdFE.patchValue([element]);
        //   }
        // });


        // if (this.selectedItems2 != null && this.selectedItems2 != undefined && this.selectedItems2.length > 0) {
        //   this.form.controls.caseCategoryIdFE.patchValue([{id: res.caseCategoryId,itemName: res.caseCategoryId}]);
        // }

        // this.multicasesubList.forEach(element => {
        //   if(element.id == res.caseSubCategoryId){
        //     this.form.controls.caseSubCategoryIdFE.patchValue([element]);
        //   }
        // });




        // if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
        //   this.form.controls.caseSubCategoryIdFE.patchValue([{id: res.caseSubCategoryId,itemName: res.caseSubCategoryId}]);
        // }

        // this.multiclientList.forEach(element => {
        //   if(element.id == res.clientId){
        //     this.form.controls.clientIdFE.patchValue([element]);
        //   }
        // });
        // if (this.selectedItems3 != null && this.selectedItems3 != undefined && this.selectedItems3.length > 0) {
        //   this.form.controls.clientIdFE.patchValue([{id: res.clientId,itemName: res.clientId}]);
        // }

     //   this.caseSubCategoryIdList.forEach(x => this.multicasesubList.push({id: x.caseSubCategoryId, itemName: x.caseSubCategoryId}))
        this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
        this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
        this.spin.hide();
        // this.getclient_class(this.form.controls.classId.value);
 
       
       // this.getsubcaseCategoryId(res.caseCategoryId);
        this.getstatusdesId(this.form.controls.statusId.value);
        this.isdisabled = this.form.controls.intakeFormNumber.value ? false : true;
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
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

    // if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
    //   this.form.patchValue({caseSubCategoryId: this.selectedItems[0] });
    // }
    // if (this.selectedItems2 != null && this.selectedItems2 != undefined && this.selectedItems2.length > 0) {
    //   this.form.patchValue({caseCategoryId: this.selectedItems2[0]});
    // }
    if (this.selectedItems3 != null && this.selectedItems3 != undefined && this.selectedItems3.length > 0) {
      this.form.patchValue({clientId: this.selectedItems3[0].id });
    }
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
     if(this.form.controls.caseCategoryId.value)
     this.getsubcaseCategoryId(this.form.controls.caseCategoryId.value);
}
onItemSelect1(item:any){
  if (item.id)
  this.getclientinfo();
}
}