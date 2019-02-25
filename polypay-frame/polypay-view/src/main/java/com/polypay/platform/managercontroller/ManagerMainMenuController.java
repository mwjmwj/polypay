package com.polypay.platform.managercontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ManagerMainMenuController {

	
	@RequestMapping("main/mainmenu")
	public String getManagetMainMenu()
	{
		
		
		return "adminmanager/main";
	}
	
}
