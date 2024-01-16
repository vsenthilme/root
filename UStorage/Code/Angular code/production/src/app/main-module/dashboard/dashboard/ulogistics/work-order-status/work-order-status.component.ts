
  import { Component, OnInit } from '@angular/core';
  import { FormBuilder } from '@angular/forms';
  import { MatDialog } from '@angular/material/dialog';
  import { Router, ActivatedRoute } from '@angular/router';
  import * as Highcharts from 'highcharts';
  import { NgxSpinnerService } from 'ngx-spinner';
  import { ToastrService } from 'ngx-toastr';
  import { CommonApiService } from 'src/app/common-service/common-api.service';
  import { CommonService } from 'src/app/common-service/common-service.service';
  import { AuthService } from 'src/app/core/core';
  import { WorkorderService } from 'src/app/main-module/operation/operation/workorder/workorder.service';
  @Component({
    selector: 'app-work-order-status',
    templateUrl: './work-order-status.component.html',
    styleUrls: ['./work-order-status.component.scss']
  })
  export class WorkOrderStatusComponent implements OnInit {
  
    
    Highcharts = Highcharts;
    chartOptions = {};
  
    array= [{
      name: 'Assigned',
      data: [1, 2, 3, 4, 7]
  }, {
      name: 'Awaiting for Parts',
      data: [10, 11, 12, 23, 6]
  }, {
      name: 'Cancelled',
      data: [6, 7, 8, 9, 4]
  },
  {
      name: 'Completed',
      data: [12, 17, 28, 9, 12]
  } ,
  {
      name: 'On Hold',
      data: [16, 7, 8, 9, 2]
  } ,
  {
      name: 'Planned',
      data: [32, 7, 8, 9, 12]
  },
  {
      name: 'WIP',
      data: [16, 7, 8, 6, 13]
  } ]
  
  constructor(private router: Router,
      private fb: FormBuilder,
      public toastr: ToastrService,
      public cs: CommonService,
      private spin: NgxSpinnerService,
      private auth: AuthService,
      public dialog: MatDialog,
      private route: ActivatedRoute,
      private service: WorkorderService,
      private cas: CommonApiService,
    ) { }
  
    ngOnInit() {
      this.getData()
      this.bindChart(this.array)
  
  
    }
    dataList: any[] = []; 
    dataArray: any;
  
    plannedArray = [0, 0, 0, 0, 0];
    completedArray  = [0, 0, 0, 0, 0];
    onholdArray   = [0, 0, 0, 0, 0];
    assignedArray   = [0, 0, 0, 0, 0];
    wipArray   = [0, 0, 0, 0, 0];
    awaitingArray   = [0, 0, 0, 0, 0];
    cancelledArray   = [0, 0, 0, 0, 0];
    getData(){
      this.service.search({}).subscribe(res => {
          this.dataArray = (this.cs.findOccurance1(res, "status", "jobCardType"));
  
          this.dataArray.forEach(element => {
              if(element.status == "Planned"){
                  if(element.jobCardType == "Villa"){
                   //this.plannedArray.push(element.y)  
               this.plannedArray.splice(0, 1, element.y);
                  }   
                  if(element.jobCardType == "Flat Apartment"){
                      //this.plannedArray.push(element.y)
                      this.plannedArray.splice(1, 1, element.y);
                  }
                  if(element.jobCardType == "Warehouse"){
                    //  this.plannedArray.push(element.y)
                    this.plannedArray.splice(2, 1, element.y);
                  }
                  if(element.jobCardType == "Commercial Space"){
                   //   this.plannedArray.push(element.y)
                      this.plannedArray.splice(3, 1, element.y);
                  }
                  if(element.jobCardType == "Studio"){
                     // this.plannedArray.push(element.y)
                      this.plannedArray.splice(4, 1, element.y);
                  }
              }
              if(element.status == "Completed"){
                  if(element.jobCardType == "Villa"){
                     // this.completedArray.push(element.y)   
                     this.completedArray.splice(0, 1, element.y);
                  }   
                  if(element.jobCardType == "Flat Apartment"){
                    //  this.completedArray.push(element.y)
                      this.completedArray.splice(1, 1, element.y);
                  }
                  if(element.jobCardType == "Warehouse"){
                    //  this.completedArray.push(element.y)
                      this.completedArray.splice(2, 1, element.y);
                  }
                  if(element.jobCardType == "Commercial Space"){
                     // this.completedArray.push(element.y)
                      this.completedArray.splice(3, 1, element.y);
                  }
                  if(element.jobCardType == "Studio"){
                    //  this.completedArray.push(element.y)
                      this.completedArray.splice(4, 1, element.y);
                  }
              }
              if(element.status == "On-hold"){
                  if(element.jobCardType == "Villa"){
                  //    this.onholdArray.push(element.y);
                  this.onholdArray.splice(0, 1, element.y);   
                  }   
                  if(element.jobCardType == "Flat Apartment"){
                   //   this.onholdArray.push(element.y)
                      this.onholdArray.splice(1, 1, element.y);  
                  }
                  if(element.jobCardType == "Warehouse"){
                     // this.onholdArray.push(element.y)
                      this.onholdArray.splice(2, 1, element.y);  
                  }
                  if(element.jobCardType == "Commercial Space"){
                   //   this.onholdArray.push(element.y)
                      this.onholdArray.splice(3, 1, element.y);  
                  }
                  if(element.jobCardType == "Studio"){
                     // this.onholdArray.push(element.y)
                      this.onholdArray.splice(4, 1, element.y);  
                  }
              }
              if(element.status == "Assigned"){
                  if(element.jobCardType == "Villa"){
                    //  this.assignedArray.push(element.y) 
                      this.assignedArray.splice(0, 1, element.y);    
                  }   
                  if(element.jobCardType == "Flat Apartment"){
                   //   this.assignedArray.push(element.y)
                      this.assignedArray.splice(1, 1, element.y); 
                  }
                  if(element.jobCardType == "Warehouse"){
                    //  this.assignedArray.push(element.y)
                      this.assignedArray.splice(2, 1, element.y); 
                  }
                  if(element.jobCardType == "Commercial Space"){
                    //  this.assignedArray.push(element.y)
                      this.assignedArray.splice(3, 1, element.y); 
                  }
                  if(element.jobCardType == "Studio"){
                    //  this.assignedArray.push(element.y)
                      this.assignedArray.splice(4, 1, element.y); 
                  }
              }
              if(element.status == "WIP"){
                  if(element.jobCardType == "Villa"){
                     // this.wipArray.push(element.y)   
                      this.wipArray.splice(0, 1, element.y); 
                  }   
                  if(element.jobCardType == "Flat Apartment"){
                    //  this.wipArray.push(element.y)
                      this.wipArray.splice(1, 1, element.y); 
                  }
                  if(element.jobCardType == "Warehouse"){
                    //  this.wipArray.push(element.y)
                      this.wipArray.splice(2, 1, element.y); 
                  }
                  if(element.jobCardType == "Commercial Space"){
                      //this.wipArray.push(element.y)
                      this.wipArray.splice(3, 1, element.y); 
                  }
                  if(element.jobCardType == "Studio"){
                     // this.wipArray.push(element.y)
                      this.wipArray.splice(4, 1, element.y); 
                  }
              }
              if(element.status == "Awaiting for parts"){ 
                  if(element.jobCardType == "Villa"){
                     // this.awaitingArray.push(element.y)   
                      this.awaitingArray.splice(0, 1, element.y); 
                  }   
                  if(element.jobCardType == "Flat Apartment"){
                     // this.awaitingArray.push(element.y)
                      this.awaitingArray.splice(1, 1, element.y); 
                  }
                  if(element.jobCardType == "Warehouse"){
                      //this.awaitingArray.push(element.y)
                      this.awaitingArray.splice(2, 1, element.y); 
                  }
                  if(element.jobCardType == "Commercial Space"){
                      //this.awaitingArray.push(element.y)
                      this.awaitingArray.splice(3, 1, element.y); 
                  }
                  if(element.jobCardType == "Studio"){
                     // this.awaitingArray.push(element.y)
                      this.awaitingArray.splice(4, 1, element.y); 
                  }
              }  
              if(element.status == "Cancelled"){ 
                  if(element.jobCardType == "Villa"){
                     // this.awaitingArray.push(element.y)   
                      this.cancelledArray.splice(0, 1, element.y); 
                  }   
                  if(element.jobCardType == "Flat Apartment"){
                     // this.awaitingArray.push(element.y)
                      this.cancelledArray.splice(1, 1, element.y); 
                  }
                  if(element.jobCardType == "Warehouse"){
                      //this.awaitingArray.push(element.y)
                      this.cancelledArray.splice(2, 1, element.y); 
                  }
                  if(element.jobCardType == "Commercial Space"){
                      //this.awaitingArray.push(element.y)
                      this.cancelledArray.splice(3, 1, element.y); 
                  }
                  if(element.jobCardType == "Studio"){
                     // this.awaitingArray.push(element.y)
                      this.cancelledArray.splice(4, 1, element.y); 
                  }
              }  
          });
      this.dataList.push(
      {name: 'Assigned',
      data: this.assignedArray},
      {name: 'Awaiting for Parts',
      data: this.awaitingArray},
      {name: 'Cancelled',
      data: this.cancelledArray},
      {name: 'Completed',
      data: this.completedArray},
      {name: 'On Hold',
      data: this.onholdArray},
      {name: 'Planned',
      data: this.plannedArray},
      {name: 'WIP',
      data: this.wipArray})
  
      this.bindChart(this.dataList)
  
              })}
  
    bindChart(array){
      this.chartOptions = {
          credits: false,
          chart: {
              type: 'spline'
          },
          title: {
              text: 'Work order status'
          },
          xAxis: {
              categories: ['Villa', 'Flat Apartment', 'Warehouse', 'Commercial Space', 'Others'],
          },
          yAxis: {
              title: {
                  text: 'Status'
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
          
          series: array
      };
    }
  }
  