import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router, ActivatedRoute } from '@angular/router';
import * as Highcharts from 'highcharts';
import variablepie from 'highcharts/modules/variable-pie.js';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { StorageunitService } from 'src/app/main-module/Masters -1/material-master/storage-unit/storageunit.service';
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
    name:'Occupied',
    y: 7,
    z:30
}, {
    name:  'Vacant',
    y: 5,
    z: 30
}, {
    name:  ' Under Maintenance',
    y: 4,
    z: 30
}, {
    name:  'Legal Dispute ',
    y: 3,
    z: 30
}, {
    name:  ' Double Locked ',
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
    private service: StorageunitService,
    private cas: CommonApiService,
  ) { }

  ngOnInit() {
    this.findBin();
    this.bindChart(this.array)

  }
  statusList: any[] = [];
  findBin(){
    this.service.Getall().subscribe(res => {
        res.forEach(element => {
          this.statusList.push({name: element.availability})
        });

        let key = "name"

    this.bindChart(this.cs.findOccurance(this.statusList, key))
    })
  }

  bindChart(array){
    this.chartOptions = {
        credits: false,
        chart: {
            type: 'variablepie',
            style: {
                fontFamily: 'Roboto Slab'
            }
        },
        title: {
            text: 'Storage unit status '
        },
        tooltip: {
            headerFormat: '',
            pointFormat: '<span style="color:{point.color}">\u25CF</span> <b> {point.name}</b>'+ ': ' +' <b>{point.y}</b><br/>' 
                //'Total Assignment: <b>{point.z}</b><br/>'
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
