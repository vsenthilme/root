import { Injectable, Inject } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Subject } from 'rxjs';

import * as CryptoJS from 'crypto-js';
import { DatePipe, DecimalPipe } from '@angular/common';

import * as XLSX from 'xlsx';
import { HttpClient } from '@angular/common/http';
import { CustomerService } from '../main-module/Masters -1/customer-master/customer.service';
export interface dropdownelement {
  item_id: any;
  item_text: any;

}

export interface dropdownelement1 {
  id: any;
  itemName: any;

}
@Injectable({
  providedIn: 'root'
})
export class CommonService {
  private notify = new Subject<any>();
  inboundorderType = [
    { id: 1, text: 'ASN' },
    { id: 2, text: 'Store Return' },
    { id: 3, text: 'WH2WH' },
    { id: 4, text: 'SaleOrder Return' },
  ]

  public getinboundorderType_text(id: any) {
    let result = this.inboundorderType.filter(x => x.id === id);
    if (result) { return result[0].text }
    else { return null }

  }
  day_callapiSearch(date: any) {
    const del_date = this.datepipe.transform(date, 'yyyy-MM-ddT00:00:00.000');
    return del_date;
  }
  outboundOrderType = [
    { id: 0, text: 'SO' },
    { id: 1, text: 'WH2WH' },
    { id: 2, text: 'Return PO' },
    { id: 3, text: 'TE Order' },
  ]

   public get_customerName(id: any) {
    if (!localStorage.getItem('status')) {
      this.callcustomer(id);
    }

    let result = JSON.parse(localStorage.getItem('customer') as '[]');
    if (result != null && result != undefined && result != '[]') {
      result = result.filter(x => x.customerCode === id);
    }

    if (result) { return result[0].status }
    else { return null }

  }
  private callcustomer(id: any) {
    this.http.get<any>('/us-trans-service/master/leadcustomer').subscribe((res) => {
      localStorage.setItem('customer', JSON.stringify(res));
      this.get_customerName(id)
    }, err => {

    })

  }
 
  /**
   * Observable string streams
   */
  notifyObservable$ = this.notify.asObservable();


  constructor(private toastr: ToastrService, private datepipe: DatePipe, private decimalPipe: DecimalPipe, private http: HttpClient,
    private service: CustomerService) { }

  public notifyOther(data: any) {
    this.notify.next(data);
  }
  datesearch(date: any): Date {

    return new Date(date);
  }
  public filterArray(targetArray: any[], filters: any) {

    var filterKeys = Object.keys(filters);
    let data = targetArray.filter(function (eachObj: any) {
      return filterKeys.every(function (eachKey) {
        if (!filters[eachKey])
          return true;
        if (!eachObj[eachKey])
          return true;
        if (eachObj[eachKey] === undefined)
          return true;
        // if (filters[eachKey]) {
        //   if (!filters[eachKey].length) {
        //     return true;
        //   }
        // }

        const fo: string = String(filters[eachKey])?.toLowerCase();
        const eo: string = String(eachObj[eachKey])?.toLowerCase();
        if (fo == "")
          return true;

        const folist = fo.split(',');

        return folist.includes(eo);
      });
    });

    if (filters.createdOn_from &&
      filters.createdOn_to) {

      data = data.filter(x =>
        this.datesearch(this.day_callapiSearch(new Date(x.createdOn.replace("+00:00", "")))) >= this.datesearch(this.day_callapiSearch(filters.createdOn_from))
        && this.datesearch(this.day_callapiSearch(new Date(x.createdOn.replace("+00:00", "")))) <= this.datesearch(this.day_callapiSearch(filters.createdOn_to))
      );

    }

    return data;
  }
  public commonerror(msg: any) {
    // 
    // if (msg.name == "HttpErrorResponse")
    //   this.toastr.error("Check your internet connection/check with Admin.", "Error");
    // else
    this.toastr.error(msg.error , "Error", {
      timeOut: 2000,
      progressBar: false,
    });


  }
  public commonerror1(msg: any) {
    // 
    // if (msg.name == "HttpErrorResponse")
    //   this.toastr.error("Check your internet connection/check with Admin.", "Error");
    // else
    this.toastr.error(msg.error.error , "Error", {
      timeOut: 2000,
      progressBar: false,
    });


  }
  dateapi(date: any) {
    const del_date = this.datepipe.transform(date, "dd-MM-yyyy", "GMT-06:00");
    return del_date;
  } dateapi1(date: any) {
    const del_date = this.datepipe.transform(date, "yyyy-MM-ddT00:00:00.000", "GMT");
    return del_date;
  }
  day_callapi(date: any) {
    const del_date = this.datepipe.transform(date, 'yyyy-MM-ddT00:00:00.000');
    return del_date;
  }
  day_time(date: any) {
    const del_date = this.datepipe.transform(date, 'yyyy-MM-ddTHH:mm:ss.000');
    return del_date;
  }
  getdate() {
    const del_date = this.datepipe.transform(Date.now(), 'dd/MM/yyyy');
    return del_date;

  }

  dateapiutc0(date: any) {
    const del_date = this.datepipe.transform(date, "dd-MM-yyyy", "UTC 0");
    return del_date;
  }
  date(date: any) {
    return new Date(date);
  }

