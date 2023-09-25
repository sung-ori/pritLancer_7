package com.team.pretLancer_7.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.team.pretLancer_7.domain.AuctionTranslator;
import com.team.pretLancer_7.domain.MyPage;
import com.team.pretLancer_7.domain.Request_L;
import com.team.pretLancer_7.messaging.MessagingService;
import com.team.pretLancer_7.service.LongService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/long")
public class LongTranslateController {
    
    @Autowired
    LongService service;
    @Autowired
    MessagingService msg;

    

    @GetMapping("/main")
    public String mainForm(){
        return "translate_long/mainForm";
    }

    @GetMapping("/request")
    public String requestForm(Model model,@AuthenticationPrincipal UserDetails user) {
        List<MyPage> translatorList = service.getTranslatorList(user.getUsername());
        log.error("돌아오나요? {}", translatorList);
        model.addAttribute("translatorList", translatorList);

        return "translate_long/translatorList";
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

    @PostMapping("/writeRequest")
    public String writeRequest(Request_L request_L, MultipartFile uploadFile, @AuthenticationPrincipal UserDetails user){
        // String originFileName = uploadFile.getOriginalFilename();
        log.debug("장문 요청 컨트롤러 {}",request_L);
        // log.debug("오리진 파일 {}", originFileName);

        request_L.setMemberid(user.getUsername());

        service.writeRequest(request_L,uploadFile);

        service.pay(user.getUsername(),request_L.getCash());

        msg.writeLR(request_L);
        return "redirect:/";
    }

    @GetMapping("/auction")
    public String auctionForm() {
        return "/translate_long/writeAuctionForm";
    }

    @PostMapping("/writeAuction")
    public String writeAuction(Request_L request_L, MultipartFile uploadFile, @AuthenticationPrincipal UserDetails user) {

        request_L.setMemberid(user.getUsername());

        service.writeAuction(request_L,uploadFile);

        return "redirect:/";
    }

    @GetMapping("auctionList")
    public String auctionList(Model model,@AuthenticationPrincipal UserDetails user) {
        List<Request_L> auctionList =  service.getAuctionList();

        model.addAttribute("auctionList", auctionList);
        return "/translate_long/auctionListForm";
    }
    
    @GetMapping("readAuctionInfo")
    public String readAuctionInfo(Model model, @RequestParam(name="requestnum_l") int requestnum_l) {
        Request_L rql = service.readAuctionInfo(requestnum_l);
        int auctionNum  = service.getAuctionNumber(rql.getRequestnum_l());

        model.addAttribute("info", rql);
        model.addAttribute("auctionNum", auctionNum);

        return "/translate_long/auctionInfo";
    }

    @GetMapping("readAuctionPrice")
    @ResponseBody
    public List<AuctionTranslator> readAuctionPrice(@RequestParam(name="auctionNum") int auctionNum) {
        List<AuctionTranslator> list ;
        list = service.readAuctionPrice(auctionNum);

        return list;
    }

    @GetMapping("bid")
    @ResponseBody
    public void bid(AuctionTranslator at,@AuthenticationPrincipal UserDetails user){
        at.setMemberid(user.getUsername());
        log.debug("컨트롤러 경매 입찰 삽입 {}", at);
        service.setBid(at);
    }

    @GetMapping("bidValidation")
    @ResponseBody
    public String bidValidation(@AuthenticationPrincipal UserDetails user, String auctionNum) {
        Map<String, String> map = new HashMap();
        map.put("auctionnum", auctionNum);
        map.put("memberid", user.getUsername());
        
        return service.bidValidation(map);
    }
// 테이블에 경먀 여부를 적어놓았다. 불러오면 다 불러 진다. 


//====
    @GetMapping("/cashCheck")
    @ResponseBody
    public String cashCheck(@AuthenticationPrincipal UserDetails user,int cash) {
        String userid = user.getUsername();
        String rst = service.cashCheck(userid, cash);

        return rst;
    }

    @GetMapping("successfulBid")
	@ResponseBody
	public void successfulBid(String biderid,String requestnum, String auctionnum) {
		
		Map<String, String> map = new HashMap() ;
		map.put("memberid",biderid);
		map.put("requestnum", requestnum);
		map.put("auctionnum",auctionnum);
		int rst =  service.successfulBid(map);

        
		

	}
    @GetMapping("/readRequestInfo")
    public String readRequestInfo(Model model, @RequestParam(name = "requestnum_l") int requestnum_l) {

        Request_L request =  service.readRequestInfo(requestnum_l);
        model.addAttribute("request", request);
        return "/translate_long/requestInfo";
    }

    @GetMapping("/requestResponse")
    @ResponseBody
    public String requestResponse(String requestnum, String message) {
        log.debug("컨트롤러 들어오나 확인");
        Map<String, String> map = new HashMap();
        map.put("requestnum", requestnum);
        map.put("message", message);
        return  service.resoponseToRequest(map);
    }
    // 번역가가 현재 변역중인지 확인.
    @GetMapping("/checkTranslateNow")
    @ResponseBody
    public Request_L checkTranslateNow (String memberid) {
        return  service.checkTranslateNow(memberid);
    }

    @GetMapping("/readAccessRequestInfo")
    public String readAccessRequestInfo(Model model, @RequestParam(name = "requestnum_l") int requestnum_l) {

        Request_L request =  service.readRequestInfo(requestnum_l);
        model.addAttribute("request", request);
        return "/translate_long/accessInfo";
    }

    @PostMapping("/uploadTest")
    @ResponseBody
    public void uploadTest(@RequestParam("uploadfile") MultipartFile uploadfile,@RequestParam("requestnum_l") int requestnum_l) {
        log.info("알려줘! {},{}", uploadfile.getOriginalFilename(),requestnum_l);
         service.uploadResult(uploadfile,requestnum_l);
    }

    @GetMapping("/readResult")
    public String readResult(@RequestParam("requestnum_l") int requestnum_l, Model model) {

        Request_L rql =  service.readRequestInfo(requestnum_l);
        model.addAttribute("result", rql);

        return "/translate_long/resultForm";
    }
    
    @GetMapping("/success")
    @ResponseBody
    public void success(@RequestParam("requestnum_l") int requestnum_l) {
        log.debug("되냐? : {}",""+requestnum_l);
         service.success(requestnum_l);
        


    }
   
}
