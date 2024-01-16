import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import * as Highcharts from "highcharts/highcharts";
import Highcharts3d from "highcharts/highcharts-3d";
import HighchartsExporting from "highcharts/modules/exporting";

Highcharts3d(Highcharts);
HighchartsExporting(Highcharts);

//const Draggable = require("highcharts/modules/draggable-points.js");
//Draggable(Highcharts);



export interface  firstFloor {
    status:  string;
    qtyAvailable:  string;
    BinNo:  string;
  } 
  const ELEMENT_DATA:  firstFloor[] = [
    {BinNo:  '1000', qtyAvailable:  '223', status:  'Available',},
    {BinNo:  '1001', qtyAvailable:  '323', status:  'Filled',},
    {BinNo:  '1002', qtyAvailable:  '323', status:  'Available',},
    {BinNo:  '1003', qtyAvailable:  '32', status:  'Filled',},
    {BinNo:  '1004', qtyAvailable:  '423', status:  'Filled',},
    {BinNo:  '1005', qtyAvailable:  '323', status:  'Available',},
    {BinNo:  '1006', qtyAvailable:  '32', status:  'Filled',},
  
  ];

@Component({
  selector: 'app-bin-popup',
  templateUrl: './bin-popup.component.html',
  styleUrls: ['./bin-popup.component.scss']
})
export class BinPopupComponent implements OnInit {

  title = "app";
  chart;
  updateFromInput = false;
  Highcharts = Highcharts;
  chartConstructor = "chart";
  chartCallback;
  
  chartOptions = {};
    binPassed: any;
  ngOnInit(): void {
      
    console.log(this.data.No)

    ELEMENT_DATA.forEach(x => {
        if(x.BinNo == this.data.No){
            this.binPassed = (x)
        }
    })
console.log(this.binPassed)
  

 this.chartOpen();
 
}


chartOpen(){
    this.chartOptions = {
        chart: {
          renderTo: 'container',
          margin: 100,
          type: 'scatter3d',
          animation: false,
          options3d: {
              enabled: true,
              alpha: 10,
              beta: 30,
              depth: 250,
              viewDistance: 5,
              fitToPlot: false,
              frame: {
                  bottom: { size: 1, color: 'red' },
                  back: { size: 1, color: 'red' },
                  side: { size: 1, color: 'red' }
              }
          }
      },
      title: {
        text: 'Bin No:' +  this.binPassed.BinNo + ' ' + 'Status:' +  this.binPassed.status + ' '  + 'Qty:' +  this.binPassed.qtyAvailable
      },
    // subtitle: {
    //              text: 'Bin No:' +  this.binPassed.BinNo + ' ' + 'Status:' +  this.binPassed.status + ' '  + 'Qty:' +  this.binPassed.qtyAvailable
    // },
      plotOptions: {
          scatter: {
              width: 10,
              height: 10,
              depth: 10
          }
      },
      yAxis: {
          min: 0,
          max: 4,
          title: null
      },
      xAxis: {
          min: 0,
          max: 4,
          gridLineWidth: 1
      },
      zAxis: {
          min: 0,
          max: 4,
          showFirstLabel: false
      },
      legend: {
          enabled: false
      },
      series: [{
          name: 'Data',
          colorByPoint: false,
          accessibility: {
              exposeAsGroupOnly: false
          },
          data: [
            //   [1, 6, 5], [8, 7, 9], [1, 3, 4], [4, 6, 8], [5, 7, 7], [6, 9, 6],
            //   [7, 0, 5], [2, 3, 3], [3, 9, 8], [3, 6, 5], [4, 9, 4], [2, 3, 3],
            //   [6, 9, 9], [0, 7, 0], [7, 7, 9], [7, 2, 9], [0, 6, 2], [4, 6, 7],
            //   [3, 7, 7], [0, 1, 7], [2, 8, 6], [2, 3, 7], [6, 4, 8], [3, 5, 9],
            //   [7, 9, 5], [3, 1, 7], [4, 4, 2], [3, 6, 2], [3, 1, 6], [6, 8, 5],
            //   [6, 6, 7], [4, 1, 1], [7, 2, 7], [7, 7, 0], [8, 8, 9], [9, 4, 1],
            //   [8, 3, 4], [9, 8, 9], [3, 5, 3], [0, 2, 4], [6, 0, 2], [2, 1, 3],
            //   [5, 8, 9], [2, 1, 1], [9, 7, 6], [3, 0, 2], [9, 9, 0], [3, 4, 8],
            //   [2, 6, 1], [8, 9, 2], [7, 6, 5], [6, 3, 1], [9, 3, 1], [8, 9, 3],
            //   [9, 1, 0], [3, 8, 7], [8, 0, 0], [4, 9, 7], [8, 6, 2], [4, 3, 0],
            //   [2, 3, 5], [9, 1, 4], [1, 1, 4], [6, 0, 2], [6, 1, 6], [3, 8, 8],
            //   [8, 8, 7], [5, 5, 0], [3, 9, 6], [5, 4, 3], [6, 8, 3], [0, 1, 5],
            //   [6, 7, 3], [8, 3, 2], [3, 8, 3], [2, 1, 6], [4, 6, 7], [8, 9, 9],
            //   [5, 4, 2], [6, 1, 3], [6, 9, 5], [4, 8, 2], [9, 7, 4], [5, 4, 2],
            //   [9, 6, 1], [2, 7, 3], [4, 5, 4], [6, 8, 1], [3, 4, 0], [2, 2, 6],
            //   [5, 1, 2], [9, 9, 7], [6, 9, 9], [8, 4, 3], [4, 1, 7], [6, 2, 5],
            //   [0, 4, 9], [3, 5, 9], [6, 9, 1], [1, 9, 2]
            ]
      }]
      }
}

constructor(
    public dialogRef: MatDialogRef<any>,
    @Inject(MAT_DIALOG_DATA) public data: any,
) {
  const self = this;
  this.chartCallback = chart => {
    self.chart = chart;
    this.addChartRotation();
  };
}


addChartRotation() {
  const chart = this.chart;
  const H = this.Highcharts;

  function dragStart(eStart) {
      eStart = chart.pointer.normalize(eStart);

      var posX = eStart.chartX,
          posY = eStart.chartY,
          alpha = chart.options.chart.options3d.alpha,
          beta = chart.options.chart.options3d.beta,
          sensitivity = 5,  // lower is more sensitive
          handlers: any[] = [];

      function drag(e) {
          // Get e.chartX and e.chartY
          e = chart.pointer.normalize(e);

          chart.update({
              chart: {
                  options3d: {
                      alpha: alpha + (e.chartY - posY) / sensitivity,
                      beta: beta + (posX - e.chartX) / sensitivity
                  }
              }
          }, undefined, undefined, false);
      }

      function unbindAll() {
          handlers.forEach(function (unbind) {
              if (unbind) {
                  unbind();
              }
          });
          handlers.length = 0;
      }

      handlers.push(H.addEvent(document, 'mousemove', drag));
      handlers.push(H.addEvent(document, 'touchmove', drag));


      handlers.push(H.addEvent(document, 'mouseup', unbindAll));
      handlers.push(H.addEvent(document, 'touchend', unbindAll));
  }
  H.addEvent(chart.container, 'mousedown', dragStart);
  H.addEvent(chart.container, 'touchstart', dragStart);

  (Highcharts);
}
}
