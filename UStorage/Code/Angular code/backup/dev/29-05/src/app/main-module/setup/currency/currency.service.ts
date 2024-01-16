import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class CurrencyService {
  constructor(private http: HttpClient, private auth: AuthService) { }
  
  apiName = '/us-master-service/';
  methodName = 'crm/';
  url = this.apiName + this.methodName;
  Getall() {
    return this.http.get<any>(this.apiName + `currency`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `currency/` + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `currency`, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.apiName + `currency/` + code, obj);
  }
  Delete(currencyId: any,) {
    return this.http.delete<any>(this.apiName + `currency/` + currencyId  );
  }
 
}

