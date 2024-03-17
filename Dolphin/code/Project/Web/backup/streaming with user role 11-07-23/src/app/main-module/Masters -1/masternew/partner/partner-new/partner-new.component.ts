import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { PartnerService } from '../partner.service';
import { MasterService } from 'src/app/shared/master.service';

@Component({
  selector: 'app-partner-new',
  templateUrl: './partner-new.component.html',
  styleUrls: ['./partner-new.component.scss']
})
export class PartnerNewComponent implements OnInit {

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
    private service : PartnerService,
    private masterService: MasterService ) { }



  form = this.fb.group({
    companyCodeId: [this.auth.companyId,],
    languageId: [this.auth.languageId,],
    plantId: [this.auth.plantId,],
    uomId: [],
    brandName: [],
    businessPartnerType: [,Validators.required],
    businessPartnerCode: [,Validators.required],
    createdBy: [],
    createdOn:[],
    createdOnFE:[],
    deletionIndicator: [],
    partnerItemBarcode: [],
    itemCode: [,Validators.required],
    mfrBarcode: [],
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
    partnerItemNo:[],
    stock: [],
    stockUom: [],
    updatedBy: [],
    updatedOn:[],
    updatedOnFE:[],
    vendorItemBarcode: [],
  
    warehouseId: [this.auth.warehouseId,Validators.required],
  
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
   
      this.form.controls.companyCodeId.patchValue(this.auth.companyId);
      this.form.controls.languageId.patchValue(this.auth.languageId);
      this.form.controls.plantId.patchValue(this.auth.plantId);
      this.dropdownlist();
      
      
    if (this.js.pageflow != 'New') {
      this.form.controls.businessPartnerCode.disable();
      this.form.controls.warehouseId.patchValue(this.form.controls.warehouseId.value);
      this.form.controls.warehouseId.disable();
      if (this.js.pageflow == 'Display')
        this.form.disable();
       this.fill();
    }
  }
  sub = new Subscription();
  fill(){
    this.spin.show();
    this.sub.add(this.service.Get(this.js.code,this.js.warehouseId,this.js.plantId,this.js.languageId,this.js.companyCodeId,this.js.businessPartnerType,this.js.itemCode,this.js.partnerItemBarcode).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });
     this.form.controls.createdOnFE.patchValue(this.cs.dateTimeApi(this.form.controls.createdOn.value));
     this.form.controls.updatedOnFE.patchValue(this.cs.dateTimeApi(this.form.controls.updatedOn.value));
    }))
    this.spin.hide();
  }
  warehouseIdList: any[] = [];
    partnercodeList: any[] = [];
    dropdownlist(){
      this.spin.show();
      this.cas.getalldropdownlist([
        this.cas.dropdownlist.setup.warehouseid.url,
         this.cas.dropdownlist.master.businesspartner.url,

      ]).subscribe((results) => {
      this.warehouseIdList = this.cas.forLanguageFilter(results[0], this.cas.dropdownlist.setup.warehouseid.key);
       this.partnercodeList = this.cas.foreachlist2(results[1], this.cas.dropdownlist.master.businesspartner.key);
  
        this.form.controls.warehouseId.patchValue(this.auth.warehouseId);  
        this.form.controls.languageId.patchValue(this.auth.languageId);
        this.form.controls.plantId.patchValue(this.auth.plantId);
        this.form.controls.companyCodeId.patchValue(this.auth.companyId);
        this.form.controls.warehouseId.disable();
      
      this.spin.hide();
      }, (err) => {
        this.toastr.error(err, "");
        this.spin.hide();
      });
    }
   
    onWarehouseChange(value){
      this.form.controls.companyCodeId.patchValue(value.companyCodeId);
      this.form.controls.languageId.patchValue(value.languageId);
      this.form.controls.plantId.patchValue(value.plantId);
      
      this.masterService.searchpartner({ warehouseId:[value.value] }).subscribe(res => {
        this.partnercodeList = [];
        res.forEach(element => {
          this.partnercodeList.push({value: element.partnerCode, label: element.partnerCode + '-' + element.partnerName});
        })
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
    this.sub.add(this.service.Update(this.form.getRawValue(),this.js.code,this.js.warehouseId,this.js.plantId,this.js.languageId,this.js.companyCodeId,this.js.businessPartnerType,this.js.itemCode,this.js.partnerItemBarcode).subscribe(res => {
      this.toastr.success(res.businessPartnerCode + " updated successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.router.navigate(['/main/masternew/partner']);

      this.spin.hide();
  
    }, err => {
  
      this.cs.commonerrorNew(err);
      this.spin.hide();
  
    }));
  }else{
    this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
      this.toastr.success(res.businessPartnerCode + " Saved Successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.router.navigate(['/main/masternew/partner']);
      this.spin.hide();
  
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
  
    }));
   }
  
  }

}

