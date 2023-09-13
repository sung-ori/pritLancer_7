package com.team.pretLancer_7.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.team.pretLancer_7.domain.MyPage;
import com.team.pretLancer_7.service.LongService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/long")
public class LongTranslateController {
    
    @Autowired
    LongService service;

    @GetMapping("/main")
    public String mainForm(){
        return "translate_long/mainForm";
    }

    @GetMapping("/request")
    public String requestForm(Model model) {
        List<MyPage> translatorList = service.getTranslatorList();
        log.error("돌아오나요? {}", translatorList);
        model.addAttribute("translatorList", translatorList);

        return "translate_long/requestForm";
    }

    @GetMapping("/translatorProfile")
    public String translatorProfile(Model model, @RequestParam(name="memberid") String memberid, @AuthenticationPrincipal UserDetails user ) {
        String loginId = user.getUsername();
        MyPage translatorProfile = service.getOneMyPage(memberid);

        model.addAttribute("tp", translatorProfile);
        model.addAttribute("loginId", loginId);


        return "/translate_long/translatorProfile";
    }

    @GetMapping("/writeRequest")
    public String request(Model model, @RequestParam(name="memberid") String memberid, @AuthenticationPrincipal UserDetails user) {
        String translatorId = memberid;
        String loginId = user.getUsername();
        
        model.addAttribute("translatorId", translatorId);
        model.addAttribute("loginId", loginId);


        return "/translate_long/writeRequestForm";
    }


}
