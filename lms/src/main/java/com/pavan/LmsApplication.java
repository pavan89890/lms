package com.pavan;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.pavan.model.RoleBO;
import com.pavan.model.UserBO;
import com.pavan.respository.RoleRepository;
import com.pavan.respository.UserRepository;

@SpringBootApplication
public class LmsApplication extends SpringBootServletInitializer{

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(LmsApplication.class,
		                      args);
	}
	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(LmsApplication.class);
	}

	@Bean
	InitializingBean sendDatabase() {
		return (
		) -> {
			if (roleRepository.findByRolename("ROLE_ADMIN") == null) {
				RoleBO role1 = new RoleBO();
				role1.setRolename("ROLE_ADMIN");
				roleRepository.save(role1);
			}
			if (roleRepository.findByRolename("ROLE_USER") == null) {
				RoleBO role2 = new RoleBO();
				role2.setRolename("ROLE_USER");
				roleRepository.save(role2);
			}

			if (userRepository.findByUsername("admin") == null) {
				UserBO user = new UserBO();
				user.setName("admin");
				user.setEmail("admin@gmail.com");
				user.setUsername("admin");
				user.setPassword(new BCryptPasswordEncoder().encode("java"));
				user.setEnabled(true);
				List<RoleBO> roles = new ArrayList<RoleBO>();
				roles.add(roleRepository.findByRolename("ROLE_ADMIN"));
				user.setRoles(roles);
				userRepository.save(user);
			}
		};
	}
}
