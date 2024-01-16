import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ValidationService {

  constructor(private http: HttpClient) { }

  searchStoreMatch(obj:any){
    return this.http.post<any>('/mnr-cg-transaction-service/storepartnerlisting/findMatchResult', obj);
  }
  findGroup(obj:any){
    return this.http.post<any>('/mnr-cg-transaction-service/storepartnerlisting/findGroup/count', obj);
  }
  
  findGroupbackup(obj:any){
    return this.http.post<any>('/mnr-cg-transaction-service/storepartnerlisting/findGroup', obj);
  }
}
