import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CliententityassignementService {

  constructor(private http: HttpClient) { }
  apiName = '/mnr-cg-setup-service/';

  Getall() {
    return this.http.get<any>(this.apiName + `clientstore`);
  }
  Get(code: string,languageId:any,companyId:any,storeId:any,versionNumber:any) {
    return this.http.get<any>(this.apiName + `ClientStore/` + code+'?languageId='+languageId+'&companyId='+companyId+'&storeId='+storeId+'&versionNumber='+versionNumber);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `clientstore`, obj);
  }
  Update(obj: any, code: any,languageId:any,companyId:any,storeId:any,versionNumber:any) {
    return this.http.patch<any>(this.apiName + `clientstore/`  + code +'?languageId='+languageId+'&companyId='+companyId+'&storeId='+storeId+'&versionNumber='+versionNumber, obj);
  }
  Delete(code: any,languageId:any,companyId:any,storeId:any,versionNumber:any) {
    return this.http.delete<any>(this.apiName + `clientstore/` + code+'?languageId='+languageId+'&companyId='+companyId+'&storeId='+storeId+'&versionNumber='+versionNumber);
  }
  search(obj:any){
    return this.http.post<any>('/mnr-cg-setup-service/clientstore/findClientStore', obj);
  }
}
