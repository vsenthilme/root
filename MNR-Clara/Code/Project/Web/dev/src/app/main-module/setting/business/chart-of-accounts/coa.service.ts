import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CoaService {


  constructor(private http: HttpClient) { }

  apiName = '/mnr-setup-service/';

  Getall() {
    return this.http.get<any>(this.apiName + `chartOfAccounts`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `chartOfAccounts/` + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `chartOfAccounts`, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.apiName + `chartOfAccounts` + `?accountNumber=` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.apiName + `chartOfAccounts/` + code);
  }
}
