import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TablaDeudasComponent } from './tabla-deudas.component';

describe('TablaDeudasComponent', () => {
  let component: TablaDeudasComponent;
  let fixture: ComponentFixture<TablaDeudasComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TablaDeudasComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TablaDeudasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
