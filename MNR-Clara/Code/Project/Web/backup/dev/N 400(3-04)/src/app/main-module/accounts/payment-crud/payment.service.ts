import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  constructor(private http: HttpClient) { }

  apiName = '/mnr-accounting-service/';
  method = 'invoiceheader/paymentUpdate'
  url = this.apiName + this.method;
  Create(obj: any) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: any, code: any,) {
    return this.http.patch<any>('/mnr-accounting-service/invoiceheader/paymentUpdate/' + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.url + '/' + code);
  }

  Search(obj: any) {
    return this.http.post<any>('/mnr-accounting-service/invoiceheader/findPaymentUpdate', obj);
  }
  Get(code: string,) {
    return this.http.get<any>(this.url + '/' + code);
  }
}
