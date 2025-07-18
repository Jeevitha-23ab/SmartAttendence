// package com.example.attendance.repository;

// import com.example.attendance.model.Student;
// import org.springframework.data.jpa.repository.JpaRepository;

// public interface StudentRepository extends JpaRepository<Student, Long> {
// }
package com.example.attendance.repository;

import com.example.attendance.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    // 👇 Add this line if it’s not already there
    boolean existsByRollNumber(String rollNumber);
    
    // Optional: fetch by roll number
    Student findByRollNumber(String rollNumber);
}
