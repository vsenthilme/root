import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { Location } from "@angular/common";
import { PreinboundService } from '../../preinbound.service';

@Component({
  selector: 'app-interwarehousetransferaddlines',
  templateUrl: './interwarehousetransferaddlines.component.html',
  styleUrls: ['./interwarehousetransferaddlines.component.scss']
})
export class InterwarehousetransferaddlinesComponent implements OnInit {

 
  constructor(private fb: FormBuilder,
    private auth: AuthService,
    public dialogRef: MatDialogRef<InterwarehousetransferaddlinesComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService,
    // private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private route: ActivatedRoute, private router: Router,
    private cs: CommonService,
    private service : PreinboundService,
    private location: Location,
    ) { }


  form = this.fb.group({
    brand: [],
  expectedDate: [],
  expectedQty: [],
  fromBranchCode: [this.auth.plantId],
  fromCompanyCode: [this.auth.companyId],
  isCompleted: [],
  lineReference: [],
  manufacturerCode: [],
  manufacturerFullName: [],
  manufacturerName: [],
  middlewareHeaderId:[],
  middlewareId: [],
  middlewareTable: [],
  orderType: [],
  origin: [],
  packQty: [],
  sku: [],
  skuDescription:[],
  supplierName: [],
  supplierPartNumber: [],
  transferOrderNo: [],
  uom: [],
  });

 cancel(){
  this.dialogRef.close();
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
  ngOnInit(): void {
    this.form.patchValue(this.data);
  }

  onItemSelect(item: any) {
    console.log(item);
  }

  onSelectAll(items: any) {
    console.log(items);
  }

  inputChange(value){
     this.form.controls.expectedDate.patchValue(this.cs.dateddMMYY(this.form.controls.expectedDate1.value))
   }
   back() {
    this.location.back();
  }

submit(){
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
  let obj: any = {};
 this.form.controls.expectedDate.patchValue(this.cs.dateddMMYY(this.form.controls.expectedDate.value));
  obj.data=this.form.value;
  this.dialogRef.close(this.form.getRawValue());
}

}



