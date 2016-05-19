import {Injectable} from "@angular/core";
import * as moment from 'moment';


@Injectable()
export class DateFormatter {
  constructor() {
  }

  public changeDateTimeToRuWithPattern(date:string, pattern:string):string {
    moment.locale('ru');
    return moment(date, moment.ISO_8601)
      .format(pattern).toString();
  }

  public getUtcOffset():string {
    return moment().format('Z');
  }

  public getCurrentDate():string{
    return moment().format("YYYY-MM-DD");
  }

  public getCurrentMonth():string{
    return moment().format("DD MMM");
  }

  public transformTime(time:string):string{
    return time.replace(":","ч")+'мин';
  }
}
