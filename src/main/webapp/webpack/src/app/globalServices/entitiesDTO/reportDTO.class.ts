import {Serializable} from './serializable.class.ts'
import {UserDTO} from './userDTO.class'

export class ReportDTO extends Serializable{
  id:string;
  report:string;
  workTime:string;
  workDate:string;
  creationDateTime:string;
  creator:UserDTO;
}
