import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class QuoteService {

  constructor(private http: HttpClient, private auth: AuthService) { }


  apiName = '/us-trans-service/';
  methodName = 'crm/';
  url = this.apiName + this.methodName;
  Getall() {
    return this.http.get<any>(this.url + `quote`);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + `quote/` + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.url + `quote`, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.url + `quote/` + code, obj);
  }
  Delete(quote: any,) {
    return this.http.delete<any>(this.url + `quote/` + quote  );
  }
  search(obj: any) {
    return this.http.post<any>(this.url + 'quote/find', obj);
  }
 
}
