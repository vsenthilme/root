import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class InboundConfirmationService {



  constructor(private http: HttpClient) { }

  apiName = '/wms-transaction-service/';
  methodName = 'inboundheader';
  methodName1 = 'inboundline';
  url = this.apiName + this.methodName;
url1 = this.apiName + this.methodName1;

  search(obj: any) {
    return this.http.post<any>(this.url + '/findInboundHeader', obj);
  }
  searchSpark(obj: any) {
    return this.http.post<any>('/mnr-spark-service/findInboundHeader', obj);
  }

  searchLine(obj: any) {
    return this.http.post<any>(this.url1 + '/findInboundLine', obj);
  }
  confirm(warehouseId: any, preInboundNo: any, refDocNumber: any) {
    return this.http.patch<any>(this.url + '/confirmIndividual?preInboundNo=' + preInboundNo + '&refDocNumber=' + refDocNumber + '&warehouseId=' + warehouseId, {});
  }
  Get(warehouseId: any, preInboundNo: any, refDocNumber: any) {
    return this.http.get<any>(this.url + '/' + refDocNumber + '?preInboundNo=' + preInboundNo + '&warehouseId=' + warehouseId, {});
  }


  replaceASN(warehouseId: any, preInboundNo: any, asnNumber: any, refDocNumber: any) {
    return this.http.get<any>(this.url + '/replaceASN?asnNumber=' + asnNumber + '&preInboundNo=' + preInboundNo + '&refDocNumber=' + refDocNumber + '&warehouseId=' + warehouseId, {});
  }

  getInboundFormPdf(asnNumber) {
    return this.http.get<any>(this.apiName + 'reports/receiptConfirmation' + `?asnNumber=${asnNumber}&reportFormat='pdf'`);
  }
}
