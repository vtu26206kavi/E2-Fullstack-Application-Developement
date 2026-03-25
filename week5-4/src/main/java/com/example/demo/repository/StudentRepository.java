package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Student;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // ─────────────────────────────────────────────
    // 1. DERIVED QUERY METHODS (Spring generates SQL automatically from method name)
    // ─────────────────────────────────────────────

    // Find all students by department (case-insensitive)
    List<Student> findByDepartmentIgnoreCase(String department);

    // Find all students by age
    List<Student> findByAge(int age);

    // Find students older than a given age
    List<Student> findByAgeGreaterThan(int age);

    // Find students younger than a given age
    List<Student> findByAgeLessThan(int age);

    // Find students within an age range
    List<Student> findByAgeBetween(int minAge, int maxAge);

    // Find by name containing a keyword (LIKE %keyword%)
    List<Student> findByNameContainingIgnoreCase(String keyword);

    // Find by department AND age greater than
    List<Student> findByDepartmentIgnoreCaseAndAgeGreaterThan(String department, int age);

    // Find by email
    Optional<Student> findByEmail(String email);

    // Sort students by age ascending
    List<Student> findByDepartmentIgnoreCaseOrderByAgeAsc(String department);


    // ─────────────────────────────────────────────
    // 2. CUSTOM JPQL QUERIES using @Query
    // ─────────────────────────────────────────────

    // Find students by department using JPQL
    @Query("SELECT s FROM Student s WHERE s.department = :dept")
    List<Student> getStudentsByDepartment(@Param("dept") String department);

    // Find students above a certain age using JPQL
    @Query("SELECT s FROM Student s WHERE s.age > :minAge ORDER BY s.age ASC")
    List<Student> getStudentsOlderThan(@Param("minAge") int minAge);

    // Find students by department and age range using JPQL
    @Query("SELECT s FROM Student s WHERE s.department = :dept AND s.age BETWEEN :minAge AND :maxAge")
    List<Student> getStudentsByDeptAndAgeRange(
            @Param("dept") String department,
            @Param("minAge") int minAge,
            @Param("maxAge") int maxAge
    );

    // Count students per department
    @Query("SELECT s.department, COUNT(s) FROM Student s GROUP BY s.department")
    List<Object[]> countStudentsPerDepartment();

    // Average age per department
    @Query("SELECT s.department, AVG(s.age) FROM Student s GROUP BY s.department")
    List<Object[]> averageAgePerDepartment();


    // ─────────────────────────────────────────────
    // 3. NATIVE SQL QUERY (raw SQL)
    // ─────────────────────────────────────────────

    // Native SQL: find youngest student in a department
    @Query(value = "SELECT * FROM students WHERE department = :dept ORDER BY age ASC LIMIT 1",
           nativeQuery = true)
    Optional<Student> findYoungestInDepartment(@Param("dept") String department);
}