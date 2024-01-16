import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class ExpirationDateService {

  constructor(private http: HttpClient) { }

  apiName = '/mnr-management-service/';
  method = 'expirationdate'
  url = this.apiName + this.method;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(matterNo: any, classId: any, clientId: any, documentType: any, languageId: any) {
    return this.http.get<any>(this.url + '/' + matterNo + '?classId=' + classId + '&clientId=' + clientId + '&documentType=' + documentType + '&languageId=' + languageId);
  }
  Create(obj: any) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: any, matterNo: any) {
    return this.http.patch<any>(this.url + `/` + matterNo, obj);
  }
  Delete(matterNo: any, classId: any, clientId: any, documentType: any, languageId: any) {
    return this.http.delete<any>(this.url + '/' + matterNo + '?classId=' + classId + '&clientId=' + clientId + '&documentType=' + documentType + '&languageId=' + languageId);
  }
  // Search(obj: any) {
  //   return this.http.post<any>(this.url + '/findMatterExpenses', obj);
  // }

}
