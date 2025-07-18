package com.example.attendance.service;

import com.example.attendance.model.Faculty;
import com.example.attendance.repository.FacultyRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomFacultyDetailsService implements UserDetailsService {

    private final FacultyRepository facultyRepository;

    public CustomFacultyDetailsService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Faculty faculty = facultyRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Faculty not found"));
        return User.builder()
            .username(faculty.getEmail())
            .password(faculty.getPassword())
            .roles("FACULTY")
            .build();
    }
}
