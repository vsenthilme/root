import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HubopsComponent } from './hubops.component';

describe('HubopsComponent', () => {
  let component: HubopsComponent;
  let fixture: ComponentFixture<HubopsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HubopsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HubopsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
