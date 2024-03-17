import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class QualityService {

  constructor(private http: HttpClient, private auth : AuthService) { }

  apiName = '/wms-transaction-service/';
  methodName = 'qualityheader';
  url = this.apiName + this.methodName;

  search(obj: any) {
    return this.http.post<any>(this.url + '/findQualityHeader', obj);
  }
  searchSpark(obj: any) {
    return this.http.post<any>(this.url +'/findQualityHeader', obj);
  }
  getpickingline(obj: any) {
    return this.http.post<any>(this.apiName + 'pickupline/findPickupLine', obj);
  }
  getpickinglineSpark(obj: any) {
    return this.http.post<any>( '/wms-spark-service/pickupLine', obj);
  }
  getqualityline(obj: any) {
    return this.http.post<any>(this.apiName + 'qualityline/findQualityLine', obj);
  }
  confirm(obj: any) {
    return this.http.post<any>(this.apiName + 'qualityline', obj);
  }

  GetStoreCode() {
    return this.http.get<any>('/wms-masters-service/businesspartner');
  }

  deleteQualitypHeader(obj: any) {
    return this.http.delete<any>('/wms-transaction-service/qualityheader/'  + obj.qualityInspectionNo + '?actualHeNo=' + obj.actualHeNo + '&partnerCode=' + obj.partnerCode + '&pickupNumber=' + obj.pickupNumber + '&preOutboundNo=' + obj.preOutboundNo + '&refDocNumber=' + obj.refDocNumber +  '&warehouseId=' + this.auth.warehouseId + '&companyCode='+ this.auth.companyId +'&plantId='+ this.auth.plantId +'&languageId='+ this.auth.languageId);
  }
}
