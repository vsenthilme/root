import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApiService } from '../config/api.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WmsIdmasterService {

  constructor(private http: HttpClient, private apiService: ApiService) { }
  wmsApiName = '/wms-idmaster-service/';
  getallcity() {
    return this.http.get<any>(this.wmsApiName + `city`);
  }
  getallcompany() {
    return this.http.get<any>(this.wmsApiName + `company`);
  }
  getallcountry() {
    return this.http.get<any>(this.wmsApiName + `country`);
  }
  getallcurrency() {
    return this.http.get<any>(this.wmsApiName + `currency`);
  }
  getallstate() {
    return this.http.get<any>(this.wmsApiName + `state`);
  }

  getallvetical() {
    return this.http.get<any>(this.wmsApiName + `vetical`);
  }

  // getNotification(userId: string): Observable<any> {
  //   return this.apiService.get('/wms-transaction-service/notification-message' + '?userId=' + userId);
  // }
  markNotificationsAsRead() {
    return this.http.get<any>("/wms-transaction-service/notification-message/mark-read-all");
  }
  readOnenotification(id: any) {
    return this.http.get<any>("/wms-transaction-service/notification-message/mark-read/" + id);
  }


  getNotification(userId: string): Observable<any[]> {
    return this.http.get<any[]>('/wms-transaction-service/notification-message' + '?userId=' + userId);
  }
}
