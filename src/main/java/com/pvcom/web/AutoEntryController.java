package com.pvcom.web;

import com.pvcom.model.AutoEntry;
import com.pvcom.model.Response;
import com.pvcom.model.Workflow;
import com.pvcom.services.AutoEntryService;
import com.pvcom.services.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/auto")
public class AutoEntryController {


    @Autowired
    private AutoEntryService autoEntryService;

    @PutMapping
    public @ResponseBody
    Response create(@RequestBody AutoEntry body, Principal principal) {
        if (autoEntryService.add(body)) {
            return new Response("Success", 100, body);
        }
        return new Response("FAIL", 101, body);
    }

    @PostMapping("/{id}")
    public @ResponseBody
    Response update(@PathVariable("id") int id, @RequestBody AutoEntry body, Principal principal) {
        if (autoEntryService.update(body)) {
            return new Response("Success", 100, body);
        }
        return new Response("FAIL", 101, body);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody
    Response delete(@PathVariable("id") int id) {
        if (autoEntryService.delete(id)) {
            return new Response("Success", 100, "true");
        }
        return new Response("FAIL", 101, null);
    }

    @GetMapping("/{id}")
    public @ResponseBody
    Response get(@PathVariable("id") int id) {
        return new Response("SUCCESS", 100, autoEntryService.get(id));
    }

    @GetMapping
    public @ResponseBody
    Response get() {
        return new Response("SUCCESS", 100, autoEntryService.get());
    }
}
