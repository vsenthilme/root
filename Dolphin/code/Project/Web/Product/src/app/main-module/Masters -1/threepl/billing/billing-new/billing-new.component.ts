import { Component, Inject, OnInit } from '@angular/core';
import { Validators, FormBuilder, FormControl } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { BillingService } from '../billing.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { MasterService } from 'src/app/shared/master.service';

@Component({
  selector: 'app-billing-new',
  templateUrl: './billing-new.component.html',
  styleUrls: ['./billing-new.component.scss']
})
export class BillingNewComponent implements OnInit {

  warehouseidDropdown: any;
  isLinear = false;
  constructor(private fb: FormBuilder,
    private auth: AuthService,
    public toastr: ToastrService,
    // private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private route: ActivatedRoute, private router: Router,
    private cs: CommonService,
    private cas: CommonApiService,
    private service : BillingService,
    private masterService: MasterService
    ) { }



  form = this.fb.group({
    billFrequencyId: [],
  billGenerationIndicator: [],
  billModeId: [],
  companyCodeId: [],
  deletionIndicator: [],
  languageId: [],
  moduleId: [,Validators.required],
  partnerCode: [,Validators.required],
  paymentModeId: [],
  paymentTermId: [],
  plantId: [],
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
  statusId: [],
  warehouseId: [,Validators.required],
  createdOnFE:[],
  updatedOnFE:[],
  createdOn:[],
  updatedOn:[],
  updatedBy:[],
  createdBy:[],
  
  });


