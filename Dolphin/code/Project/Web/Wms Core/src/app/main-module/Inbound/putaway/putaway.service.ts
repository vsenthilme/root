import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PutawayService {



  constructor(private http: HttpClient) { }

  apiName = '/wms-transaction-service/';
  methodName = 'putawayheader';
  url = this.apiName + this.methodName;
  urlLine = this.apiName + 'putawayline';

  Get(putAwayNumber: any, warehouseId: any, caseCode: any, goodsReceiptNo: any, packBarcodes: any, palletCode: any, preInboundNo: any, proposedStorageBin: any, refDocNumber: any) {
    return this.http.get<any>(this.url + '/' + putAwayNumber + '?caseCode=' + caseCode + '&goodsReceiptNo=' + goodsReceiptNo +
      '&packBarcodes=' + packBarcodes + '&palletCode=' + palletCode + '&preInboundNo=' + preInboundNo + '&proposedStorageBin=' + proposedStorageBin + '&refDocNumber=' + refDocNumber + '&warehouseId=' + warehouseId);
  }
  
  CreateLine(obj: any) {
    return this.http.post<any>(this.urlLine, obj); //
  }
  search(obj: any) {
    return this.http.post<any>(this.url + '/findPutAwayHeader', obj); 
  } 

  reverse(refDocNumber: any, packBarcodes: any,companyCodeId:any,plantId:any,warehouseId:any,languageId:any) {
    return this.http.patch<any>(this.url + '/' + refDocNumber +  '/reverse?packBarcodes=' + packBarcodes+'&companyCodeId='+companyCodeId+'&plantId='+plantId+'&warehouseId='+warehouseId+'&languageId='+languageId, {}); //'/v2' +
  }
  Update(obj:any,putAwayNumber: any, warehouseId: any, caseCode: any, goodsReceiptNo: any, packBarcodes: any, palletCode: any, preInboundNo: any, proposedStorageBin: any, refDocNumber: any,companyCode:any,plantId:any,languageId:any) {
    return this.http.patch<any>(this.url +  '/'+ putAwayNumber  + '?caseCode=' + caseCode + '&goodsReceiptNo=' + goodsReceiptNo +
    '&packBarcodes=' + packBarcodes + '&palletCode=' + palletCode + '&preInboundNo=' + preInboundNo + '&proposedStorageBin=' + proposedStorageBin + '&refDocNumber=' + refDocNumber + '&warehouseId=' + warehouseId+'&companyCode='+companyCode+'&plantId='+plantId+'&languageId='+languageId,obj);
  }
  
  searchLine(obj: any) {
    return this.http.post<any>(this.urlLine + '/findPutAwayLine', obj); //
  }


  deletePutawayHeader(obj: any) {
    return this.http.delete<any>('/wms-transaction-service/putawayheader/'  + obj.putAwayNumber + '?caseCode=' + obj.caseCode + '&goodsReceiptNo=' + obj.goodsReceiptNo + '&packBarcodes=' + obj.packBarcodes + '&palletCode=' + obj.palletCode + '&preInboundNo=' + obj.preInboundNo + '&proposedStorageBin=' + obj.proposedStorageBin + '&refDocNumber=' + obj.refDocNumber + '&warehouseId=' + obj.warehouseId + '&companyCode=' + obj.companyCodeId + '&plantId=' + obj.plantId + '&languageId=' + obj.languageId); 
  }

}

