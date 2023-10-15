package br.com.dantebeatriz.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.dantebeatriz.todolist.user.IUserRepository;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// é meio que um middleware... toda requisição vai passar por aqui. *É* um middleware.
// @Component
// public class FilterTaskAuth implements Filter{

// 	@Override
// 	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
// 			throws IOException, ServletException {
// 				System.out.println("Chegou no filtro");
// 				chain.doFilter(request, response);

// 	}
	
// }

//"@Component" é meio que um middleware... toda requisição vai passar por aqui. *É* um middleware.
@Component
public class FilterTaskAuth extends OncePerRequestFilter{
	
	@Autowired
	private IUserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
				

				var servletPath = request.getServletPath();
				if(servletPath.startsWith("/tasks/")){
					System.out.println(servletPath);
						// Pegar a autenticação (user & senha)
						var authorization = request.getHeader("Authorization");

						var authBase64 = authorization.substring("Basic".length()).trim();

						byte[] authDecoded = Base64.getDecoder().decode(authBase64);

						var authDecodedString = new String(authDecoded);

						String[] credentials = authDecodedString.split(":");
						String username = credentials[0];
						String password = credentials[1];
						System.out.println(username);
						System.out.println(password);
						// Validar usuário
						var user = this.userRepository.findByUsername(username);
						if(user == null){
							response.sendError(401, "usuário não encontrado");
						}else{
							//Validar senha
							var verifyPassword = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
							
							if(verifyPassword.verified){
								request.setAttribute("userId", user.getId());
								filterChain.doFilter(request, response);
							}else{
								response.sendError(401);
							}
						}
				}else{
					filterChain.doFilter(request, response);
				}
				
				
	}
}
