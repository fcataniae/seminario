import { TestBed, inject } from '@angular/core/testing';

import { RolService } from './rol.service';

describe('RolService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RolService]
    });
  });

  it('should be created', inject([RolService], (service: RolService) => {
    expect(service).toBeTruthy();
  }));
});
