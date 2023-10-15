package br.com.dantebeatriz.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface IUserRepository extends JpaRepository<User, UUID>{
	User findByUsername(String username);
}
