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
  GetClientdetails() {
    return this.http.get<any>('/mnr-management-service/clientgeneral/');
  }
  getmatter() {
    return this.http.get<any>('/mnr-management-service/mattergenacc');
  }

  getAllQuotationByPagination(pageNo, pageSize) {
    return this.http.get<any>(this.url + `/pagination?pageNo=${pageNo}&pageSize=${pageSize}`);
  }
}
