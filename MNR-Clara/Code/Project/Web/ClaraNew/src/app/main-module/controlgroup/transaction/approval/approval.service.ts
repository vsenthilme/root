import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ApprovalService {

  constructor(private http: HttpClient) { }
  apiName = '/mnr-cg-transaction-service/';

  Create(obj: any) {
    return this.http.post<any>(this.apiName + `storepartnerlisting`, obj);
  }
}
