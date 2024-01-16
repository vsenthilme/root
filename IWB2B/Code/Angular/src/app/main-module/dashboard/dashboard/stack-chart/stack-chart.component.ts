import { Component, OnInit } from '@angular/core';
import * as Highcharts from 'highcharts';
@Component({
  selector: 'app-stack-chart',
  templateUrl: './stack-chart.component.html',
  styleUrls: ['./stack-chart.component.scss']
})
export class StackChartComponent implements OnInit {

  
  Highcharts = Highcharts;
  chartOptions = {};

 

  constructor() { }

  ngOnInit() {
    this.chartOptions = {
        credits: false,
        chart: {
            type: 'spline'
        },
        title: {
            text: 'Productivity - Picking'
        },
        xAxis: {
            categories: ['Hour 1', 'Hour 2', 'Hour 3', 'Hour 4', 'Hour 5','Hour 6','Hour 7','Hour 8'],
        },
        yAxis: {
            title: {
                text: 'PICKING'
            },
        },
        tooltip: {
            headerFormat: '<b>{point.x}</b><br/>',
            pointFormat: '{series.name}: {point.y}'
        },
        plotOptions: {
            spline: {
                marker: {
                    radius: 4,
                    lineColor: '#666666',
                    lineWidth: 1
                }
            }
        },
        
        series: [{
            name: 'Abdul',
            data: [1, 2, 3, 4, 5, 4, 3, 1]
        }, {
            name: 'Salim',
            data: [10, 11, 12, 13, 8, 7, 6, 5]
        }, {
            name: 'Dhanish',
            data: [6, 7, 8, 9, 10, 9, 8, 7]
        }, {
            name: 'Akbar',
            data: [1, 2, 3, 4, 5, 6, 4, 5]
        }, {
            name: 'Aazir',
            data: [4, 5, 6, 4, 3, 2, 1, 0]
        }],
    };


  }

}
