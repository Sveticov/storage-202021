import {Component, HostListener, OnInit} from '@angular/core';
import {CarServiceService} from "./service/car-service.service";
import {CarOne} from "./model/CarOne";
import {CarTwo} from "./model/CarTwo";

import {SatelitCarOne} from "./model/SatelitCarOne";
import {SatelitCarTwo} from "./model/SatelitCarTwo";
import {BoardService} from "./service/board.service";
import {Board} from "./model/Board";
import {interval} from "rxjs";
import {SettingStoragService} from "../setting-storag/setting-storag.service";

@Component({
  selector: 'app-app-storag-area',
  templateUrl: './app-storag-area.component.html',
  styleUrls: ['./app-storag-area.component.css']
})
export class AppStoragAreaComponent implements OnInit {
  windowX: string
  windowY: string
  carOne: CarOne;
  carOneModify: CarOne;
  carTwo: CarTwo;
  carTwoModify: CarTwo;
  satelitCarOne:SatelitCarOne;
  satelitCarTwo:SatelitCarTwo;
  boards:Board[]
  boardsModify:Board[]
  boardsModifyl:Board
  offsetX:number
  offsetZ:number

  constructor(private carService: CarServiceService,
              private boardService:BoardService,
              private settingStoragService:SettingStoragService) {
    this.carOne=new CarOne('0px','0px',false)
    this.carOneModify=new CarOne('0px','0px',false)
    this.carTwo=new CarTwo('0px','0px',false)
    this.carTwoModify=new CarTwo('0px','0px',false)
    this.satelitCarOne=new SatelitCarOne(0,0,0,0,true)
    this.satelitCarTwo=new SatelitCarTwo(0,0,0,0,true)
  }

  ngOnInit() {
    this.offsetX=this.settingStoragService.getSetting().x
    this.offsetZ=this.settingStoragService.getSetting().z

interval(1000).subscribe(car=>{
  this.onPositionCarOne()
  this.onPositionCarTwo()

  }
)
    interval(10000).subscribe(boards=>
      this.onShowAllBoards()
    )

  }

  @HostListener('window:resize', ['$event'])
  onResize(event?) {
    this.windowX = window.innerWidth-20 + 'px'
    this.windowY = window.innerHeight-150 + 'px'
    // console.log(this.windowY + " " + this.windowX)
  }

  onPositionCarOne(){
    this.carService.onPositionCarOne().subscribe(car=>this.carOne=car)
    // console.log('pos 1x: '+parseInt(this.carOne.positionX))
    // console.log('pos 1z: '+this.carOne.positionZ)
    this.carOneModify.positionX=((parseInt(this.carOne.positionX)-100000)/111.67)+100+''
    this.carOneModify.positionZ=((parseInt(this.carOne.positionZ)-143570)/(-90.698))+100+''
    // console.log("x pos1")
    // console.log(this.carOneModify.positionX)

  }
  onPositionCarTwo(){
    this.carService.onPositionCarTwo().subscribe(car=>this.carTwo=car)
    // console.log('pos 2: '+this.carTwo.positionX)
    // console.log('pos 2:'+this.carTwo.positionZ)
    this.carTwoModify.positionX=((parseInt(this.carTwo.positionX)-100000)/111.67)+100+''
    this.carTwoModify.positionZ=((parseInt(this.carTwo.positionZ)-143570)/(-90.698))+100+''
    // console.log("x pos")
    // console.log(this.carTwoModify.positionX)

  }
  onTestBoardStandStillCarOne(){
this.boardService.onTestBoard(parseInt(this.carOneModify.positionX),parseInt(this.carOneModify.positionZ))
  .subscribe(boards=>{this.boards=boards;console.log(boards);})
  }

  onDeleteBoard(idBox: number) {
    this.boardService.onDeleteBoardBuId(idBox).subscribe(
      boards=>this.boards=boards
    )
  }
  onShowAllBoards(){
    this.boardService.onShowStorage().subscribe(
      boards=>this.boards=boards
    )
  }


}
