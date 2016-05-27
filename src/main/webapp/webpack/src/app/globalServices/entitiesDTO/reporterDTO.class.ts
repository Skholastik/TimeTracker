import {Serializable} from './serializable.class.ts'

import {ReportDTO} from './reportDTO.class'

export class ReporterDTO extends Serializable {
  id:string;
  name:string;
  taskElapsedTime:string;
  reportList:ReportDTO[]=[];
}
