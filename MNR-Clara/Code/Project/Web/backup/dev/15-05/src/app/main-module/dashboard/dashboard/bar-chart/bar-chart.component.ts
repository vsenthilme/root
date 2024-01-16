import {
  Component,
  OnInit
} from '@angular/core';
import * as Highcharts from 'highcharts';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ExcelService } from 'src/app/common-service/excel.service';
import { AuthService } from 'src/app/core/core';
import { DashboardService } from '../../dasboard-service.service';

enum PeriodDataEnum {
  CURRENTMONTH = 'currentMonth',
  CURRENTQUARTER = 'currentQuarter',
  PREVIOUSMONTH = 'previousMonth',
  PREVIOUSQUARTER = 'previousQuarter',
  CURRENTYEAR = 'currentYear',
  PREVIOUSYEAR = 'previousYear'
}
@Component({
  selector: 'app-bar-chart',
  templateUrl: './bar-chart.component.html',
  styleUrls: ['./bar-chart.component.scss']
})
export class BarChartComponent implements OnInit {


  Highcharts = Highcharts;
  chartOptions = {};
  sub = new Subscription();
  data: any[] = [];

  seriesArray = [{
    name: "Jacob Monty",
    y: 75000,
    drilldown: "Jacob Monty"
  }, {
    name: "Sarah Monty",
    y: 40000,
    drilldown: "Sarah Monty"
  }
  ]

  periodEnum = PeriodDataEnum;
  period = PeriodDataEnum.CURRENTMONTH;
  classId = 1;

  constructor(
    private service: DashboardService,
    private cs: CommonService,
    private spin: NgxSpinnerService,
    public toastr: ToastrService,
    private excel: ExcelService,
    private cas: CommonApiService,
    private auth: AuthService
  ) { }

  ngOnInit() {

    this.getBilledIncome();
    this.bindChart(this.seriesArray);
  }

  getBilledIncome() {
    this.sub.add(this.service.getBilledIncome(this.classId, this.period.toString().toUpperCase()).subscribe(res => {
      if (res[this.period] != null && res[this.period].length > 0) {
        this.data = res[this.period];
        let seriesArray: any[] = [];
        this.data.forEach(element => {
          seriesArray.push({
            name: element[0], //partnername
            y: Number(element[1]), //invoiceAmount
            drilldown: element[0]
          })
        });
        this.bindChart(seriesArray);
      } else {
        let seriesArray: any[] = [];
        this.bindChart(seriesArray);
      }
    },
      err => {
        this.bindChart(this.seriesArray);
        // this.cs.commonerror(err);
      }));
  }

  bindChart(seriesArray: any) {
    this.chartOptions = {
      credits: false,
      exporting: false,
      chart: {
        type: 'column',
        style: {
          fontFamily: 'Roboto Slab'
        }
      },
      colors: [
        '#0F487F',
        '#434348',
        '#90ED7D',
        '#F7A35C',
        '#8085E9',
        '#F15C80',
    ],

      title: {
        text: 'Billed Income by Partners'
      },
      // subtitle: {
      //     text: 'Click the columns to view versions. Source: <a href="http://statcounter.com" target="_blank">statcounter.com</a>'
      // },
      accessibility: {
        announceNewData: {
          enabled: true
        }
      },
      xAxis: {
        type: 'category'
      },
      yAxis: {
        title: {
          text: 'Dollors'
        }

      },
      legend: {
        enabled: false
      },
      plotOptions: {
        series: {
          borderWidth: 0,
          dataLabels: {
            enabled: true,
            format: '${point.y:.1f}'
          }
        }
      },

      tooltip: {
        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}</b> of total<br/>'
      },

      series: [{
        name: "Data",
        colorByPoint: true,
        data: seriesArray
      }],
    };


  }

}
