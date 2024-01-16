import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgreeementTemplateComponent } from './agreeement-template.component';

describe('AgreeementTemplateComponent', () => {
  let component: AgreeementTemplateComponent;
  let fixture: ComponentFixture<AgreeementTemplateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AgreeementTemplateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AgreeementTemplateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
