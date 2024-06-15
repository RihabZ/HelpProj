package com.rihab.interventions.restControllers;


import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rihab.interventions.dto.UserDTO;
import com.rihab.interventions.entities.AuthenticationResponse;
import com.rihab.interventions.entities.Magasinier;
import com.rihab.interventions.entities.Role;
import com.rihab.interventions.entities.User;
import com.rihab.interventions.register.ChangePasswordRequest;
import com.rihab.interventions.register.LoginRequest;
import com.rihab.interventions.repos.DemandeurRepository;
import com.rihab.interventions.repos.MagasinierRepository;
import com.rihab.interventions.repos.ManagerRepository;
import com.rihab.interventions.repos.TechnicienRepository;
import com.rihab.interventions.repos.UserRepository;
import com.rihab.interventions.service.AuthenticationService;
import com.rihab.interventions.service.JwtService;
import com.rihab.interventions.service.MagasinierService;
import com.rihab.interventions.util.EmailSender;
import com.rihab.interventions.util.EmailService;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@RestController
public class AuthenticationController {

    private final AuthenticationService authService;
    private final MagasinierService magasinierService;
    private final UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    //@Autowired
   // private TokenRepository tokenRepository;
   
    
    
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody UserDTO request
            ) {
        return ResponseEntity.ok(authService.register(request));
    }

 /*
    
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestParam String login,
            @RequestParam String password
    ) {
        return ResponseEntity.ok(authService.authenticate(login, password));
    }
*/
  
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
             @RequestBody UserDTO request
    ) {
    	 return ResponseEntity.ok(authService.authenticate(request));
    }
   
    @GetMapping("/allUsers")
    public List<UserDTO> getAllUsers() {
        return authService.getAllUsers();
    }
    

 // supprimer un user
    @RequestMapping(value="/delUser/{user_id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable("user_id") Long userId) {
        authService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    
    @PutMapping("/updateProfile")
    public ResponseEntity<UserDTO> updateProfile(@ModelAttribute UserDTO userDTO, 
                                                 @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        UserDTO updatedUser = authService.updateProfile(userDTO, imageFile);
        return ResponseEntity.ok(updatedUser);
    }
    
    @GetMapping("/current")
    public ResponseEntity<UserDTO> getCurrentUserProfile() {
        UserDTO currentUserProfile = authService.getCurrentUserDetails();
        return ResponseEntity.ok(currentUserProfile);
    }
    /*
    @PostMapping("/requestPasswordReset")
    public ResponseEntity<?> requestPasswordReset(@RequestParam String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        tokenRepository.save(passwordResetToken);

        String resetUrl = "http://localhost:3000/resetPassword/" + token; // Assurez-vous que cette URL correspond à votre front-end
        emailService.sendPasswordResetEmail(user.getEmail(), resetUrl);

        return ResponseEntity.ok("Email de réinitialisation de mot de passe envoyé");
    }


    
    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        PasswordResetToken passwordResetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token non valide ou expiré"));

        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        repository.save(user);

        return ResponseEntity.ok("Mot de passe réinitialisé avec succès");
    }

    
    */
    
    
    
    
    
/*
    @GetMapping("/allMag") // Définissez votre URL spécifique pour cette méthode
    public List<UserDTO> getAllMagasiniers() {
        List<Magasinier> magasiniers = magasinierService.getAllMagasiniers();
        List<User> magasiniersWithRole = magasiniers.stream()
                                                .filter(m -> m.getRole() == Role.MAGASINIER)
                                                .collect(Collectors.toList());
        return magasiniersWithRole.stream()
                .map(magasinier -> convertToDTO(magasinier))
                .collect(Collectors.toList());
    }

    private UserDTO convertToDTO(User magasinier) {
        return UserDTO.builder()
                .id(magasinier.getId())
                .nom(magasinier.getNom())
                .prenom(magasinier.getPrenom())
                .email(magasinier.getEmail())
                .tel(magasinier.getTel())
                .age(magasinier.getAge())
                .role(magasinier.getRole())
                .sexe(magasinier.getSexe())
                .dateEmbauche(magasinier.getDateEmbauche())
                .build();
    }
}
    
  */  @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/totalTechniciens")
    public ResponseEntity<Long> getTotalTechniciens() {
        long totalTechniciens = authService.getTotalTechniciens();
        return ResponseEntity.ok(totalTechniciens);
    }
  @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/totaMagasiniers")
    public ResponseEntity<Long> getTotalMagasiniers() {
        long totalMagasiniers = authService.getTotalMagasiniers();
        return ResponseEntity.ok(totalMagasiniers);
    }
  @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/totalDemandeurs")
    public ResponseEntity<Long> getTotalDemandeurs() {
        long totalDemandeurs = authService.getTotalDemandeurs();
        return ResponseEntity.ok(totalDemandeurs);
    }


  @PatchMapping("/change-password")
  public ResponseEntity<?> changePassword(
        @RequestBody ChangePasswordRequest request,
        Principal connectedUser
  ) {
	  authService.changePassword(request, connectedUser);
      return ResponseEntity.ok().build();
  }
	@PreAuthorize("hasAnyAuthority('MANAGER','ADMIN','CLIENT')")
  @RequestMapping(path="Techniciens",method = RequestMethod.GET)
  public ResponseEntity<List<UserDTO>> getAllTechniciens() {
      List<UserDTO> techniciens = authService.getAllTechniciens();
      return ResponseEntity.ok(techniciens);
  }
  
  @PreAuthorize("hasAuthority('ADMIN')")
  @RequestMapping(path="Demandeurett",method = RequestMethod.GET)
  public ResponseEntity<List<UserDTO>> getAllDemandeurs() {
      List<UserDTO> demandeurs = authService.getAllDemandeurs();
      return ResponseEntity.ok(demandeurs);
  }
  
  @PreAuthorize("hasAuthority('ADMIN')")
  @RequestMapping(path="allMagasiniers",method = RequestMethod.GET)
  public ResponseEntity<List<UserDTO>> getAllMagasiniers() {
      List<UserDTO> demandeurs = authService.getAllMagasiniers();
      return ResponseEntity.ok(demandeurs);
  }
  
  @PreAuthorize("hasAuthority('ADMIN')")
  @RequestMapping(path="allManagers",method = RequestMethod.GET)
  public ResponseEntity<List<UserDTO>> getAllManagers() {
      List<UserDTO> demandeurs = authService.getAllManagers();
      return ResponseEntity.ok(demandeurs);
  }
  
}