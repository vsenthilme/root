import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SetupServiceService {

  constructor(private http: HttpClient) {

   }
   searchCompany(obj: any){
    return this.http.post<any>('/mnr-cg-setup-service/companyid/findcompanyid', obj);
  }
  searchLanguage(obj: any){
    return this.http.post<any>('/mnr-cg-setup-service/languageid/findlanguageid', obj);
  }
  searchgroupmapping(obj: any){
    return this.http.post<any>('/mnr-cg-setup-service/clientcontrolgroup/findClientControlGroup', obj);
  }
  searchCountry(obj: any){
    return this.http.post<any>('/mnr-cg-setup-service/country/findcountry', obj);
  }
  searchState(obj: any){
    return this.http.post<any>('/mnr-cg-setup-service/state/findState', obj);
  }
  searchCity(obj: any){
    return this.http.post<any>('/mnr-cg-setup-service/city/findcity', obj);
  }
  searchControlType(obj: any){
    return this.http.post<any>('/mnr-cg-setup-service/controlgrouptype/findControlGroupType', obj);
  }
  searchClient(obj: any){
    return this.http.post<any>('/mnr-cg-setup-service/client/findClient', obj);
  }
  searchClientNew(obj: any){
    return this.http.post<any>('/mnr-cg-transaction-service/storepartnerlisting/findStorePartnerListing', obj);
  }
  searchStore(obj: any){
    return this.http.post<any>('/mnr-cg-setup-service/store/findStore', obj);
  }
  getStore(code: string,languageId:any,companyId:any) {
    return this.http.get<any>('/mnr-cg-setup-service/store/' + code+'?languageId='+languageId+'&companyId='+companyId);
  }
  searchStoredropdown(){
    return this.http.get<any>( `/mnr-cg-setup-service/getAllStoreGroupDown`);
  }
  searchControlGroup(obj: any){
    return this.http.post<any>('/mnr-cg-setup-service/controlgroup/findControlGroup', obj);
  }
  searchrelationship(obj: any){
    return this.http.post<any>('/mnr-cg-setup-service/relationshipid/findRelationShipId', obj);
  }
  searchSubGroupType(obj: any){
    return this.http.post<any>('/mnr-cg-setup-service/subgrouptype/findSubGroupType', obj);
  }
  
}
