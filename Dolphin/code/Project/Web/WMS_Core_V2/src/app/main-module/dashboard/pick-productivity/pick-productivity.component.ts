import { Component, OnInit } from '@angular/core';
import * as Highcharts from 'highcharts';
import { PickingService } from '../../Outbound/picking/picking.service';
import { AuthService } from 'src/app/core/core';
import { CommonService } from 'src/app/common-service/common-service.service';
import { DatePipe } from '@angular/common';
require('highcharts/modules/exporting')(Highcharts);
require('highcharts/modules/export-data')(Highcharts);

@Component({
  selector: 'app-pick-productivity',
  templateUrl: './pick-productivity.component.html',
  styleUrls: ['./pick-productivity.component.scss']
})
export class PickProductivityComponent implements OnInit {


  seriesArray: any[] = [{
    color: '#7CB5EC',
    name: 'Open',
    data: [7, 4, 12]
  },
  ]

  constructor(private pickingService: PickingService, private auth: AuthService, private cs: CommonService, private datepipe: DatePipe) { }

  currentMonthStartDate: any;
  startDate: any;
  ngOnInit(): void {


    let currentDate = new Date();
    let yesterdayDate = new Date();
    let currentMonthStartDate = new Date();
    yesterdayDate.setDate(currentDate.getDate() - 1);
    this.startDate = this.datepipe.transform(yesterdayDate, 'dd');
    currentMonthStartDate.setDate(currentDate.getDate() - this.startDate);
    this.currentMonthStartDate = currentMonthStartDate;


    this.picking()
    var chart = Highcharts.chart("container", this.chartOptions);


  }

  Highcharts: typeof Highcharts = Highcharts;

  chartOptions: Highcharts.Options = {

    title: {
      text: 'Picking Productivity'
    },

    yAxis: {
      title: {
        text: 'Number of Employees'
      }
    },

    xAxis: {
      accessibility: {
        rangeDescription: 'Range: 2010 to 2020'
      }
    },

    legend: {
      layout: 'vertical',
      align: 'right',
      verticalAlign: 'middle'
    },

    plotOptions: {
      series: {
        label: {
          connectorAllowed: false
        },
        pointStart: 2010
      }
    },

    series: this.seriesArray,

    responsive: {
      rules: [{
        condition: {
          maxWidth: 500
        },
        chartOptions: {
          legend: {
            layout: 'horizontal',
            align: 'center',
            verticalAlign: 'bottom'
          }
        }
      }]
    }
  }

  dataArray: any[] = [];
  picking() {
    let obj: any = {};

    obj.warehouseId = [this.auth.warehouseId];
    obj.companyCode = [this.auth.companyId];
    obj.languageId = [this.auth.languageId];
    obj.plantId = [this.auth.plantId];
    obj.fromPickConfirmedOn = '2022-08-27T12:56:08.265Z' //this.currentMonthStartDate;
    obj.toPickConfirmedOn = new Date();

    this.pickingService.pickuplineSpark(obj).subscribe(res => {
      this.dataArray = (this.cs.findOccurance1(res, "assignedPickerId", "pickConfirmQty"));
    })
  }

}
