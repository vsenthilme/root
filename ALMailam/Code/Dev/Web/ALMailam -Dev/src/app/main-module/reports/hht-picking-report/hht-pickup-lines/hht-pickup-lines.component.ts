import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { DialogExampleComponent } from 'src/app/common-field/innerheader/dialog-example/dialog-example.component';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { InboundConfirmationService } from 'src/app/main-module/Inbound/inbound-confirmation/inbound-confirmation.service';

@Component({
  selector: 'app-hht-pickup-lines',
  templateUrl: './hht-pickup-lines.component.html',
  styleUrls: ['./hht-pickup-lines.component.scss']
})
export class HhtPickupLinesComponent implements OnInit {
  inventory:any[]=[];
  constructor(  public dialogRef: MatDialogRef<DialogExampleComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
   // private service: InboundConfirmationService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private auth: AuthService,
    private fb: FormBuilder,
    public cs: CommonService,) { }

  ngOnInit(): void {
    console.log(this.data)
    this.inventory=this.data;
  }

}
