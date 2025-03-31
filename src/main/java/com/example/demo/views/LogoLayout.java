package com.example.demo.views;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class LogoLayout extends HorizontalLayout {
	
	private Image image;
	
	public LogoLayout() {
		
		image = new Image("images/4758019_39046.jpg", "My Image");
		image.setWidth("100px");
		image.setHeight("auto"); 
		
		setJustifyContentMode(JustifyContentMode.CENTER);
		
		add(image);
	}

}
