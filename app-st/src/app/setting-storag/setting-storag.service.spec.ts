import { TestBed } from '@angular/core/testing';

import { SettingStoragService } from './setting-storag.service';

describe('SettingStoragService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SettingStoragService = TestBed.get(SettingStoragService);
    expect(service).toBeTruthy();
  });
});
