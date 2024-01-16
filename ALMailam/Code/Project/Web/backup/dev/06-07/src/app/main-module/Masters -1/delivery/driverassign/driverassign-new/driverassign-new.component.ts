import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { MasterService } from 'src/app/shared/master.service';
import { DriverassignService } from '../driverassign.service';

@Component({
  selector: 'app-driverassign-new',
  templateUrl: './driverassign-new.component.html',
  styleUrls: ['./driverassign-new.component.scss']
})
export class DriverassignNewComponent implements OnInit {

  driverDropdown: any;
  isLinear = false;
  constructor(private fb: FormBuilder,
    private auth: AuthService,
    public toastr: ToastrService,
    // private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private route: ActivatedRoute, private router: Router,
    private cs: CommonService,
    private cas: CommonApiService,
    private service : DriverassignService,
    private masterService: MasterService
    ) { }



  form = this.fb.group({
    companyCodeId: [],
    createdBy: [],
    createdOn: [],
    deletionIndicator: [],
    driverId: [,Validators.required],
    driverName: [],
    languageId: [],
    plantId: [],
    referenceField1:[],
    referenceField10:[],
    referenceField2:[],
    referenceField3:[],
    referenceField4:[],
    referenceField5:[],
    referenceField6:[],
    referenceField7:[],
    referenceField8:[],
    referenceField9:[],
    routeId:[,Validators.required],
    routeName: [],
    statusId: [],
    updatedBy: [],
    updatedOn: [],
    vehicleNumber: [,Validators.required],
    warehouseId: [],
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
  priceDropdown:any;
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
      this.form.controls.warehouseId.patchValue(this.auth.warehouseId);
     
        this.form.controls.languageId.patchValue(this.auth.languageId);
        this.form.controls.warehouseId.patchValue(this.auth.warehouseId);
        this.form.controls.companyCodeId.patchValue(this.auth.companyId);
        this.form.controls.plantId.patchValue(this.auth.languageId);
        this.form.controls.plantId.disable();
        this.dropdownlist();
  
     
       
       
       
    if (this.js.pageflow != 'New') {
      this.form.controls.driverId.disable();
      if (this.js.pageflow == 'Display')
        this.form.disable();
       this.fill();
    }
  }
  sub = new Subscription();
 

  fill(){
    this.spin.show();
    this.sub.add(this.service.Get(this.js.code,this.js.warehouseId,this.js.languageId,this.js.plantId,this.js.companyCodeId,this.js.vehicleNumber,this.js.routeId).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });
     this.form.controls.createdOnFE.patchValue(this.cs.dateTimeApi(this.form.controls.createdOn.value));
     this.form.controls.updatedOnFE.patchValue(this.cs.dateTimeApi(this.form.controls.updatedOn.value));
    }))
    this.spin.hide();
  }
  floorList: any[] = [];
  moduleList:any[]=[];
  businesspartnerList: any[] = [];
  businesspartner2List: any[] = [];
  warehouseIdList: any[] = [];
  routeList: any[] = [];
    billingmodeList:any[]=[];
    vechileList:any[]=[];
    driverList:any[]=[];
    dropdownlist(){
      this.spin.show();
      this.cas.getalldropdownlist([
        this.cas.dropdownlist.master.driver.url,
     this.cas.dropdownlist.master.vehicle.url,
     this.cas.dropdownlist.master.route.url,
      
      ]).subscribe((results) => {
     this.driverList=this.cas.forLanguageFilter(results[0],this.cas.dropdownlist.master.driver.key);
  this.vechileList=this.cas.forLanguageFilter(results[1],this.cas.dropdownlist.master.vehicle.key);
  this.routeList=this.cas.forLanguageFilter(results[2],this.cas.dropdownlist.master.route.key);

// this.priceList=this.cas.foreachlist2(results[1],this.cas.dropdownlist.master.pricelist.key);

    this.form.controls.warehouseId.patchValue(this.auth.warehouseId);  
    this.form.controls.languageId.patchValue(this.auth.languageId);
    this.form.controls.plantId.patchValue(this.auth.plantId);
    this.form.controls.companyCodeId.patchValue(this.auth.companyId);
    this.form.controls.warehouseId.disable();
  
      });
    
      this.spin.hide();
    }
  
  
     ondriverChange(value){
     this.form.controls.companyCodeId.patchValue(value.companyCodeId);
     this.form.controls.languageId.patchValue(value.languageId);
      this.form.controls.plantId.patchValue(value.plantId);
      this.form.controls.warehouseId.patchValue(value.warehouseId);
      if(this.auth.userTypeId != 1){
        this.form.controls.warehouseId.patchValue(this.auth.warehouseId);  
        this.form.controls.languageId.patchValue(this.auth.languageId);
        this.form.controls.plantId.patchValue(this.auth.plantId);
        this.form.controls.companyCodeId.patchValue(this.auth.companyId);
       
      }
     this.masterService.searchvehicle({ warehouseId:[this.form.controls.warehouseId.value],plantId:[this.form.controls.plantId.value],companyCodeId:[this.form.controls.companyCodeId.value],languageId:[this.form.controls.languageId.value],driverId:[this.form.controls.driverId.value] }).subscribe(res => {
         this.vechileList = [];
        res.forEach(element => {
          this.vechileList.push({value: element.vehicleNumber, label: element.vehicleNumber});
       })
      });
      this.masterService.searchroute({ warehouseId:[this.form.controls.warehouseId.value],plantId:[this.form.controls.plantId.value],companyCodeId:[this.form.controls.companyCodeId.value],languageId:[this.form.controls.languageId.value] ,driverId:[this.form.controls.driverId.value] }).subscribe(res => {
        this.routeList = [];
       res.forEach(element => {
         this.routeList.push({value: element.routeId, label: element.routeId});
      })
     });
     this.masterService.searchdriver({warehouseId:[this.form.controls.warehouseId.value],plantId:[this.form.controls.plantId.value],companyCodeId:[this.form.controls.companyCodeId.value],languageId:[this.form.controls.languageId.value], driverId: [this.form.controls.driverId.value]}).subscribe(res => {
      this.form.controls.driverName.patchValue(res[0].driverName);
  });
      
    //   this.masterService.searchstoragesection({companyCodeId: [this.form.controls.companyCodeId.value], plantId: [this.form.controls.plantId.value], warehouseId:[value.value], languageId: [this.form.controls.languageId.value],floorId:[this.form.controls.floorId.value]}).subscribe(res => {
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
    this.sub.add(this.service.Update(this.form.getRawValue(),this.js.code,this.js.warehouseId,this.js.languageId,this.js.plantId,this.js.companyCodeId,this.js.vehicleNumber,this.js.routeId).subscribe(res => {
      this.toastr.success(res.driverId + " updated successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.router.navigate(['/main/delivery/drivervehicleassign']);

      this.spin.hide();
  
    }, err => {
  
      this.cs.commonerrorNew(err);
      this.spin.hide();
  
    }));
  }else{
    this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
      this.toastr.success(res.driverId + " Saved Successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.router.navigate(['/main/delivery/drivervehicleassign']);
      this.spin.hide();
  
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
  
    }));
   }
  
  }

}

















 
