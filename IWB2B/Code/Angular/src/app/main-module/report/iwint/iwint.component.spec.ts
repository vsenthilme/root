import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IwintComponent } from './iwint.component';

describe('IwintComponent', () => {
  let component: IwintComponent;
  let fixture: ComponentFixture<IwintComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IwintComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IwintComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
