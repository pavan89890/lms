package com.pavan.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.pavan.model.RoleBO;
import com.pavan.model.UserBO;
import com.pavan.respository.UserRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserBO currentUser = userRepository.findByUsername(username);
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		if (currentUser != null) {
			for (RoleBO role : currentUser.getRoles()) {
				grantedAuthorities.add(new SimpleGrantedAuthority(role.getRolename()));
			}
			return new org.springframework.security.core.userdetails.User(currentUser.getUsername(),
					currentUser.getPassword(), grantedAuthorities);
		} else {
			return null;
		}
	}

}
