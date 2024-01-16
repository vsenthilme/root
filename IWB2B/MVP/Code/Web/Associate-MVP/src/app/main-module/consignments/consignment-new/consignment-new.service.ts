import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class ConsignmentNewService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  
  apiName = '/mv-master-service/';
  Getall() {
    return this.http.get<any>(this.apiName + `orderdetailsheader`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `orderdetailsheader/` + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `orderdetailsheader`, obj);
  }
  Update(code: string, obj: any) {
    return this.http.patch<any>(this.apiName + `orderdetailsheader/` + code, obj);
  }

  Delete(code: string) {
    return this.http.delete<any>(this.apiName + `orderdetailsheader/` + code);
  }


}


