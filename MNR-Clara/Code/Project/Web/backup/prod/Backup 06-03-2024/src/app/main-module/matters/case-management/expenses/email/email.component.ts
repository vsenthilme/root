import { DatePipe } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { ExpenseService } from 'src/app/main-module/setting/business/expense-code/expense.service';
import { MatterExpensesService } from '../matter-expenses.service';
import { UserProfileService } from 'src/app/main-module/setting/admin/user-profile/user-profile.service';
import { AddStringArrayPopupComponent } from 'src/app/common-field/dialog_modules/add-string-array-popup/add-string-array-popup.component';

@Component({
  selector: 'app-email',
  templateUrl: './email.component.html',
  styleUrls: ['./email.component.scss']
})
export class EmailComponent implements OnInit {
  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);


  form = this.fb.group({
    fromEmail: ["checkrequest@montyramirezlaw.com",],
    toEmail: [],
  });


  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  getErrorMessage() {
    if (this.email.hasError('required')) {
      return ' Field should not be blank';
    }
    return this.email.hasError('email') ? 'Not a valid email' : '';
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
  newPage = true;


  constructor(
    public dialogRef: MatDialogRef<any>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: MatterExpensesService, 
    public toastr: ToastrService,
    public datepipe: DatePipe,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,  public dialog: MatDialog,
    private auth: AuthService, private fb: FormBuilder,
    private cs: CommonService, private mexpenseservice: ExpenseService,  private userService: UserProfileService,
  ) { }
  matter: any;
  btntext = "Save"
  ngOnInit(): void {

    this.form.controls.fromEmail.disable();
    this.userService.Get(this.data.checkRequestCreatedBy).subscribe(res => {
        this.form.controls.toEmail.patchValue(res.emailId);
        this.emailArray[0] = {email: res.emailId};
    })
  }
 
 submit(){
  this.dialogRef.close(this.form.controls.toEmail.value);
 }


 emailArray:any[] = [];
 addEmail() {
  const dialogRef = this.dialog.open(AddStringArrayPopupComponent, {
    disableClose: true,
    width: '50%',
    maxWidth: '80%',
    position: { top: '6.5%' },
    data: this.emailArray
  });

  dialogRef.afterClosed().subscribe(result => {
    console.log(this.emailArray)
    let mailString = "";
    this.emailArray.forEach((email: any, i) => {
      if (i == 0) {
        mailString = email.email;
      } else {
        if (mailString != "") {
          mailString = mailString + ',' + email.email;
        }
      }
    })
  this.form.controls.toEmail.patchValue(mailString);
  });
}

}