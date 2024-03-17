
import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { DialogExampleComponent } from 'src/app/common-field/dialog-example/dialog-example.component';
import { CommonService } from 'src/app/common-service/common-service.service';
import { SetupServiceService } from 'src/app/common-service/setup-service.service';
import { ClientpopupComponent } from '../../master/client/client-new/clientpopup/clientpopup.component';
import { SuccessTemplateComponent } from './success-template/success-template.component';
import { MatTableDataSource } from '@angular/material/table';
import { OwnershipService } from '../ownership/ownership.service';
import { ToastrService } from 'ngx-toastr';
import { NgxSpinnerService } from 'ngx-spinner';
import { Location } from '@angular/common';
import { SummaryService } from '../summary/summary.service';
import { ApprovalService } from '../approval/approval.service';
import { ConfirmComponent } from 'src/app/common-field/dialog_modules/confirm/confirm.component';
import { AdminApprovalComponent } from '../admin-approval/admin-approval.component';


// const ELEMENT_DATA: any[] = [
//   { coOwner1: 'ClientName 1', coOwner2: 'ClientName 2',  coOwner3: 'ClientName 3', coOwner4: 'ClientName 4',  coOwner5: 'ClientName 5',  store1: 30, store2: 30, store3: 30, store4: 10, store5: 20, storeName: 'Store Name 1',},
//   { coOwner1: 'ClientName 1', coOwner2: 'ClientName 2',  coOwner3: 'ClientName 3', coOwner4: 'ClientName 4',  coOwner5: 'ClientName 5',  store1: 20, store2: 10, store3: 30, store4: 10, store5: 20, storeName: 'Store Name 2',},
//   { coOwner1: 'ClientName 1', coOwner2: 'ClientName 2',  coOwner3: 'ClientName 3', coOwner4: 'ClientName 4',  coOwner5: 'ClientName 5',  store1: 10, store2: 20, store3: 30, store4: 10, store5: 20, storeName: 'Store Name 3',},
//   { coOwner1: 'ClientName 1', coOwner2: 'ClientName 2',  coOwner3: 'ClientName 3', coOwner4: 'ClientName 4',  coOwner5: 'ClientName 5',  store1: 30, store2: 10, store3: 10, store4: 20, store5: 10, storeName: 'Store Name 4',},
//   { coOwner1: 'ClientName 1', coOwner2: 'ClientName 2',  coOwner3: 'ClientName 3', coOwner4: 'ClientName 4',  coOwner5: 'ClientName 5',  store1: 15, store2: 25, store3: 20, store4: 10, store5: 10, storeName: 'Store Name 5',},
// ];


const ELEMENT_DATA= {
  
    
  "exactMatchResult": [
    {
      "coOwnerName": "Rafael Ortega",
      "store": [
        {
          "storeName": "SALVADOR'S MEAT MARKET LTD",
          "storePercentage": 70,
          "storeId": "107",
        },
        {
          "storeName": "PRIVATE COMPANY",
          "storePercentage": 70,
          "storeId": "3012",
        },
        {
          "storeName": "PRIVATE COMPANY 1",
          "storePercentage": 70,
          "storeId": "3011",
        }
      ]
    },
    {
      "coOwnerName": "ODA LLC",
      "store": [
        {
          "storeName": "SALVADOR'S MEAT MARKET LTD",
          "storePercentage": 70,
          "storeId": "107",
        },
        {
          "storeName": "PRIVATE COMPANY",
          "storePercentage": 32,
          "storeId": "3012",
        },
        {
          "storeName": "PRIVATE COMPANY 1",
          "storePercentage": 48,
          "storeId": "3011",
        }
      ]
    }
  ],
  "likeMatchResult": [
    {
      "coOwnerName": "Rafael Ortega",
      "store": [
        {
          "storeName": "1-LA MICHOACANA LTD",
          "storePercentage": 50,
          "storeId": "107",
        },
        {
          "storeName": "SALVADOR'S MEAT MARKET LTD",
          "storePercentage": 70,
          "storeId": "107",
        },
        {
          "storeName": "TsoreTest",
          "storePercentage": 50,
          "storeId": "107",
        }
      ]
    },
    {
      "coOwnerName": "ODA LLC",
      "store": [
        {
          "storeName": "SALVADOR'S MEAT MARKET LTD",
          "storePercentage": 30,
          "storeId": "107",
        }
      ]
    },
    {
      "coOwnerName": "Elvira Ortega",
      "store": [
        {
          "storeName": "1-LA MICHOACANA LTD",
          "storePercentage": 50,
          "storeId": "107",
        },
        {
          "storeName": "TsoreTest",
          "storePercentage": 50,
          "storeId": "107",
        }
      ]
    }
  ]
}


