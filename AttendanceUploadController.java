// package com.example.attendance.controller;

// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.core.userdetails.User;

// import org.springframework.util.StringUtils;

// import org.springframework.ui.Model;

// import com.example.attendance.model.Faculty;
// import com.example.attendance.model.Photo;
// import com.example.attendance.repository.FacultyRepository;
// import com.example.attendance.repository.PhotoRepository;
// import java.io.File;
// import java.io.IOException;
// import java.time.LocalDateTime;

// @Controller
// public class AttendanceUploadController {

//     @Autowired
// private FacultyRepository facultyRepository;

// @Autowired
// private PhotoRepository photoRepository;

//      @GetMapping("/upload")
//     public String showUploadPage() {
//         return "upload";
//     }

//     @PostMapping("/upload-photo")
//     public String uploadPhoto(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
//         try {
//             // File uploadDir = new File("uploads");
//             String uploadPath = System.getProperty("user.dir") + File.separator + "uploads";
//             File uploadDir = new File(uploadPath);

//             if (!uploadDir.exists()) uploadDir.mkdirs();

//             //File savedFile = new File(uploadDir, file.getOriginalFilename());
//             File savedFile = new File(uploadPath, file.getOriginalFilename());

//             file.transferTo(savedFile);

//             redirectAttributes.addFlashAttribute("message", "✅ Photo uploaded and attendance processing started.");
//             return "redirect:/dashboard";

//         } catch (IOException e) {
//             redirectAttributes.addFlashAttribute("error", "❌ Failed to upload: " + e.getMessage());
//             return "redirect:/upload";
//         }
//     }

//     @PostMapping("/upload")
// public String handlePhotoUpload(@RequestParam("photo") MultipartFile file,
//                                 @AuthenticationPrincipal User user,
//                                 Model model) throws IOException, InterruptedException {
//     String fileName = StringUtils.cleanPath(file.getOriginalFilename());

//     // Save to uploads/ directory
//     String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
//     File dir = new File(uploadDir);
//     if (!dir.exists()) dir.mkdirs();

//     File uploadedFile = new File(uploadDir + File.separator + fileName);
//     file.transferTo(uploadedFile);

//     // Save photo metadata in DB
//     Faculty faculty = facultyRepository.findByEmail(user.getUsername()).orElseThrow();
//     Photo photo = new Photo();
//     photo.setFileName(fileName);
//     photo.setUploadTime(LocalDateTime.now());
//     photo.setUploadedBy(faculty);
//     photoRepository.save(photo); // ✅ Save to database

//     // Optional: Call Python for face recognition (if already integrated)
//     // ...

//     model.addAttribute("message", "✅ Photo uploaded and saved to DB.");
//     return "upload";
// }

//     @GetMapping("/dashboard/upload")
//     public String showDashboard() {
//         return "dashboard"; // Make sure dashboard.html exists
//     }
// }

// package com.example.attendance.controller;

// import com.example.attendance.model.Faculty;
// import com.example.attendance.model.Photo;
// import com.example.attendance.repository.FacultyRepository;
// import com.example.attendance.repository.PhotoRepository;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.stereotype.Controller;
// //import org.springframework.ui.Model;
// import org.springframework.util.StringUtils;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// import java.io.File;
// import java.io.IOException;
// import java.time.LocalDateTime;

// @Controller
// public class AttendanceUploadController {

//     @Autowired
//     private FacultyRepository facultyRepository;

//     @Autowired
//     private PhotoRepository photoRepository;

//     // ✅ Show the upload form
//     @GetMapping("/upload")
//     public String showUploadPage() {
//         return "upload"; // Looks for upload.html in /templates
//     }

//     // ✅ Handle form submission and save file + DB record
//     @PostMapping("/upload-photo")
// public String handlePhotoUpload(@RequestParam("file") MultipartFile file,
//                                 @AuthenticationPrincipal User user,
//                                 RedirectAttributes redirectAttributes) {
//     try {
//         String fileName = StringUtils.cleanPath(file.getOriginalFilename());

