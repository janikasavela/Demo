package com.example.demo.views;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.model.Status;
import com.example.demo.model.Student;
import com.example.demo.services.StudentService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;

@PageTitle(value = "Home")
@Route(value = "")

public class MainView extends VerticalLayout {
	
	private final StudentService studentService;
	private LogoLayout logoLayout;
	private Grid<Student> grid;
	private TextField filterField;
	private Checkbox themeToggle;
	private static boolean isChecked;
	
	public MainView(StudentService studentService) {
		this.studentService = studentService;
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		
	
		
		createFieldVariables();
		configureGrid();
	
		
		add(logoLayout, createToolBar(), grid);
		
		loadStudents();
	}

	private Checkbox createToggle() {
		themeToggle = new Checkbox("Dark Mode");
		themeToggle.setValue(isChecked);
		themeToggle.addValueChangeListener(e -> {
			MainView.isChecked = !isChecked;
			setTheme(isChecked);
		});
		
		return themeToggle;
		
	}

	private void setTheme(boolean dark) {
	    var js = MessageFormat.format("""
	        document.documentElement.setAttribute("theme", "{0}")
	        """, dark ? Lumo.DARK : Lumo.LIGHT);

	    getElement().executeJs(js);
	}

	private Component createToolBar() {
		filterField.setPlaceholder("Filter by name...");
		filterField.setClearButtonVisible(true);
		filterField.setValueChangeMode(ValueChangeMode.LAZY);
		filterField.addValueChangeListener(e -> updateStudents());
		
		Button addStudentButton = new Button("Add Student");
		Button removeStudentButton = new Button("Remove Student");
		
		addStudentButton.addClickListener(e -> 
		getUI().ifPresent(ui -> ui.navigate("add-student")));
		
		return new HorizontalLayout(filterField, addStudentButton, removeStudentButton, createToggle());
	}

	private void updateStudents() {
		grid.setItems(studentService.find(filterField.getValue()));
	}

	private void configureGrid() {
		grid.setSizeFull();
		grid.setColumns("country", "zipCode");
		grid.addColumn(s -> s.getName()).setHeader("Name");
		grid.addColumn(s -> s.getAge()).setHeader("Age");
		
	
		grid.addComponentColumn(s -> {
			Icon icon;
			
			if(s.getStatus().getName().equals("ACTIVE")) {
				icon = VaadinIcon.CIRCLE.create();
				icon.setColor("green");
			} else if (s.getStatus().getName().equals("PASSIVE")) {
				icon = VaadinIcon.CLOSE_CIRCLE.create();
				icon.setColor("red");
			} else {
				icon = VaadinIcon.CHECK_CIRCLE.create();
				icon.setColor("orange");
			}
			
			return icon;
			
		}).setHeader("Status");
	
		grid.getColumns().forEach(col -> col.setAutoWidth(true));
	}

	private void loadStudents() {
		// List<Student> students = new ArrayList<>();
		// students.add(new Student("Janika", 23, 1627, "UK", new Status("ACTIVE")));
		// students.add(new Student("Jenna", 33, 1627, "FI", new Status("PASSIVE")));
	    // students.add(new Student("Matias", 24, 1627, "UK", new Status("ACTIVE")));
		// students.add(new Student("Teppo", 29, 1627, "UK", new Status("ABSOLVED")));
		// grid.setItems(students);
		
		grid.setItems(studentService.findAll());
	}

	private void createFieldVariables() {
		this.logoLayout = new LogoLayout();
		this.grid = new Grid<>(Student.class);
		this.filterField = new TextField();
	}
}
