import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PLCDbData} from "../model/PLCDbData";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class PlcdbService {
private host:string
  constructor(private http:HttpClient) {
  this.host='http://172.20.255.254:8097/app/plcdb'
  }

  onCreateDB(plcDB:PLCDbData):Observable<any>{
  return this.http.post(this.host+'/create',plcDB)
  }

  onAllPLCDB():Observable<any>{
  return this.http.get(this.host+"/all/db")
  }

  onDeletePLCDB(id:number):Observable<any>{
  return this.http.get(this.host+"/delete/"+id);
  }
}
