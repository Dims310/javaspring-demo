package com.example.demo.controller;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.mail.MailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.model.Person;
import com.example.demo.model.User;
import com.example.demo.model.dto.ChangePasswordDTO;
import com.example.demo.model.dto.ForgotPasswordDTO;
import com.example.demo.model.dto.LoginDTO;
import com.example.demo.model.dto.RegisterDTO;
import com.example.demo.model.dto.ResetPasswordDTO;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.repository.PersonRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.PasswordResetService;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/user")
public class UserManagementController {

  private UserRepository userRepository;
  private PersonRepository personRepository;
  private RoleRepository roleRepository;
  private PasswordEncoder passwordEncoder;
  private EmailService emailService;
  private PasswordResetService passwordResetService;
  private PasswordResetTokenRepository tokenRepository;

  public UserManagementController(
    UserRepository userRepository, 
    PersonRepository personRepository, 
    RoleRepository roleRepository, 
    PasswordEncoder passwordEncoder,
    EmailService emailService,
    MailSender mailSender,
    PasswordResetService passwordResetService,
    PasswordResetTokenRepository passwordResetTokenRepository
  ) {
    this.userRepository = userRepository;
    this.personRepository = personRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
    this.passwordResetService = passwordResetService;
    this.tokenRepository = passwordResetTokenRepository;
  }

  @GetMapping("register")
  public String getRegister(Model model) {
      model.addAttribute("register", new RegisterDTO());
      return "user/register";
  }

  @PostMapping("register")
  public String postRegister(RegisterDTO registerDTO) {
    Person person = new Person();
    person.setEmail(registerDTO.getEmail());
    person.setFullname(registerDTO.getFullname());
    person.setPhone(registerDTO.getPhone());
    
    person = personRepository.save(person);

    User user = new User();
    user.setId(person.getId());
    user.setRole(roleRepository.lowLevelRole());
    user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

    userRepository.save(user);  
    
    Boolean sendEmail = emailService.sendMessage(person.getEmail(), "Success Create Account", "Account registration is successful, please log in");
    if (sendEmail) {
      return "redirect:/user/login";
    }
    return "redirect:/user/register";
  }
  
  @GetMapping("login")
  public String getLogin(Model model) {
    model.addAttribute("login", new LoginDTO());
    return "user/login";
  }

  @PostMapping("auth")
  public String auth(LoginDTO loginDTO, Model model) {
    UserDTO userDTO = userRepository.getUserByEmail(loginDTO.getEmail());
    if (userDTO != null && passwordEncoder.matches(loginDTO.getPassword(), userDTO.getPassword())) {
      try {
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(
          userDTO.getId().toString(),
          "",
          getAuthorities(userDTO.getRoleName())
        );
        PreAuthenticatedAuthenticationToken authenticationToken = new PreAuthenticatedAuthenticationToken(
          user,
          "",
          user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } else {
      return "redirect:/user/login";
    }

    return "redirect:/dashboard";
  }

  @GetMapping(value = {"edit", "edit/{id}"})
  public String getEdit(@PathVariable(required = false) Integer id, Model model) {
    User user = userRepository.findById(id).get();
    model.addAttribute("user", user);
    model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
    return "user/edit";
  }

  @PostMapping("/edit")
  public String postEdit(ChangePasswordDTO changePasswordDTO) {
    User existingUser = userRepository.findById(changePasswordDTO.getId()).get();

    if (changePasswordDTO.getOldPassword() != null && passwordEncoder.matches(changePasswordDTO.getOldPassword(), existingUser.getPassword())) {
      existingUser.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
      emailService.sendMessage(existingUser.getPerson().getEmail(), "Password Changed", "Successfully changed password to:\n" + changePasswordDTO.getNewPassword());
    } else {
      return "redirect:/user/edit/" + changePasswordDTO.getId();
    }
    
    userRepository.save(existingUser);

    return "redirect:/dashboard";
  }

  @GetMapping("/forgotpassword")
  public String forgotPassword(Model model) {
    model.addAttribute("forgotPassword", new ForgotPasswordDTO());
    return "user/forgotpassword";
  }

  @PostMapping("/forgotpassword/sendEmail")
  public String forgotPasswordSendMail(ForgotPasswordDTO forgotPasswordDTO) {
    UserDTO userDTO = userRepository.getUserByEmail(forgotPasswordDTO.getEmail());
    User user = userRepository.findById(userDTO.getId()).get();
  
    Boolean sendResetLink = passwordResetService.createPasswordResetToken(user);
    if (sendResetLink) {
      return "redirect:/user/login";
    }
    return "redirect:/user/forgotpassword";
  }
  
  @GetMapping(value = {"/forgotpassword/reset", "/forgotpassword/reset/{token}"})
  public String getResetPassword(@PathVariable(required = false) String token, Model model) {
    // Integer userId = tokenRepository.findIdUserByToken(token);
    // User user = userRepository.findById(userId).get();
    ResetPasswordDTO resetPasswordDTO = new ResetPasswordDTO();
    resetPasswordDTO.setToken(token);
    model.addAttribute("resetPasswordDTO", resetPasswordDTO);
    return "user/resetpassword";
  }

  @PostMapping("/forgotpassword/resetpassword")
  public String resetPassword(ResetPasswordDTO resetPasswordDTO) {
      // User existingUser = userRepository.findById(user.getId()).get();

      // if (user.getPassword() != null && user.getPassword().length() > 0) {
      //   existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
      // }

      // userRepository.save(existingUser);

      passwordResetService.resetPassword(resetPasswordDTO.getToken(), resetPasswordDTO.getNewPassword());

      // emailService.sendMessage(existingUser.getPerson().getEmail(), "Success Reset Password", "Success changes password to:\n" + user.getPassword());
      
      return "redirect:/user/login";
  }

  // Mendefinisikan otoritas yang diberikan tiap pengguna dengan memasukkan roles
  // ke dalam SimpleGrantedAuthority
  private static Collection<? extends GrantedAuthority> getAuthorities(String roles) {
    final List<SimpleGrantedAuthority> authorities = new LinkedList<>();
    authorities.add(new SimpleGrantedAuthority(roles));
    return authorities;
  }
}
