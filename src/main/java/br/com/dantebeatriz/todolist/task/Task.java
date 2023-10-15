package br.com.dantebeatriz.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import br.com.dantebeatriz.todolist.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "tb_tasks")
@AllArgsConstructor
@NoArgsConstructor
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	private UUID userId;
	private String description;

	@Column(length = 50) // tamanho máximo de caracteres que essa propriedade pode ter
	private String title;
	private String priority; // prioridade por ser um ENUM... talvez
	private LocalDateTime startAt;
	private LocalDateTime finishAt;

	@CreationTimestamp
	private LocalDateTime createdAt;

	public void setTitle(String title) throws Exception{
		if(title.length() > 50){
			throw new Exception("Title deve conter no máximo 50 caracteres");
		}
		this.title = title;
	}

}
