import { NgModule } from "@angular/core";

import { AccordionModule } from "primeng/accordion";
import { TreeModule } from "primeng/tree";
import { FileUploadModule } from "primeng/fileupload";
import { MessagesModule } from "primeng/messages";
import { MessageModule } from "primeng/message";
import { BlockUIModule } from "primeng/blockui";
import { PaginatorModule } from "primeng/paginator";
import { PanelModule } from "primeng/panel";
import { EditorModule } from "primeng/editor";
import { ScrollPanelModule } from "primeng/scrollpanel";
import { TreeTableModule } from "primeng/treetable";
import { DataViewModule } from "primeng/dataview";
import { MenubarModule } from "primeng/menubar";
import { TableModule } from "primeng/table";
import { CalendarModule } from "primeng/calendar";
import { OrganizationChartModule } from "primeng/organizationchart";
import {AutoCompleteModule} from 'primeng/autocomplete';
import {MultiSelectModule} from 'primeng/multiselect';

@NgModule({
	exports: [
		AccordionModule,
		TreeModule,
		FileUploadModule,
		MessagesModule,
		MessageModule,
		PaginatorModule,
		BlockUIModule,
		PanelModule,
		EditorModule,
		ScrollPanelModule,
		TreeTableModule,
		DataViewModule,
		MenubarModule,
		TableModule,
		CalendarModule,
		OrganizationChartModule,
		AutoCompleteModule,
		MultiSelectModule
	]
})
export class PrimeModule { }
