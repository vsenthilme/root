import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';

@Component({
  selector: 'app-download',
  templateUrl: './download.component.html',
  styleUrls: ['./download.component.scss']
})
export class DownloadComponent implements OnInit {

  constructor(
  public dialogRef: MatDialogRef < any > ,
  @Inject(MAT_DIALOG_DATA) public data: any,
  public toastr: ToastrService,
  private spin: NgxSpinnerService, public dialog: MatDialog,
  private auth: AuthService,
  private fb: FormBuilder,
  public cs: CommonService,
  private cas: CommonApiService,
  private router: Router, ) {}


  ngOnInit(): void {
  }

}
