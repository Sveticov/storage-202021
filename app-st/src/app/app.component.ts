import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {interval, Observable} from "rxjs";
import {PLCStatus} from "./PLCStatus";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'app-st';
  onPLCMenu: boolean = false;
  onStorageMenu: boolean = false;
  onPLCDBMenu: boolean = false;
  onHomePage: boolean = true;
  onSetting: boolean = false;
  private host: string;
  plcStatuses: PLCStatus[]

  constructor(private http: HttpClient) {
    this.host = "'http://172.20.255.254:8097/app/plc/status/connect"
  }

  ngOnInit() {
    interval(60000).subscribe(status=>{
      this.statusPLCConnect()
    })
  }


  statusPLCConnect() {
    this.statusPLC().subscribe(plcStatus => this.plcStatuses = plcStatus)
  }

  statusPLC(): Observable<any> {
    return this.http.get(this.host)
  }

  showPLCMenu() {
    this.onStorageMenu = false
    this.onHomePage = false
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
    this.onHomePage = false
    if (this.onPLCDBMenu == false) this.onPLCDBMenu = true
    else this.onPLCDBMenu = false
  }

  onShowHome() {
    this.onPLCMenu = false
    this.onPLCDBMenu = false
    this.onStorageMenu = false
    if (this.onHomePage == false) this.onHomePage = true
    else this.onHomePage = false
  }

  showSetting() {
    this.onPLCMenu = false
    this.onPLCDBMenu = false
    this.onStorageMenu = false
    if (this.onSetting == false) this.onSetting = true
    else this.onSetting = false
  }
}
