package poris.fruitlight.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import poris.fruitlight.dto.Shopper;
import poris.fruitlight.service.MyPageChangeInfoService;
import poris.fruitlight.util.AlertScript;

@Slf4j
@Controller
public class MyPageChangeInfoController {
	
	@Resource
	public MyPageChangeInfoService myPageChangeInfoService;
	
	@RequestMapping("/mypageChangeInfo")
	public String mypageChangeInfo(Model model, int shopperNo, HttpSession session, HttpServletResponse response) {
		Shopper shopper = (Shopper) session.getAttribute("ShopperInfo");
		if(shopper == null) {
			try {
				AlertScript.alertAndMovePage(response, "로그인을 해주세요", "/fruitlight/login");
			} catch (IOException e) {
				return "redirect:/main";
			}
		} else {
			Shopper shopperinfo = myPageChangeInfoService.getShopper(shopperNo);
			model.addAttribute("mypageChangeInfo", shopperinfo);
			
		}
		return "mypageChangeInfo";
	}
	
	@PostMapping("/mypageChangeInfo")
	public String checkPw(String shopperPwd, HttpSession session, Model model, int shopperNo, HttpServletResponse response) throws IOException {
		
		if(!shopperPwd.equals("")) {
			// Step1. 로그인 세션 정보 조회
			Shopper shopper = (Shopper) session.getAttribute("ShopperInfo");
			Shopper shopperinfo = myPageChangeInfoService.getShopper(shopperNo);
			shopper.setShopperPw(shopperPwd);
			
			// Step2. Service에 입력 PW와 DB에 저장된 PW 비교 요청 메소드 실행
			boolean resultCheckPw = myPageChangeInfoService.isShopperPw(shopper);
			if(resultCheckPw) {
				// Model에 결과 값을 넣어 JSP에 전달
				model.addAttribute("resultCheckPw", resultCheckPw);
				model.addAttribute("mypageChangeInfo", shopperinfo);
			} else {
				AlertScript.alertAndBackPage(response, "입력한 비밀번호를 다시 확인해주세요.");
			}
			return "mypageChangeInfo";
		} else {
			AlertScript.alertAndBackPage(response, "비밀번호를 입력하세요.");
			return "mypageChangeInfo";
		}
	}
	
	@PostMapping("/mypageChangeInfo/updateId")
	public String updateId(String shopperId , HttpSession session, HttpServletResponse response) throws IOException {
		
		Shopper shopper = (Shopper) session.getAttribute("ShopperInfo");
		shopper.setShopperId(shopperId);
		
		boolean result = myPageChangeInfoService.shopperIdDuplicateCheck(shopper);
		
		if(result) {
			AlertScript.alertAndBackPage(response, "중복된 아이디가 존재합니다.");
		} else {
			AlertScript.alertAndMovePage(response, "개인정보가 변경되어 로그아웃 되었습니다.", "/fruitlight/login");
			session.removeAttribute("ShopperInfo");
		}
		return "mypageChangeInfo";
	}
	
	
	@PostMapping("/mypageChangeInfo/updateTel")
	public String updateTel(String shopperTel , HttpSession session , HttpServletResponse response) throws IOException {
		
		Shopper shopper = (Shopper) session.getAttribute("ShopperInfo");
		shopper.setShopperTel(shopperTel);
		
		myPageChangeInfoService.setShopperTel(shopper);

		return "mypageChangeInfo";
	}
	
	
	@PostMapping("/mypageChangeInfo/updatePW")
	public String updatePW(String updateShopperPw , HttpSession session, HttpServletResponse response) throws IOException {
		Shopper shopper = (Shopper) session.getAttribute("ShopperInfo");
		shopper.setShopperPw(updateShopperPw);
		
		myPageChangeInfoService.setShopperPassword(shopper);
		
		AlertScript.alertAndMovePage(response, "개인정보가 변경되어 로그아웃 되었습니다.", "/fruitlight/login");
		session.removeAttribute("ShopperInfo");

		return "mypageChangeInfo";
	}
}
