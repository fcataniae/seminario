import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InformeStockComponent } from '../../informe-stock.component';

describe('InformeStockComponent', () => {
  let component: InformeStockComponent;
  let fixture: ComponentFixture<InformeStockComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InformeStockComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InformeStockComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
