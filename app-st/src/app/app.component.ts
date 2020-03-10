import {Component} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app-st';
  onPLCMenu: boolean = false;
  onStorageMenu: boolean = false;
  onPLCDBMenu: boolean = false;
  onHomePage:boolean=true;
  onSetting: boolean=false;

  showPLCMenu() {
    this.onStorageMenu = false
    this.onHomePage=false
    if (this.onPLCMenu == false) {
      this.onPLCMenu = true

    } else {
      this.onPLCMenu = false

    }
    console.log("click " + this.onPLCMenu)
  }

  showStorageMenu() {
    this.onPLCMenu = false
    this.onPLCDBMenu = false
    if (this.onStorageMenu == false) this.onStorageMenu = true
    else {
      this.onStorageMenu = false
    }
  }

  showPLCDBMenu() {
    this.onStorageMenu = false
    this.onHomePage=false
    if (this.onPLCDBMenu == false) this.onPLCDBMenu = true
    else this.onPLCDBMenu = false
  }

  onShowHome() {
    this.onPLCMenu = false
    this.onPLCDBMenu = false
    this.onStorageMenu = false
    if(this.onHomePage==false) this.onHomePage=true
    else this.onHomePage=false
  }

  showSetting() {
    this.onPLCMenu = false
    this.onPLCDBMenu = false
    this.onStorageMenu = false
    if(this.onSetting==false)this.onSetting=true
    else this.onSetting=false
  }
}
