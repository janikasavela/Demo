package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Status;
import com.example.demo.repository.StatusRepository;

@Service
public class StatusServiceImpl implements StatusService {
	
	@Autowired
	private StatusRepository statusRepository;

	@Override
	public void save(Status status) {
		statusRepository.save(status);
		
	}

	@Override
	public List<Status> findAll() {
		return statusRepository.findAll();
	}

}
