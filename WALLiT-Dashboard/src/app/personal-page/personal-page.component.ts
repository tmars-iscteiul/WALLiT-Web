import { Component, OnInit } from '@angular/core';
import { Chart } from 'chart.js';
import { Color, BaseChartDirective, Label } from 'ng2-charts';
import * as $ from 'jquery';

@Component({
  selector: 'app-personal-page',
  templateUrl: './personal-page.component.html',
  styleUrls: ['./personal-page.component.css']
})
export class PersonalPageComponent implements OnInit {

  type = null;
  chart = null;

  constructor() { 
  	
  }

  ngOnInit() {
	this.fetchInformationFromUser();
  }

  fetchInformationFromUser() {
	fetch('http://localhost:4202/getMovementEntriesFromUser', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/plain-text',
      },
      mode: 'cors'
    })
    .then((response) => response.json())
    .then((response) => {
    	var labelsJSON = [];
  		var valuesJSON = [];

    	response.forEach( function(output) {
        	labelsJSON.push(output.date);
	        valuesJSON.push(output.balance);
        });

        this.updateChart(labelsJSON, valuesJSON);
	})
	  	
  }

  clearFilter() {
  	document.forms["initialDateForm"].elements.namedItem("startDate").value = "";
  	document.forms["endDateForm"].elements.namedItem("endDate").value = "";

  	this.filter("All");
  }

  filter(type) {
  	document.getElementById("all").classList.remove("btn-active");
  	document.getElementById("deposit").classList.remove("btn-active");
  	document.getElementById("withdraw").classList.remove("btn-active");

  	if (type == "") {
  		type = this.type;
  	}

  	if (type == "All") {
  		document.getElementById("all").classList.add("btn-active");
  	} else if (type == "Deposit") {
  		document.getElementById("deposit").classList.add("btn-active");
  	} else if (type == "Withdraw") {
  		document.getElementById("withdraw").classList.add("btn-active");
	}

  	this.type = type;

    var table, tr, tdType, tdDate, i;
	table = document.getElementById("movementsTable");
	tr = table.getElementsByTagName("tr");

	var startDateTxt, endDateTxt;
  	startDateTxt = document.forms["initialDateForm"].elements.namedItem("startDate").value;
  	endDateTxt = document.forms["endDateForm"].elements.namedItem("endDate").value;

  	if (startDateTxt == "") {
  		startDateTxt = "1500-01-01";
  	}

  	if (endDateTxt == "") {
  		endDateTxt = "2199-12-31";
  	}

  	var startDate = this.stringToDate(startDateTxt);
  	var endDate = this.stringToDate(endDateTxt);

  	var diff = endDate.getTime() - startDate.getTime();

  	if (diff < 0) {
  		endDate = startDate;
  		document.forms["endDateForm"].elements.namedItem("endDate").value = startDateTxt;
  		alert("End date cannot be previous to start date.");
  	}
	  	 
	for (i = 0; i < tr.length; i++) {
	  	tdDate = tr[i].getElementsByTagName("td")[0];
	    tdType = tr[i].getElementsByTagName("td")[1];
	    if (tdDate && tdType) {
	      if (tdType.textContent == type || type == "All") {
	      	if (this.stringToDate(tdDate.textContent).getTime() > startDate.getTime() && this.stringToDate(tdDate.textContent).getTime() < endDate.getTime()) {
	          tr[i].style.display = "";
	        } else {
	          tr[i].style.display = "none";
	        }
	      } else {
	      	tr[i].style.display = "none";
	      }
	    } 
	}

  }

  stringToDate(str) {
  	var date = str.split("-"),
        y = date[0],
        m = date[1],
        d = date[2],
        temp = [];
    temp.push(y,m,d);
    return new Date(temp.join("-"));
  }

  updateChart(labelsJSON, valuesJSON) {
  	var path = null;
  	var scale = 'sixMonths';

  	var labelsJSON = labelsJSON;
  	var valuesJSON = valuesJSON;
	
    var canvas = <HTMLCanvasElement> document.getElementById("chart");
	var ctx = canvas.getContext("2d");

	var chart = new Chart(ctx, {
	    type: 'line',
	    data: {
	        labels: labelsJSON,
	        datasets: [{
	            label: 'Balance',
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
    
  }
}
