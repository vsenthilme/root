import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor(private http: HttpClient) { }

  getBoutiqaat(obj){
    return this.http.post<any>('/iwe-integration-service/dashboard/getBoutiqaatDashboardCount', obj); 
  }
  getJNT(obj){
    return this.http.post<any>('/iwe-integration-service/dashboard/getJNTDashboardCount', obj); 
  }

  getDashboardCount(obj){
    return this.http.post<any>('/iwe-integration-service/dashboard/getDashboardCount', obj); 
  }
}
