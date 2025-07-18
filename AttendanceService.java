// package com.example.attendance.service;

// import com.example.attendance.model.Attendance;
// import com.example.attendance.model.Student;
// import com.example.attendance.repository.AttendanceRepository;
// import com.example.attendance.repository.StudentRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.time.LocalDate;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// @Service
// public class AttendanceService {

//     @Autowired
//     private AttendanceRepository attendanceRepository;

//     @Autowired
//     private StudentRepository studentRepository;

    
//     public void markAttendance(List<String> presentRollNumbers, LocalDate date) {
//         List<Student> allStudents = studentRepository.findAll();
//         for (Student student : allStudents) {
            // boolean isPresent = presentRollNumbers.contains(student.getRollNumber());
            // Attendance attendance = new Attendance();
            // attendance.setStudent(student);
            // attendance.setDate(date);
            // attendance.setPresent(isPresent);
            // attendanceRepository.save(attendance);
            // Attendance attendance = new Attendance();
            // attendance.setStudent(student);
            // attendance.setDate(date);
            //attendance.setPresent(presentRollNumbers.contains(student.getRollNumber()));
            // attendance.setPresent(isPresent);
            // attendanceRepository.save(attendance);

            // Update counters
    //         student.setTotalClasses(student.getTotalClasses() + 1);
    //         if (isPresent) {
    //             student.setClassesAttended(student.getClassesAttended() + 1);
    //         }

    //         studentRepository.save(student);
    //     }
    // }
//     @Transactional
// public void markAttendance(List<String> presentRolls, LocalDate date) {
//     List<Student> allStudents = studentRepository.findAll();

//     for (Student student : allStudents) {
//         // Increment total sessions
//         student.setTotalSessions(student.getTotalSessions() + 1);

//         boolean isPresent = presentRolls.contains(student.getRollNumber());

//         if (isPresent) {
//             student.setDaysPresent(student.getDaysPresent() + 1);
//         }

//         // Save attendance record
//         Attendance attendance = new Attendance();
//         attendance.setStudent(student);
//         attendance.setDate(date);
//         attendance.setPresent(isPresent);
//         attendanceRepository.save(attendance);

//         studentRepository.save(student);
//     }

//     public Map<String, Double> calculateAttendancePercentage() {
//         Map<String, Double> result = new HashMap<>();
//         List<Student> students = studentRepository.findAll();
//         for (Student student : students) {
//             List<Attendance> records = attendanceRepository.findByStudent(student);
//             long totalDays = records.size();
//             long presentDays = records.stream().filter(Attendance::isPresent).count();
//             double percentage = totalDays == 0 ? 0.0 : (presentDays * 100.0) / totalDays;
//             result.put(student.getRollNumber(), percentage);
//         }
//         return result;
//     }
// }

package com.example.attendance.service;

import com.example.attendance.model.Attendance;
import com.example.attendance.model.Student;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    public void markAttendance(List<String> presentRollNumbers, LocalDate date) {
        List<Student> allStudents = studentRepository.findAll();

        for (Student student : allStudents) {
            // ✅ Correctly define the variable here
            boolean isPresent = presentRollNumbers.contains(student.getRollNumber());

            // ✅ Save attendance record
            Attendance attendance = new Attendance();
            attendance.setStudent(student);
            attendance.setDate(date);
            attendance.setPresent(isPresent);
            attendanceRepository.save(attendance);

            // ✅ Update attendance stats
            student.setTotalClasses(student.getTotalClasses() + 1);
            if (isPresent) {
                student.setClassesAttended(student.getClassesAttended() + 1);
            }

            studentRepository.save(student); // save changes
        }
    }
}
