package com.team.pretLancer_7.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.team.pretLancer_7.domain.Member;
import com.team.pretLancer_7.domain.QnA;
import com.team.pretLancer_7.service.MemberService;

import oracle.jdbc.proxy.annotation.Post;

@Controller
public class MainController {
    
	@Autowired
	MemberService service;
	
	// 로그인 전 메인페이지
    @GetMapping({" ", "/"})
    public String mainForm() {
        // 마감기한(번역을 수주한 경우), 회원등급, 신뢰도, 보유포인트 불러오는 메서드 필요. 
        return "public";
    }
  
    
    // 로그인 후 페이지
    @GetMapping("main")
    public String pubilcForm(@AuthenticationPrincipal UserDetails user, Model m) {
    	
    	Member member = service.getUser(user.getUsername());
    	m.addAttribute("member", member);
        
    	return "main3";
    }
 
    //문의 페이지
    @GetMapping("qna")
    public String qnaForm() {
    	return "qna";
    }
 
    //문의 페이지
    @PostMapping("qna")
    public String qnaForm(QnA q) {
    	int n = service.insertQnA(q);
    	
    	return "redirect:/qna";
    }
 
    //사이드바,Nav바 로드
    @GetMapping("snBar")
    public String snBar(){
    	return "fragments/snBar";
    }
}