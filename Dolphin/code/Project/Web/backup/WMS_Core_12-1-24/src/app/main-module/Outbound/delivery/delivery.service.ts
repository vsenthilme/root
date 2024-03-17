import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class DeliveryService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/wms-transaction-service/';
  methodName = 'outboundheader';
  url = this.apiName + this.methodName;

  search(obj: any) {
    return this.http.post<any>(this.url + '/findOutboundHeader', obj);
  }
  
  searchSpark(obj: any) {
    return this.http.post<any>( '/wms-spark-service/outboundHeader', obj);
  }
  searchRfd(obj: any) {
    return this.http.post<any>(this.url + '/findOutboundHeader/rfd', obj);
  }
  searchLine(obj: any) {
    return this.http.post<any>(this.apiName + 'outboundline/findOutboundLine', obj);
  }
  searchLinenew(obj: any) {
    return this.http.post<any>(this.apiName + 'outboundline/findOutboundLine-new', obj);
  }
  confirm(data: any,) {
    return this.http.get<any>(this.apiName + 'outboundline/delivery/confirmation?partnerCode=' + data.partnerCode + '&preOutboundNo=' + data.preOutboundNo + '&refDocNumber=' + data.refDocNumber + '&warehouseId=' + this.auth.warehouseId + '&companyCodeId='+ this.auth.companyId +'&plantId='+ this.auth.plantId +'&languageId='+ this.auth.languageId);
  }

  getOtBoundDeliveryFormPdf(orderNumber, warehouseId) {
    return this.http.get<any>(this.apiName + 'reports/shipmentDelivery' + `?orderNumber=${orderNumber}` + '&warehouseId=' + this.auth.warehouseId + '&companyCode='+ this.auth.companyId +'&plantId='+ this.auth.plantId +'&languageId='+ this.auth.languageId);
  }

  GetStoreCode() {
    return this.http.get<any>('/wms-masters-service/businesspartner');
  }

  searchBusinessPartner(obj: any) {
    return this.http.post<any>('/wms-masters-service/businesspartner/findBusinessPartner', obj);
  }

  
  updateOutboundLineBatch(obj: any) {
    return this.http.patch<any>('/wms-transaction-service/outboundline/lineNumbers', obj);
  }
}
