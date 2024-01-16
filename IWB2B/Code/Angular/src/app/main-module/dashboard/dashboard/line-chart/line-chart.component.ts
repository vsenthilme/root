import { Component, OnInit } from '@angular/core';
import * as Highcharts from 'highcharts';
import HC_heatmap from 'highcharts/modules/heatmap';
HC_heatmap(Highcharts);
@Component({
  selector: 'app-line-chart',
  templateUrl: './line-chart.component.html',
  styleUrls: ['./line-chart.component.scss']
})
export class LineChartComponent implements OnInit {

  Highcharts = Highcharts;
  chartOptions = {};

 

  constructor() { }

  ngOnInit() {
    this.chartOptions = {
      credits: false,
        chart: {
            type: 'heatmap',
            style: {
                fontFamily: 'Roboto Slab'
            },
            marginTop: 40,
            marginBottom: 80,
            plotBorderWidth: 0,
            events: {
              load: function () {
                // this.myTooltip = new Highcharts.Tooltip(this, this.options.tooltip);
              },
            },
          },
          title: {
            text: 'Bin Status',
          },
          xAxis: {
            categories: [
              '11',
              '12',
              '13',
              '14',
              '15',
              '16',
              '17',
              '18',
              '19',
              '20',
            ],
            title: {
              text: 'ROWS'
          }
          },
          yAxis: {
            categories: ['1', '2', '3', '4', '5'],
            title: {
              text: 'LEVEL'
          }
          },
          colorAxis: {
            stops: [
              [0, '#3060cf'],
              [0.599999999999, '#99C5F0'],
              [0.6, '#EEF6FD'],
              [1, '#FAFCFE'],
            ],
          },
          tooltip: {
            headerFormat: '',
            // pointFormat: '<span style="color:{point.color}">\u25CF</span> <b> {categories.x}</b>' +   '</b> sold <b>' + '<b> {point.value}</b>'
            //     + '</b> items on <br><b>' + '<b> {point.y}</b><br/>'
              pointFormat: '</b> Empty <b>'
        },
          legend: {
            align: 'right',
            layout: 'vertical',
            margin: 0,
            verticalAlign: 'top',
            y: 25,
            symbolHeight: 280,
          },
          plotOptions: {
            heatmap: {
              pointPadding: 5,
            },
            series: {
              stickyTracking: false,
            },
          },
    
          series: [
            {
              name: 'Sales per employee',
              borderWidth: 1,
              data: [
                [0, 0, 111, { id: 'abc' }],
                [0, 1, 211, { id: 'abc' }],
                [0, 2, 311, { id: 'abc' }],
                [0, 3, 411],
                [0, 4, 511],
                [1, 0, 112],
                [1, 1, 212],
                [1, 2, 312],
                [1, 3, 412],
                [1, 4, 512],
                [2, 0, 113],
                [2, 1, 213],
                [2, 2, 313],
                [2, 3, 413],
                [2, 4, 513],
                [3, 0, 114],
                [3, 1, 214],
                [3, 2, 314],
                [3, 3, 414],
                [3, 4, 514],
                [4, 0, 115],
                [4, 1, 215],
                [4, 2, 315],
                [4, 3, 415],
                [4, 4, 515],
                [5, 0, 116],
                [5, 1, 216],
                [5, 2, 316],
                [5, 3, 416],
                [5, 4, 516],
                [6, 0, 117],
                [6, 1, 217],
                [6, 2, 317],
                [6, 3, 417],
                [6, 4, 517],
                [7, 0, 118],
                [7, 1, 218],
                [7, 2, 318],
                [7, 3, 418],
                [7, 4, 518],
                [8, 0, 119],
                [8, 1, 219],
                [8, 2, 319],
                [8, 3, 419],
                [8, 4, 519],
                [9, 0, 120],
                [9, 1, 220],
                [9, 2, 320],
                [9, 3, 420],
                [9, 4, 520],
              ],
              dataLabels: {
                enabled: true,
                color: '#000000',
              },
            },
          ],
    };


  }

}
