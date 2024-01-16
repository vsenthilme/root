
  import {
    Component,
    Injectable,
    OnInit
  } from '@angular/core';
  import * as Highcharts from 'highcharts';
  import { NgxSpinnerService } from 'ngx-spinner';
  import { ToastrService } from 'ngx-toastr';
  import { Subscription } from 'rxjs';
  import { CommonApiService } from 'src/app/common-service/common-api.service';
  import { CommonService } from 'src/app/common-service/common-service.service';
  import { AuthService } from 'src/app/core/core';
import { DashboardService } from '../dashboard.service';
  
@Injectable({
  providedIn: 'root'
})


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
  
    categories: any[] = [    
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
  ];
  
    // leadArray = [7.0, 6.9, 9.5, 14.5, 18.2, 21.5];
    // customerArray = [3.0, 5.9, 1.5, 6.5, 7.2, 9.5];
    array = [{
      name: 'No. of Lead',
      type: 'column',
      yAxis: 1,
      data: [0,0,0,0,0,0,0,0,0,0,0,0],
      color: '#1097ea',
    }, {
      name: 'No of Customers',
      type: 'column',
      yAxis: 1,
      data: [0,0,0,0,0,0,0,0,0,0,0,0],
      color: '#1e9606',
    }, 
    ]
    leadArray: any[] = [];
    customerArray: any[] = [];
    dataList: any[] = [];
  
    constructor(
      private service: DashboardService,
      private cs: CommonService,
      private spin: NgxSpinnerService,
      public toastr: ToastrService,
      private cas: CommonApiService,
      private auth: AuthService
    ) { }
    

    year = new Date().getFullYear();
    ngOnInit() {
      this.yearValue();
      this.bindChart(this.array);
      this.getLeadConvesion(new Date().getFullYear());
    }
    onSelectEvent(e){ this.getLeadConvesion(e)}
    yearValueList: any[] = [];
    yearValue() {
      for (let i = new Date().getFullYear() - 2; i < new Date().getFullYear() + 1; i++) {
        this.yearValueList.push({value: i, label: i })
      }
     
      this.yearValueList.sort((a, b) => (a.value < b.value) ? 1 : -1);
    }


    getLeadConvesion(year) {
      this.spin.show();
      this.dataList = [];
      this.sub.add(this.service.leadCustomer(year).subscribe(res => {
        // this.leadArray.push((res.lead));
        // this.customerArray.push((res.customer));

        this.dataList.push(
          {
            name: 'No. of Lead',
            type: 'column',
            yAxis: 1,
            data: res.lead,
            color: '#1097ea',
          }, {
            name: 'No of Customers',
            type: 'column',
            yAxis: 1,
            data: res.customer,
            color: '#1e9606',
          }, 
        )
      this.bindChart(this.dataList);
      this.spin.hide();
      },
       ));
    }
  
    bindChart(array) {
      console.log(array);
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
        series: array
  
      }
    }
  }
  