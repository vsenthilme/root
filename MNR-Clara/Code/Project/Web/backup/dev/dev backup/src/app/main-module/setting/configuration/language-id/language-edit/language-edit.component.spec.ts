import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LanguageEditComponent } from './language-edit.component';

describe('LanguageEditComponent', () => {
  let component: LanguageEditComponent;
  let fixture: ComponentFixture<LanguageEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LanguageEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LanguageEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
