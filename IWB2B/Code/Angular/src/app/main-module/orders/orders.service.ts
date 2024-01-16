import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class OrdersService {

    constructor(private http: HttpClient) { }
    Getall() {
      return this.http.get<any>('/iwe-integration-service/softdata');
    }

    GetOrderType(type: any) {
      return this.http.get<any>('/iwe-integration-service/softdata/type' + '?type=' + type );
    }
    postJNT(referenceNumber: any){
      return this.http.get<any>('/iwe-integration-service/softdata/jnt' + '?referenceNumber=' + referenceNumber);
    }
  }
  