import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StorePartnerService {
  constructor(private http: HttpClient) { }
  apiName = '/mnr-cg-transaction-service/';

  Getall() {
    return this.http.get<any>(this.apiName + `storepartnerlisting`);
  }
  Get(code: string,languageId:any,companyId:any) {
    return this.http.get<any>(this.apiName + `storepartnerlisting/` + code+'?languageId='+languageId+'&companyId='+companyId);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `storepartnerlisting`, obj);
  }
  Update(obj: any, code: any,languageId:any,companyId:any) {
    return this.http.patch<any>(this.apiName + `storepartnerlisting/`  + code +'?languageId='+languageId+'&companyId='+companyId, obj);
  }
  Delete(code: any,languageId:any,companyId:any, versionNumber: any) {
    return this.http.delete<any>(this.apiName + `storepartnerlisting/` + code+'?languageId='+languageId+'&companyId='+companyId +'&versionNumber='+versionNumber);
  }
  search(obj:any){
    return this.http.post<any>('/mnr-cg-transaction-service/storepartnerlisting/findStorePartnerListing', obj);
  }
}
