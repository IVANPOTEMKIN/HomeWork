SELECT s.name, s.age, f.name
FROM student s
FULL JOIN faculty f ON s.faculty_id = f.id

SELECT s.name
FROM student s
INNER JOIN avatar a ON s.id = a.student_id