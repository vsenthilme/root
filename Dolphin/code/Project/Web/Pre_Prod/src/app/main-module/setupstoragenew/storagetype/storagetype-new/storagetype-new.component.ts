import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { StoragetypeService } from '../storagetype.service';
import { MasterService } from 'src/app/shared/master.service';

@Component({
  selector: 'app-storagetype-new',
  templateUrl: './storagetype-new.component.html',
  styleUrls: ['./storagetype-new.component.scss']
})
export class StoragetypeNewComponent implements OnInit {

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
    private service : StoragetypeService,
    private masterService: MasterService,) { }



  form = this.fb.group({
    addToExistingStock: [],
  allowNegativeStock: [],
  capacityByQty:[],
  capacityByWeight: [],
  capacityCheck: [],
  companyId: [],
  createdBy: [],
  createdOn: [],
  createdOnFE:[],
  deletionIndicator: [],
  languageId: [],
  mixToStock: [0,],
  plantId: [],
  returnToSameStorageType:[],
  storageClassId: [,Validators.required],
  storageTemperatureFrom: [],
  storageTemperatureTo: [],
  storageTypeId: [,Validators.required],
  storageUom: [],
  updatedBy: [],
  updatedOn: [],
  updatedOnFE:[],
  warehouseId: [,Validators.required],
  });



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
    if(this.auth.userTypeId != 1){
      this.dropdownlist();
    
      
    }
    if(this.auth.userTypeId == 1){
      this.superadmindropdownlist();
      }
    if (this.js.pageflow != 'New') {
      if (this.js.pageflow == 'Display')
        this.form.disable();
       this.fill();
    }
  }
  sub = new Subscription();
  fill(){
    this.spin.show();
    this.sub.add(this.service.Get(this.js.code,this.js.warehouseId,this.js.storageClassId,this.js.languageId,this.js.plantId,this.js.companyId).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });
       this.form.controls.createdOnFE.patchValue(this.cs.dateTimeApi(this.form.controls.createdOn.value));
       this.form.controls.updatedOnFE.patchValue(this.cs.dateTimeApi(this.form.controls.updatedOn.value));
    }))
    this.spin.hide();
  }
  warehouseidList: any[] = [];
  subitemgroupList: any[] = [];
  itemtypeList: any[] = [];
  storagetypeList: any[] = [];
  itemgroupList: any[] = [];
  storageclassList: any[] = [];
  dropdownlist(){
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.warehouseid.url,
      this.cas.dropdownlist.setup.storagetypeid.url,
      this.cas.dropdownlist.setup.storageclassid.url,
    ]).subscribe((results) => {
    this.warehouseidList = this.cas.forLanguageFilter(results[0], this.cas.dropdownlist.setup.warehouseid.key);
    this.storagetypeList = this.cas.forLanguageFilter(results[1], this.cas.dropdownlist.setup.storagetypeid.key);
    this.storagetypeList = this.cs.removeDuplicatesFromArrayNewstatus(this.storagetypeList);
    this.storageclassList = this.cas.forLanguageFilter(results[2], this.cas.dropdownlist.setup.storageclassid.key);
    this.storageclassList = this.cs.removeDuplicatesFromArrayNewstatus(this.storageclassList);
    if(this.auth.userTypeId != 1){
      this.form.controls.warehouseId.patchValue(this.auth.warehouseId);  
      this.form.controls.languageId.patchValue(this.auth.languageId);
      this.form.controls.plantId.patchValue(this.auth.plantId);
      this.form.controls.companyId.patchValue(this.auth.companyId);
      this.form.controls.warehouseId.disable();
    }
    this.spin.hide();
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }
  superadmindropdownlist(){
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.warehouseid.url,
      this.cas.dropdownlist.setup.storageclassid.url,
      this.cas.dropdownlist.setup.storagetypeid.url,
    ]).subscribe((results) => {
    this.warehouseidDropdown = results[0];
    this.warehouseidDropdown.forEach(element => {
      this.warehouseidList.push({value: element.warehouseId, label: element.warehouseId + '-' + element.warehouseDesc, companyCodeId: element.companyCodeId, plantId: element.plantId, languageId: element.languageId});
    });
  ;
 
 this.storageclassList=this.cas.foreachlist2(results[1],this.cas.dropdownlist.setup.storageclassid.key);
this.storagetypeList=this.cas.foreachlist2(results[2],this.cas.dropdownlist.setup.storagetypeid.key);
    });
    this.spin.hide();
  }
  onWarehouseChange(value){
    this.form.controls.companyId.patchValue(value.companyCodeId);
    this.form.controls.languageId.patchValue(value.languageId);
    this.form.controls.plantId.patchValue(value.plantId);

  
    this.masterService.searchstorageclass({companyCodeId:this.form.controls.companyId.value ,plantId:this.form.controls.plantId.value,warehouseId:value.value, languageId:[this.form.controls.languageId.value],}).subscribe(res => {
      this.storageclassList = [];
      res.forEach(element => {
        this.storageclassList.push({value: element.storageClassId, label: element.storageClassId + '-' + element.description});
      })
    });
    this.masterService.searchstoragetype({companyCodeId: this.form.controls.companyId.value, plantId: this.form.controls.plantId.value, warehouseId: value.value,storageClassId:[this.form.controls.storageClassId.value], languageId: [this.form.controls.languageId.value]}).subscribe(res => {
      this.storagetypeList = [];
      res.forEach(element => {
        this.storagetypeList.push({value: element.storageTypeId, label: element.storageTypeId + '-' + element.description});
      });
    });
  }
  onstorageclassChange(value){
    this.masterService.searchstoragetype({companyCodeId: this.form.controls.companyId.value, plantId: this.form.controls.plantId.value, warehouseId: this.form.controls.warehouseId.value,storageClassId:[value.value], languageId: [this.form.controls.languageId.value]}).subscribe(res => {
      this.storagetypeList = [];
      res.forEach(element => {
        this.storagetypeList.push({value: element.storageTypeId, label: element.storageTypeId + '-' + element.description});
      });
    });
  }
  onstoragetypeChange(value){
    this.masterService.searchstoragetype({companyCodeId: this.form.controls.companyId.value, warehouseId:this.form.controls.warehouseId.value, plantId:this.form.controls.plantId.value, 
      languageId: [this.form.controls.languageId.value], storageClassId: [this.form.controls.storageClassId.value],storageTypeId:[value.value]}).subscribe(res => {
        this.form.controls.description.patchValue(res[0].description);
      
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
    this.sub.add(this.service.Update(this.form.getRawValue(),this.js.code,this.auth.warehouseId,this.js.storageClassId,this.js.languageId,this.js.plantId,this.js.companyId).subscribe(res => {
      this.toastr.success(res.storageTypeId + " updated successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.router.navigate(['/main/productstorage/storagetype']);

      this.spin.hide();
  
    }, err => {
  
      this.cs.commonerrorNew(err);
      this.spin.hide();
  
    }));
  }else{
    this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
      this.toastr.success(res.storageTypeId + " Saved Successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.router.navigate(['/main/productstorage/storagetype']);
      this.spin.hide();
  
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
  
    }));
   }
  
  }

}

