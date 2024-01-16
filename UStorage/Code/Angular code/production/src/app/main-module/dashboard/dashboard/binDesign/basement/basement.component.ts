import { ThrowStmt } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { StorageunitService } from 'src/app/main-module/Masters -1/material-master/storage-unit/storageunit.service';
import { AgreementService } from 'src/app/main-module/operation/operation/agreement/agreement.service';
import { ReportsService } from 'src/app/main-module/reports/reports.service';

@Component({
  selector: 'app-basement',
  templateUrl: './basement.component.html',
  styleUrls: ['./basement.component.scss']
})
export class BasementComponent implements OnInit {
  showDetailsBox: boolean;

  constructor(public service: StorageunitService, public agreementService: AgreementService, public reportService: ReportsService,
    private spin: NgxSpinnerService,
    public toastr: ToastrService,
    private cas: CommonApiService,) { }

  storeNumbers: any[] = [];
  ngOnInit(): void {
    this.service.search({}).subscribe(res => {
      //res.sort((a, b) => (a.codeId > b.codeId) ? 1 : -1);
      this.storeNumbers = res;
      console.log(this.storeNumbers);
      this.dropdownlist();
    })
  }
  startDate: any;
  endDate: any;
  agreementNo: any;
  statusID: any;
  customerName: any;
  totalAfterDiscount: any;
  storeNo: any
  getStoreDetails(e){
    this.spin.show();
    setTimeout(() => {   
      this.foundStoreNumber = ''
      this.agreementService.finStoreNumber({storeNumber: [e]}).subscribe(storeResult => {
        storeResult.forEach(element => {
          if(element.status == "Open"){
            this.startDate = element.startDate;
            this.endDate = element.endDate;
            this.agreementNo = element.agreementNumber;
            this.customerName = element.customerName;
            this.showDetailsBox = true;
              this.service.Get(e).subscribe(res => {
                this.storeNo = res.codeId;
              })
            }
        });
  
        this.reportService.getpaymentdue({storeNumber: [e]}).subscribe(paymentDueResult => {
        paymentDueResult.agreementDetails.forEach(element1 => {
  
          this.statusID = element1.dueStatus;
          this.totalAfterDiscount = element1.dueAmount;
        })
        })
      })
    this.spin.hide();
     }, 1000);

  }

  storageunitList: any[] =[];
  customerList: any[] =[];
  storeNumber: any;
  foundStoreNumber: any;
  customer: any;
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.trans.storageunit.url,
      this.cas.dropdownlist.trans.customerID.url,
      
    ]).subscribe(results => {
      
      this.storageunitList = this.cas.foreachlist3(results[0], this.cas.dropdownlist.trans.storageunit.key);
      this.customerList = this.cas.foreachlist3(results[1], this.cas.dropdownlist.trans.customerID.key);
      console.log(this.storageunitList)
      this.spin.hide();
    },);
  }
  customerStoreNumber: any[] = [];
  findStoreDeatails(){
    this.customerStoreNumber = [];
   this.showDetailsBox = false;
    if(this.storeNumber){
     // this.getStoreDetails(this.storeNumber)
     this.foundStoreNumber =  this.storeNumber;
     return this.foundStoreNumber;
    }

    if(this.customer){
    this.agreementService.search({customerName: this.customer}).subscribe(customerResult => {
      customerResult.forEach(result => {
        if(result.status == "Open"){
          result.storeNumbers.forEach(x => {
            this.customerStoreNumber.push(x.storeNumber)
          })
        }
      })
 
        console.log(this.customerStoreNumber)
    })
    }
    else{
      this.toastr.error(
        "Please fill the details to continue",
        "Notification", {
          timeOut: 2000,
          progressBar: false,
        }
      );
    }
  }

  foundCustomer(storeno){
    // this.customerStoreNumber.forEach(x => {
    //   if(storeno == x){
    //     return true
    //   }
    // })

    for(let i=0; i< this.customerStoreNumber.length; i++){
      if(storeno == this.customerStoreNumber[i]){
       return storeno
      }
     }   
  }
  bgLightRed: '#ff7f7f';

  statusColor(storeno){
    let ret: any
    type Color = "#FFFFFF" | "#FF0000" | "#0000FF";
    const RED: Color = "#FF0000";
    console.log(storeno);
    console.log(this.storeNumbers)
    for(let i=0; i< this.storeNumbers.length; i++){
      if(storeno == this.storeNumbers[i].itemCode && this.storeNumbers[i].availability == "Occupied"){
        console.log(222)
        ret = RED;
      }
     }   
  }
  reset(){
    this.customer = null;
    this.storeNumber = null;
    this.showDetailsBox = false;
  }
}
