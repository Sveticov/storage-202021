import {Injectable} from '@angular/core';
import {SettingStorage} from "./SettingStorage";
import {Observable} from "rxjs";


@Injectable({
  providedIn: 'root'
})
export class SettingStoragService {
  s: SettingStorage

  constructor() {
    this.s = new SettingStorage();
  }

  addSetting(x: number, z: number) {
    this.s.x = x
    this.s.z = z
  }

  getSetting() {
    return this.s
  }
}
