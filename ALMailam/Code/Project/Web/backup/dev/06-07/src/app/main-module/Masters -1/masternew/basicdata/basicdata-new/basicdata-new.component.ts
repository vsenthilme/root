import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { BasicdataService } from '../basicdata.service';
import { MasterService } from 'src/app/shared/master.service';

@Component({
  selector: 'app-basicdata-new',
  templateUrl: './basicdata-new.component.html',
  styleUrls: ['./basicdata-new.component.scss']
})
export class BasicdataNewComponent implements OnInit {
  warehouseidDropdown: any;
  isLinear = false;
  constructor(private fb: FormBuilder,
    public auth: AuthService,
    public toastr: ToastrService,
    // private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private route: ActivatedRoute, private router: Router,
    private cs: CommonService,
    private cas: CommonApiService,
    private service : BasicdataService,
    private masterService: MasterService
    ) { }



  form = this.fb.group({
    companyCodeId: [this.auth.companyId,Validators.required],
    companyIdAndDescription: [],
    createdBy: [],
    createdOn: [],
    createdOnFE:[],
    deletionIndicator: [],
    description: [],
    eanUpcNo: [],
    hsnCode: [],
    itemCode: [,Validators.required],
    itemGroup: [],
    itemType: [],
    languageId: [this.auth.languageId,Validators.required],
    manufacturerPartNo: [,Validators.required],
    maximumStock: [],
    minimumStock: [],
    model: [],
    plantId: [this.auth.plantId,Validators.required],
    plantIdAndDescription: [],
    referenceField1: [],
    referenceField10:[],
    referenceField2: [],
    referenceField3: [],
    referenceField4: [],
    referenceField5: [],
    referenceField6: [],
    referenceField7: [],
    referenceField8: [],
    referenceField9: [],
    reorderLevel: [],
    replenishmentQty: [],
    safetyStock: [],
    specifications1: [],
    specifications2: [],
    statusId: [],
    storageSectionId: [],
    subItemGroup: [],
    totalStock: [],
    uomId: [,Validators.required],
    updatedBy: [],
    updatedOn: [],
    updatedOnFE:[],
    warehouseId: [,Validators.required],
    warehouseIdAndDescription: [],
  
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
this.form.controls.storageSectionId.patchValue(this.form.controls.storageSectionId.value);
    this.form.controls.updatedBy.disable();
    this.form.controls.createdBy.disable();
    this.form.controls.updatedOnFE.disable();
    this.form.controls.createdOnFE.disable();
 
    this.form.controls.warehouseId.patchValue(this.auth.warehouseId);  
    this.form.controls.languageId.patchValue(this.auth.languageId);
    this.form.controls.plantId.patchValue(this.auth.plantId);
    this.form.controls.companyCodeId.patchValue(this.auth.companyId);
    this.form.controls.languageId.patchValue(this.auth.languageId);
    this.form.controls.warehouseId.disable();
  
      this.dropdownlist();      
    
      
    
  this.form.controls.manufacturerPartNo.disable();
    
    if (this.js.pageflow != 'New') {
      this.form.controls.itemCode.disable();
      this.form.controls.uomId.disable();
      if (this.js.pageflow == 'Display')
        this.form.disable();
       this.fill();
    }
  }
  sub = new Subscription();
  fill(){
    this.spin.show();
    this.sub.add(this.service.Get(this.js.code,this.js.warehouseId,this.js.languageId,this.js.plantId,this.js.companyCodeId,this.js.uomId,this.js.itemCode,this.js.manufacturerPartNo).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });
     this.form.controls.createdOnFE.patchValue(this.cs.dateTimeApi(this.form.controls.createdOn.value));
     this.form.controls.updatedOnFE.patchValue(this.cs.dateTimeApi(this.form.controls.updatedOn.value));
    }))
    this.spin.hide();
  }
  itemtypeList: any[] = [];
  storagesectionList: any[] = [];
  itemgroupList: any[] = [];
  warehouseIdList: any[] = [];
    partnercodeList: any[] = [];
    companyList: any[] = [];
    plantList: any[]=[];
    languageList: any[]=[];
    dropdownlist(){
      this.spin.show();
      this.cas.getalldropdownlist([
        this.cas.dropdownlist.setup.warehouseid.url,
     this.cas.dropdownlist.setup.itemgroupid.url,
        this.cas.dropdownlist.setup.itemtypeid.url,
        this.cas.dropdownlist.setup.storagesectionid.url,
        this.cas.dropdownlist.setup.companyid.url,
        this.cas.dropdownlist.setup.plantid.url,
        this.cas.dropdownlist.setup.languageid.url,
      ]).subscribe((results) => {
     this.warehouseIdList=this.cas.forLanguageFilter(results[0],this.cas.dropdownlist.setup.warehouseid.key);
      this.itemgroupList=this.cas.forLanguageFilter(results[1],this.cas.dropdownlist.setup.itemgroupid.key);
   this.itemtypeList=this.cas.forLanguageFilter(results[2],this.cas.dropdownlist.setup.itemtypeid.key);
   this.storagesectionList=this.cas.forLanguageFilter(results[3],this.cas.dropdownlist.setup.storagesectionid.key);
  this.companyList = this.cas.forLanguageFilter(results[4], this.cas.dropdownlist.setup.companyid.key);
   this.plantList = this.cas.forLanguageFilter(results[5], this.cas.dropdownlist.setup.plantid.key);
   this.languageList=this.cas.foreachlist2(results[6],this.cas.dropdownlist.setup.languageid.key);
   this.masterService.searchitemtype({companyCodeId: this.auth.companyId, languageId: [this.auth.languageId],plantId:this.auth.plantId,warehouseId:this.auth.warehouseId}).subscribe(res => {
    this.itemtypeList = [];
    res.forEach(element => {
      this.itemtypeList.push({value: element.itemTypeId, label: element.itemTypeId + '-' + element.itemType});
    });
  })
    this.form.controls.warehouseId.patchValue(this.auth.warehouseId);  
    this.form.controls.languageId.patchValue(this.auth.languageId);
    this.form.controls.plantId.patchValue(this.auth.plantId);
    this.form.controls.companyCodeId.patchValue(this.auth.companyId);
    this.form.controls.warehouseId.disable();
  this.form.controls.companyCodeId.disable();
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
  
      this.masterService.searchitemtype({companyCodeId: this.form.controls.companyCodeId.value, warehouseId:value.value, plantId:this.form.controls.plantId.value, languageId: [this.form.controls.languageId.value]}).subscribe(res => {
        this.itemtypeList = [];
        res.forEach(element => {
          this.itemtypeList.push({value: element.itemTypeId, label: element.itemTypeId + '-' + element.itemType});
        })
      });
      this.masterService.searchitemgroup({companyCodeId: this.form.controls.companyCodeId.value, plantId: this.form.controls.plantId.value, warehouseId:value.value, languageId: [this.form.controls.languageId.value],itemTypeId:[this.form.controls.itemTypeId.value]}).subscribe(res => {
        this.itemgroupList = [];
        res.forEach(element => {
          this.itemgroupList.push({value: element.itemGroupId, label: element.itemGroupId + '-' + element.itemGroup});
        })
      });
      this.masterService.searchstoragesection({companyCodeId: [this.form.controls.companyCodeId.value] ,plantId: [this.form.controls.plantId.value],warehouseId:[value.value], languageId: [this.form.controls.languageId.value]}).subscribe(res => {
        this.storagesectionList = [];
        res.forEach(element => {
          this.storagesectionList.push({value: element.storageSectionId, label: element.storageSectionId + '-' + element.description});
        })
      });
     
    }
    onItemtypeChange(value){
      this.masterService.searchitemgroup({companyCodeId: this.form.controls.companyCodeId.value, warehouseId:this.form.controls.warehouseId.value, plantId:this.form.controls.plantId.value,languageId: [this.form.controls.languageId.value], itemTypeId: [value.value]}).subscribe(res => {
        this.itemgroupList = [];
        res.forEach(element => {
          this.itemgroupList.push({value: element.itemGroupId, label: element.itemGroupId + '-' + element.itemGroup});
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
    this.sub.add(this.service.Update(this.form.getRawValue(),this.js.code,this.js.warehouseId,this.js.languageId,this.js.plantId,this.js.companyCodeId,this.js.uomId,this.js.itemCode,this.js.manufacturerPartNo).subscribe(res => {
      this.toastr.success(res.itemCode + " updated successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.router.navigate(['/main/masternew/basicdata']);

      this.spin.hide();
  
    }, err => {
  
      this.cs.commonerrorNew(err);
      this.spin.hide();
  
    }));
  }else{
    this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
      this.toastr.success(res.itemCode + " Saved Successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.router.navigate(['/main/masternew/basicdata']);
      this.spin.hide();
  
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
  
    }));
   }
  
  }

}


