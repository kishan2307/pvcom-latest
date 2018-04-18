package com.pvcom.web;

import com.pvcom.beans.AssignBean;
import com.pvcom.beans.DashboardResponseBean;
import com.pvcom.model.Response;
import com.pvcom.model.UserWorkflows;
import com.pvcom.projections.SearchBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pvcom.services.WorkflowService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/flow")
public class WorkflowController {

    @Autowired
    private WorkflowService workflowService;

    @GetMapping("/{id}")
    private @ResponseBody
    Response getById(@PathVariable int id) {
        UserWorkflows userWorkflows = workflowService.get(id);
        if (userWorkflows != null) {
            return new Response("SUCCESS", 100, userWorkflows);
        }
        return new Response("FAIL", 101, null);
    }

    @PutMapping
    private @ResponseBody
    Response add(@RequestBody UserWorkflows userWorkflows, Principal principal) {
        userWorkflows = workflowService.add(userWorkflows, principal.getName());
        if (userWorkflows != null) {
            return new Response("SUCCESS", 100, userWorkflows);
        }
        return new Response("FAIL", 101, null);
    }

    @PostMapping("/{id}")
    private @ResponseBody
    Response update(@RequestBody UserWorkflows userWorkflows, @PathVariable int id, Principal principal) {
        userWorkflows = workflowService.update(userWorkflows, principal.getName());
        if (userWorkflows != null) {
            return new Response("SUCCESS", 100, userWorkflows);
        }
        return new Response("FAIL", 101, null);
    }

    @DeleteMapping("/{id}")
    private @ResponseBody
    Response delete(@PathVariable int id) {
        if (workflowService.delete(id)) {
            return new Response("SUCCESS", 100, null);
        }
        return new Response("FAIL", 101, null);
    }

    @GetMapping
    private @ResponseBody
    Response list() {
        Iterable<UserWorkflows> list = workflowService.list();
        if (list != null) {
            return new Response("SUCCESS", 100, list);
        }
        return new Response("FAIL", 101, null);
    }

    @GetMapping("/home")
    private @ResponseBody
    Response home() {
        DashboardResponseBean dashBoardData = workflowService.getDashBoardData();
        if (dashBoardData != null) {
            return new Response("SUCCESS", 100, dashBoardData);
        }
        return new Response("FAIL", 101, null);
    }

    @GetMapping("/list/{name}")
    private @ResponseBody
    Response flowList(@PathVariable String name) {
        Iterable<UserWorkflows> list = workflowService.list(name);
        if (list != null) {
            return new Response("SUCCESS", 100, list);
        }
        return new Response("FAIL", 101, null);
    }
    
    @GetMapping("/search/{name}")
    private @ResponseBody
    Response flowSearch(@PathVariable String name) {
      Map<String,Object> map=workflowService.getSarchResults(name);
        if (map != null) {
            return new Response("SUCCESS", 100, map);
        }
        return new Response("FAIL", 101, null);
    }

    @PostMapping("/filter")
    private @ResponseBody
    Response search(@RequestBody SearchBean searchBean) {
        Map<String, Object> list = workflowService.search(searchBean);
        if (list != null) {
            return new Response("SUCCESS", 100, list);
        }
        return new Response("FAIL", 101, null);
    }

    @PostMapping("/assign")
    private @ResponseBody
    Response assign(@RequestBody AssignBean assignBean,Principal principal) {
        if (workflowService.assignAll(assignBean,principal.getName())) {
            return new Response("SUCCESS", 100,"success");
        }
        return new Response("FAIL", 101, null);
    }
}
