import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartnernumberingNewComponent } from './partnernumbering-new.component';

describe('PartnernumberingNewComponent', () => {
  let component: PartnernumberingNewComponent;
  let fixture: ComponentFixture<PartnernumberingNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PartnernumberingNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PartnernumberingNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
