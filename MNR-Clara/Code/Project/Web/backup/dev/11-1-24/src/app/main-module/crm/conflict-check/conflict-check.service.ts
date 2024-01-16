import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ConflictCheckService {

  constructor(private http: HttpClient) { }

  apiName = '/mnr-crm-service/';

  methodeName = 'conflictSearch';
  url = this.apiName + this.methodeName;

  Get(searchFieldValue: any, searchTableNames: any[]) {
    let params: string = '';

    searchTableNames.forEach(x => params += '&searchTableNames=' + x);

    return this.http.get<any>(this.url + `?searchFieldValue=` + searchFieldValue + params);
  }
}
