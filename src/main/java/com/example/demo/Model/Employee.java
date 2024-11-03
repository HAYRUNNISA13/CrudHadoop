package com.example.demo.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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
    private Long deptno;


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

    public Long getDeptno() { return deptno; }
    public void setDeptno(Long deptno) { this.deptno = deptno; }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
}
