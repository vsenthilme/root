import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class StoragetypeService {

 
  constructor(private http: HttpClient, private auth: AuthService) { }

  Getall() {
    return this.http.get<any>('/wms-idmaster-service/stroagetypeid');
  }
  Get(statusID: string, warehouseId: string) {
    return this.http.get<any>('/wms-idmaster-service/stroagetypeid/' + statusID + '?warehouseId=' + warehouseId);
  }
  Create(obj: any) {
    return this.http.post<any>('/wms-idmaster-service/stroagetypeid', obj);
  }
  Update(obj: any, code: any, warehouseId: any) {
    return this.http.patch<any>('/wms-idmaster-service/stroagetypeid/' + code + '?warehouseId=' + warehouseId, obj);
  }
  Delete(storageTypeId: any,) {
    return this.http.delete<any>('/wms-idmaster-service/stroagetypeid/' + storageTypeId );
  }
}





