package br.com.dantebeatriz.todolist.user;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping(value = "/user")
public class UserController {
	
	@Autowired // A anotação Autowired indica para o Spring cuidar do ciclo de vida dessa obj, inicializano ele quando necessário.
	private IUserRepository userRepository;

	@PostMapping("/post")
	public ResponseEntity create(@RequestBody User user){
		var foundUser = this.userRepository.findByUsername(user.getUsername());

		if(foundUser != null){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe!");
		}

		var HashedPassword = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
		user.setPassword(HashedPassword);
		
		var createdUser = this.userRepository.save(user);
		return ResponseEntity.status(HttpStatus.OK).body(createdUser);
	}

	@GetMapping("/get")
	public ResponseEntity<List<User>> getUsers() {
		return ResponseEntity.status(HttpStatus.OK).body(this.userRepository.findAll());
	}
}
