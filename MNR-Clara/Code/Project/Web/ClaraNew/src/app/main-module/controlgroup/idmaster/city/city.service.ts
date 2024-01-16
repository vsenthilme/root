import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CityService {
  constructor(private http: HttpClient) { }
  apiName = '/mnr-cg-setup-service/';

  Getall() {
    return this.http.get<any>(this.apiName + `city`);
  }
  Get(code: string,languageId:any,companyId:any,countryId:any,stateId:any) {
    return this.http.get<any>(this.apiName + `city/` + code+'?languageId='+languageId+'&companyId='+companyId+'&countryId='+countryId+'&stateId='+stateId);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `city`, obj);
  }
  Update(obj: any, code: any,languageId:any,companyId:any,countryId:any,stateId:any) {
    return this.http.patch<any>(this.apiName + `city/`  + code +'?languageId='+languageId+'&companyId='+companyId+'&countryId='+countryId+'&stateId='+stateId, obj);
  }
  Delete(code: any,languageId:any,companyId:any,countryId:any,stateId:any) {
    return this.http.delete<any>(this.apiName + `city/` + code+'?languageId='+languageId+'&companyId='+companyId+'&countryId='+countryId+'&stateId='+stateId);
  }
  search(obj:any){
    return this.http.post<any>('/mnr-cg-setup-service/city/findcity', obj);
  }
}
