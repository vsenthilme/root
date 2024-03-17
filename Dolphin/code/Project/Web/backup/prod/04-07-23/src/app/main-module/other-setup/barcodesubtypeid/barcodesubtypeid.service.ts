import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class BarcodesubtypeidService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  Getall() {
    return this.http.get<any>('/wms-idmaster-service/barcodesubtypeid');
  }
  Get(statusID: string, warehouseId: string) {
    return this.http.get<any>('/wms-idmaster-service/barcodesubtypeid' + statusID + '?warehouseId=' + warehouseId);
  }
  Create(obj: any) {
    return this.http.post<any>('/wms-idmaster-service/barcodesubtypeid', obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>('/wms-idmaster-service/barcodesubtypeid' + code, obj);
  }
  Delete({barcodeSubTypeId}: any,) {
    return this.http.delete<any>('/wms-idmaster-service/barcodesubtypeid' + {barcodeSubTypeId});
}

}
