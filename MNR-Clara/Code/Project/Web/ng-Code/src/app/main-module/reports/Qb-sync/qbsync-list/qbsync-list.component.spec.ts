import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QbsyncListComponent } from './qbsync-list.component';

describe('QbsyncListComponent', () => {
  let component: QbsyncListComponent;
  let fixture: ComponentFixture<QbsyncListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ QbsyncListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(QbsyncListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
