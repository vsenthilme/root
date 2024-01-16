import { Injectable } from '@angular/core';
import { ApiService } from '../config/api.service';
import { ConfigService } from '../config/config.service';

@Injectable({
  providedIn: 'root'
})
export class MasterService {

  constructor(private apiService: ApiService, private configService: ConfigService) { }

  getCompanyEnterpriseDetails() {
    return this.apiService.get(this.configService.company_url);
  }

  getPlantEnterpriseDetails() {
    return this.apiService.get(this.configService.plant_url);
  }

  getWarehouseEnterpriseDetails() {
    return this.apiService.get(this.configService.warehouse_url);
  }
  getFloorEnterpriseDetails() {
    return this.apiService.get(this.configService.floor_url);
  }

  //masters
  getCompanyMasterDetails() {
    return this.apiService.get(this.configService.companyMaster_url);
  }

  getVerticalMasterDetails() {
    return this.apiService.get(this.configService.veticalMaster_url);
  }

  getCurrencyMasterDetails() {
    return this.apiService.get(this.configService.currencyMaster_url);
  }

  getCountryMasterDetails() {
    return this.apiService.get(this.configService.countryMaster_url);
  }

  getStateMasterDetails() {
    return this.apiService.get(this.configService.stateMaster_url);
  }

  getCityMasterDetails() {
    return this.apiService.get(this.configService.cityMaster_url);
  }

  getItemTypeMasterDetails() {
    return this.apiService.get(this.configService.itemTypeMaster_url);
  }

  getItemGroupMasterDetails() {
    return this.apiService.get(this.configService.itemGroupMaster_url);
  }

  getPlantMasterDetails() {
    return this.apiService.get(this.configService.plantMaster_url);
  }

  getWarehouseMasterDetails() {
    return this.apiService.get(this.configService.warehouseMaster_url);
  }

  getFloorMasterDetails() {
    return this.apiService.get(this.configService.floorMaster_url);
  }

  getStorageSectionMasterDetails() {
    return this.apiService.get(this.configService.storageSectionMaster_url);
  }

  getStorageClassMasterDetails() {
    return this.apiService.get(this.configService.storageClassMaster_url);
  }

  getStorageTypeMasterDetails() {
    return this.apiService.get(this.configService.storageTypeMaster_url);
  }

  getStorageBinTypeMasterDetails() {
    return this.apiService.get(this.configService.storageBinTypeMaster_url);
  }

  getSubItemGroupMasterDetails() {
    return this.apiService.get(this.configService.subItemGroupMaster_url);
  }

  getLevelMasterDetails() {
    return this.apiService.get(this.configService.levelMaster_url);
  }

  getVariantMasterDetails() {
    return this.apiService.get(this.configService.variantMaster_url);
  }

  getStrategyTypeMasterDetails() {
    return this.apiService.get(this.configService.strategyTypeMaster_url);
  }

  getAllStorageBin(obj: any) {
    return this.apiService.post(this.configService.getStorageBin, obj);
  }

  getAllHHTUser() {
    return this.apiService.get(this.configService.getAllHHTUserList);
  }

}
