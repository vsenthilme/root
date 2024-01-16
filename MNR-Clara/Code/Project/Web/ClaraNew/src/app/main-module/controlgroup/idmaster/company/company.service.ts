import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {
  constructor(private http: HttpClient) { }

  apiName = '/mnr-cg-setup-service/';

  Getall() {
    return this.http.get<any>(this.apiName + `companyid`);
  }
  Get(code: string,languageId:any) {
    return this.http.get<any>(this.apiName + `companyid/` + code+'?languageId='+languageId);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `companyid`, obj);
  }
  Update(obj: any, code: any,languageId:any) {
    return this.http.patch<any>(this.apiName + `companyid/`  + code +'?languageId='+languageId, obj);
  }
  Delete(code: any,languageId:any) {
    return this.http.delete<any>(this.apiName + `companyid/` + code+'?languageId='+languageId);
  }
  search(obj:any){
    return this.http.post<any>('/mnr-cg-setup-service/companyid/findcompanyid', obj);
  }
  searchlanguage(obj:any){
    return this.http.post<any>('/mnr-cg-setup-service/languageid/findlanguageid', obj);
  }
  searchStorePartner(obj:any){
    return this.http.post<any>('/mnr-cg-transaction-service/storepartnerlisting/findStorePartnerListing', obj);
  }
}