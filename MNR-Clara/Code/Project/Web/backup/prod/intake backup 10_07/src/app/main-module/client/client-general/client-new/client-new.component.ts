import {
  Component,
  OnInit
} from '@angular/core';
import {
  Location
} from '@angular/common';
import {
  FormControl,
  FormBuilder,
  Validators
} from '@angular/forms';
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
  Subscription
} from 'rxjs';
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
  ClientGeneralService
} from '../client-general.service';
import {
  DacaFormService
} from 'src/app/customerform/daca-form/daca-form.service';
import {
  EnglishN400Service
} from 'src/app/customerform/english-n400/english-n400.service';
import {
  EnglishService
} from 'src/app/customerform/english/english.service';
import {
  ImmigrationService
} from 'src/app/customerform/immigration-form/immigration.service';
import {
  LAndEService
} from 'src/app/customerform/l-and-e/l-and-e.service';
import {
  SpanishService
} from 'src/app/customerform/spanish/spanish.service';
import { ClientDocumentService } from '../../client-document/client-document.service';

interface SelectItem {
  id: string;
  itemName: string;
}


@Component({
  selector: 'app-client-new',
  templateUrl: './client-new.component.html',
  styleUrls: ['./client-new.component.scss']
})
export class ClientNewComponent implements OnInit {
  public mask = [/\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/]
  
