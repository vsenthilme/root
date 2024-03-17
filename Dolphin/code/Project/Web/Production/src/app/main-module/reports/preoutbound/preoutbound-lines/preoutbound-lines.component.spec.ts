import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PreoutboundLinesComponent } from './preoutbound-lines.component';

describe('PreoutboundLinesComponent', () => {
  let component: PreoutboundLinesComponent;
  let fixture: ComponentFixture<PreoutboundLinesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PreoutboundLinesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PreoutboundLinesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
