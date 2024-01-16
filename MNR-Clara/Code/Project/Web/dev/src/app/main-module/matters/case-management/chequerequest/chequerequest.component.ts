import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { MatterExpensesService } from '../expenses/matter-expenses.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ChequerequestService } from './chequerequest.service';

@Component({
  selector: 'app-chequerequest',
  templateUrl: './chequerequest.component.html',
  styleUrls: ['./chequerequest.component.scss']
})

export class ChequerequestComponent implements OnInit {
  js: any = {};
  matterdes:any;
  constructor(private fb: FormBuilder, private spin: NgxSpinnerService,private cheque:MatterExpensesService,  private route: ActivatedRoute,
    private router: Router,
    private cs: CommonService,
    private service: ChequerequestService ) { }

  ngOnInit(): void {
    let code = this.route.snapshot.params.code;
   
    this.js = this.cs.decrypt(code);
    console.log(this.js);
    this.form.patchValue(this.js.data, { emitEvent: false });
  
    this.form.controls.requestDate.patchValue(this.cs.dateapiutc0(this.form.controls.referenceField2.value))
  this.sub.add(this.service.Get(this.js.data.clientId).subscribe(res=>{
    this.form.controls.clientName.patchValue(res.lastNameFirstName,{emitEvent:false})
  }));
  console.log(this.js.description);
  this.matterdes.patchValue(this.js.data.description);
   
  }
  sub = new Subscription();
  form = this.fb.group({
    billType: [],
  billableToClient: [],
  cardNotAccepted: [],
  caseCategoryId: [],
  caseInformationNo: [],
  caseSubCategoryId: [],
  classId: [],
  clientId: [],
  costPerItem: [],
  createdBy: [],
  createdOn: [],
  deletionIndicator: [],
  expenseAccountNumber: [],
  expenseAmount: [],
  expenseCode: [],
  expenseDescription: [],
  expenseType: [],
  languageId: [],
  matterExpenseId: [],
  matterNumber: [],
  numberofItems: [],
  payableTo: [],
  paymentMode: [],
  rateUnit: [],
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
  requiredDate: [],
  screatedOn: [],
  sreferenceField2: [],
  statusId: [],
  updatedBy: [],
  requestDate:[],
  updatedOn: [],
  userName: [],
  vendorFax: [],
  vendorMailingAddress: [],
  vendorNotes: [],
  vendorPhone: [],
  clientName:[],
  writeOff: [],

  });
  

  submit() {

 

}
}