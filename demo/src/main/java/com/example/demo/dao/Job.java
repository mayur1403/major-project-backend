
package com.example.demo.dao;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
@Entity
public class Job {
	@Id
	@GeneratedValue
	@Column(name="job_id")
	private Integer jobId;
	@ManyToOne
	private Recruiter recruiter;
	@Column(name="job_name")
	private String jobName;
	@Lob
	@Column(name="description")
	private String description;
	@Column(name="company")
	private String companyName;
	@Column(name="sector")
	private String sector;
	public Integer getJobId() {
		return jobId;
	}
	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}
	public Recruiter getRecruiter() {
		return recruiter;
	}
	public void setRecruiter(Recruiter recruiter) {
		this.recruiter = recruiter;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	@Override
	public String toString() {
		return "Job [jobId=" + jobId + ", recruiter=" + recruiter + ", jobName="
				+ jobName + ", description=" + description + ", companyName=" + companyName + ", sector=" + sector
				+ "]";
	}
	
}
