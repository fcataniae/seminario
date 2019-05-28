import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmarMovimientoComponent } from './confirmar-movimiento.component';

describe('ConfirmarMovimientoComponent', () => {
  let component: ConfirmarMovimientoComponent;
  let fixture: ComponentFixture<ConfirmarMovimientoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfirmarMovimientoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmarMovimientoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
