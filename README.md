# AI-Powered Face Recognition-Based Attendance System

This project is an intelligent, contactless attendance system that uses **AI-based facial recognition** to identify and mark the presence of individuals in real-time. Designed for use in educational institutions or workplaces, it enhances security, reduces manual effort, and prevents proxy attendance.

---

## 🚀 Features

- Real-time face detection using webcam
- Automatic face encoding and matching
- Attendance logging with timestamp
- Tolerance-based face match accuracy
- Modular and scalable structure

---

## 🧠 Technologies Used

| Category         | Tools / Libraries                            |
|------------------|----------------------------------------------|
| Programming      | Python, Java (Maven)                         |
| AI / ML          | `face_recognition` (based on dlib + CNN)     |
| Computer Vision  | OpenCV (`cv2`)                                |
| Build Tool       | Maven (for Java modules)                     |
| Version Control  | Git                                          |

---

## 🗂️ Project Structure

attendance/
├── face_recognition/
│ ├── recognize_faces.py # Main recognition script
│ └── debug_faces.py # Debugging and image capture
├── .gitignore
├── pom.xml # Java Maven configuration (optional backend)
├── mvnw, mvnw.cmd # Maven wrapper scripts
└── HELP.md

yaml
Copy
Edit

---

## 🛠️ How to Run

### 1. Install Requirements (Python)
```bash
pip install face_recognition opencv-python
2. Register Faces
Place known face images in a folder (e.g., known_faces/), each image named with the person's name.

3. Run the Attendance Script
bash
Copy
Edit
python recognize_faces.py
4. Output
Attendance is marked in attendance.csv with timestamp.

Unrecognized faces can be logged or ignored based on settings.

⚙️ Configuration
You can set the tolerance level in recognize_faces.py to adjust match accuracy (default is 0.6).

Optional: Enable model='cnn' for more accurate face detection (requires GPU).

📌 Future Improvements
Add database or cloud storage integration

Integrate with mobile or web dashboard

Implement anti-spoofing and liveness detection

Generate attendance analytics reports
