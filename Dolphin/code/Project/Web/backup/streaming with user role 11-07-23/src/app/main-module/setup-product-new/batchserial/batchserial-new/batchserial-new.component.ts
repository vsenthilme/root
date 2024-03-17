import { Component, OnInit, ViewChild } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Table } from 'primeng/table';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { BatchserialService } from '../batchserial.service';
import { MasterService } from 'src/app/shared/master.service';

@Component({
  selector: 'app-batchserial-new',
  templateUrl: './batchserial-new.component.html',
  styleUrls: ['./batchserial-new.component.scss']
})
export class BatchserialNewComponent implements OnInit {
  storageMethodList: any[] = [];
  maintainanceList: any[] = [];
  warehouseidList: any[] = [];
  warehouseidDropdown: any;
  advanceFilterShow: boolean;
  @ViewChild('Setupbatch') Setupbatch: Table | undefined;
  batch: any;
  selectedbatch : any;
  isLinear = false;
  constructor(private fb: FormBuilder,
    private auth: AuthService,
    public toastr: ToastrService,
    // private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private route: ActivatedRoute, private router: Router,
    private cs: CommonService,
    private cas: CommonApiService,
    private service : BatchserialService,
    private masterService: MasterService) { 
      this.storageMethodList = [{'storageMethodId': 'Batch', 'description': 'Batch'},{'storageMethodId': 'Serial', 'description': 'Serial'},{'storageMethodId': 'Not Applicable', 'description': 'Not Applicable'}];
      this.maintainanceList = [{'maintainanceId': 'Internal', 'description': 'Internal'},{'maintainanceId': 'External', 'description': 'External'}]
    }




  public levelReferences: FormGroup;
  form = this.fb.group({
    companyId: [],
    companyIdAndDescription: [],
    deletionIndicator: [],
    description: [],
    languageId: [],
    levelId: [,Validators.required],
    levelIdAndDescription: [],
    maintenance: [,Validators.required],
    plantId: [],
    plantIdAndDescription: [],
    storageMethod: [,Validators.required],
    warehouseId: [,Validators.required],
    warehouseIdAndDescription: [],
    updatedBy: [],
    updatedOn: [],
    updatedOnFE: [],
    createdOn: [],
    createdOnFE: [],
    createdBy: [],
  });
  
  get formArr() {
    return this.levelReferences.get('Rows') as FormArray;
  }

  initRows() {
    return this.fb.group({
    levelReference:  []
    });
  }

  addNewRow() {
    this.formArr.push(this.initRows());
  }
  removerow(x: any){
    this.formArr.removeAt(x);
  }

  
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

    this.levelReferences = this.fb.group({
      Rows: this.fb.array([this.initRows()]),
    });

    let code = this.route.snapshot.params.code;
    this.js = this.cs.decrypt(code);

    this.form.controls.updatedBy.disable();
    this.form.controls.createdBy.disable();
    this.form.controls.updatedOn.disable();
    this.form.controls.createdOn.disable();
    if(this.auth.userTypeId != 1){
      this.form.controls.companyId.patchValue(this.auth.companyId);
      this.dropdownlist();
    
      
    }
    if(this.auth.userTypeId == 1){
      this.superadmindropdownlist();
      }
    if (this.js.pageflow != 'New') {
   
     // if (this.js.pageflow == 'Display')
        this.form.disable();
       this.fill();
    }
  }
  sub = new Subscription();
  fill(){
    this.spin.show();
    this.sub.add(this.service.Get(this.js.code,this.js.languageId,this.js.plantId,this.js.warehouseId,this.js.companyId,this.js.levelId).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });
      this.levelReferences.get('Rows')?.patchValue(res.levelReferences);
     this.form.controls.createdOnFE.patchValue(this.cs.dateTimeApi(this.form.controls.createdOn.value));
     this.form.controls.updatedOnFE.patchValue(this.cs.dateTimeApi(this.form.controls.updatedOn.value));
    }))
  }
 
  levelList: any[] = [];
  stateList: any[] = [];
  cityList: any[] = [];
  countryList: any[] = [];
  dropdownlist(){
    this.spin.show();
    this.cas.getalldropdownlist([ 
      this.cas.dropdownlist.setup.warehouseid.url,
      this.cas.dropdownlist.setup.levelid.url,
      
    ]).subscribe((results) => {
  this.warehouseidList=this.cas.forLanguageFilter(results[0],this.cas.dropdownlist.setup.warehouseid.key);
   this.levelList=this.cas.forLanguageFilter(results[1],this.cas.dropdownlist.setup.levelid.key);

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
   this.cas.dropdownlist.setup.levelid.url,
   
    ]).subscribe((results) => {
    this.warehouseidDropdown = results[0];
    this.warehouseidDropdown.forEach(element => {
      this.warehouseidList.push({value: element.warehouseId, label: element.warehouseId + '-' + element.warehouseDesc, companyCodeId: element.companyCodeId, plantId: element.plantId, languageId: element.languageId});
    });
    this.levelList=this.cas.foreachlist2(results[1],this.cas.dropdownlist.setup.levelid.key);
    this.form.controls.warehouseId.patchValue(this.form.controls.warehouseId.value);

    
    this.spin.hide();
  }, (err) => {
    this.toastr.error(err, "");
    this.spin.hide();
  });
   }
  onWarehouseChange(value){
    this.form.controls.companyId.patchValue(value.companyCodeId);
    this.form.controls.languageId.patchValue(value.languageId);
    this.form.controls.plantId.patchValue(value.plantId);
    this.masterService.searchlevel({companyCodeId: this.form.controls.companyId.value, warehouseId:value.value, plantId:this.form.controls.plantId.value, languageId: [this.form.controls.languageId.value]}).subscribe(res => {
      this.levelList = [];
      res.forEach(element => {
        this.levelList.push({value: element.levelId, label: element.levelId + '-' + element.level});
      })
    });
  }
  submit() {
    let obj: any ={};
    let levelReferences1: any[] = [];
    obj = this.form.getRawValue();
    levelReferences1 = (this.levelReferences.getRawValue().Rows);
    obj.levelReferences = levelReferences1;
    console.log(obj);
    console.log(this.levelReferences.getRawValue().Rows);
    console.log(this.form.getRawValue());
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
    this.sub.add(this.service.Update(obj, this.js.code,this.js.languageId,this.js.plantId,this.js.warehouseId,this.js.companyId,this.js.levelId).subscribe(res => {
      this.toastr.success(res.storageMethod + " updated successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.router.navigate(['/main/productsetup/batch']);

      this.spin.hide();
  
    }, err => {
  
      this.cs.commonerrorNew(err);
      this.spin.hide();
  
    }));
  }else{
    this.sub.add(this.service.Create(obj).subscribe(res => {
      this.toastr.success(res.itemGroupId + " Saved Successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.router.navigate(['/main/productsetup/batch']);
      this.spin.hide();
  
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
  
    }));
   }
  
  }
  onChange() {
    console.log(this.selectedbatch.length)
    const choosen= this.selectedbatch[this.selectedbatch.length - 1];   
    this.selectedbatch.length = 0;
    this.selectedbatch.push(choosen);
  } 
}
