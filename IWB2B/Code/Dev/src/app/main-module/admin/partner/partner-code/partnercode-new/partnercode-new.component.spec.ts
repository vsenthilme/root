import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartnercodeNewComponent } from './partnercode-new.component';

describe('PartnercodeNewComponent', () => {
  let component: PartnercodeNewComponent;
  let fixture: ComponentFixture<PartnercodeNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PartnercodeNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PartnercodeNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
