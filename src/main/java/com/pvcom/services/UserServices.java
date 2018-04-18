/**
 *
 */
package com.pvcom.services;

import com.pvcom.common.WorkflowConstants;
import com.pvcom.model.User;
import com.pvcom.projections.UserIdNames;

import java.util.List;

/**
 * @author Kishan
 */
public interface UserServices {
    
    public User create(User user);

    public Iterable<User> list();

    public User update(int id, User user);

    public User get(int id);

    public User get(String email);

    public WorkflowConstants.ErrorCodes resetPassword(String email);

    public boolean delete(int id);

    public List<UserIdNames> getIdNames();

    public Integer getIdByEmail(String email);

}
