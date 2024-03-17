import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { PreoutboundService } from '../../preoutbound.service';

@Component({
  selector: 'app-interwarehouselines',
  templateUrl: './interwarehouselines.component.html',
  styleUrls: ['./interwarehouselines.component.scss']
})
export class InterwarehouselinesComponent implements OnInit {

  constructor(private fb: FormBuilder,
    private auth: AuthService,
    public dialogRef: MatDialogRef<InterwarehouselinesComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService,
    // private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private route: ActivatedRoute, private router: Router,
    private cs: CommonService,
    private service : PreoutboundService,
    ) { }


  form = this.fb.group({
    brand: [],
  lineReference: [],
  manufacturerCode: [],
  manufacturerName: [],
  orderType: [],
  orderedQty: [],
  sku: [],
  skuDescription: [],
  uom: [],
  });


  ngOnInit(): void {
   
    //this.form.controls.confirmedStorageBin.patchValue(this.data.confirmedStorageBin.value);
    console.log(this.data);
    this.form.patchValue(this.data);
  }

  onItemSelect(item: any) {
    console.log(item);
  }

  onSelectAll(items: any) {
    console.log(items);
  }

  inputChange(value){
    console.log((this.form.controls.expectedDate.patchValue(this.cs.dateddMMYY(this.form.controls.expectedDate1.value))))
 
     this.form.controls.expectedDate.patchValue(this.cs.dateddMMYY(this.form.controls.expectedDate1.value))
   }
submit(){
  
  let obj: any = {};
  console.log(this.form.value);
  obj.data=this.form.value;
  console.log(obj.data)
  this.dialogRef.close(this.form.getRawValue());
  console.log(this.form.getRawValue());
}
cancel(){
  this.dialogRef.close();
}
}