//         // Save to /uploads directory
//         String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
//         File dir = new File(uploadDir);
//         if (!dir.exists()) dir.mkdirs();

//         File uploadedFile = new File(uploadDir + File.separator + fileName);
//         file.transferTo(uploadedFile);

//         // Save metadata in DB
//         Faculty faculty = facultyRepository.findByEmail(user.getUsername())
//                 .orElseThrow(() -> new RuntimeException("Faculty not found"));

//         Photo photo = new Photo();
//         photo.setFileName(fileName);
//         photo.setUploadTime(LocalDateTime.now());
//         photo.setUploadedBy(faculty);
//         photoRepository.save(photo);

//         // (Optional) Call Python face recognition script here

//         redirectAttributes.addFlashAttribute("message", "✅ Photo uploaded and attendance processing started.");

//         // 🔁 Redirect to dashboard after success
//         return "redirect:/dashboard";

//     } catch (IOException e) {
//         redirectAttributes.addFlashAttribute("error", "❌ Failed to upload photo: " + e.getMessage());
//         return "redirect:/upload";
//     }
// }

//     // (Optional) Redirect to dashboard
//     // @GetMapping("/dashboard")
//     // public String showDashboard() {
//     //     return "dashboard"; // Only needed if you have dashboard.html
//     // }
// }
// package com.example.attendance.controller;

// import com.example.attendance.model.Faculty;
// import com.example.attendance.model.Photo;
// import com.example.attendance.repository.FacultyRepository;
// import com.example.attendance.repository.PhotoRepository;
// import com.example.attendance.service.AttendanceService;
// import com.fasterxml.jackson.core.type.TypeReference;
// import com.fasterxml.jackson.databind.ObjectMapper;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.stereotype.Controller;
// import org.springframework.util.StringUtils;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// import java.io.*;
// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.util.List;

// @Controller
// public class AttendanceUploadController {

//     @Autowired
//     private FacultyRepository facultyRepository;

//     @Autowired
//     private PhotoRepository photoRepository;

//     @Autowired
//     private AttendanceService attendanceService;

//     // ✅ Show upload form
//     @GetMapping("/upload")
//     public String showUploadPage() {
//         return "upload"; // Returns upload.html
//     }

//     // ✅ Handle class photo upload and face recognition
//     @PostMapping("/upload-photo")
//     public String handlePhotoUpload(@RequestParam("file") MultipartFile file,
//                                     @AuthenticationPrincipal User user,
//                                     RedirectAttributes redirectAttributes) {
//         try {
//             // 🧠 Extract file name
//             String fileName = StringUtils.cleanPath(file.getOriginalFilename());

//             // 📁 Save to class_photos directory
//             String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "class_photos";
//             File dir = new File(uploadDir);
//             if (!dir.exists()) dir.mkdirs();

//             File uploadedFile = new File(uploadDir, fileName);
//             file.transferTo(uploadedFile);

//             // 🧑 Save metadata in DB
//             Faculty faculty = facultyRepository.findByEmail(user.getUsername())
//                     .orElseThrow(() -> new RuntimeException("Faculty not found"));

//             Photo photo = new Photo();
//             photo.setFileName(fileName);
//             photo.setUploadTime(LocalDateTime.now());
//             photo.setUploadedBy(faculty);
//             photoRepository.save(photo);

//             // 🧠 Call Python script for face recognition
//             //String scriptPath = "face_recognition/recognize_faces.py"; // Update if your script is elsewhere
//             String scriptPath = System.getProperty("user.dir") + File.separator + "face_recognition" + File.separator + "recognize_faces.py";
//             String classPhotoPath = uploadedFile.getAbsolutePath();
//             String studentPhotoDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "student_photos";

//             ProcessBuilder pb = new ProcessBuilder("python", scriptPath, classPhotoPath, studentPhotoDir);
//             pb.redirectErrorStream(true);
//             Process process = pb.start();

