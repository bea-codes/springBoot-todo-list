package br.com.dantebeatriz.todolist.task;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ITaskRepository extends JpaRepository<Task, UUID>{
	public List<Task> findByUserId(UUID userId);
	
}
