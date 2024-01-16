import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LanguageIdComponent } from './language-id.component';

describe('LanguageIdComponent', () => {
  let component: LanguageIdComponent;
  let fixture: ComponentFixture<LanguageIdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LanguageIdComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LanguageIdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
