import { Component, Inject, OnInit } from '@angular/core';
import { Validators, FormBuilder, FormControl } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { forkJoin, of, Subscription } from "rxjs";
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { CbminboundService } from '../cbminbound.service';
import { catchError } from "rxjs/operators";
import { ActivatedRoute, Router } from '@angular/router';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { MasterService } from 'src/app/shared/master.service';
import { ReportsService } from 'src/app/main-module/reports/reports.service';

@Component({
  selector: 'app-cbminbound-new',
  templateUrl: './cbminbound-new.component.html',
  styleUrls: ['./cbminbound-new.component.scss']
})
export class CbminboundNewComponent implements OnInit {

 

  itemcodeDropdown: any;
  isLinear = false;
  constructor(private fb: FormBuilder,
    private auth: AuthService,
    public toastr: ToastrService,
    // private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private route: ActivatedRoute, private router: Router,
    private cs: CommonService,
    private cas: CommonApiService,
    private reportService: ReportsService,
    private service : CbminboundService,
    private masterService: MasterService
    ) { }



  form = this.fb.group({
    cbm: [],
    companyCodeId: [this.auth.companyId,],
    deletionIndicator: [],
    height: [],
    itemCode: [],
    languageId: [this.auth.languageId,],
    length: [],
    packQty: [],
    packType: [],
    plantId: [this.auth.plantId,],
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
    uomId: [],
    warehouseId: [this.auth.warehouseId,],
    width: [],
    updatedBy:[],
    createdBy:[],
    createdOn:[],
   updatedOn:[],
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
  this.dropdownlist();
    
   
     
       
       
       
    if (this.js.pageflow != 'New') {
      this.form.controls.itemCode.disable();
      if (this.js.pageflow == 'Display')
        this.form.disable();
       this.fill();
    }
  }
  sub = new Subscription();
 
  multiselectItemCodeList: any[] = [];
  itemCodeList: any[] = [];

  onItemType(searchKey) {
    let searchVal = searchKey?.filter;
    if (searchVal !== '' && searchVal !== null) {
      forkJoin({
        itemList: this.reportService.getItemCodeDropDown2(searchVal.trim(),this.auth.companyId,this.auth.plantId,this.auth.warehouseId,this.auth.languageId).pipe(catchError(err => of(err))),
      })
        .subscribe(({ itemList }) => {
          if (itemList != null && itemList.length > 0) {
            this.multiselectItemCodeList = [];
            this.itemCodeList = itemList;
            this.itemCodeList.forEach(x => this.multiselectItemCodeList.push({ value: x.itemCode, label: x.itemCode + ' - ' + x.description }))
          }
        });
    }
  }
  fill(){
    this.spin.show();
    this.sub.add(this.service.Get(this.js.code,this.js.warehouseId,this.js.languageId,this.js.plantId,this.js.companyCodeId).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });
     this.form.controls.createdOnFE.patchValue(this.cs.dateTimeApi(this.form.controls.createdOn.value));
     this.form.controls.updatedOnFE.patchValue(this.cs.dateTimeApi(this.form.controls.updatedOn.value));
    }))
    this.spin.hide();
  }
  uomidList: any[] = [];
  itemcodeList:any[]=[];
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
        this.cas.dropdownlist.master.imbasicdata1.url,
     this.cas.dropdownlist.setup.uomid.url,
     //this.cas.dropdownlist.master.businesspartnertype.url,
      
      ]).subscribe((results) => {
      this.multiselectItemCodeList=this.cas.forLanguageFilter(results[0],this.cas.dropdownlist.master.imbasicdata1.key);
  this.uomidList=this.cas.forLanguageFilter(results[1],this.cas.dropdownlist.setup.uomid.key);

    this.form.controls.warehouseId.disable();
  
      });
    
      this.spin.hide();
    }
  
     onitemcodeChange(value){
  
      this.masterService.searchuom({ warehouseId:this.auth.warehouseId,companyCodeId:this.auth.companyId ,languageId:[this.auth.languageId],plantId:this.auth.plantId}).subscribe(res => {
        this.uomidList = [];
       res.forEach(element => {
         this.uomidList.push({value: element.uomId, label: element.uomId});
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
    this.sub.add(this.service.Update(this.form.getRawValue(),this.js.code,this.js.warehouseId,this.js.languageId,this.js.plantId,this.js.companyCodeId).subscribe(res => {
      this.toastr.success(res.itemCode + " updated successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.router.navigate(['/main/threePLmaster/cbm']);

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
      this.router.navigate(['/main/threePLmaster/cbm']);
      this.spin.hide();
  
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
  
    }));
   }
  
  }

}

















 

