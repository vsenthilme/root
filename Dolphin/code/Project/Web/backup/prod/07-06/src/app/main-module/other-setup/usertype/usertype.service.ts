import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class UsertypeService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  Getall() {
    return this.http.get<any>('/wms-idmaster-service/usertypeid');
  }
  Get(statusID: string, warehouseId: string) {
    return this.http.get<any>('/wms-idmaster-service/usertypeid' + statusID + '?warehouseId=' + warehouseId);
  }
  Create(obj: any) {
    return this.http.post<any>('/wms-idmaster-service/usertypeid', obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>('/wms-idmaster-service/usertypeid' + code ,obj);
  }
  Delete(userTypeId: any,) {
    return this.http.delete<any>('/wms-idmaster-service/usertypeid' + userTypeId);
  }
}






