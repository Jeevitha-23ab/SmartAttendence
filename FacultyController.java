package com.example.attendance.controller;

import com.example.attendance.model.Faculty;
import com.example.attendance.model.Photo;
import com.example.attendance.model.Student;
import com.example.attendance.repository.FacultyRepository;
import com.example.attendance.repository.PhotoRepository;
import com.example.attendance.repository.StudentRepository;
import com.example.attendance.service.AttendanceService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;

import java.io.IOException;
import java.time.LocalDateTime;
//import java.util.Map;

@Controller
public class FacultyController {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // ✅ FIXED: Use interface, not implementation

    // 🔐 Registration Page
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("faculty", new Faculty());
        return "register";
    }

    // 🔐 Handle Registration
    @PostMapping("/register")
    public String registerFaculty(@ModelAttribute Faculty faculty) {
        String hashedPassword = passwordEncoder.encode(faculty.getPassword());
        faculty.setPassword(hashedPassword);
        facultyRepository.save(faculty);
        return "redirect:/login";
    }

    // 📤 Upload Page
    @GetMapping("/faculty/upload")
    public String showUploadForm() {
        return "upload";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Maps to src/main/resources/templates/login.html
    }

    // 📊 Dashboard Page
    // @GetMapping("/dashboard")
    // public String showDashboard(Model model) {
    // Map<String, Double> percentages =
    // attendanceService.calculateAttendancePercentage();
    // model.addAttribute("attendanceSummary", percentages);
    // return "dashboard";
    // }
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<Student> students = studentRepository.findAll();

        model.addAttribute("students", students); // Thymeleaf will access attendancePercentage directly
        return "dashboard"; // dashboard.html
    }

    @GetMapping("/error")
    public String handleError(Model model) {
        model.addAttribute("errorMessage", "An unexpected error occurred.");
        return "error"; // Maps to error.html
    }

    @PostMapping("/faculty/upload-photo")
    public String handlePhotoUpload(@RequestParam("photo") MultipartFile file,
            @AuthenticationPrincipal User user,
            Model model) throws IOException, InterruptedException {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Save uploaded file
        String uploadDir = "uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists())
            dir.mkdirs();
        File uploadedFile = new File(uploadDir + fileName);
        file.transferTo(uploadedFile);

        // Save metadata
        Faculty faculty = facultyRepository.findByEmail(user.getUsername()).orElseThrow();
        Photo photo = new Photo();
        photo.setFileName(fileName);
        photo.setUploadTime(LocalDateTime.now());
        photo.setUploadedBy(faculty);
        photoRepository.save(photo);

        // 🔍 Run Python script
        String pythonScript = "face_recognition/recognize_faces.py";
        ProcessBuilder pb = new ProcessBuilder("python", pythonScript, uploadedFile.getAbsolutePath());
        pb.redirectErrorStream(true); // combine stderr and stdout
        Process process = pb.start();

        // 🔄 Read output (roll numbers in JSON)
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line);
        }
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            model.addAttribute("message", "Python script failed to run.");
            return "upload";
        }

        // 🧠 Convert JSON string to list of roll numbers
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> rollNumbers = objectMapper.readValue(output.toString(), new TypeReference<>() {
        });

        // ✅ Mark attendance
        attendanceService.markAttendance(rollNumbers, LocalDate.now());

        model.addAttribute("message", "Attendance updated successfully.");
        return "upload";
    }

}
