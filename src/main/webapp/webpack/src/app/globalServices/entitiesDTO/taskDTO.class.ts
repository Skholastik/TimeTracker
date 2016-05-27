import {Serializable} from './serializable.class.ts'
import {UserDTO} from './userDTO.class'
import {ReporterDTO} from './reporterDTO.class'

export class TaskDTO extends Serializable{
  id:string;
  name:string;
  description:string;
  creationDateTime:string;
  plannedEndDateTime:string;
  actualEndDateTime:string;
  status:string;
  creator:UserDTO;
  executor:UserDTO;
  reporterList:ReporterDTO[]=[];
}
