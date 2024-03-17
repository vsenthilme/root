import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { PartnerService } from '../../partner/partner.service';
import { AltuomService } from '../altuom.service';


@Component({
  selector: 'app-altuom-new',
  templateUrl: './altuom-new.component.html',
  styleUrls: ['./altuom-new.component.scss']
})
export class AltuomNewComponent implements OnInit {
  warehouseidDropdown: any;
  isLinear = false;
  batch: any;
  selectedbatch : any;
  warehouseId: any;
  languageId: any;
  plantId: any;
  companyCodeId: any;
  itemCode: any;


  constructor(private fb: FormBuilder,
    private auth: AuthService,
    public toastr: ToastrService,
    // private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private route: ActivatedRoute, private router: Router,
    private cs: CommonService,
    private cas: CommonApiService,
    private service : AltuomService) {  }


    form: FormGroup;  
 

 private createTableRow(): FormGroup {  
    return this.fb.group({  
      alternateUom: new FormControl(),
      alternateUomBarcode: new FormControl(),
      companyCodeId: new FormControl(this.companyCodeId, ),
      deletionIndicator: new FormControl(),
      fromUnit: new FormControl(),
      itemCode: new FormControl(),
      languageId: new FormControl(this.languageId, ),
      plantId: new FormControl(this.plantId, ),
      qpc20Ft: new FormControl(),
      qpc40Ft: new FormControl(),
      referenceField1: new FormControl(),
      referenceField10: new FormControl(),
      referenceField2: new FormControl(),
      referenceField3: new FormControl(),
      referenceField4: new FormControl(),
      referenceField5: new FormControl(),
      referenceField6: new FormControl(),
      referenceField7: new FormControl(),
      referenceField8: new FormControl(),
      referenceField9: new FormControl(),
      slNo: new FormControl(),
      statusId: new FormControl(),
      toUnit: new FormControl(),
      uomId: new FormControl(),
      warehouseId: new FormControl(this.warehouseId,)
    });  
}

get tableRowArray(): FormArray {  
  return this.form.get('tableRowArray') as FormArray;  
}

onDeleteRow(rowIndex:number): void {  
  this.tableRowArray.removeAt(rowIndex);  
} 

addNewRow(): void {  
   this.tableRowArray.push(this.createTableRow());  
}  

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


  js: any = {}

  createForm(): void {  
    this.form = this.fb.group({  
        tableRowArray: this.fb.array([  
            this.createTableRow()  
        ])  
    })  
} 


  ngOnInit(): void {
    let code = this.route.snapshot.params.code;
    this.js = this.cs.decrypt(code);
    if(this.auth.userTypeId != 1){
      this.warehouseId = (this.auth.warehouseId);  
      this.languageId = (this.auth.languageId);
      this.plantId = (this.auth.plantId);
      this.companyCodeId = (this.auth.companyId);
      this.dropdownlist();
      }
      if(this.auth.userTypeId == 1){
       
        this.superadmindropdownlist();
        }
        this.createForm();

    if (this.js.pageflow != 'New') {
      if (this.js.pageflow == 'Display')
        this.form.disable();
       this.fill();
    }
  }



  superadmindropdownlist(){
      
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.warehouseid.url,
   
     
    ]).subscribe((results) => {
    this.warehouseidDropdown = results[0];
    this.warehouseidDropdown.forEach(element => {
      this.warehouseIdList.push({value: element.warehouseId, label: element.warehouseId + '-' + element.warehouseDesc, companyCodeId: element.companyCodeId, plantId: element.plantId, languageId: element.languageId, warehouseId: element.warehouseId});
    });
  
  
    });
    this.spin.hide();
  }
  sub = new Subscription();
   fill(){
   this.spin.show();
   this.sub.add(this.service.Get(this.js.code,this.js.warehouseId,this.js.languageId,this.js.plantId,this.js.companyCodeId,this.js.itemCode,this.js.alternateUom).subscribe(res => {
    this.warehouseSelection = res[0].warehouseId;
    this.tableRowArray.patchValue(res); 
    }))
     this.spin.hide();
  }
  warehouseIdList: any[] = [];
    partnercodeList: any[] = [];
    warehouseSelection: any;
    dropdownlist(){
      this.spin.show();
      this.cas.getalldropdownlist([
        this.cas.dropdownlist.setup.warehouseid.url,  
      ]).subscribe((results) => {
      this.warehouseIdList = this.cas.foreachlist2(results[0], this.cas.dropdownlist.setup.warehouseid.key);
      if(this.auth.userTypeId != 1){
        this.warehouseId = (this.auth.warehouseId);  
        this.languageId = (this.auth.languageId);
        this.plantId = (this.auth.plantId);
        this.companyCodeId = (this.auth.companyId);
      }
      
      this.spin.hide();
      }, (err) => {
        this.toastr.error(err, "");
        this.spin.hide();
      });
    }


    onWarehouseChange(value){
      this.companyCodeId = (value.companyCodeId);
      this.languageId = (value.languageId);
      this.plantId = (value.plantId);
      this.warehouseId = (value.warehouseId);
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
       
      this.form.controls.tableRowArray.value.forEach(element => {
        element.warehouseId = this.warehouseId;
        element.languageId = this.languageId;
        element.companyCodeId = this.companyCodeId;
        element.plantId = this.plantId;
        element.itemCode = this.itemCode;
      })

       this.cs.notifyOther(false);
       this.spin.show();
      //  this.form.controls.createdOnFE.patchValue(this.cs.dateTimeApi(this.form.controls.createdOn.value));
      // this.form.controls.updatedOnFE.patchValue(this.cs.dateTimeApi(this.form.controls.updatedOn.value));
        if (this.js.code) {
        this.sub.add(this.service.Update(this.form.controls.tableRowArray.value,this.js.code,this.js.warehouseId,this.js.languageId,this.js.plantId,this.js.companyCodeId,this.js.itemCode,this.js.uomId).subscribe(res => {
         this.toastr.success(res.alternateUom + " updated successfully!","Notification",{
           timeOut: 2000,
          progressBar: false,
         });
          this.router.navigate(['/main/masternew/altuom']);
   
          this.spin.hide();
     
        }, err => {
     
          this.cs.commonerrorNew(err);
          this.spin.hide();
     
        }));
      }else{
        this.sub.add(this.service.Create(this.form.controls.tableRowArray.value).subscribe(res => {
          this.toastr.success(res.alternateUom + " Saved Successfully!","Notification",{
            timeOut: 2000,
            progressBar: false,
          });
         this.router.navigate(['/main/masternew/altuom']);
          this.spin.hide();
     
        }, err => {
          this.cs.commonerrorNew(err);
         this.spin.hide();
     
        }));
       }
     
      }
}

