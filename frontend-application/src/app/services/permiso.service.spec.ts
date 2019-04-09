import { TestBed, inject } from '@angular/core/testing';

import { PermisoService } from './permiso.service';

describe('PermisoService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PermisoService]
    });
  });

  it('should be created', inject([PermisoService], (service: PermisoService) => {
    expect(service).toBeTruthy();
  }));
});
