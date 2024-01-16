import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class UserprofileService {
  constructor(private http: HttpClient, private auth: AuthService) { }
  
  apiName = '/mv-master-service/';
  Getall() {
    return this.http.get<any>(this.apiName + `login/users`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `login/user/` + code );
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `login/user`, obj);
  }
  Update(code: string, obj: any) {
    return this.http.patch<any>(this.apiName + `login/user/` + code , obj);
  }

  Delete(id: any,) {
    return this.http.delete<any>(this.apiName + `login/user/` + id);
  }

}