@Component({
  selector: 'app-brother-sister-template',
  templateUrl: './brother-sister-template.component.html',
  styleUrls: ['./brother-sister-template.component.scss']
})
export class BrotherSisterTemplateComponent implements OnInit {


  
  constructor(public dialog: MatDialog, private cs : CommonService, private fb: FormBuilder, private route: ActivatedRoute, private router: Router,
    private storePartner :OwnershipService, private toastr: ToastrService, private spin: NgxSpinnerService, private location: Location,
    private summary: SummaryService,     private storePartnerListring: ApprovalService, private pdf: AdminApprovalComponent) { }
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  showfooter= false;
  public icon = 'expand_more';
  toggleFloat() {
    this.isShowDiv = !this.isShowDiv;
    this.toggle = !this.toggle;
    if (this.icon === 'expand_more') {
      this.icon = 'chevron_left';
    } else {
      this.icon = 'expand_more'
    }
    this.showFloatingButtons = !this.showFloatingButtons;
  }
  js: any = {}
  value = 10;
  ELEMENT_DATA: any[] = [];
  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
  displayedColumns = ['coOwnerName', 'store1','store2', 'store3', 'store4', 'store5', 'store6', 'store7',  'store8', 'store9', 'store10', 'Effective'];

  showFooter: false;
  ngOnInit(): void {
    let code = this.route.snapshot.params.code;
   

    this.js = this.cs.decrypt(code);
    console.log(  this.js)
    this.findStorePartner();
    this.displayedColumns = ['coOwnerName', 'store1','store2', 'store3', 'store4', 'store5','store6', 'store7',  'store8', 'store9', 'store10','Effective'];


    if(this.js.pageflow == 'Display'){
          this.tableBrotherStatus = 'Yes';
          this.tableControlStatus = 'Yes';
          this.tableEffectiveStatus = 'Yes';
          this.showfooter=true;
          
      this.displayedColumns = ['coOwnerName', 'store1','store2', 'store3', 'store4', 'store5','store6', 'store7',  'store8', 'store9', 'store10','Effective'];
    }
  }


  findStorePartner(){
    let searchObj: any = {};
    if(this.js.code.coOwnerId1 != null)
      searchObj.coOwnerId1 = this.js.code.coOwnerId1;
    
      if(this.js.code.coOwnerId2 != null)
      searchObj.coOwnerId2 = this.js.code.coOwnerId2;

      if(this.js.code.coOwnerId3 != null)
      searchObj.coOwnerId3 = this.js.code.coOwnerId3;

      if(this.js.code.coOwnerId4 != null)
      searchObj.coOwnerId4 = this.js.code.coOwnerId4;

      if(this.js.code.coOwnerId5 != null)
      searchObj.coOwnerId5 = this.js.code.coOwnerId5;
    
      if(this.js.code.coOwnerId6 != null)
      searchObj.coOwnerId6 = this.js.code.coOwnerId6;
    
      if(this.js.code.coOwnerId7 != null)
      searchObj.coOwnerId7 = this.js.code.coOwnerId7;
    
      if(this.js.code.coOwnerId8 != null)
      searchObj.coOwnerId8 = this.js.code.coOwnerId8;
    
      if(this.js.code.coOwnerId9 != null)
      searchObj.coOwnerId9 = this.js.code.coOwnerId9;
    
      if(this.js.code.coOwnerId10 != null )
      searchObj.coOwnerId10 = this.js.code.coOwnerId10;

    this.storePartner.templateMatchResult(searchObj).subscribe(res => {
        let result = res.exactMatchResult.filter(x => x.storeId != this.js.code.storeId)
        result.splice(0, 0, this.js.code);

        
    this.storePartner.templateFormat(result).subscribe(formatResult => {
        if(formatResult){
          this.formatResult(formatResult[0]);
        }
    })

    })
  }


