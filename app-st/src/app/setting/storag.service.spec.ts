import { TestBed } from '@angular/core/testing';

import { StoragService } from './storag.service';

describe('StoragService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: StoragService = TestBed.get(StoragService);
    expect(service).toBeTruthy();
  });
});
