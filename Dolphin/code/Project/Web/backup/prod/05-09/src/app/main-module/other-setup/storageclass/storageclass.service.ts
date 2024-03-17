import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class StorageclassService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  Getall() {
    return this.http.get<any>('/wms-idmaster-service/storageclassid');
  }
  Get(statusID: string, warehouseId: string) {
    return this.http.get<any>('/wms-idmaster-service/storageclassid/' + statusID + '?warehouseId=' + warehouseId);
  }
  Create(obj: any) {
    return this.http.post<any>('/wms-idmaster-service/storageclassid', obj);
  }
  Update(obj: any, code: any, warehouseId: any) {
    return this.http.patch<any>('/wms-idmaster-service/storageclassid/' + code + '?warehouseId=' + warehouseId, obj);
  }
  Delete(storageClassId: any,) {
    return this.http.delete<any>('/wms-idmaster-service/storageclassid/' + storageClassId );
  }
}





