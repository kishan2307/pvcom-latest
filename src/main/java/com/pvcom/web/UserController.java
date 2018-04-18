/**
 *
 */
package com.pvcom.web;

import com.pvcom.model.Response;
import com.pvcom.model.User;
import com.pvcom.projections.UserIdNames;
import com.pvcom.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Kishan
 */
@RestController
@RequestMapping("/user/resources")
public class UserController {

    @Autowired
    private UserServices userservices;

    @PutMapping
    public @ResponseBody
    Response create(@RequestBody User usr) {
        Integer id=userservices.getIdByEmail(usr.getEmail());
        if(id==null) {
            User user = userservices.create(usr);
            if (user != null) {
                return new Response("SUCCESS", 100, user);
            } else {
                return new Response("FAIL", 101, user);
            }
        }else{
            return new Response("FAIL", 102, "Email id already exists");
        }
    }

    @GetMapping
    public @ResponseBody
    Response list() {
        Iterable<User> users = userservices.list();
        if (users != null) {
            return new Response("SUCCESS", 100, users);
        } else return new Response("FAIL", 101, users);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public @ResponseBody
    Response update(@RequestBody User usr, @PathVariable Integer id) {
        User user = userservices.update(id, usr);
        if (user != null) {
            return new Response("SUCCESS", 100, user);
        } else return new Response("FAIL", 101, user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Response get(@PathVariable Integer id) {
        User user = userservices.get(id);
        if (user != null) {
            return new Response("SUCCESS", 100, user);
        } else return new Response("FAIL", 101, user);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody
    Response delete(@PathVariable Integer id) {
        if (userservices.delete(id)) {
            return new Response("SUCCESS", 100, null);
        } else return new Response("FAIL", 101, null);
    }

    @GetMapping("/dropdownList")
    public @ResponseBody
    Response dropDownList() {
        List<UserIdNames> users = userservices.getIdNames();
        if (users != null) {
            return new Response("SUCCESS", 100, users);
        } else return new Response("FAIL", 101, users);
    }

    @GetMapping("/{email}/exist")
    public @ResponseBody
    Response isExist(@PathVariable String email) {
        if (userservices.getIdByEmail(email)>0) {
            return new Response("SUCCESS", 100, null);
        } else return new Response("FAIL", 101, null);
    }
}
