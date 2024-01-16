import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientMatterComponent } from './client-matter.component';

describe('ClientMatterComponent', () => {
  let component: ClientMatterComponent;
  let fixture: ComponentFixture<ClientMatterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientMatterComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientMatterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
