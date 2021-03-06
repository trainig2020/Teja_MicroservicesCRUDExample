package com.spring.Department_Service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.spring.Department_Service.model.Employee;
import com.spring.Department_Service.model.EmployeeList;



@Service
public class EmployeeService {
	@Autowired
	private RestTemplate restTemplate;
	
	
	public EmployeeList getAllEmployees(){
		return  restTemplate.getForObject("http://employee-service/emp", EmployeeList.class);
	}
	
	@HystrixCommand(fallbackMethod = "getFallbackEmp")
	public Employee getEmployeeById(int id) {
		return  restTemplate.getForObject("http://employee-service/listEmp/"+id, Employee.class);
	}
	
	public Employee getFallbackEmp(int id) {
		return new Employee(id, "No Employee available", 0, 0);
	}
	@HystrixCommand(fallbackMethod = "getFallbackAddEmp")
	public Employee insertEmployee(Employee employee) {
		return restTemplate.postForObject("http://employee-service/addEmp", employee, Employee.class);
	}
	
	public Employee getFallbackAddEmp( Employee employee) {
		return new Employee(0, "No Employee available", 0, 0);
	}
	
	public void updateEmployee(int id ,Employee employee) {
		 restTemplate.put("http://employee-service/updateEmp/"+id, employee);
	}
	
	public void deleteEmployee(int id) {
		restTemplate.delete("http://employee-service/deleteEmp/"+id);
	}
	
	public EmployeeList getAllEmployeesByDeptId(int deptId){
		return   restTemplate.getForObject("http://employee-service/empList/"+deptId, EmployeeList.class);
	}
}
