package com.pvcom.web;

import com.pvcom.model.Response;
import com.pvcom.model.UserWorkflows;
import com.pvcom.services.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/resource/home")
public class Dashboardcontroller {

    @Autowired
    private WorkflowService workflowService;

    @GetMapping("/user")
    public @ResponseBody
    Response userHome(Principal principal) {
        if (principal.getName() != null) {
            List<UserWorkflows> userWorkflowsList = workflowService.getUserWorkflowList(principal.getName());
            if (userWorkflowsList != null) {
                return new Response("SUCESS", 100, userWorkflowsList);
            }
        }

        return new Response("FAIL", 101, null);
    }

}
