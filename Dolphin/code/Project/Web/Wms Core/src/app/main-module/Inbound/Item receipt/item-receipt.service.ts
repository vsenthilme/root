import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class ItemReceiptService {



  constructor(private http: HttpClient, private auth: AuthService,) { }

  apiName = '/wms-transaction-service/';
  methodName = 'grheader';
  methodName1 = 'grline';
  url = this.apiName + this.methodName;
  url1 = this.apiName + this.methodName1;

  urlline = this.apiName + 'grline';
  Get(goodsReceiptNo: any, caseCode: any, palletCode: any, preInboundNo: any, refDocNumber: any, stagingNo: any, warehouseId: any) {
    return this.http.get<any>(this.url + '/' + goodsReceiptNo + '?caseCode=' + caseCode + '&palletCode=' + palletCode + '&preInboundNo=' + preInboundNo + '&refDocNumber=' + refDocNumber + '&stagingNo=' + stagingNo + '&warehouseId=' + warehouseId);
  }
  updateGrHeader(obj:any,goodsReceiptNo: any, caseCode: any, palletCode: any, preInboundNo: any, refDocNumber: any, stagingNo: any, warehouseId: any,companyCode:any,plantId:any,languageId:any) {
    return this.http.patch<any>(this.url + '/' + goodsReceiptNo + '?caseCode=' + caseCode + '&palletCode=' + palletCode + '&preInboundNo=' + preInboundNo + '&refDocNumber=' + refDocNumber + '&stagingNo=' + stagingNo + '&warehouseId=' + warehouseId+'&companyCode='+companyCode+'&plantId='+plantId+'&languageId='+languageId,obj);
  }
  search(obj: any) {
    return this.http.post<any>(this.url + '/findGrHeader', obj);
  }
  searchnew(obj: any) {
    return this.http.post<any>(this.url + '/findGrHeaderNew', obj);
  }
  searchline(obj: any) {
    return this.http.post<any>(this.apiName + 'findGrLine', obj);
  }

  CreateLine(obj: any) {
    return this.http.post<any>(this.urlline, obj);
  }


  getheVaidate(heno: any) {
   return this.http.get<any>('/wms-masters-service/handlingequipment/' + heno + '/barCode?warehouseId=' + this.auth.warehouseId + '&companyCode='+ this.auth.companyId +'&plantId='+ this.auth.plantId +'&languageId='+ this.auth.languageId);
  }
  packBarcodev3(acceptedQty: any, damageQty: any,  warehouseId: any) {
    return this.http.get<any>(this.urlline + '/packBarcode?acceptQty=' + acceptedQty + '&damageQty=' + damageQty + '&warehouseId=' + warehouseId);
  }

  packBarcode(acceptedQty: any, damageQty: any,  warehouseId: any,  companyCode: any,  plantId: any,  languageId: any) {
    return this.http.get<any>(this.urlline + '/packBarcode?acceptQty=' + acceptedQty + '&damageQty=' + damageQty + '&warehouseId=' + warehouseId + '&companyCodeId=' + companyCode + '&plantId=' + plantId + '&languageId=' + languageId);
  }

  deleteGRHeader(obj: any) {
    return this.http.delete<any>(this.url + '/'  + obj.goodsReceiptNo + '?caseCode=' + obj.caseCode + '&palletCode=' + obj.palletCode + '&preInboundNo=' + obj.preInboundNo + '&refDocNumber=' + obj.refDocNumber + '&stagingNo=' + obj.stagingNo + '&warehouseId=' + obj.warehouseId  + '&companyCode=' + obj.companyCode + '&plantId=' + obj.plantId + '&languageId=' + obj.languageId);
  }
  deleteGRLine(obj: any) {
    return this.http.delete<any>(this.url1 + '/'  + obj.lineNo + '?caseCode=' + obj.caseCode + '&goodsReceiptNo=' + obj.goodsReceiptNo + '&itemCode=' + obj.itemCode + '&packBarcodes=' + obj.packBarcodes + '&palletCode=' + obj.palletCode +  '&preInboundNo=' + obj.preInboundNo + '&refDocNumber=' + obj.refDocNumber + '&warehouseId=' + obj.warehouseId + '&companyCode=' + obj.companyCode + '&plantId=' + obj.plantId + '&languageId=' + obj.languageId);
  }
}
