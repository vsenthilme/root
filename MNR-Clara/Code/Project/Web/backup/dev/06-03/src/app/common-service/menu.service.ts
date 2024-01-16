import {
  Injectable
} from '@angular/core';
import { AuthService } from '../core/core';
import { CommonService } from './common-service.service';



export interface NavItem {
  displayName: string;
  iconName: string;
  route?: string;
  id: number;
  children?: NavItem[];
  disabled: boolean;
}
@Injectable({
  providedIn: 'root'
})
export class MenuService {
  constructor( public cs: CommonService,) { }
  menu: NavItem[] = [{
    displayName: 'Settings',
    iconName: 'fas fa-cog mx-2  font_1-5',
    id: 1,
    children: [{
      displayName: 'Admin',
      iconName: 'fas fa-user-cog mx-2 mr-3 font_1-5',
      id: 6,
      route: 'setting/admin',
      disabled: true,
      children: [{
        displayName: 'Company',
        iconName: 'fas fa-user font_1-5 mx-2 mr-3 font_1-5',
        route: '/main/setting/admin/company',
        id: 1082,
        disabled: true,
      },
      {
        displayName: 'Class',
        iconName: 'fas fa-users mx-2 mr-3 font_1-5',
        route: '/main/setting/admin/class',
        id: 1019,
        disabled: false,
      },
      {
        displayName: 'Intake',
        iconName: 'fas fa-file-signature mx-2 mr-3 font_1-5',
        route: '/main/setting/admin/intake',
        id: 1011,
        disabled: false,
      },
      {
        displayName: 'Client Category',
        iconName: 'fas fa-user-tag mx-2 mr-3 font_1-5',
        route: '/main/setting/admin/clientcategory',
        id: 1010,
        disabled: false,
      },
      {
        displayName: 'Client Type',
        iconName: 'fas fa-user-check mx-2 mr-3 font_1-5',
        route: '/main/setting/admin/clienttype',
        id: 1007,
        disabled: false,
      },
      {
        displayName: 'User Type',
        iconName: 'fas fa-users mx-2 mr-3 font_1-5',
        route: '/main/setting/admin/usertype',
        id: 1005,
        disabled: false,
      },
      {
        displayName: 'User Profile',
        iconName: 'fas fa-user-shield mx-2 mr-3 font_1-5',
        route: '/main/setting/admin/userprofile',
        id: 1015,
        disabled: false,
      },
      {
        displayName: 'User Role',
        iconName: 'fas fa-user-lock mx-2 mr-3 font_1-5',
        route: '/main/setting/admin/userrole',
        id: 1013,
        disabled: false,
      },
      {
        displayName: 'GL Mapping',
        iconName: 'fas fa-map-marked-alt mx-2 mr-3 font_1-5',
        route: '/main/setting/admin/glmapping',
        id: 1138,
        disabled: false,
      }
      ],
    },
    {
      displayName: 'Business',
      iconName: 'fas fa-print mx-2 mr-3  font_1-5',
      id: 6,
      route: 'setting/business',
      disabled: false,
      children: [{
        displayName: 'Case Category',
        iconName: 'fas fa-folder mx-2 mr-3 font_1-5',
        route: '/main/setting/business/casecategory',
        id: 1021,
        disabled: true,
      },
      {
        displayName: 'Case Sub Category',
        iconName: 'fas fa-folder-open mx-2 mr-3 font_1-5',
        route: '/main/setting/business/casesubcategory',
        id: 1023,
        disabled: false,
      },
      {
        displayName: 'Agreement Template',
        iconName: 'fas fa-file-alt mx-2 mr-3 font_1-5',
        route: '/main/setting/business/agreementtemplate',
        id: 1047,
        disabled: false,
      },
      {
        displayName: 'Inquiry Mode',
        iconName: 'fas fa-external-link-alt mx-2 mr-3 font_1-5',
        route: '/main/setting/business/inquirymode',
        id: 1053,
        disabled: false,
      },
      {
        displayName: 'Referral',
        iconName: 'fas fa-retweet mx-2 mr-3 font_1-5',
        route: '/main/setting/business/referral',
        id: 1055,
        disabled: false,
      },
      {
        displayName: 'Note Type',
        iconName: 'fas fa-clipboard mx-2 mr-3 font_1-5',
        route: '/main/setting/business/notetype',
        id: 1057,
        disabled: false,
      },
      {
        displayName: 'Time Keeper',
        iconName: 'fas fa-business-time mx-2 mr-3 font_1-5',
        route: '/main/setting/business/timekeeper',
        id: 1017,
        disabled: false,
      },
      {
        displayName: 'Activity',
        iconName: 'fas fa-clipboard mx-2 mr-3 font_1-5',
        route: '/main/setting/business/activity',
        id: 1025,
        disabled: false,
      },
      {
        displayName: 'Task Type',
        iconName: 'far fa-calendar-alt mx-2 mr-3 font_1-5',
        route: '/main/setting/business/tasktype',
        id: 1033,
        disabled: false,
      },
      {
        displayName: 'Task Based',
        iconName: 'far fa-calendar-check mx-2 mr-3 font_1-5',
        route: '/main/setting/business/tasktype',
        id: 1027,
        disabled: false,
      },
      {
        displayName: 'Expense',
        iconName: 'far fa-credit-card mx-2 mr-3 font_1-5',
        route: '/main/setting/business/expense',
        id: 1031,
        disabled: false,
      },
      {
        displayName: 'Deadline Calculator',
        iconName: 'far fa-calendar-times mx-2 mr-3 font_1-5',
        route: '/main/setting/business/deadlinecalculator',
        id: 1043,
        disabled: false,
      },
      {
        displayName: 'Document Template',
        iconName: 'fas fa-file-alt mx-2 mr-3 font_1-5',
        route: '/main/setting/business/documenttemplate',
        id: 1049,
        disabled: false,
      },
      {
        displayName: 'Checklist Template',
        iconName: 'fas fa-tasks mx-2 mr-3 font_1-5',
        route: '/main/setting/business/documentchecklist',
        id: 1128,
        disabled: false,
      },
      {
        displayName: 'Chart Of Accounts',
        iconName: 'fas fa-chart-line mx-2 mr-3 font_1-5',
        route: '/main/setting/business/coc',
        id: 1029,
        disabled: false,
      },
      {
        displayName: 'Admin Cost',
        iconName: 'fas fa-donate  mx-2 mr-3 font_1-5',
        route: '/main/setting/business/admincost',
        id: 1035,
        disabled: false,
      },
      {
        displayName: 'Billing Mode',
        iconName: 'fas fa-file-invoice mx-2 mr-3 font_1-5',
        route: '/main/setting/business/billingmode',
        id: 1037,
        disabled: false,
      },
      {
        displayName: 'Billing Frequency',
        iconName: 'fas fa-file-alt mx-2 mr-3 font_1-5',
        route: '/main/setting/business/billingfrequency',
        id: 1039,
        disabled: false,
      },
      {
        displayName: 'Billing Format',
        iconName: 'fas fa-file-alt mx-2 mr-3 font_1-5',
        route: '/main/setting/business/billingformat',
        id: 1041,
        disabled: false,
      },
      {
        displayName: 'Expiration Document Type',
        iconName: 'far fa-calendar-times  mx-2 mr-3 font_1-5',
        route: '/main/setting/business/expirationdocument',
        id: 1129,
        disabled: false,
      }
      ],

    },
    {
      displayName: 'Configuration',
      iconName: 'fas fa-cogs mx-2 mr-3 font_1-5',
      id: 6,
      route: 'setting/configuration',
      disabled: false,
      children: [{
        displayName: 'Language',
        iconName: 'fas fa-globe  font_1-5 mx-2 mr-3 font_1-5',
        route: '/main/setting/configuration/language',
        id: 1003,
        disabled: true,
      },
      {
        displayName: 'Status',
        iconName: 'fas fa-check-square mx-2 mr-3 font_1-5',
        route: '/main/setting/configuration/status',
        id: 1008,
        disabled: false,
      },
      {
        displayName: 'Transaction',
        iconName: 'fas fa-exchange-alt mx-2 mr-3 font_1-5',
        route: '/main/setting/configuration/transaction',
        id: 1006,
        disabled: false,
      },
      {
        displayName: 'Number Range',
        iconName: 'fas fa-sort-amount-down mx-2 mr-3 font_1-5',
        route: '/main/setting/configuration/numberrange',
        id: 1045,
        disabled: false,
      }
      ],

    },
    {
      displayName: 'Data Upload',
      iconName: 'fas fa-upload mx-2 mr-3 font_1-5',
      id: 1127,
      route: 'setting/upload-data',
      disabled: false,
    },
    {
      displayName: 'QB Sync - EH',
      iconName: 'fas fa-sync-alt mx-2 mr-3 font_1-5',
      route: 'reports/qbsync',
      id: 1170,
      disabled: false,
    },

    {
      displayName: 'Docketwise Sync - Repush',
      iconName: 'fas fa-sync-alt mx-2 mr-3 font_1-5',
      route: 'reports/docketwisesync',
      id: 1174,
      disabled: false,
    },

    ],
    disabled: false,

  },

  {
    displayName: 'CRM',
    iconName: 'far fa-address-card mx-2 font_1-5',
    id: 2,
    children: [{
      displayName: 'Inquiry',
      iconName: 'fas fa-external-link-alt mx-2 mr-3 font_1-5',
      id: 200001,
      children: [{
        displayName: 'New Inquiry',
        iconName: 'fas fa-file-signature mx-2 mr-3 font_1-5',
        route: 'crm/inquirynew',
        id: 1060,
        disabled: true,
      },
      {
        displayName: 'Inquiry Assign',
        iconName: 'fas fa-user-cog mx-2 mr-3 font_1-5',
        route: 'crm/inquiryassign',
        id: 1062,
        disabled: false,
      },
      {
        displayName: 'Inquiry Validation',
        iconName: 'fas fa-user mx-2 mr-3 font_1-5',
        route: 'crm/inquiryvalidate',
        id: 1064,
        disabled: false,
      }
      ],
      disabled: false,
    },

    {
      displayName: 'Intake',
      iconName: 'fas fa-file-signature mx-2 mr-3 font_1-5',
      route: 'crm/inquiryform',
      id: 1073,
      disabled: false,

    },

    {
      displayName: 'Prospective Client',
      iconName: 'fas fa-user mx-2 mr-3 font_1-5',
      id: 1075,
      route: 'crm/potential',
      disabled: false,

    },
    {
      displayName: 'Agreement',
      iconName: 'fas fa-file-alt  mx-2 mr-3 font_1-5',
      id: 1079,
      route: 'crm/agreementlist',
      disabled: false,

    },
    {
      displayName: 'Conflict Check',
      iconName: 'fas fa-user-cog mx-2 font_1-5',
      id: 1080,
      route: 'crm/conflictcheck',
      disabled: false,
    },
    ],
    disabled: false,
  },

  {
    displayName: 'Client',
    iconName: 'fas fa-user mx-2  font_1-5',
    id: 3,
    children: [
      {
        displayName: 'Client New',
        iconName: 'fas fa-user-plus mx-2 mr-3 font_1-5',
        id: 1085,
        route: `client/clientNew/${this.cs.encrypt({ pageflow: 'New' })}`,
        disabled: false,
      },
      {
        displayName: 'Client Management',
        iconName: 'fas fa-user mx-2 mr-3 font_1-5',
        id: 1084,
        route: 'client/clientlist',
        disabled: false,
      },
      {
        displayName: 'Client Portal',
        iconName: 'fas fa-user mx-2 mr-3 font_1-5',
        id: 1156,
        route: 'client/clientportal',
        disabled: false,
      },
      //  {
      //    displayName: 'Intake Form',
      //    iconName: 'fas fa-file-signature mx-2  font_1-5',
      //    id: 1,
      //  },
      //  {
      //    displayName: 'Form/Document',
      //    iconName: 'fas fa-file-alt mx-2 mr-3 font_1-5',
      //    id: 1,
      //  },
    ]
    ,
    disabled: false,
  },

  {
    displayName: 'Matter',
    iconName: 'fas fa-briefcase mx-2  font_1-5',
    id: 4,

    disabled: false,
    children: [
    
      {
        displayName: 'Case Info Sheet',
        iconName: 'far fa-id-badge align-middle mx-2 mr-3 font_1-5',
        id: 1,
        children: [{
          displayName: 'L&E',
          iconName: 'fas fa-people-carry mx-2 mr-3 font_1-5',
          id: 1094,
          route: 'matters/case-info/lande',
          disabled: false,
        },
        {
          displayName: 'Immigration',
          iconName: 'fas fa-chalkboard-teacher mx-2 mr-3 font_1-5',
          id: 1096,
          route: 'matters/case-info/immigration',
          disabled: false,
        },],
        disabled: false,
      },
      {
        displayName: 'Matter Management',
        iconName: 'fas fa-briefcase mx-2 mr-3 font_1-5',
        id: 1098,
        route: 'matters/case-management/general',
        disabled: false,
      },
      {
        displayName: 'Case Assignment',
        iconName: 'far fa-calendar-check mx-2 mr-3 font_1-5',
        id: 1118,
        route: 'matters/case-assignment/caselist',
        disabled: false,
      },
     
      {
        displayName: 'Expiration Report',
        iconName: 'far fa-edit mx-2 mr-3 font_1-5',
        route: '/main/reports/expirarion',
        id: 1172,
        disabled: false,
      }
      // {
      //   iconName: 'far fa-clock mx-2 mr-3 font_1-5',
      //   id: 1,
      // },
      // {
      //   displayName: 'Productivity',
      //   iconName: 'far fa-chart-bar mx-2 mr-3 font_1-5',
      //   id: 1,
      //   disabled: false,
      // },
    ]
  },

  {
    displayName: 'Billing',
    iconName: 'fas fa-dollar-sign mx-2  font_1-5',
    id: 5,
    children: [
      {
        displayName: 'Quotation',
        iconName: 'fas fa-file-alt mx-2 mr-4 font_1-5',
        id: 1134,
        route: 'accounts/quotationslist',
        disabled: false,

      },
      {
        displayName: 'Payment Plan',
        iconName: 'fas fa-money-check-alt ml-1 mr-3  font_1-5',
        id: 1136,
        route: 'accounts/paymentplanlist',
        disabled: false,
      },
      {
        displayName: 'PreBilling',
        iconName: 'fas fa-file-alt  mx-2 mr-4 font_1-5',
        id: 1,
        children: [{
          displayName: 'PreBilling Generate',
          iconName: 'fas fa-file-alt  mx-2 mr-3 font_1-5',
          id: 1137,
          route: 'accounts/prebilllist',
          disabled: true,
        },
        {
          displayName: 'PreBilling Approval',
          iconName: 'fas fa-check-square mx-2 mr-3 font_1-5',
          id: 1139,
          route: 'accounts/prebilllist/Approve',
          disabled: false,
        },
        ],

        disabled: false,
      },
      {
        displayName: 'Invoice',
        iconName: 'fas fa-file-invoice-dollar mx-2 mr-4 font_1-5',
        id: 1140,
        route: 'accounts/billlist',
        disabled: false,
      },
      {
        displayName: 'Matter Billing Activity',
        iconName: 'fas fa-briefcase mx-2 mr-3 font_1-5',
        id: 1167,
        route: 'matters/case-management/matteractivity',
        disabled: false,
      },
      {
        displayName: 'Expenses Summary',
        iconName: 'fas fa-dollar-sign mx-2 mr-3 font_1-5',
        id: 1169,
        route: 'accounts/expensesaccounts',
        disabled: false,
      },
      {
        displayName: 'Transfer Matter Billing Activity',         
        iconName: 'fas fa-briefcase mx-2 mr-3 font_1-5',
        id: 1186,
        route: 'matters/case-management/transferactivity',
        disabled: false,
      },
      {
        displayName: 'Time Ticket Summary',
        iconName: 'fas fa-dollar-sign mx-2 mr-3 font_1-5',
        id: 1171,
        route: 'accounts/timeticket',
        disabled: false,
      },
      // {
      //   displayName: 'Matter Activity',
      //   iconName: 'fas fa-business-time mx-2 mr-3 font_1-5',
      //   id: 1,
      //   disabled: false,

      // },
      // {
      //   displayName: 'Expenses',
      //   iconName: 'far fa-credit-card mx-2 mr-3 font_1-5',
      //   id: 1,
      //   disabled: false,
      // },




      // {
      //   displayName: 'Time Ticket Diary',
      //   iconName: 'far fa-clock ml-1 mr-4 font_1-5',
      //   id: 1,
      //   disabled: false,
      // },


    ],
    disabled: false,
  },


  {
    displayName: 'Reports',
    iconName: 'fas fa-file-medical-alt mx-2 font_1-5',
    id: 6,
    children: [{
      displayName: 'CRM',
      iconName: 'far fa-address-card mx-2 mr-3 font_1-5',
      id: 6,
      route: 'reports/crmlist',
      disabled: false,
      children: [{
        displayName: 'Propective Client',
        iconName: 'fas fa-users  font_1-5 mx-2 mr-3 font_1-5',
        route: '/main/reports/prospectiveclient',
        id: 1158,
        disabled: true,
      },
      {
        displayName: 'Lead Conversion',
        iconName: 'fas fa-check-square mx-2 mr-3 font_1-5',
        route: '/main/reports/leadconversion',
        id: 1159,
        disabled: false,
      },
      {
        displayName: 'Referral',
        iconName: 'fas fa-retweet mx-2 mr-3 font_1-5',
        route: '/main/reports/referral',
        id: 1160,
        disabled: false,
      }
      ],
    },
    {
      displayName: 'Clients',
      iconName: 'fas fa-users mx-2 mr-3 font_1-5',
      id: 6,
      route: 'reports/clientlist',
      disabled: false,
      children: [{
        displayName: 'New Client Info - L&E',
        iconName: 'fas fa-people-carry  font_1-5 mx-2 mr-3 font_1-5',
        route: '/main/reports/lande',
        id: 1161,
        disabled: true,
      },
      {
        displayName: 'New Client Info - Immigration',
        iconName: 'fas fa-chalkboard-teacher mx-2 mr-3 font_1-5',
        route: '/main/reports/immigration',
        id: 1162,
        disabled: false,
      },
      ],
    },
    {
      displayName: 'Matter Management',
      iconName: 'fas fa-briefcase mx-2 mr-3 font_1-5',
      route: 'reports/matterlist',
      id: 6,
      disabled: false,
      children: [{
        displayName: 'L&E Matter',
        iconName: 'fas fa-people-carry  font_1-5 mx-2 mr-3 font_1-5',
        route: '/main/reports/landematter',
        id: 1163,
        disabled: true,
      },
      {
        displayName: 'Immigration Matter',
        iconName: 'fas fa-chalkboard-teacher mx-2 mr-3 font_1-5',
        route: '/main/reports/immigrationmatter',
        id: 1164,
        disabled: false,
      },
      {
        displayName: 'Matter Listing',
        iconName: 'fas fa-users mx-2 mr-3 font_1-5',
        route: '/main/reports/clientmatter',
        id: 1164,
        disabled: false,
      },
      {
        displayName: 'Matter Rates',
        iconName: 'fas fa-file-invoice-dollar mx-2 mr-3 font_1-5',
        route: '/main/reports/matterRates',
        id: 1164,
        disabled: false,
      },
      {
        displayName: 'Task',
        iconName: 'far fa-calendar-alt mx-2 mr-3 font_1-5',
        route: '/main/reports/taskReport',
        id: 1187,
        disabled: false,
      },
      ],
    },
    {
      displayName: 'Accounting',
      iconName: 'fas fa-dollar-sign mx-2 mr-3 font_1-5',
      route: 'reports/accountlist',
      id: 6,
      disabled: false,
      children: [{
        displayName: 'WIP Aged',
        iconName: 'fas fa-file-invoice-dollar font_1-5 mx-2 mr-3 font_1-5',
        route: '/main/reports/wipagedpb',
        id: 1166,
        disabled: true,
      },
      {
        displayName: 'AR Aging',
        iconName: 'fas fa-check-square mx-2 mr-3 font_1-5',
        route: '/main/reports/araging',
        id: 1165,
        disabled: false,
      },
      {
        displayName: 'Billing',
        iconName: 'fas fa-file-alt mx-2 mr-3 font_1-5',
        route: '/main/reports/billing',
        id: 1185,
        disabled: false,
      },
      {
        displayName: 'Client Cash Receipt',
        iconName: 'fas fa-hand-holding-usd mx-2 mr-3 font_1-5',
        route: '/main/reports/clientcash',
        id: 1180,
        disabled: false,
      },
      {
        displayName: 'Client Income Summary',
        iconName: 'fas fa-dollar-sign mx-2 mr-3 font_1-5',
        route: '/main/reports/clientIncomeSummary',
        id: 1181,
        disabled: false,
      },
      {
        displayName: 'AR',
        iconName: 'far fa-file-alt mx-2 mr-3 font_1-5',
        route: '/main/reports/ArReportComponent',
        id: 1176,
        disabled: false,
      },
      {
        displayName: 'Billed & Unbilled',
        iconName: 'fas fa-file-invoice-dollar mx-2 mr-3 font_1-5',
        route: '/main/reports/BilledUnbilledComponent',
        id: 1178,
        disabled: false,
      },
      {
        displayName: 'Billed Hours Paid',
        iconName: 'fas fa-file-invoice-dollar mx-2 mr-3 font_1-5',
        route: '/main/reports/billedHoursPaid',
        id: 1179,
        disabled: false,
      },
      {
        displayName: 'Write Off',
        iconName: 'far fa-edit mx-2 mr-3 font_1-5',
        route: '/main/reports/writeoff',
        id: 1182,
        disabled: false,
      },
      {
        displayName: 'Billed Paid Fees',
        iconName: 'far fa-edit mx-2 mr-3 font_1-5',
        route: '/main/reports/billedPaid',
        id: 1177,
        disabled: false,
      },
      ],
    },
    {
      displayName: 'Productivity',
      iconName: 'far fa-chart-bar mx-2 mr-3 font_1-5',
      id: 6,
      disabled: false,
    },
    ],
    disabled: false,
  },

  ];


  getMeuList() {
    return this.menu;

  }
}
