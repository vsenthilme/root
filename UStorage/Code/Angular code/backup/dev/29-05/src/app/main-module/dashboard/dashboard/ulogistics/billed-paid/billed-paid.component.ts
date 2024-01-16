
  import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import * as Highcharts from 'highcharts';
import * as theme from 'highcharts/themes/dark-unica';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { DashboardService } from '../../dashboard.service';
@Component({
  selector: 'app-billed-paid',
  templateUrl: './billed-paid.component.html',
  styleUrls: ['./billed-paid.component.scss']
})
export class BilledPaidComponent implements OnInit {

 
  array=[
    {   color: '#90ED7D',
        "name": "Billed",
        "data": [0,0,0,0,0,0,0,0,0,0,0,0]
    },
    {   color: '#7CB5EC',
        "name": "Paid",
        "data": [0,0,0,0,0,0,0,0,0,0,0,0]
    }
]

 

  
  Highcharts = Highcharts;
  chartOptions = {};

 

  constructor(      private service: DashboardService,
    private fb: FormBuilder,
    public toastr: ToastrService,
    public cs: CommonService,
    private spin: NgxSpinnerService,
    private auth: AuthService,
    public dialog: MatDialog,
    private route: ActivatedRoute,
    private cas: CommonApiService,
  ) { }
  
  customerType: any[] = [];
  leadType: any[] = [];
  vendorType: any[] = [];
  dataArray: any;

  year = new Date().getFullYear();
  ngOnInit() {
   this.getData(new Date().getFullYear());
  this.bindChart(this.array);
  this.yearValue();
  }

  onSelectEvent(e){ this.getData(e)}
  yearValueList: any[] = [];
  yearValue() {
    for (let i = new Date().getFullYear() - 2; i < new Date().getFullYear() + 1; i++) {
      this.yearValueList.push({value: i, label: i })
    }
   
    this.yearValueList.sort((a, b) => (a.value < b.value) ? 1 : -1);
  }
  dataList: any[] = []; 
  getData(year){
    this.spin.show();
    this.dataList = [];
    this.service.billedPaid(year).subscribe(res => {
     
      this.dataList.push(
        {   color: '#90ED7D',
        "name": "Billed",
        "data": res.ulogisticsBilled
        },
        {   color: '#7CB5EC',
            "name": "Paid",
            "data": res.ulogisticsPaid
        }
      )

      this.bindChart(this.dataList);
      this.spin.hide();
    })
  }
  bindChart(array){
    
    this.chartOptions = {
      credits: false,
      chart: {
        type: 'column'
    },
    title: {
        text: 'Billed vs Paid'
    },
    xAxis: {
        categories: [
            'Jan',
            'Feb',
            'Mar',
            'Apr',
            'May',
            'Jun',
            'Jul',
            'Aug',
            'Sep',
            'Oct',
            'Nov',
            'Dec'
        ],
        crosshair: true
    },
    yAxis: {
        min: 0,
        title: {
            text: 'Amount'
        }
    },
    tooltip: {
        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
            '<td style="padding:0"><b>{point.y:.3f}KWD</b></td></tr>',
        footerFormat: '</table>',
        shared: true,
        useHTML: true
    },
    plotOptions: {
        column: {
            pointPadding: 0.2,
            borderWidth: 0
        }
    },
    series: array
    };
  }

}
