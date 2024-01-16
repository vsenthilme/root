// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  title: 'CLASSIC WMS',
 // apiURL: 'https://dev.classicwms.com:8086/classicwms',
 apiURL: 'http://15.207.62.25:5001/iwexpress',
// apiURL: 'https://www.classicwms.com:8086/classicwms',      // prod
  OAUTH_CLIENT: 'pixeltrice',
  OAUTH_SECRET: 'pixeltrice-secret-key'
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
