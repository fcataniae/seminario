import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmacionPopupComponent } from './confirmacion-popup.component';

describe('ConfirmacionPopupComponent', () => {
  let component: ConfirmacionPopupComponent;
  let fixture: ComponentFixture<ConfirmacionPopupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfirmacionPopupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmacionPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
