import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { ItemtypeService } from '../../itemtype/itemtype.service';
import { ItemgroupService } from '../itemgroup.service';
import { MasterService } from 'src/app/shared/master.service';

@Component({
  selector: 'app-itemgroup-new',
  templateUrl: './itemgroup-new.component.html',
  styleUrls: ['./itemgroup-new.component.scss']
})
export class ItemgroupNewComponent implements OnInit {

  warehouseidList: any[] = [];
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
    private service : ItemgroupService,
    private masterService: MasterService,) { }



  form = this.fb.group({
    companyId: [this.auth.companyId],
    createdBy: [],
    createdOn: [],
    deletionIndicator: [],
    itemGroupId: [, Validators.required],
    itemTypeId: [, Validators.required],
    languageId: [this.auth.languageId,Validators.required ],
    plantId: [],
    storageClassId: [, ],
    storageSectionId: [],
    subItemGroupId:[,Validators.required],
    updatedBy: [],
    updatedOn: [],
    updatedOnFE:[],
    createdOnFE:[],
    warehouseId: [, Validators.required],
  
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
    this.form.controls.updatedOnFE.disable();
    this.form.controls.createdBy.disable();
    this.form.controls.createdOnFE.disable();
  
      this.dropdownlist();
    
   
    if (this.js.pageflow != 'New') {
      this.form.controls.plantId.disable();
      this.form.controls.warehouseId.disable();
      this.form.controls.itemTypeId.disable();
      this.form.controls.itemGroupId.disable();
      this.form.controls.subItemGroupId.disable();
     if (this.js.pageflow == 'Display')
        this.form.disable();
       this.fill();
      
    }
  }
  sub = new Subscription();
  fill(){
    this.spin.show();
    this.sub.add(this.service.Get(this.js.code,this.js.warehouseId,this.js.subItemGroupId,this.js.itemTypeId,this.js.companyId,this.js.languageId,this.js.plantId).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });
      this.form.controls.createdOnFE.patchValue(this.cs.dateTimeApi(this.form.controls.createdOn.value));
      this.form.controls.updatedOnFE.patchValue(this.cs.dateTimeApi(this.form.controls.updatedOn.value));
    }))
    this.spin.hide();
  }
 
  currencyList: any[] = [];
  itemtypeList: any[] = [];
  stateList: any[] = [];
  cityList: any[] = [];
  countryList: any[] = [];
  languageidList: any[] = [];
  companyList: any[] = [];
    plantList: any[] = [];
  itemtypedesList:any[]=[];
  subitemgroupList: any[] = [];
  storagesectionList: any[] = [];
  itemgroupList: any[] = [];
  storageclassList: any[] = [];
  
  plantidList:any[]=[];
  dropdownlist(){
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.warehouseid.url,
      this.cas.dropdownlist.setup.itemtypeid.url,
      this.cas.dropdownlist.setup.itemgroupid.url,
      this.cas.dropdownlist.setup.storagesectionid.url,
      this.cas.dropdownlist.setup.storageclassid.url,
      this.cas.dropdownlist.setup.subitemgroupid.url,
      this.cas.dropdownlist.setup.companyid.url,
      this.cas.dropdownlist.setup.plantid.url,
      this.cas.dropdownlist.setup.languageid.url,
    ]).subscribe((results) => {
  this.warehouseidList=this.cas.forLanguageFilter(results[0],this.cas.dropdownlist.setup.warehouseid.key);
   this.itemgroupList=this.cas.forLanguageFilter(results[2],this.cas.dropdownlist.setup.itemgroupid.key);
 this.itemtypeList=this.cas.forLanguageFilter(results[1],this.cas.dropdownlist.setup.itemtypeid.key);
 this.storagesectionList=this.cas.forLanguageFilter(results[3],this.cas.dropdownlist.setup.storagesectionid.key);
 this.storageclassList=this.cas.forLanguageFilter(results[4],this.cas.dropdownlist.setup.storageclassid.key);
 this.subitemgroupList=this.cas.forLanguageFilter(results[5],this.cas.dropdownlist.setup.subitemgroupid.key);
 this.companyList = this.cas.forLanguageFilter(results[6], this.cas.dropdownlist.setup.companyid.key);
 this.plantList = this.cas.forLanguageFilter(results[7], this.cas.dropdownlist.setup.plantid.key);
 this.languageidList = this.cas.foreachlist2(results[8], this.cas.dropdownlist.setup.languageid.key);
  
  this.form.controls.languageId.patchValue(this.auth.languageId);
  this.form.controls.companyId.patchValue(this.auth.companyId);
  //this.itemtypeList=this.cas.forLanguageFilter(results[1],this.cas.dropdownlist.setup.itemtypeid.key)
  this.masterService.searchPlantenter({companyId: this.auth.companyId, languageId: this.auth.languageId}).subscribe(res => {
   this.plantList = [];
   res.forEach(element => {
     this.plantList.push({value: element.plantId, label: element.plantId + '-' + element.description});
   });
 });
//  this.masterService.searchWarehouse({companyCodeId: this.auth.companyId, languageId: [this.auth.languageId],plantId:this.form.controls.plantId.value}).subscribe(res => {
//    this.warehouseidList = [];
//    res.forEach(element => {
//      this.warehouseidList.push({value: element.warehouseId, label: element.warehouseId + '-' + element.warehouseDesc});
//    });
//  });
  this.form.controls.companyId.disable();
this.form.controls.languageId.disable();


    });
  
    this.spin.hide();
  }
 
  onplantchange(value){

    this.masterService.searchWarehouseenter({companyId: this.auth.companyId, languageId:this.auth.languageId,plantId:this.form.controls.plantId.value}).subscribe(res => {
     this.warehouseidList = [];
     res.forEach(element => {
       this.warehouseidList.push({value: element.warehouseId, label: element.warehouseId + '-' + element.description});
     });
   });
  }
   onWarehouseChange(value){
   

    this.masterService.searchitemtypeenter({companyId: this.auth.companyId, warehouseId:value.value, plantId:this.form.controls.plantId.value, 
      languageId: this.auth.languageId}).subscribe(res=>{  this.itemtypeList = [];
        res.forEach(element => {
          this.itemtypeList.push({value: element.itemTypeId, label: element.itemTypeId + '-' + element.description});
        })
      });
    this.masterService.searchitemgroup({companyCodeId: this.form.controls.companyId.value, plantId: this.form.controls.plantId.value, warehouseId:value.value, languageId: [this.form.controls.languageId.value],itemTypeId:[this.form.controls.itemTypeId.value]}).subscribe(res => {
      this.itemgroupList = [];
      res.forEach(element => {
        this.itemgroupList.push({value: element.itemGroupId, label: element.itemGroupId + '-' + element.itemGroup});
      })
    });
    this.masterService.searchstoragesection({companyCodeId: [this.form.controls.companyId.value] ,plantId: [this.form.controls.plantId.value],warehouseId:[value.value], languageId: [this.form.controls.languageId.value]}).subscribe(res => {
      this.storagesectionList = [];
      res.forEach(element => {
        this.storagesectionList.push({value: element.storageSectionId, label: element.storageSectionId + '-' + element.description});
      })
    });
    this.masterService.searchstorageclass({companyCodeId:this.form.controls.companyId.value ,plantId:this.form.controls.plantId.value,warehouseId:value.value, languageId:[this.form.controls.languageId.value],}).subscribe(res => {
      this.storageclassList = [];
      res.forEach(element => {
        this.storageclassList.push({value: element.storageClassId, label: element.storageClassId + '-' + element.description});
      })
    });
  }
  onItemtypeChange(value){
    this.masterService.searchitemgroup({companyCodeId: this.form.controls.companyId.value, warehouseId:this.form.controls.warehouseId.value, plantId:this.form.controls.plantId.value,languageId: [this.form.controls.languageId.value], itemTypeId: [value.value]}).subscribe(res => {
      this.itemgroupList = [];
      res.forEach(element => {
        this.itemgroupList.push({value: element.itemGroupId, label: element.itemGroupId + '-' + element.itemGroup});
     })
    });
  }
   
   onItemgroupChange(value){
    this.masterService.searchsubitemgroup({companyCodeId: this.form.controls.companyId.value, warehouseId:this.form.controls.warehouseId.value, plantId:this.form.controls.plantId.value, 
      languageId: [this.form.controls.languageId.value], itemTypeId: [this.form.controls.itemTypeId.value],itemGroupId:[value.value]}).subscribe(res => {
       this.subitemgroupList = [];
       res.forEach(element => {
         this.subitemgroupList.push({value: element.subItemGroupId, label: element.subItemGroupId + '-' + element.subItemGroup});
      })
     });
     this.masterService.searchitemgroup({companyCodeId: this.form.controls.companyId.value, warehouseId:this.form.controls.warehouseId.value, plantId:this.form.controls.plantId.value, 
      languageId: [this.form.controls.languageId.value], itemTypeId: [this.form.controls.itemTypeId.value],itemGroup:[value.value]}).subscribe(res => {
        this.form.controls.description.patchValue(res[0].itemGroup);
      
    });
    this.masterService.searchitemgroup({companyCodeId: this.form.controls.companyId.value, warehouseId:this.form.controls.warehouseId.value, plantId:this.form.controls.plantId.value, 
      languageId: [this.form.controls.languageId.value], itemTypeId: [this.form.controls.itemTypeId.value],itemGroup:[value.value]}).subscribe(res => {
        this.form.controls.description.patchValue(res[0].itemGroup);
      
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
    this.sub.add(this.service.Update(this.form.getRawValue(),this.js.code,this.js.warehouseId,this.js.subItemGroupId,this.js.itemTypeId,this.js.companyId,this.js.languageId,this.js.plantId).subscribe(res => {
      this.toastr.success(res.itemGroupId + " updated successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.router.navigate(['/main/productsetup/itemgroup']);

      this.spin.hide();
  
    }, err => {
  
      this.cs.commonerrorNew(err);
      this.spin.hide();
  
    }));
  }else{
    this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
      this.toastr.success(res.itemGroupId + " Saved Successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.router.navigate(['/main/productsetup/itemgroup']);
      this.spin.hide();
  
    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
  
    }));
   }
  
  }

}

