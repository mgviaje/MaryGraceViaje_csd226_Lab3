package csd226.lecture8.controllers;

import csd226.lecture8.data.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewControllers {

    private final AccessToken accessToken;

    public ViewControllers(AccessToken token) {
        accessToken = token;
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
    @GetMapping("/")
    public String index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        authentication.getDetails().
        String t = accessToken.getAccessToken();
        model.addAttribute("token", "123456789abcdefg");

        return "index";
    }


}