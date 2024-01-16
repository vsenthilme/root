import { Component, OnInit, ViewChild } from '@angular/core';
import { Table } from 'primeng/table';
import {
  AbstractControl,
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  Validators

} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonService } from 'src/app/common-service/common-service.service';
import { NgxSpinnerComponent, NgxSpinnerService } from 'ngx-spinner';
import { Subscription } from 'rxjs';
import { ConsignmentService } from '../../consignment.service';
import { MessageService } from 'primeng/api';
import { CustomerService } from 'src/app/main-module/customers/customer.service';
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

  public orderLines: FormGroup;

  consignmentNew1: any;
  selectedconsignmentNew1 : variant1[];
  sub = new Subscription();
  constructor(
    private fb: FormBuilder,  private route: ActivatedRoute, private cs: CommonService, private spinner: NgxSpinnerService,
    private service: ConsignmentService, private messageService: MessageService, private router: Router, private customerService: CustomerService) {}
    initRows() {
      return this.fb.group({
        commodityName: [],
        noOfPieces: [],
        declaredValue: [],
        dimensionUnit: [],
        length: [],
        width: [],
        height: [],
        weightUnit: [],
        weight: [],
      });
    }
      form = this.fb.group({
          languageId: ["EN", ],
          companyId: ["1000",],
          orderId: [],
          referenceNo: [],
          shipsyOrderNo: [],
          customerId: [],
          loadType: [],
          typeOfDelivery: [],
          deliveryCharge: [],
          originDetailsName: [],
          originDetailsPhone: [],
          originDetailsAddressLine1: [],
          originDetailsAddressLine2: [],
          originDetailsPincode: [],
          destinationDetailsName: [],
          destinationDetailsPhone: [],
          destinationDetailsAddressLine1: [],
          destinationDetailsAddressLine2: [],
          destinationDetailsPincode: [],
          serviceTypeId: [],
          loyaltyPoint: [],
          loyaltyAmount: [],
          originCity: [],
          originState: [],
          originCountry: [],
          destinationCity: [],
          destinationState: [],
          destinationCountry: [],
          status: ['New',],
          deletionIndicator: [],
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
          createdBy: [],
          createdOn: [],
          updatedOn: [],
          updatedBy: [],
    });

    get formArr() {
      return this.orderLines.get('Rows') as FormArray;
    }
  
    js: any = {}
    btnText = 'Save'
  ngOnInit(): void {
    let code = this.route.snapshot.params.code;

   
    this.js = this.cs.decrypt(code);
    this.orderLines = this.fb.group({
      Rows: this.fb.array([this.initRows()]),
    });

    if (this.js.pageflow != 'New') {
      this.btnText ="Update"
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
        this.multiCustomerList.push({ value: element.customerId, label: element.customerName })
      });
    }))
  }

  fill(code){
    this.spinner.show();
    this.sub.add(this.service.Get(code.orderId).subscribe(res => {
        this.form.patchValue(res, {
          emitEvent: false
        });
        this.form.controls.createdOn.patchValue(this.cs.dateapiutc0(this.form.controls.createdOn.value));
        this.form.controls.updatedOn.patchValue(this.cs.dateapiutc0(this.form.controls.updatedOn.value));
        res.orderDetailsLines.forEach((element, index) => {
          if(index != res.length -1){
            this.addNewRow()
          }
          });
        this.orderLines.get('Rows')?.patchValue(res.orderDetailsLines);

        this.spinner.hide();
      },
      err => {
        this.cs.commonerror(err);
        this.spinner.hide();
      }
    ));
  }

  addNewRow() {
    this.formArr.push(this.initRows());
  }

  removerow(x: any){
    this.formArr.removeAt(x);
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

  save(){
            let obj: any = {};
            obj = this.form.getRawValue();
            let orderDetailsLines: any[] = [];
            orderDetailsLines =    this.orderLines.getRawValue().Rows
            obj.orderDetailsLines = orderDetailsLines;

            console.log(obj)
           if(this.js.code){
            this.sub.add(this.service.Update(this.js.code.orderId, obj).subscribe(res => {
              this.messageService.add({key: 'br', severity:'success', summary:'Success', detail: res.orderId  + " updated successfully"});
              this.router.navigate(['/main/consignment/consignmentNew']);
              this.spinner.hide();
              },
              err => {
                this.cs.commonerror(err);
                this.spinner.hide();
              }))
           }
           else{
            this.sub.add(this.service.Create(obj).subscribe(res => {
              this.messageService.add({key: 'br', severity:'success', summary:'Success', detail: res.orderId  + " created successfully"});
              this.router.navigate(['/main/consignment/consignmentNew']);
              this.spinner.hide();
              },
              err => {
                this.cs.commonerror(err);
                this.spinner.hide();
              }))
           }
  }

  calculate(){
    const random = Math.floor(Math.random() * (10 - 1) + 1);
      this.form.controls.deliveryCharge.patchValue(random)
  }
}

