import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { FormGroup, FormArray, FormControl, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { MessageService } from 'primeng/api';
import { Table } from 'primeng/table';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { ProfileNewComponent } from 'src/app/main-module/customers/customer-profile/profile-new/profile-new.component';
import { DashboardService } from 'src/app/main-module/dashboard/dashboard.service';
import { ConsignmentNewService } from '../consignment-new.service';

export interface  variant1 {
  code:  string;
  employeeName:  string;
  userName:  string;
  password:  string;
  userProfile:  string;
  accessType:  string;
  accessType1:  string;
  accessType2:  string;
} 
const ELEMENT_DATA1:  variant1[] = [
  {code:  '11020030',employeeName:  'Test',userName:  '2',password: '3',userProfile: '3',accessType: '5',
  accessType1: '5',accessType2: '3'}

];
@Component({
  selector: 'app-consignment-new-popup',
  templateUrl: './consignment-new-popup.component.html',
  styleUrls: ['./consignment-new-popup.component.scss']
})
export class ConsignmentNewPopupComponent implements OnInit {
  @ViewChild('consignmentNew') consignmentNew: Table | undefined;

  
  consignmentNew1: any;
  selectedconsignmentNew1 : variant1[];
  constructor(
    // public dialogRef: MatDialogRef < ProfileNewComponent > ,
    // @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private auth: AuthService,
    private fb: FormBuilder,
    public cs: CommonService,
    public dialog: MatDialog,
    private router: Router, 
    private service:ConsignmentNewService ,
    private messageService: MessageService,
    private route: ActivatedRoute,
    private customerService: DashboardService,) {
    
    }
 sub = new Subscription();
    submitted = false;
 // form: FormGroup;
  js: any = {}
  form = new FormGroup({
    type: new FormControl(''),
    languageId: new FormControl('EN',),
    companyId: new FormControl('1000',),
    orderId: new FormControl(''),
    referenceNo: new FormControl(''),
    shipsyOrderNo: new FormControl(''),
    customerId: new FormControl(''),
    loadType: new FormControl(''),
    typeOfDelivery: new FormControl(''),
    deliveryCharge: new FormControl(''),
    originDetailsName: new FormControl(''),
    originDetailsPhone: new FormControl(''),
    originDetailsAddressLine1: new FormControl(''),
    originDetailsAddressLine2: new FormControl(''),
    originDetailsPincode: new FormControl(''),
    destinationDetailsName: new FormControl(''),
    destinationDetailsPhone: new FormControl(''),
    destinationDetailsAddressLine1: new FormControl(''),
    destinationDetailsAddressLine2: new FormControl(''),
    destinationDetailsPincode: new FormControl(''),
    serviceTypeId: new FormControl(''),
    loyaltyPoint: new FormControl(''),
    loyaltyAmount: new FormControl(''),
    originCity: new FormControl(''),
    originState: new FormControl(''),
    originCountry: new FormControl(''),
    destinationCity: new FormControl(''),
    destinationState: new FormControl(''),
    destinationCountry: new FormControl(''),
    status: new FormControl('New',),
    deletionIndicator: new FormControl(''),
    referenceField1: new FormControl(''),
    referenceField2: new FormControl(''),
    referenceField3: new FormControl(''),
    referenceField4: new FormControl(''),
    referenceField5: new FormControl(''),
    referenceField6: new FormControl(''),
    referenceField7: new FormControl(''),
    referenceField8: new FormControl(''),
    referenceField9: new FormControl(''),
    referenceField10: new FormControl(''),
    createdBy: new FormControl(''),
    createdOn: new FormControl(''),
    updatedOn: new FormControl(''),
    updatedBy: new FormControl(''),
    orderDetailsLines: new FormArray([
      new FormGroup({
      commodityName:  new FormControl(''),
      noOfPieces:  new FormControl(''),
      declaredValue:  new FormControl(''),
      dimensionUnit:  new FormControl(''),
      length:  new FormControl(''),
      width:  new FormControl(''),
      height:  new FormControl(''),
      weightUnit:  new FormControl(''),
      weight:  new FormControl(''),
      }),
    ]),
  });
  ngOnInit(): void {
    this.consignmentNew1= (ELEMENT_DATA1);

   // this.removeContactField(0);
    
    let code = this.route.snapshot.params.code;
    this.js = this.cs.decrypt(code);
 

    if (this.js.pageflow != 'New') {
      if (this.js.pageflow == 'Display')
        this.form.disable();
      this.fill(this.js.code);
    }
    this.customerDetails()
  }
  multiCustomerList: any[] = [];
  customerDetails(){
    this.sub.add(this.customerService.search({}).subscribe(res => {
      res.forEach(element => {
        this.multiCustomerList.push({ value: element.customerId, label: element.customerName });
        this.form.controls.customerId.patchValue(this.auth.customerId)
      });
    }))
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

  
  get orderDetailsLines(): FormArray {
    return this.form.get('orderDetailsLines') as FormArray;
  }
  addrows() {
    console.log(this.orderDetailsLines.value);
    this.consignmentNew1 = [];
    this.consignmentNew1 = (this.orderDetailsLines.value)
    this.orderDetailsLines.push(
      new FormGroup({
        commodityName:  new FormControl(''),
        noOfPieces:  new FormControl(''),
        declaredValue:  new FormControl(''),
        dimensionUnit:  new FormControl(''),
        length:  new FormControl(''),
        width:  new FormControl(''),
        height:  new FormControl(''),
        weightUnit:  new FormControl(''),
        weight:  new FormControl(''),
      })
    );
  }

  removeContactField(index: number): void {
    if (this.orderDetailsLines.length > 1) this.orderDetailsLines.removeAt(index);
    else
      this.orderDetailsLines.patchValue([
        { type: null, description: null, items: null },
      ]);
  }
  removeContactField1(index: number): void {
    this.orderDetailsLines.removeAt(index);
  }
  delete(i){ 
    this.consignmentNew1 = this.consignmentNew1.filter(val => val.description !== i.description );
  }
   
  fill(code){
    this.spin.show();
    this.sub.add(this.service.Get(code.orderId).subscribe(res => {
        this.form.patchValue(res, {
          emitEvent: false
        });
         this.form.controls.createdOn.patchValue(this.cs.dateapiutc0(this.form.controls.createdOn.value));
        this.form.controls.updatedOn.patchValue(this.cs.dateapiutc0(this.form.controls.updatedOn.value));
        // res.orderDetailsLines.forEach((element, index) => {
        //   if(index != res.length -1){
        //     this.addNewRow()
        //   }
        //   });
       // this.orderLines.get('orderDetailsLines')?.patchValue(res.orderDetailsLines);

        this.spin.hide();
      },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }
    ));
  }
  
  submit() {
    this.submitted = true;
    if (this.form.invalid) {
      this.toastr.error(
        "Please fill required fields to continue",
        "Notification", {
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
      this.sub.add(this.service.Update(this.js.code.orderId, this.form.getRawValue()).subscribe(res => {
        this.messageService.add({key: 'br', severity:'success', summary:'Success', detail: res.orderId  + " updated successfully"});
        
    this.router.navigate(['/main/consignment/consignmentNew/']);
        this.spin.hide();

      }, err => {

        this.cs.commonerror(err);
        this.spin.hide();

      }));
    } else {
      this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
        this.messageService.add({key: 'br', severity:'success', summary:'Success', detail: res.orderId  + " saved successfully"});
        this.router.navigate(['/main/consignment/consignmentNew/']);
        this.spin.hide();

      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();

      }));
    }

  }


  save(){
    this.removeContactField1(this.orderDetailsLines.length)
    console.log(this.form)
  }

  calculate(){
    const random = Math.floor(Math.random() * (10 - 1) + 1);
      this.form.controls.deliveryCharge.patchValue(random)
  }
}

