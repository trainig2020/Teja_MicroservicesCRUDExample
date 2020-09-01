package com.DeptEmpUI.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.DeptEmpUI.model.Department;
import com.DeptEmpUI.model.DepartmentList;
import com.DeptEmpUI.model.Employee;
import com.DeptEmpUI.model.EmployeeList;

@RestController

public class DeptController {
	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping("/")
	public ModelAndView loginPage() {
		return new ModelAndView("redirect:/listDept");

	}
	
	
	@GetMapping("/home")
	public ModelAndView listDeptId(HttpServletRequest request) {
		DepartmentList lst =  restTemplate.getForObject("http://department-service/department/dept", DepartmentList.class);
		int deptId = lst.getDepartments().get(0).getDeptId();
		return new ModelAndView("redirect:/listDeptName?deptId=" + deptId);

	}

	
	@GetMapping("/listDeptName")
	public ModelAndView listDeptName(HttpServletRequest request, ModelAndView modelAndView) {
		int id = Integer.parseInt(request.getParameter("deptId"));
		List<Department> lstDept = new ArrayList<Department>();
		
		
		DepartmentList lst =  restTemplate.getForObject("http://department-service/department/dept", DepartmentList.class);
		for(int i=0; i< lst.getDepartments().size();i++) {
			lstDept.add(lst.getDepartments().get(i));
		}
		EmployeeList empLst =  restTemplate.getForObject("http://department-service/department/emp/"+id, EmployeeList.class);
		List<Employee> listEmp = new ArrayList<Employee>();
		for(int i=0; i< empLst.getEmployees().size();i++) {
			listEmp.add(empLst.getEmployees().get(i));
		}

		
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute("empLst", listEmp);
		modelAndView.addObject("deptLst", lstDept);
		modelAndView.addObject("empLst", listEmp);
		modelAndView.addObject("home", "homemp");
		modelAndView.addObject("check", "checklist");
		modelAndView.setViewName("form");

		return modelAndView;

	}


	@GetMapping("/listDept")
	public ModelAndView listDepartment(HttpServletRequest request) {
		List<Department> lstDept = new ArrayList<Department>();
		
		DepartmentList lst =  restTemplate.getForObject("http://department-service/department/dept", DepartmentList.class);
		System.out.println("In list dept");
		for(int i=0; i< lst.getDepartments().size();i++) {
			lstDept.add(lst.getDepartments().get(i));
		}
		ModelAndView modelAndView = new ModelAndView("form");
		modelAndView.addObject("deptList", lstDept);

		HttpSession session = request.getSession();
		session.setAttribute("deptList", lstDept);
		return modelAndView;

	}
	
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/newDept")
	public ModelAndView newDepartment(ModelAndView model, HttpServletRequest request) {
		String Register = "NewFormDept";
		HttpSession session1 = request.getSession();
		List<Department> lst = (List<Department>) session1.getAttribute("deptList");
		session1.setAttribute("deptList", lst);
		model.addObject("Register", Register);
		model.addObject("insertDept", "newDept");
		model.setViewName("form");
		Department department = new Department();
		model.addObject("department", department);
		return model;
	}
	
	@PostMapping(value = "/saveDept")
	public ModelAndView saveDepartment1(@ModelAttribute Department department,HttpServletRequest request) {
		System.out.println("In before");
		ModelAndView model = new ModelAndView("form");
		Department department1 = new Department();
		department1.setDeptId(department.getDeptId());
		department1.setDeptName(department.getDeptName());
		restTemplate.postForObject("http://department-service/department/addDept", department1, Department.class);
		System.out.println("In save department");
		return new ModelAndView("redirect:/listDept");
		
		
	}

	
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/editDept")
	public ModelAndView editDepartment(HttpServletRequest request) {
		int deptId = Integer.parseInt(request.getParameter("id"));
		HttpSession session2 = request.getSession();
		Department department = restTemplate.getForObject("http://department-service/department/listDept/"+deptId, Department.class);
		session2.setAttribute("department", department);
		List<Department> lst = (List<Department>) session2.getAttribute("deptList");
		session2.setAttribute("deptList", lst);
		ModelAndView model = new ModelAndView("form");
		model.addObject("deptList", lst);
		model.addObject("departId", deptId);
		return model;
	}
	
	@PostMapping(value = "/updateDept")
	public ModelAndView updateEmployee(HttpServletRequest request, @ModelAttribute Department department) {
		int deptId =  Integer.parseInt(request.getParameter("deptId"));

		  Department department1 = new Department();
		  department1.setDeptId(department.getDeptId());
		  department1.setDeptName(department.getDeptName());
		 
		  restTemplate.put("http://department-service/department/updateDept/"+deptId, department1);

		return new ModelAndView("redirect:/listDept");

	}
	
	@GetMapping(value = "/deleteDept")
	public ModelAndView deleteDepartment(HttpServletRequest request) {
		int departId = Integer.parseInt(request.getParameter("id"));
		restTemplate.delete("http://department-service/department/deleteDept/"+departId);
		return new ModelAndView("redirect:/listDept");
	}




	 

}