  startDate(date: any) {
    const del_date = this.datepipe.transform(date, 'yyyy-MM-ddT00:00:00.000');
    return del_date;
  }

  endDate(date: any) {
    const del_date = this.datepipe.transform(date, 'yyyy-MM-ddT23:59:00.000');
    return del_date;
  }
  getDataDiff(startDate: any, endDate: any) {

    var diff = this.date(endDate).getTime() - this.date(startDate).getTime();
    var days = Math.floor(diff / (60 * 60 * 24 * 1000));
    var hours = Math.floor(diff / (60 * 60 * 1000)) - (days * 24);
    var minutes = Math.floor(diff / (60 * 1000)) - ((days * 24 * 60) + (hours * 60));
    var seconds = Math.floor(diff / 1000) - ((days * 24 * 60 * 60) + (hours * 60 * 60) + (minutes * 60));
    let days1 = days == 0 ? '' : days + ' Days ';
    return days1 + hours + ':' + minutes + ':' + seconds
  };
  encrypt(storageData: any) {
    //Encrypt Info

    return encodeURIComponent(CryptoJS.AES.encrypt(JSON.stringify(storageData), 'secret key 123').toString());

  }

  decrypt(storageData: any) {
    var deData = CryptoJS.AES.decrypt(decodeURIComponent(storageData), 'secret key 123');

    return JSON.parse(deData.toString(CryptoJS.enc.Utf8));
  }

  todayapi() {
    const del_date = this.datepipe.transform(new Date().toLocaleDateString(), 'yyyy-MM-ddTh:mm:ss.SSS');
    return del_date;
  }

  exportAsExcel(data: any, filename: string = "Downloaded_Excel") {
    // const asnConf = data;

    const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet(data);
    // converts a DOM TABLE element to a worksheet
    const wb: XLSX.WorkBook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');
    /* save to file */
    XLSX.writeFile(
      wb,
      filename + `_${new Date().getDate() +
      '-' +
      (new Date().getMonth() + 1) +
      '-' +
      new Date().getFullYear()
      }.xlsx`
    );
  }


  removeDuplicatesFromArrayNew(list:any = []){
    let dataObj: any = {};
    for (let i = 0; i < list.length; i++) {
      dataObj[list[i]['value']] = list[i];
    }
    list = new Array();
    for (let key in dataObj) {
      list.push(dataObj[key]);
    }
    return list;
  }
  removeDuplicatesFromArrayNew1(list:any = []){
    let dataObj: any = {};
    for (let i = 0; i < list.length; i++) {
      dataObj[list[i]['value']] = list[i];
    }
    list = new Array();
    for (let key in dataObj) {
      list.push([key]);
    }
    return list;
  }

  removeDuplicatesFromArrayList(list: any = [], fieldName) {
    let dataObj: any = {};
    for (let i = 0; i < list.length; i++) {
      dataObj[list[i][fieldName]] = list[i];
    }
    list = new Array();
    for (let key in dataObj) {
      list.push(dataObj[key]);
    }
    return list;
  }
  
  customerDetails(){
    this.service.getDropdown().subscribe(res => {

      console.log(res.customerDropDown)
      return res.customerDropDown
    })
  }

findOccurance(arr, key){
  let arr2: any[] = [];
        
  arr.forEach((x)=>{
       
    // Checking if there is any object in arr2
    // which contains the key value
     if(arr2.some((val)=>{ 
      return val[key] == x[key] })){
         
       // If yes! then increase the occurrence by 1
       arr2.forEach((k)=>{
         if(k[key] === x[key]){ 
           k["y"]++
         }
      })
         
     }else{
       let a = {}
       a[key] = x[key]
       a["y"] = 1
       a["z"] = 30
       arr2.push(a);
     }
  })
    
  return arr2
}



findOccurance1(arr, key, type){
  let arr2: any[] = [];
        
  arr.forEach((x)=>{
       
    // Checking if there is any object in arr2
    // which contains the key value
     if(arr2.some((val)=>{ 
      return val[key] == x[key] && val[type] == x[type]})){
         
       // If yes! then increase the occurrence by 1
       arr2.forEach((k)=>{
         if(k[key] === x[key] && k[type] == x[type]){ 
           k["y"]++
         }
      })
         
     }else{
       let a = {}
       a[key] = x[key]
       a[type] = x[type]
       a["y"] = 1
       arr2.push(a);
     }
  })
    
  return arr2
}


findOccurance2(arr, key){
  let arr2: any[] = [];
        
  arr.forEach((x)=>{
       
    // Checking if there is any object in arr2
    // which contains the key value
     if(arr2.some((val)=>{ 
      return val[key] == x[key] })){
         
       // If yes! then increase the occurrence by 1
       arr2.forEach((k)=>{
         if(k[key] === x[key]){ 
           k["y"]++
         }
      })
         
     }else{
       let a = {}
       a[key] = x[key]
       a["y"] = 1
       arr2.push(a);
     }
  })
    
  return arr2
}



public findInvalidControls(form: any) {
  const invalid: any[] = [];
  const controls = form.controls;
  for (const name in controls) {
    if (controls[name].invalid) {
      invalid.push(name);
    }
  }
  return invalid; 
}
}