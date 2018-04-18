package com.pvcom.web;

import com.pvcom.common.WorkflowConstants;
import com.pvcom.common.WorkflowConstants.DeaultValues;
import com.pvcom.model.User;
import com.pvcom.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserServices userServices;

    @RequestMapping("/")
    public String home(Authentication auth, Model model) {
        boolean hasUserRole = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("admin"));

        User user = userServices.get(auth.getName());
        if (user != null) {
            model.addAttribute("name", user.getFirstName() + " " + user.getLastName());
            model.addAttribute("user", user);
            model.addAttribute("roleTitle", DeaultValues.geRoleTitle(user.getRole()));
        }

        if (!hasUserRole) {
            boolean hasUserGuestRole = auth.getAuthorities().stream()
                    .anyMatch(r -> r.getAuthority().equals("guest"));
            if (hasUserGuestRole) {
                return "guest";
            }
        } else {
            return "admin";
        }

        return "user";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/forgot")
    public String forgot() {
        return "forgot";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @PostMapping("/forgot")
    public String forgotPost(@RequestParam("email") String email, Model model) {
        if (email != null) {
            WorkflowConstants.ErrorCodes errorCodes = userServices.resetPassword(email);
            if (errorCodes == WorkflowConstants.ErrorCodes.SUCCESS) {
                model.addAttribute("success", "true");
            } else {
                model.addAttribute("fail", errorCodes.getDesc());
            }
        }
        return "forgot";
    }
}
