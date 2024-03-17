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
    return this.http.post<any>(this.url + '/findInboundHeader', obj);
  }
  searchSpark(obj: any) {
    return this.http.post<any>( '/wms-spark-service/inboundHeader', obj);
  }
  searchLine(obj: any) {
    return this.http.post<any>(this.url1 + '/findInboundLine', obj);
  }
  searchLineSpark(obj: any) {
    return this.http.post<any>( '/wms-spark-service/inboundline', obj);
  }
  confirm(warehouseId: any, preInboundNo: any, refDocNumber: any,plantId:any,companyCode:any,languageId:any) {
    return this.http.patch<any>(this.url + '/confirmIndividual?preInboundNo=' + preInboundNo + '&refDocNumber=' + refDocNumber + '&warehouseId=' + warehouseId +'&plantId='+plantId+'&companyCode='+companyCode+'&languageId='+languageId, {});
  }
  Get(warehouseId: any, preInboundNo: any, refDocNumber: any, companyCode: any, languageId: any, plantId: any) {
    return this.http.get<any>(this.url + '/' + refDocNumber + '?preInboundNo=' + preInboundNo + '&warehouseId=' + warehouseId + '&companyCode=' + companyCode + '&languageId=' + languageId + '&plantId=' + plantId, {});
  }
  Get1(warehouseId: any, preInboundNo: any, refDocNumber: any, companyCode: any, languageId: any, plantId: any,itemCode:any,lineNo:any) {
    return this.http.get<any>(this.url1 + '/' +lineNo+'?refDocNumber='+refDocNumber + '&preInboundNo=' + preInboundNo + '&warehouseId=' + warehouseId + '&companyCode=' + companyCode + '&languageId=' + languageId + '&plantId=' + plantId+'&itemCode='+itemCode, {});
  }

  replaceASN(warehouseId: any, preInboundNo: any, asnNumber: any, refDocNumber: any) {
    return this.http.get<any>(this.url + '/replaceASN?asnNumber=' + asnNumber + '&preInboundNo=' + preInboundNo + '&refDocNumber=' + refDocNumber + '&warehouseId=' + warehouseId, {});
  }

  getInboundFormPdf(asnNumber) {
    return this.http.get<any>(this.apiName + 'reports/receiptConfirmation' + `?asnNumber=${asnNumber}&reportFormat='pdf'`);
  }

  updateinboundHeader(obj:any,refDocNumber: any, warehouseId: any,companyCode:any,plantId:any,languageId:any,preInboundNo:any) {
    return this.http.patch<any>(this.url + '/' + refDocNumber  + '?warehouseId=' + warehouseId+'&companyCode='+companyCode+'&plantId='+plantId+'&languageId='+languageId+'&preInboundNo='+preInboundNo,obj);
  }
  updateinboundline(obj:any,refDocNumber: any, warehouseId: any,companyCode:any,plantId:any,languageId:any,preInboundNo:any,lineNo:any,itemCode:any) {
    return this.http.patch<any>(this.url1 + '?refDocNumber='+refDocNumber  + '&warehouseId=' + warehouseId+'&companyCode='+companyCode+'&plantId='+plantId+'&languageId='+languageId+'&preInboundNo='+preInboundNo+'&lineNo='+lineNo+'&itemCode='+itemCode,obj);
  }
}
