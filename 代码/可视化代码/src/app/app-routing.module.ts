import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {GlobalMapComponent} from './components/global-map/global-map.component';
import {ProjectPopularComponent} from './components/project-popular/project-popular.component';
import {ChinaMapComponent} from './components/china-map/china-map.component';
import {TagWordCloudComponent} from './components/tag-word-cloud/tag-word-cloud.component';



const routes: Routes = [
  {
    path: 'global', component: GlobalMapComponent

  }, {
    path: 'popular', component: ProjectPopularComponent

  }, {
    path: 'china', component: ChinaMapComponent

  }, {
    path: 'wordCloud', component: TagWordCloudComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
