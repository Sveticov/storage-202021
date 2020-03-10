import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {SettingsST} from "../model/SettingsST";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class SettingsService {
host:string

  constructor(private http:HttpClient) {
  this.host='http://172.20.255.254:8097/app/settings'
  }
  onAddSettings(settings:SettingsST):Observable<any>{
  return this.http.post(this.host+'/add',settings)
  }
  onFindLastSettings():Observable<any>{
  return this.http.get(this.host+'/last')
  }
  onDeleteSettings(id:number):Observable<any>{
  return this.http.get(this.host+'/delete/'+id)
  }
  onLastID(id:number):Observable<any>{
  return this.http.get(this.host+'/last/'+id)
  }

}
