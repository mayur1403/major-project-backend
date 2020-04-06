
package com.example.demo.controller;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.Application;
import com.example.demo.dao.Job;
import com.example.demo.dao.Recruiter;
import com.example.demo.dao.User;

@RestController
public class JobController {
	Configuration con;
	ServiceRegistry sr;
	SessionFactory sf;
	Session session;
	Transaction tx;
	
	public JobController() {
		con = new Configuration().configure().addAnnotatedClass(Job.class).addAnnotatedClass(Recruiter.class).addAnnotatedClass(Application.class).addAnnotatedClass(User.class);
		sr = new ServiceRegistryBuilder().applySettings(con.getProperties()).buildServiceRegistry();
		sf = con.buildSessionFactory(sr);
		session = sf.openSession();
		tx = session.beginTransaction();
		tx.commit();
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping(path = "/PostJob")
	public Job addJob(@RequestBody Job job) {
		System.out.println(job.toString());
		session = sf.openSession();
		tx = session.beginTransaction();
		session.save(job);
		tx.commit();
		return job;
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PutMapping(path = "/editJob", consumes = "application/json")
	public Job editJob(@RequestBody Job job) {
		session = sf.openSession();
		tx = session.beginTransaction();
		Query query = session.createQuery("from Job where jobId=:i");
		query.setParameter("i",job.getJobId());
		Job job2 = (Job) query.uniqueResult();
		session.delete(job2);
		session.save(job);
		tx.commit();
		return job;
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/getJob/{id}")
	public Job getJob(@PathVariable(name = "id") Integer id) {
		session = sf.openSession();
		Query query = session.createQuery("from Job where jobId=:i");
		query.setParameter("i", id);
		Job job = (Job) query.uniqueResult();
		return job;
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@DeleteMapping("/deleteJob/{id}")
	public String deleteJob(@PathVariable(name = "id") Integer id) {
		session = sf.openSession();
		tx = session.beginTransaction();
		Query query = session.createQuery("from Application where job.jobId=:i");
		query.setParameter("i", id);
		List<Application> applications = query.list();
		for(int i=0;i<applications.size();i++)
		session.delete(applications.get(i));
		session.createQuery("from Job where jobId=:i");
		query.setParameter("i", id);
		Job job = (Job) query.uniqueResult();
		session.delete(job);
		tx.commit();
		return "deleted";
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/getJobs")
	public List<Job> getJobs() {
		session = sf.openSession();

		Query query = session.createQuery("from Job");
		List<Job> jobs = query.list();
		return jobs;
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping(path = "/getJobsPostedByRecruiter/{id}")
	public List<Job> getJobsPostedByRecruiter(@PathVariable(name = "id") Integer id) {
		session = sf.openSession();
		Query query = session.createQuery("from Job where recruiter.recruiterId=:i");
		query.setParameter("i", id);
		List<Job> jobs = query.list();
		return jobs;
	}

}


