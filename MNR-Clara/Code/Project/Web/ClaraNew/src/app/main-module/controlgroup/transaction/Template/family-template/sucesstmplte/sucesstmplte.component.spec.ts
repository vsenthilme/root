import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SucesstmplteComponent } from './sucesstmplte.component';

describe('SucesstmplteComponent', () => {
  let component: SucesstmplteComponent;
  let fixture: ComponentFixture<SucesstmplteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SucesstmplteComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SucesstmplteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
