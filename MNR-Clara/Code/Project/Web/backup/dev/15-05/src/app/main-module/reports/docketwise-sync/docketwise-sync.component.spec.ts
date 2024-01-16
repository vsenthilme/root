import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocketwiseSyncComponent } from './docketwise-sync.component';

describe('DocketwiseSyncComponent', () => {
  let component: DocketwiseSyncComponent;
  let fixture: ComponentFixture<DocketwiseSyncComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DocketwiseSyncComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DocketwiseSyncComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
