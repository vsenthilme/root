import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  constructor(private http: HttpClient, private auth: AuthService) { }
  
  apiName = '/mv-master-service/';
  Getall() {
    return this.http.get<any>(this.apiName + `customer`);
  }
  Get(code: string, companyId: any, languageId: any) {
    return this.http.get<any>(this.apiName + `customer/` + code  + '?companyId=' + companyId + '&languageId=' + languageId);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `customer`, obj);
  }
  Update(code: string, companyId: any, languageId: any, obj: any) {
    return this.http.patch<any>(this.apiName + `customer/` + code  + '?companyId=' + companyId + '&languageId=' + languageId, obj);
  }
  Delete(customer: any, companyId: any, languageId: any) {
    return this.http.delete<any>(this.apiName + `customer/` + customer  + '?companyId=' + companyId + '&languageId=' + languageId);
  }

  search(obj: any) {
    return this.http.post<any>(this.apiName + 'customer/findCustomer', obj);
  }
}
