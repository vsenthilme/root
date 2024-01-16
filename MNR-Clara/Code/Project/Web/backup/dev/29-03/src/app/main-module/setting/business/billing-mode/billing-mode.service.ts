import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class BillingModeService {
  constructor(private http: HttpClient) { }

  apiName = '/mnr-setup-service/';

  Getall() {
    return this.http.get<any>(this.apiName + `billingMode`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `billingMode/` + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `billingMode`, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.apiName + `billingMode` + `/{billingModeId}?billingModeId=` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.apiName + `billingMode/` + code);
  }
}

