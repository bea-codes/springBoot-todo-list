package br.com.dantebeatriz.todolist.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api")
public class MinhaPrimeiraController {
	
	@GetMapping(value = "/helloWorld", produces = "text/plain")
	public ResponseEntity<String> getReturn(){
		return ResponseEntity.status(HttpStatus.OK).body("Hello World");
	}

	@GetMapping("/string")
	public String justString(){
		return "This is just a string!";
	}
}
