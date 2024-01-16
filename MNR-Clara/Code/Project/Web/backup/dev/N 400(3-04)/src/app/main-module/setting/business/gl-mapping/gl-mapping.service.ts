import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class GlMappingService {



  constructor(private http: HttpClient) { }

  apiName = '/mnr-setup-service/';
  method = 'glmappingmaster'
  url = this.apiName + this.method;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(code: string, languageId: any) {
    return this.http.get<any>(this.url + '/' + code + '?languageId=' + languageId);
  }
  Create(obj: any) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: any, code: any, itemNumber: any) {
    return this.http.patch<any>(this.url + `/` + code + '?itemNumber=' + itemNumber, obj);
  }
  Delete(code: any, languageId: any) {
    return this.http.delete<any>(this.url + '/' + code + '?languageId=' + languageId);
  }
}
