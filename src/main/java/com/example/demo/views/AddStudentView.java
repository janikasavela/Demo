package com.example.demo.views;

import java.util.List;

import com.example.demo.model.Status;
import com.example.demo.model.Student;
import com.example.demo.services.StatusService;
import com.example.demo.services.StudentService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Add Students")
@Route(value = "add-student")
public class AddStudentView extends VerticalLayout {
    
    private final StatusService statusService;
    private final StudentService studentService;
    
    private TextField age;
    private TextField zipCode;
    private TextField name;
    private TextField country;
    private ComboBox<Status> status;
    private LogoLayout image;
    
    private Button save;
    private Button close;
    
    private Student student;
    private Binder<Student> binder;

    public AddStudentView(StatusService statusService, StudentService studentService) {
        
        this.studentService = studentService;
        this.statusService = statusService;
        
        setAlignItems(Alignment.CENTER);
        createVariables();
        createStatus();
        createBinder();
        
        add(image);
        add(createFormLayout());
    }

    private void createBinder() {
        student = new Student();
        binder = new BeanValidationBinder<>(Student.class);
        
        // Bind instance fields (this should bind fields in the form to the binder)
        binder.bindInstanceFields(this);
    }

    private void createStatus() {
        List<Status> statusItems = statusService.findAll();
        status.setItems(statusItems);
        status.setValue(statusItems.get(0)); // Set default value to the first item
        status.setItemLabelGenerator(Status::getName); // Set how items are displayed in ComboBox
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        
        // Add the fields and buttons to the layout
        formLayout.add(name, age, country, zipCode, status, createButtons());
        
        // Set column span for image and name field
        formLayout.setColspan(image, 2);
        formLayout.setColspan(name, 2);
        
        return formLayout;
    }

    private Component createButtons() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        
        // Add click listeners for the buttons
        close.addClickListener(e -> closeView());
        save.addClickListener(e -> saveStudent());
        
        return new HorizontalLayout(save, close);
    }

    private void saveStudent() {
        try {
            // Write form values to the student object
            binder.writeBean(student);
            // Save the student to the service
            studentService.save(student);
            // Clear the form and show a success notification
            clearFields();
            Notification notification = Notification.show("Student saved successfully!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setPosition(Position.TOP_CENTER);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        student = new Student(); // Reset student object
        status.setValue(statusService.findAll().get(0)); // Reset status ComboBox to default
        binder.getFields().forEach(HasValue::clear); // Clear all form fields
    }

    private void closeView() {
        // Navigate to the main view or close the form
        getUI().ifPresent(ui -> ui.navigate(""));
    }

    private void createVariables() {
        age = new TextField("Age");
        name = new TextField("Name");
        country = new TextField("Country");
        zipCode = new TextField("Zip Code");
        status = new ComboBox<Status>("Status");
        image = new LogoLayout();
        save = new Button("Save");
        close = new Button("Cancel");
    }
}
