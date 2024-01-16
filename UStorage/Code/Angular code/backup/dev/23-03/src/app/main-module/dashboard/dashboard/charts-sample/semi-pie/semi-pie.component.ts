

  import { Component, OnInit } from '@angular/core';
  import * as Highcharts from 'highcharts';// import highcharts3d from "highcharts/highcharts-3d";
  import cylinder from "highcharts/modules/cylinder";
  import highcharts3d from "highcharts/highcharts-3d";
  highcharts3d(Highcharts);
  cylinder(Highcharts);
  import lollipop from 'highcharts/modules/lollipop.js';
  //lollipop(Highcharts);
  
  
  //dumbbell(Highcharts)
  
  //let indicators = require('highcharts/modules/lollipop');
  
  // import item from 'highcharts/modules/item-series.js';
  // item(Highcharts);
  
  
  // import highcharts3d from "highcharts/highcharts-3d";
  // import cylinder from "highcharts/modules/cylinder";
  
  // highcharts3d(Highcharts);
  // cylinder(Highcharts);


  @Component({
    selector: 'app-semi-pie',
    templateUrl: './semi-pie.component.html',
    styleUrls: ['./semi-pie.component.scss']
  })
  export class SemiPieComponent implements OnInit {
  
    Highcharts = Highcharts;
    chartOptions = {};
  
   
  
    constructor() { }
  
    ngOnInit() {
      this.chartOptions = {
          credits: false,
          chart: {
            type: 'line',
            backgroundColor: 'transparent'
        },
        title: {
            text: 'SHIPPING'
        },
        xAxis: {
            categories: ['TV Wh', 'Shuwaikh', 'Alrai', 'Egaila', 'Corner']
        },
        yAxis: {
            title: {
                text: 'Count'
            }
        },
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true
                },
                enableMouseTracking: true
            }
        },
        series: [{
            name: 'Normal',
            data: [3,5,13,10,0],
            point: {
              events: {
                click: function (event: any,) {
                 // location.href = 'http://localhost:4200/#/main/dashboard/shipping' ; 
                  location.href = 'http://27.7.57.232:8000/classicwms/#/main/dashboard/shipping' ; 
            
                }
              }
            }
           
          //  color: "#90ED7D"
        }, {
            name: 'Special',
            data: [0,2,3,2,3],
            point: {
              events: {
                click: function (event: any,) {
                 // location.href = 'http://localhost:4200/#/main/dashboard/special-order' ; 
                  location.href = 'http://27.7.57.232:8000/classicwms/#/main/dashboard/special-order' ; 
            
                }
              }
            }
         
        }]
      };
  
  
    }
  
  }
  