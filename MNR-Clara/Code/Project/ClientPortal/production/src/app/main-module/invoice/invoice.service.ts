import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class InvoiceService {

  constructor(private http: HttpClient) { }

  apiName = '/mnr-accounting-service/';
  method = 'invoiceheader'
  url = this.apiName + this.method;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + '/' + code);
  }
  Create(obj: any, preBillNumber: any) {
    return this.http.post<any>(this.url + '?preBillNumber=' + preBillNumber, obj);
  }
  search(obj: any,) {
    return this.http.post<any>(this.url + '/findInvoiceHeader', obj);
  }

  Getstatus() {
    return this.http.get<any>('/mnr-setup-service/status');
  }

  getInvoicePdfData(invoiceNumber: any) {
    return this.http.get<any>(this.apiName + 'outputforms/invoice' + `?invoiceNumber=${invoiceNumber}`);
  }
  
}
