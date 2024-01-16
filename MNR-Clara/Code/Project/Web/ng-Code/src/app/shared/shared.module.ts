import { NgModule } from "@angular/core";
import { MaterialModule } from "./material/material.module";
import { ReactiveFormsModule, FormsModule } from "@angular/forms";
import { NgxDocViewerModule } from "ngx-doc-viewer";
import { NgNumbersOnlyInputDirectiveModule } from "ng-numbers-only-input-directive";
import { TwoDigitDecimaNumberDirective } from "../common-field/two-digit-decima-number.directive";
import { PrimeModule } from "./prime/prime.module";
// import { UppercaseDirective } from "../common/directives/uppercase.directive";
// import { MatSelectSearchModule } from '../mat-select-search/mat-select-search.module';
// import { SignaturePadModule } from 'angular2-signaturepad';

@NgModule({
  declarations: [
    TwoDigitDecimaNumberDirective
    // UppercaseDirective
  ],
  imports: [MaterialModule, NgxDocViewerModule,],
  exports: [
    MaterialModule,
    ReactiveFormsModule,
    FormsModule,
    NgxDocViewerModule,
    NgNumbersOnlyInputDirectiveModule,
    PrimeModule

    // UppercaseDirective,
    // AgmDirectionModule,
    // MatSelectSearchModule,
    // SignaturePadModule
  ]
})
export class SharedModule { }
