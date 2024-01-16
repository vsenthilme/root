import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FamilyTemplateComponent } from './family-template.component';

describe('FamilyTemplateComponent', () => {
  let component: FamilyTemplateComponent;
  let fixture: ComponentFixture<FamilyTemplateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FamilyTemplateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FamilyTemplateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
