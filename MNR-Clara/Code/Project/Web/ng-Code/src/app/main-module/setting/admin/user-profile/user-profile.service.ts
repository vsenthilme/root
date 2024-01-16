import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class UserProfileService {


  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/mnr-setup-service/' + 'userProfile';
  Getall() {
    return this.http.get<any>(this.apiName);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `/` + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.apiName + '/' + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.apiName + `/` + code);
  }
}
