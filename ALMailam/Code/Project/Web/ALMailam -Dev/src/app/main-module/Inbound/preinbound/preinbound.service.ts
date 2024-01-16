import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PreinboundService {



  constructor(private http: HttpClient) { }

  apiName = '/wms-transaction-service/';
  methodName = 'preinboundheader';
  url = this.apiName + this.methodName;
  Getall() {
    return this.http.get<any>(this.url + '/v2');
  }
  Get(preInboundNo: any, warehouseId: any, companyCode, plantId, languageId) {
    return this.http.get<any>(this.url + '/' + preInboundNo + '/v2' + '?warehouseId=' + warehouseId + '&companyCode=' + companyCode + '&plantId=' + plantId + '&languageId=' + languageId);
  }
  Create(obj: any) {
    return this.http.post<any>(this.url + '/v2', obj);
  }
  Update(obj: any, preInboundNo: any, warehouseId: any) {
    return this.http.patch<any>(this.url + '/' + preInboundNo + '/v2' + '?warehouseId=' + warehouseId, obj);
  }
  Delete(preInboundNo: any, warehouseId: any) {
    return this.http.delete<any>(this.url + '/' + preInboundNo  + '/v2' + '?warehouseId=' + warehouseId);
  }
  search(obj: any) {
    return this.http.post<any>(this.url + '/findPreInboundHeader/v2', obj);
  }
  Getline(preInboundNo: any) {
    return this.http.get<any>(this.apiName + 'preinboundline/' + preInboundNo + '/v2');
  }

  processASN(obj: any,) {
    return this.http.post<any>(this.url + '/processASN', obj);
    //return this.http.get<any>(this.url + '/' + preInboundNo + '/processASN?containerNo=' + containerNo + '&invoiceNo=' + invoiceNo);
  }

  createbom(itemCode: any, lineNo: any, preInboundNo: any, refDocNumber: any, warehouseId: any) {
    return this.http.post<any>(this.apiName + 'preinboundline/bom?itemCode=' + itemCode + '&lineNo=' + lineNo + '&preInboundNo=' + preInboundNo + '&refDocNumber=' + refDocNumber + '&warehouseId=' + warehouseId, {});
  }
  Getcontainer() {
    return this.http.get<any>('/wms-transaction-service/containerreceipt');
  }


  
  createAsnOrder(obj: any) {
    return this.http.post<any>('/wms-transaction-service/warehouse/inbound/asn', obj);
  }
}
