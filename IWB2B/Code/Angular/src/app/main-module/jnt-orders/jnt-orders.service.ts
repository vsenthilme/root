import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class JntOrdersService {

  constructor(private http: HttpClient) { }
  getAll(){
    return this.http.get<any>('/iwe-integration-service/softdata/JT/orders'); // + '?referenceNumber=' + referenceNumber
  }
  getAll1(){
    return this.http.get<any>('/iwe-integration-service/softdata'); // + '?referenceNumber=' + referenceNumber
  }
  getLabel(billCode: any){
    return this.http.get<any>('/iwe-integration-service/softdata/jnt/' + billCode + '/printLabel');
  }

  // getBulkLabel(billCode: any){
  //   return this.http.get<any>('/iwe-integration-service/softdata/jnt/bulk/printLabel' + billCode);
  // }

  getBulkLabel(billCode: any): Promise<File> {
    return this.http
      .post<any>(`/iwe-integration-service/softdata/jnt/bulk/printLabel`, billCode, {
        responseType: 'blob' as 'json',
      })
      .toPromise();
  }
  

  getConsignmentTracking(referenceNumber: any){
    return this.http.get<any>('/iwe-integration-service/tracking/' + referenceNumber + '/shipment');
  }


  findConsignment(obj: any) {
    return this.http.post<any>('/iwe-integration-service/softdata/findConsignment', obj);
  }
}
