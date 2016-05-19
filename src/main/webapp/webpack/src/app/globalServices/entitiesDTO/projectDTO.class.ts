import {Serializable} from './serializable.class.ts'

import {TaskDTO} from './taskDTO.class'
import {UserDTO} from './userDTO.class'

export class ProjectDTO extends Serializable{
  id:string;
  name:string;
  description:string;
  creationDateTime:string;
  status:string;
  taskList:TaskDTO[]=[];
  owner:UserDTO;
}
