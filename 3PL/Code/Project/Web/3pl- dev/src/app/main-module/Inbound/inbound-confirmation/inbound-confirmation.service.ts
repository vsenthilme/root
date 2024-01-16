import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class InboundConfirmationService {



  constructor(private http: HttpClient) { }

  apiName = '/wms-transaction-service/';
  methodName = 'inboundheader';  methodName1 = 'inboundline';
  url = this.apiName + this.methodName;
  url1 = this.apiName + this.methodName1;

  search(obj: any) {
    return this.http.post<any>(this.url + '/findInboundHeader/v2', obj);
  }
  searchLine(obj: any) {
    return this.http.post<any>(this.url1 + '/findInboundLine', obj);
  }
  confirm(warehouseId: any, preInboundNo: any, refDocNumber: any) {
    return this.http.patch<any>(this.url + '/confirmIndividual?preInboundNo=' + preInboundNo + '&refDocNumber=' + refDocNumber + '&warehouseId=' + warehouseId, {});
  }
  confirmV2(warehouseId: any, preInboundNo: any, refDocNumber: any,plantId:any,companyCode:any,languageId:any) {
    return this.http.patch<any>(this.url + '/v2/confirmIndividual?preInboundNo=' + preInboundNo + '&refDocNumber=' + refDocNumber + '&warehouseId=' + warehouseId +'&plantId='+plantId+'&companyCode='+companyCode+'&languageId='+languageId, {});
  }
  Get(warehouseId: any, preInboundNo: any, refDocNumber: any, companyCode: any, languageId: any, plantId: any) {
    return this.http.get<any>(this.url + '/v2/' + refDocNumber + '?preInboundNo=' + preInboundNo + '&warehouseId=' + warehouseId + '&companyCode=' + companyCode + '&languageId=' + languageId + '&plantId=' + plantId, {});
  }


  replaceASN(warehouseId: any, preInboundNo: any, asnNumber: any, refDocNumber: any) {
    return this.http.get<any>(this.url + '/replaceASN/v2?asnNumber=' + asnNumber + '&preInboundNo=' + preInboundNo + '&refDocNumber=' + refDocNumber + '&warehouseId=' + warehouseId, {});
  }

  getInboundFormPdf(asnNumber) {
    return this.http.get<any>(this.apiName + 'reports/receiptConfirmation' + `?asnNumber=${asnNumber}&reportFormat='pdf'`);
  }
}
