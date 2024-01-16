
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


// const ELEMENT_DATA: any[] = [
//   { coOwner1: 'ClientName 1', coOwner2: 'ClientName 2',  coOwner3: 'ClientName 3', coOwner4: 'ClientName 4',  coOwner5: 'ClientName 5',  store1: 30, store2: 30, store3: 30, store4: 10, store5: 20, storeName: 'Store Name 1',},
//   { coOwner1: 'ClientName 1', coOwner2: 'ClientName 2',  coOwner3: 'ClientName 3', coOwner4: 'ClientName 4',  coOwner5: 'ClientName 5',  store1: 20, store2: 10, store3: 30, store4: 10, store5: 20, storeName: 'Store Name 2',},
//   { coOwner1: 'ClientName 1', coOwner2: 'ClientName 2',  coOwner3: 'ClientName 3', coOwner4: 'ClientName 4',  coOwner5: 'ClientName 5',  store1: 10, store2: 20, store3: 30, store4: 10, store5: 20, storeName: 'Store Name 3',},
//   { coOwner1: 'ClientName 1', coOwner2: 'ClientName 2',  coOwner3: 'ClientName 3', coOwner4: 'ClientName 4',  coOwner5: 'ClientName 5',  store1: 30, store2: 10, store3: 10, store4: 20, store5: 10, storeName: 'Store Name 4',},
//   { coOwner1: 'ClientName 1', coOwner2: 'ClientName 2',  coOwner3: 'ClientName 3', coOwner4: 'ClientName 4',  coOwner5: 'ClientName 5',  store1: 15, store2: 25, store3: 20, store4: 10, store5: 10, storeName: 'Store Name 5',},
// ];
@Component({
  selector: 'app-brother-sister-template',
  templateUrl: './brother-sister-template.component.html',
  styleUrls: ['./brother-sister-template.component.scss']
})
export class BrotherSisterTemplateComponent implements OnInit {


  
  constructor(public dialog: MatDialog, private cs : CommonService, private fb: FormBuilder, private route: ActivatedRoute, private router: Router,
    private storePartner :OwnershipService, private toastr: ToastrService, private spin: NgxSpinnerService, private location: Location) { }
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
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
  displayedColumns = ['ownerName', 'store1','store2', 'store3', 'store4', 'store5', 'store6', 'store7', 'Effective'];

  showFooter: false;
  ngOnInit(): void {
    let code = this.route.snapshot.params.code;
   
    this.js = this.cs.decrypt(code);

    console.log(this.js.code)
    this.findStorePartner();
    this.displayedColumns = ['ownerName', 'store1','store2', 'store3', 'store4', 'store5','store6', 'store7',];


    if(this.js.pageflow == 'Display'){
          this.tableBrotherStatus = 'Yes';
          this.tableControlStatus = 'Yes';
          this.tableEffectiveStatus = 'Yes';
          
      this.displayedColumns = ['ownerName', 'store1','store2', 'store3', 'store4', 'store5','store6', 'store7','Effective'];
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

    this.storePartner.searchStoreMatch(searchObj).subscribe(res => {
      if(res.exactMatchResult.length > 0){
        this.findResult(res.exactMatchResult,  Object.keys(searchObj).length, 'entity');
      }else{
       this.findResult([this.js.code], Object.keys(searchObj).length, 'coOwnerPercentage');
      }
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
    (element.store3 ? element.store3 : 0), (element.store4 ? element.store4 : 0),(element.store5 ? element.store5 : 0)];
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
        }
        if(this.effectiveTest == true){
          console.log(2);
          this.tableEffectiveStatus = 'Yes';
        }
        if(this.effectiveTest == true && this.controlTest == true){
          this.tableBrotherStatus = 'Yes';
        }
        if(this.controlTest == false){
          this.tableControlStatus = 'No';
        }
        if(this.effectiveTest == false){
          console.log(3);
          this.tableEffectiveStatus = 'No';
        }
        if(this.effectiveTest == false || this.controlTest == false){
          this.tableBrotherStatus = 'No';
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

    this.dataSource.data.forEach(element => {
      totalStore1 = totalStore1 + (element.store1 != null ? Number(element.store1) : 0);
      totalStore2 = totalStore2 + (element.store2 != null ? Number(element.store2) : 0);
      totalStore3 = totalStore3 + (element.store3 != null ? Number(element.store3) : 0);
      totalStore4 = totalStore4 + (element.store4 != null ? Number(element.store4) : 0);
      totalStore5 = totalStore5 + (element.store5 != null ? Number(element.store5) : 0);
      totalStore6 = totalStore6 + (element.store6 != null ? Number(element.store6) : 0);
      totalStore7 = totalStore7 + (element.store7 != null ? Number(element.store7) : 0);
    })

    if((totalStore1 == 100 || totalStore1 == 0) && (totalStore2 == 100 || totalStore2 == 0) &&  (totalStore3 == 100 || totalStore3 == 0) &&  (totalStore4 == 100 || totalStore4 == 0)
    &&  (totalStore5 == 100 || totalStore5 == 0) &&  (totalStore6 == 100 || totalStore6 == 0) &&  (totalStore7 == 100 || totalStore7 == 0)){
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