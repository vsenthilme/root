import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsumablesNewComponent } from './consumables-new.component';

describe('ConsumablesNewComponent', () => {
  let component: ConsumablesNewComponent;
  let fixture: ComponentFixture<ConsumablesNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConsumablesNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConsumablesNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
