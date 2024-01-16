import {
  Component,
  OnInit
} from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router, ActivatedRoute } from '@angular/router';
import * as Highcharts from 'highcharts';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { StorageunitService } from 'src/app/main-module/Masters -1/material-master/storage-unit/storageunit.service';
import { AgreementService } from 'src/app/main-module/operation/operation/agreement/agreement.service';

@Component({
  selector: 'app-binning',
  templateUrl: './binning.component.html',
  styleUrls: ['./binning.component.scss']
})
export class BinningComponent implements OnInit {


  Highcharts = Highcharts;
  chartOptions = {};

array: any[] =   [{
    name: 'Abdul',
    data: [2, 3, 4, 5, 6, 4, 7, 9]
}]

constructor(private router: Router,
    private fb: FormBuilder,
    public toastr: ToastrService,
    public cs: CommonService,
    private spin: NgxSpinnerService,
    private auth: AuthService,
    public dialog: MatDialog,
    private route: ActivatedRoute,
    private service: AgreementService,
    private cas: CommonApiService,) {}


    statusList: any[] = [];
    finalData: any[] = [];
  ngOnInit() {
      this.getData();
this.bindChart(this.array)


  }
  dataArray:any[] = [];
  agreementStatus  = [0, 0, 0, 0, 0];
  getData(){
    this.service.search({}).subscribe(res => {
        this.cs.findOccurance2(res, "status",).forEach(x => {
            if(x.status == "Open"){this.agreementStatus.splice(0, 1, x.y);}
            if(x.status == "Renewed"){this.agreementStatus.splice(1, 1, x.y);}
            if(x.status == "Cancelled"){this.agreementStatus.splice(2, 1, x.y);}
            if(x.status == "Closed"){this.agreementStatus.splice(3, 1, x.y);}
            if(x.status == "Expired"){this.agreementStatus.splice(4, 1, x.y);}
        })
        this.finalData.push({
            name: 'Status',
            data: this.agreementStatus
        })
        this.bindChart(this.finalData)
    })
  }
bindChart(array){
    this.chartOptions = {
        credits: false,
      title: {
        text: 'Agreement Status'
    },
    

    yAxis: {
        title: {
            text: 'Value'
        },
        
    },

    xAxis: {
        categories: ['Open', 'Renewed', 'Cancelled', 'Closed', 'Expired']
    },
    tooltip: {
        headerFormat: '<b>{point.x}</b>',
        pointFormat: '<b>{point.name}</b>' + ':' + '<b>{point.y}</b>' ,
    },
    // legend: {
    //     layout: 'vertical',
    //     align: 'right',
    //     verticalAlign: 'middle'
    // },

    plotOptions: {
        series: {
            label: {
                connectorAllowed: false
            },
        }
    },

    series: array,
  

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
    };
}
}
