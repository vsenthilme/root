import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class QuotationService {


  constructor(private http: HttpClient) { }

  apiName = '/mnr-accounting-service/';
  method = 'quotationheader'
  url = this.apiName + this.method;

  apiName1 = '/mnr-management-service/';
  method1 = 'clientgeneral'
  url1 = this.apiName1 + this.method1;

  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(code: string, quotationRevisionNo: any) {
    return this.http.get<any>(this.url + '/' + code + `?quotationRevisionNo=` + quotationRevisionNo);
  }
  Create(obj: any) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: any, code: any, quotationRevisionNo: any) {
    return this.http.patch<any>(this.url + '/' + code + `?quotationRevisionNo=` + quotationRevisionNo, obj);
  }
  Delete(code: any, quotationRevisionNo: any) {
    return this.http.delete<any>(this.url + '/' + code + `?quotationRevisionNo=` + quotationRevisionNo);
  }

  Search(obj: any) {
    return this.http.post<any>(this.url + '/findQuotationHeader', obj);
  }

  Getstatus() {
    return this.http.get<any>('/mnr-setup-service/status');
  }
  Getquotation(code: string, quotationRevisionNo: any) {
    return this.http.get<any>(this.url + '/' + code + `?quotationRevisionNo=` + quotationRevisionNo);
  }

  Getclient(code: string) {
    return this.http.get<any>(this.url1 + '/' + code);
  }

}
