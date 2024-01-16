import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class AgreementService {

  constructor(private http: HttpClient, private auth: AuthService) { }
  
  apiName = '/us-trans-service/';
  methodName = 'operations/';
  url = this.apiName + this.methodName;
  Getall() {
    return this.http.get<any>(this.url + `agreement`);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + `agreement/` + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.url + `agreement`, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.url + `agreement/` + code, obj);
  }
  Delete(agreement: any,) {
    return this.http.delete<any>(this.url + `agreement/` + agreement  );
  }
  search(obj: any) {
    return this.http.post<any>(this.url + 'agreement/find', obj);
  }
  finStoreNumber(obj: any) {
    return this.http.post<any>(this.url + 'agreement/findStoreNumber', obj);
}

rentCalculation(obj: any) {
  return this.http.post<any>(this.apiName + `rentCalculation`, obj);
}

}