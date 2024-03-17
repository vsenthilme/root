import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { BasicdataService } from 'src/app/main-module/Masters -1/masternew/basicdata/basicdata.service';
import { ReportsService } from 'src/app/main-module/reports/reports.service';

@Component({
  selector: 'app-confirm',
  templateUrl: './confirm.component.html',
  styleUrls: ['./confirm.component.scss']
})
export class ConfirmComponent implements OnInit {

  panelOpenState = false;

  
  constructor(
    public dialog: MatDialog,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private router: Router,
    public cs: CommonService,
    // private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService,
    private service: BasicdataService,
    private service1: ReportsService,
    public dialogRef: MatDialogRef<ConfirmComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) { }
  ngOnInit(): void {
  }


  file: File;
  onFilechange(event: any) {
    console.log(event)
    this.file = event.target.files[0];
  }
  Confirm(): void {
    this.spin.show();
    this.service.uploadfile(this.file).subscribe((resp: any) => {
      this.toastr.success("File uploaded successfully.", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    })
   // this.dialogRef.close(this.file);
  }

}