  filterpartnercodeList: any[] = [];
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  public icon = 'expand_more';

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
  submitted = false;
  email = new FormControl('', [Validators.required, Validators.email]);
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  getErrorMessage() {
    if (this.email.hasError('required')) {
      return ' Field should not be blank';
    }
    return this.email.hasError('email') ? 'Not a valid email' : '';
  }
  js: any = {}
  ngOnInit(): void {
    let code = this.route.snapshot.params.code;
    this.js = this.cs.decrypt(code);

    this.form.controls.updatedBy.disable();
    this.form.controls.createdBy.disable();
    this.form.controls.updatedOnFE.disable();
    this.form.controls.createdOnFE.disable();
    
   
 
      this.form.controls.languageId.patchValue(this.auth.languageId);
      this.form.controls.warehouseId.patchValue(this.auth.warehouseId);
      this.form.controls.companyCodeId.patchValue(this.auth.companyId);
      this.form.controls.plantId.patchValue(this.auth.languageId);
      this.form.controls.plantId.disable();
      this.dropdownlist();
 
   
     
       
       
       
    if (this.js.pageflow != 'New') {
      this.form.controls.partnerCode.disable();
      if (this.js.pageflow == 'Display')
        this.form.disable();
       this.fill();
    }
  }
  sub = new Subscription();
  fill(){
    this.spin.show();
    this.sub.add(this.service.Get(this.js.code,this.js.warehouseId,this.js.languageId,this.js.plantId,this.js.companyCodeId,this.js.moduleId).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });
     this.form.controls.createdOnFE.patchValue(this.cs.dateTimeApi(this.form.controls.createdOn.value));
     this.form.controls.updatedOnFE.patchValue(this.cs.dateTimeApi(this.form.controls.updatedOn.value));
    }))
    this.spin.hide();
  }
  floorList: any[] = [];
  moduleList:any[]=[];
  paymenttermList: any[] = [];
  itemgroupList: any[] = [];
  warehouseIdList: any[] = [];
    partnercodeList: any[] = [];
    billingmodeList:any[]=[];
    billingfrequencyList:any[]=[];
    paymentmodeList:any[]=[];
    companyList: any[] = [];
    plantList: any[]=[];
    languageList: any[]=[];
    dropdownlist(){
      this.spin.show();
      this.cas.getalldropdownlist([
        this.cas.dropdownlist.setup.warehouseid.url,
        this.cas.dropdownlist.setup.floorid.url,

        this.cas.dropdownlist.setup.paymenttermid.url,
        
       this.cas.dropdownlist.setup.moduleid.url,
       this.cas.dropdownlist.setup.billingmodeid.url,
      this.cas.dropdownlist.setup.billingfrequencyid.url,
      this.cas.dropdownlist.setup.paymentmodeid.url,
      this.cas.dropdownlist.setup.companyid.url,
      this.cas.dropdownlist.setup.plantid.url,
      this.cas.dropdownlist.setup.languageid.url,
      ]).subscribe((results) => {
       this.warehouseIdList=this.cas.forLanguageFilter(results[0],this.cas.dropdownlist.setup.warehouseid.key);
   this.floorList=this.cas.forLanguageFilter(results[1],this.cas.dropdownlist.setup.floorid.key);
   this.paymenttermList=this.cas.forLanguageFilter(results[2],this.cas.dropdownlist.setup.paymenttermid.key);
// this.moduleList=this.cas.forLanguageFilter(results[3],this.cas.dropdownlist.setup.moduleid.key);
 this.billingmodeList=this.cas.forLanguageFilter(results[4],this.cas.dropdownlist.setup.billingmodeid.key);
 this.billingfrequencyList=this.cas.forLanguageFilter(results[5],this.cas.dropdownlist.setup.billingfrequencyid.key);
 this.paymentmodeList=this.cas.forLanguageFilter(results[6],this.cas.dropdownlist.setup.paymentmodeid.key);
 this.companyList = this.cas.forLanguageFilter(results[7], this.cas.dropdownlist.setup.companyid.key);
 this.plantList = this.cas.forLanguageFilter(results[8], this.cas.dropdownlist.setup.plantid.key);
 this.languageList=this.cas.foreachlist2(results[9],this.cas.dropdownlist.setup.languageid.key); 
    this.form.controls.warehouseId.patchValue(this.auth.warehouseId);  
    this.form.controls.languageId.patchValue(this.auth.languageId);
    this.form.controls.plantId.patchValue(this.auth.plantId);
    this.form.controls.companyCodeId.patchValue(this.auth.companyId);
    this.masterService.searchpaymentterm({companyCodeId: this.auth.companyId, warehouseId:this.auth.warehouseId, plantId:this.auth.plantId, languageId: [this.auth.languageId]}).subscribe(res => {
      this.paymenttermList = [];
     res.forEach(element => {
         this.paymenttermList.push({value: element.paymentTermId, label: element.paymentTermId + '-' + element.description});
       })
    });
    this.masterService.searchModule({companyCodeId: this.auth.companyId, plantId:this.auth.plantId, warehouseId: this.auth.warehouseId, languageId: [this.auth.languageId]}).subscribe(res => {
      this.moduleList = [];
      res.forEach(element => {
        this.moduleList.push({value: element.moduleId, label: element.moduleId + '-' + element.moduleDescription});
      });
    });
    this.masterService.searchbillfrequency({companyCodeId: this.auth.companyId, warehouseId:this.auth.warehouseId, plantId:this.auth.plantId, languageId: [this.auth.languageId]}).subscribe(res => {
      this.billingfrequencyList = [];
     res.forEach(element => {
         this.billingfrequencyList.push({value: element.billFrequencyId, label: element.billFrequencyId + '-' + element.description});
       })
    });
    this.masterService.searchbillmode({companyCodeId: this.auth.companyId, warehouseId:this.auth.warehouseId, plantId:this.auth.plantId, languageId: [this.auth.languageId]}).subscribe(res => {
      this.billingmodeList = [];
     res.forEach(element => {
         this.billingmodeList.push({value: element.billModeId, label: element.billModeId + '-' + element.description});
       })
    });
    this.masterService.searchpaymentmode({companyCodeId: this.auth.companyId, warehouseId:this.auth.warehouseId, plantId:this.auth.plantId, languageId: [this.auth.languageId]}).subscribe(res => {
      this.paymentmodeList = [];
     res.forEach(element => {
         this.paymentmodeList.push({value: element.paymentModeId, label: element.paymentModeId + '-' + element.description});
       })
    });
    this.form.controls.warehouseId.disable();
    this.form.controls.companyId.disable();
    this.form.controls.plantId.disable();
    this.form.controls.languageId.disable();
  
      });
    
      this.spin.hide();
    }
    
   
     onWarehouseChange(value){
     console.log(value);
     this.form.controls.companyCodeId.patchValue(value.companyCodeId);
     this.form.controls.languageId.patchValue(value.languageId);
      this.form.controls.plantId.patchValue(value.plantId);
      
      this.masterService.searchpaymentterm({companyCodeId: this.form.controls.companyCodeId.value, warehouseId:value.value, plantId:this.form.controls.plantId.value, languageId: [this.form.controls.languageId.value]}).subscribe(res => {
       this.paymenttermList = [];
      res.forEach(element => {
          this.paymenttermList.push({value: element.paymentTermId, label: element.paymentTermId + '-' + element.description});
        })
     });
     this.masterService.searchModule({companyCodeId: this.form.controls.companyCodeId.value, warehouseId:value.value, plantId:this.form.controls.plantId.value, languageId: [this.form.controls.languageId.value]}).subscribe(res => {
      this.moduleList = [];
     res.forEach(element => {
         this.moduleList.push({value: element.moduleId, label: element.moduleId + '-' + element.moduleDescription});
       })
    });
    this.masterService.searchbillfrequency({companyCodeId: this.form.controls.companyCodeId.value, warehouseId:value.value, plantId:this.form.controls.plantId.value, languageId: [this.form.controls.languageId.value]}).subscribe(res => {
      this.billingfrequencyList = [];
     res.forEach(element => {
         this.billingfrequencyList.push({value: element.billFrequencyId, label: element.billFrequencyId + '-' + element.description});
       })
    });
    this.masterService.searchbillmode({companyCodeId: this.form.controls.companyCodeId.value, warehouseId:value.value, plantId:this.form.controls.plantId.value, languageId: [this.form.controls.languageId.value]}).subscribe(res => {
      this.billingmodeList = [];
     res.forEach(element => {
         this.billingmodeList.push({value: element.billModeId, label: element.billModeId + '-' + element.description});
       })
    });
    this.masterService.searchpaymentmode({companyCodeId: this.form.controls.companyCodeId.value, warehouseId:value.value, plantId:this.form.controls.plantId.value, languageId: [this.form.controls.languageId.value]}).subscribe(res => {
      this.paymentmodeList = [];
     res.forEach(element => {
         this.paymentmodeList.push({value: element.paymentModeId, label: element.paymentModeId + '-' + element.description});
       })
    });
    //   t his.masterService.searchstoragesection({companyCodeId: [this.form.controls.companyCodeId.value], plantId: [this.form.controls.plantId.value], warehouseId:[value.value], languageId: [this.form.controls.languageId.value],floorId:[this.form.controls.floorId.value]}).subscribe(res => {
    //     this.storagesectionList = [];
    //     res.forEach(element => {
    //       this.storagesectionList.push({value: element.storageSectionId, label: element.storageSectionId + '-' + element.storageSection});
    //     })
    //   });
      
    //   this.masterService.searchaisle({companyCodeId:this.form.controls.companyCodeId.value ,plantId: this.form.controls.plantId.value,warehouseId:value.value, languageId: [this.form.controls.languageId.value],storageSectionId:[this.form.controls.storageSectionId.value],floorId:[this.form.controls.floorId.value]}).subscribe(res => {
    //     this.aisleList = [];
    //     res.forEach(element => {
    //       this.aisleList.push({value: element.aisleId, label: element.aisleId + '-' + element.aisleDescription});
    //     })
    //   });
     }
    // onfloorChange(value){
    //   this.masterService.searchstoragesection({companyCodeId: [this.form.controls.companyCodeId.value], plantId: [this.form.controls.plantId.value], warehouseId:[this.form.controls.warehouseId.value], languageId: [this.form.controls.languageId.value],floorId:[value.value]}).subscribe(res => {
    //     this.storagesectionList = [];
    //     res.forEach(element => {
    //       this.storagesectionList.push({value: element.storageSectionId, label: element.storageSectionId + '-' + element.storageSection});
    //    })
    //   });
    //   this.masterService.searchaisle({companyCodeId:this.form.controls.companyCodeId.value ,plantId: this.form.controls.plantId.value,warehouseId:this.form.controls.warehouseId.value, languageId: [this.form.controls.languageId.value],storageSectionId:[this.form.controls.storageSectionId.value],floorId:[value.value]}).subscribe(res => {
    //     this.aisleList = [];
    //     res.forEach(element => {
    //       this.aisleList.push({value: element.aisleId, label: element.aisleId + '-' + element.aisleDescription});
    //     })
    //   });
    // }
    // onStorageSectionChange(value){
    //   this.masterService.searchaisle({companyCodeId: this.form.controls.companyCodeId.value, plantId: this.form.controls.plantId.value, warehouseId:this.form.controls.warehouseId.value, languageId: [this.form.controls.languageId.value],floorId:[this.form.controls.floorId.value],storageSectionId:[value.value]}).subscribe(res => {
    //     this.aisleList = [];
    //     res.forEach(element => {
    //       this.aisleList.push({value: element.aisleId, label: element.aisleId + '-' + element.aisleDescription});
    //    })
    //   });
    // }
  submit() {
    this.submitted = true;
    if (this.form.invalid) {
      this.toastr.error(
        "Please fill required fields to continue",
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
  this.form.controls.createdOn.patchValue(this.cs.day_callapiSearch(this.form.controls.createdOn.value));
  this.form.controls.updatedOn.patchValue(this.cs.day_callapiSearch(this.form.controls.updatedOn.value));
  if (this.js.code) {
    this.sub.add(this.service.Update(this.form.getRawValue(),this.js.code,this.js.warehouseId,this.js.languageId,this.js.plantId,this.js.companyCodeId,this.js.moduleId).subscribe(res => {
      this.toastr.success(res.storageBin + " updated successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.router.navigate(['/main/threePLmaster/billing']);

      this.spin.hide();
  
    }, err => {
  
      this.cs.commonerrorNew(err);
      this.spin.hide();
  
    }));
  }else{
    this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
      this.toastr.success(res.paymentModeId + " Saved Successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.router.navigate(['/main/threePLmaster/billing']);
      this.spin.hide();
  
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
  
    }));
   }
  
  }

}



