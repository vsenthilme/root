import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class OrdermanagementService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  apiName = '/wms-transaction-service/';
  methodName = 'ordermanagementline';
  url = this.apiName + this.methodName;

  searchLine(obj: any) {
    return this.http.post<any>(this.url + '/findOrderManagementLine', obj);
  }
  searchLineSpark(obj: any) {
    return this.http.post<any>( '/wms-spark-service/orderManagementLine', obj);
  }
  AssignPicker(obj: any, assignedPickerId: any) {
    return this.http.patch<any>(this.url + '/assignPicker?assignedPickerId=' + assignedPickerId, obj);
  }
  Unallocate(itemCode: any, lineNumber: any, partnerCode: any, preOutboundNo: any, refDocNumber: any,warehouseId: any, proposedPackBarCode: any,  proposedStorageBin: any) {
    return this.http.patch<any>(this.url + '/unallocate?itemCode=' + itemCode + '&lineNumber=' + lineNumber + '&partnerCode=' + partnerCode + '&preOutboundNo=' + preOutboundNo + '&refDocNumber=' + refDocNumber + '&warehouseId=' + this.auth.warehouseId + '&companyCodeId='+ this.auth.companyId +'&plantId='+ this.auth.plantId +'&languageId='+ this.auth.languageId  + '&proposedPackBarCode=' + proposedPackBarCode + '&proposedStorageBin=' + proposedStorageBin, {});
  }

  Allocate(itemCode: any, lineNumber: any, partnerCode: any, preOutboundNo: any, refDocNumber: any, warehouseId: any,companyCodeId:any,plantId:any,languageId:any) {
    return this.http.patch<any>(this.url + '/allocate?itemCode=' + itemCode + '&lineNumber=' + lineNumber + '&partnerCode=' + partnerCode + '&preOutboundNo=' + preOutboundNo + '&refDocNumber=' + refDocNumber + '&warehouseId=' + this.auth.warehouseId + '&companyCodeId='+ this.auth.companyId +'&plantId='+ this.auth.plantId +'&languageId='+ this.auth.languageId, {});
  }

  GetStoreCode() {
    return this.http.get<any>('/wms-masters-service/businesspartner');
  }
}
