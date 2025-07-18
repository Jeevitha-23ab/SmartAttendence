// package com.example.attendance.controller;

// import com.example.attendance.model.Student;
// import com.example.attendance.repository.StudentRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.util.StringUtils;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;

// import java.io.File;
// import java.io.IOException;

// @Controller
// @RequestMapping("/students")
// public class StudentController {

//     @Autowired
//     private StudentRepository studentRepository;

//     @GetMapping("/form")
//     public String showStudentForm() {
//         return "student_form";
//     }

//     @PostMapping("/add")
//     public String addStudent(@RequestParam("name") String name,
//                              @RequestParam("rollNumber") String rollNumber,
//                              @RequestParam("photo") MultipartFile photo,
//                              Model model) {
//         try {
//             // Save photo to "student_photos/" directory
//             String uploadDir = "student_photos/";
//             File dir = new File(uploadDir);
//             if (!dir.exists()) dir.mkdirs();

//             String fileName = StringUtils.cleanPath(photo.getOriginalFilename());
//             File savedFile = new File(uploadDir + fileName);
//             photo.transferTo(savedFile);

//             // Save student data to DB
//             Student student = new Student();
//             student.setName(name);
//             student.setRollNumber(rollNumber);
//             // You can also store the photo file path if needed in DB (optional)
//             studentRepository.save(student);

//             model.addAttribute("message", "✅ Student added successfully!");
//         } catch (IOException e) {
//             model.addAttribute("error", "❌ Failed to upload photo: " + e.getMessage());
//         }
//         return "student_form";
//     }
// }


package com.example.attendance.controller;

import com.example.attendance.model.Student;
import com.example.attendance.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Controller
@RequestMapping("/students")
public class StudentController {

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/student_photos/";

    @Autowired
    private StudentRepository studentRepository;

    // Show the add student form
    @GetMapping("/form")
    public String showStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "student_form"; // Ensure student_form.html exists in /templates/
    }

    // Handle form submission
    @PostMapping("/add")
    public String addStudent(@ModelAttribute Student student,
                             @RequestParam("photo") MultipartFile photo,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        try {
            // Check for duplicate roll number
            if (studentRepository.existsByRollNumber(student.getRollNumber())) {
                model.addAttribute("error", "❌ Student with this roll number already exists.");
                return "student_form";
            }

            // Create the directory if not exists
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            // Use roll number as filename
            String extension = StringUtils.getFilenameExtension(photo.getOriginalFilename());
            String fileName = student.getRollNumber() + "." + (extension != null ? extension : "jpg");
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Set student fields
            student.setPhotoPath(fileName);
            student.setClassesAttended(0);
            student.setTotalClasses(0);

            // Save to DB
            studentRepository.save(student);

            redirectAttributes.addFlashAttribute("message", "✅ Student added successfully!");
            return "redirect:/students/form";

        } catch (IOException e) {
            model.addAttribute("error", "❌ Failed to upload photo: " + e.getMessage());
            return "student_form";
        } catch (Exception e) {
            model.addAttribute("error", "❌ Failed to add student: " + e.getMessage());
            return "student_form";
        }
    }
}
