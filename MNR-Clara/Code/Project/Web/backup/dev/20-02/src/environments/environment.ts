// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  title: 'Monty and Ramirez',
 //apiURL: 'https://mrclara.com:27070/mnrclara/api/services',     // PROD
 apiURL: 'http://35.154.84.178:27070/mnrclara/api/services',      // UAT
  OAUTH_CLIENT: 'pixeltrice',
  OAUTH_SECRET: 'pixeltrice-secret-key',
  hyperlink_userid: 'raj',
  hyperlink_pasword: 'welcome',
  name: 'uat',
 // webScoketURL:'http://35.154.84.178:7073/mnrclara/api/mnr-crm-service/',
   webScoketURL: 'https://mrclara.com:27070/mnrclara/api/mnr-crm-service/'
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
