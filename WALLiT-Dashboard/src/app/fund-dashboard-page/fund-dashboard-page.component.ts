import { Component, OnInit } from '@angular/core';
import { Chart } from 'chart.js';
import { Color, BaseChartDirective, Label } from 'ng2-charts';
import { DataService } from "../data.service";
import * as $ from 'jquery';

@Component({
  selector: 'app-fund-dashboard-page',
  templateUrl: './fund-dashboard-page.component.html',
  styleUrls: ['./fund-dashboard-page.component.css']
})
export class FundDashboardPageComponent implements OnInit {
  title = 'WALLiT-Dashboard';
  chart = null;
  type = "percentage";
  scale = "threeMonths";
  message:string;

  ngOnInit() {
  	this.data.currentMessage.subscribe(message => {
  		if (message == "") {
  			document.getElementById("value").innerHTML = "(n/a)";
  			document.getElementById("alpha").innerHTML = "(n/a)";
  		}
  	});

	document.getElementById(this.scale).classList.add("btn-active");
  	document.getElementById(this.type).classList.add("btn-active");

	this.updateChartData(this.scale);
  }

  updateTimeScale(scale) {
  	document.getElementById("fiveYears").classList.remove("btn-active");
  	document.getElementById("oneYear").classList.remove("btn-active");
  	document.getElementById("sixMonths").classList.remove("btn-active");
  	document.getElementById("threeMonths").classList.remove("btn-active");
  	document.getElementById("oneMonth").classList.remove("btn-active");
  	document.getElementById("fiveYears").classList.remove("btn-euro-active");
  	document.getElementById("oneYear").classList.remove("btn-euro-active");
  	document.getElementById("sixMonths").classList.remove("btn-euro-active");
  	document.getElementById("threeMonths").classList.remove("btn-euro-active");
  	document.getElementById("oneMonth").classList.remove("btn-euro-active");
  	
  	if (this.type == "euro") {
  		document.getElementById(scale).classList.add("btn-euro-active");
  	} else {
  		document.getElementById(scale).classList.add("btn-active");
  	}

  	this.updateChartData(scale);
  }

  updateChartType(type) {
  	document.getElementById("percentage").classList.remove("btn-active");
  	document.getElementById("euro").classList.remove("btn-euro-active");

  	if (type == "euro") {
  		document.getElementById(type).classList.add("btn-euro-active");
  	} else {
  		document.getElementById(type).classList.add("btn-active");
  	}

  	if (type != this.type) {
  		this.type = type;
  		this.updateTimeScale(this.scale);
  	}  	
  }
  
  constructor(private data: DataService) {
  } 

  updateChartData(scale) {
  	this.scale = scale;

  	$("canvas").remove();
	$(".chartBox").append('<canvas id="chart" height="120"></canvas>');

	if (this.type == "percentage") {
		this.buildPercentageChart();
	} else {
		this.buildEuroChart();
	}

  }

  buildPercentageChart() {
  	var path = "../assets/dataTestPercentage.json";
  	var type = this.type;
  	var scale = this.scale;

	$.getJSON(path, function (data) {
  		var labelsJSON = [];
  		var valuesJSON = [];

	    if (data.hasOwnProperty(scale)) {
    		labelsJSON = Object.keys(data[scale]);
    		valuesJSON = Object.values(data[scale]);
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

		                    var num = Math.round(Number(tooltipItem.yLabel)*100)/100;
		                    label += '' + num.toString();
		                    return label;
	                	}
	            	}
	        	}
	        }
		});

		if (scale == "fiveYears") {
	    	this.chart.options.scales.xAxes[0].time.unit='month';

	    } else if (scale == "oneMonth") {
	    	this.chart.options.scales.xAxes[0].time.unit='day';

	    }

	    this.chart.update();
    });
  }

  buildEuroChart() {
  	var path = null;
  	var scale = this.scale;

  	if (scale == "fiveYears") {
  		path = "getFundEntries5Years";
  	} else if (this.scale == "oneYear") {
  		path = "getFundEntries1Year";
  	} else if (this.scale == "sixMonths") {
  		path = "getFundEntries6Months";
  	} else if (this.scale == "threeMonths") {
  		path = "getFundEntries3Months";
  	} else if (this.scale == "oneMonth") {
  		path = "getFundEntries1Month";
  	}

  	fetch('http://localhost:4202/' + path, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/plain-text',
      },
      mode: 'cors'
    })
    .then((response) => response.json())
    .then((response) => {
        console.log(response);

        var labelsJSON = [];
  		var valuesJSON = [];

        var canvas = <HTMLCanvasElement> document.getElementById("chart");
		var ctx = canvas.getContext("2d");

		response.forEach( function(output) {
        	labelsJSON.push(output.date);
	        valuesJSON.push(output.value);    
        });

		this.chart = new Chart(ctx, {
		    type: 'line',
		    data: {
		        labels: labelsJSON,
		        datasets: [{
		            label: 'Fund total value',
		            data: valuesJSON,
		            borderWidth: 1,
		            backgroundColor: '#E9F0C3',
			        borderColor: '#A7B846',
			        pointBackgroundColor: '#687328',
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
			            min: 0
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

		                    var num = Math.round(Number(tooltipItem.yLabel)*100)/100;
		                    label += '' + num.toString();
		                    return label;
	                	}
	            	}
	        	}
	        }
		});

		if (scale == "fiveYears") {
	    	this.chart.options.scales.xAxes[0].time.unit='month';
	    } else if (scale == "oneMonth") {
	        this.chart.options.scales.xAxes[0].time.unit='day';
	    }
  

    	this.chart.update();
        
    });
  }

}
