import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class LoyaltyService {

  constructor(private http: HttpClient, private auth: AuthService) { }
  
  apiName = '/mv-master-service/';
  Getall() {
    return this.http.get<any>(this.apiName + `loyaltycategory`);
  }
  Get(rangeId: string) {
    return this.http.get<any>(this.apiName + `loyaltycategory/` + rangeId);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `loyaltycategory`, obj);
  }
  Update(rangeId: any, obj: any) {
    return this.http.patch<any>(this.apiName + `loyaltycategory/` + rangeId, obj);
  }

  Delete(rangeId: any) {
    return this.http.delete<any>(this.apiName + `loyaltycategory/` + rangeId);
  }


  SetupGetall() {
    return this.http.get<any>(this.apiName + `loyaltysetup`);
  }
  SetupGet(code: string, companyId: any, languageId: any, categoryId: any) {
    return this.http.get<any>(this.apiName + `loyaltysetup/` + code  + '?companyId=' + companyId + '&languageId=' + languageId + '&categoryId=' + categoryId);
  }
  SetupCreate(obj: any) {
    return this.http.post<any>(this.apiName + `loyaltysetup`, obj);
  }
  SetupUpdate(code: string, companyId: any, languageId: any,  categoryId: any, obj: any) {
    return this.http.patch<any>(this.apiName + `loyaltysetup/` + code  + '?companyId=' + companyId + '&languageId=' + languageId + '&categoryId=' + categoryId, obj);
  }

  SetupDelete(customer: any, companyId: any, languageId: any, categoryId: any) {
    return this.http.delete<any>(this.apiName + `loyaltysetup/` + customer  + '?companyId=' + companyId + '&languageId=' + languageId + '&categoryId=' + categoryId);
  }

}
