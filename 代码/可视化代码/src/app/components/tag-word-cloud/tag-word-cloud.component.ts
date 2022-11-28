import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import * as echarts from 'echarts';
import 'echarts-wordcloud/src/wordCloud.js';
import {forkJoin} from 'rxjs';

@Component({
  selector: 'app-tag-word-cloud',
  templateUrl: './tag-word-cloud.component.html',
  styleUrls: ['./tag-word-cloud.component.css']
})

export class TagWordCloudComponent implements OnInit {

  index = 0;

  @ViewChild('wordCloud') wordCloud;
  private imageData = {};
  private jsonData = [];
  private year = ['2009', '2010', '2011', '2012', '2013', '2014', '2015', '2016', '2017', '2018', '2019', '2020', '2021', '2022'];
  private imageMap = [
    'andriod',
    'eclipse',
    'spring',
    'facebook',
    'google',
    'alibaba',
    'rocketmq',
    'rabbitmq',
    'kafka',
    'apache',
    'storm',
    'flink',
    'spark',
    'java'];

  private color = [
    '124,255,178',
    '141,72,227',
    '124,255,178',
    '73,146,255',
    '253,221,96',
    '255,99,71',
    '221,121,255',
    '255,110,118',
    '88,217,249',
    '255,110,118',
    '253,221,96',
    '224,31,84',
    '255,110,118',
    '88,217,249'  ];
  public options = [];

  constructor(public  httpClient: HttpClient, private ref: ChangeDetectorRef) {
//    this.loadJsonData();
    //  this.loadImage();
  }


  ngOnInit(): void {


    this.loadWordCloud();


  }

  public init() {

    this.wordCloud.goTo(0);
  }

  /*
    private loadJsonData() {


      this.httpClient.get('assets/data/wordCloud.json').subscribe((jsonData: any) => {

        this.jsonData = jsonData;
        console.log(this.jsonData);
      });
    }


    private loadImage() {
      this.httpClient.get('assets/data/image.json').subscribe((jsonData: any) => {
        // alert(jsonData);
        this.imageData = jsonData;

      });
    }
  */


  private loadWordCloud() {
    const sources = [this.httpClient.get('assets/data/wordCloud.json'), this.httpClient.get('assets/data/image.json')];
    forkJoin(sources).subscribe((res: any) => {
      this.jsonData = res[0];
      this.imageData = res[1];
      let current = -1;
      this.year.forEach(o => this.options.push(this.loadOption(++current)));

      // this.loadOption();
      /* setInterval(() => {

         this.index = (this.index % this.year.length);
         this.index++;
         //  this.updateOption();
         this.loadOption();

       }, 10000);*/

    });


  }

  /*  private updateOption() {
      const image = new Image();
      image.src = this.imageData[this.imageMap[this.index]];
      const yearText = this.year[this.index];
      this.updateOptions = {
        title: {
          text: yearText + '年',
        },
        series: [{
          maskImage: image,
          data: this.jsonData[this.index]
        }]
      };
    }*/

  private loadOption(current: number): any {
    const yearText = this.year[current];
    const image = new Image();
    image.src = this.imageData[this.imageMap[current]];
    return {
      title: {
        text: yearText + '年' + 'Java流行标签',
        x: 'center',
        top: 30,
        textStyle: {
          fontSize: 25,
          color: '#fff'
        },
      },
      backgroundColor: '#000',
      series: [{
        type: 'wordCloud',
        sizeRange: [15, 35],
        rotationRange: [-45, 0, 45, 90],

        width: '45%',
        height: '80%',
        maskImage: image,
        drawOutOfBound: false,
        layoutAnimation: false,
        textStyle: {
          fontFamily: 'sans-serif',
          fontWeight: 'bold',
          // 颜色可以用一个函数来返回字符串
          color: () => {
            // Random color
            return 'rgb(' + this.color[current % this.color.length] + ')';
          },
          emphasis: {
            shadowBlur: 10,
            shadowColor: '#ffffff'
          }
        },
        data: this.jsonData[current]
      }]
    };

    // this.ref.markForCheck();	// 就是在拿到数据后，执行这两行代码，这是关键
    //this.ref.detectChanges();


  }


}
