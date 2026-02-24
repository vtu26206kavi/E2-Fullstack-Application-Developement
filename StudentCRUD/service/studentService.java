package com.example.demo.StudentCRUD.service;
import com.example.demo.StudentCRUD.model.Student;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class studentService {
    private List<Student> students = new ArrayList<>(
            Arrays.asList(
                    new Student(1, "kavi", "AI"),
                    new Student(2, "shree", "ML")
            )
    );
    public  List<Student> getStudents() {
        return students;
    }
    public Student getStudentbyrno(int rollnumber) {

        int index=0;
        boolean isfound=false;
        for (int i=0;i<students.size();i++) {
            if(students.get(i).getRno() == rollnumber) {
                index=i;
            }
        }
        if(isfound){return students.get(index);}
        else{
            return new Student(0," ","");
        }
    }
    public void addStudent(Student student) {
        students.add(student);
    }
    public String updateStudent(Student student) {
        int index=0;
        boolean isfound=false;
        for(int i=0;i<students.size();i++){
            if(students.get(i).getRno() == student.getRno()){
                index=i;
                isfound=true;
                break;
            }
        }
        if(!isfound) {
            return "no such student exist";
        }else{
            students.set(index, student);
            return "updation is done....";
        }


    }

    public String deleteStudent(int rno) {
        int index =0;
        boolean isvalue=false;
        for(int i=0;i<students.size();i++){
            if(students.get(i).getRno()==rno) {
                index = i;
                isvalue=true;
            }
        }
        if(!isvalue){
            return "no such element found";
        }else{
            students.remove(index);
            return "sucessfully removed element";
        }
    }
}