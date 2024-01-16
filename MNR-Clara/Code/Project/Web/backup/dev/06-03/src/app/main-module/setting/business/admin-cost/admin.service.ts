import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http: HttpClient) { }

  apiName = '/mnr-setup-service/';

  Getall() {
    return this.http.get<any>(this.apiName + `adminCost`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `adminCost/` + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `adminCost`, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.apiName + `adminCost` + `?adminCostId=` + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.apiName + `adminCost/` + code);
  }
}
