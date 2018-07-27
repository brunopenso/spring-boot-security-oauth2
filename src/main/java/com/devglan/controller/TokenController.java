package com.devglan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TokenController {
	@Autowired
	private DefaultTokenServices tokenServices;
	
	@RequestMapping(value="/oauth/token/revoke", method=RequestMethod.POST)
	public @ResponseBody void create(@RequestParam("token") String value) {
		tokenServices.revokeToken(value);
	}
}
