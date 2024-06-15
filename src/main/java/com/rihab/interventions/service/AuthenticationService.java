package com.rihab.interventions.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.rihab.interventions.dto.UserDTO;
import com.rihab.interventions.entities.AuthenticationResponse;
import com.rihab.interventions.entities.Client;
import com.rihab.interventions.entities.Demandeur;
import com.rihab.interventions.entities.Departement;
import com.rihab.interventions.entities.Image;
import com.rihab.interventions.entities.Magasinier;
import com.rihab.interventions.entities.Manager;
import com.rihab.interventions.entities.Role;
import com.rihab.interventions.entities.Technicien;
import com.rihab.interventions.entities.User;
import com.rihab.interventions.register.ChangePasswordRequest;
import com.rihab.interventions.repos.ClientRepository;
import com.rihab.interventions.repos.DemandeurRepository;
import com.rihab.interventions.repos.DepartementRepository;
import com.rihab.interventions.repos.MagasinierRepository;
import com.rihab.interventions.repos.ManagerRepository;
import com.rihab.interventions.repos.TechnicienRepository;
import com.rihab.interventions.repos.UserRepository;
import com.rihab.interventions.util.EmailSender;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailSender emailSender;
    private final UserDetailsService userDetailsService;
    private final DemandeurRepository demandeurRepository;
    private final TechnicienRepository technicienRepository;
    //private final TokenRepository tokenRepository;
    private final ManagerRepository managerRepository;
    private final AuthenticationManager authenticationManager;
    private final MagasinierRepository magasinierRepository;
    private final DepartementRepository departementRepository;
    private final ClientRepository clientRepository;
    private final ImageService imageService;
    
    
    
    public AuthenticationResponse register(UserDTO request) {
        try {

            // Vérifier que les champs requis ne sont pas null ou vides
            if (request.getSexe() == null || request.getDateNaiss() == null || 
                (request.getRole() == Role.TECHNICIEN && request.getDateEmbauche() == null)) {
                return new AuthenticationResponse(null, "Les champs sexe, date de naissance, et date d'embauche (pour technicien) sont requis", null);
            }

            // Vérifier si l'utilisateur existe déjà
            if (repository.findByUsername(request.getUsername()).isPresent()) {
                return new AuthenticationResponse(null, "L'utilisateur existe déjà", null);
            }

            // Générer un nom d'utilisateur automatiquement
            String login = "user" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);

            // Générer un mot de passe sécurisé
            String password = generateSecurePassword();
            
            User user = new User();
            user.setNom(request.getNom());
            user.setPrenom(request.getPrenom());
            user.setUsername(login); // Utiliser le nom d'utilisateur généré automatiquement
            user.setPassword(passwordEncoder.encode(password)); // Encodage du mot de passe sécurisé
            user.setEmail(request.getEmail());
            user.setSexe(request.getSexe());
            user.setDateNaiss(request.getDateNaiss());
            user.setCin(request.getCin());
          
            user.setRole(request.getRole());

            // Enregistrer l'utilisateur dans la base de données
            user = repository.save(user);
           
            // Récupérer le rôle de l'utilisateur
            Role role = user.getRole();
            System.out.println("role"+role);
            	 if(role.equals(Role.CLIENT)) {
            		 Demandeur demandeur=new Demandeur();
            		 demandeur.setPost(request.getPost());
            		 demandeur.setUser(user);
            		 
            		 demandeur.setClient(Client.builder().codeClient(request.getCodeClient()).build());
            		 demandeurRepository.save(demandeur);
            		 user.setDemandeur(demandeur);
            		  repository.save(user);
            	 }
            	 else if(role.equals(Role.TECHNICIEN)) {
            		 Technicien technicien=new Technicien();
            		 technicien.setDateEmbauche(request.getDateEmbauche());
            		 technicien.setMatricule(request.getMatricule());
            		 technicien.setResponsable(request.getResponsable());
            		 technicien.setInternet(request.getInternet());
            		 technicien.setNumeroAbrege(request.getNumeroAbrege());
            		
            		 technicien.setUser(user);
            		 technicien.setDepartement(Departement.builder().codeDepart(request.getCodeDepart()).build());
            		
					technicienRepository.save(technicien);
            		 user.setTechnicien(technicien);
            		  repository.save(user);
            	 }
            	
            	 else if(role.equals(Role.MANAGER)) {
            		 Manager manager=new Manager();
            		
            		 manager.setUser(user);
            		
            		
					managerRepository.save(manager);
            		 user.setManager(manager);
            		  repository.save(user);
            	 }

            	 else if(role.equals(Role.MAGASINIER)) {
            		 Magasinier magasinier=new Magasinier();
            		
            		 magasinier.setUser(user);
            		
            		
            		 magasinierRepository.save(magasinier);
            		 user.setMagasinier(magasinier);
            		  repository.save(user);
            	 }

            	 
            // Générer le token JWT en tenant compte du rôle de l'utilisateur
            String jwt = jwtService.generateTokenWithRole(user, role);

            // Construire le contenu de l'e-mail
            StringBuilder emailContent = new StringBuilder();
            emailContent.append("Bonjour ").append(user.getNom()).append(" ").append(user.getPrenom()).append(",\n\n");
            emailContent.append("Votre compte a été créé avec succès.\n");
            emailContent.append("Voici vos informations d'inscription :\n");
            emailContent.append("Nom d'utilisateur : ").append(login).append("\n");
            emailContent.append("Mot de passe : ").append(password).append("\n\n");
            emailContent.append("Merci de votre inscription.");

            // Envoyer l'e-mail
            emailSender.sendEmail(user.getEmail(), emailContent.toString());
     
            return new AuthenticationResponse(jwt, "Inscription de l'utilisateur réussie", role);
              
        } catch (Exception e) {
            return new AuthenticationResponse(null, "Erreur lors de l'enregistrement de l'utilisateur : " + e.getMessage(), null);
        } 
    }


    
    private String generateSecurePassword() {
	// TODO Auto-generated method stub
    	 return RandomStringUtils.randomAlphanumeric(10);
}/*
    public AuthenticationResponse authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password
                    )
            );

            User user = repository.findByUsername(username).orElseThrow();
            String jwt = jwtService.generateToken(user);

            return new AuthenticationResponse(jwt, "User login was successful");
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("Invalid credentials");
        }
    }
 
*/
   /* 
    public AuthenticationResponse authenticate(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = repository.findByUsername(request.getUsername()).orElseThrow();
        String jwt = jwtService.generateToken(user);

      
        return new AuthenticationResponse(jwt, "User login was successful");

    }
    */
    
    public AuthenticationResponse authenticate(UserDTO request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
            );

            // Récupérer les détails de l'utilisateur après l'authentification
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

            // Récupérer le rôle de l'utilisateur
            Role role = ((User) userDetails).getRole();

            // Générer le token JWT en tenant compte du rôle de l'utilisateur
            String jwt = jwtService.generateTokenWithRole(userDetails, role);

            // Utiliser le constructeur mis à jour avec le rôle
            return new AuthenticationResponse(jwt, "User login was successful", role);
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("Invalid credentials");
        }
    }

    
    /*
	public AuthenticationResponse authenticate(String login, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            login,
                            password
                    )
            );

            User user = repository.findByUsername(login).orElseThrow();
            String jwt = jwtService.generateToken(user);

            return new AuthenticationResponse(jwt, "User login was successful");
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("Invalid credentials");
        }
    }
    */
    
    
	/*
    public AuthenticationResponse register(User request) {

        // check if user already exist. if exist than authenticate the user
        if(repository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthenticationResponse(null, "User already exist");
        }

        User user = new User();
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setUsername(UUID.randomUUID().toString());
      
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setEmail(request.getEmail());

        user.setRole(request.getRole());

        user = repository.save(user);
        // Envoyer les informations par e-mail
       // envoyerEmail(user);
        
        String jwt = jwtService.generateToken(user);

       // saveUserToken(jwt, user);

        return new AuthenticationResponse(jwt, "User registration was successful");

    }

  */
    /*// hedhya loula mt3 email:
    public AuthenticationResponse register(User request) {

        // Vérifier si l'utilisateur existe déjà
        if (repository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthenticationResponse(null, "L'utilisateur existe déjà");
        }

        // Générer un nom d'utilisateur automatiquement
        String login = "user" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);

        // Générer un mot de passe sécurisé
        String password = generateSecurePassword();

        User user = new User();
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setUsername(login); // Utiliser le nom d'utilisateur généré automatiquement
        user.setPassword(passwordEncoder.encode(password)); // Encodage du mot de passe sécurisé
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());

        // Enregistrer l'utilisateur dans la base de données
        try {
            user = repository.save(user);
        } catch (Exception e) {
            return new AuthenticationResponse(null, "Erreur lors de l'enregistrement de l'utilisateur");
        }

        // Construire le contenu de l'e-mail
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("Bonjour ").append(user.getNom()).append(" ").append(user.getPrenom()).append(",\n\n");
        emailContent.append("Votre compte a été créé avec succès.\n");
        emailContent.append("Voici vos informations d'inscription :\n");
        emailContent.append("Nom d'utilisateur : ").append(login).append("\n");
        emailContent.append("Mot de passe : ").append(password).append("\n\n");
        emailContent.append("Merci de votre inscription.");

        // Envoyer l'e-mail
        emailSender.sendEmail(user.getEmail(), emailContent.toString());

        // Générer le token JWT
        String jwt = jwtService.generateToken(user);

        return new AuthenticationResponse(jwt, "Inscription de l'utilisateur réussie");
    }
/*
    // Méthode pour générer un mot de passe sécurisé
    private String generateSecurePassword() {
        // Utilisez une bibliothèque pour générer un mot de passe sécurisé, par exemple, RandomStringUtils de Apache Commons
        // Ici, je vais utiliser une chaîne aléatoire simple pour illustrer.
        return RandomStringUtils.randomAlphanumeric(10); // Génère une chaîne alphanumérique de 10 caractères
    }

  /*  
    public AuthenticationResponse authenticate(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = repository.findByUsername(request.getUsername()).orElseThrow();
        String jwt = jwtService.generateToken(user);

      //  revokeAllTokenByUser(user);
        //saveUserToken(jwt, user);

        return new AuthenticationResponse(jwt, "User login was successful");

    }*/
    
    
    /*
    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllTokensByUser(user.getId());
        if(validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t-> {
            t.setLoggedOut(true);
        });

        tokenRepository.saveAll(validTokens);
    }
    */
    /*
    private void saveUserToken(String jwt, User user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }*/
    
  
 // Importez les classes nécessaires
    public List<UserDTO> getAllUsers() {
        List<User> users = repository.findAll();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
            .id(user.getId())
            .nom(user.getNom())
            .prenom(user.getPrenom())
            .email(user.getEmail())
            .tel(user.getTel())
            .age(user.getAge())
            .role(user.getRole())
            .sexe(user.getSexe())
            .dateNaiss(user.getDateNaiss())
            .username(user.getUsername())
            //.codeDemandeur(user.getDemandeur() != null ? user.getDemandeur().getCodeDemandeur() : null)
            //.codeTechnicien(user.getTechnicien() != null ? user.getTechnicien().getCodeTechnicien() : null)
            //.codeManager(user.getManager() != null ? user.getManager().getCodeManager() : null)
            //.codeMagasinier(user.getMagasinier() != null ? user.getMagasinier().getCodeMagasinier() : null)
            
            .build();
    }

    // Méthode pour supprimer un utilisateur par son identifiant
    @Transactional
    public void deleteUser(Long userId) {
        repository.deleteById(userId);
    }
    
    
    
    public List<UserDTO> getAllManagers() {
        List<User> managers = repository.findByRole(Role.MANAGER); // Utilisation de l'énumération Role.MANAGER pour rechercher les utilisateurs avec le rôle de manager
        List<UserDTO> managerDTOs = new ArrayList<>();

        for (User manager : managers) {
            UserDTO managerDTO = UserDTO.builder()
                .id(manager.getId())
                .codeManager(manager.getManager().getCodeManager())
                .nom(manager.getNom())
                .prenom(manager.getPrenom())
                .email(manager.getEmail())
                .tel(manager.getTel())
                .age(manager.getAge())
                .role(manager.getRole())
                .sexe(manager.getSexe())
                .dateNaiss(manager.getDateNaiss())
                .cin(manager.getCin())
                .imageId(Optional.ofNullable(manager.getImage()).map(Image::getIdImage).orElse(null))
              
                .build();
            managerDTOs.add(managerDTO);
        }
        return managerDTOs;
    }

    public List<UserDTO> getAllTechniciens() {
        List<User> techniciens = repository.findByRole(Role.TECHNICIEN);
        List<UserDTO> technicienDTOs = new ArrayList<>();

        for (User technicien : techniciens) {
            UserDTO technicienDTO = UserDTO.builder()
                .id(technicien.getId())
                .codeTechnicien(technicien.getTechnicien().getCodeTechnicien())
                .nom(technicien.getNom())
                .prenom(technicien.getPrenom())
                .email(technicien.getEmail())
                .tel(technicien.getTel())
                .age(technicien.getAge())
                .role(technicien.getRole())
                .sexe(technicien.getSexe())
                .cin(technicien.getCin())
                .dateEmbauche(technicien.getTechnicien().getDateEmbauche())
                .responsable(technicien.getTechnicien().getResponsable())
                .dateNaiss(technicien.getDateNaiss())
                .codeDepart(technicien.getTechnicien() != null && technicien.getTechnicien().getDepartement() != null ? technicien.getTechnicien().getDepartement().getCodeDepart() : null)
                .nomDepart(technicien.getTechnicien() != null && technicien.getTechnicien().getDepartement() != null ? technicien.getTechnicien().getDepartement().getNomDepart() : null)
                .imageId(Optional.ofNullable(technicien.getImage()).map(Image::getIdImage).orElse(null))
               //.imageUrl(technicien.getImage() != null ? "/" + technicien.getImage().getIdImage() : null)
                .build();
            technicienDTOs.add(technicienDTO);
        }
        return technicienDTOs;
    }

    public List<UserDTO> getAllDemandeurs() {
        List<User> demandeurs = repository.findByRole(Role.CLIENT);
        List<UserDTO> demandeurDTOs = new ArrayList<>();

        for (User demandeur : demandeurs) {
            UserDTO demandeurDTO = UserDTO.builder()
                .id(demandeur.getId())
                .codeDemandeur(demandeur.getDemandeur() != null ? demandeur.getDemandeur().getCodeDemandeur() : null)
                .nom(demandeur.getNom())
                .prenom(demandeur.getPrenom())
                .email(demandeur.getEmail())
                .tel(demandeur.getTel())
                .age(demandeur.getAge())
                .role(demandeur.getRole())
                .sexe(demandeur.getSexe())
                .cin(demandeur.getCin())
                .dateNaiss(demandeur.getDateNaiss())
                .post(demandeur.getDemandeur() != null ? demandeur.getDemandeur().getPost() : null)
                .codeClient(demandeur.getDemandeur() != null && demandeur.getDemandeur().getClient() != null ? demandeur.getDemandeur().getClient().getCodeClient() : null)
                .nomSociete(demandeur.getDemandeur() != null && demandeur.getDemandeur().getClient() != null ? demandeur.getDemandeur().getClient().getNomSociete() : null)
                .imageId(Optional.ofNullable(demandeur.getImage()).map(Image::getIdImage).orElse(null))
               // .imageUrl(demandeur.getImage() != null ? "/api/images/" + demandeur.getImage().getIdImage() : null)
                .build();
            demandeurDTOs.add(demandeurDTO);
        }
        return demandeurDTOs;
    }


  
    
    public List<UserDTO> getAllMagasiniers() {
        List<User> magasiniers = repository.findByRole(Role.MAGASINIER); // Utilisation de l'énumération Role.MANAGER pour rechercher les utilisateurs avec le rôle de manager
        List<UserDTO> magasinierDTOs = new ArrayList<>();

        for (User magasinier : magasiniers) {
            UserDTO magasinierDTO = UserDTO.builder()
                .id(magasinier.getId())
                .codeMagasinier(magasinier.getMagasinier().getCodeMagasinier())
                .nom(magasinier.getNom())
                .prenom(magasinier.getPrenom())
                .email(magasinier.getEmail())
                .tel(magasinier.getTel())
                .age(magasinier.getAge())
                .role(magasinier.getRole())
                .sexe(magasinier.getSexe())
                .imageId(Optional.ofNullable(magasinier.getImage()).map(Image::getIdImage).orElse(null))
         .dateNaiss(magasinier.getDateNaiss())
         .cin(magasinier.getCin())    
         
         .build();
            magasinierDTOs.add(magasinierDTO);
        }
        return magasinierDTOs;
    }
    
   
    /*
    public UserDTO updateProfile(UserDTO request, MultipartFile imageFile) {
        try {
            // Récupérer l'utilisateur actuel à partir du contexte de sécurité
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            User user = repository.findByUsername(currentUsername)
                    .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

          

            // Mettre à jour les informations de base de l'utilisateur
            user.setNom(request.getNom());
            user.setPrenom(request.getPrenom());
            user.setEmail(request.getEmail());
            user.setTel(request.getTel());
            user.setAge(request.getAge());
            user.setSexe(request.getSexe());
            user.setUsername(request.getUsername());
            user.setDateEmbauche(request.getDateEmbauche());

            // Mettre à jour les informations spécifiques au rôle
            switch (user.getRole()) {
                case CLIENT:
                    Demandeur demandeur = user.getDemandeur();
                    demandeur.setPost(request.getPost());
                    Client client = clientRepository.findById(request.getCodeClient())
                            .orElseThrow(() -> new EntityNotFoundException("Client non trouvé"));
                    demandeur.setClient(client);
                    demandeurRepository.save(demandeur);
                    break;
                case TECHNICIEN:
                    Technicien technicien = user.getTechnicien();
                    technicien.setMatricule(request.getMatricule());
                    technicien.setResponsable(request.getResponsable());
                    technicien.setInternet(request.getInternet());
                    technicien.setNumeroAbrege(request.getNumeroAbrege());
                    Departement departement = departementRepository.findById(request.getCodeDepart())
                            .orElseThrow(() -> new EntityNotFoundException("Departement non trouvé"));
                    technicien.setDepartement(departement);
                    technicienRepository.save(technicien);
                    break;
                case MANAGER:
                    // Aucun champ spécifique à mettre à jour pour Manager
                    break;
                case MAGASINIER:
                    // Aucun champ spécifique à mettre à jour pour Magasinier
                    break;
                default:
                    throw new IllegalArgumentException("Rôle non supporté : " + user.getRole());
            }

            // Mettre à jour l'image de l'utilisateur s'il y en a une
            if (imageFile != null && !imageFile.isEmpty()) {
                Image newImage = imageService.uplaodImage(imageFile);
                user.setImage(newImage);
            }

            // Enregistrer les modifications dans la base de données
            repository.save(user);

            // Créer et retourner un UserDTO mis à jour
            UserDTO updatedUser = new UserDTO();
            updatedUser.setId(user.getId());
            updatedUser.setUsername(user.getUsername());
            updatedUser.setNom(user.getNom());
            updatedUser.setPrenom(user.getPrenom());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setTel(user.getTel());
            updatedUser.setAge(user.getAge());
            updatedUser.setSexe(user.getSexe());
            updatedUser.setDateEmbauche(user.getDateEmbauche());
           
            
            // Ajouter les informations de l'image
            if (user.getImage() != null) {
                updatedUser.setImageId(user.getImage().getIdImage());
                updatedUser.setImagePath("/api/image/load/" + user.getImage().getIdImage());
            }

            switch (user.getRole()) {
                case CLIENT:
                    updatedUser.setPost(user.getDemandeur().getPost());
                    updatedUser.setCodeClient(user.getDemandeur().getClient().getCodeClient());
                    break;
                case TECHNICIEN:
                    updatedUser.setMatricule(user.getTechnicien().getMatricule());
                    updatedUser.setResponsable(user.getTechnicien().getResponsable());
                    updatedUser.setInternet(user.getTechnicien().getInternet());
                    updatedUser.setNumeroAbrege(user.getTechnicien().getNumeroAbrege());
                    updatedUser.setCodeDepart(user.getTechnicien().getDepartement().getCodeDepart());
                    break;
                case MANAGER:
                    // Aucun champ spécifique à ajouter pour Manager
                    break;
                case MAGASINIER:
                    // Aucun champ spécifique à ajouter pour Magasinier
                    break;
                default:
                    throw new IllegalArgumentException("Rôle non supporté : " + user.getRole());
            }

            return updatedUser;

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du profil : " + e.getMessage(), e);
        }
    
}
*/
    public UserDTO updateProfile(UserDTO request, MultipartFile imageFile) {
        try {
            // Récupérer l'utilisateur actuel à partir du contexte de sécurité
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            User user = repository.findByUsername(currentUsername)
                    .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

            // Indicateur pour savoir si le nom d'utilisateur a changé
            boolean isUsernameChanged = false;

            // Mettre à jour les informations de base de l'utilisateur si elles sont présentes
            if (request.getNom() != null) {
                user.setNom(request.getNom());
            }
            if (request.getPrenom() != null) {
                user.setPrenom(request.getPrenom());
            }
            if (request.getEmail() != null) {
                user.setEmail(request.getEmail());
            }
            if (request.getTel() != null) {
                user.setTel(request.getTel());
            }
            if (request.getAge() != 0) { // Vérifiez si l'âge est fourni
                user.setAge(request.getAge());
            }
            if (request.getSexe() != null) {
                user.setSexe(request.getSexe());
            }
            if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
                user.setUsername(request.getUsername());
                isUsernameChanged = true;
            }

            // Mettre à jour les informations spécifiques au rôle
            switch (user.getRole()) {
                case CLIENT:
                    Demandeur demandeur = user.getDemandeur();
                    if (request.getPost() != null) {
                        demandeur.setPost(request.getPost());
                    }
                    if (request.getCodeClient() != 0) {
                        Client client = clientRepository.findById(request.getCodeClient())
                                .orElseThrow(() -> new EntityNotFoundException("Client non trouvé"));
                        demandeur.setClient(client);
                    }
                    demandeurRepository.save(demandeur);
                    break;
                case TECHNICIEN:
                    Technicien technicien = user.getTechnicien();
                    if (request.getDateEmbauche() != null) {
                        technicien.setDateEmbauche(request.getDateEmbauche());
                    }
                    if (request.getMatricule() != null) {
                        technicien.setMatricule(request.getMatricule());
                    }
                    if (request.getResponsable() != null) {
                        technicien.setResponsable(request.getResponsable());
                    }
                    if (request.getInternet() != null) {
                        technicien.setInternet(request.getInternet());
                    }
                    if (request.getNumeroAbrege() != null) {
                        technicien.setNumeroAbrege(request.getNumeroAbrege());
                    }
                    if (request.getCodeDepart() != 0) {
                        Departement departement = departementRepository.findById(request.getCodeDepart())
                                .orElseThrow(() -> new EntityNotFoundException("Departement non trouvé"));
                        technicien.setDepartement(departement);
                    }
                    technicienRepository.save(technicien);
                    break;
                case MANAGER:
                    // Aucun champ spécifique à mettre à jour pour Manager
                    break;
                case MAGASINIER:
                    // Aucun champ spécifique à mettre à jour pour Magasinier
                    break;
                default:
                    throw new IllegalArgumentException("Rôle non supporté : " + user.getRole());
            }

            // Mettre à jour l'image de l'utilisateur s'il y en a une
            if (imageFile != null && !imageFile.isEmpty()) {
                Image newImage = imageService.uploadImage(imageFile);
                if (user.getImage() != null) {
                    imageService.deleteImage(user.getImage().getIdImage());
                }
                user.setImage(newImage);
            }

            // Enregistrer les modifications dans la base de données
            repository.save(user);

            // Créer et retourner un UserDTO mis à jour
            UserDTO updatedUser = new UserDTO();
            updatedUser.setId(user.getId());
            updatedUser.setUsername(user.getUsername());
            updatedUser.setNom(user.getNom());
            updatedUser.setPrenom(user.getPrenom());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setTel(user.getTel());
            updatedUser.setAge(user.getAge());
            updatedUser.setSexe(user.getSexe());
            updatedUser.setDateNaiss(user.getDateNaiss());

            if (user.getImage() != null) {
                updatedUser.setImageId(user.getImage().getIdImage());
                updatedUser.setImagePath("/api/image/load/" + user.getImage().getIdImage());
            }

            switch (user.getRole()) {
                case CLIENT:
                    updatedUser.setPost(user.getDemandeur().getPost());
                    updatedUser.setCodeClient(user.getDemandeur().getClient().getCodeClient());
                    break;
                case TECHNICIEN:
                    updatedUser.setDateEmbauche(user.getTechnicien().getDateEmbauche());
                    updatedUser.setMatricule(user.getTechnicien().getMatricule());
                    updatedUser.setResponsable(user.getTechnicien().getResponsable());
                    updatedUser.setInternet(user.getTechnicien().getInternet());
                    updatedUser.setNumeroAbrege(user.getTechnicien().getNumeroAbrege());
                    updatedUser.setCodeDepart(user.getTechnicien().getDepartement().getCodeDepart());
                    break;
                case MANAGER:
                    // Aucun champ spécifique à ajouter pour Manager
                    break;
                case MAGASINIER:
                    // Aucun champ spécifique à ajouter pour Magasinier
                    break;
                default:
                    throw new IllegalArgumentException("Rôle non supporté : " + user.getRole());
            }

            if (isUsernameChanged) {
                String newToken = jwtService.generateToken(user);
                updatedUser.setToken(newToken);
                System.out.println("New token generated: " + newToken);
            }
            return updatedUser;

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du profil : " + e.getMessage(), e);
        }
    }
    
    // ...
