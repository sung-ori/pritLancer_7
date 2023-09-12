package com.team.pretLancer_7.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.team.pretLancer_7.domain.Request_M;
import com.team.pretLancer_7.domain.Request_S;
import com.team.pretLancer_7.service.RequestService;

@Controller
@RequestMapping("request")
public class RequestController {
	
	@Autowired
	RequestService service;

	@GetMapping("main")
	public String requestPage() {
		
		return "/requestForm/requestPage";
	}
	
	@PostMapping("insertRS")
	public String insertRequest_S(@AuthenticationPrincipal UserDetails user, Request_S r) {
		r.setMemberid(user.getUsername());
		r.setStartlang("JP");
		r.setEndlang("KR");
		service.insertRequest_S(r);
		return "redirect:/";
	}
	
	@GetMapping("cancelRS")
	public String cancelRequest_S(@AuthenticationPrincipal UserDetails user, Request_S r) {
		r.setMemberid(user.getUsername());
		service.cancelRequest_S(r);
		return "redirect:/";
	}
	
	@PostMapping("insertRM")
	public String insertRequest_M(@AuthenticationPrincipal UserDetails user, Request_M r) {
		r.setMemberid(user.getUsername());
		service.insertRequest_M(r);
		return "redirect:/";
	}
	
	@GetMapping("cancelRM")
	public String cancelRequest_M(@AuthenticationPrincipal UserDetails user, Request_M r) {
		r.setMemberid(user.getUsername());
		service.cancelRequest_M(r);
		return "redirect:/";
	}

}
