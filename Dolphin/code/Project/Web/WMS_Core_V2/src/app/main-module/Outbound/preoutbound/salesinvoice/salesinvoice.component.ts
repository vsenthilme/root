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
import { PreoutboundService } from '../preoutbound.service';

@Component({
  selector: 'app-salesinvoice',
  templateUrl: './salesinvoice.component.html',
  styleUrls: ['./salesinvoice.component.scss']
})
export class SalesinvoiceComponent implements OnInit {

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
    private service : PreoutboundService,
    private masterService: MasterService
    ) { }



  form = this.fb.group({
    branchCode: [this.auth.plantId],
    companyCode: [this.auth.companyId],
    invoiceDate: [],
    pickListNumber: [],
    salesInvoiceNumber: [],
    salesOrderNumber: [],
  
  
  
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
    this.form.get('fromBranchCode')?.disable();
    this.form.get('fromCompanyCode')?.disable();   
console.log(this.js)

  this.form.controls.branchCode.patchValue(this.auth.plantId);
  this.form.controls.companyCode.patchValue(this.auth.companyId);
  
  }
  
  sub = new Subscription();


  submit() {
    let obj: any = {};
   obj.salesInvoiceNumber = this.form.controls.salesInvoiceNumber.value;
    obj.salesOrderNumber = this.form.controls.salesOrderNumber.value;
    obj.branchCode = this.auth.plantId;
   this.form.controls.invoiceDate.patchValue(this.cs.dateapiutc0(this.form.controls.invoiceDate.value));
    obj.companyCode=this.auth.companyId;
    obj.invoiceDate = this.form.controls.invoiceDate.value;
    obj.pickListNumber = this.form.controls.pickListNumber.value;
   


    this.sub.add(this.service.createsalesinvoice(obj).subscribe(res => {
  if(res){
    this.toastr.success("Order created successfully!",   "Notification");
    this.router.navigate(['/main/outbound/preoutbound']);
    this.spin.hide();
  }

    }, err => {
      this.cs.commonerrorNew(err);
      this.spin.hide();
    }));
  };
}


