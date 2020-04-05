package com.securelogging.auth.controller;

import com.securelogging.auth.model.ConfirmationToken;
import com.securelogging.auth.model.User;
import com.securelogging.auth.service.ConfirmationTokenService;
import com.securelogging.auth.service.EmailSenderService;
import com.securelogging.auth.service.SecurityService;
import com.securelogging.auth.service.UserService;
import com.securelogging.auth.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private EmailSenderService emailSenderService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @GetMapping("/viewUsers")
    public String viewUsers(Model model) {
        model.addAttribute("users", userService.findAll());

        return "viewUser";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);

        ConfirmationToken token = new ConfirmationToken(userForm);
        confirmationTokenService.save(token);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userForm.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("ysnemreozbarut@gmail.com");
        mailMessage.setText("To confirm your account, please click here : "
                + "http://localhost:8082/confirm-account?token=" + token.getToken());

        emailSenderService.sendEmail(mailMessage);

        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/user/welcome";
    }

    /*@GetMapping("/confirm-account")
    public String confirmAccount(Model model, @RequestParam("token") String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.findByToken(token);

        if (Objects.isNull(confirmationToken)) {
            return "error";
        }
        User user = userService.findByEmail(confirmationToken.getUser().getEmail());
        user.setEnabled(true);
        userService.save(user);
        securityService.autoLogin(user.getUsername(), user.getPasswordConfirm(), user.isEnabled());
        return "redirect:/welcome";
    }*/

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @GetMapping({"/", "/welcome"})
    public String welcome(Model model) {
        return "welcome";
    }
}