//             BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//             StringBuilder output = new StringBuilder();
//             String line;
//             while ((line = reader.readLine()) != null) {
//                 output.append(line);
//             }

//             int exitCode = process.waitFor();
//             if (exitCode != 0) {
//                 redirectAttributes.addFlashAttribute("error", "❌ Face recognition script failed.");
//                 return "redirect:/upload";
//             }

//             // 🧾 Parse roll numbers from JSON
//             ObjectMapper mapper = new ObjectMapper();
//             List<String> presentRollNumbers = mapper.readValue(output.toString(), new TypeReference<>() {});

//             // ✅ Mark attendance
//             attendanceService.markAttendance(presentRollNumbers, LocalDate.now());

//             redirectAttributes.addFlashAttribute("message", "✅ Attendance marked successfully!");
//             return "redirect:/dashboard";

//         } catch (Exception e) {
//             redirectAttributes.addFlashAttribute("error", "❌ Failed to process photo: " + e.getMessage());
//             return "redirect:/upload";
//         }
//     }
// }

// package com.example.attendance.controller;

// import com.example.attendance.model.Faculty;
// import com.example.attendance.model.Photo;
// import com.example.attendance.repository.FacultyRepository;
// import com.example.attendance.repository.PhotoRepository;
// import com.example.attendance.service.AttendanceService;
// import com.fasterxml.jackson.core.type.TypeReference;
// import com.fasterxml.jackson.databind.ObjectMapper;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.stereotype.Controller;
// import org.springframework.util.StringUtils;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// import java.io.*;
// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.util.List;

// @Controller
// public class AttendanceUploadController {

//     @Autowired
//     private FacultyRepository facultyRepository;

//     @Autowired
//     private PhotoRepository photoRepository;

//     @Autowired
//     private AttendanceService attendanceService;

//     @GetMapping("/upload")
//     public String showUploadPage() {
//         return "upload"; // Thymeleaf template name
//     }

//     @PostMapping("/upload-photo")
//     public String handlePhotoUpload(@RequestParam("file") MultipartFile file,
//             @AuthenticationPrincipal User user,
//             RedirectAttributes redirectAttributes) {
//         try {
//             // Extract file name
//             String fileName = StringUtils.cleanPath(file.getOriginalFilename());

//             // Define directories
//             String rootDir = System.getProperty("user.dir");
//             String classPhotoDir = rootDir + File.separator + "uploads" + File.separator + "class_photos";
//             String studentPhotoDir = rootDir + File.separator + "uploads" + File.separator + "student_photos";
//             //String scriptPath = rootDir + File.separator + "face_recognition" + File.separator + "recognize_faces.py";
//             String scriptPath = System.getProperty("user.dir") + File.separator + "face_recognition" + File.separator + "recognize_faces.py";


//             // Save class photo
//             File uploadDir = new File(classPhotoDir);
//             if (!uploadDir.exists())
//                 uploadDir.mkdirs();

//             File uploadedFile = new File(classPhotoDir, fileName);
//             file.transferTo(uploadedFile);

//             // Save upload info in DB
//             Faculty faculty = facultyRepository.findByEmail(user.getUsername())
//                     .orElseThrow(() -> new RuntimeException("Faculty not found"));

//             Photo photo = new Photo();
//             photo.setFileName(fileName);
//             photo.setUploadTime(LocalDateTime.now());
//             photo.setUploadedBy(faculty);
//             photoRepository.save(photo);

//             // Run Python face recognition script
//             ProcessBuilder pb = new ProcessBuilder("python", scriptPath, uploadedFile.getAbsolutePath(),
//                     studentPhotoDir);
//             pb.redirectErrorStream(true); // Combine stderr and stdout
//             Process process = pb.start();

//             // Read output
//             BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//             StringBuilder output = new StringBuilder();
//             String line;
//             while ((line = reader.readLine()) != null) {
//                 output.append(line);
//             }

