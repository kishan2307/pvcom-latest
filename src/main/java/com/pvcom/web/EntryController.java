package com.pvcom.web;

import com.pvcom.model.Response;
import com.pvcom.model.Workflow;
import com.pvcom.services.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/entry")
public class EntryController {

    @Autowired
    private EntryService entryService;

    @PutMapping
    public @ResponseBody
    Response create(@RequestBody Workflow body,Principal principal) {
        Workflow workflow = entryService.add(body,principal.getName());
        if (workflow != null) {
            return new Response("SUCCESS", 100, body);
        } else return new Response("FAIL", 101, body);
    }

    @PostMapping("/{id}")
    public @ResponseBody
    Response update(@PathVariable("id") int id, @RequestBody Workflow body,Principal principal) {
        if (entryService.update(id, body,principal.getName())) {
            return new Response("SUCCESS", 100, body);
        } else return new Response("FAIL", 101, null);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody
    Response delete(@PathVariable("id") int id) {
        if (entryService.delete(id)) {
            return new Response("SUCCESS", 100, null);
        } else return new Response("FAIL", 101, null);
    }

    @GetMapping("/{id}")
    public @ResponseBody
    Response get(@PathVariable("id") int id) {
        Workflow workflow = entryService.get(id);
        return new Response("SUCCESS", 100, workflow);
    }

    @GetMapping
    public @ResponseBody
    Response get(Principal principal) {
        Iterable<Workflow> list = entryService.list(principal.getName());
        return new Response("SUCCESS", 100, list);
    }

    @GetMapping("/uniq")
    public @ResponseBody
    Response getUniq() {
        List<Workflow> list = entryService.getUniqByLocalRef();
        return new Response("SUCCESS", 100, list);
    }

    @GetMapping("/dup")
    public @ResponseBody
    Response getDuplicate() {
        List<Workflow> list = entryService.getDuplicateByLocalRef();
        return new Response("SUCCESS", 100, list);
    }

    @GetMapping("/list/{type}")
    public @ResponseBody
    Response getListByType(@PathVariable("type") String type) {
        List<Workflow> list = entryService.getListByType(type);
        return new Response("SUCCESS", 100, list);
    }

    @GetMapping("/localRefs")
    public @ResponseBody
    Response getLocalRefList() {
        List<String> list = entryService.getLocalRefNumberList();
        return new Response("SUCCESS", 100, list);
    }
}
