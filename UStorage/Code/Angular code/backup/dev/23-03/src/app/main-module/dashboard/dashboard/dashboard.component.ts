import { ArrayDataSource } from '@angular/cdk/collections';
import { NestedTreeControl } from '@angular/cdk/tree';
import { DatePipe } from '@angular/common';
import { Component, Injectable, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/core/core';
import { BarChartComponent } from './bar-chart/bar-chart.component';
import { DashboardService } from './dashboard.service';



@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})

@Injectable({
  providedIn: 'root'
})

export class DashboardComponent implements OnInit {
  planModel: any = { start_time: new Date() };
  day_callapiSearch(date: any) {
    const del_date = this.datepipe.transform(date, 'yyyy-MM-ddT00:00:00.000');
    return del_date;
  }
  panelOpenState = false;
  dashboardCountData: any = {};
  date = new Date();
  constructor(
    private datepipe: DatePipe,
    private bar: BarChartComponent,
    public auth: AuthService,
    public spin: NgxSpinnerService,
    public toastr: ToastrService,
    public dashboardService: DashboardService,
  ) { }

  ngOnInit() {
    this.date.setDate(this.date.getDate() - 1);
  }

  tabChanged(e){
    console.log(e.index)
   // this.bar.getData(e.index)
  }

}
