
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
import com.example.demo.dao.Application;
import com.example.demo.dao.Recruiter;
import com.example.demo.dao.User;

@RestController
public class ApplicationsController {
	Configuration con;
	ServiceRegistry sr;
	SessionFactory sf;
	Session session;
	Transaction tx;
	
	public ApplicationsController() {
		con = new Configuration().configure().addAnnotatedClass(Application.class);
		con.addAnnotatedClass(User.class).addAnnotatedClass(Job.class).addAnnotatedClass(Recruiter.class);
		sr = new ServiceRegistryBuilder().applySettings(con.getProperties()).buildServiceRegistry();
		sf = con.buildSessionFactory(sr);
		session = sf.openSession();
		tx = session.beginTransaction();
		tx.commit();
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping(path = "/addApplication")
	public Application addApplication(@RequestBody Application application) {
		session = sf.openSession();
		tx = session.beginTransaction();
		session.save(application);
		tx.commit();
		return application;
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PutMapping(path = "/editApplication")
	public Application editApplication(@RequestBody Application application) {
		session = sf.openSession();
		tx = session.beginTransaction();
		Query query = session.createQuery("from Application where applicationId=:i");
		query.setParameter("i",application.getApplicationId());
		Application application2 = (Application) query.uniqueResult();
		session.delete(application2);
		session.save(application);
		tx.commit();
		return application;
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/getApplication/{id}")
	public Application getApplication(@PathVariable(name = "id") Integer id) {
		session = sf.openSession();
		Query query = session.createQuery("from Application where applicationId=:i");
		query.setParameter("i", id);
		Application application = (Application) query.uniqueResult();
		return application;
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@DeleteMapping("/deleteApplication/{id}")
	public String deleteApplication(@PathVariable(name = "id") Integer id) {
		session = sf.openSession();
		tx = session.beginTransaction();
		Query query = session.createQuery("from Application where applicationId=:i");
		query.setParameter("i", id);
		Application application = (Application) query.uniqueResult();
		session.delete(application);
		tx.commit();
		return "deleted";
	}

	@CrossOrigin(origins = "http:/localhost:3000")
	@GetMapping("/getApplications")
	public List<Application> getApplications() {
		session = sf.openSession();

		Query query = session.createQuery("from Application");
		List<Application> application = query.list();
		return application;
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/getApplicationsForRecruiterpostedJobs/{id}")
	public List<Application> getApplicationsForRecruiterpostedJobs(@PathVariable(name = "id") Integer id) {
		session = sf.openSession();
			Query query = session.createQuery("from Application where job.jobId=:i");
			query.setParameter("i", id);
			List<Application> application = query.list();
		return application;
	}
	
	@CrossOrigin(origins = "http://172.17.0.3:3000")
	@GetMapping(value ="/getUsersForApplication/{id}")
	public User getUsersForApplication(@PathVariable(name = "id") Integer id) {
		session = sf.openSession();
			Query query = session.createQuery("from Application where applicationId=:i");
			query.setParameter("i", id);
			Application application =(Application)query.uniqueResult();
		return application.getUser();
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/getApplicationsAppliedByUser/{id}")
	public List<Application> getApplicationsAppliedByUser(@PathVariable(name = "id") Integer id) {
		session = sf.openSession();
			Query query = session.createQuery("from Application where user.userId=:i");
			query.setParameter("i", id);
			List<Application> applications =query.list();
		return applications;
	}
}


