package com.spring.Employee_Service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.Employee_Service.model.Employee;
import com.spring.Employee_Service.model.EmployeeList;
import com.spring.Employee_Service.service.EmployeeService;

@RestController
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping("/listEmp")
	public List<Employee> getAllEmployees(){
		return employeeService.getAllEmployees();
	}
	
	@GetMapping("/emp")
	public EmployeeList getEmployees(){
		List<Employee> emp = employeeService.getAllEmployees();
		EmployeeList list = new EmployeeList();
		list.setEmployees(emp);
		return list;
		
	}
	
	
	@GetMapping("/listEmp/{id}")
	public Employee getEmployeeById(@PathVariable int id) {
		return employeeService.getEmployees(id);
	}
	
	@GetMapping("emp/{deptId}")
	public List<Employee> getEmployeeByDeptId(@PathVariable int deptId){
		return employeeService.getEmployeesByDept(deptId);
	}
	
	@GetMapping("empList/{deptId}")
	public EmployeeList getEmpByDeptId(@PathVariable int deptId) {
		List<Employee> emp = employeeService.getEmployeesByDept(deptId);
		EmployeeList list = new EmployeeList();
		list.setEmployees(emp);
		return list;
	}
	
	@PostMapping("/addEmp")
	public Employee addEmployee(@RequestBody Employee employee) {
		employeeService.insertEmployee(employee);
		return employee;
	}
	
	@PutMapping("/updateEmp/{id}")
	public Employee updateEmployee(@RequestBody Employee employee, @PathVariable int id) {
		employeeService.updateEmployee(id, employee);
		return employee;
	}
	
	@DeleteMapping("/deleteEmp/{id}")
	public String deleteEmployee(@PathVariable int id) {
		employeeService.deleteEmployee(id);
		return "Record Deleted";
		
	}

}
