import { Component, Inject, OnInit } from '@angular/core';
import { Validators, FormBuilder, FormControl } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { PricelistService } from '../pricelist.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { MasterService } from 'src/app/shared/master.service';

@Component({
  selector: 'app-pricelist-new',
  templateUrl: './pricelist-new.component.html',
  styleUrls: ['./pricelist-new.component.scss']
})
export class PricelistNewComponent implements OnInit {

 
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
    private service : PricelistService,
    private masterService: MasterService
    ) { }



  form = this.fb.group({
    languageId: [],
    companyCodeId: [],
    plantId: [],
    warehouseId: [,Validators.required],
    moduleId: [,Validators.required],
    priceListId: [,Validators.required],
    serviceTypeId: [,Validators.required],
    chargeRangeId:[],
    fromPeriod: [],
    toPeriod: [],
    chargeRangeFrom: [],
    chargeRangeTo: [],
    chargeUnit: [],
    pricePerChargeUnit: [],
    priceUnit: [],
    minMonthlyPrice: [],
    statusId: [],
    companyIdAndDescription: [],
    plantIdAndDescription: [],
    warehouseIdAndDescription: [],
    moduleIdAndDescription: [],
    serviceTypeIdAndDescription: [],
    description: [],
    deletionIndicator: [],
    referenceField1: [],
    referenceField2: [],
    referenceField3: [],
    referenceField4: [],
    referenceField5: [],
    referenceField6: [],
    referenceField7: [],
    referenceField8: [],
    referenceField9: [],
    referenceField10: [],
    createdBy: [],
    createdOn: [],
    updatedBy: [],
    updatedOn: [],
    updatedOnFE:[],
    createdOnFE:[],
  
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
      this.form.controls.warehouseId.disable();
      this.form.controls.plantId.disable();
      this.form.controls.languageId.disable();
      this.form.controls.companyCodeId.disable();
      this.dropdownlist();
   
       
       
       
    if (this.js.pageflow != 'New') {
      this.form.controls.priceListId.disable();
      if (this.js.pageflow == 'Display')
        this.form.disable();
       this.fill();
    }
  }
  sub = new Subscription();
  fill(){
    this.spin.show();
    this.sub.add(this.service.Get(this.js.code,this.js.warehouseId,this.js.languageId,this.js.plantId,this.js.companyCodeId,this.js.moduleId,this.js.chargeRangeId,this.js.serviceTypeId).subscribe(res => {
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
    serviceTypeList:any[]=[];
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
      this.cas.dropdownlist.setup.servicetypeid.url,
      this.cas.dropdownlist.setup.companyid.url,
      this.cas.dropdownlist.setup.plantid.url,
      this.cas.dropdownlist.setup.languageid.url,
      ]).subscribe((results) => {
        this.warehouseIdList=this.cas.forLanguageFilter(results[0],this.cas.dropdownlist.setup.warehouseid.key);
   this.floorList=this.cas.forLanguageFilter(results[1],this.cas.dropdownlist.setup.floorid.key);
   this.paymenttermList=this.cas.forLanguageFilter(results[2],this.cas.dropdownlist.setup.paymenttermid.key);
 //this.moduleList=this.cas.forLanguageFilter(results[3],this.cas.dropdownlist.setup.moduleid.key);
 this.billingmodeList=this.cas.forLanguageFilter(results[4],this.cas.dropdownlist.setup.billingmodeid.key);
 this.billingfrequencyList=this.cas.forLanguageFilter(results[5],this.cas.dropdownlist.setup.billingfrequencyid.key);
 this.serviceTypeList=this.cas.forLanguageFilter(results[6],this.cas.dropdownlist.setup.servicetypeid.key);
 this.companyList = this.cas.forLanguageFilter(results[7], this.cas.dropdownlist.setup.companyid.key);
 this.plantList = this.cas.forLanguageFilter(results[8], this.cas.dropdownlist.setup.plantid.key);
 this.languageList=this.cas.foreachlist2(results[9],this.cas.dropdownlist.setup.languageid.key);
    this.form.controls.warehouseId.patchValue(this.auth.warehouseId);  
    this.form.controls.languageId.patchValue(this.auth.languageId);
    this.form.controls.plantId.patchValue(this.auth.plantId);
    this.form.controls.companyCodeId.patchValue(this.auth.companyId);
    this.form.controls.warehouseId.disable();
    this.form.controls.companyCodeId.disable();
    this.form.controls.plantId.disable();
    this.form.controls.languageId.disable();
    this.masterService.searchModule({companyCodeId: this.auth.companyId, plantId:this.auth.plantId, warehouseId: this.auth.warehouseId, languageId: [this.auth.languageId]}).subscribe(res => {
      this.moduleList = [];
      res.forEach(element => {
        if(element.moduleDescription != null){
          this.moduleList.push({value: element.moduleId, label: element.moduleId + '-' + element.moduleDescription});
        }
      });
      this.moduleList = this.cs.removeDuplicatesFromArrayNewstatus(this.moduleList);
    });
  
      });
    
      this.spin.hide();
    }
  
  
  
     onWarehouseChange(value){
      console.log(value);
     this.form.controls.companyCodeId.patchValue(value.companyCodeId);
     this.form.controls.languageId.patchValue(value.languageId);
      this.form.controls.plantId.patchValue(value.plantId);
      
      
    
        this.form.controls.languageId.patchValue(this.auth.languageId);
        this.form.controls.plantId.patchValue(this.auth.plantId);
        this.form.controls.companyCodeId.patchValue(this.auth.companyId);
       
      
      this.masterService.searchModule({companyCodeId: this.form.controls.companyCodeId.value, plantId:this.form.controls.plantId.value, warehouseId: value.value, languageId: [this.form.controls.languageId.value]}).subscribe(res => {
        this.moduleList = [];
           res.forEach(element => {
        if(element.moduleDescription != null){
          this.moduleList.push({value: element.moduleId, label: element.moduleId + '-' + element.moduleDescription});
        }
      });
      this.moduleList = this.cs.removeDuplicatesFromArrayNewstatus(this.moduleList);
      });
      this.masterService.searchserviceType({companyCodeId: this.form.controls.companyCodeId.value, plantId:this.form.controls.plantId.value, warehouseId: value.value, languageId: [this.form.controls.languageId.value],moduleId:[this.form.controls.moduleId.value]}).subscribe(res => {
        this.moduleList = [];
        res.forEach(element => {
          if(element.moduleDescription != null){
          this.moduleList.push({value: element.serviceTypeId, label: element.serviceTypeId + '-' + element.serviceTypeDescription});
          }
        });
        this.moduleList = this.cs.removeDuplicatesFromArrayNewstatus(this.moduleList);
      });
  
     }
     onmoduleChange(value){
    
      this.masterService.searchserviceType({companyCodeId: this.form.controls.companyCodeId.value, plantId:this.form.controls.plantId.value, warehouseId: this.form.controls.warehouseId.value, languageId: [this.form.controls.languageId.value],moduleId:[value.value]}).subscribe(res => {
        this.serviceTypeList = [];
        res.forEach(element => {
          this.serviceTypeList.push({value: element.serviceTypeId, label: element.serviceTypeId + '-' + element.serviceTypeDescription});
        });
      });
     
  
     }
  
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
    this.sub.add(this.service.Update(this.form.getRawValue(),this.js.code,this.js.warehouseId,this.js.languageId,this.js.plantId,this.js.companyCodeId,this.js.moduleId,this.js.chargeRangeId,this.js.serviceTypeId).subscribe(res => {
      this.toastr.success(res.priceListId + " updated successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.router.navigate(['/main/threePLmaster/pricelist']);

      this.spin.hide();
  
    }, err => {
  
      this.cs.commonerrorNew(err);
      this.spin.hide();
  
    }));
  }else{
    this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
      this.toastr.success(res.priceListId + " Saved Successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.router.navigate(['/main/threePLmaster/pricelist']);
      this.spin.hide();
  
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
  
    }));
   }
  
  }

}



