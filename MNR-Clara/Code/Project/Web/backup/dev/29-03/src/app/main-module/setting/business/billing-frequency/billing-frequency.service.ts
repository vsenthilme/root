import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class BillingFrequencyService {

  constructor(private http: HttpClient) { }

  apiName = '/mnr-setup-service/';

  Getall() {
    return this.http.get<any>(this.apiName + `billingFrequency`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `billingFrequency/` + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `billingFrequency`, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.apiName + `billingFrequency` + `?billingFrequencyId=` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.apiName + `billingFrequency/` + code);
  }
}
