import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SummaryService {
  constructor(private http: HttpClient) { }

  reportPdf(){
    return this.http.get<any>('/mnr-cg-transaction-service/storepartnerlisting/findGroupStore');
  }
  reportstorepartner(obj:any){
    return this.http.post<any>('/mnr-cg-transaction-service/storepartnerlisting/findStorePartnerListing',obj);
  }
}