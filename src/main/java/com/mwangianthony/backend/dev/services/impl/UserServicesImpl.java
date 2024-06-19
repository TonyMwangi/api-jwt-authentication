package com.mwangianthony.backend.dev.services.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mwangianthony.backend.dev.repository.UserRepository;
import com.mwangianthony.backend.dev.services.UserServices;

@Service
public class UserServicesImpl implements UserServices {

	private UserRepository userRepository;

	@Override
	public UserDetailsService userDetailsServices() {
		
		return new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				return userRepository.findByEmail(username)
						.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
			}
			
		};
	}
	
}
