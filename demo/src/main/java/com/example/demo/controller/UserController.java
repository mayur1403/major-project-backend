

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
public class UserController {
	@Autowired
	SendEmailService sendEmailService;
	Configuration con;
	ServiceRegistry sr;
	SessionFactory sf;
	Session session;
	Transaction tx;
	
	public UserController() {
		con = new Configuration().configure().addAnnotatedClass(User.class).addAnnotatedClass(Application.class)
				.addAnnotatedClass(Recruiter.class).addAnnotatedClass(Job.class);
		sr = new ServiceRegistryBuilder().applySettings(con.getProperties()).buildServiceRegistry();
		sf = con.buildSessionFactory(sr);
		session = sf.openSession();
		tx = session.beginTransaction();
		tx.commit();
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping(path = "/UserSignUpRequest")
	public String UserSignUpRequest(@RequestBody User user) {
		System.out.println(user);
		session = sf.openSession();
		tx = session.beginTransaction();
		Query query = session.createQuery("from User where emailAddress=:i");
		query.setParameter("i", user.getEmailAddress());
		if(query.uniqueResult()==null)
		{
			String otp=sendEmailService.sendMail(user.getEmailAddress());
			return otp;
		}
		else
		{
			return "email id already exists";
		}
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping(path = "/UserSignUp")
	public void UserSignUp(@RequestBody User user) {
		session = sf.openSession();
		tx = session.beginTransaction();
		session.save(user);
		tx.commit();
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping(path = "/LoginUser/{email}/{password}")
	public User LoginUser(@PathVariable(name="email") String email,@PathVariable(name="password") String password) {
		System.out.print(email+" "+password);
		session = sf.openSession();
		tx = session.beginTransaction();
		Query query = session.createQuery("from User where emailAddress=:i and password=:j");
		query.setParameter("i",email);
		query.setParameter("j",password);
		User user = (User) query.uniqueResult();
		System.out.print(user);
		if(user==null)
			return null;
		else
			return user;
	}
	@CrossOrigin(origins = "http://localhost:3000")
	@PutMapping(path = "/editUser", consumes = "application/json")
	public User editUser(@RequestBody User user) {
		session = sf.openSession();
		tx = session.beginTransaction();
		Query query = session.createQuery("from User where userId=:i");
		query.setParameter("i",user.getUserId());
		User user2 = (User) query.uniqueResult();
		session.delete(user2);
		session.save(user);
		tx.commit();
		return user;
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/getUser/{id}")
	public User getUser(@PathVariable(name = "id") Integer id) {
		session = sf.openSession();
		Query query = session.createQuery("from User where userId=:i");
		query.setParameter("i", id);
		User user = (User) query.uniqueResult();
		return user;
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@DeleteMapping("/deleteUser/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id) {
		session = sf.openSession();
		tx = session.beginTransaction();
		Query query = session.createQuery("from User where userId=:i");
		query.setParameter("i", id);
		User user = (User) query.uniqueResult();
		session.delete(user);
		tx.commit();
		return "deleted";
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/getUsers")
	public List<User> getUsers() {
		session = sf.openSession();

		Query query = session.createQuery("from User");
		List<User> users = query.list();
		return users;
	}

}


