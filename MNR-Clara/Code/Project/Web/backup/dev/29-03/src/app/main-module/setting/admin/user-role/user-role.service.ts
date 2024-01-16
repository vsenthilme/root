import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";


@Injectable({
  providedIn: 'root'
})
export class UserRoleService {


  constructor(private http: HttpClient) { }

  apiName = '/mnr-setup-service/' + 'userRole';
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