  screenid = 1085;
  input: any;
  isbtntext = true;
  public icon = 'expand_more';

  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);
  form = this.fb.group({
    addressLine1: [, [Validators.required]],
    addressLine2: [, ],
    addressLine3: [, ],
    alternateEmailId: [, [Validators.email]],
    city: [, ],
    classId: [, [Validators.required]],
    clientCategoryId: [, [Validators.required]],
    //  clientCategoryIdFE: [, [Validators.required]],
    clientId: [],
    consultationDate: [, ],
    contactNumber: [, [Validators.pattern('[0-9 -]+$'), Validators.minLength(12), Validators.maxLength(12), ]],
    corporationClientId: [, ],
    corporationClientIdFE: [
      [],
    ],
    country: [, ],
    createdBy: [, ],
    createdOn: [, ],
    deletionIndicator: [, ],
    emailId: [, [Validators.email]],
    fax: [, ],
    firstName: [, ],
    firstNameLastName: [, Validators.required],
    homeNo: [, [Validators.pattern('[0-9 -]+$'), Validators.minLength(12), Validators.maxLength(12), ]],
    inquiryNumber: [, ],
    intakeFormId: [, ],
    intakeFormNumber: [, ],
    isMailingAddressSame: [false, ],
    languageId: [this.auth.languageId, ],
    lastName: [, ],
    lastNameFirstName: [, ],
    mailingAddress: [, ],
    occupation: [, ],
    potentialClientId: ['999999', ],
    referenceField1: [, ],
    referenceField10: [, ],
    referenceField11: [, ],
    referenceField12: [, ],
    referenceField13: [, ],
    referenceField14: [, ],
    referenceField15: [false, ],
    referenceField16: [, ],
    referenceField17: [, ],
    referenceField18: [, ],
    referenceField19: [, ],
    referenceField20: [, ],
    referenceField2: [, ],
    referenceField3: [, [Validators.pattern('[0-9 -]+$'), Validators.minLength(12), Validators.maxLength(12), ]],
    referenceField4: [],
    referenceField5: [, ],
    referenceField6: [, ],
    referenceField7: [, ],
    referenceField8: [, ],
    referenceField9: [, ],
    referralId: [, ],
    //referralIdFE: [,],
    socialSecurityNo: [, ],
    state: [, ],
    statusId: [18, ],
    suiteDoorNo: [, ],
    transactionId: [, ],
    updatedBy: [, ],
    updatedOn: [, ],
    workNo: [, [Validators.pattern('[0-9 -]+$'), Validators.minLength(12), Validators.maxLength(12), ]],
    zipCode: [, ],


  });

  todaydate = new Date();
  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  clientList: any;
  petitionlist: any;
  filtersclient: any;
  filterspetition: any;
  referenceList: any;

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


  constructor(private fb: FormBuilder,
    private location: Location,
    private auth: AuthService,

    private service: ClientGeneralService,
    private clientUserService: ClientDocumentService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private route: ActivatedRoute, private router: Router,
    private cs: CommonService,
    private englishService: EnglishService,
    private dacaService: DacaFormService,
    private english400Service: EnglishN400Service,
    private immigrationService: ImmigrationService,
    private landeService: LAndEService,
    private spnishService: SpanishService,

  ) {}

  RA: any = {};
  addresscombined: string;

  ngOnInit(): void {



    this.RA = this.auth.getRoleAccess(this.screenid);
    this.ngdropdown();
    console.log(this.form)
    this.form.controls.isMailingAddressSame.valueChanges.subscribe(val => {
      if (val == true) {
        this.form.controls.referenceField5.patchValue(this.form.controls.suiteDoorNo.value);
        this.form.controls.mailingAddress.patchValue(this.form.controls.addressLine1.value);
        this.form.controls.referenceField6.patchValue(this.form.controls.city.value);
        this.form.controls.referenceField7.patchValue(this.form.controls.state.value);
        this.form.controls.referenceField8.patchValue(this.form.controls.zipCode.value);
        this.form.controls.referenceField10.patchValue(this.form.controls.country.value);
      } else {
        this.form.controls.mailingAddress.patchValue('');
        this.form.controls.referenceField5.patchValue('');
        this.form.controls.referenceField6.patchValue('');
        this.form.controls.referenceField7.patchValue('');
        this.form.controls.referenceField8.patchValue('');
        this.form.controls.referenceField10.patchValue('');
      }


    });

    
    this.form.controls.referenceField15.valueChanges.subscribe(val => {
      if (val == true) {
        this.form.controls.referenceField16.patchValue(this.form.controls.suiteDoorNo.value);
        this.form.controls.addressLine3.patchValue(this.form.controls.addressLine1.value);
        this.form.controls.referenceField17.patchValue(this.form.controls.city.value);
        this.form.controls.referenceField18.patchValue(this.form.controls.state.value);
        this.form.controls.referenceField19.patchValue(this.form.controls.zipCode.value);
        this.form.controls.referenceField20.patchValue(this.form.controls.country.value);
      } else {
        this.form.controls.referenceField16.patchValue('');
        this.form.controls.addressLine3.patchValue('');
        this.form.controls.referenceField17.patchValue('');
        this.form.controls.referenceField18.patchValue('');
        this.form.controls.referenceField19.patchValue('');
        this.form.controls.referenceField20.patchValue('');
      }


    });


    // this.form.controls.addressLine1.valueChanges.subscribe(x => {
    //   if (this.form.controls.isMailingAddressSame.value)
    //     this.form.controls.mailingAddress.patchValue(this.form.controls.addressLine1.value);


    // });

    sessionStorage.setItem('client', this.route.snapshot.params.code);
   // this.form.controls.referenceField4.patchValue(this.cs.todayCallApi());
    // this.form.controls.consultationDate.disable();
    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOn.disable();
    //  this.form.controls.firstNameLastName.disable();

    this.form.controls.potentialClientId.disable();
    this.form.controls.inquiryNumber.disable();
    /// this.form.controls.consultationDate.disable();
    this.form.controls.clientId.disable();
    this.form.controls.referenceField9.disable();
    this.auth.isuserdata();
    // this.dropdownlist();

    let code = this.route.snapshot.params.code;

    let js: any = {}
    js = this.cs.decrypt(code);
    console.log(js)
    if (js.pageflow == 'New') {
      // let js = this.cs.decrypt(code);
      // this.fill(js);
      this.newPage = true;
    } else {
      this.newPage = false;
    }
    this.dropdownlist(js);




  }
  addressPatch() {
    // alert(2)
  }

  Fullname() {
    this.form.patchValue({
      firstName: this.form.value.firstName,
      referenceField1: this.form.value.referenceField1,
      lastName: this.form.value.lastName,

      firstNameLastName: this.form.value.firstName + ' ' + this.form.value.referenceField1 + ' ' + this.form.value.lastName
    });


    this.form.controls.classId.valueChanges.subscribe(x => {
      console.log(x)
      if (this.form.controls.classId.value == 1) {
        this.form.controls.corporationClientId.disable();
      }
    });





  }
  classChange() {
    console.log(this.form.controls.classId.value)
    if (this.form.controls.classId.value == 1) {
      this.form.controls.corporationClientId.disable();
      this.form.controls.referenceField2.disable();
    } else {
      this.form.controls.corporationClientId.enable();
      this.form.controls.referenceField2.enable();
    }
  }

  languageIdList: any[] = [];
  classIdList: any[] = [];
  clientCategoryIdList: any[] = [];
  statusIdList: any[] = [];
  //referenceList: any[] = [];
  clientIdList: any[] = [];
  clientIdListPetitioner: any[] = [];


  selectedItems3: SelectItem[] = [];
  multiselectclientList: any[] = [];
  multiclientList: any[] = [];

  selectedItems2: SelectItem[] = [];
  multiselectmatterList: any[] = [];
  multimatterList: any[] = [];


  selectedItems5: SelectItem[] = [];
  multiselectreferralList: any[] = [];
  multireferralist: any[] = [];


  selectedItems6: SelectItem[] = [];
  multiselectclientcatList: any[] = [];
  multiclientcatist: any[] = [];


  dropdownSettings = {
    singleSelection: true,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };




  ngdropdown() {
    this.spin.show();
    this.sub.add(this.service.GetClientdetails().subscribe(res => {
        this.clientList = res;
        this.filtersclient = this.clientList.filter((element: {
          clientCategoryId: string;
        }) => {
          return element.clientCategoryId == '1';
        });
        this.filtersclient.forEach((x: {
          clientId: string;firstNameLastName: string;
        }) => this.multiclientList.push({
          value: x.clientId,
          label: x.clientId + '-' + x.firstNameLastName
        }))
        this.multiselectclientList = this.multiclientList;

      },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    this.sub.add(this.service.GetClientdetails().subscribe(res => {
        this.petitionlist = res;
        this.filterspetition = this.petitionlist.filter((element: {
          clientCategoryId: string;
        }) => {
          return element.clientCategoryId == '1' && '2' && '3';
        });
        this.filterspetition.forEach((x: {
          clientId: string;firstNameLastName: string;
        }) => this.multimatterList.push({
          value: x.clientId,
          label: x.clientId + '-' + x.firstNameLastName
        }))
        this.multiselectmatterList = this.multimatterList;

   

      },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
      this.spin.hide();
    // this.cas.getalldropdownlist([
    //   this.cas.dropdownlist.setup.referralId.url,
    // ]).subscribe((results) => {
    //   this.referenceList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.referralId.key, { languageId: 'EN' });
    //   this.referenceList.forEach((x: { key: string; value: string; }) => this.multireferralist.push({ value: x.key, label: x.value }))
    //   this.multiselectreferralList = this.multireferralist;

    // }, (err) => {
    //   this.toastr.error(err, "");
    // });
  }


  dropdownlist(js ? : any) {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.languageId.url,
      this.cas.dropdownlist.setup.classId.url,

      this.cas.dropdownlist.setup.clientCategoryId.url,
      this.cas.dropdownlist.setup.statusId.url,
      this.cas.dropdownlist.setup.referralId.url,
      this.cas.dropdownlist.client.clientId.url,
    ]).subscribe((results) => {
      this.languageIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.languageId.key);
      this.classIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.classId.key).filter(x => x.key != 3);

      this.clientCategoryIdList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.clientCategoryId.key);
      this.clientCategoryIdList.forEach((x: {
        key: string;value: string;
      }) => this.multiclientcatist.push({
        value: x.key,
        label: x.value
      }))
      this.multiselectclientcatList = this.multiclientcatist;

      this.statusIdList = this.cas.foreachlist(results[3], this.cas.dropdownlist.setup.statusId.key).filter(s => [18, 21, this.form.controls.statusId.value].includes(s.key));


      this.referenceList = results[4];
      this.referenceList.forEach((x: {
        referralId: string;referralDescription: string;classId: any
      }) => this.multireferralist.push({
        value: x.referralId,
        label: x.referralId + ' - ' + x.referralDescription,
        classId: x.classId
      }))
      this.multiselectreferralList = this.multireferralist;


      this.clientIdList = this.cas.foreachlist(results[5], this.cas.dropdownlist.client.clientId.key, {
        clientCategoryId: '1'
      }, true);
      this.clientIdListPetitioner = this.cas.foreachlist(results[5], this.cas.dropdownlist.client.clientId.key, {
        clientCategoryId: ['2', '3']
      }, true);

      if (!this.newPage) {
        this.fill(js);
      }

    }, (err) => {
      this.toastr.error(err, "");
    });
    if (this.auth.classId != '3')
      this.form.controls.classId.patchValue(this.auth.classId);
    this.spin.hide();
  }
  btntext = "Save";
  pageflow = "New";
  fill(data: any) {
    this.spin.show();
    this.pageflow = data.pageflow;
    if (data.pageflow != 'New') {
      this.btntext = "Update";
      if (data.pageflow == 'Display') {
        this.form.disable();
        this.dropdownSettings.disabled = true;
        this.isbtntext = false;
      }
      this.form.controls.clientId.disable();
      this.form.controls.consultationDate.disable();
      this.form.controls.classId.disable();
      this.sub.add(this.service.Get(data.code).subscribe(res => {
        if (this.form.controls.isMailingAddressSame.value == true) {
          this.form.controls.isMailingAddressSame.patchValue(true);
        } else {
          this.form.controls.isMailingAddressSame.patchValue(false);
        }
        console.log(res)
        // this.form.patchValue(res);
        this.form.patchValue(res, {
          emitEvent: false
        });

        console.log(this.multireferralist)
        this.multiselectreferralList = [];
        this.multireferralist.forEach(x => {
          if (res.classId == x.classId) {
            this.multiselectreferralList.push(x)
          }
        })
        console.log(this.multiselectreferralList)

        this.form.controls.mailingAddress.patchValue(res.mailingAddress);
        this.form.controls.referenceField5.patchValue(res.referenceField5);
        this.form.controls.referenceField6.patchValue(res.referenceField6);
        this.form.controls.referenceField7.patchValue(res.referenceField7);
            this.form.controls.referenceField8.patchValue(res.referenceField8);
            this.form.controls.referenceField10.patchValue(res.referenceField10);


        this.form.controls.referenceField16.patchValue(res.referenceField16);
        this.form.controls.addressLine3.patchValue(res.addressLine3);
        this.form.controls.referenceField17.patchValue(res.referenceField17);
        this.form.controls.referenceField18.patchValue(res.referenceField18);
        this.form.controls.referenceField19.patchValue(res.referenceField19);
        this.form.controls.referenceField20.patchValue(res.referenceField20);

        console.log(this.form.controls.referenceField6.value)

        if (this.form.controls.classId.value == 1) {
          this.form.controls.corporationClientId.disable();
          this.form.controls.referenceField2.disable();
        }

        //  this.form.controls.suiteDoorNo.patchValue(res.suiteDoorNo)
        if(res.suiteDoorNo != null){
          this.form.controls.suiteDoorNo.patchValue(res.suiteDoorNo)
        }
        else{
          this.form.controls.suiteDoorNo.patchValue(res.addressLine2)
        }

      //  this.form.controls.suiteDoorNo.patchValue(res.addressLine2);
        //   this.form.controls.referenceField15.patchValue('false');
        if (this.form.controls.referenceField15.value == 'true') {
          this.form.controls.referenceField15.patchValue(true);
        } else {
          this.form.controls.referenceField15.patchValue(false);
        }

   


        console.log(this.form.controls.referenceField6.value)
        // if (this.selectedItems2 != null && this.selectedItems2 != undefined && this.selectedItems2.length > 0) {
        //   this.form.controls.referenceField2.patchValue([{ id: res.referenceField2, itemName: res.referenceField2 }]);
        // }
        // if (this.selectedItems6 != null && this.selectedItems6 != undefined && this.selectedItems6.length > 0) {
        //   this.form.controls.clientCategoryIdFE.patchValue([{ id: res.clientCategoryId, itemName: res.clientCategoryId }]);
        // }



        // this.multiclientcatist.forEach(element => {
        //   if(element.id == res.clientCategoryId){
        //     this.form.controls.clientCategoryIdFE.patchValue([element]);
        //   }
        // });





        // this.multiclientList.forEach(element => {
        //   if(element.id == res.corporationClientId){
        //     this.form.controls.corporationClientIdFE.patchValue([element]);
        //   }
        // });

        // this.multireferralist.forEach(element => {
        //   if (element.id == res.referralId) {
        //     console.log(this.form)
        //     this.form.controls.referralIdFE.patchValue([element]);
        //   }
        // });

        // if (this.selectedItems5 != null && this.selectedItems5 != undefined && this.selectedItems5.length > 0) {
        //   this.form.controls.referralId.patchValue([{id: res.referralId,itemName: res.referralId}]);}


        this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
        this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
        // if (this.form.invalid)
        //  this.form.controls.consultationDate.patchValue(null);

        //   let multiclientcatist: any[]=[]
        // multiclientcatist.push(res.clientCategoryId);
        // this.form.patchValue({clientCategoryIdFE: multiclientcatist });


        if (res.intakeFormId == 1 && res.fax == null) {
          this.sub.add(this.immigrationService.Get(res.classId, res.inquiryNumber, res.intakeFormId, res.intakeFormNumber, res.languageId).subscribe(ress => {
          if(ress){
            this.form.controls.fax.patchValue(ress.contactNumber.faxNo);
           }
          }));
        }
        if (res.intakeFormId == 2 && res.fax == null) {
          this.sub.add(this.englishService.Get(res.classId, res.inquiryNumber, res.intakeFormId, res.intakeFormNumber, res.languageId).subscribe(ress => {
          if(ress){
            this.form.controls.fax.patchValue(ress.contactNumber.faxNo);
           }
          }));
        }
        if (res.intakeFormId == 3 && res.fax == null) {
          this.sub.add(this.spnishService.Get(res.classId, res.inquiryNumber, res.intakeFormId, res.intakeFormNumber, res.languageId).subscribe(ress => {
          if(ress){
            this.form.controls.fax.patchValue(ress.contactNumber.faxNo);
           }
          }));
        }
        if (res.intakeFormId == 4 && res.fax == null) {
          this.sub.add(this.english400Service.Get(res.classId, res.inquiryNumber, res.intakeFormId, res.intakeFormNumber, res.languageId).subscribe(ress => {
          if(ress){
            this.form.controls.fax.patchValue(ress.contactNumber.faxNo);
           }
          }));
        }
        if (res.intakeFormId == 5 && res.fax == null) {
          this.sub.add(this.dacaService.Get(res.classId, res.inquiryNumber, res.intakeFormId, res.intakeFormNumber, res.languageId).subscribe(ress => {
          if(ress){
            this.form.controls.fax.patchValue(ress.contactNumber.faxNo);
           }
          }));
        }
        if (res.intakeFormId == 6 && res.fax == null) {
          this.sub.add(this.landeService.Get(res.classId, res.inquiryNumber, res.intakeFormId, res.intakeFormNumber, res.languageId).subscribe(ress => {
           if(ress){
            this.form.controls.fax.patchValue(ress.contactNumber.faxNo);
           }
          }));
        }


        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }
  }

  submit() {

    console.log(this.form.controls.corporationClientId.value);
    this.submitted = true;
    this.form.updateValueAndValidity;

    console.log(this.form.controls.corporationClientId.value);
  

    let code = this.route.snapshot.params.code;
    let js: any = {}
    js = this.cs.decrypt(code);
      if(js.pageflow == 'New'){
        this.form.controls['referenceField1'].setValidators([Validators.pattern('[a-zA-Z0-9 \S]+')]);
        this.form.controls['lastName'].setValidators([Validators.pattern('[a-zA-Z0-9 \S]+')]);
        this.form.controls['firstNameLastName'].setValidators([Validators.pattern('[a-zA-Z0-9 \S]+')]);
        this.form.controls['firstName'].setValidators([Validators.pattern('[a-zA-Z0-9 \S]+')]);
        this.form.controls['referenceField1'].updateValueAndValidity();
        this.form.controls['lastName'].updateValueAndValidity();
        this.form.controls['firstNameLastName'].updateValueAndValidity();
        this.form.controls['firstName'].updateValueAndValidity();

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
      
    if (this.form.controls.clientCategoryId.value.id == 3) {
      //   alert(2)
      if (!this.selectedItems3) {
        this.toastr.error(
          "Please fill corporation fields to continue",
          ""
        );
        return;
      }
    }

    if (this.form.controls.clientCategoryId.value.id == 4) {
      if (!this.form.controls.referenceField2.value) {
        this.toastr.error(
          "Please fill Petitioner fields to continue",
          ""
        );
        return;
      }
    }




    this.cs.notifyOther(false);
    this.spin.show();
    this.form.removeControl('updatedOn');
    this.form.removeControl('createdOn');
    this.form.patchValue({
      updatedby: this.auth.username
    });
    // if (this.selectedItems3 != null && this.selectedItems3 != undefined && this.selectedItems3.length > 0) {
    //   this.form.patchValue({ corporationClientId: this.selectedItems3[0].id });
    // }

    // if (this.selectedItems2 != null && this.selectedItems2 != undefined && this.selectedItems2.length > 0) {
    //   this.form.patchValue({ referenceField2: this.selectedItems2[0].id });
    // }
    // else {
    //   this.form.patchValue({ referenceField2: null });
    // }

    // if (this.selectedItems5 != null && this.selectedItems5 != undefined && this.selectedItems5.length > 0) {
    //   this.form.patchValue({ referralId: this.selectedItems5[0].id });
    // }
    // else {
    //   this.form.patchValue({ referralId: null });
    // }

    // if (this.selectedItems6 != null && this.selectedItems6 != undefined && this.selectedItems6.length > 0) {
    //   this.form.patchValue({ clientCategoryId: this.selectedItems6[0] });
    // }
    // else {
    //   this.form.patchValue({ clientCategoryId: null });
    // }

    // if (this.form.controls.clientCategoryId.value == 3) {
    //   if (!this.selectedItems3) {
    //     this.toastr.error(
    //       "Please fill corporation fields to continue",
    //       ""
    //     );
    //     return;
    //   }
    // }

    // if (this.form.controls.clientCategoryId.value == 4) {
    //   if (!this.selectedItems2) {
    //     this.toastr.error(
    //       "Please fill Petitioner fields to continue",
    //       ""
    //     );
    //     return;
    //   }
    // }


    // if (this.selectedItems5 && this.selectedItems5.length > 0)
    // {
    //   let multireferralist: any[]=[]
    //   this.selectedItems5.forEach((a: any)=> multireferralist.push(a.id))
    //   this.form.patchValue({referralId: multireferralist });
    // }

    if (this.form.controls.clientId.value) {
      this.sub.add(this.service.Update(this.form.getRawValue(), this.form.controls.clientId.value).subscribe(res => {

        this.clientUserService.searchClientList({clientId: [this.form.controls.clientId.value]}).subscribe(clientUserResult =>{
          let obj: any = {}
          obj.contactNumber = this.form.controls.contactNumber.value;
          obj.emailId = this.form.controls.emailId.value;
          obj.firstName = this.form.controls.firstNameLastName.value;
          this.clientUserService.patchClientUser(obj, clientUserResult[0].clientUserId).subscribe(clientUserUpdateResult => {

          })
        })
        this.toastr.success(this.form.controls.clientId.value + " updated successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();

        this.router.navigate(['/main/client/matters/' + this.route.snapshot.params.code]);



      }, err => {

        this.cs.commonerror(err);
        this.spin.hide();

      }));
    } else {
      this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
        this.toastr.success(res.clientId + " saved successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();
        this.router.navigate(['/main/client/clientlist']);

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


  onItemSelect(item: any) {
    console.log(item.id);
    console.log(this.multimatterList);
  }
  OnItemDeSelect(item: any) {
    console.log(item);
  }
  onSelectAll(items: any) {
    console.log(items);
  }
  onDeSelectAll(items: any) {
    console.log(items);
  }
  change(item: any) {
    console.log(item)
    console.log(this.form.controls.corporationClientId.value);
  }


}
