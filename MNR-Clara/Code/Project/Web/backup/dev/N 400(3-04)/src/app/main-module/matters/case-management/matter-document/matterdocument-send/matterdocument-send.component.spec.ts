import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatterdocumentSendComponent } from './matterdocument-send.component';

describe('MatterdocumentSendComponent', () => {
  let component: MatterdocumentSendComponent;
  let fixture: ComponentFixture<MatterdocumentSendComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatterdocumentSendComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatterdocumentSendComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
