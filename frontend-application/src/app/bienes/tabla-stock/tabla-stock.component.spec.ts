import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TablaStockComponent } from './tabla-stock.component';

describe('TablaStockComponent', () => {
  let component: TablaStockComponent;
  let fixture: ComponentFixture<TablaStockComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TablaStockComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TablaStockComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
