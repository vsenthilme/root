import { NgModule } from "@angular/core";
import { MaterialModule } from "./material/material.module";
import { ReactiveFormsModule, FormsModule } from "@angular/forms";
import { NgxDocViewerModule } from "ngx-doc-viewer";
import { NgxIntlTelInputModule } from "ngx-intl-tel-input";
import { NgNumbersOnlyInputDirectiveModule } from "ng-numbers-only-input-directive";
// import { UppercaseDirective } from "../common/directives/uppercase.directive";
// import { MatSelectSearchModule } from '../mat-select-search/mat-select-search.module';
// import { SignaturePadModule } from 'angular2-signaturepad';

@NgModule({
  declarations: [

    // UppercaseDirective
  ],
  imports: [MaterialModule, NgxDocViewerModule, NgxIntlTelInputModule,],
  exports: [
    MaterialModule,
    ReactiveFormsModule,
    FormsModule,
    NgxDocViewerModule, NgxIntlTelInputModule,
    NgNumbersOnlyInputDirectiveModule
    // UppercaseDirective,
    // AgmDirectionModule,
    // MatSelectSearchModule,
    // SignaturePadModule
  ]
})
export class SharedModule { }
