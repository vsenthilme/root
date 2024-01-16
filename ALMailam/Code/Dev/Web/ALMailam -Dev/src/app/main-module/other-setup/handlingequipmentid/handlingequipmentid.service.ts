import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

@Injectable({
  providedIn: 'root'
})
export class HandlingequipmentidService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  Getall() {
    return this.http.get<any>('/wms-idmaster-service/handlingequipmentid');
  }
  Get(code: string,  warehouseId: string,languageId:any,companyCodeId:any,plantId:any) {
    return this.http.get<any>('/wms-idmaster-service/handlingequipmentid/' + code +'?warehouseId=' + warehouseId+'&languageId='+languageId+'&companyCodeId='+companyCodeId+'&plantId='+plantId);
  }
  Create(obj: any) {
    return this.http.post<any>('/wms-idmaster-service/handlingequipmentid', obj);
  }
  Update(obj: any, code: any, warehouseId: string,languageId:any,companyCodeId:any,plantId:any) {
    return this.http.patch<any>('/wms-idmaster-service/handlingequipmentid/' + code+'?warehouseId=' + warehouseId+'&languageId='+languageId+'&companyCodeId='+companyCodeId+'&plantId='+plantId, obj);
  }
  Delete(handlingEquipmentNumber: any, warehouseId: string,languageId:any,companyCodeId:any,plantId:any) {
    return this.http.delete<any>('/wms-idmaster-service/handlingequipmentid/' + handlingEquipmentNumber+'?warehouseId='+warehouseId+'&languageId='+languageId+'&companyCodeId='+companyCodeId+'&plantId='+plantId);
}
search(obj: any) {
  return this.http.post<any>('/wms-idmaster-service/handlingequipmentid/findHandlingEquipmentId', obj);
}
}


