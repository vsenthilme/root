import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewbillListComponent } from './newbill-list.component';

describe('NewbillListComponent', () => {
  let component: NewbillListComponent;
  let fixture: ComponentFixture<NewbillListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewbillListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewbillListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
