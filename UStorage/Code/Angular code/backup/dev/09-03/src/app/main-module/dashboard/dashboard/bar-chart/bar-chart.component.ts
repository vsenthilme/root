import { Component, Injectable, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import * as Highcharts from 'highcharts';

import Drilldown from 'highcharts/modules/drilldown';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { ReportsService } from 'src/app/main-module/reports/reports.service';
Drilldown(Highcharts);

@Component({
  selector: 'app-bar-chart',
  templateUrl: './bar-chart.component.html',
  styleUrls: ['./bar-chart.component.scss']
})

@Injectable({
    providedIn: 'root'
  })

  
export class BarChartComponent implements OnInit {

    constructor(
        private router: Router,
        private service: ReportsService,
        private fb: FormBuilder,
        public toastr: ToastrService,
        public cs: CommonService,
        private spin: NgxSpinnerService,
        private auth: AuthService,
        public dialog: MatDialog,
        private route: ActivatedRoute,
        private cas: CommonApiService,
        //  private route: ActivatedRoute
      ) { }
  
  Highcharts = Highcharts;
  chartOptions = {};

 
array: any[] = 
[
            {
                name: "7 ",
                y: 0,
                // formatter: function() {
                //     this.router.navigate(['/main/masters/Alternate-uom']);
                // },
                drilldown: "Item 1"
            },
            {
                name: "15",
                y: 0,
                drilldown: "Item 2"
            },
            {
                name: "30 ",
                y: 0,
                drilldown: "Item 3"
            },
            {
                name: "45",
                y: 0,
                drilldown: "Item 3"
            },
            {
                name: "60 ",
                y: 0,
                drilldown: "Item 4"
            }
        ]
sub = new Subscription();
lessThanSeven= 0;
lessThanFourteen= 0;
lessThanTweentyOne= 0;
lessThanTweentyEight= 0;
greaterThanTweentyEight= 0;
dataList: any[] = [];
  ngOnInit() {
    this.getData()
   this.bindChart(this.array)
  }
  getData(){
    this.sub.add(this.service.getpaymentdue({}).subscribe(res => {
        res.forEach(element => {
            if(parseInt(element.dueDays) < 7){
              //  this.lessThanSeven.push(element)
                this.lessThanSeven = this.lessThanSeven + (element.totalAfterDiscount != null ? element.totalAfterDiscount : 0.00);
                
            };
            if(parseInt(element.dueDays) > 7 && parseInt(element.dueDays) < 14){
               // this.lessThanFourteen.push(element.dueDays)
                this.lessThanFourteen = this.lessThanFourteen + (element.totalAfterDiscount != null ? parseInt(element.totalAfterDiscount) : 0.00);
            };
            if(parseInt(element.dueDays) >= 14 && parseInt(element.dueDays) < 21){
                //this.greaterThanTweentyEight.push(element.dueDays)
                this.lessThanTweentyOne = this.lessThanTweentyOne + (element.totalAfterDiscount != null ? parseInt(element.totalAfterDiscount) : 0.00);
            };
            if(parseInt(element.dueDays) >= 21 && parseInt(element.dueDays) < 28){
            //  this.lessThanTweentyEight.push(element.dueDays)
               this.lessThanTweentyEight = this.lessThanTweentyEight + (element.totalAfterDiscount != null ? parseInt(element.totalAfterDiscount) : 0.00);
            };
            if(parseInt(element.dueDays) >= 28){
              //  this.greaterThanTweentyEight.push(element.dueDays)
                this.greaterThanTweentyEight = this.greaterThanTweentyEight + (element.totalAfterDiscount != null ? parseInt(element.totalAfterDiscount) : 0.00);
            };
        });
        this.dataList.push(
            {
                name: "0 < 7",
                y: this.lessThanSeven,
            },
            {
                name: "8 < 14",
                y: this.lessThanFourteen,
            },
            {
                name: "15 < 21",
                y: this.lessThanTweentyOne,
            },
            {
                name: "22 < 28",
                y: this.lessThanTweentyEight,
            },
            {
                name: "> 28",
                y: this.greaterThanTweentyEight,
            },
        )
       this.bindChart(this.dataList)
       console.log(this.dataList)
    }))
  }
  bindChart(array){
    this.chartOptions = {
        credits: false,
        chart: {
            type: 'column'
        },
        title: {
            text: 'Payment Due Status '
        },
        accessibility: {
            announceNewData: {
                enabled: true
            }
        },
        xAxis: {
            type: 'category',
            title: {
                text: 'Due Days'
            }
        },
        yAxis: {
            title: {
                text: 'Due Amount'
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
                  //  format: '{point.y:.1f}'
                }
            }
        },
    
        tooltip: {
            headerFormat: '<b>{point.name}</b>',
            pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b><br/>'
        },
    
        series: [
            {
                name: "Payment Due",
                colorByPoint: true,
                data: array
            }]
    };
  }

}


