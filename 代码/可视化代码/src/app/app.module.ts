import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { ChinaMapComponent } from './components/china-map/china-map.component';
import { ProjectPopularComponent } from './components/project-popular/project-popular.component';
import { TagWordCloudComponent } from './components/tag-word-cloud/tag-word-cloud.component';
import {NgZorroAntdModule, NzIconModule} from 'ng-zorro-antd';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NzSpaceModule} from 'ng-zorro-antd/space';
import {NgxEchartsModule} from 'ngx-echarts';
import {GlobalMapComponent} from './components/global-map/global-map.component';


@NgModule({
  declarations: [
    AppComponent,
    GlobalMapComponent,
    ChinaMapComponent,
    ProjectPopularComponent,
    TagWordCloudComponent,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserModule,
    AppRoutingModule,
    NgZorroAntdModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    NzIconModule,
    NzSpaceModule,
    NgxEchartsModule.forRoot({
      echarts: () => import('echarts')
    }),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