  findResult(result, clientLength, storeName1){
    // let ownerCount  = Object.keys(clientLength).length;
     let ownerCount  = clientLength;
     let ownerArray: any[] = [];
     
     for(let i=0 ; i < ownerCount ; i++){
       var data = result[0]['coOwnerName'+(i+1)];
       ownerArray.push({ownerName : data});
     }
     
     ownerArray.forEach((res, i) => {
      var j = 1;
      result.forEach(res1 => {
        res['store'+(j++)] = res1[storeName1 +(i + 1)]; 
      })
    })
 
    let obj: any = {}
    var h = 1;
    for(let i=0 ; i < result.length; i++){
      obj['storeName'+(h++)] = result[i].storeName;
    }

  ownerArray.splice(0, 0, obj);

  console.log(ownerArray)
  
  ownerArray.forEach(element => {
    let a = [(element.store1 ? element.store1 : 0), (element.store2 ? element.store2 : 0), 
    (element.store3 ? element.store3 : 0), (element.store4 ? element.store4 : 0),(element.store5 ? element.store5 : 0)
    (element.store6 ? element.store6 : 0), (element.store7 ? element.store7 : 0),(element.store8 ? element.store8 : 0),
    (element.store9 ? element.store9 : 0),(element.store10 ? element.store10 : 0)];
    let b = a.filter(x => x > 0);
     var minValue = Math.min(...b);
     element['effective'] = minValue;
  })

     this.dataSource = new MatTableDataSource<any>(ownerArray);
     this.validateControlTest();
     this.validateEffectiveControl();
   }

  back() {
    this.location.back();
  }



tableControlStatus: any;
tableEffectiveStatus: any;
tableBrotherStatus: any;

