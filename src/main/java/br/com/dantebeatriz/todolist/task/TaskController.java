package br.com.dantebeatriz.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.dantebeatriz.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

	@Autowired
	ITaskRepository taskRepository;
	
	@PostMapping("/create")
	public ResponseEntity createTask(@RequestBody Task task, HttpServletRequest request){
		var userId = request.getAttribute("userId");

		task.setUserId((UUID)userId);
		
		var currentDate = LocalDateTime.now();
		
		
		// Validação da data da task
		if(currentDate.isAfter(task.getStartAt()) || currentDate.isAfter(task.getFinishAt())){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início deve ser maior ou igual que a data atual");
		}

		if(task.getStartAt().isAfter(task.getFinishAt())){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início não pode vir depois da data de término");
		}
		
		var createdTask = this.taskRepository.save(task);
		return ResponseEntity.status(HttpStatus.OK).body(createdTask);
	}

	@GetMapping("/list")
	public ResponseEntity readTasks(HttpServletRequest request){
		var userId = request.getAttribute("userId");
		List<Task> userTasks = this.taskRepository.findByUserId((UUID) userId);
		
		return ResponseEntity.status(HttpStatus.OK).body(userTasks);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity updateTask(@RequestBody Task task, HttpServletRequest request, @PathVariable UUID id){
		var userId = request.getAttribute("userId"); 
		var existingTask = this.taskRepository.findById(id).orElse(null);

		if(task == null){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada");
		}

		if(!task.getUserId().equals(userId)){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O Usuário autenticado não possui acesso a essa tarefa");
		}
		
		Utils.copyNonNullProperties(task, existingTask);
		var updatedTask = this.taskRepository.save(existingTask);
		return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
	}
}
