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
  selector: 'app-top-clients',
  templateUrl: './top-clients.component.html',
  styleUrls: ['./top-clients.component.scss']
})
export class TopClientsComponent implements OnInit {


  Highcharts: typeof Highcharts = Highcharts;
  chartOptions = {};
  sub = new Subscription();
  data: any[] = [];

  // seriesArray: any[] = [{
  //   name: 'Hours',
  //   data: [10]
  // }]
  // categories: any[] = ["Africa"];

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
    this.bindChart();
    this.getTopClients();
  }

  getTopClients() {
    this.sub.add(this.service.getTopClients(this.period.toString().toUpperCase()).subscribe(res => {
      if (res[this.period] != null && res[this.period].length > 0) {
        this.data = res[this.period];
        this.seriesArray = [];
        // this.categories = [];
        // let dataArray: any[] = [];
        this.data.forEach((element, i) => {
          // this.categories.push(element[1]);//partnername
          // dataArray.push(Number(element[2]))
          this.seriesArray.push({
            name: element[1], //partnername
            y: Number(element[2]), //invoiceAmount
            drilldown: element[1]
          })
        });
        // this.seriesArray = [{
        //   name: "Data",//partnername
        //   data: dataArray, //invoiceAmount
        // }]
        this.bindChart();
      } else {
        this.seriesArray = [];
        // this.categories = [];
        this.bindChart();
      }
    },
      err => {
        this.bindChart();
        // this.cs.commonerror(err);
      }));
  }

  bindChart() {
    this.chartOptions = {};
    this.chartOptions = {
      credits: false,
      exporting: false,
      chart: {
        type: 'bar',
        style: {
          fontFamily: 'Roboto Slab'
        }
      },
      title: {
        text: 'Top 10 clients by Billed Hours'
      },
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
          text: 'Time Ticket Hours'            //CLARA/AMS/2022/148 Mugilan 11-10-22
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
            format: '{point.y:.1f}'
          }
        }
      },

      tooltip: {
        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}</b> - total Hours<br/>'
      },

      series: [{
        name: "Data",
        colorByPoint: true,
        data: this.seriesArray
      }],
    };
    // this.chartOptions = {
    //   credits: false,
    //   exporting: false,
    //   chart: {
    //     type: 'bar',
    //     style: {
    //       fontFamily: 'Roboto Slab'
    //     }
    //   },
    //   title: {
    //     text: 'Top Clients'
    //   },
    //   xAxis: {
    //     categories: this.categories,
    //   },
    //   yAxis: {
    //     min: 0,
    //     title: {
    //       text: 'Time Ticket Hours',
    //       align: 'high'
    //     },
    //     labels: {
    //       overflow: 'justify'
    //     }
    //   },
    //   legend: {
    //     enabled: false
    //   },
    //   plotOptions: {
    //     bar: {
    //       dataLabels: {
    //         enabled: true
    //       }
    //     }
    //   },
    //   // tooltip: {
    //   //   headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
    //   //   pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}</b> of total<br/>'
    //   // },

    //   series: this.seriesArray
    // };



  }

}
