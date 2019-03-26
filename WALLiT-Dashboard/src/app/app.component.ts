import { Component, OnInit } from '@angular/core';
import { Chart } from 'chart.js';
import { Color, BaseChartDirective, Label } from 'ng2-charts';
import * as $ from 'jquery';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {
  title = 'WALLiT-Dashboard';
  chart = null;

  ngOnInit() {
	document.getElementById("threeMonths").classList.add("btn-active");
  	document.getElementById("percentage").classList.add("btn-active");

	this.updateChartData("threeMonths");
  }

  updateTimeScale(code) {
  	document.getElementById("fiveYears").classList.remove("btn-active");
  	document.getElementById("oneYear").classList.remove("btn-active");
  	document.getElementById("sixMonths").classList.remove("btn-active");
  	document.getElementById("threeMonths").classList.remove("btn-active");
  	document.getElementById("oneMonth").classList.remove("btn-active");
  	
  	document.getElementById(code).classList.add("btn-active");

  	this.updateChartData(code);
  }

  updateChartType(code) {
  	document.getElementById("percentage").classList.remove("btn-active");
  	document.getElementById("euro").classList.remove("btn-active");

  	document.getElementById(code).classList.add("btn-active");
  }
  
  constructor() {
  } 

  updateChartData(code) {
  	$("canvas").remove();
	$(".chartBox").append('<canvas id="chart" height="120"></canvas>');

	$.getJSON("../assets/dataTest.json", function (data) {
  		var labelsJSON = [];
  		var valuesJSON = [];
  		var scale = 'week';

	    if (data.hasOwnProperty(code)) {
    		labelsJSON = Object.keys(data[code]);
    		valuesJSON = Object.values(data[code]);
	    }
	
	    var canvas = <HTMLCanvasElement> document.getElementById("chart");
		var ctx = canvas.getContext("2d");

		this.chart = new Chart(ctx, {
		    type: 'line',
		    data: {
		        labels: labelsJSON,
		        datasets: [{
		            label: 'Alpha value',
		            data: valuesJSON,
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
	            },
	            tooltips: {
	            	custom: function(tooltip) {
				        if (!tooltip) return;
				        // disable displaying the color box;
				        tooltip.displayColors = false;
				    },
		            callbacks: {
		                title: function(tooltipItems, data) {
		                    return tooltipItems[0].xLabel.substring(0,10);

		           		},
		           		label: function(tooltipItem, data) {
		           			var label = data.datasets[tooltipItem.datasetIndex].label || '';

		                    if (label) {
		                        label += ': ';
		                    }
		                    label += '' + tooltipItem.yLabel;
		                    return label.substring(0,19);
	                	}
	            	}
	        	}
	        }
		});

		if (code == "fiveYears") {
	    	this.chart.options.scales.xAxes[0].time.unit='month';
   			this.chart.update();
	    } else if (code == "oneMonth") {
	    	this.chart.options.scales.xAxes[0].time.unit='day';
   			this.chart.update();
	    }
    });
  }
}

