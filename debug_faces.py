import warnings
warnings.filterwarnings("ignore", category=UserWarning)

import face_recognition
import os
import sys
import json
import cv2

TOLERANCE = 0.6

if len(sys.argv) != 4:
    print("Usage: python debug_faces.py <group_photo> <student_photo_dir> <output_image>")
    sys.exit(1)

GROUP_PHOTO = sys.argv[1]
STUDENT_DIR = sys.argv[2]
OUTPUT_IMAGE = sys.argv[3]

# Load known faces
known_encodings = []
roll_numbers = []

for filename in os.listdir(STUDENT_DIR):
    if filename.lower().endswith(('.jpg', '.png', '.jpeg')):
        image_path = os.path.join(STUDENT_DIR, filename)
        image = face_recognition.load_image_file(image_path)
        encodings = face_recognition.face_encodings(image)
        if encodings:
            known_encodings.append(encodings[0])
            roll_numbers.append(os.path.splitext(filename)[0])

# Load group photo
group_image = face_recognition.load_image_file(GROUP_PHOTO)
face_locations = face_recognition.face_locations(group_image)
face_encodings = face_recognition.face_encodings(group_image, face_locations)

# For drawing
output_image = cv2.cvtColor(group_image, cv2.COLOR_RGB2BGR)

for (top, right, bottom, left), face_encoding in zip(face_locations, face_encodings):
    distances = face_recognition.face_distance(known_encodings, face_encoding)
    best_match_index = distances.argmin() if len(distances) > 0 else None
    best_distance = distances[best_match_index] if best_match_index is not None else None

    if best_match_index is not None and distances[best_match_index] <= TOLERANCE:
        name = roll_numbers[best_match_index]
        label = f"✅ {name} ({best_distance:.2f})"
        print(f"✅ Match: {name} (distance: {best_distance:.2f})")
    else:
        name = "❌ Unknown"
        label = f"{name} (min dist: {best_distance:.2f})"
        print(f"❌ No match (best distance: {best_distance:.2f})")

    # Draw bounding box
    cv2.rectangle(output_image, (left, top), (right, bottom), (0, 255, 0), 2)
    cv2.putText(output_image, label, (left, top - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.6, (255, 0, 0), 2)

# Save debug image
cv2.imwrite(OUTPUT_IMAGE, output_image)
print(f"✅ Debug image saved to {OUTPUT_IMAGE}")
