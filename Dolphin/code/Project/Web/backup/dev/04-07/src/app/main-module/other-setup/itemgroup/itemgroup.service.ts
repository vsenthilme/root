import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class ItemgroupService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  Getall() {
    return this.http.get<any>('/wms-idmaster-service/itemgroupid');
  }
  Get(statusID: string, warehouseId: string) {
    return this.http.get<any>('/wms-idmaster-service/itemgroupid' + statusID + '?warehouseId=' + warehouseId);
  }
  Create(obj: any) {
    return this.http.post<any>('/wms-idmaster-service/itemgroupid', obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>('/wms-idmaster-service/itemgroupid' + code, obj);
  }
  Delete(itemGroupId: any,) {
    return this.http.delete<any>('/wms-idmaster-service/itemgroupid' + itemGroupId);
}

}

