import { Component, OnInit } from '@angular/core';
import { ControlContainer, ControlValueAccessor, FormBuilder, FormControl, FormGroup, NgControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-immigiration-from007',
  templateUrl: './immigiration-from007.component.html',
  styleUrls: ['./immigiration-from007.component.scss']
})
export class ImmigirationFrom007Component implements OnInit {

  constructor(private controlContainer: ControlContainer) {
    // formControl.valueAccessor = this;

  }
  writeValue(obj: any): void {
    // throw new Error('Method not implemented.');
  }
  registerOnChange(fn: any): void {
    // throw new Error('Method not implemented.');
  }
  registerOnTouched(fn: any): void {
    // throw new Error('Method not implemented.');
  }
  public chform: FormGroup;
  ngOnInit(): void {
    this.chform = <FormGroup>this.controlContainer.control;
  }

  todaydate = new Date();
  email = new FormControl('', [Validators.required, Validators.email]);
  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return false;
    // if (control.includes('.')) {
    //   const controls = this.form.get(control);
    //   return controls ? controls.hasError(error) : false && this.submitted;

    // }
    // return this.form.controls[control].hasError(error) && this.submitted;
  }
  public errorHandling_admin = (control: string, error: string = "required") => {
    return false;
    // if (control.includes('.')) {
    //   const controls = this.form.get(control);
    //   return controls ? controls.hasError(error) : false && this.submitted;
    // }
    // return this.intakefg.controls[control].hasError(error) && this.submitted;
  }
  getErrorMessage() {
    return '';
    // if (this.email.hasError('required')) {
    //   return ' Field should not be blank';
    // }
    // return this.email.hasError('email') ? 'Not a valid email' : this.email.hasError('email') ? ' Field should not be blank' : '';
  }
}
