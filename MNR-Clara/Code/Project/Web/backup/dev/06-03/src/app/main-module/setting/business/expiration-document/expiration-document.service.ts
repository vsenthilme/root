import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';


@Injectable({
  providedIn: 'root'
})
export class ExpirationDocumentService {



  constructor(private http: HttpClient) { }

  apiName = '/mnr-setup-service/';
  method = 'expirationdoctype'
  url = this.apiName + this.method;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(code: string, classId: any, languageId: any) {
    return this.http.get<any>(this.url + '/' + code + '?classId=' + classId + '&languageId=' + languageId);
  }
  Create(obj: any) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.url + `/` + code, obj);
  }
  Delete(code: any, classId: any, languageId: any) {
    return this.http.delete<any>(this.url + '?classId=' + classId + '&documentType=' + code + '&languageId=' + languageId);
  }
}
