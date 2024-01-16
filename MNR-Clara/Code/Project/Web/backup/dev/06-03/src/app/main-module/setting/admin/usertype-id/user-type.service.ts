import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class UserTypeService {


  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/mnr-setup-service/' + 'userType';
  Getall() {
    return this.http.get<any>(this.apiName);
  }
  Get(code: string, classId: any, languageId: any) {
    return this.http.get<any>(this.apiName + `/` + code + '?classId=' + classId + '&languageId=' + languageId);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.apiName + '?userTypeId=' + code, obj);
  }
  Delete(code: any) {
    return this.http.delete<any>(this.apiName + `/` + code);
  }
}
