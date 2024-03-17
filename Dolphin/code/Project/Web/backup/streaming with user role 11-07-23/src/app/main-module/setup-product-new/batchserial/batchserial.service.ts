import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class BatchserialService {

  constructor(private http: HttpClient, private auth: AuthService) { }


  apiName = '/wms-enterprise-service/';
  Getall() {
    return this.http.get<any>(this.apiName + `batchserial`);
  }
  Get(code: string,languageId:any,plantId:any,warehouseId:any,companyId:any,levelId:any) {
    return this.http.get<any>(this.apiName + `batchserial/`+ code + '?languageId='+languageId+'&plantId='+plantId+'&warehouseId='+warehouseId+'&companyId='+companyId+'&levelId='+levelId);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `batchserial`, obj);
  }
  Update(obj: any, code: any,languageId:any,plantId:any,warehouseId:any,companyId:any,levelId:any) {
    return this.http.patch<any>(this.apiName + `batchserial/` + code +'?languageId='+languageId+'&plantId='+plantId+'&warehouseId='+warehouseId+'&companyId='+companyId+'&levelId='+levelId, obj);
  }
  Delete(storageMethod: any) {
    return this.http.delete<any>(this.apiName + `batchserial/`+storageMethod);
  }
  search(obj: any) {
    return this.http.post<any>(this.apiName + 'batchserial/findBatchSerial', obj);
  }
}
