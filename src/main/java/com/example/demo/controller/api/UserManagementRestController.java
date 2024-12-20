package com.example.demo.controller.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import com.example.demo.utils.CustomResponse;
import com.example.demo.utils.EmailService;
import com.example.demo.utils.JWTUtil;
import com.example.demo.utils.PasswordResetService;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("api/user")
public class UserManagementRestController {

  private UserRepository userRepository;
  private PersonRepository personRepository;
  private RoleRepository roleRepository;
  private PasswordEncoder passwordEncoder;
  private EmailService emailService;
  private PasswordResetService passwordResetService;
  private PasswordResetTokenRepository tokenRepository;
  private JWTUtil jwtUtil;
  private AuthenticationManager authManager;

  public UserManagementRestController(
    UserRepository userRepository, 
    PersonRepository personRepository, 
    RoleRepository roleRepository, 
    PasswordEncoder passwordEncoder,
    EmailService emailService,
    MailSender mailSender,
    PasswordResetService passwordResetService,
    PasswordResetTokenRepository passwordResetTokenRepository,
    JWTUtil jwtUtil,
    AuthenticationManager authManager
  ) {
    this.userRepository = userRepository;
    this.personRepository = personRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
    this.passwordResetService = passwordResetService;
    this.tokenRepository = passwordResetTokenRepository;
    this.jwtUtil = jwtUtil;
    this.authManager = authManager;
  }

  @PostMapping("register")
  public ResponseEntity<Object> postRegister(@RequestBody RegisterDTO registerDTO) {
    Person person = new Person();
    person.setEmail(registerDTO.getEmail());
    person.setFullname(registerDTO.getFullname());
    person.setPhone(registerDTO.getPhone());
    
    person = personRepository.save(person);

    User user = new User();
    user.setId(person.getId());
    user.setRole(roleRepository.lowLevelRole());
    user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

    user = userRepository.save(user);  

    if (userRepository.findById(user.getId()).get() != null) {
      Boolean sendEmail = emailService.sendMessage(person.getEmail(), "Success Create Account", "Account registration is successful, please log in");
      if (sendEmail) {
        return CustomResponse.generate(
          HttpStatus.OK,
          "Success create an account, check your email for the confirmation"
        );
      }
    }

    return CustomResponse.generate(
      HttpStatus.OK,
      "Failed create an account, try again"
    );
  }

  @PostMapping("auth")
  public ResponseEntity<Object> postAuth(@RequestBody LoginDTO loginDTO) {
    UserDTO userDTO = userRepository.getUserByEmail(loginDTO.getEmail());

    if (userDTO != null && passwordEncoder.matches(loginDTO.getPassword(), userDTO.getPassword())) {
      UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
      authManager.authenticate(authInputToken);

      String token = jwtUtil.generateToken(loginDTO.getEmail());
      
      return CustomResponse.generate(
        HttpStatus.OK,
        "token",
        token
      );
    }

    return CustomResponse.generate(
      HttpStatus.OK,
      "Invalid credentials"
    );

    // OLD 1 TRANSMIGRATION
    //   try {
    //     org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(
    //       userDTO.getId().toString(),
    //       "",
    //       getAuthorities(userDTO.getRoleName())
    //     );
    //     PreAuthenticatedAuthenticationToken authenticationToken = new PreAuthenticatedAuthenticationToken(
    //       user,
    //       "",
    //       user.getAuthorities()
    //     );
    //     SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    //   } catch (Exception e) {
    //     throw new RuntimeException(e);
    //   }
    // } else {
    //   return CustomResponse.generate(
    //     HttpStatus.OK,
    //     "Wrong password or email, try again" 
    //   );
    // }

    // return CustomResponse.generate(
    //   HttpStatus.OK,
    //   "Login successful"
    // );
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<Object> getUserById(@PathVariable(required = true) Integer id) {
    String userEmailBySecurity = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Integer userIdBySecurity = userRepository.getUserByEmail(userEmailBySecurity).getId();
    User user = userRepository.findById(userIdBySecurity).get();
    
    if (userIdBySecurity == id) {
      return CustomResponse.generate(
        HttpStatus.OK,
        "Success get a user",
        user
      );
    } else {
      return CustomResponse.generate(
        HttpStatus.FORBIDDEN,
        "Jenengan sopo?!"
      );
    }

  }

  @PostMapping("edit/{id}")
  public ResponseEntity<Object> postEdit(@RequestBody ChangePasswordDTO changePasswordDTO) {
    User existingUser = userRepository.findById(changePasswordDTO.getId()).get();

    if (changePasswordDTO.getOldPassword() != null && passwordEncoder.matches(changePasswordDTO.getOldPassword(), existingUser.getPassword())) {
      existingUser.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
      emailService.sendMessage(existingUser.getPerson().getEmail(), "Password Changed", "Successfully changed password to:\n" + changePasswordDTO.getNewPassword());

      return CustomResponse.generate(
        HttpStatus.OK,
        "Password changed successfully",
        userRepository.save(existingUser)
      );
    } else {
      return CustomResponse.generate(
        HttpStatus.OK, 
        "Failed change password"
      );
    }
  }

  @PostMapping("/forgotpassword/sendEmail")
  public ResponseEntity<Object> postLinkResetPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {
    UserDTO userDTO = userRepository.getUserByEmail(forgotPasswordDTO.getEmail());
    User user = userRepository.findById(userDTO.getId()).get();
  
    Boolean sendResetLink = passwordResetService.createPasswordResetToken(user);
    if (sendResetLink) {
      return CustomResponse.generate(
        HttpStatus.OK,
        "The password reset link has been sent via email"
      );
    }
    
    return CustomResponse.generate(
      HttpStatus.OK,
      "Failed to sent reset password link via email, try again"
    );
  }
  
  @PostMapping("/forgotpassword/reset/{token}")
  public ResponseEntity<Object> postMethodName(@PathVariable(required = true) String token, @RequestBody ResetPasswordDTO resetPasswordDTO) {
    Boolean isPasswordReset = passwordResetService.resetPassword(token, resetPasswordDTO.getNewPassword());
    
    if (isPasswordReset) {
      return CustomResponse.generate(
        HttpStatus.OK,
        "Password reset successfully to: " + resetPasswordDTO.getNewPassword()
      );
    } else {
      return CustomResponse.generate(
        HttpStatus.OK,
        "Password failed to reset, please try to send another link"
      );
    }
  }
  
  @PostMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest request) {
    SecurityContextHolder.clearContext();
    HttpSession session = request.getSession(false);
    if (session != null) {
        session.invalidate();
    }
      
    return CustomResponse.generate(
      HttpStatus.OK,
      "Logout successful"
    );
  }

  private static Collection<? extends GrantedAuthority> getAuthorities(String roles) {
    final List<SimpleGrantedAuthority> authorities = new LinkedList<>();
    authorities.add(new SimpleGrantedAuthority(roles));
    return authorities;
  }
}
