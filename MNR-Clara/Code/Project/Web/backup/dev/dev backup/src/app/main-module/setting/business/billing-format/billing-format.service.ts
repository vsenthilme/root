import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class BillingFormatService {

  constructor(private http: HttpClient) { }

  apiName = '/mnr-setup-service/';

  Getall() {
    return this.http.get<any>(this.apiName + `billingFormat`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `billingFormat/` + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `billingFormat`, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.apiName + `billingFormat` + `?billingFormatId=` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.apiName + `billingFormat/` + code);
  }
}

