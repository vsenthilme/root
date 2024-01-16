import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router, ActivatedRoute } from '@angular/router';
import * as Highcharts from 'highcharts';
import variablepie from 'highcharts/modules/variable-pie.js';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { JntOrdersService } from 'src/app/main-module/jnt-orders/jnt-orders.service';
variablepie(Highcharts);


@Component({
  selector: 'app-piechart',
  templateUrl: './piechart.component.html',
  styleUrls: ['./piechart.component.scss']
})
export class PiechartComponent implements OnInit {

  Highcharts = Highcharts;
  chartOptions = {};

 array= [{
    name:'Slice',
    y: 7,
    z:30
}, {
    name:  'Delivery Scan',
    y: 5,
    z: 30,
}, {
    name:  'Sign Scan',
    y: 4,
    z: 30
}, {
    name:  'Abnoarmal Parcel Scan',
    y: 3,
    z: 30
}, {
    name:  'Station Arrival',
    y: 1,
    z: 30
}, ]

constructor(private router: Router,
    private fb: FormBuilder,
    public toastr: ToastrService,
    public cs: CommonService,
    private spin: NgxSpinnerService,
    private auth: AuthService,
    public dialog: MatDialog,
    private route: ActivatedRoute,
    private service : JntOrdersService) { }

  ngOnInit() {
    this.findBin();
    this.bindChart(this.array)

  }
  statusList: any[] = [];
  total: any;
  
  findBin(){
    this.service.getAll().subscribe(res => {
        res.forEach(element => {
            if(element.awb_3rd_Party != null && element.orderType == "1"){
                element.scanType == null ? element.scanType = "Created" : '';
                this.statusList.push({name: element.scanType})
            }
        
        });

        let key = "name"
        let result = this.cs.findOccurance(this.statusList, key);
        let total = 0
        result.forEach(x => {
            total = x.y + total;
        })
    this.total = total;
    this.bindChart(this.cs.findOccurance(this.statusList, key))
    })
  }

  bindChart(array){
    this.chartOptions = {
        credits: false,
        chart: {
            type: 'pie',
            style: {
                fontFamily: 'Roboto Slab'
            }
        },
        colors: [
            '#F7A35C',
            '#F15C80',
            '#F15C80',
            '#8085E9',
        ],
        title: {
            text: ''
        },
        tooltip: {
            headerFormat: '',
            pointFormat: '<span style="color:{point.color}">\u25CF</span> <b> {point.name}</b>'+ ': ' +' <b>{point.y}</b><br/>' 
                //'Total Assignment: <b>{point.z}</b><br/>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b>: {point.y}'
                },
                showInLegend: true
            },
            
        },
        series: [{
            minPointSize: 10,
            innerSize: '20%',
            zMin: 0,
            name: 'countries',
            data: array
        }]
    };

  }
}
