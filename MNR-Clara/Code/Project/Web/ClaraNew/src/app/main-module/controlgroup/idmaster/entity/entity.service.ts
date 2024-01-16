import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class EntityService {
  constructor(private http: HttpClient) {}
  apiName = '/mnr-cg-setup-service/';

  Getall() {
    return this.http.get < any > (this.apiName + `cgentities`);
  }
  Get(code: string, languageId: any, companyId: any,clientId:any) {
    return this.http.get < any > (this.apiName + `cgentity/` + code + '?languageId=' + languageId + '&companyId=' + companyId +'&clientId='+clientId);
  }
  Create(obj: any) {
    return this.http.post < any > (this.apiName + `cgentity`, obj);
  }
  Update(obj: any, code: any, languageId: any, companyId: any,clientId:any) {
    return this.http.patch < any > (this.apiName + `cgentity/` + code + '?languageId=' + languageId + '&companyId=' + companyId+'&clientId='+clientId, obj);
  }
  Delete(code: any, languageId: any, companyId: any,clientId:any) {
    return this.http.delete < any > (this.apiName + `cgentity/` + code + '?languageId=' + languageId + '&companyId=' + companyId+'&clientId='+clientId);
  }
  search(obj: any) {
    return this.http.post < any > ('/mnr-cg-setup-service/cgentity/find', obj);
  }
}

