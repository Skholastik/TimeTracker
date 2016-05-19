import {Serializable} from './serializable.class.ts'
import {UserDTO} from './userDTO.class'
import {ReportDTO} from './reportDTO.class'

export class TaskDTO extends Serializable{
  id:string;
  name:string;
  description:string;
  creationDateTime:string;
  plannedEndDateTime:string;
  actualEndDateTime:string;
  status:string;
  owner:UserDTO;
  executor:UserDTO;
  reportList:ReportDTO[]=[];
}
