package com.example.demo.services;

import java.util.List;

import com.example.demo.model.Status;

public interface StatusService {
	
	public void save(Status status);
	public List<Status> findAll();

}
