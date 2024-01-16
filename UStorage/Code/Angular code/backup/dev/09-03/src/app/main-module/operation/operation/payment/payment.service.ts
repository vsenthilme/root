import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  constructor(private http: HttpClient, private auth: AuthService) { }
  
  apiName = '/us-trans-service/';
  methodName = 'operations/';
  url = this.apiName + this.methodName;
  Getall() {
    return this.http.get<any>(this.url + `paymentvoucher`);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + `paymentvoucher/` + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.url + `paymentvoucher`, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.url + `paymentvoucher/` + code, obj);
  }
  Delete(paymentVoucher: any,) {
    return this.http.delete<any>(this.url + `paymentvoucher/` + paymentVoucher  );
  }
  search(obj: any) {
    return this.http.post<any>(this.url + 'paymentvoucher/find', obj);
  }


  rentCalculation(obj: any) {
    return this.http.post<any>(this.apiName + `rentCalculation`, obj);
  }
}
