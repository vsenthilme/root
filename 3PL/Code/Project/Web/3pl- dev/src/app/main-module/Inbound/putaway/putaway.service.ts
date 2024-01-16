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
    return this.http.get<any>(this.url + '/' + putAwayNumber + '/v2' + '?caseCode=' + caseCode + '&goodsReceiptNo=' + goodsReceiptNo +
      '&packBarcodes=' + packBarcodes + '&palletCode=' + palletCode + '&preInboundNo=' + preInboundNo + '&proposedStorageBin=' + proposedStorageBin + '&refDocNumber=' + refDocNumber + '&warehouseId=' + warehouseId);
  }
  CreateLine(obj: any) {
    return this.http.post<any>(this.urlLine + '/v2', obj); // + '/v2'
  }
  search(obj: any) {
    return this.http.post<any>(this.url + '/findPutAwayHeader', obj); 
  } 
  searchv2(obj: any) {
    return this.http.post<any>(this.url + '/findPutAwayHeader/v2', obj); ///v2
  }

  reverse(refDocNumber: any, packBarcodes: any) {
    return this.http.patch<any>(this.url + '/' + refDocNumber +  '/reverse?packBarcodes=' + packBarcodes, {}); //'/v2' +
  }

  getbin() {
    return this.http.get<any>('/wms-masters-service/storagebin');
  }
  findStorageBin(obj: any) {
    return this.http.post<any>('/wms-masters-service/storagebin/findStorageBin', obj);
  }

  searchLine(obj: any) {
    return this.http.post<any>(this.urlLine + '/findPutAwayLine', obj); // + '/v2'
  }


  deletePutawayHeader(obj: any) {
    return this.http.delete<any>('/wms-transaction-service/putawayheader/'  + obj.putAwayNumber + '/v2' + '?caseCode=' + obj.caseCode + '&goodsReceiptNo=' + obj.goodsReceiptNo + '&packBarcodes=' + obj.packBarcodes + '&palletCode=' + obj.palletCode + '&preInboundNo=' + obj.preInboundNo + '&proposedStorageBin=' + obj.proposedStorageBin + '&refDocNumber=' + obj.refDocNumber + '&warehouseId=' + obj.warehouseId); // + '&companyCode=' + obj.companyCodeId + '&plantId=' + obj.plantId + '&languageId=' + obj.languageId
  }

}

