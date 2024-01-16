import { Component, OnInit } from '@angular/core';
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
    selector: 'app-basic-line',
    templateUrl: './basic-line.component.html',
    styleUrls: ['./basic-line.component.scss']
})
export class BasicLineComponent implements OnInit {



    Highcharts = Highcharts;
    chartOptions: any = {};
    sub = new Subscription();
    data: any[] = [];

    categoryArray: any[] = ['L&E - Adm', 'L&E HR Conslt', 'Business IMM']

    seriesArray: any[] = [{
        color: '#7CB5EC',
        name: 'Open',
        data: [7, 4, 12]
    },
        // {
        //     color: '#90ED7D',
        //     name: 'Closed',
        //     data: [3, 4, 2]
        // }
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
        this.getPracticeBreakdown();
    }

    getPracticeBreakdown() {
        this.sub.add(this.service.getPracticeBreakdown(this.classId, this.period.toString().toUpperCase()).subscribe(res => {
            //[0] : caseCategoryId
            //[1] : caseCategoryName
            //[2] : totalInvocieAmount
            if (res[this.period] != null && res[this.period].length > 0) {
                this.data = res[this.period];
                this.categoryArray = [];
                this.seriesArray = [];
                let seriesArrayData: any[] = [];
                this.data.forEach(element => {
                    this.categoryArray.push(element[1]);
                    seriesArrayData.push(Number(element[2]));
                });
                this.seriesArray.push({
                    name: '',
                    data: seriesArrayData
                })
                this.bindChart();
            } else {
                this.categoryArray = [];
                this.seriesArray = [];
                this.bindChart();
            }
        },
            err => {
                // this.cs.commonerror(err);
                this.bindChart();
            }));
    }


    bindChart() {
        this.chartOptions = {};
        this.chartOptions = {
            credits: false,
            exporting: false,
            responsive: false,
            title: {
                text: 'Practice BreakDown by Invoice Value'
            },

            yAxis: {
                title: {
                    text: 'Invoice Amount ($)'
                }
            },

            xAxis: {
                categories: this.categoryArray
            },

            legend: {
                enabled: false,
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle'
            },

            // plotOptions: {
            //     series: {
            //         label: {
            //             connectorAllowed: true
            //         },
            //         pointStart: 2010
            //     }
            // },
            // tooltip: {
            //     headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
            //     pointFormat: '<span style="color:{point.color}">{point.data} Hrs<br/>'
            // },
            series: this.seriesArray,

            // responsive: {
            //     rules: [{
            //         condition: {
            //             maxWidth: 500
            //         },
            //         chartOptions: {
            //             legend: {
            //                 layout: 'horizontal',
            //                 align: 'center',
            //                 verticalAlign: 'bottom'
            //             }
            //         }
            //     }]
            // }

        };

    }

}
