import {Component, ViewChild} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'bigdata';
  @ViewChild('globalMap') globalMap;
  @ViewChild('chinaMap') chinaMap;
  @ViewChild('tagWordCloud') tagWordCloud;
  @ViewChild('projectPopular') projectPopular;

  public changePage(fromTo: any) {

    if (fromTo.to === 1) {

      this.chinaMap.init();
    } else if (fromTo.to === 2) {

      this.tagWordCloud.init();
    } else if (fromTo.to === 3) {
      this.projectPopular.init();
    } else {

    }
  }


}