//             int exitCode = process.waitFor();
//             if (exitCode != 0) {
//                 redirectAttributes.addFlashAttribute("error", "❌ Face recognition script failed.");
//                 return "redirect:/upload";
//             }

//             // Parse JSON roll numbers from script output
//             // ObjectMapper mapper = new ObjectMapper();
//             // List<String> presentRollNumbers = mapper.readValue(output.toString(), new
//             // TypeReference<>() {});
//             System.out.println("🔍 Script Output: " + output); // <-- debug print

//             ObjectMapper mapper = new ObjectMapper();
//             List<String> presentRollNumbers = mapper.readValue(output.toString(), new TypeReference<>() {
//             });

//             // Mark attendance
//             attendanceService.markAttendance(presentRollNumbers, LocalDate.now());

//             redirectAttributes.addFlashAttribute("message", "✅ Attendance marked successfully!");
//             return "redirect:/dashboard";

//         } catch (Exception e) {
//             e.printStackTrace(); // Optional: log to console for dev
//             redirectAttributes.addFlashAttribute("error", "❌ Failed to process photo: " + e.getMessage());
//             return "redirect:/upload";
//         }
//     }
// }

package com.example.attendance.controller;

import com.example.attendance.model.Faculty;
import com.example.attendance.model.Photo;
import com.example.attendance.repository.FacultyRepository;
import com.example.attendance.repository.PhotoRepository;
import com.example.attendance.service.AttendanceService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class AttendanceUploadController {

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/upload")
    public String showUploadPage() {
        return "upload";
    }

    @PostMapping("/upload-photo")
    public String handlePhotoUpload(@RequestParam("file") MultipartFile file,
                                    @AuthenticationPrincipal User user,
                                    RedirectAttributes redirectAttributes) {
        try {
            // Step 1: Extract file name
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            // Step 2: Define paths
            String rootDir = System.getProperty("user.dir");
            String classPhotoDir = rootDir + File.separator + "uploads" + File.separator + "class_photos";
            String studentPhotoDir = rootDir + File.separator + "uploads" + File.separator + "student_photos";
            String scriptPath = rootDir + File.separator + "face_recognition" + File.separator + "recognize_faces.py";

            // Step 3: Save uploaded photo
            File uploadDir = new File(classPhotoDir);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            File uploadedFile = new File(uploadDir, fileName);
            file.transferTo(uploadedFile);

            // Step 4: Save metadata in DB
            Faculty faculty = facultyRepository.findByEmail(user.getUsername())
                    .orElseThrow(() -> new RuntimeException("Faculty not found"));

            Photo photo = new Photo();
            photo.setFileName(fileName);
            photo.setUploadTime(LocalDateTime.now());
            photo.setUploadedBy(faculty);
            photoRepository.save(photo);

            // Step 5: Run Python script
            ProcessBuilder pb = new ProcessBuilder("python", scriptPath,
                    uploadedFile.getAbsolutePath(), studentPhotoDir);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Step 6: Capture output from Python script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[PYTHON] " + line);  // Log for debug
                output.append(line.trim()); // remove trailing newline
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                redirectAttributes.addFlashAttribute("error", "❌ Face recognition script failed.");
                return "redirect:/upload";
            }

            // Step 7: Parse output (must be valid JSON like ["18", "17"])
            ObjectMapper mapper = new ObjectMapper();
            List<String> presentRollNumbers = mapper.readValue(output.toString(), new TypeReference<List<String>>() {});

            // Step 8: Mark attendance
            attendanceService.markAttendance(presentRollNumbers, LocalDate.now());

            redirectAttributes.addFlashAttribute("message", "✅ Attendance marked successfully!");
            return "redirect:/dashboard";

        } catch (Exception e) {
            e.printStackTrace();  // For server logs
            redirectAttributes.addFlashAttribute("error", "❌ Failed to process photo: " + e.getMessage());
            return "redirect:/upload";
        }
    }
}
