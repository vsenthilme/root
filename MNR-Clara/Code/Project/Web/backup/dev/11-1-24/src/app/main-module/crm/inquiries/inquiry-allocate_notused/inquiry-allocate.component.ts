import { Component, OnInit, Inject } from "@angular/core";
import { FormControl, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { DialogExampleComponent, DialogData } from "src/app/common-field/dialog-example/dialog-example.component";



@Component({
  selector: 'app-inquiry-allocate',
  templateUrl: './inquiry-allocate.component.html',
  styleUrls: ['./inquiry-allocate.component.scss']
})
export class InquiryAllocateComponent implements OnInit {
  screenid: 1063 | undefined;

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
  constructor(
    public dialogRef: MatDialogRef<DialogExampleComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) { }
  ngOnInit(): void {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}

