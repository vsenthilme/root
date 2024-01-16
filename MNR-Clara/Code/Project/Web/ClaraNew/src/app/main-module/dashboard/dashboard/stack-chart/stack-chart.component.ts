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
    selector: 'app-stack-chart',
    templateUrl: './stack-chart.component.html',
    styleUrls: ['./stack-chart.component.scss']
})
export class StackChartComponent implements OnInit {


    Highcharts = Highcharts;
    chartOptions = {};
    sub = new Subscription();
    data: any[] = [];

    periodEnum = PeriodDataEnum;
    period = PeriodDataEnum.CURRENTMONTH;
    classId = 1;

    dataArray = [
        {
            name: 'Social Media',
            y: 45,
            dataLabels: {
                enabled: false
            }
        },
        {
            name: 'Website',
            y: 25,
            dataLabels: {
                enabled: false
            }
        },
        {
            name: 'Client Referral',
            y: 12,
            dataLabels: {
                enabled: false
            }
        },
        {
            name: 'Family/Friend Referral',
            y: 8,
            dataLabels: {
                enabled: false
            }
        },
        {
            name: 'In-Person Events',
            y: 6,
            dataLabels: {
                enabled: false
            }
        },
        {
            name: 'Smart bit System',
            y: 4,
            dataLabels: {
                enabled: false
            }
        }]

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
        this.bindChart(this.dataArray);
        this.getClientReferralData();
    }

    getClientReferralData() {
        this.sub.add(this.service.getClientReferralData(this.classId, this.period.toString().toUpperCase()).subscribe((res: any) => {
            //[0] : referralId
            //[1] : referralName
            //[2] : Count
            //[3] : Calculated Average
            console.log(res[this.period])
            if (res[this.period] != null && res[this.period].length > 0) {
                this.data = res[this.period];
                this.dataArray = [];
                this.data.forEach(element => {
                    this.dataArray.push({
                        name: element[1],
                        y: Number(element[3]),
                        dataLabels: {
                            enabled: false
                        }
                    })
                });
                this.bindChart(this.dataArray);
            } else {
                this.dataArray = [];
                this.bindChart(this.dataArray);
            }

        },
            err => {
                // this.cs.commonerror(err);
                this.bindChart(this.dataArray);
            }));
    }

    bindChart(dataArray: any) {
        this.chartOptions = {};
        this.chartOptions = {
            credits: false,
            exporting: false,
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: 0,
                plotShadow: false
            },
            colors: [
                '#F7A35C',
                '#90ED7D',
                '#434348',
                '#8085E9',
            ],
            title: {
                text: 'Client Referral',
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            },
            accessibility: {
                point: {
                    valueSuffix: '%'
                }
            },
            plotOptions: {
                pie: {
                    dataLabels: {
                        enabled: true,
                        distance: -50,
                        style: {
                            fontWeight: 'bold',
                            color: 'white'
                        }
                    },
                    showInLegend: true,
                    startAngle: -90,
                    endAngle: 90,
                    center: ['50%', '125%'],
                    size: '250%',
                }
            },
            series: [{
                type: 'pie',
                name: 'Data',
                innerSize: '50%',
                data: dataArray,           }]
        };
    }

}
