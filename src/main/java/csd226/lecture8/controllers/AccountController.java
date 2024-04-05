package csd226.lecture8.controllers;

import csd226.lecture8.data.Account;
import csd226.lecture8.repositories.AccountRepository;
import csd226.lecture8.security.JwtTokenUtil;
import csd226.lecture8.security.RefreshTokenUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import csd226.lecture8.data.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Map;
import csd226.lecture8.data.AuthResponse;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JwtTokenUtil jwtUtil;

    @Autowired
    RefreshTokenUtil refreshTokenUtil;

    private final AccessToken token;


    public AccountController(AccessToken accessToken) {
        this.token = accessToken;
    }


//        @PostMapping("/test_form")
//    public String test_form(@ModelAttribute Account account, Model model) {
//        model.addAttribute("email", account);
//        return "result";
//    }



// this is the one working
//    @PostMapping(path="/auth/login")
//    public ResponseEntity<?> login(@ModelAttribute Account acc, Model model) {
//        String refreshToken = "your_refresh_token_value_here";
//
//        try {
//            Authentication authentication = authManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            acc.getEmail(), acc.getPassword())
//            );
//
//            Account account = (Account) authentication.getPrincipal();
//            String accessToken = jwtUtil.generateAccessToken(account);
//            System.out.println("AccessToken: "+accessToken);
////            model.addAttribute("token", accessToken);
//            token.setAccessToken(accessToken);
//
//            // AuthResponse response = new AuthResponse(account.getEmail(), accessToken);
//            AuthResponse response = new AuthResponse(account.getEmail(), accessToken, refreshToken);
//
//            return ResponseEntity.ok().body(response+"<script>alert('setting var \"accessToken\"');var accessToken='"+accessToken+"'</script>");
//
//        } catch( Exception ex) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }




