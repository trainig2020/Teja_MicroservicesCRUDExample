package com.DeptEmpUI.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Department {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int deptId;
	//@Size(min=2, max=20,message="Please give valid name")
	private String deptName;
	

	public Department() {
		super();
	}

	public Department(int deptId, String deptName) {
		super();
		this.deptId = deptId;
		this.deptName = deptName;
	}

	public Department(int deptId) {
		super();
		this.deptId = deptId;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	

	@Override
	public String toString() {
		return "Department [deptId=" + deptId + ", deptName=" + deptName + "]";
	}


}
