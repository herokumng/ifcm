package com.dkbmc.ifcm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	@RequestMapping("/")
	public String root() {
		return "redirect:/map/wa100";
	}
}