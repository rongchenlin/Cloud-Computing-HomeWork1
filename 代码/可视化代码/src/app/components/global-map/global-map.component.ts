import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import * as echarts from 'echarts';

@Component({
  selector: 'app-global-map',
  templateUrl: './global-map.component.html',
  styleUrls: ['./global-map.component.css']
})

export class GlobalMapComponent implements OnInit {
  globalMapOption: any;
  globalBarOption: any;
  chartInstance: any;
  prevIndex = -1;
  updateOptions: any;
  majorLanguage = [[], [], [], [], [], [], []];
  indexLanguage = {Java: 0, JavaScript: 1, Python: 2, 'C++/C': 3, Go: 4, HTML: 5, PHP: 6};

  colors = ['#4992ff',
    '#7cffb2',
    '#fddd60',
    '#ff6e76',
    '#58d9f9',
    '#05c091',
    '#ff8a45',
    '#8d48e3',
    '#dd79ff'];
  title = '';

  constructor(public  httpClient: HttpClient) {
  }

  ngOnInit(): void {
    this.loadMap();
    this.title = '世界各种编程语言人数';
  }

  onChartInit(e: any) {
    this.chartInstance = e;

  }


  private loadMap(): void {

    this.httpClient.get('assets/data/world.json').subscribe((geoJson: any) => {
      echarts.registerMap('world', geoJson, {
        Alaska: {
          left: -131,
          top: 25,
          width: 15
        },
        Hawaii: {
          left: -110,
          top: 28,
          width: 5
        },
        'Puerto Rico': {
          left: -76,
          top: 26,
          width: 2
        }
      });
      this.globalMapOption = {

        geo: {
          selectedMode: 'multiple',
          map: 'world',
          roam: false,
          itemStyle: {
            normal: {
              areaColor: '#fff',
              borderColor: '#000', //线
              borderWidth: 0,
              borderJoin: 'round',
              shadowColor: 'grey', //外发光
              shadowOffsetX: -3,
              shadowOffsetY: 5,
              shadowBlur: 2, //图形阴影的模糊大小
            },
            emphasis: {
              areaColor: '#2f9eff', //悬浮区背景
            },
          },
          select: {

            label: {
              show: true,
              color: '#111'
            }
          }
        },
        grid: {

          top: '1%',
        },
        tooltip: {},
        legend: {},
        series: []
      };
    });


    this.httpClient.get('assets/data/language.json').subscribe((barData: any) => {

      barData.forEach(v => {
        const lan = v.language;
        let index = -1;
        let max = -1;
        lan.forEach((o, idx) => {
          if (o > max) {
            max = o;
            index = idx;
          }
        });
        this.majorLanguage[index].push(v.country);
      });
      this.globalBarOption = {
        /*    legend: {
              top: 'bottom'
            },*/
        grid: {

          top: 0,
          bottom: 50
        },

        series: [

          {
            name: '世界编程语言人数',
            type: 'pie',
            radius: [50, 125],
            center: ['50%', '50%'],
            roseType: 'area',
            label: {
              color: '#fff',
              fontSize: 15
            },
            itemStyle: {
              borderRadius: 8
            },
            data: [
              {value: barData.map(o => o.language[0]).reduce((n1, n2) => n1 + n2), name: 'Java'},
              {value: barData.map(o => o.language[1]).reduce((n1, n2) => n1 + n2), name: 'JavaScript'},
              {value: barData.map(o => o.language[2]).reduce((n1, n2) => n1 + n2), name: 'Python'},
              {value: barData.map(o => o.language[3]).reduce((n1, n2) => n1 + n2), name: 'C++/C'},
              {value: barData.map(o => o.language[4]).reduce((n1, n2) => n1 + n2), name: 'Go'},
              {value: barData.map(o => o.language[5]).reduce((n1, n2) => n1 + n2), name: 'HTML'},
              {value: barData.map(o => o.language[6]).reduce((n1, n2) => n1 + n2), name: 'PHP'},
            ]
          }
        ]
      };
    });

  }

  public changeBar(params: any) {
    const index = this.indexLanguage[params.data.name];
    if (index !== this.prevIndex) {


      this.updateOptions =
        {
          geo: {
            select: {
              itemStyle: {
                areaColor: this.colors[index % this.colors.length], //悬浮区背景
              },
            }
          }
        };
      if (this.prevIndex !== -1) {

        this.majorLanguage[this.prevIndex].forEach(o => {
          this.chartInstance.dispatchAction({
            type: 'geoUnSelect',
            name: o,
          });
        });

        this.title = '世界' + params.data.name + '编程语言占多数的国家';
      }

      this.majorLanguage[index].forEach(o => {
        this.chartInstance.dispatchAction({
          type: 'geoSelect',
          name: o,
        });
      });
      this.prevIndex = index;
    }


  }


}
