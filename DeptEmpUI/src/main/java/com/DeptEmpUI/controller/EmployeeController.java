package com.DeptEmpUI.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.DeptEmpUI.model.Department;
import com.DeptEmpUI.model.Employee;

@RestController
public class EmployeeController {
	@Autowired
	private RestTemplate restTemplate;
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/newEmp")
	public ModelAndView showFormForAdd(HttpServletRequest request) {
		
		String Register  = "NewForm";
		HttpSession session1 = request.getSession();
		
		List<Employee> lst =(List<Employee>)session1.getAttribute("empLst");
		List<Department> deptlst = (List<Department>) session1.getAttribute("deptList");
		 
		//int did = lst.get(0).getDeptId();
		ModelAndView model = new ModelAndView("form");
		PagedListHolder<Employee> pagedListHolder = new PagedListHolder<Employee>(lst);
		pagedListHolder.setPageSize(3);

		model.addObject("maxPages", pagedListHolder.getPageCount());
		Integer page =  (Integer) session1.getAttribute("page");
		if (page == null || page < 1 || page > pagedListHolder.getPageCount())
			page = 1;

		model.addObject("page", page);
		if (page == null || page < 1 || page > pagedListHolder.getPageCount()) {
			pagedListHolder.setPage(0);
			model.addObject("empLst", pagedListHolder.getPageList());
		} else if (page <= pagedListHolder.getPageCount()) {
			pagedListHolder.setPage(page - 1);
			model.addObject("empLst", pagedListHolder.getPageList());
		}
		model.addObject("deptLst", deptlst);
		//model.addObject("empLst", lst);
		model.addObject("Register", Register);
		model.addObject("addEmp", "regEmp");
		model.addObject("home", "homemp");
		return model;	
	}
	
	@PostMapping(value = "/saveEmp")
	public ModelAndView saveEmployee( HttpServletRequest request,@ModelAttribute Employee employee, HttpServletResponse response) {
		//String deptName =request.getParameter("deptName");
		//List<Department> lst = departmentService.getAllDepartments();
		int deptId =  Integer.parseInt(request.getParameter("deptId"));
		Employee employee1 = new Employee();
		employee1.setEmpId(employee.getEmpId());
		employee1.setEmpName(employee.getEmpName());
		employee1.setAge(employee.getAge());
		employee1.setDeptId(employee.getDeptId());
		
		
		restTemplate.postForObject("http://gateway-service/department/addEmp", employee, Employee.class);

		return new ModelAndView("redirect:/listEmp?deptId="+deptId);
	}


	@SuppressWarnings("unchecked")
	@GetMapping(value = "/editEmp")
	public ModelAndView editEmployee(HttpServletRequest request) {
		int employeeId = Integer.parseInt(request.getParameter("empId"));
		int did =  Integer.parseInt(request.getParameter("deptId"));
		HttpSession session2 = request.getSession();
		List<Employee> lst =(List<Employee>) session2.getAttribute("empLst");
		session2.setAttribute("empLst", lst);
		ModelAndView model = new ModelAndView("form");
		model.addObject("home", "homemp");
		model.addObject("empLst", lst);
		model.addObject("employeeid", employeeId);
		model.addObject("deptId", did);
		return model;
	}

	@PostMapping(value = "/updateEmp")
	public ModelAndView updateEmployee(HttpServletRequest request, @ModelAttribute Employee employee, HttpServletResponse response) {
		
		int employeeId = Integer.parseInt(request.getParameter("empId"));
		int deptId =  Integer.parseInt(request.getParameter("deptId"));
		
		  Employee employee1 = new Employee();
		  employee1.setEmpId(employee.getEmpId());
		  employee1.setEmpName(employee.getEmpName());
		  employee1.setAge(employee.getAge());
		  employee1.setDeptId(employee.getDeptId());
		 
		  restTemplate.put("http://gateway-service/department/updateEmp/"+employeeId, employee);

		return new ModelAndView("redirect:/listEmp?deptId="+deptId);

	}

	@GetMapping(value = "/deleteEmp")
	public ModelAndView deleteEmployee(HttpServletRequest request) {
		int employeeId = Integer.parseInt(request.getParameter("empId"));
		int deptId = Integer.parseInt(request.getParameter("deptId"));
		restTemplate.delete("http://gateway-service/department/deleteEmp/"+employeeId);
		return new ModelAndView("redirect:/listEmp?deptId="+deptId);
	}


}
