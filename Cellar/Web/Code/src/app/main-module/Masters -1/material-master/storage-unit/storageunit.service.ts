import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/core';

export interface storageElement{
  availability: string;
  availableAfter: Date;
  bin: string;
  createdBy: string;
  createdOn: Date;
  deletionIndicator: number;
  description: string;
  doorType: string;
  halfYearly: string;
  itemCode: string;
  itemGroup: string;
  itemType: string;
  length: string;
  linkedAgreement: string;
  monthly: string;
  occupiedFrom: Date;
  originalDimention: string;
  phase: string;
  priceMeterSquare: string;
  quarterly: string;
  rack: string;
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
  room: string;
  roundedDimention: string;
  storageType: string;
  updatedBy: string;
  updatedOn: Date;
  weekly: string;
  width: string;
  yearly: string;
  zone: string;
}

@Injectable({
  providedIn: 'root'
})
export class StorageunitService {

  constructor(private http: HttpClient, private auth: AuthService) { }


  apiName = '/us-trans-service/master/';
  Getall() {
    return this.http.get<any>(this.apiName + `storageunit`);
  }
  Get(code: string) {
    return this.http.get<any>(this.apiName + `storageunit/` + code);
  }
  Create(obj: any) {
    return this.http.post<any>(this.apiName + `storageunit`, obj);
  }
  Update(obj: any, code: any) {
    return this.http.patch<any>(this.apiName + `storageunit/` + code, obj);
  }
  Delete(storageUnit: any,) {
    return this.http.delete<any>(this.apiName + `storageunit/` + storageUnit);
  }
  search(obj: any) {
    return this.http.post<any>(this.apiName + 'storageunit/find', obj);
  }
}