  openDialog(): void {
    const dialogRef = this.dialog.open(SuccessTemplateComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      data: {effectiveControlTest: this.effectiveTest, ControlTest: this.controlTest },
      position: {
        top: '6.5%'
      },
    });
    dialogRef.afterClosed().subscribe(result => {
        if(this.controlTest == true){
          this.tableControlStatus = 'Yes';
          this.showfooter=true;
          
        }
        if(this.effectiveTest == true){
          console.log(2);
          this.tableEffectiveStatus = 'Yes';
          this.showfooter=true;
        }
        if(this.effectiveTest == true && this.controlTest == true){
          this.tableBrotherStatus = 'Yes';
          this.showfooter=true;
        }
        if(this.controlTest == false){
          this.tableControlStatus = 'No';
          this.showfooter=true;
        }
        if(this.effectiveTest == false){
          console.log(3);
          this.tableEffectiveStatus = 'No';
          this.showfooter=true;
        }
        if(this.effectiveTest == false || this.controlTest == false){
          this.tableBrotherStatus = 'No';
          this.showfooter=true;
        }
        if(this.js.pageflow == 'Display'){
          this.showfooter=true;
        }

        console.log('tableBrotherStatus',this.tableBrotherStatus)
        console.log('tableEffectiveStatus', this.tableEffectiveStatus)
        console.log('tableControlStatus', this.tableControlStatus)
    });
  }

  getStore1(){
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.store1 != null ? Number(element.store1) : 0);
    })
    return total;
  }
  
  getStore2() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.store2 != null ? Number(element.store2) : 0);
    })
    return total;
  }
  
  getStore3() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.store3 != null ? Number(element.store3) : 0);
    })
    return total;
  }
  
  getStore4() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.store4 != null ? Number(element.store4) : 0);
    })
    return total;
  }
  
  getStore5() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.store5 != null ? Number(element.store5) : 0);
    })
    return total;
  }
  getStore6() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.store6 != null ? Number(element.store6) : 0);
    })
    return total;
  }

  getStore7() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.store7 != null ? Number(element.store7) : 0);
    })
    return total;
  }
  getStore8() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.store8 != null ? Number(element.store8) : 0);
    })
    return total;
  }
  getStore9() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.store9 != null ? Number(element.store9) : 0);
    })
    return total;
  }
  getStore10() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.store10 != null ? Number(element.store10) : 0);
    })
    return total;
  }


  getEffectiveTotal(){
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.effective != null && element.effective != Infinity ? element.effective : 0);
    })
    return total;
  }

  controlTest = false;
  effectiveTest= false;

  validateControlTest(){
    let totalStore1 = 0;
    let totalStore2 = 0;
    let totalStore3 = 0;
    let totalStore4 = 0;
    let totalStore5 = 0;
    let totalStore6 = 0;
    let totalStore7 = 0;
    let totalStore8 = 0;
    let totalStore9 = 0;
    let totalStore10 = 0;

    this.dataSource.data.forEach(element => {
      totalStore1 = totalStore1 + (element.store1 != null ? Number(element.store1) : 0);
      totalStore2 = totalStore2 + (element.store2 != null ? Number(element.store2) : 0);
      totalStore3 = totalStore3 + (element.store3 != null ? Number(element.store3) : 0);
      totalStore4 = totalStore4 + (element.store4 != null ? Number(element.store4) : 0);
      totalStore5 = totalStore5 + (element.store5 != null ? Number(element.store5) : 0);
      totalStore6 = totalStore6 + (element.store6 != null ? Number(element.store6) : 0);
      totalStore7 = totalStore7 + (element.store7 != null ? Number(element.store7) : 0);
      totalStore8 = totalStore8 + (element.store8 != null ? Number(element.store8) : 0);
      totalStore9 = totalStore9 + (element.store9 != null ? Number(element.store9) : 0);
      totalStore10 = totalStore10 + (element.store10 != null ? Number(element.store10) : 0);
    })

    if((totalStore1 >= 80 || totalStore1 == 0) && (totalStore2 >= 80 || totalStore2 == 0) &&  (totalStore3 >= 80 || totalStore3 == 0) &&  (totalStore4 >= 80 || totalStore4 == 0)
    &&  (totalStore5 >= 80 || totalStore5 == 0) &&  (totalStore6 >= 80 || totalStore6 == 0) &&  (totalStore7 >= 80 || totalStore7 == 0)
    &&  (totalStore8 >= 80 || totalStore8 == 0) &&  (totalStore9 >= 80 || totalStore9 == 0) &&  (totalStore10 >= 80 || totalStore10 == 0)){
      this.controlTest = true;
    }else{
        this.controlTest = false;
    }
  }

  validateEffectiveControl(){
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.effective != null && element.effective != Infinity ? element.effective : 0);
    })
    if(total > 50){
      this.effectiveTest = true;
    }else{
      this.effectiveTest = false;
    }
  }
  
  approve(){

    if (this.controlTest == false || this.effectiveTest == false) {
      this.toastr.error(
        "Validation Failed",
        "Notification",{
          timeOut: 2000,
          progressBar: false,
        }
      );

      this.cs.notifyOther(true);
      return;
    }

    this.spin.show();
    this.js.code.statusId = 2;
    this.storePartner.Update(this.js.code, this.js.code.requestId, this.js.code.languageId, this.js.code.companyId).subscribe(res =>{
      this.toastr.success(this.js.code.requestId + " validated successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      let paramdata = "";
      paramdata = this.cs.encrypt({ line: this.js.code});
      this.router.navigate(['/main/controlgroup/transaction/validation/' +  paramdata])
      this.spin.hide();
    }, err =>{
      this.cs.commonerrorNew(err);
      this.spin.hide();
    })
  }


  
  formatResult(Result){ 
    let obj: any = {};
    let arrayNew: any[] = [];
    Result.exactMatchResult.forEach((clientName, i) => {
      clientName.store.forEach((stores, j) => {
        arrayNew.push(stores.storeName);
      });
    });
  
    let res: any; 
    res = Result;

    // console.log(res.exactMatchResult);
    // console.log(obj);
    
    // res.exactMatchResult.splice(0, 0, obj);
    //  this.dataSource = new MatTableDataSource<any>(res.exactMatchResult);
    //  this.validateControlTest();
    //  this.validateEffectiveControl();

    
    let duplicatedArray = this.cs.uniqByFilter(arrayNew);


    this.formatDataFinal(res, duplicatedArray)

  }

  formatDataFinal(Result, duplicatedArray) {
    console.log(duplicatedArray)
    duplicatedArray.forEach((x, index) => {
      Result.exactMatchResult.forEach((clientName, i) => {
        clientName.store.forEach((stores, j) => {
          //clientName['store' + (j+1)] = stores.storePercentage;
          if (stores.storeName == x) {
            clientName['store' + (index + 1)] = stores.storePercentage;
          }
        });
      });
    })

    Result.exactMatchResult.forEach(element => {
      let a = [(element.store1 ? element.store1 : 0), (element.store2 ? element.store2 : 0), 
      (element.store3 ? element.store3 : 0), (element.store4 ? element.store4 : 0),(element.store5 ? element.store5 : 0),
      (element.store6 ? element.store6 : 0), (element.store7 ? element.store7 : 0),(element.store8 ? element.store8 : 0),
      (element.store9 ? element.store9 : 0),(element.store10 ? element.store10 : 0),];
      let b = a.filter(x => x > 0);
       var minValue = Math.min(...b);
       element['effective'] = minValue;
    })
    
    

    let obj: any = {};
    duplicatedArray.filter((x, i) => (obj['storeName' + (i + 1)] = x));
    Result.exactMatchResult.splice(0, 0, obj);
    this.dataSource = new MatTableDataSource<any>(Result.exactMatchResult);
    this.validateControlTest();
    this.validateEffectiveControl();
  }

ApproveAdmin(element, action): void {
  const dialogRef = this.dialog.open(ConfirmComponent, {
    disableClose: true,
    width: '40%',
    maxWidth: '80%',
    position: {
      top: '6.5%'
    },
    data: {title: "Confirm", message:  action == 'Approve' ? "Are you sure you want to approve this record?" : "Are you sure you want to reject this record?"}
  });
  dialogRef.afterClosed().subscribe(result => {
    if(result && action == "Approve"){
      this.spin.show();
      element.statusId = 5;
      this.storePartnerListring.Create(element).subscribe(res =>{
        this.toastr.success(element.requestId + " approved successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.storePartner.Update(element, element.requestId, element.languageId, element.companyId).subscribe(res =>{

          this.summary.reportPdf().subscribe(summaryRes => {
            this.pdf.generatePdf(summaryRes, element);
            this.spin.hide();
          }, err => {
            this.cs.commonerror(err);
            this.spin.show();
          })

        }, err =>{
          this.cs.commonerrorNew(err);
          this.spin.hide();
        })
        
        this.router.navigate(['/main/controlgroup/transaction/approvalAdmin'])
        this.spin.hide();
      }, err =>{
        this.cs.commonerrorNew(err);
        this.spin.hide();
      })
    
    }
    if(result && action == "Reject"){
      this.spin.show();
      element.statusId = 1;
      element.groupId = null;
      element.groupName = null;
      this.storePartner.Update(element, element.requestId, element.languageId, element.companyId).subscribe(res =>{
        this.toastr.success(element.requestId + " rejected successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
      //  this.router.navigate(['/main/controlgroup/transaction/approval'])
      let paramdata = '';
      paramdata = this.cs.encrypt({ line: res});
      this.router.navigate(['/main/controlgroup/transaction/ownership/' +  paramdata])
        this.spin.hide();
      }, err =>{
        this.cs.commonerrorNew(err);
        this.spin.hide();
      })
    }
  });
  
}
}






