package com.team.pretLancer_7.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.team.pretLancer_7.domain.Exam;
import com.team.pretLancer_7.domain.Member;
import com.team.pretLancer_7.service.ExamService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("translated")
public class ExamController {

	@Autowired
	ExamService service;
	
	@GetMapping("tutorial")
	public String tutorial (HttpSession session, @AuthenticationPrincipal UserDetails user, Exam ex, Model m) {
		ex.setMemberid(user.getUsername());
		Member member = service.getMemberOne(ex.getMemberid());
		ex.setLanguage(member.getMemberlang());
		log.error("ex 객체 {}", ex);
		Exam question = service.getQuestion(ex);
		log.error("question 객체 {}", question);
		log.error("member 객체 {}", member);
		m.addAttribute("question", question);
		m.addAttribute("member", member);
		String answer = (String) session.getAttribute("answer");
		m.addAttribute("answer", answer);
		
		if (member.getTutorial_num() >= 50 && member.getTutorial().equals("Y"))
			return "redirect:/translated/exam";
		else
			return "examForm/tutorial";
	}
	
	
	@PostMapping("tutorial")
	public String tutorialAnswer (HttpSession session, @AuthenticationPrincipal UserDetails user, Exam ex, Model m) {
		// Exam ex에 my_answer와 examnum의 값을 HTML에서 받음
		ex.setMemberid(user.getUsername());
		log.error("post로 가져오는 Exam 객체 {}", ex);
		int cnt = service.getAnswerTu(ex);
		if (cnt == 1) {
			session.setAttribute("answer", "correct");
		}
		else session.setAttribute("answer", "");

		// tutorial_num이 오른 횟수를 확인
		Member member = service.getMemberOne(ex.getMemberid());
		
		// tutorial_num이 50 이상일 때 tutorial의 값을 Y로 변경 
		if (member.getTutorial_num() >= 50) {
			service.tutorialCheck(ex.getMemberid());
		}
		
		m.addAttribute("member", member);
		if (member.getTutorial_num() >= 50 && member.getTutorial().equals("Y"))
			return "redirect:/translated/exam";
		else
			return "redirect:/translated/tutorial";
	}
	
	
	@GetMapping("exam")
	public String exam (@AuthenticationPrincipal UserDetails user, Exam ex, Model m) {
		ex.setMemberid(user.getUsername());
		Member member = service.getMemberOne(ex.getMemberid());
		ex.setLanguage(member.getMemberlang());
		Exam question = service.getQuestion(ex);
		m.addAttribute("question", question);
		m.addAttribute("member", member);
		return "examForm/exam";
	}
	
	
	
	@PostMapping("exam")
	public String examAnswer (HttpSession session, @AuthenticationPrincipal UserDetails user, Exam ex, Model m) {
		ex.setMemberid(user.getUsername());
		log.error("post로 가져오는 Exam 객체 {}", ex);
		int cnt = service.getAnswerEx(ex);
		if (cnt == 1) {
			session.setAttribute("answer", "correct");
		}
		else session.setAttribute("answer", "");

		return "redirect:/translated/exam";
	}
	
	
	/*
	@ResponseBody
	@PostMapping("answer_check")
	public String answer_check(int examnum, String memberid) {
		Exam ex = new Exam();
		ex.setMemberid(memberid);
		ex.setExamnum(examnum - 1);
		
		int cnt = service.getExamInfo(ex);
		if (cnt == 1) {
			return "맞았습니다!";
		} else
			return "틀렸습니다.";
	}
	*/
}
