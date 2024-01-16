import { Component, EventEmitter, Input, OnChanges, OnInit, Optional, Output, Self } from '@angular/core';
import { ControlValueAccessor, NgControl } from '@angular/forms';
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { CommonApiService, dropdownelement } from 'src/app/common-service/common-api.service';

@Component({
  selector: 'app-ng-multiselect-dropdown',
  templateUrl: './ng-multiselect-dropdown.component.html',
  styleUrls: ['./ng-multiselect-dropdown.component.scss'],

})
export class NgMultiselectDropdownComponent implements OnInit, OnChanges, ControlValueAccessor {
  @Input() dropdownList: dropdownelement[];
  @Input() filerbased: any;
  @Input() forsearch: boolean = false;
  @Input() valuewithID: boolean = true;
  @Input() selectedItems: any = [];
  @Input() fromid: any;
  @Input() filter: any;
  @Input() apiName: any;
  @Input() apiMethod: any = "setup";
  @Input() singleSelection: any = false;
  @Input() ShowFilter: any = true;
  @Input() disabled: boolean = false;
  @Input() itemsShowLimit: any = 200000;
  @Input() limitSelection: any = 200000;

  @Output() messageToEmit = new EventEmitter<any>();

  dataList: dropdownelement[];

  dropdownSettings: IDropdownSettings = {
    singleSelection: this.singleSelection,
    idField: 'key',
    textField: 'value',
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    //itemsShowLimit: this.itemsShowLimit,
    itemsShowLimit: 3,
    allowSearchFilter: this.ShowFilter,
    //limitSelection: this.limitSelection
    //maxHeight: 197,
  };
  constructor(public formControl: NgControl,

    private toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
  ) {
    formControl.valueAccessor = this;
  }
  writeValue(obj: any): void {

    this.fill();
  }
  registerOnChange(fn: any): void {

  }
  registerOnTouched(fn: any): void {

  }
  // myForm = this.fb.group({
  //   element: this.formControl
  // });
  ngOnInit() {

    if (this.formControl.disabled)
      this.disabled = true;
    this.dropdownSettings.singleSelection = this.singleSelection;
    if (!this.apiName)
      this.apiName = this.formControl.name;
    this.dropdownlist()
    this.fill();
  }
  ngOnChanges(val: any) {



    if (this.formControl.disabled)
      this.disabled = true;
    this.dropdownSettings.singleSelection = this.singleSelection;
    if (this.formControl.control?.value)
      this.fill();

    if (this.dropdownList || this.filerbased)
      this.dropdownlist();
    // if (this.dropdownList)
    //   this.dataList = this.dropdownList;
  }
  data: dropdownelement[];
  fill() {
    if (this.singleSelection && this.formControl.control?.value) {
      this.selectedItems = [this.formControl.control?.value];
    }
    else if (this.formControl.control?.value) {
      this.data = this.dataList;
      let values: any[] = [];
      this.formControl.control?.value.forEach((x: any) => values.push(x.toString()));
      if (this.data && values.length > 0) {
        if (this.valuewithID)
          this.selectedItems = this.data.filter(x => values.includes(x.key.toString()));
        else
          this.selectedItems = this.data.filter(x => values.includes(x.value.toString()));

      }


    }
    else {
      this.selectedItems = null;
    }
  }

  dropdownlist() {


    if (this.dropdownList) {


      this.dataList = this.dropdownList;
      this.fill();


    }
    else {
      if (this.apiName) {
        this.spin.show();


        this.cas.getalldropdownlist([this.cas.dropdownlist[this.apiMethod][this.apiName].url]).subscribe((results) => {
          for (let item of results) {

            if (this.valuewithID)
              this.dataList = this.cas.foreachlist(item, this.cas.dropdownlist[this.apiMethod][this.apiName].key, this.filerbased);
            else
              this.dataList = this.cas.foreachlist_searchpage(item, this.cas.dropdownlist[this.apiMethod][this.apiName].key, this.filerbased);
            // need to work
            this.fill();
          }
          if (this.apiName == 'partnerAssigned') {
           this.dataList = this.cas.removeDuplicatesFromArray(this.dataList);
          }
        }, (err) => {
          this.toastr.error(err, "");
        });

        this.spin.hide();
      }
    }
  }
  seletedlistkey: any[] = [];
  onItemSelect(item: any) {


    if (this.forsearch) {
      if (this.singleSelection) {

        this.formControl.control?.patchValue(item.value);
      }
      else {
        this.seletedlistkey = []
        this.selectedItems.forEach((x: any) => { this.seletedlistkey.push(x.value.toString()); });
        this.formControl.control?.patchValue(this.seletedlistkey);
      }

    }
    else {
      if (this.singleSelection) {

        this.formControl.control?.patchValue(item.key);
      } else {
        this.seletedlistkey = []
        this.selectedItems.forEach((x: any) => { this.seletedlistkey.push(x.key.toString()); });
        this.formControl.control?.patchValue(this.seletedlistkey);
      }
    }
    this.messageToEmit.emit(this.selectedItems)
  }
  onSelectAll(items: any) {
    if (this.forsearch) {

      this.seletedlistkey = []
      this.dataList.forEach((x: any) => { this.seletedlistkey.push(x.value.toString()); });
      this.formControl.control?.patchValue(this.seletedlistkey);


    }
    else {

      this.seletedlistkey = []
      this.dataList.forEach((x: any) => { this.seletedlistkey.push(x.key.toString()); });
      this.formControl.control?.patchValue(this.seletedlistkey);

    }
    this.messageToEmit.emit(this.selectedItems)
  }

  onItemDeSelect(item: any) {

    if (this.forsearch) {
      if (this.singleSelection)
        this.formControl.control?.patchValue('');
      else {
        this.seletedlistkey = []
        this.selectedItems.forEach((x: any) => { this.seletedlistkey.push(x.value.toString()); });
        this.formControl.control?.patchValue(this.seletedlistkey);
      }

    }
    else {
      if (this.singleSelection)
        this.formControl.control?.patchValue('');
      else {
        this.seletedlistkey = []
        this.selectedItems.forEach((x: any) => { this.seletedlistkey.push(x.key.toString()); });
        this.formControl.control?.patchValue(this.seletedlistkey);
      }
    }
    this.messageToEmit.emit(this.selectedItems)
  }
  onUnSelectAll() {

    this.seletedlistkey = [];
    this.selectedItems = [];
    this.formControl.control?.patchValue(this.seletedlistkey);


    this.messageToEmit.emit(this.selectedItems)
  }

}