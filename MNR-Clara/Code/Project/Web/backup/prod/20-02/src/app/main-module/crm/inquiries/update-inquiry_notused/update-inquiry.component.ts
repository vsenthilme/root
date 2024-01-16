import { Component, OnInit, Inject } from "@angular/core";
import { FormControl, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { DialogExampleComponent, DialogData } from "src/app/common-field/dialog-example/dialog-example.component";
import { inquiryElement } from "../models/inquiry";


@Component({
  selector: 'app-update-inquiry',
  templateUrl: './update-inquiry.component.html',
  styleUrls: ['./update-inquiry.component.scss']
})
export class UpdateInquiryComponent implements OnInit {


  email = new FormControl('', [Validators.required, Validators.email]);
  input: any;

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





  formControl = new FormControl('', [
    Validators.required
    // Validators.email,
  ]);



  constructor(
    public dialogRef: MatDialogRef<DialogExampleComponent>,
    @Inject(MAT_DIALOG_DATA) public data: inquiryElement) { }
  ngOnInit(): void {

  }

  onNoClick(): void {
    this.dialogRef.close({ data: this.data });
  }

  submit() { }
}

