import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";


@Injectable({
  providedIn: 'root'
})
export class CarServiceService {
private readonly host:string
  constructor(private http:HttpClient) {
  this.host='http://172.20.255.254:8097/app/car/'
  }

  onPositionCarOne():Observable<any>{
    return this.http.get(this.host+'car1')
  }
  onPositionCarTwo():Observable<any>{
    return this.http.get(this.host+'car2')
  }
}
