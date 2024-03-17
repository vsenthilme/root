import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ContainerReceiptService {



  constructor(private http: HttpClient) { }

  apiName = '/wms-transaction-service/';
  methodName = 'containerreceipt';
  url = this.apiName + this.methodName;
  Getall() {
    return this.http.get<any>(this.url);
  }
  Get(containerReceiptNo: any, company, plant, language, warehoueId) {
    return this.http.get<any>(this.url + '/' + containerReceiptNo + '?companyCode=' + company + '&plantId=' + plant + '&languageId=' + language + '&warehouseId=' + warehoueId);
  }
  Create(obj: any) {
    return this.http.post<any>(this.url, obj);
  }
  Update(obj: any, containerReceiptNo: any,refDocNumber:any,preInboundNo:any, companyCodeId, plant, language, warehouseId) {
    return this.http.patch<any>(this.url + '/' + containerReceiptNo+'?refDocNumber='+refDocNumber+'&preInboundNo='+preInboundNo + '&companyCodeId=' + companyCodeId + '&plantId=' + plant + '&languageId=' + language + '&warehouseId=' + warehouseId, obj);
  }
  Delete(obj: any) {
    return this.http.delete<any>(this.url + '/' + obj.containerReceiptNo  + '?companyCode=' + obj.companyCodeId + '&plantId=' + obj.plantId + '&languageId=' + obj.languageId + '&warehouseId=' + obj.warehouseId);
  }
  search(obj: any) {
    return this.http.post<any>(this.url + '/findContainerReceipt', obj);
  }
  GetWh() {
    return this.http.get<any>('/wms-idmaster-service/warehouseid');
  }
}
