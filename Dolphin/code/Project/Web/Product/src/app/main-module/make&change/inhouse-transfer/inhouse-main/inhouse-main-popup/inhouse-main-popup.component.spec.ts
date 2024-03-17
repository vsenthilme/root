import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InhouseMainPopupComponent } from './inhouse-main-popup.component';

describe('InhouseMainPopupComponent', () => {
  let component: InhouseMainPopupComponent;
  let fixture: ComponentFixture<InhouseMainPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InhouseMainPopupComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InhouseMainPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
