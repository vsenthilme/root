import { DatePipe, DecimalPipe } from '@angular/common';
import { Injectable, Inject } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Subject } from 'rxjs';
import * as CryptoJS from 'crypto-js';
import moment from 'moment';
import { environment } from 'src/environments/environment';
import { color } from 'highcharts';
import { FormGroup, FormArray } from '@angular/forms';
export interface dropdownelement {
  item_id: any;
  item_text: any;

}
export interface MenuAccess {
  createUpdate: boolean;
  view: boolean;
  delete: boolean;

}
@Injectable({
  providedIn: 'root'
})
export class CommonService {
  private notify = new Subject<any>();
  /**
   * Observable string streams
   */
  notifyObservable$ = this.notify.asObservable();

  constructor(private toastr: ToastrService, private datepipe: DatePipe, private decimalPipe: DecimalPipe,) { }

  public notifyOther(data: any) {
    this.notify.next(data);
  }
  public commonerror(msg: any) {
    // 
    // if (msg.name == "HttpErrorResponse")
    //   this.toastr.error("Check your internet connection/check with Admin.", "Error");
    // else
    if (msg.error)
      this.toastr.error(msg.error.error, "Error", {
        timeOut: 2000,
        progressBar: false,
      });


    else if (msg)
      this.toastr.error(msg, "Error", {
        timeOut: 2000,
        progressBar: false,
      });

    else
      this.toastr.error("Kindly Contact System Admin", "Error", {
        timeOut: 2000,
        progressBar: false,
      });
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
  today() {
    const del_date = this.datepipe.transform(new Date(), "MM-dd-yyyy", "GMT-06:00");
    return del_date;
  }
  todayapi() {
    const del_date = this.datepipe.transform(new Date().toLocaleDateString(), 'yyyy-MM-ddTh:mm:ss.SSS');
    return del_date;
  }
  dateapi(date: any) {
    const del_date = this.datepipe.transform(date, "MM-dd-yyyy", "GMT-06:00");
    return del_date;
  }
  dateapiutc0(date: any) {
    const del_date = this.datepipe.transform(date, "MM-dd-yyyy", "GMT00:00");
    return del_date;
  }
  dateapiutc01(date: any) {
    const del_date = this.datepipe.transform(date, "dd-MM-yyyyTHH:mm:ss", "UTC-05:00");
    return del_date;
  }
  dateapiutc02(date: any) {
    const del_date = this.datepipe.transform(date, "yyyy-MM-ddTHH:mm:ss.sss", "UTC-06:00");
    return del_date;
  }
  day_callapiSearch(date: any) {
    const del_date = this.datepipe.transform(date, 'yyyy-MM-ddT00:00:00.000');
    return del_date;
  }
  matterApiSearch(date: any) {
    const del_date = this.datepipe.transform(date, 'yyyy-MM-ddT00:00:00.000', "GMT00:00");
    return del_date;
  }
  matterApiSearch1(date: any) {
    const del_date = this.datepipe.transform(date, 'yyyy-MM-ddT00:00:00.000', "GMT-06:00");
    return del_date;
  }
  excel_date(date: any) {
    const del_date = this.datepipe.transform(date, 'MM-dd-yyyy');
    return del_date;
  }
  day_callapiSearch1(date: any) {
    const del_date = this.datepipe.transform(date, 'MM-dd-yyyyT00:00:00.000');
    return del_date;
  }
  todayCallApi() {
    const del_date = this.datepipe.transform(new Date(), 'yyyy-MM-ddT00:00:00.000');
    return del_date;
  }

  //Date with time for angular date picker
  getMomentDate(date){
    const _ = moment();
    return (moment(date).set({hours: _.hour(), minutes:_.minute() , seconds:_.second()})).toDate();
  }
  datesearch(date: any): Date {

    return new Date(date);
  }
  date_task_api(date: any) {
    const del_date = this.datepipe.transform(date, "yyyy-MM-dd 00:00:00", "GMT-06:00");
    return del_date;
  }


  dateNewFormat(date: any) {
    const del_date = this.datepipe.transform(date, 'yyyy-MM-dd HH:mm:ss');
    return del_date;
  }
  dateNewFormat1(date: any) {
    const del_date = this.datepipe.transform(date, 'yyyy-MM-dd');
    return del_date;
  }
  dateNewFormatPatch(date: any) {
    const del_date = this.datepipe.transform(date, 'yyyy-MM-ddT00:00:00.000', "GMT00:00");
    return del_date;
  }

  encrypt(storageData: any) {
    //Encrypt Info

    return encodeURIComponent(CryptoJS.AES.encrypt(JSON.stringify(storageData), 'secret key 123').toString());

  }

  decrypt(storageData: any) {
    var deData = CryptoJS.AES.decrypt(decodeURIComponent(storageData), 'secret key 123');

    return JSON.parse(deData.toString(CryptoJS.enc.Utf8));
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


  public findInvalidControlsRecursive(formToInvestigate:FormGroup|FormArray):string[] {
    var invalidControls:string[] = [];
    let recursiveFunc = (form:FormGroup|FormArray) => {
      Object.keys(form.controls).forEach(field => { 
        const control = form.get(field);
   if(control){
    if (control.invalid) invalidControls.push(field);
    if (control instanceof FormGroup) {
      recursiveFunc(control);
    } else if (control instanceof FormArray) {
      recursiveFunc(control);
    }   
   }     
   
      });
    }
    recursiveFunc(formToInvestigate);
    return invalidControls;
  }
  
  datePlusOne(date: any) {
    let plusOneToDate = new Date(date);
    plusOneToDate.setDate(plusOneToDate.getDate() + 1);
    
    return plusOneToDate; 
  }


  // public findInvalidControlsNew(form: any) {
  //   const invalid: any[] = [];
  //   const controls = form.controls;
  //   for (const name in controls) {
      
  //     if (controls[name].invalid) {
  //       if(controls[name].value != undefined && controls[name].value != null  && controls[name].value){
  //      console.log(controls[name].value)
  //      invalid.push(controls[name].value);
  //       }
  //       else{
          
  //       }
  //       invalid.push(name);
  //     }
  //   }
  //   return invalid; 
  // }

  customerformname(id: any) {
    let formname = '';
    switch (id) {
      case 1: {
        formname = 'immigrationform';
        break;
      }
      case 2: {
        formname = 'englishform';
        break;
      }
      case 3: {
        formname = 'spanishform';
        break;
      }
      case 4: {
        formname = 'english400form';
        break;
      }
      case 5: {
        formname = 'dacaform';
        break;
      }
      case 6: {
        formname = 'landeform';
        break;
      }
      case 7: {
        formname = 'form007';
        break;
      }
      case 8: {
        formname = 'form008';
        break;
      }
      case 9: {
        formname = 'form009';
        break;
      }
      case 10: {
        formname = 'form010';
        break;
      }
      case 11: {
        formname = 'form011';
        break;
      }

      default: {
        //statements; 
        formname = '';
      }
    }

    return formname;
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

  removeDuplicateInArray(array: any[]) {
    let uniqueArray = array.filter((key, index) => {
      const _thing = JSON.stringify(key);
      return index === array.findIndex(obj => {
        return JSON.stringify(obj) === _thing;
      });
    });
    return uniqueArray;
  }

  currentEnv: string;

  prod: boolean;
  prodInstance(){
    this.currentEnv = environment.name; 

    if(this.currentEnv == 'prod'){
      this.prod = true;
    }else{
      this.prod = false;
    }
    console.log(this.prod)
    return this.prod;
 
  }
}