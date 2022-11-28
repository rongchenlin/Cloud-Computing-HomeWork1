import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-project-popular',
  templateUrl: './project-popular.component.html',
  styleUrls: ['./project-popular.component.css']
})
export class ProjectPopularComponent implements OnInit {
  colors = ['#4992ff',
    '#7cffb2',
    '#fddd60',
    '#ff6e76',
    '#58d9f9',
    '#05c091',
    '#ff8a45',
    '#8d48e3',
    '#dd79ff'];
  year = ['2009年', '2010年', '2011年', '2012年', '2013年', '2014年', '2015年', '2016年', '2017年', '2018年', '2019年', '2020年', '2021年', '2022年'];
  popularOption: any;

  constructor(public  httpClient: HttpClient) {
  }

  ngOnInit(): void {
    this.loadBar();
  }

  public init() {
    this.loadBar();
  }

  private loadBar() {
    this.httpClient.get('assets/data/popular.json').subscribe((jsonData: any) => {
      const categoryData = [];

      const barData = [];
      let i = 0;

      for (const key in jsonData) {
        barData.push([]);
        categoryData.push([]);
        for (let j = 0; j < jsonData[key].length; j++) {
          barData[i].push(jsonData[key][j].pullRequests);
          categoryData[i].push(jsonData[key][j].proName);
        }
        barData[i].reverse();
        categoryData[i].reverse();
        i += 1;
      }
      this.popularOption = {
        timeline: {
          data: this.year,
          axisType: 'category',
          autoPlay: true,
          playInterval: 2500,
          left: '10%',
          right: '10%',
          bottom: '10%',
          width: '80%',
          //  height: null,
          label: {
            normal: {
              textStyle: {
                color: '#fff',
                fontSize: 16
              }
            },
            emphasis: {
              textStyle: {
                color: '#fff',
                fontSize: 16
              }
            }
          },
          symbol: 'none',
          lineStyle: {
            color: '#fff',
            fontSize: 16
          },
          checkpointStyle: {
            borderColor: '#fff',
            borderWidth: 2
          },
          controlStyle: {
            showNextBtn: false,
            showPrevBtn: false,
            normal: {
              color: '#666',
              borderColor: '#666',
              fontSize: 16
            },
            emphasis: {
              color: '#aaa',
              borderColor: '#aaa'
            }
          },

        },
        baseOption: {
          animation: true,
          animationDuration: 1500,
          animationEasing: 'cubicInOut',
          animationDurationUpdate: 1500,
          animationEasingUpdate: 'cubicInOut',
          grid: {
            left: '25%',
            top: '15%',
            bottom: '15%',
            width: '50%'
          },
          tooltip: {
            trigger: 'axis', // hover触发器
            axisPointer: { // 坐标轴指示器，坐标轴触发有效
              type: 'shadow', // 默认为直线，可选为：'line' | 'shadow'
              shadowStyle: {
                color: 'rgba(150,150,150,0.1)',
              }
            }
          }
        },
        options: []

      };
      for (let n = 0; n < this.year.length; n++) {
        this.popularOption.options.push({
          backgroundColor: '#000',
          title: [{},
            {
              id: 'statistic',
              text: this.year[n] + 'Java开源项目活跃度',
              left: 'center',
              top: '3%',
              textStyle: {
                color: '#fff',
                fontSize: 25
              }
            }
          ],
          xAxis: {
            type: 'value',
            scale: true,
            position: 'top',
            min: 0,
            boundaryGap: false,
            splitLine: {
              show: false
            },
            axisLine: {
              show: false
            },
            axisTick: {
              show: false
            },
            axisLabel: {
              margin: 2,
              textStyle: {
                color: '#fff',
                fontSize: 15
              }
            }
          },
          yAxis: {
            type: 'category',
            //  name: 'TOP 20',
            nameGap: 16,
            axisLine: {
              show: true,
              lineStyle: {
                color: '#ddd'
              }
            },
            axisTick: {
              show: false,
              lineStyle: {
                color: '#ddd'
              }
            },
            axisLabel: {
              interval: 0,
              textStyle: {
                color: '#fff',
                fontSize: 15
              }
            },
            data: categoryData[n]
          },
          series: [
            {
              zlevel: 2,
              type: 'bar',
              symbol: 'none',
              itemStyle: {
                normal: {
                  color: this.colors[n % this.colors.length]
                }
              },
              width: '100%',
              height: '100%',
              data: barData[n]
            }]
        });
      }

    });

  }

}
