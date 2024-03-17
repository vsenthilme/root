import { Component, Inject, OnInit } from '@angular/core';
import { Validators, FormBuilder, FormControl } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { catchError } from "rxjs/operators";
import { PricelistassignmentService } from '../pricelistassignment.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { MasterService } from 'src/app/shared/master.service';

@Component({
  selector: 'app-pricelistassignment-new',
  templateUrl: './pricelistassignment-new.component.html',
  styleUrls: ['./pricelistassignment-new.component.scss']
})
export class PricelistassignmentNewComponent implements OnInit {


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
    private service : PricelistassignmentService,
    private masterService: MasterService
    ) { }



  form = this.fb.group({
    businessPartnerType: [],
    chargeRangeId: [],
    companyCodeId: [],
    companyIdAndDescription: [],
    createdBy: [],
    createdOn: [],
    deletionIndicator: [],
    languageId: [],
    moduleId: [],
    partnerCode: [],
    partnerCodeAndDescription: [],
    plantId: [],
    plantIdAndDescription: [],
    priceListId: [],
    priceListIdAndDescription: [],
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
    serviceTypeId: [],
    statusId: [],
    updatedBy: [],
    updatedOn: [],
    createdOnFE:[],
    updatedOnFE:[],
    warehouseId: [],
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
    
    
        this.form.controls.languageId.patchValue(this.auth.languageId);
        this.form.controls.warehouseId.patchValue(this.auth.warehouseId);
        this.form.controls.companyCodeId.patchValue(this.auth.companyId);
        this.form.controls.plantId.patchValue(this.auth.languageId);
        this.form.controls.plantId.disable();
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
    this.sub.add(this.service.Get(this.js.code,this.js.warehouseId,this.js.languageId,this.js.plantId,this.js.companyCodeId,this.js.partnerCode).subscribe(res => {


      this.masterService.searchbuisnesspartner({ warehouseId: [res.warehouseId], }).subscribe(res1 => {
        this.businesspartnerList = [];
        res1.forEach(element => {
         this.businesspartnerList.push({value: element.partnerCode, label: element.partnerCode});
      })
     });
     this.masterService.searchbuisnesspartner({ warehouseId:[res.warehouseId], partnerCode:[res.partnerCode] }).subscribe(res => {
      this.businesspartner2List = [];
     res.forEach(element => {
       this.businesspartner2List.push({value: element.businessPartnerType, label: element.businessPartnerType});
    })
    });
    
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
    partnercodeList: any[] = [];
    billingmodeList:any[]=[];
    billingfrequencyList:any[]=[];
    priceList:any[]=[];
    dropdownlist(){
      this.spin.show();
      this.cas.getalldropdownlist([
        this.cas.dropdownlist.master.businesspartner.url,
     this.cas.dropdownlist.master.pricelist.url,
     this.cas.dropdownlist.master.businesspartnertype.url,
      
      ]).subscribe((results) => {
        this.priceDropdown = results[1];
        this.priceDropdown.forEach(element => {
          this.priceList.push({value: element.priceListId, label: element.priceListId, companyCodeId: element.companyCodeId, plantId: element.plantId, languageId: element.languageId,warehouseId: element.warehouseId,moduleId: element.moduleId,serviceTypeId:element.serviceTypeId,chargeRangeId:element.chargeRangeId});
        });
  this.businesspartnerList=this.cas.forLanguageFilter(results[0],this.cas.dropdownlist.master.businesspartner.key);
  this.businesspartner2List=this.cas.forLanguageFilter(results[2],this.cas.dropdownlist.master.businesspartnertype.key);

      });
    
      this.spin.hide();
    }
  
     onpricelistChange(value){
      console.log(value);
     this.form.controls.companyCodeId.patchValue(value.companyCodeId);
     this.form.controls.languageId.patchValue(value.languageId);
      this.form.controls.plantId.patchValue(value.plantId);
      this.form.controls.warehouseId.patchValue(value.warehouseId);
      this.form.controls.moduleId.patchValue(value.moduleId);
      this.form.controls.serviceTypeId.patchValue(value.serviceTypeId);
      this.form.controls.chargeRangeId.patchValue(value.chargeRangeId);
     this.masterService.searchbuisnesspartner({ warehouseId:[this.form.controls.warehouseId.value], }).subscribe(res => {
         this.businesspartnerList = [];
        res.forEach(element => {
          this.businesspartnerList.push({value: element.partnerCode, label: element.partnerCode});
       })
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
     onbusinesspartnerChange(value){
      this.masterService.searchbuisnesspartner({ warehouseId:[this.form.controls.warehouseId.value],partnerCode:[value.value] }).subscribe(res => {
        this.businesspartner2List = [];
       res.forEach(element => {
         this.businesspartner2List.push({value: element.businessPartnerType, label: element.businessPartnerType});
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
    this.sub.add(this.service.Update(this.form.getRawValue(),this.js.code,this.js.warehouseId,this.js.languageId,this.js.plantId,this.js.companyCodeId,this.js.partnerCode).subscribe(res => {
      this.toastr.success(res.priceListId + " updated successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.router.navigate(['/main/threePLmaster/pricelistassign']);

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
      this.router.navigate(['/main/threePLmaster/pricelistassign']);
      this.spin.hide();
  
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
  
    }));
   }
  
  }

}

















 

