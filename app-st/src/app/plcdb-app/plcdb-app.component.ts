import {Component, OnInit} from '@angular/core';
import {NgForm} from "@angular/forms";
import {PLCDbData} from "./model/PLCDbData";
import {PlcdbService} from "./service/plcdb.service";

@Component({
  selector: 'app-plcdb-app',
  templateUrl: './plcdb-app.component.html',
  styleUrls: ['./plcdb-app.component.css']
})
export class PlcdbAppComponent implements OnInit {

  plcDbData: PLCDbData
  plcDBDataAll: PLCDbData[]

  constructor(private plcdbService: PlcdbService) {
    this.plcDbData = new PLCDbData()
  }

  ngOnInit() {
    this.onAllPLCDBData()
  }

  plcDbSubmit(plcForm: NgForm) {
    this.plcdbService.onCreateDB(this.plcDbData).subscribe(plcdbs => this.plcDBDataAll = plcdbs)

  }

  onAllPLCDBData() {
    this.plcdbService.onAllPLCDB().subscribe(plcdbs => this.plcDBDataAll = plcdbs)
  }

  onDeletePLCDB(idDB: number) {
    this.plcdbService.onDeletePLCDB(idDB).subscribe(plcdbs=>this.plcDBDataAll=plcdbs)
  }
}
