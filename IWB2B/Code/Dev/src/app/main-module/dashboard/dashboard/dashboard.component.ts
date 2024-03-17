import { ArrayDataSource } from '@angular/cdk/collections';
import { NestedTreeControl } from '@angular/cdk/tree';
import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/core/core';
import { DashboardService } from './dashboard.service';
import { CommonService } from 'src/app/common-service/common-service.service';



@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})

export class DashboardComponent implements OnInit {
  planModel: any = { start_time: new Date() };
  showDashboard: boolean;
  day_callapiSearch(date: any) {
    const del_date = this.datepipe.transform(date, 'yyyy-MM-ddT00:00:00.000');
    return del_date;
  }
  panelOpenState = false;
  dashboardCountData: any = {};
  date = new Date();
  constructor(
    private datepipe: DatePipe,
    public auth: AuthService,
    public spin: NgxSpinnerService,
    public toastr: ToastrService,
    private service: DashboardService,
    private cs: CommonService
  ) { }


  fromCreatedOn: any;
  toCreatedOn: any;
  fromCreatedOn1: any;
  toCreatedOn1: any;

  ngOnInit() {
    this.showDashboard = false
    this.date.setDate(this.date.getDate() - 1);

    this.fromCreatedOn = new Date("07/01/23 00:00:00"),
    this.toCreatedOn = new Date();

    this.Boutiqaat = 0;
    this.JNT = 0;
    this.BoutiqaatFailure = 0;
    this.JNTFailure = 0;
    this.iwint = 0;
    this.iwintFailure = 0;

    this.getBoutiqaat();
  }

  dateChanged(e){
    this.getBoutiqaat();
  }

  Boutiqaat: any;
  JNT: any;
  BoutiqaatFailure: any;
  JNTFailure: any;
  iwint: any;
  iwintFailure: any;



  getBoutiqaat(){
    let obj: any = {};
    obj.fromCreatedOn = this.cs.dateYYMMDD(this.fromCreatedOn);
    obj.toCreatedOn = this.cs.dateYYMMDD(this.toCreatedOn);

    this.spin.show();
    this.service.getDashboardCount(obj).subscribe(res => {
      this.Boutiqaat =  res.boutiqaatPassCount;
      this.JNT =  res.jntPassCount;

      this.BoutiqaatFailure =  res.boutiqaatFailCount;
      this.JNTFailure =  res.jntFailCount;
      
      this.iwint =  res.iwintlPassCount;
      this.iwintFailure =  res.iwintlFailCount;

      this.spin.hide();
    },(err) =>{
      this.spin.hide();      
this.cs.commonerror(err);
    });
  }


}
