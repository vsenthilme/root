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
  selector: 'app-lead-conversion',
  templateUrl: './lead-conversion.component.html',
  styleUrls: ['./lead-conversion.component.scss']
})
export class LeadConversionComponent implements OnInit {


  Highcharts: typeof Highcharts = Highcharts;
  chartOptions = {};
  sub = new Subscription();
  data: any[] = [];

  categories: any[] = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'June', 'July', 'August'];

  averageArray = [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5];
  assignedUserArray = [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5];
  clientInquiryArray = [3.0, 5.9, 1.5, 6.5, 7.2, 9.5, 12.2, 11.5];

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
    this.getLeadConvesion();
  }

  getLeadConvesion() {
    this.sub.add(this.service.getLeadConversion(this.classId, this.period.toString().toUpperCase()).subscribe(res => {
      if (res[this.period] != null && res[this.period].length > 0) {
        this.data = res[this.period];
        this.averageArray = [];
        this.assignedUserArray = [];
        this.clientInquiryArray = [];
        this.categories = [];
        // let dataArray: any[] = [];
        this.data.forEach((element, i) => {
          this.categories.push(element[0]);//assignedUserId
          this.assignedUserArray.push(Number(element[1])) // totalAssignedUserCount
          this.clientInquiryArray.push(Number(element[2])) // totalAssignedUserCount
          this.averageArray.push(Number(element[3])) // Average
        });
        console.log(this.categories)
        this.bindChart();
      } else {
        this.averageArray = [];
        this.assignedUserArray = [];
        this.clientInquiryArray = [];
        this.categories = [];
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
        zoomType: 'xy'
      },
      title: {
        text: 'Lead Conversion'
      },
      xAxis: [{
        categories: this.categories,
        crosshair: true
      }],
      yAxis: [{ // Secondary yAxis
        title: {
          text: 'Average',
        },
        labels: {
          format: '{value}%',
        },
        opposite: true
      }, { // Primary yAxis
        labels: {
          format: '{value}',
        },
        title: {
          text: 'Count',
        }
      }],
      tooltip: {
        shared: true
      },
      legend: {
        layout: 'horizontal',
        align: 'bottom',
        x: 40,
        verticalAlign: 'right',
        y: 371,
       // floating: true,
      },
      series: [{
        name: 'No. of inquiries',
        type: 'column',
        yAxis: 1,
        data: this.assignedUserArray,
        color: '#1097ea',
      }, {
        name: 'No of Clients',
        type: 'column',
        yAxis: 1,
        data: this.clientInquiryArray,
        color: '#1e9606',
      }, {
        name: 'Lead conversion percentage',
        type: 'spline',
        data: this.averageArray,
        color: '#666666',
        tooltip: {
          valueSuffix: '%'
        }
      },
      ]

    }
  }
}
