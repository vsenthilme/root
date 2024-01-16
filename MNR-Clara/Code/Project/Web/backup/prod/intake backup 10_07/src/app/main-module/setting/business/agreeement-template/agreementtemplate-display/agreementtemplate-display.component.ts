
import { AgreementElement, AgreementTemplateService } from "../agreement-template.service";
import {
  HttpClient, HttpRequest,
  HttpResponse, HttpEvent, HttpErrorResponse
} from '@angular/common/http'
import { Component, OnInit, Inject, OnDestroy, ViewChild } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DialogExampleComponent, DialogData } from "src/app/common-field/dialog-example/dialog-example.component";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";


interface SelectItem {
  id: string;
  itemName: string;
}

@Component({
  selector: 'app-agreementtemplate-display',
  templateUrl: './agreementtemplate-display.component.html',
  styleUrls: ['./agreementtemplate-display.component.scss']
})
export class AgreementtemplateDisplayComponent implements OnInit {

  screenid: 1048 | undefined;
  email = new FormControl('', [Validators.required, Validators.email]);
  input: any;
  sub = new Subscription();

  form = this.fb.group({
    agreementCode: [,],
    createdBy: [this.auth.userID, [Validators.required]],
    agreementUrl: [,],
    classId: [, [Validators.required]],
    languageId: [, [Validators.required]],
    agreementStatus: ["Active", [Validators.required]],
    agreementFileDescription: [, [Validators.required]],
    updatedBy: [this.auth.userID, [Validators.required]],
    deletionIndicator: [0],
    caseCategoryId: [,],
    mailMerge: [, [Validators.required]],
    createdOn: [],
    updatedOn: []

  });

  uploadbutton=true;
  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  uploadstatus: boolean;
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
  formgr: AgreementElement | undefined;

  panelOpenState = false;
  constructor(
    public dialogRef: MatDialogRef<DialogExampleComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: AgreementTemplateService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private auth: AuthService, private fb: FormBuilder,
    private cs: CommonService, public HttpClient: HttpClient,
  ) { }
  ngOnInit(): void {
    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOn.disable();
    this.auth.isuserdata();
    this.dropdownlist();
    if (this.data.pageflow != 'New') {
      if (this.data.pageflow == 'Display')
      this.uploadbutton=false
        this.form.disable();
      this.fill();
      this.form.controls.agreementCode.disable();

      this.form.controls.classId.disable();
      this.form.controls.caseCategoryId.disable();
      this.form.controls.languageId.disable();
    }

    if (this.data.pageflow != 'Display' && this.data.pageflow != 'New'){
      this.form.controls.agreementFileDescription.enable();
      this.form.controls.mailMerge.enable();
      this.form.controls.agreementStatus.enable();
    }
  }


  caseCategoryIdList: any[] = [];
  classIdList: any[] = [];
  languageIdList: any[] = [];

  selectedItems: SelectItem[] = [];
  multiselectcasesubList: SelectItem[] = [];
  multicasesubList: SelectItem[] = [];


    
  dropdownSettings = {
    singleSelection: true,
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2
  };


  

  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url,
    this.cas.dropdownlist.setup.caseCategoryId.url, this.cas.dropdownlist.setup.languageId.url]).subscribe((results) => {

      this.classIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.classId.key);
      this.caseCategoryIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.caseCategoryId.key);
      this.caseCategoryIdList.forEach((x: { key: string; value: string; }) => this.multicasesubList.push({id: x.key, itemName:  x.value}))
      this.multiselectcasesubList = this.multicasesubList;
      this.languageIdList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.languageId.key);

    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();
    this.form.controls.languageId.patchValue("EN");
  }

  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });
      this.multicasesubList.forEach(element => {
        if(element.id == res.caseCategoryId){
          console.log(this.form)
          this.form.controls.caseCategoryId.patchValue([element]);
        }
      });
      this.form.controls.mailMerge.patchValue(res.mailMerge ? 'true' : 'false');
      this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
      this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  files: File[] = [];
  postUrl = '/doc-storage/upload';
  myFormData!: FormData;

  submit() {
    this.submitted = true;
    this.uploadstatus = this.files.length > 0;
    if (this.form.invalid) {
      this.toastr.error(
        "Please fill the required fields to continue",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );

      this.cs.notifyOther(true);
      return;
    }

    this.cs.notifyOther(false);
    this.spin.show();
    
    this.form.removeControl('updatedOn');
    this.form.removeControl('createdOn');
    if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
      this.form.patchValue({caseCategoryId: this.selectedItems[0].id });
    }else{
      this.form.patchValue({caseCategoryId: 0 });
    }
    this.form.patchValue({ updatedby: this.auth.username });
    if (this.data.code) {

      if (this.files.length > 0) {
        this.spin.show();
        const config = new HttpRequest('POST', `/doc-storage/upload?location=agreement`, this.myFormData, {
          reportProgress: true
        })
        this.HttpClient.request(config)
          .subscribe(event => {
            // this.httpEvent = event


            if (event instanceof HttpResponse) {
             // this.dialogRef.close();
              this.spin.hide();

              let body: any = event.body;
              this.form.controls.agreementUrl.patchValue(body.file);
              this.spin.show();
              this.toastr.success("Document uploaded successfully.", "Notification", {
                timeOut: 2000,
                progressBar: false,
              });

              this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code).subscribe(res => {
                this.toastr.success(this.data.code + " updated successfully!", "Notification", {
                  timeOut: 2000,
                  progressBar: false,
                });
                this.spin.hide();

                this.dialogRef.close();
              }, err => {

                this.cs.commonerror(err);
                this.spin.hide();

              }));
            }
          },
            error => {
              this.spin.hide();

              this.cs.commonerror(error);

            })
      }
      else
        this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code).subscribe(res => {
          this.toastr.success(this.data.code + " updated successfully!", "Notification", {
            timeOut: 2000,
            progressBar: false,
          });
          this.spin.hide();

          this.dialogRef.close();
        }, err => {

          this.cs.commonerror(err);
          this.spin.hide();

        }));


    }
    else {

      if (this.files.length > 0) {
        this.spin.show();
        const config = new HttpRequest('POST', `/doc-storage/upload?location=agreement`, this.myFormData, {
          reportProgress: true
        })
        this.HttpClient.request(config)
          .subscribe(event => {
            // this.httpEvent = event


            if (event instanceof HttpResponse) {
              this.spin.hide();
              this.spin.show();

              let body: any = event.body;
              this.form.controls.agreementUrl.patchValue(body.file);
              this.toastr.success("Document uploaded successfully.", "Notification", {
                timeOut: 2000,
                progressBar: false,
              });

              this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
                this.toastr.success(res.agreementCode + " saved successfully!", "Notification", {
                  timeOut: 2000,
                  progressBar: false,
                });
                this.spin.hide();
                this.dialogRef.close();



              }, err => {
                this.cs.commonerror(err);
                this.spin.hide();

              }));
            }
          },
            error => {
              this.spin.hide();

              this.cs.commonerror(error);

            })
      }
      else {
        this.toastr.error("Please upload the document.", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();

      }

    }
  };

  onNoClick(): void {
    this.dialogRef.close();
  }
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }
}





