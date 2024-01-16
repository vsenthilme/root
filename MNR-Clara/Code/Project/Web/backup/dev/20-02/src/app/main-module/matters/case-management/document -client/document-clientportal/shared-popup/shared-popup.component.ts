import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import {
  HttpClient, HttpRequest,
  HttpResponse, HttpEvent, HttpErrorResponse
} from '@angular/common/http'
import { Router, ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ExcelService } from 'src/app/common-service/excel.service';
import { AuthService } from 'src/app/core/core';
import { MatterDocumetService } from '../../../matter-document/matter-documet.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-shared-popup',
  templateUrl: './shared-popup.component.html',
  styleUrls: ['./shared-popup.component.scss']
})
export class SharedPopupComponent implements OnInit {

  code: any = this.cs.decrypt(sessionStorage.getItem('matter')).code;

  constructor(public dialog: MatDialog,
    private service: MatterDocumetService, private router: Router,
    public toastr: ToastrService, private route: ActivatedRoute,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    public HttpClient: HttpClient,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService) { }

    sub = new Subscription();

    location = '';
  ngOnInit(): void {

    this.sub.add(this.service.get_matterdetails(this.code).subscribe(ress => {
      this.location= 'X:' + '\\' + 'clara'  + '\\' + 'clientportal' + '\\' + ress.clientId + '\\' + ress.matterNumber;
      console.log( this.location)
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  testdata= this.location
}

