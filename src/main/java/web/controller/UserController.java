package web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/index")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView showAdminPage() {
        ModelAndView model = new ModelAndView();
        model.addObject("users", userService.getAllUser());
        model.setViewName("admin");
        return model;
    }

    @GetMapping("/user")
    public ModelAndView  showUserPage(@AuthenticationPrincipal User user) {
        ModelAndView model = new ModelAndView();
        model.addObject("user", userService.findUserByName(user.getUsername()));
        model.setViewName("user");
        return model;
    }

    @GetMapping("/user/add")
    public ModelAndView  showNewUserForm() {
        ModelAndView model = new ModelAndView();
        model.addObject("user", new User());
        model.setViewName("addUser");
        return model;
    }

    @PostMapping("/admin")
    public ModelAndView addUser(@ModelAttribute("user") User user, BindingResult bindingResult) {
        ModelAndView model = new ModelAndView();
        model.setViewName("addUser");

        if(bindingResult.hasErrors()){
            model.addObject("bindingResult", bindingResult.getAllErrors());
            return model;
        }

        if (userService.getAllUser()
                .stream()
                .anyMatch(u -> u.getUsername().equals(user.getUsername()))) {
            model.addObject("loginError", "Login already exists, please choose another login");
            return model;
        }

        if (user.getPassword() != null && !user.getPassword().equals(user.getPasswordConfirm())) {
            model.addObject("passwordError", "Passwords are different");
            return model;
        }

        model.setViewName("redirect:/admin");
        userService.saveUser(user);
        return model;
    }

    @GetMapping("/user/update/{id}")
    @ResponseBody
    public User updateUserForm(@PathVariable("id") Long id) {
        User userFromDB = userService.findById(id);
        return userFromDB;
    }

    @PostMapping("/user/update")
    public ModelAndView updateUser(User user,
                             @RequestParam Map<String, String> form) {
        ModelAndView model = new ModelAndView();
        List<Role> roles = roleService.getAllRoles();

        for (String key : form.values()) {
            if (roles.stream().anyMatch(role -> role.getName().equals(key))) {
                user.getRoles().add(roleService.findByName(key));
            }
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.saveUser(user);
        model.setViewName("redirect:/admin");
        return model;
    }

    @GetMapping("/user/remove")
    public ModelAndView removeUser( @RequestParam Map<String, String> map) {
        ModelAndView model = new ModelAndView();
        String id1 = map.get("id1");
        Long id = Long.valueOf(id1);
        User userFromDB = userService.findById(id);
        userService.removeUser(userFromDB);
        model.setViewName("redirect:/admin");
        return model;
    }
}
