package ru.kata.spring.boot_security.demo.start;


import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.Set;

@Component
public class OnStart implements ApplicationListener<ContextRefreshedEvent> {

    private final UserService userService;


    public OnStart(UserService userService) {
        this.userService = userService;

    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Role userRole = new Role();
        userRole.setName("ROLE_USER");


        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");

        Set<Role> userRoles = Set.of(userRole);
        Set<Role> adminRoles = Set.of(adminRole);

        User user = new User("user", "user","u@mail.ru", userRoles);
        userService.saveUser(user);

        User admin = new User("admin", "admin","a@mail.ru", adminRoles);
        userService.saveUser(admin);
    }
}
