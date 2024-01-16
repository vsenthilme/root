import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class WorkorderService {

  constructor(private http: HttpClient, private auth: AuthService) { }
  
  apiName = '/us-trans-service/';
  methodName = 'operations/';
  url = this.apiName + this.methodName;
  Getall() {
    return this.http.get<any>(this.url + `workorder`);
  }
  Get(code: string) {
    return this.http.get<any>(this.url + `workorder/` + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.url + `workorder`, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.url + `workorder/` + code, obj);
  }
  Delete(workOrderId: any,) {
    return this.http.delete<any>(this.url + `workorder/` + workOrderId  );
  }
  search(obj: any) {
    return this.http.post<any>(this.url + 'workorder/find', obj);
  }
}
