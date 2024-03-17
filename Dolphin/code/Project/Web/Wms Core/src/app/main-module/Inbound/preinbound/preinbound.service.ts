import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PreinboundService {



  constructor(private http: HttpClient) { }

  apiName = '/wms-transaction-service/';
  methodName = 'preinboundheader';
  methodName1='preinboundline';
  methodName2='stagingline'
  url = this.apiName + this.methodName;
  url2=this.apiName+this.methodName1;
  url3=this.apiName+this.methodName2;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(preInboundNo: any, warehouseId: any, companyCode, plantId, languageId) {
    return this.http.get<any>(this.url + '/' + preInboundNo + '?warehouseId=' + warehouseId + '&companyCode=' + companyCode + '&plantId=' + plantId + '&languageId=' + languageId);
  }
  Create(obj: any) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: any, preInboundNo: any, warehouseId: any, companyCode, plantId, languageId) {
    return this.http.patch<any>(this.url + '/' + preInboundNo + '?warehouseId=' + warehouseId+'&companyCode=' + companyCode + '&plantId=' + plantId + '&languageId=' + languageId, obj);
  }
  Updateline(obj: any, preInboundNo: any, warehouseId: any, companyCode, plantId, languageId,itemCode,lineNo,refDocNumber) {
    return this.http.patch<any>(this.url2 + '/' + preInboundNo + '?warehouseId=' + warehouseId+'&companyCode=' + companyCode + '&plantId=' + plantId + '&languageId=' + languageId+'&itemCode='+itemCode+'&lineNo='+lineNo+'&refDocNumber='+refDocNumber, obj);
  }
  Updatestagingline(obj: any, lineNo: any, warehouseId: any, companyCode, plantId, languageId,itemCode,preInboundNo,refDocNumber,stagingNo,caseCode,palletCode) {
    return this.http.patch<any>(this.url3 + '/' + lineNo + '?warehouseId=' + warehouseId+'&companyCode=' + companyCode + '&plantId=' + plantId + '&languageId=' + languageId+'&itemCode='+itemCode+'&preInboundNo='+preInboundNo+'&refDocNumber='+refDocNumber+'&stagingNo='+stagingNo+'&caseCode='+caseCode+'&palletCode='+palletCode, obj);
  }
  Delete(obj) {
    return this.http.delete<any>(this.url + '/' + obj.preInboundNo  + '?warehouseId=' + obj.warehouseId +'&companyCode=' + obj.companyCode + '&plantId=' + obj.plantId + '&languageId=' + obj.languageId);
  }
  search(obj: any) {
    return this.http.post<any>(this.url + '/findPreInboundHeader', obj);
  }
  Getline(preInboundNo: any) {
    return this.http.get<any>(this.apiName + 'preinboundline/' + preInboundNo);
  }

  processASN(obj: any,) {
    return this.http.post<any>(this.url + '/processASN', obj);
  }

  createbom(itemCode: any, lineNo: any, preInboundNo: any, refDocNumber: any, warehouseId: any, company: any, plant: any, languageId: any) {
    return this.http.post<any>(this.apiName + 'preinboundline/bom?itemCode=' + itemCode + '&lineNo=' + lineNo + '&preInboundNo=' + preInboundNo + '&refDocNumber=' + refDocNumber + '&warehouseId=' + warehouseId + '&companyCode=' + company + '&plantId=' + plant + '&languageId=' + languageId, {});
  }
  createAsnOrder(obj: any) {
    return this.http.post<any>('/wms-transaction-service/warehouse/inbound/asn', obj);
  }
}
