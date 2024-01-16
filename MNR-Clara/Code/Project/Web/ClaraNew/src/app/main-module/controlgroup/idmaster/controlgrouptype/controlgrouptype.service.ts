import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ControlgrouptypeService {
  constructor(private http: HttpClient) { }
  apiName = '/mnr-cg-setup-service/';

  Getall() {
    return this.http.get<any>(this.apiName + `controlgrouptype`);
  }
  Get(code: string,languageId:any,companyId:any,versionNumber:any) {
    return this.http.get<any>(this.apiName + `controlgrouptype/` + code+'?languageId='+languageId+'&companyId='+companyId+'&versionNumber='+versionNumber);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `controlgrouptype`, obj);
  }
  Update(obj: any, code: any,languageId:any,companyId:any,versionNumber:any) {
    return this.http.patch<any>(this.apiName + `controlgrouptype/`  + code +'?languageId='+languageId+'&companyId='+companyId+'&versionNumber='+versionNumber, obj);
  }
  Delete(code: any,languageId:any,companyId:any,versionNumber:any) {
    return this.http.delete<any>(this.apiName + `controlgrouptype/` + code+'?languageId='+languageId+'&companyId='+companyId+'&versionNumber='+versionNumber);
  }
  search(obj:any){
    return this.http.post<any>('/mnr-cg-setup-service/controlgrouptype/findControlGroupType', obj);
  }
  searchStorePartner(obj:any){
    return this.http.post<any>('/mnr-cg-transaction-service/storepartnerlisting/findStorePartnerListing', obj);
  }
}
