import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, FormControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { MasterService } from 'src/app/shared/master.service';
import { AltpartService } from '../altpart.service';

@Component({
  selector: 'app-altpart-new',
  templateUrl: './altpart-new.component.html',
  styleUrls: ['./altpart-new.component.scss']
})
export class AltpartNewComponent implements OnInit {

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
    private service :AltpartService ,
    private masterService: MasterService
    ) { }



  form = this.fb.group({
    companyCodeId: [],
    plantId: [],
    languageId: [],
    warehouseId: [this.auth.warehouseId,Validators.required],
    itemCode: [,Validators.required],
    altItemCode: [,Validators.required],
    subBarcode: [],
    manufacturer: [],
    brand: [],
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
    deletionIndicator: [],
    createdBy: [],
    createdOn: [],
    createdOnFE:[],
    updatedBy: [],
    updatedOn: [],
    updatedOnFE:[],
  
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
   
    this.form.controls.warehouseId.patchValue(this.auth.warehouseId);  
    this.form.controls.languageId.patchValue(this.auth.languageId);
    this.form.controls.plantId.patchValue(this.auth.plantId);
    this.form.controls.companyCodeId.patchValue(this.auth.companyId);
   this.form.controls.warehouseId.disable();
   
      this.dropdownlist();      
    
 
  
    if (this.js.pageflow != 'New') {
      this.form.controls.altItemCode.disable();
      this.form.controls.warehouseId.patchValue(this.form.controls.warehouseId.value);
      //this.form.controls.putawayBlock.patchValue(this.form.controls.putawayBlock.value);
      // this.form.controls.warehouseId.disable();
      if (this.js.pageflow == 'Display')
     
      
        this.form.disable();
       this.fill();
    }
  }
  sub = new Subscription();
  fill(){
    this.spin.show();
    this.sub.add(this.service.Get(this.js.code,this.js.warehouseId,this.js.languageId,this.js.plantId,this.js.companyCodeId,this.js.itemCode).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });
     this.form.controls.createdOnFE.patchValue(this.cs.dateTimeApi(this.form.controls.createdOn.value));
     this.form.controls.updatedOnFE.patchValue(this.cs.dateTimeApi(this.form.controls.updatedOn.value));
    }))
    this.spin.hide();
  }
  floorList: any[] = [];
  levelList:any[]=[];
  storagesectionList: any[] = [];
  itemgroupList: any[] = [];
  warehouseIdList: any[] = [];
    partnercodeList: any[] = [];
    binclassList:any[]=[];
    aisleList:any[]=[];
    dropdownlist(){
      this.spin.show();
      this.cas.getalldropdownlist([
        this.cas.dropdownlist.setup.warehouseid.url,
     this.cas.dropdownlist.setup.floorid.url,
        this.cas.dropdownlist.setup.levelid.url,
        this.cas.dropdownlist.setup.storagesectionid.url,
       this.cas.dropdownlist.setup.binclassid.url,
       this.cas.dropdownlist.setup.aisleid.url,
      ]).subscribe((results) => {
     this.warehouseIdList=this.cas.forLanguageFilter(results[0],this.cas.dropdownlist.setup.warehouseid.key);
      this.floorList=this.cas.forLanguageFilter(results[1],this.cas.dropdownlist.setup.floorid.key);
      this.levelList=this.cas.forLanguageFilter(results[2],this.cas.dropdownlist.setup.levelid.key);
   this.storagesectionList=this.cas.forLanguageFilter(results[3],this.cas.dropdownlist.setup.storagesectionid.key);
  this.binclassList=this.cas.forLanguageFilter(results[4],this.cas.dropdownlist.setup.binclassid.key);
  this.aisleList=this.cas.forLanguageFilter(results[5],this.cas.dropdownlist.setup.aisleid.key);
  if(this.auth.userTypeId != 1){
    this.form.controls.warehouseId.patchValue(this.auth.warehouseId);  
    this.form.controls.languageId.patchValue(this.auth.languageId);
    this.form.controls.plantId.patchValue(this.auth.plantId);
    this.form.controls.companyCodeId.patchValue(this.auth.companyId);
    this.form.controls.warehouseId.disable();
  }
      });
      this.spin.hide();
    }
    superadmindropdownlist(){
      this.spin.show();
      this.cas.getalldropdownlist([
        this.cas.dropdownlist.setup.warehouseid.url,
     this.cas.dropdownlist.setup.floorid.url,
        this.cas.dropdownlist.setup.levelid.url,
        this.cas.dropdownlist.setup.storagesectionid.url,
       this.cas.dropdownlist.setup.binclassid.url,
       this.cas.dropdownlist.setup.aisleid.url,
      ]).subscribe((results) => {
      this.warehouseidDropdown = results[0];
      this.warehouseidDropdown.forEach(element => {
        this.warehouseIdList.push({value: element.warehouseId, label: element.warehouseId + '-' + element.warehouseDesc, companyCodeId: element.companyCodeId, plantId: element.plantId, languageId: element.languageId});
      });
      this.floorList=this.cas.foreachlist2(results[1],this.cas.dropdownlist.setup.floorid.key);
      this.levelList=this.cas.foreachlist2(results[2],this.cas.dropdownlist.setup.levelid.key);
   this.storagesectionList=this.cas.foreachlist2(results[3],this.cas.dropdownlist.setup.storagesectionid.key);
  this.binclassList=this.cas.foreachlist2(results[4],this.cas.dropdownlist.setup.binclassid.key);
  this.aisleList=this.cas.foreachlist2(results[5],this.cas.dropdownlist.setup.aisleid.key);
  if(this.auth.userTypeId == 1){
    this.form.controls.warehouseId.patchValue(this.form.controls.warehouseId.value);  
    this.form.controls.languageId.patchValue(this.form.controls.languageId.value);
    this.form.controls.plantId.patchValue(this.form.controls.plantId.value);
    this.form.controls.companyCodeId.patchValue(this.form.controls.companyCodeId.value);
    this.form.controls.storageSectionId.patchValue(this.form.controls.storageSectionId.value);
   // this.form.controls.warehouseId.disable();
  }
      });
      this.spin.hide();
    }
    onWarehouseChange(value){
      console.log(55);
      this.form.controls.companyCodeId.patchValue(value.companyCodeId);
      this.form.controls.languageId.patchValue(value.languageId);
      this.form.controls.plantId.patchValue(value.plantId);
      
     
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
    this.sub.add(this.service.Update(this.form.getRawValue(),this.js.code,this.js.warehouseId,this.js.languageId,this.js.plantId,this.js.companyCodeId,this.js.itemCode).subscribe(res => {
      this.toastr.success(res.altItemCode + " updated successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.router.navigate(['/main/masternew/altpart']);

      this.spin.hide();
  
    }, err => {
  
      this.cs.commonerrorNew(err);
      this.spin.hide();
  
    }));
  }else{
    this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
      this.toastr.success(res.altItemCode + " Saved Successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.router.navigate(['/main/masternew/altpart']);
      this.spin.hide();
  
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
  
    }));
   }
  
  }

}