// this is sir freds code
//
//    @PostMapping(path="/auth/login")
//    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
//        // String refreshToken = "your_refresh_token_value_here";
//
//        try {
//            Authentication authentication = authManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            acc.getEmail(), acc.getPassword())
//            );
//
//            Account account = (Account) authentication.getPrincipal();
//            String accessToken = jwtUtil.generateAccessToken(account);
//            RefreshToken refreshToken = refreshTokenUtil.createRefreshToken(account.getId());
//
//
//            System.out.println("AccessToken: "+accessToken);
////            model.addAttribute("token", accessToken);
//            token.setAccessToken(accessToken);
//
//            // AuthResponse response = new AuthResponse(account.getEmail(), accessToken);
//            // AuthResponse response = new AuthResponse(account.getEmail(), accessToken, refreshToken);
//            AuthResponse response = new AuthResponse(account.getEmail(), accessToken, refreshToken.getToken());
//            return ResponseEntity.ok().body(response);
//
//
//
//
//            //return ResponseEntity.ok().body(response+"<script>alert('setting var \"accessToken\"');var accessToken='"+accessToken+"'</script>");
//
//        } catch( Exception ex) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }


    // this is generated from GPT to fix errors

    @PostMapping(path="/auth/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword())
            );

            Account account = (Account) authentication.getPrincipal();
            String accessToken = jwtUtil.generateAccessToken(account);
            // Assuming refreshTokenUtil is a valid utility class
            RefreshToken refreshToken = refreshTokenUtil.createRefreshToken(account.getId());

            System.out.println("AccessToken: "+accessToken);
            token.setAccessToken(accessToken);

            AuthResponse response = new AuthResponse(account.getEmail(), accessToken, refreshToken.getToken());
            return ResponseEntity.ok().body(response);

        } catch( Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }



    // this is sir freds code

    @PostMapping("/auth/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        try {
            RefreshToken refreshToken = refreshTokenUtil.findByToken(requestRefreshToken);

            if (refreshToken != null) {
                if (refreshTokenUtil.verifyExpiration(refreshToken)) {
                    Account account = accountRepository.getById(refreshToken.getAccount().getId());
                    String token = jwtUtil.generateAccessToken(account);
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                } else {
                    throw new Exception("RefreshTokenExpired");
                }
            }
        } catch(Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    @GetMapping("/signin")
    public ResponseEntity<String> getSignin(){ // map a URL to a method
        String s="" +
                "<form hx-post=\"/auth/login\" hx-target=\"this\" hx-swap=\"outerHTML\">" +
                "<h1>Sign in</h1>" +
                "    <div class=\"form-group\">" +
                "        <label>Username (your email)</label>" +
                "        <input type=\"email\" name=\"email\" value=\"fred.carella@gmail.com\">" +
                "    </div>" +
                "    <div class=\"form-group\">" +
                "        <label>Password</label>" +
                "        <input type=\"password\" name=\"password\" value=\"fredspassword\">" +
                "    </div>" +
                "    <button class=\"btn\">Submit</button>" +
                "    <button class=\"btn\" hx-get=\"/signin\">Cancel</button>" +
                "    <div class=\"form-group\">" +
                "        <p>Dont have an account?   Create one by signing up.</p>" +
                "        <a href=\"#\" hx-get=\"/signup\" hx-target=\"#home\"><span class=\"glyphicon glyphicon-log-in\"></span> Create an account</a>" +
                "    </div>" +
                "</form>";
        return ResponseEntity.ok(s);
    }
    @GetMapping("/signup")
    public ResponseEntity<String> getSignUp(){ // map a URL to a method
        String s="" +
                "<form hx-post=\"/createAccount\" hx-target=\"this\" hx-swap=\"outerHTML\">" +
                "<h1>Create an Account</h1>" +
                "    <div>" +
                "        <label>First Name</label>" +
                "        <input type=\"text\" name=\"firstname\" value=\"Fred\">" +
                "    </div>" +
                "    <div class=\"form-group\">" +
                "        <label>Last Name</label>" +
                "        <input type=\"text\" name=\"lastname\" value=\"Carella\">" +
                "    </div>" +
                "    <div class=\"form-group\">" +
                "        <label>Email Address</label>" +
                "        <input type=\"email\" name=\"email\" value=\"fred.carella@gmail.com\">" +
                "    </div>" +
                "    <div class=\"form-group\">" +
                "        <label>Password</label>" +
                "        <input type=\"password\" name=\"password\" value=\"fredspassword\">" +
                "    </div>" +
                "    <div class=\"form-group\">" +
                "        <label>Confirm Password</label>" +
                "        <input type=\"password\" name=\"confirmPassword\" value=\"fredspassword\">" +
                "    </div>" +
                "    <button class=\"btn\">Submit</button>" +
                "    <button class=\"btn\" hx-get=\"/signin\">Cancel</button>" +
                "</form>";
        return ResponseEntity.ok(s);
    }


    @PostMapping("/createAccount")
//    public ResponseEntity<String> createAccount(@RequestBody Account signUpFormData) {
    public ResponseEntity<?> createAccount(@ModelAttribute Account signUpFormData) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(signUpFormData.getPassword());

        signUpFormData.setPassword(password);

        Account savedAccount = accountRepository.save(signUpFormData);

        return ResponseEntity.ok("createAccount(): " + signUpFormData.getEmail());
    }

// methods are protected in the csd226.lecture8.security.ApplicationSecurity class
@GetMapping("/protectedPage")
public ResponseEntity<String> getProtectedPage(Model model){ // map a URL to a method
    String s="" +
            "<h1>Protected page</h1>";
    return ResponseEntity.ok(s);
}
    @GetMapping("/unProtectedPage")
    public ResponseEntity<String> getUnProtectedPage(@RequestHeader Map<String, String> headers) { // map a URL to a method

        ArrayList<String> h=new ArrayList<>();
        headers.forEach((key, value) -> {
            h.add(String.format("Header '%s' = %s", key, value));
        });
        String s="" +
                "<h1>Un Protected page</h1>";
        return ResponseEntity.ok(s);
    }

}
