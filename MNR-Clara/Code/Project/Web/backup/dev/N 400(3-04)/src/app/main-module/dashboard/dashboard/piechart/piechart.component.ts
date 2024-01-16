import { Component, OnInit } from '@angular/core';
import * as Highcharts from 'highcharts';
import variablepie from 'highcharts/modules/variable-pie.js';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ExcelService } from 'src/app/common-service/excel.service';
import { AuthService } from 'src/app/core/core';
import { DashboardService } from '../../dasboard-service.service';
variablepie(Highcharts);

enum TimeKeeperDataEnum {
    PARTNERS = 'partners',
    ORIGINATINGTIMEKEEPERS = 'originatingTimekeepers',
    RESPONSIBLETIMEKEEPERS = 'responsibleTimekeepers',
    LEGALASSISTANTS = 'legalAssistants'
}

@Component({
    selector: 'app-piechart',
    templateUrl: './piechart.component.html',
    styleUrls: ['./piechart.component.scss']
})
export class PiechartComponent implements OnInit {

    Highcharts = Highcharts;
    chartOptions = {};
    sub = new Subscription();
    data: any[] = [];

    seriesArray = [{
        name: 'Maricella',
        y: 7,
        z: 30
    },
    {
        name: 'Client',
        y: 19,
        z: 60
    }]

    timeKeeperEnum = TimeKeeperDataEnum;
    timeKeeper = TimeKeeperDataEnum.RESPONSIBLETIMEKEEPERS;
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
        this.getCaseAssignment();
        this.bindChart(this.seriesArray);
    }

    getCaseAssignment() {
        this.sub.add(this.service.getCaseAssignment(this.classId, this.timeKeeper.toString().toUpperCase()).subscribe(res => {
            if (res[this.timeKeeper] != null && res[this.timeKeeper].length > 0) {
                this.data = res[this.timeKeeper];
                let seriesArray: any[] = [];
                console.log(this.data)
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
        this.chartOptions = {
            credits: false,
            exporting: false,
            chart: {
                type: 'pie',
                style: {
                    fontFamily: 'Roboto Slab'
                }
            },
            colors: [
                '#F7A35C',
                '#F15C80',
                '#8085E9',
                '#5DBA4A'
            ],
            title: {
                text: 'Case Assignment'
            },
            tooltip: {
                headerFormat: '',
                pointFormat: '<span style="color:{point.color}">\u25CF</span> <b> {point.name}</b><br/>' +
                    'Case Assignment: <b>{point.y}</b><br/>'
            },
            series: [{
                minPointSize: 10,
                innerSize: '20%',
                zMin: 0,
                name: '',
                data: seriesArray
            }],
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true
                    },
                    showInLegend: true
                }
            },
        };
    }
}