public UserDTO getCurrentUserDetails() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    User user = repository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    UserDTO currentUser = new UserDTO();
    currentUser.setId(user.getId());
    currentUser.setUsername(user.getUsername());
    currentUser.setNom(user.getNom());
    currentUser.setPrenom(user.getPrenom());
    currentUser.setEmail(user.getEmail());
    currentUser.setTel(user.getTel());
    currentUser.setAge(user.getAge());
    currentUser.setSexe(user.getSexe());
    currentUser.setDateNaiss(user.getDateNaiss());
    currentUser.setRole(user.getRole());
    currentUser.setCin(user.getCin());

    // Vérifie le rôle de l'utilisateur et ajoute les champs spécifiques
    if (user.getRole() == Role.CLIENT) {
        currentUser.setPost(user.getDemandeur().getPost());
        currentUser.setCodeClient(user.getDemandeur().getClient().getCodeClient());
        currentUser.setNomSociete(user.getDemandeur().getClient().getNomSociete());
    } else if (user.getRole() == Role.TECHNICIEN) {
    	currentUser.setDateEmbauche(user.getTechnicien().getDateEmbauche());
        currentUser.setMatricule(user.getTechnicien().getMatricule());
        currentUser.setResponsable(user.getTechnicien().getResponsable());
        currentUser.setInternet(user.getTechnicien().getInternet());
        currentUser.setNumeroAbrege(user.getTechnicien().getNumeroAbrege());
        currentUser.setNomDepart(user.getTechnicien().getDepartement().getNomDepart());
    } else if (user.getRole() == Role.MANAGER) {
        // Ajoutez ici les champs spécifiques au rôle de manager si nécessaire
    } else if (user.getRole() == Role.MAGASINIER) {
        // Ajoutez ici les champs spécifiques au rôle de magasinier si nécessaire
    }

    // Vérifie s'il y a une image associée à l'utilisateur
    if (user.getImage() != null) {
        currentUser.setImageId(user.getImage().getIdImage());
        currentUser.setImagePath("/api/image/load/" + user.getImage().getIdImage()); // Chemin de l'image
    }

    return currentUser;
}

    public long getTotalTechniciens() {
        return repository.countByRole(Role.TECHNICIEN);
    }

    public long getTotalMagasiniers() {
        return repository.countByRole(Role.MAGASINIER);
    }

    public long getTotalDemandeurs() {
        return repository.countByRole(Role.CLIENT);
    }

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);
    }
}
