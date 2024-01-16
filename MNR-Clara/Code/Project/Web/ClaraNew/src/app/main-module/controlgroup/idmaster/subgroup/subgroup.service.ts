import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';



@Injectable({
  providedIn: 'root'
})
export class SubgroupService {
  constructor(private http: HttpClient) { }
  apiName = '/mnr-cg-setup-service/';

  Getall() {
    return this.http.get<any>(this.apiName + `subgrouptype`);
  }
  Get(code: string,languageId:any,companyId:any,groupTypeId:any,versionNumber:any) {
    return this.http.get<any>(this.apiName + `subgrouptype/` + code+'?languageId='+languageId+'&companyId='+companyId +'&groupTypeId='+groupTypeId+'&versionNumber='+versionNumber);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `subgrouptype`, obj);
  }
  Update(obj: any, code: any,languageId:any,companyId:any,groupTypeId:any,versionNumber:any) {
    return this.http.patch<any>(this.apiName + `subGrouptype/`  + code +'?languageId='+languageId+'&companyId='+companyId+'&groupTypeId='+groupTypeId+'&versionNumber='+versionNumber, obj);
  }
  Delete(code: any,languageId:any,companyId:any,groupTypeId:any,versionNumber:any) {
    return this.http.delete<any>(this.apiName + `subgrouptype/` + code+'?languageId='+languageId+'&companyId='+companyId+'&groupTypeId='+groupTypeId +'&versionNumber='+versionNumber);
  }
  search(obj:any){
    return this.http.post<any>('/mnr-cg-setup-service/subgrouptype/findSubGroupType', obj);
  }
  searchStorePartner(obj:any){
    return this.http.post<any>('/mnr-cg-transaction-service/storepartnerlisting/findStorePartnerListing', obj);
  }
}