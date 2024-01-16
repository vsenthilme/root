import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BrotherSisterTemplateComponent } from './brother-sister-template.component';

describe('BrotherSisterTemplateComponent', () => {
  let component: BrotherSisterTemplateComponent;
  let fixture: ComponentFixture<BrotherSisterTemplateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BrotherSisterTemplateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BrotherSisterTemplateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
