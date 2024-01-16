import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { DialogExampleComponent } from 'src/app/common-field/innerheader/dialog-example/dialog-example.component';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { PreinboundService } from '../../preinbound.service';

@Component({
  selector: 'app-preinboundaddlines',
  templateUrl: './preinboundaddlines.component.html',
  styleUrls: ['./preinboundaddlines.component.scss']
})
export class PreinboundaddlinesComponent implements OnInit {
  constructor(private fb: FormBuilder,
    private auth: AuthService,
    public dialogRef: MatDialogRef<DialogExampleComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService,
    // private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private route: ActivatedRoute, private router: Router,
    private cs: CommonService,
    private service : PreinboundService,
    ) { }


  form = this.fb.group({
    containerNumber: [],
    expectedDate: [],
    expectedDate1: [],
    expectedQty:[],
    invoiceNumber:[],
    lineReference: [],
    manufacturerName: [],
    manufacturerCode: [],
    packQty: [],
    sku: [],
    skuDescription: [],
    supplierCode: [],
    supplierPartNumber: [],
    uom: [],
  });


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
submit(){
  
  let obj: any = {};
  obj.data=this.form.value;
  this.dialogRef.close(this.form.getRawValue());
}
}


