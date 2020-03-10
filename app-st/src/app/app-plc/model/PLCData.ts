import {PLCDbData} from "../../plcdb-app/model/PLCDbData";

export class PLCData {

  id:number
  plcName:string
  adrIP: string
  lengthRead: number
  lengthWrite: number
  dbRead: number
  dbWrite: number
  rack: number
  slot: number
  plcDbData:PLCDbData[]
}
