import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { DialogExampleComponent } from 'src/app/common-field/dialog-example/dialog-example.component';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';

@Component({
  selector: 'app-matter-popup',
  templateUrl: './matter-popup.component.html',
  styleUrls: ['./matter-popup.component.scss']
})
export class MatterPopupComponent implements OnInit {
 isLinear = false;
 constructor(
  public dialogRef: MatDialogRef<DialogExampleComponent>,
  @Inject(MAT_DIALOG_DATA) public data: any,
  public toastr: ToastrService,
  private spin: NgxSpinnerService,
  private auth: AuthService,
  private fb: FormBuilder,
  private cs: CommonService,
) { }

  ngOnInit(): void {
    console.log(this.data.code.matterNumber)
  }

}