// drilldown: {
//     breadcrumbs: {
//         position: {
//             align: 'right'
//         }
//     },
//     series: [
//         {
//             name: "Item 1",
//             id: "Item 1",
//             data: [
//                 [
//                     "v65.0",
//                     0.1
//                 ],
//                 [
//                     "v64.0",
//                     1.3
//                 ],
//                 [
//                     "v63.0",
//                     53.02
//                 ],
//                 [
//                     "v62.0",
//                     1.4
//                 ],
//                 [
//                     "v61.0",
//                     0.88
//                 ],
//                 [
//                     "v60.0",
//                     0.56
//                 ],
//                 [
//                     "v59.0",
//                     0.45
//                 ],
//                 [
//                     "v58.0",
//                     0.49
//                 ],
//                 [
//                     "v57.0",
//                     0.32
//                 ],
//                 [
//                     "v56.0",
//                     0.29
//                 ],
//                 [
//                     "v55.0",
//                     0.79
//                 ],
//                 [
//                     "v54.0",
//                     0.18
//                 ],
//                 [
//                     "v51.0",
//                     0.13
//                 ],
//                 [
//                     "v49.0",
//                     2.16
//                 ],
//                 [
//                     "v48.0",
//                     0.13
//                 ],
//                 [
//                     "v47.0",
//                     0.11
//                 ],
//                 [
//                     "v43.0",
//                     0.17
//                 ],
//                 [
//                     "v29.0",
//                     0.26
//                 ]
//             ]
//         },
//         {
//             name: "Item 2",
//             id: "Item 2",
//             data: [
//                 [
//                     "v58.0",
//                     1.02
//                 ],
//                 [
//                     "v57.0",
//                     7.36
//                 ],
//                 [
//                     "v56.0",
//                     0.35
//                 ],
//                 [
//                     "v55.0",
//                     0.11
//                 ],
//                 [
//                     "v54.0",
//                     0.1
//                 ],
//                 [
//                     "v52.0",
//                     0.95
//                 ],
//                 [
//                     "v51.0",
//                     0.15
//                 ],
//                 [
//                     "v50.0",
//                     0.1
//                 ],
//                 [
//                     "v48.0",
//                     0.31
//                 ],
//                 [
//                     "v47.0",
//                     0.12
//                 ]
//             ]
//         },
//         {
//             name: "Item 3",
//             id: "Item 3",
//             data: [
//                 [
//                     "v11.0",
//                     6.2
//                 ],
//                 [
//                     "v10.0",
//                     0.29
//                 ],
//                 [
//                     "v9.0",
//                     0.27
//                 ],
//                 [
//                     "v8.0",
//                     0.47
//                 ]
//             ]
//         },
//         {
//             name: "Item 4",
//             id: "Item 4",
//             data: [
//                 [
//                     "v11.0",
//                     3.39
//                 ],
//                 [
//                     "v10.1",
//                     0.96
//                 ],
//                 [
//                     "v10.0",
//                     0.36
//                 ],
//                 [
//                     "v9.1",
//                     0.54
//                 ],
//                 [
//                     "v9.0",
//                     0.13
//                 ],
//                 [
//                     "v5.1",
//                     0.2
//                 ]
//             ]
//         },
//         {
//             name: "Item 5",
//             id: "Item 5",
//             data: [
//                 [
//                     "v16",
//                     2.6
//                 ],
//                 [
//                     "v15",
//                     0.92
//                 ],
//                 [
//                     "v14",
//                     0.4
//                 ],
//                 [
//                     "v13",
//                     0.1
//                 ]
//             ]
//         },
//         {
//             name: "Item 6",
//             id: "Item 6",
//             data: [
//                 [
//                     "v50.0",
//                     0.96
//                 ],
//                 [
//                     "v49.0",
//                     0.82
//                 ],
//                 [
//                     "v12.1",
//                     0.14
//                 ]
//             ]
//         },
//         {
//             name: "Other",
//             id: "Other",
//             data: [
//                 [
//                     "v50.0",
//                     0.96
//                 ],
//                 [
//                     "v49.0",
//                     0.82
//                 ],
//                 [
//                     "v12.1",
//                     0.14
//                 ]
//             ]
//         }
//     ]
// }