// findResult(){
//   let ownerCount  = (Object.keys(ELEMENT_DATA[0]).length -1) /2;
//   let ownerArray: any[] = [];

//   let obj: any = {}
//   var h = 1;
//   for(let i=0 ; i < ownerCount ; i++){
//     var data = ELEMENT_DATA[0]['coOwner'+(i+1)];
//     ownerArray.push({ownerName : data});
//     obj['storeName'+(h++)] = ELEMENT_DATA[i].storeName;
//   }
  
//   ownerArray.splice(0, 0, obj);
  
//   ownerArray.forEach((res, i) => {
//     var j = 1;
//     ELEMENT_DATA.forEach(res1 => {
//       res['store'+(j++)] = res1['store'+(i)]; //res1['store'+(i+1)];
//     })
//   })

// //   let obj: any = {}
// //   var h = 1;
// //   for(let i=0 ; i < ownerCount; i++){
// //     obj['storeName'+(h++)] = ELEMENT_DATA[i].storeName;
// //   }

// // ownerArray.splice(0, 0, obj);
//   this.dataSource = new MatTableDataSource<any>(ownerArray);
//   this.displayedColumns = ['ownerName', 'store1','store2', 'store3', 'store4', 'store5', 'store6', 'store7',  'Effective'];
// }
 //-- Hareesh



// findResult(result, clientLength, storeName){
//   // let ownerCount  = Object.keys(clientLength).length;
//    let ownerCount  = clientLength;
//    let ownerArray: any[] = [];
   
//    let obj: any = {}
//    var h = 1;
//    for(let i=0 ; i < ownerCount ; i++){
//      console.log(result[i].storeName)
//      var data = result[0]['coOwnerName'+(i+1)];
//      ownerArray.push({ownerName : data});
//      obj['storeName'+(h++)] = result[i].storeName;
//    }
   
//    ownerArray.splice(0, 0, obj);
   
//    ownerArray.forEach((res, i) => {
//      var j = 1;
//      result.forEach(res1 => {
//        res['store'+(j++)] = res1[storeName +(i)]; //res1['store'+(i+1)];
//      })
//    })


//    ownerArray.forEach(element => {
//      element['effective'] = (Math.min(element.store1 != null ? element.store1 : 2), (element.store2 != null ? element.store2 : 2) ,
//      (element.store3 != null ? element.store3 : 2) , (element.store4 != null ? element.store4 : 2), (element.store5 != null ? element.store5 : 2));
//    })

//    console.log(ownerArray);
//    this.dataSource = new MatTableDataSource<any>(ownerArray);
//    this.displayedColumns = ['ownerName', 'store1','store2', 'store3', 'store4', 'store5', 'Effective'];
//  }

//-- previous one