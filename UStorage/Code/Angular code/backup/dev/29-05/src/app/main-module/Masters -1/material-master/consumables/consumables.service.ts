import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';


export interface consumablesElement {
  createdBy: string;
        createdOn: Date;
        deletionIndicator: number;
        description: string;
        height: string;
        inStock: string;
        inventoryUnitOfMeasure: string;
        itemCode: string;
        itemGroup: string;
        itemType: string;
        lastReceipt: string;
        length: string;
        openWo: string;
        purchaseUnitOfMeasure: string;
        referenceField1: string;
        referenceField10: string;
        referenceField2: string;
        referenceField3: string;
        referenceField4: string;
        referenceField5: string;
        referenceField6: string;
        referenceField7: string;
        referenceField8: string;
        referenceField9: string;
        saleUnitOfMeasure: string;
        status: string;
        unitPrice: string;
        updatedBy: string;
        updatedOn: Date;
        volume: string;
        warehouse: string;
        weight: string;
        width: string;
}


@Injectable({
  providedIn: 'root'
})
export class ConsumablesService {
  constructor(private http: HttpClient, private auth: AuthService) { }


  apiName = '/us-trans-service/master/';
  methodName = 'crm/';
  url = this.apiName + this.methodName;
  Getall() {
    return this.http.get<any>(this.apiName + `consumables`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `consumables/` + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `consumables`, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.apiName + `consumables/` + code, obj);
  }
  Delete(consumablesId: any,) {
    return this.http.delete<any>(this.apiName + `consumables/` + consumablesId);
  }
  search(obj: any) {
    return this.http.post<any>(this.apiName + 'consumables/find', obj);
  }
  saveLines(obj: any) {
    return this.http.post<any>(this.apiName + 'consumablepurchase', obj);
  }
  GetLines(code: string) {
    return this.http.get<any>(this.apiName + `consumablepurchase/` + code);
  }
}
