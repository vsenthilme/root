import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class UsermanagementService {

  constructor(private http: HttpClient, private auth: AuthService) { }
  
  apiName = '/us-master-service/';
  methodName = 'login/';
  url = this.apiName + this.methodName;
  Getall() {
    return this.http.get<any>(this.url + `users`);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + `user/` + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.url + `user`, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.url + `user/` + code, obj);
  }
  Delete(id: any,) {
    return this.http.delete<any>(this.url + `user/` + id);
  }
 
}

