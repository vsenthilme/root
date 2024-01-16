import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { GeneralMatterService } from 'src/app/main-module/matters/case-management/General/general-matter.service';
import { QuotationService } from '../quotation.service';

@Component({
  selector: 'app-payment-link',
  templateUrl: './payment-link.component.html',
  styleUrls: ['./payment-link.component.scss']
})
export class PaymentLinkComponent implements OnInit {
  constructor(public dialogRef: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) public data: any) { }

  public toggleButton: boolean = false;

//  url: string = 'https://www.domainname.com';

  ngOnInit(): void {
    if(this.data != null){
     this.toggleButton = true;
    }
  }
  submit() {
this.data.statusId = 52;
    this.dialogRef.close(this.data);
    console.log(this.data)
  }
  edit(){
    this.toggleButton = false;
  }
}
