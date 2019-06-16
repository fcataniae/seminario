import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InformeMovimientosComponent } from './informe-movimientos.component';

describe('InformeMovimientosComponent', () => {
  let component: InformeMovimientosComponent;
  let fixture: ComponentFixture<InformeMovimientosComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InformeMovimientosComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InformeMovimientosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
