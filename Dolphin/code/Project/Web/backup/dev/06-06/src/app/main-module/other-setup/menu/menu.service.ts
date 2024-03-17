import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class MenuService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  Getall() {
    return this.http.get<any>('/wms-idmaster-service/menuid');
  }
  Get(code: string) {
    return this.http.get<any>('/wms-idmaster-service/menuid' + code);
  }
  Create(obj: any) {
    return this.http.post<any>('/wms-idmaster-service/menuid', obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>('/wms-idmaster-service/menuid' + code, obj);
  }
  Delete(menuId: any,) {
    return this.http.delete<any>('/wms-idmaster-service/menuid' + menuId );
  }
}


