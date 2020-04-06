
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.Application;
import com.example.demo.dao.Job;
import com.example.demo.dao.Recruiter;
import com.example.demo.dao.User;
import com.example.demo.service.SendEmailService;

@RestController
public class RecruiterController {
	
	@Autowired
	SendEmailService sendEmailService;
	Configuration con;
	ServiceRegistry sr;
	SessionFactory sf;
	Session session;
	Transaction tx;
	
	public RecruiterController() {
		con = new Configuration().configure().addAnnotatedClass(Recruiter.class)
		.addAnnotatedClass(Job.class).addAnnotatedClass(Application.class).addAnnotatedClass(User.class);
		sr = new ServiceRegistryBuilder().applySettings(con.getProperties()).buildServiceRegistry();
		sf = con.buildSessionFactory(sr);
		session = sf.openSession();
		tx = session.beginTransaction();
		tx.commit();
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping(path = "/RecruiterSignUpRequest", consumes = "application/json")
	public String RecruiterSignUpRequest(@RequestBody Recruiter recruiter) {
		session = sf.openSession();
		tx = session.beginTransaction();
		Query query = session.createQuery("from Recruiter where emailAddress=:i");
		query.setParameter("i", recruiter.getEmailAddress());
		if(query.uniqueResult()==null)
		{
			String otp=sendEmailService.sendMail(recruiter.getEmailAddress());
			return otp;
		}
		else
		{
			return "email id already exists";
		}
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping(path = "/RecruiterSignUp", consumes = "application/json")
	public void RecruiterSignUp(@RequestBody Recruiter recruiter) {
		session = sf.openSession();
		tx = session.beginTransaction();
		session.save(recruiter);
		tx.commit();
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping(path = "/LoginRecruiter/{email}/{password}")
	public Recruiter LoginRecruiter(@PathVariable(name="email") String email,@PathVariable(name="password") String password) {
		System.out.println(email+" "+password);
		session = sf.openSession();
		tx = session.beginTransaction();
		Query query = session.createQuery("from Recruiter where emailAddress=:i and password=:j");
		query.setParameter("i",email);
		query.setParameter("j",password);
		Recruiter recruiter = (Recruiter) query.uniqueResult();
		System.out.print(recruiter);
		if(recruiter==null)
			return null;
		else
			return recruiter;
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PutMapping(path = "/editRecruiter", consumes = "application/json")
	public Recruiter editRecruiter(@RequestBody Recruiter recruiter) {
		session = sf.openSession();
		tx = session.beginTransaction();
		Query query = session.createQuery("from Recruiter where recruiterId=:i");
		query.setParameter("i",recruiter.getRecruiterId());
		Recruiter recruiter2 = (Recruiter) query.uniqueResult();
		session.delete(recruiter2);
		session.save(recruiter);
		tx.commit();
		return recruiter;
	}

	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/getRecruiter/{id}")
	public Recruiter getRecruiter(@PathVariable(name = "id") Integer id) {
		session = sf.openSession();
		Query query = session.createQuery("from Recruiter where recruiterId=:i");
		query.setParameter("i", id);
		Recruiter recruiter = (Recruiter) query.uniqueResult();
		return recruiter;
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@DeleteMapping("/deleteRecruiter/{id}")
	public String deleteRecruiter(@PathVariable(name = "id") Integer id) {
		session = sf.openSession();
		tx = session.beginTransaction();
		Query query = session.createQuery("from Recruiter where recruiterId=:i");
		query.setParameter("i", id);
		Recruiter recruiter = (Recruiter) query.uniqueResult();
		session.delete(recruiter);
		tx.commit();
		return "deleted";
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/getRecruiters")
	public List<Recruiter> getRecruiters() {
		session = sf.openSession();

		Query query = session.createQuery("from Recruiter");
		List<Recruiter> recruiters = query.list();
		return recruiters;
	}

}

