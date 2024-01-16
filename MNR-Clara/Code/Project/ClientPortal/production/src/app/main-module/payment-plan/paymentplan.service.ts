import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PaymentplanService {

  

  constructor(private http: HttpClient) { }

  apiName = '/mnr-accounting-service/';
  method = 'paymentplanheader'
  url = this.apiName + this.method;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(code: string, paymentPlanRevisionNo: any) {
    return this.http.get<any>(this.url + '/' + code + `?paymentPlanRevisionNo=` + paymentPlanRevisionNo);
  }
  Create(obj: any) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: any, code: any, paymentPlanRevisionNo: any) {
    return this.http.patch<any>(this.url + '?paymentPlanNumber=' + code + `&paymentPlanRevisionNo=` + paymentPlanRevisionNo, obj);
  }
  Delete(code: any, paymentPlanRevisionNo: any) {
    return this.http.delete<any>(this.url + '/' + code + `?paymentPlanRevisionNo=` + paymentPlanRevisionNo);
  }

  Search(obj: any) {
    return this.http.post<any>(this.url + '/findPaymentPlanHeader', obj);
  }

  Getstatus() {
    return this.http.get<any>('/mnr-setup-service/status');
  }
}
