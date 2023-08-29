package com.team.pretLancer_7.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.team.pretLancer_7.domain.Member;
import com.team.pretLancer_7.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("member")
public class memberController {
	
	@Autowired
	MemberService service;
	
	// 회원가입 폼으로 이동
	@GetMapping("join")
	public String join() {
		log.debug("조인 컨트롤러 들어오니?");
		return "memberForm/join";
	}
	
	// 회원가입 기능
	@PostMapping("join")
	public String joinMember(Member m) {
		log.error("회원가입 {}",m);
		service.insertMember(m);
		return "redirect:/";
	}
	
	// 로그인 폼으로 이동
	@GetMapping("login")
	public String login() {
		
		return "memberForm/login";
	}
	
	// ID중복확인 폼
	@GetMapping("idcheck")
	public String idcheck() {
		
		return "memberForm/idcheck";
	}
		
	// ID중복 확인 처리
	@PostMapping("idcheck")
	public String idcheck(String searchid, Model m) {
		// ID 검색결과가 null이면 true, 아니면 false를 리턴
		log.debug("searchid : {}", searchid);
		boolean result = service.idcheck(searchid);
			
		m.addAttribute("searchid", searchid);
		m.addAttribute("result", result);
			
		return "memberForm/idcheck";
	}
}
