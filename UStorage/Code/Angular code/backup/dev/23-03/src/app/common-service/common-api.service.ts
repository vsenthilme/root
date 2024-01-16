import { Injectable } from '@angular/core';
import { AuthService } from '../core/core';
import { HttpClient } from '@angular/common/http';
import { NgxSpinnerService } from 'ngx-spinner';
import { forkJoin, Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';
import { CommonService } from './common-service.service';
export interface dropdownelement {
  key: any;
  value: any;
  referenceField?: any;
}

export interface dropdownelement1 {
  value: any;
  label: any;
  referenceField?: any;
}

@Injectable({
  providedIn: 'root'
})
export class CommonApiService {

  constructor(private http: HttpClient, private cs: CommonService, private auth: AuthService, private spin: NgxSpinnerService, public toastr: ToastrService,) { }

  setup = '/us-master-service/';
  trans = '/us-trans-service/master/';
  transcrm = '/us-trans-service/crm/';
  transOperation = '/us-trans-service/operations/';

  
  trans1 = '/us-trans-service/';

  dropdownlist: any = {
    setup: {
      doortype: { url: this.setup + 'doortype', key: { key: 'codeId' , value: 'description' } },
      sbu: { url: this.setup + 'sbu', key: { key: 'codeId' , value: 'description' } },
      uomType: { url: this.setup + 'unitofmeasure', key: { key: 'codeId' , value: 'description' } },
      itemgroup: { url: this.setup + 'itemgroup', key: { key: 'codeId' , value: 'description' } },
      currency: { url: this.setup + 'currency', key: { key: 'codeId' , value: 'description' } },
      nationality: { url: this.setup + 'nationality', key: { key: 'codeId' , value: 'description' } },
      paymentmode: { url: this.setup + 'paymentmode', key: { key: 'codeId' , value: 'description' } },
      itemtype: { url: this.setup + 'itemtype', key: { key: 'codeId' , value: 'description' } },
      phase: { url: this.setup + 'phase', key: { key: 'codeId' , value: 'description' } },
      zone: { url: this.setup + 'zone', key: { key: 'codeId' , value: 'description' } },
      room: { url: this.setup + 'room', key: { key: 'codeId' , value: 'description' } },
      rack: { url: this.setup + 'rack', key: { key: 'codeId' , value: 'description' } },
      bin: { url: this.setup + 'bin', key: { key: 'codeId' , value: 'description' } },
      customertype: { url: this.setup + 'customertype', key: { key: 'codeId' , value: 'description' } },
      customergroup: { url: this.setup + 'customergroup', key: { key: 'codeId' , value: 'description' } },
      servicerendered: { url: this.setup + 'servicerendered', key: { key: 'codeId' , value: 'description' } },
      status: { url: this.setup + 'status', key: { key: 'codeId' , value: 'description' } },
      inquiryStatus: { url: this.setup + 'enquirystatus', key: { key: 'codeId' , value: 'description' } },

      //TRANSACTIONS
      requirementtype: { url: this.setup + 'requirementtype', key: { key: 'codeId' , value: 'description' } },

      //STORAGE
  
      storagetype: { url: this.setup + 'storagetype', key: { key: 'codeId' , value: 'description' } },
      storenumbersize: { url: this.setup + 'storenumbersize', key: { key: 'codeId' , value: 'size' } },
      

      //PAYMENT
      paymenttype: { url: this.setup + 'paymenttype', key: { key: 'codeId' , value: 'description' } },
      modeofpayment: { url: this.setup + 'modeofpayment', key: { key: 'codeId' , value: 'description' } },
      paymentperiod: { url: this.setup + 'paymentperiod', key: { key: 'codeId' , value: 'description' } },
      paymentterm: { url: this.setup + 'paymentterm', key: { key: 'codeId' , value: 'description' } },
      accountstatus: { url: this.setup + 'accountstatus', key: { key: 'codeId' , value: 'description' } },
      
      documentstatus: { url: this.setup + 'documentstatus', key: { key: 'codeId' , value: 'description' } },

      //WORK ORDER
      workorderprocessedby: { url: this.setup + 'workorderprocessedby', key: { key: 'codeId' , value: 'description' } },
      workordercreatedby: { url: this.setup + 'workordercreatedby', key: { key: 'codeId' , value: 'description' } },
      workorderstatus: { url: this.setup + 'workorderstatus', key: { key: 'codeId' , value: 'description' } },

      //INVOICE
      invoicedocumentstatus: { url: this.setup + 'invoicedocumentstatus', key: { key: 'codeId' , value: 'description' } },
      invoicecurrency: { url: this.setup + 'invoicecurrency', key: { key: 'codeId' , value: 'description' } },

      //EMPLOYEE
      
      employee: { url: this.setup + 'employee', key: { key: 'employeeName' , value: 'employeeName' } },

    },



    trans: {
      storageunit: { url: this.trans + 'storageunit', key: { key: 'itemCode', value: 'codeId' } },
      consumables: { url: this.trans + 'consumables', key: { key: 'itemCode', value: 'description' } },
      customerID: { url: this.trans + 'leadcustomer', key: { key: 'customerCode', value: 'customerName' } },
      agreement: { url: this.transOperation + 'agreement', key: { key: 'agreementNumber', value: 'agreementNumber' } },
      workorderNumber: { url: this.transOperation + 'workorder', key: { key: 'workOrderId', value: 'workOrderId' } },
      enquiryIdBy: { url: this.transcrm + 'enquiry', key: { key: 'enquiryId', value: 'enquiryId' } },
      quoteIdBy: { url: this.transcrm + 'quote', key: { key: 'quoteId', value: 'quoteId' } },
      
      //CUSTOMER DROPDOWN
      
      customerDropdown: { url: this.trans1 + 'reports/dropdown/' + 'customerName'},
      storageDropdown: { url: this.trans1 + 'reports/dropdown/' + 'storageUnit'},
    },
  }

  getalldropdownlist(url: string[]) {
    let observableBatch: any[] = [];
    url.forEach((url: string) => { observableBatch.push(this.http.get<any>(url).pipe(catchError(err => of(err)))) });
    return forkJoin(observableBatch);
  }

  foreachlist(list: any, val: { key: any, value: any }, _filter: any = {}, addblank: boolean = false,) {
    let dropdownlist: dropdownelement[] = [];
    let dropdownlist1: dropdownelement1[] = [];
    let filter = list;
    if (_filter)
      filter = this.cs.filterArray(list, _filter);
    if (addblank)
      dropdownlist.push({ key: '', value: '' });
    for (const l of filter) {
      let filter2 = this.cs.filterArray(dropdownlist, { key: l[val.key] })
      if (filter2.length == 0)
      dropdownlist1.push({ value: l[val.value], label: l[val.key] + ' - ' + l[val.value] });
    }
    return dropdownlist1.sort((a, b) => (a.value > b.value) ? 1 : -1);
  }

  foreachlist2(list: any, val: { key: any, value: any }, _filter: any = {}, addblank: boolean = false,) {
    let dropdownlist: dropdownelement[] = [];
    let dropdownlist1: dropdownelement1[] = [];
    let filter = list;
    if (_filter)
      filter = this.cs.filterArray(list, _filter);
    if (addblank)
      dropdownlist.push({ key: '', value: '' });
    for (const l of filter) {
      let filter2 = this.cs.filterArray(dropdownlist, { key: l[val.key] })
      if (filter2.length == 0)
      dropdownlist1.push({ value: l[val.key], label: l[val.key] + ' - ' + l[val.value] });
    }
    return dropdownlist1.sort((a, b) => (a.value > b.value) ? 1 : -1);
  }



  foreachlist1(list: any, val: { key: any, value: any }, _filter: any = {}, addblank: boolean = false,) {
    let dropdownlist: dropdownelement[] = [];
    let dropdownlist1: dropdownelement1[] = [];
    let filter = list;
    if (_filter)
      filter = this.cs.filterArray(list, _filter);
    if (addblank)
      dropdownlist.push({ key: '', value: '' });
    for (const l of filter) {
      let filter2 = this.cs.filterArray(dropdownlist, { key: l[val.key] })
      if (filter2.length == 0)
      dropdownlist1.push({ value: l[val.value], label: l[val.value] });
    }
    return dropdownlist1.sort((a, b) => (a.value > b.value) ? 1 : -1);
  }


  foreachlist3(list: any, val: { key: any, value: any }, _filter: any = {}, addblank: boolean = false,) {
    let dropdownlist: dropdownelement[] = [];
    let dropdownlist1: dropdownelement1[] = [];
    let filter = list;
    if (_filter)
      filter = this.cs.filterArray(list, _filter);
    if (addblank)
      dropdownlist.push({ key: '', value: '' });
    for (const l of filter) {
      let filter2 = this.cs.filterArray(dropdownlist, { key: l[val.key] })
      if (filter2.length == 0)
      dropdownlist1.push({ value: l[val.key], label: l[val.value] });
    }
    return dropdownlist1.sort((a, b) => (a.value > b.value) ? 1 : -1);
  }

  foreachlist_searchpage(list: any, val: { key: any, value: any }, _filter: any = {}, addblank: boolean = false,) {
    let dropdownlist: dropdownelement[] = [];
    let filter = list;

    if (_filter)
      filter = this.cs.filterArray(list, _filter);
    if (addblank)
      dropdownlist.push({ key: '', value: '' });
    for (const l of filter) {
      let filter2 = this.cs.filterArray(dropdownlist, { key: l[val.key] })
      if (filter2.length == 0)
        dropdownlist.push({ key: l[val.key], value: l[val.value] });
    }
    return dropdownlist.sort((a, b) => (a.key > b.key) ? 1 : -1);
  }
  dockerSignStatusUpdatepotentialClientId(url: any[]) {
    let observableBatch: any[] = [];
    url.forEach((url) => { observableBatch.push(this.http.patch<any>(url.url, url.obj).pipe(catchError(err => of(err)))) });
    return forkJoin(observableBatch);
  }

  removeDuplicatesFromArray(list:any = []){
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

  removeDuplicatesFromArray1(list:any = []){
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

}

