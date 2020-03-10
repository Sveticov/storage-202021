import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class BoardService {
 host:string
  constructor(private http:HttpClient) {
    this.host='http://172.20.255.254:8097/app/storage/board/'
  }

  onTestBoard(x:number,z:number):Observable<any>{
    return this.http.get(this.host+x+'/'+z)
  }
  onDeleteBoardBuId(id:number):Observable<any>{
   return this.http.get(this.host+'delete/'+id)
  }
 onShowStorage():Observable<any>{
   return this.http.get(this.host+'/all')
 }
}
