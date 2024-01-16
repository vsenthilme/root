import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FeessharingListComponent } from './feessharing-list.component';

describe('FeessharingListComponent', () => {
  let component: FeessharingListComponent;
  let fixture: ComponentFixture<FeessharingListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FeessharingListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FeessharingListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
