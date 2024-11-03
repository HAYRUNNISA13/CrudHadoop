package com.example.demo.Model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
public class Employee {
    @Id
    private Long empno;
    private String ename;
    private String job;
    private Long mgr;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date hiredate;
    private Double sal;
    private Double comm;


    @ManyToOne
    @JoinColumn(name = "deptno", nullable = false) // Using deptno as the foreign key
    private Department department;

    private String imgName;

    public Long getEmpno() { return empno; }
    public void setEmpno(Long empno) { this.empno = empno; }

    public String getEname() { return ename; }
    public void setEname(String ename) { this.ename = ename; }

    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }

    public Long getMgr() { return mgr; }
    public void setMgr(Long mgr) { this.mgr = mgr; }

    public Date getHiredate() { return hiredate; }
    public void setHiredate(Date hiredate) { this.hiredate = hiredate; }

    public Double getSal() { return sal; }
    public void setSal(Double sal) { this.sal = sal; }

    public Double getComm() { return comm; }
    public void setComm(Double comm) { this.comm = comm; }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
}
