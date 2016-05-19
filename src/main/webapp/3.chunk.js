webpackJsonp([3],{751:function(t,e,r){"use strict";var s=r(1),n=r(136);r(219);var o=function(){function API_Task(t){this.http=t}return API_Task.prototype.getJsonHeader=function(){var t=new n.Headers;return t.append("Content-Type","application/json"),t},API_Task.prototype.getUrlencodedHeader=function(){var t=new n.Headers;return t.append("Content-Type","application/x-www-form-urlencoded"),t},API_Task.prototype.correctOffset=function(t){return t.replace("+","%2B")},API_Task.prototype.getProjectHighTaskList=function(t,e){var r=this.getUrlencodedHeader(),s=new n.URLSearchParams;return s.set("projectId",t),s.set("utcOffset",this.correctOffset(e)),this.http.get("/api/task/getProjectHighTaskList",{headers:r,search:s}).map(function(t){return t.json()})},API_Task.prototype.getCreatedTaskList=function(t){var e=this.getUrlencodedHeader(),r=new n.URLSearchParams;return r.set("utcOffset",this.correctOffset(t)),this.http.get("/api/task/getCreatedTaskList",{headers:e,search:r}).map(function(t){return t.json()})},API_Task.prototype.createTask=function(t,e,r){var s=this.getJsonHeader(),o=new n.URLSearchParams;o.set("utcOffset",this.correctOffset(r));var a=JSON.stringify({name:t,ancestorProjectId:e});return this.http.post("/api/task/createTask",a,{headers:s,search:o}).map(function(t){return t.json()})},API_Task.prototype.addTaskExecutor=function(t,e){var r=JSON.stringify({id:e}),s=this.getJsonHeader(),o=new n.URLSearchParams;return o.set("taskId",t),this.http.put("/api/task/addTaskExecutor",r,{headers:s,search:o}).map(function(t){return t.json()})},API_Task.prototype.changeName=function(t,e){var r=JSON.stringify({id:t,name:e}),s=this.getJsonHeader();return this.http.put("/api/task/setName",r,{headers:s}).map(function(t){return t.json()})},API_Task.prototype.changeStatus=function(t,e){var r=JSON.stringify({id:t,status:e}),s=this.getJsonHeader();return this.http.put("/api/task/setStatus",r,{headers:s}).map(function(t){return t.json()})},API_Task.prototype.changeDescription=function(t,e){var r=JSON.stringify({id:t,description:e}),s=this.getJsonHeader();return this.http.put("/api/task/setDescription",r,{headers:s}).map(function(t){return t.json()})},API_Task.prototype.checkLowLevelAuthorities=function(t){var e=this.getUrlencodedHeader(),r=new n.URLSearchParams;return r.set("id",t),this.http.get("/api/task/checkLowLevelAuthorities",{headers:e,search:r}).map(function(t){return t.json()})},API_Task=__decorate([s.Injectable(),__metadata("design:paramtypes",[n.Http])],API_Task)}();e.API_Task=o},749:function(t,e,r){"use strict";var s=r(748),n=function(t){function ProjectDTO(){t.apply(this,arguments),this.taskList=[]}return __extends(ProjectDTO,t),ProjectDTO}(s.Serializable);e.ProjectDTO=n},748:function(t,e){"use strict";var r=function(){function Serializable(){}return Serializable.prototype.fillFromJSON=function(t){var e=JSON.parse(t);for(var r in e)this[r]=e[r]},Serializable}();e.Serializable=r},466:function(t,e,r){"use strict";var s=r(1),n=r(463),o=r(751),a=r(749),i=r(462),p=function(){function Reports(t,e,r){this.api_Project=t,this.api_Task=e,this.dateFormatter=r,this.reportTypeList=["Проект","Задача"],this.projectList=[]}return Reports.prototype.ngOnInit=function(){this.getCurrentUserProjects()},Reports.prototype.reportTypeSelected=function(){this.selectedType==this.reportTypeList[0]&&this.getCurrentUserProjects(),this.selectedType==this.reportTypeList[1]&&this.getCurrentUserTaskList()},Reports.prototype.getCurrentUserProjects=function(){var t=this;this.api_Project.getCreatedProjectList(this.dateFormatter.getUtcOffset()).subscribe(function(e){console.log(e),t.pushTransferProjectListToProject(e.responseObjects.projectList)},function(t){})},Reports.prototype.pushTransferProjectListToProject=function(t){for(var e=0;e<t.length;e++){var r=new a.ProjectDTO;r.fillFromJSON(JSON.stringify(t[e])),r.creationDateTime=this.dateFormatter.changeDateTimeToRuWithPattern(r.creationDateTime,"Do MMMM YYYY"),this.projectList.push(r)}},Reports.prototype.getCurrentUserTaskList=function(){var t=this;this.api_Task.getCreatedTaskList(this.dateFormatter.getUtcOffset()).subscribe(function(e){console.log(e),t.pushTransferProjectListToProject(e.responseObjects.projectList)},function(t){console.log(t)})},Reports=__decorate([s.Component({selector:"reports",providers:[n.API_Project,o.API_Task],directives:[],pipes:[],encapsulation:s.ViewEncapsulation.Native,styles:[r(776)],template:r(777)}),__metadata("design:paramtypes",[n.API_Project,o.API_Task,i.DateFormatter])],Reports)}();e.Reports=p},776:565,777:function(t,e){t.exports='<select [(ngModel)]="selectedType"\n        (ngModelChange)="reportTypeSelected()">\n  <option selected>Выберите отчет</option>\n  <option *ngFor="let reportType of reportTypeList">\n    {{reportType}}\n  </option>\n\n</select>\n\n<select>\n  <option>Выберете конкретный проект или задачу</option>\n  <option *ngFor="let project of projectList">\n    {{project.name}}\n  </option>\n</select>\n\n<label>С</label>\n<input placeholder="укажите дату в формате dd-mm-yyyy">\n<label>По</label>\n<input placeholder="укажите дату в формате dd-mm-yyyy">\n\n<select>\n  <option>Можете указать сотрудников</option>\n  <option>Все</option>\n</select>\n'}});
//# sourceMappingURL=3.bundle.map