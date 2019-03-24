import { Component, OnInit } from '@angular/core';
import { Chart } from 'chart.js';
import { Color, BaseChartDirective, Label } from 'ng2-charts';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'WALLiT-Dashboard';
  nextDate = new Date('2019-02-26T00:00:00');
  nextValue = 108;
  chart = null;
  chartData = null;

  ngOnInit() {
  	var canvas = <HTMLCanvasElement> document.getElementById("chart");
	var ctx = canvas.getContext("2d");
	this.chart = new Chart(ctx, {
	    type: 'line',
	    data: {
	        labels: ['2019-01-01T00:00:00','2019-01-02T00:00:00',
			  '2019-01-03T00:00:00','2019-01-04T00:00:00','2019-01-05T00:00:00',
			  '2019-01-06T00:00:00','2019-01-07T00:00:00','2019-01-08T00:00:00',
			  '2019-01-09T00:00:00','2019-01-10T00:00:00','2019-01-11T00:00:00',
			  '2019-01-12T00:00:00','2019-01-13T00:00:00','2019-01-14T00:00:00',
			  '2019-01-15T00:00:00','2019-01-16T00:00:00','2019-01-17T00:00:00',
			  '2019-01-18T00:00:00','2019-01-19T00:00:00','2019-01-20T00:00:00',
			  '2019-01-21T00:00:00','2019-01-22T00:00:00','2019-01-23T00:00:00',
			  '2019-01-24T00:00:00','2019-01-25T00:00:00','2019-01-26T00:00:00',
			  '2019-01-27T00:00:00','2019-01-28T00:00:00','2019-01-29T00:00:00',
			  '2019-01-30T00:00:00','2019-01-31T00:00:00','2019-02-01T00:00:00',
			  '2019-02-02T00:00:00','2019-02-03T00:00:00','2019-02-04T00:00:00',
			  '2019-02-05T00:00:00','2019-02-06T00:00:00','2019-02-07T00:00:00',
			  '2019-02-08T00:00:00','2019-02-09T00:00:00','2019-02-10T00:00:00',
			  '2019-02-11T00:00:00','2019-02-12T00:00:00','2019-02-13T00:00:00',
			  '2019-02-14T00:00:00','2019-02-15T00:00:00','2019-02-16T00:00:00',
			  '2019-02-17T00:00:00','2019-02-18T00:00:00','2019-02-19T00:00:00',
			  '2019-02-20T00:00:00','2019-02-21T00:00:00','2019-02-22T00:00:00',
			  '2019-02-23T00:00:00','2019-02-24T00:00:00','2019-02-25T00:00:00'],
	        datasets: [{
	            label: 'Alpha value',
	            data: [100,100.2498094,100.6155225,100.690999,100.8300365,101.3111866,101.7967969,
			    	102.3136151,102.5657118,102.3879484,102.0106974,102.5040854,102.341078,102.4427883,
			    	102.4850992,102.810702,102.4457907,102.3551049,102.4105543,102.9875922,103.3688392,
			    	103.0560711,103.4481524,103.2446832,103.3109998,103.6317988,103.6197369,104.0468228,
			    	104.3704896,104.513179,104.4352692,104.9031311,105.09669,105.6645403,105.806679,
			    	105.4605818,105.5428597,105.857247,106.088333,106.2531249,106.1547333,105.9019754,
			    	105.6692124,106.0436886,105.7339542,105.9914533,106.0301708,106.3293085,106.6010458,
			    	107.032005,107.4385323,107.6293926,107.8100855,107.7294207,107.6745468,107.5628946],
	            borderWidth: 1,
	            backgroundColor: '#EBEDF3',
		        borderColor: '#6699CC',
		        pointBackgroundColor: '#003B6D',
		        pointBorderColor: '#FFF',
		        pointHoverBackgroundColor: '#FFF',
		        pointHoverBorderColor: '#BDBDBD'
	        }],
	    },
	    options: {
	        scales: {
		      xAxes: [
		      	{
		    	  type: 'time',
		                time: {
		                    unit: 'week'
		                },
		    	  gridLines: {
		    	  	color: '#FFF'
		    	  },
		    	  ticks: {
		            fontColor: '#676767'
		          }
		      	}],
		      yAxes: [
		        {
		          id: 'y-axis-0',
		          position: 'left',
		          gridLines: {
		            color: '#FFF',
		          },
		          ticks: {
		            fontColor: '#676767',
		            min: 90,
		            max: 120
		          }
		        },
		      ]
    		},
    		legend: {
            	display: false,
            }
        }
	});

  }

  addData() {
  	this.chart.data.labels.shift();
	this.chart.data.labels.push(this.nextDate.toString());
    
    this.chart.data.datasets.forEach((dataset) => {
        dataset.data.shift();
        dataset.data.push(this.nextValue);
    });

    this.chart.update();

    this.nextDate.setDate(this.nextDate.getDate() + 1);
    this.nextValue = this.nextValue + Math.random() - 0.4;
  }

  updateTimeScale(code) {
  	
  }
  
  constructor() {
  }


}
