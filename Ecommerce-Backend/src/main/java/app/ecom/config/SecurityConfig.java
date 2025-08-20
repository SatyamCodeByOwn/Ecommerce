package app.ecom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import app.ecom.services.CustomUserDetailsService;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    //  Password encoder for hashing and verifying user passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authentication provider that uses our CustomUserDetailsService
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Authentication manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        //  Anyone can view products
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()

                        //  Only SELLER can add, update, delete products
                        .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("SELLER")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("SELLER")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("SELLER")


                        //  Cart endpoints
                        .requestMatchers(HttpMethod.GET, "/api/carts/**").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/carts/**").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/api/carts/**").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE, "/api/carts/**").hasRole("CUSTOMER")
                        //  Allow all other requests without authentication


                        // Commission endpoints
                        .requestMatchers(HttpMethod.POST, "/api/commissions/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.GET, "/api/commissions/**").hasAnyRole("OWNER","SELLER")
                        .requestMatchers(HttpMethod.GET, "/api/commissions/owner/revenue").hasRole("OWNER")
                        // Order endpoints
                        .requestMatchers(HttpMethod.POST, "/api/orders/**").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/orders/**").hasAnyRole("CUSTOMER","SELLER","OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/orders/{id}/status").hasAnyRole("SELLER")
                        .requestMatchers(HttpMethod.DELETE, "/api/orders/**").hasRole("CUSTOMER")

                        // OrderItem endpoints
                        .requestMatchers(HttpMethod.POST, "/api/orders/users/{userId}/items").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/orders/{orderId}/items").hasAnyRole("CUSTOMER","SELLER","OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/order-items/{itemId}").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE, "/api/order-items/{itemId}").hasRole("CUSTOMER")


                        // Payment endpoints
                        .requestMatchers(HttpMethod.POST, "/api/payments").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/payments/{id}").hasAnyRole("CUSTOMER","OWNER")
                        .requestMatchers(HttpMethod.GET, "/api/payments/order/{orderId}").hasAnyRole("CUSTOMER","OWNER")

                        // Review endpoints
                        .requestMatchers(HttpMethod.POST, "/api/reviews").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/reviews/products/{productId}").permitAll()

                        // Seller endpoints
                        .requestMatchers(HttpMethod.POST, "/api/sellers").hasRole("OWNER")
                        .requestMatchers(HttpMethod.GET, "/api/sellers/{id}").hasAnyRole("OWNER","SELLER")
                        .requestMatchers(HttpMethod.GET, "/api/sellers").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/sellers/{id}").hasAnyRole("OWNER","SELLER")
                        .requestMatchers(HttpMethod.DELETE, "/api/sellers/{id}").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/sellers/{id}/reject").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/sellers/{id}/approve").hasRole("OWNER")
                        .requestMatchers(HttpMethod.GET, "/api/sellers/{sellerUserId}/revenue").hasRole("SELLER")


                        // Shipping Address endpoints
                        .requestMatchers(HttpMethod.POST, "/api/shipping-addresses").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/shipping-addresses/users/{userId}").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/shipping-addresses/{id}").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/api/shipping-addresses/{id}").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE, "/api/shipping-addresses/{id}").hasRole("CUSTOMER")

                        // User endpoints

                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("OWNER")


                        // Wishlist endpoints
                        .requestMatchers(HttpMethod.GET, "/api/wishlist/users/{userId}").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/wishlist/users/{userId}/products/{productId}").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE, "/api/wishlist/users/{userId}/products/{productId}").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE, "/api/wishlist/users/{userId}/clear").hasRole("CUSTOMER")

                        .anyRequest().permitAll()


                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(withDefaults()); // Only triggered for POST/PUT/DELETE above

        return http.build();
    }

}
