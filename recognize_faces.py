# # recognize_faces.py

# import face_recognition
# import os
# import sys
# import json

# # Constants
# KNOWN_DIR = "student-photos"  # ✅ This should be passed from Java ideally
# UNKNOWN_IMAGE = sys.argv[1]   # ✅ Full path to class photo
# TOLERANCE = 0.45              # You can adjust this for stricter/looser matching

# def load_known_faces():
#     known_encodings = []
#     roll_numbers = []
#     for filename in os.listdir(KNOWN_DIR):
#         if filename.lower().endswith(('.jpg', '.png')):
#             path = os.path.join(KNOWN_DIR, filename)
#             image = face_recognition.load_image_file(path)
#             encodings = face_recognition.face_encodings(image)
#             if encodings:
#                 known_encodings.append(encodings[0])
#                 roll_numbers.append(os.path.splitext(filename)[0])  # "18CS001.jpg" → "18CS001"
#     return known_encodings, roll_numbers

# def recognize_faces(known_encodings, roll_numbers):
#     image = face_recognition.load_image_file(UNKNOWN_IMAGE)
#     face_locations = face_recognition.face_locations(image)
#     face_encodings = face_recognition.face_encodings(image, face_locations)

#     present = set()

#     for encoding in face_encodings:
#         results = face_recognition.compare_faces(known_encodings, encoding, tolerance=TOLERANCE)
#         for match, roll in zip(results, roll_numbers):
#             if match:
#                 present.add(roll)

#     return list(present)

# if __name__ == "__main__":
#     known_encodings, roll_numbers = load_known_faces()
#     present_students = recognize_faces(known_encodings, roll_numbers)
#     print(json.dumps(present_students))  # Output roll numbers to be captured by Java

# recognize_faces.py
# import warnings
# warnings.filterwarnings("ignore")  # 👈 suppress UserWarnings
# import face_recognition
# import os
# import sys
# import json

# TOLERANCE = 0.45

# if len(sys.argv) != 3:
#     #print("Usage: python recognize_faces.py <class_photo_path> <student_photo_dir>")
#     sys.exit(1)

# UNKNOWN_IMAGE = sys.argv[1]
# KNOWN_DIR = sys.argv[2]

# def load_known_faces():
#     known_encodings = []
#     roll_numbers = []
#     for filename in os.listdir(KNOWN_DIR):
#         if filename.lower().endswith(('.jpg', '.png')):
#             path = os.path.join(KNOWN_DIR, filename)
#             image = face_recognition.load_image_file(path)
#             encodings = face_recognition.face_encodings(image)
#             if encodings:
#                 known_encodings.append(encodings[0])
#                 roll_numbers.append(os.path.splitext(filename)[0])
#     return known_encodings, roll_numbers

# def recognize_faces(known_encodings, roll_numbers):
#     image = face_recognition.load_image_file(UNKNOWN_IMAGE)
#     face_locations = face_recognition.face_locations(image)
#     face_encodings = face_recognition.face_encodings(image, face_locations)

#     present = set()

#     for encoding in face_encodings:
#         results = face_recognition.compare_faces(known_encodings, encoding, tolerance=TOLERANCE)
#         for match, roll in zip(results, roll_numbers):
#             if match:
#                 present.add(roll)

#     return list(present)

# known_encodings, roll_numbers = load_known_faces()
# present_students = recognize_faces(known_encodings, roll_numbers)
# print(json.dumps(present_students))  # Output: ["21CS001", "21CS004"]

#new one
import warnings
warnings.filterwarnings("ignore")  # Suppress warnings

import face_recognition
import os
import sys
import json

TOLERANCE = 0.6

if len(sys.argv) != 3:
    sys.exit(1)  # Exit if incorrect number of arguments

UNKNOWN_IMAGE = sys.argv[1]
KNOWN_DIR = sys.argv[2]

def load_known_faces():
    known_encodings = []
    roll_numbers = []
    for filename in os.listdir(KNOWN_DIR):
        if filename.lower().endswith(('.jpg', '.png')):
            path = os.path.join(KNOWN_DIR, filename)
            image = face_recognition.load_image_file(path)
            encodings = face_recognition.face_encodings(image)
            if encodings:
                # Extract roll number correctly even if UUID is prefixed
                base_name = os.path.splitext(filename)[0]  # e.g. "uuid_4PS22CS070"
                if '_' in base_name:
                    roll_number = base_name.split('_')[-1]  # Take the part after last _
                else:
                    roll_number = base_name
                known_encodings.append(encodings[0])
                roll_numbers.append(roll_number)
    return known_encodings, roll_numbers

def recognize_faces(known_encodings, roll_numbers):
    image = face_recognition.load_image_file(UNKNOWN_IMAGE)
    face_locations = face_recognition.face_locations(image)
    face_encodings = face_recognition.face_encodings(image, face_locations)

    present = set()
    for encoding in face_encodings:
        results = face_recognition.compare_faces(known_encodings, encoding, tolerance=TOLERANCE)
        for match, roll in zip(results, roll_numbers):
            if match:
                present.add(roll)

    return list(present)

known_encodings, roll_numbers = load_known_faces()
present_students = recognize_faces(known_encodings, roll_numbers)
print(json.dumps(present_students))

# import warnings
# warnings.filterwarnings("ignore")

# import face_recognition
# import os
# import sys
# import json

# TOLERANCE = 0.6  # You can make this stricter (0.4) or looser (0.6)
# MODEL = "hog"     # "cnn" is more accurate but slower (needs GPU support)

# if len(sys.argv) != 3:
#     sys.exit(1)

# UNKNOWN_IMAGE = sys.argv[1]
# KNOWN_DIR = sys.argv[2]

# def load_known_faces():
#     known_encodings = []
#     roll_numbers = []
#     for filename in os.listdir(KNOWN_DIR):
#         if filename.lower().endswith(('.jpg', '.jpeg', '.png')):
#             path = os.path.join(KNOWN_DIR, filename)
#             image = face_recognition.load_image_file(path)
#             encodings = face_recognition.face_encodings(image)
#             if encodings:
#                 known_encodings.append(encodings[0])
#                 roll_numbers.append(os.path.splitext(filename)[0])
#                 print(f"✅ Loaded encoding for: {filename}")
#             else:
#                 print(f"⚠️ No face found in: {filename}")
#     return known_encodings, roll_numbers

# def recognize_faces(known_encodings, roll_numbers):
#     image = face_recognition.load_image_file(UNKNOWN_IMAGE)
#     face_locations = face_recognition.face_locations(image, model=MODEL)
#     face_encodings = face_recognition.face_encodings(image, face_locations)

#     present = set()

#     for i, face_encoding in enumerate(face_encodings):
#         distances = face_recognition.face_distance(known_encodings, face_encoding)
#         best_match_index = distances.argmin()
#         best_distance = distances[best_match_index]

#         print(f"\n🧠 Face {i + 1} distances:")
#     for roll, dist in zip(roll_numbers, distances):
#         print(f"  {roll}: {dist:.2f}")


#         if best_distance < TOLERANCE:
#             matched_roll = roll_numbers[best_match_index]
#             present.add(matched_roll)
#             print(f"✅ Match: {matched_roll} (distance: {best_distance:.2f})")
#         else:
#             print(f"❌ No match (best distance: {best_distance:.2f})")

#     return list(present)

# # --- MAIN ---
# known_encodings, roll_numbers = load_known_faces()
# present_students = recognize_faces(known_encodings, roll_numbers)
# print(json.dumps(present_students))  # final output for Java
