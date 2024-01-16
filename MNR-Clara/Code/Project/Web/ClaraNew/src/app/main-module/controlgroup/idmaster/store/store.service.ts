import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';


@Injectable({
  providedIn: 'root'
})
export class StoreService {
  constructor(private http: HttpClient) { }
  apiName = '/mnr-cg-setup-service/';

  Getall() {
    return this.http.get<any>(this.apiName + `store`);
  }
  Get(code: string,languageId:any,companyId:any) {
    return this.http.get<any>(this.apiName + `store/` + code+'?languageId='+languageId+'&companyId='+companyId);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `store`, obj);
  }
  Update(obj: any, code: any,languageId:any,companyId:any) {
    return this.http.patch<any>(this.apiName + `store/`  + code +'?languageId='+languageId+'&companyId='+companyId, obj);
  }
  Delete(code: any,languageId:any,companyId:any) {
    return this.http.delete<any>(this.apiName + `store/` + code+'?languageId='+languageId+'&companyId='+companyId);
  }
  search(obj:any){
    return this.http.post<any>('/mnr-cg-setup-service/store/findStore', obj);
  }

  searchStorePartner(obj:any){
    return this.http.post<any>('/mnr-cg-transaction-service/storepartnerlisting/findStorePartnerListing', obj);
  }
}
