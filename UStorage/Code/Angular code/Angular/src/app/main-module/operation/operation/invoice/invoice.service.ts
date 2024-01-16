import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class InvoiceService {

  constructor(private http: HttpClient, private auth: AuthService) { }
  
  apiName = '/us-trans-service/';
  methodName = 'operations/';
  url = this.apiName + this.methodName;
  Getall() {
    return this.http.get<any>(this.url + `invoice`);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + `invoice/` + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.url + `invoice`, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.url + `invoice/` + code, obj);
  }
  Delete(invoiceNumber: any,) {
    return this.http.delete<any>(this.url + `invoice/` + invoiceNumber  );
  }
  search(obj: any) {
    return this.http.post<any>(this.url + 'invoice/find', obj);
  }

  rentCalculation(obj: any) {
    return this.http.post<any>(this.apiName + `rentCalculation`, obj);
  }
}
