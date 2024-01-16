import {
  Injectable
} from '@angular/core';



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

  menu: NavItem[] = [{
    displayName: 'Settings',
    iconName: 'fas fa-cog mx-2  font_1-5',
    id: 100001,
    children: [{
      displayName: 'Admin',
      iconName: 'fas fa-user-cog mx-2 mr-3 font_1-5',
      id: 1,
      route: 'setting/admin',
      disabled: true,
    },
    {
      displayName: 'Business',
      iconName: 'fas fa-print mx-2 mr-3  font_1-5',
      id: 1,
      route: 'setting/business',
      disabled: false,
    },
    {
      displayName: 'Configuration',
      iconName: 'fas fa-cogs mx-2 mr-3 font_1-5',
      id: 1,
      route: 'setting/configuration',
      disabled: false,
    },
    {
      displayName: 'Data Upload',
      iconName: 'fas fa-upload mx-2 mr-3 font_1-5',
      id: 1,
      route: 'setting/upload-data',
      disabled: false,
    },

    ],
    disabled: false,

  },

  {
    displayName: 'CRM',
    iconName: 'far fa-address-card mx-2 font_1-5',
    id: 100002,
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
      id: 1073,
      route: 'crm/agreementlist',
      disabled: false,

    },
    {
      displayName: 'Conflict Check',
      iconName: 'fas fa-user-cog mx-2  font_1-5',
      id: 1073,
      route: 'crm/conflictcheck',
      disabled: false,
    },
    ],
    disabled: false,
  },

  {
    displayName: 'Client',
    iconName: 'fas fa-user mx-2  font_1-5',
    id: 1,
    children: [
      {
        displayName: 'Client New',
        iconName: 'fas fa-user-plus mx-2 mr-3 font_1-5',
        id: 1,
        route: 'client/clientNew/U2FsdGVkX1%252BsVNtpCJyOd2eRpJ20wz1WPsNfPdVEievX6ehFCzCs6EZX6CITLbHc',
        disabled: false,
      },
      {
        displayName: 'Client Management',
        iconName: 'fas fa-user mx-2 mr-3 font_1-5',
        id: 1,
        route: 'client/clientlist',
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
    id: 1,

    disabled: false,
    children: [
      {
        displayName: 'Case Info Sheet',
        iconName: 'far fa-id-badge align-middle mx-2 mr-3 font_1-5',
        id: 1,
        children: [{
          displayName: 'L&E',
          iconName: 'fas fa-people-carry mx-2 mr-3 font_1-5',
          id: 1,
          route: 'matters/case-info/lande',
          disabled: false,
        },
        {
          displayName: 'Immigration',
          iconName: 'fas fa-chalkboard-teacher mx-2 mr-3 font_1-5',
          id: 1,
          route: 'matters/case-info/immigration',
          disabled: false,
        },],
        disabled: false,
      },
      {
        displayName: 'Matter Management',
        iconName: 'fas fa-briefcase mx-2 mr-3 font_1-5',
        id: 1,
        route: 'matters/case-management/general',
        disabled: false,
      },
      {
        displayName: 'Case Assignment',
        iconName: 'far fa-calendar-check mx-2 mr-3 font_1-5',
        id: 1,
        route: 'matters/case-assignment/caselist',
        disabled: false,
      },
      // {
      //   iconName: 'far fa-clock mx-2 mr-3 font_1-5',
      //   id: 1,
      // },
      {
        displayName: 'Productivity',
        iconName: 'far fa-chart-bar mx-2 mr-3 font_1-5',
        id: 1,
        disabled: false,
      },
    ]
  },

  {
    displayName: 'Accounting',
    iconName: 'fas fa-dollar-sign mx-2  font_1-5',
    id: 1,
    children: [{
      displayName: 'Expenses',
      iconName: 'far fa-credit-card mx-2 mr-3 font_1-5',
      id: 1,

      disabled: false,
    },

    {
      displayName: 'Quotation',
      iconName: 'fas fa-file-alt mx-2 mr-3 font_1-5',
      id: 1,
      route: 'accounts/quotationslist',
      disabled: false,

    },
    {
      displayName: 'PreBill',
      iconName: 'fas fa-file-invoice-dollar mx-2 mr-3 font_1-5',
      id: 1,
      disabled: false,
    },
    {
      displayName: 'Matter Activity',
      iconName: 'fas fa-business-time mx-2 mr-3 font_1-5',
      id: 1,
      disabled: false,

    },
    {
      displayName: 'Time Ticket Dairy',
      iconName: 'far fa-clock ml-1 mr-3 font_1-5',
      id: 1,
      disabled: false,
    },
    {
      displayName: 'Bill',
      iconName: 'fas fa-file-alt mx-2 mr-3 font_1-5',
      id: 1,
      disabled: false,
    },
    {
      displayName: 'Payment Update',
      iconName: 'fas fa-money-check-alt ml-1 mr-2  font_1-5',
      id: 1,
      disabled: false,
    },
    ],
    disabled: false,
  },


  {
    displayName: 'Reports',
    iconName: 'fas fa-file-medical-alt mx-2 font_1-5',
    id: 1,
    children: [{
      displayName: 'CRM',
      iconName: 'far fa-address-card mx-2 mr-3 font_1-5',
      id: 1,
      route: 'setting/admin',
      disabled: false,
    },
    {
      displayName: 'Clients',
      iconName: 'fas fa-users mx-2 mr-3 font_1-5',
      id: 1,
      disabled: false,
    },
    {
      displayName: 'Matter Management',
      iconName: 'fas fa-briefcase mx-2 mr-3 font_1-5',
      id: 1,
      disabled: false,
    },
    {
      displayName: 'Accounting',
      iconName: 'fas fa-dollar-sign mx-2 mr-3 font_1-5',
      id: 1,
      disabled: false,
    },
    {
      displayName: 'Productivity',
      iconName: 'far fa-chart-bar mx-2 mr-3 font_1-5',
      id: 1,
      disabled: false,
    },
    ],
    disabled: false,
  },

  ];

  constructor() { }
  getMeuList() {
    return this.menu;

  }
}
