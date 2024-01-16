import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from "rxjs/operators";
@Injectable({
  providedIn: 'root'
})
export class PreoutboundService {

  constructor(private http: HttpClient) { }

  apiName = '/wms-transaction-service/';
  methodName = 'preoutboundheader';
  url = this.apiName + this.methodName;

  search(obj: any) {
    return this.http.post<any>(this.url + '/findPreOutboundHeader', obj);
  }
  searchNew(obj: any) {
    return this.http.post<any>(this.url + '/findPreOutboundHeaderNew', obj);
  }
  searchspeed(obj: any) {
    return this.http.post<any>(this.url + '/findPreOutboundHeader', obj).toPromise();
  }
  searchLine(obj: any) {
    return this.http.post<any>(this.apiName + 'preoutboundline/findPreOutboundLine', obj);
  }
  GetStoreCode() {
    return this.http.get<any>('/wms-masters-service/businesspartner');
  }

  createShipmentOrder(obj: any) {
    return this.http.post<any>('/wms-transaction-service/warehouse/outbound/so', obj);
  }


  updateOutBoundLine(lineNumber: any,itemCode:any,partnerCode: any,preOutboundNo:any,refDocNumber:any,warehouseId: any, obj: any) {
    return this.http.patch<any>('/wms-transaction-service/outboundline/'+ lineNumber + '?itemCode=' + itemCode +  '&partnerCode=' + partnerCode +  '&preOutboundNo=' + preOutboundNo +  '&refDocNumber=' + refDocNumber  +  '&warehouseId=' + warehouseId  ,obj);
  }

}
