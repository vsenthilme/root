import { Component, OnInit } from '@angular/core';
import * as Highcharts from 'highcharts'; // import highcharts3d from "highcharts/highcharts-3d";
import highcharts3d from "highcharts/highcharts-3d";
import cylinder from "highcharts/modules/cylinder";
import exporting from 'highcharts/modules/exporting';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ExcelService } from 'src/app/common-service/excel.service';
import { AuthService } from 'src/app/core/core';
import { DashboardService } from '../../dasboard-service.service';
highcharts3d(Highcharts);
cylinder(Highcharts);
exporting(Highcharts);

enum PeriodDataEnum {
    CURRENTMONTH = 'currentMonth',
    CURRENTQUARTER = 'currentQuarter',
    PREVIOUSMONTH = 'previousMonth',
    PREVIOUSQUARTER = 'previousQuarter',
    CURRENTYEAR = 'currentYear',
    PREVIOUSYEAR = 'previousYear'
}
@Component({
    selector: 'app-line-chart',
    templateUrl: './line-chart.component.html',
    styleUrls: ['./line-chart.component.scss']
})
export class LineChartComponent implements OnInit {

    Highcharts = Highcharts;
    chartOptions = {};
    sub = new Subscription();
    data: any[] = [];

    seriesArray = [{
        name: "Jacob Monty",
        y: 75000,
        drilldown: "Jacob Monty"
    }]

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

        this.getBillingUnBillingTimeData();
        this.bindChart(this.seriesArray);
    }

    getBillingUnBillingTimeData() {
        this.sub.add(this.service.getBillableUnBillableTimeData(this.classId, this.period.toString().toUpperCase()).subscribe(res => {
            if (res[this.period] != null && res[this.period].length > 0) {
                this.data = res[this.period];
                console.log(this.data)
                let seriesArray: any[] = [];
                this.data.forEach(element => {
                    if (element[0] != "NoCharge" && element[0] != "No%20Charge") {
                        seriesArray.push({
                            name: element[0],
                            y: Number(element[1]),
                            drilldown: element[0]
                        })
                    }
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
        this.chartOptions = {};
        this.chartOptions = {
            credits: false,
            chart: {
                type: 'column',
                style: {
                    fontFamily: 'Roboto Slab'
                }
            },
            colors: [
                '#FFD944',
                '#434348',
            ],
            title: {
                text: 'Billable and Unbillable Hours'
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
                    text: 'Hours'
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
                        format: '{point.y:.1f} hrs<br/>'
                    }
                }
            },

            tooltip: {
                headerFormat: '<span style="font-size:11px">{series.y}</span><br>',
                pointFormat: '<span style="color:{point.color}">{point.name} Hrs<br/> </span>: <b>{point.y:.2f}</b>hrs<br/>'
            },

            series: [{
                name: "Data",
                colorByPoint: true,
                data: seriesArray
            }],
            exporting: false
            // exporting: {
            //     buttons: [{
            //       text: 'custom button',
            //       onclick: function () {
            //         alert(1)
            //       },
            //       theme: {
            //             'stroke-width': 1,
            //             stroke: 'silver',
            //             r: 0,
            //             states: {
            //                 hover: {
            //                     fill: '#a4edba'
            //                 },
            //                 select: {
            //                     stroke: '#039',
            //                     fill: '#a4edba'
            //                 }
            //             }
            //         }
            //     }]
            // }
            // exporting: {
            //     buttons: {
            //       contextButton: {
            //         menuItems: ["printChart", "separator", "downloadPNG", "downloadPDF"]
            //     }
            //   }
            // },
        };
    }
}
