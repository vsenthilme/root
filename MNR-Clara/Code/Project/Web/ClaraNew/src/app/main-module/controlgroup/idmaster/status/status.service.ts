import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StatusService {
  constructor(private http: HttpClient) { }

  apiName = '/mnr-cg-setup-service/';

  Getall() {
    return this.http.get<any>(this.apiName + `status`);
  }
  Get(code: string,languageId:any) {
    return this.http.get<any>(this.apiName + `StatusId/` + code+'?languageId='+languageId);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `statusId`, obj);
  }
  Update(obj: any, code: any,languageId:any) {
    return this.http.patch<any>(this.apiName + `StatusId/`  + code +'?languageId='+languageId, obj);
  }
  Delete(code: any,languageId:any) {
    return this.http.delete<any>(this.apiName + `StatusId/` + code+'?languageId='+languageId);
  }
  search(obj:any){
    return this.http.post<any>('/mnr-cg-setup-service/statusId/findStatusId', obj);
  }
  searchlanguage(obj:any){
    return this.http.post<any>('/mnr-cg-setup-service/languageid/findlanguageid', obj);
  }
}
