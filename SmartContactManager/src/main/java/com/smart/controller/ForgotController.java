package com.smart.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotController {
	Random random=new Random(1000);
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@RequestMapping("/forgot")
	public String openEmailFrom()
	{
		return "forgot_email_from";
	}
	
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email,HttpSession session)
	{
		System.out.println("EMAIL "+email);
	
		int otp=random.nextInt(999999);
		System.out.println("OTP "+otp);
		
		String subject="OTP From SCM";
		String message=""+
		"<div style='border:1px solid #e2e2e2; padding:20px'>"+
				"<h1>"+
		"OTP is "+"<b>"+otp+"</n>"+"</h1>"+
				"</div>";
		String to=email;
		boolean flag=this.emailService.sendEmail(message, subject, to);
		if(flag)
		{
			session.setAttribute("myotp",otp);
			session.setAttribute("email",email);
			return "varify_otp";
			
		}
		else {
			session.setAttribute("message", "Check your email id !!");
			return "forgot_email_form";
	}}
	
	
	
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp,HttpSession session)
	{
		int myotp=(int)session.getAttribute("myotp");
		String email=(String)session.getAttribute("email");
		if(myotp==otp)
		{
			User user=this.userRepository.getUserByUserName(email);
			if(user==null)
			{
				session.setAttribute("message", "User does not exists with this email !!");
				return "forgot_email_from";
			}
			else
			{
				
			}
			return "password_change_form";
		}
		else
		{
			session.setAttribute("message", "You have entered wrong otp !!");
			return "verify_otp";
		}
	}
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newpassword") String newpassword,HttpSession session)
	{
		String email=(String)session.getAttribute("email");
		User user=this.userRepository.getUserByUserName(email);
		user.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
		this.userRepository.save(user);
		return "redirect:/signin?change=password changed successfully..";
	}
}
