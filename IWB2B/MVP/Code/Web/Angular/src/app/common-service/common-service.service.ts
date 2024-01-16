import { Injectable, Inject } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Subject } from 'rxjs';

import * as CryptoJS from 'crypto-js';
import { DatePipe } from '@angular/common';

import * as XLSX from 'xlsx';
import { HttpClient } from '@angular/common/http';
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

  day_callapiSearch(date: any) {
    const del_date = this.datepipe.transform(date, 'yyyy-MM-ddT00:00:00.000');
    return del_date;
  }

  /**
   * Observable string streams
   */
  notifyObservable$ = this.notify.asObservable();


  constructor(private toastr: ToastrService, private datepipe: DatePipe, private http: HttpClient) { }

  public notifyOther(data: any) {
    this.notify.next(data);
  }


  public commonerror(msg: any) {
    // 
    // if (msg.name == "HttpErrorResponse")
    //   this.toastr.error("Check your internet connection/check with Admin.", "Error");
    // else
    this.toastr.error(msg.error, "Error", {
      timeOut: 2000,
      progressBar: false,
    });


  }
  dateapi(date: any) {
    const del_date = this.datepipe.transform(date, "dd-MM-yyyy", "GMT-06:00");
    return del_date;
  }

  dateTimeApi(date: any) {
    const del_date = this.datepipe.transform(date, "dd-MM-yyyy  HH:mm:ss", "UTC 0");
    return del_date;
  }

  day_callapi(date: any) {
    const del_date = this.datepipe.transform(date, 'yyyy-MM-ddT00:00:00.000');
    return del_date;
  }

  dateapiutc0(date: any) {
    const del_date = this.datepipe.transform(date, "dd-MM-yyyy", "UTC 0");
    return del_date;
  }

  getdate() {
    const del_date = this.datepipe.transform(Date.now(), 'dd/MM/yyyy');
    return del_date;

  }
  getOnlyDate(date) {
    const del_date = this.datepipe.transform(date, 'MM/dd/yyyy');
    return del_date;

  }
  date(date: any) {
    return new Date(date);
  }

  dateMMYY(date: any) {
    const del_date = this.datepipe.transform(date, "dd-MM-yyyy");
    return del_date;
  }
  dateHHSS(date: any) {
    const del_date = this.datepipe.transform(date, "HH:mm");
    return del_date;
  }

  getDataDiff(startDate: any, endDate: any) {

    var diff = this.date(endDate).getTime() - this.date(startDate).getTime();
    var days = Math.floor(diff / (60 * 60 * 24 * 1000));
    var hours = Math.floor(diff / (60 * 60 * 1000)) - (days * 24);
    var minutes = Math.floor(diff / (60 * 1000)) - ((days * 24 * 60) + (hours * 60));
    var seconds = Math.floor(diff / 1000) - ((days * 24 * 60 * 60) + (hours * 60 * 60) + (minutes * 60));
    days == 0 ? '' : days + 'Days ';
    return days + hours + ':' + minutes + ':' + seconds
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



  removeDuplicatesFromArrayNew(list: any = []) {
    let dataObj: any = {};
    for (let i = 0; i < list.length; i++) {
      dataObj[list[i]['case_no']] = list[i];
    }
    list = new Array();
    for (let key in dataObj) {
      list.push(dataObj[key]);
    }
    return list;
  }
  removeDuplicatesOldDropdown(list: any = []) {
    let dataObj: any = {};
    for (let i = 0; i < list.length; i++) {
      dataObj[list[i]['id']] = list[i];
    }
    list = new Array();
    for (let key in dataObj) {
      list.push(dataObj[key]);
    }
    return list;
  }

  removeDuplicatesFromArrayNew1(list: any = []) {
    let dataObj: any = {};
    for (let i = 0; i < list.length; i++) {
      dataObj[list[i]['lineNo']] = list[i];
    }
    list = new Array();
    for (let key in dataObj) {
      list.push(dataObj[key]);
    }
    return list;
  }
  removeDuplicatesFromArrayNewstatus(list: any = []) {
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

  removeDuplicatesFromArraydropdown(list: any = []) {
    let dataObj: any = {};
    for (let i = 0; i < list.length; i++) {
      dataObj[list[i]['key']] = list[i];
    }
    list = new Array();
    for (let key in dataObj) {
      list.push(dataObj[key]);
    }
    return list;
  }



  groupByData(list: any[], keyGetter: any) {
    let map = new Map();
    list.forEach((item) => {
      let key = keyGetter(item);
      let collection = map.get(key);
      if (!collection) {
        map.set(key, [item]);
      } else {
        collection.push(item);
      }
    });
    return map;
  }

  ngxDropdownFilter(list: any = []) {
    let dataObj: any = {};
    for (let i = 0; i < list.length; i++) {
      dataObj[list[i]['item_id']] = list[i];
    }
    list = new Array();
    for (let key in dataObj) {
      list.push(dataObj[key]);
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

}