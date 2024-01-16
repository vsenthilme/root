import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StorePartnerListingComponent } from './store-partner-listing.component';

describe('StorePartnerListingComponent', () => {
  let component: StorePartnerListingComponent;
  let fixture: ComponentFixture<StorePartnerListingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StorePartnerListingComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StorePartnerListingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
