package com.pvcom.services;

public interface EmailService {
    public void sendNewUserWelcomeEmail(String email,String name,String username,String pwd);
    public boolean sendForgetPwdEmail(String email, String name, String pwd);
}
