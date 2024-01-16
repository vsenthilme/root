import { Injectable } from '@angular/core';
import { AuthService } from '../core/core';
import { HttpClient } from '@angular/common/http';
import { NgxSpinnerService } from 'ngx-spinner';
import { forkJoin, Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';
import { CommonService } from './common-service.service';
export interface dropdownelement {
  key: any;
  value: any;
  referenceField?: any;
}
@Injectable({
  providedIn: 'root'
})
export class CommonApiService {

  constructor(private http: HttpClient, private cs: CommonService, private auth: AuthService, private spin: NgxSpinnerService, public toastr: ToastrService,) { }

  setup = '/mnr-setup-service/';
  dashboard = '/mnr-crm-service/dashboard/';
  management = '/mnr-management-service/';
  crm = '/mnr-crm-service/';
  dropdownlist: any = {
    setup: {
      classId: { url: this.setup + 'class', key: { key: 'classId', value: 'classDescription' } },
      clientTypeId: { url: this.setup + 'clientType', key: { key: 'clientTypeId', value: 'clientTypeDescription' } },
      languageId: { url: this.setup + 'language', key: { key: 'languageId', value: 'languageDescription' } },
      screenId: { url: this.setup + 'screen', key: { key: 'screenId', value: 'screenText' } },
      subScreenId: { url: this.setup + 'screen', key: { key: 'subScreenId', value: 'subScreenName' } },
      subScreenId_search: { url: this.setup + 'screen', key: { key: 'subScreenName', value: 'subScreenName' } },
      transactionId: { url: this.setup + 'transaction', key: { key: 'transactionId', value: 'transactionDescription' } },
      messageType: { url: this.setup + 'message', key: { key: 'messageType', value: 'messageType' } },//check with raj
      companyId: { url: this.setup + 'company', key: { key: 'companyId', value: 'companyName' } },
      messageId: { url: this.setup + 'message', key: { key: 'messageId', value: 'messageDescription' } },
      //messageType: { url: this.setup + 'language', key: { key: 'languageId', value: 'languageDescription' } },//check with raj


      inquiryModeId: { url: this.setup + 'inquiryMode', key: { key: 'inquiryModeId', value: 'inquiryModeDescription' } },
      statusId: { url: this.setup + 'status', key: { key: 'statusId', value: 'status' } },
      userId: { url: this.setup + 'user', key: { key: 'username', value: 'username' } },
      userTypeId: { url: this.setup + 'userType', key: { key: 'userTypeId', value: 'userTypeDescription' } },
      caseCategoryId: { url: this.setup + 'caseCategory', key: { key: 'caseCategoryId', value: 'caseCategory' } },
      caseSubcategoryId: { url: this.setup + 'caseSubcategory', key: { key: 'caseSubcategoryId', value: 'subCategoryDescription' } },
      intakeFormId: { url: this.setup + 'intakeFormId', key: { key: 'intakeFormId', value: 'intakeFormDescription' } },
      agreementCode: { url: this.setup + 'agreementTemplate', key: { key: 'agreementCode', value: 'agreementFileDescription', ismailmerge: 'mailMerge' } },


      referralId: { url: this.setup + 'referral', key: { key: 'referralId', value: 'referralDescription' } },
      subReferalId: { url: this.setup + 'referral', key: { key: 'subReferalId', value: 'subReferralDescription' } },
      clientCategoryId: { url: this.setup + 'clientCategory', key: { key: 'clientCategoryId', value: 'clientCategoryDescription' } },
      taskTypeCode: { url: this.setup + 'taskType', key: { key: 'taskTypeCode', value: 'taskTypeDescription' } },
      numberRangeCode: { url: this.setup + 'numberRange', key: { key: 'numberRangeCode', value: 'numberRangeObject' } },
      noteTypeId: { url: this.setup + 'noteType', key: { key: 'noteTypeId', value: 'noteTypeText' } },
      timeKeeperCode: { url: this.setup + 'timekeeperCode', key: { key: 'timekeeperCode', value: 'timekeeperName' } },

      //newly written api
      activityCode: { url: this.setup + 'activityCode', key: { key: 'activityCode', value: 'activityCodeDescription' } },
      taskbasedCode: { url: this.setup + 'taskbasedCode', key: { key: 'taskCode', value: 'taskcodeType' } },
      expenseCode: { url: this.setup + 'expenseCode', key: { key: 'expenseCode', value: 'expenseCodeDescription' } },
      documentcode: { url: this.setup + 'documentTemplate', key: { key: 'documentNumber', value: 'documentNumber' } },
      notificationId: { url: this.setup + 'notification', key: { key: 'notificationId', value: 'notificationDescription' } },


      accountNumber: { url: this.setup + 'chartOfAccounts', key: { key: 'accountNumber', value: 'accountDescription' } },
      adminCost: { url: this.setup + 'adminCost', key: { key: 'adminCost', value: 'adminCost' } },
      billingFrequencyId: { url: this.setup + 'billingFrequency', key: { key: 'billingFrequencyId', value: 'billingFrequencyDescription' } },
      billingFormatId: { url: this.setup + 'billingFormat', key: { key: 'billingFormatId', value: 'billingFormatDescription' } },
      billingModeId: { url: this.setup + 'billingMode', key: { key: 'billingModeId', value: 'billingModeDescription' } },

      userRoleId: { url: this.setup + 'userRole', key: { key: 'userRoleId', value: 'userRoleName' } },
      documentType: { url: this.setup + 'expirationdoctype', key: { key: 'documentType', value: 'documentTypeDescription' } },

    },


    dashboard: {
      potentialClient: { url: this.dashboard + 'potentialClient', key: { key: 'fiteredCount', value: 'totalCount' } },
      pcIntakeForm: { url: this.dashboard + 'potentialClient', key: { key: 'fiteredCount', value: 'totalCount' } },
      inquiry: { url: this.dashboard + 'inquiry', key: { key: 'fiteredCount', value: 'totalCount' } },
      agreement: { url: this.dashboard + 'agreement', key: { key: 'fiteredCount', value: 'totalCount' } },
      agreementReceived: { url: this.dashboard + 'agreementReceived', key: { key: 'fiteredCount', value: 'totalCount' } },
      agreementResent: { url: this.dashboard + 'agreementResent', key: { key: 'fiteredCount', value: 'totalCount' } },
      agreementSent: { url: this.dashboard + 'agreementSent', key: { key: 'fiteredCount', value: 'totalCount' } },
      agreementTotal: { url: this.dashboard + 'agreementTotal', key: { key: 'fiteredCount', value: 'totalCount' } },

    },

    dashboard_matters: {
      total: { url: this.management + 'mattergenacc/dashboard/total', key: { key: 'fiteredCount', value: 'totalCount' } },
      ret: { url: this.management + 'mattergenacc/dashboard/RTE', key: { key: 'fiteredCount', value: 'totalCount' } },
      open: { url: this.management + 'mattergenacc/dashboard/open', key: { key: 'fiteredCount', value: 'totalCount' } },
      filed: { url: this.management + 'mattergenacc/dashboard/filed', key: { key: 'fiteredCount', value: 'totalCount' } },
      closed: { url: this.management + 'mattergenacc/dashboard/closed', key: { key: 'fiteredCount', value: 'totalCount' } },


    },
    dashboard_client: {
      active: { url: this.management + 'clientgeneral/dashboard/active', key: { key: 'fiteredCount', value: 'totalCount' } },
      recentClients: { url: this.management + 'clientgeneral/dashboard/recentClients', key: { key: 'fiteredCount', value: 'totalCount' } },
      total: { url: this.management + 'clientgeneral/dashboard/total', key: { key: 'fiteredCount', value: 'totalCount' } },

    },

    client: {
      clientId: { url: this.management + 'clientgeneral', key: { key: 'clientId', value: 'firstNameLastName' } },
      notesNumber: { url: this.management + 'clientnote', key: { key: 'notesNumber', value: 'notesNumber' } },
    },
    matter: {
      matterNumber: { url: this.management + 'mattergenacc', key: { key: 'matterNumber', value: 'matterNumber' } },
      notesNumber: { url: this.management + 'matternote', key: { key: 'notesNumber', value: 'notesNumber' } },
      timeTicketNumber: { url: this.management + 'mattertimeticket', key: { key: 'timeTicketNumber', value: 'timeTicketNumber' } },
      taskNumber: { url: this.management + 'mattertask', key: { key: 'taskNumber', value: 'taskNumber' } },

    }
    ,
    crm: {
      inquiryNumber: { url: this.crm + 'inquiry', key: { key: 'inquiryNumber', value: 'inquiryNumber' } },

      //newly written api
      potentialClientId: { url: this.crm + 'potentialClient', key: { key: 'potentialClientId', value: 'potentialClientId' } },
    }
  }

  getalldropdownlist(url: string[]) {
    let observableBatch: any[] = [];
    url.forEach((url: string) => { observableBatch.push(this.http.get<any>(url).pipe(catchError(err => of(err)))) });
    return forkJoin(observableBatch);
  }

  foreachlist(list: any, val: { key: any, value: any }, _filter: any = {}, addblank: boolean = false,) {
    let dropdownlist: dropdownelement[] = [];
    let filter = list;
    if (_filter)
      filter = this.cs.filterArray(list, _filter);
    if (addblank)
      dropdownlist.push({ key: '', value: '' });
    for (const l of filter) {
      dropdownlist.push({ key: l[val.key], value: l[val.key] + ' - ' + l[val.value] });
    }
    return dropdownlist.sort((a, b) => (a.key > b.key) ? 1 : -1);
  }



  foreachlist_searchpage(list: any, val: { key: any, value: any }, _filter: any = {}, addblank: boolean = false,) {
    let dropdownlist: dropdownelement[] = [];
    let filter = list;

    if (_filter)
      filter = this.cs.filterArray(list, _filter);
    if (addblank)
      dropdownlist.push({ key: '', value: '' });
    for (const l of filter) {
      dropdownlist.push({ key: l[val.key], value: l[val.value] });
    }
    return dropdownlist.sort((a, b) => (a.key > b.key) ? 1 : -1);
  }
  dockerSignStatusUpdatepotentialClientId(url: any[]) {
    let observableBatch: any[] = [];
    url.forEach((url) => { observableBatch.push(this.http.patch<any>(url.url, url.obj).pipe(catchError(err => of(err)))) });
    return forkJoin(observableBatch);
  }

}
