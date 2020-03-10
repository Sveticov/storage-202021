import {Component, OnInit} from '@angular/core';
import {SettingsST} from "./model/SettingsST";
import {NgForm} from "@angular/forms";
import {SettingsService} from "./service/settings.service";
import {SettingStoragService} from "../setting-storag/setting-storag.service";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {
  settingsST: SettingsST
  settingsBack: SettingsST[]
  lastSettingST: SettingsST


  constructor(private settingsService: SettingsService,
              private setStoragService: SettingStoragService) {
    this.settingsST = new SettingsST();
  }

  ngOnInit() {
    this.lastSettings()
  }

  settingSubmit(settingForm: NgForm) {
    console.log(settingForm)
    console.log(this.settingsST)

    this.settingsService.onAddSettings(this.settingsST).subscribe(sett => this.settingsBack = sett)
  }

  public lastSettings() {
    this.settingsService.onFindLastSettings().subscribe(sett => this.settingsBack = sett)
  }

  deleteSetting(idSettings: number) {
    console.log(this.settingsST);
    this.settingsService.onDeleteSettings(idSettings).subscribe(sett => this.settingsBack = sett)
  }

  okSetting(id: number) {
    this.settingsService.onLastID(id).subscribe(set => this.setStoragService.addSetting(set.valueOf().storageXOffset,set.valueOf().storageZOffset))

  }
}
