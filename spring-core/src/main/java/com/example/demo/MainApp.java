package com.example.demo;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {

    public static void main(String[] args) {

        BeanFactory factory =
                new AnnotationConfigApplicationContext(SpringCoreApplication.class);

        EmployeeService service =
                factory.getBean(EmployeeService.class);

        service.addEmployee(1, "Kavi");
        service.addEmployee(2, "Shree");

        service.displayEmployees();
    }
}
