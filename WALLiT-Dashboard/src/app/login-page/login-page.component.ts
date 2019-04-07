import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { DataService } from "../data.service";
import * as $ from 'jquery';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {

  user = "John Doe";
  pass = "12345";
  message:string;

  ngOnInit() {
  	this.data.currentMessage.subscribe(message => this.message = message)
  }

  checkLogin() {
  	if ((<HTMLInputElement>document.getElementById("user")).value == this.user 
  		&& (<HTMLInputElement>document.getElementById("pass")).value == this.pass) {
  		this.router.navigateByUrl("/personal");
  		this.data.changeMessage(this.user);
  	} else {
		  alert("Sorry! The credentials are incorrect.");
  	}  	
  }

  constructor(private router: Router, private data: DataService) {}
}
