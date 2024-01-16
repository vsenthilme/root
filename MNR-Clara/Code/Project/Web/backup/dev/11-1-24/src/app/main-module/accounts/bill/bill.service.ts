import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class BillService {
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
  Create(obj: any,) {
    return this.http.post<any>(this.url, obj);
  }

  Update(obj: any, code: any) {
    return this.http.patch<any>(this.url + '/' + code, obj);
  }
  search(obj: any,) {
    return this.http.post<any>(this.url + '/findInvoiceHeader', obj);
  }

  invoiceExecute(obj: any,) {
    return this.http.post<any>(this.url + '/invoiceExecute', obj);
  }
  getmatter() {
    return this.http.get<any>('/mnr-management-service/mattergenacc');
  }
  getprebill() {
    return this.http.get<any>('/mnr-accounting-service/prebilldetails/preBillNumber');
  }
  getprebillbatch() {
    return this.http.get<any>('/mnr-accounting-service/prebilldetails/preBillNumber');
  }
  GetClientdetails() {
    return this.http.get<any>('/mnr-management-service/clientgeneral/');
  }
  getInvoicePdfData(invoiceNumber) {
    return this.http.get<any>(this.apiName + 'outputforms/invoice' + `?invoiceNumber=${invoiceNumber}`);
  }

  Search(obj: any) {
    return this.http.post<any>(this.url + '/findInvoiceHeader', obj);
  }

  SearchNew(obj: any) {
    return this.http.post<any>('/mnr-spark-service/invoiceheader/findInvocieHeader', obj);
  }
  
  getAllInvociecByPagination(pageNo, pageSize) {
    return this.http.get<any>(this.url + `/pagination?pageNo=${pageNo}&pageSize=${pageSize}`);
  }

  delete(invoiceNumber: any) {
    return this.http.delete<any>(this.url + '/' + invoiceNumber);
  }


    //uploadInvoiceQuotation
  public uploadfile(file: File, location) {
    let formParams = new FormData();
    formParams.append('file', file)
    return this.http.post('/doc-storage/upload?' + `location=${location}`, formParams)
  }
  
}
