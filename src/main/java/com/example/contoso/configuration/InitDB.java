package com.example.contoso.configuration;

import com.example.contoso.entity.Client;
import com.example.contoso.entity.Product;
import com.example.contoso.entity.User;
import com.example.contoso.repository.ClientRepository;
import com.example.contoso.repository.ProductRepository;
import com.example.contoso.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Neevels
 * @version 1.0
 * @date 5/5/2023 12:21 PM
 */
@Component
public class InitDB {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public InitDB(UserRepository userRepository, ClientRepository clientRepository, ProductRepository productRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initDb() {
        List<User> users = List.of(User.builder()
                        .name("admin")
                        .image("1.png")
                        .login("admin@gmail.com")
                        .password(passwordEncoder.encode("admin"))
                        .phoneNumber("+375297694430")
                        .role(User.Role.ADMIN)
                        .surname("admin")
                        .build(),
                User.builder()
                        .name("Иван")
                        .image("2.png")
                        .login("manager@gmail.com")
                        .password(passwordEncoder.encode("manager"))
                        .phoneNumber("+375297694430")
                        .role(User.Role.MANAGER)
                        .surname("Иванов")
                        .build()
        );

        List<Product> products = List.of(Product.builder()
                        .price(5.0)
                        .code(123456)
                        .amount(10)
                        .reservedAmount(0)
                        .name("Ластик")
                        .build(),
                Product.builder()
                        .price(12.0)
                        .code(123455)
                        .amount(20)
                        .reservedAmount(0)
                        .name("Ручка")
                        .build(),
                Product.builder()
                        .price(6.0)
                        .code(123445)
                        .amount(10)
                        .reservedAmount(0)
                        .name("Карандаш")
                        .build(),
                Product.builder()
                        .price(5.0)
                        .code(654321)
                        .amount(10)
                        .reservedAmount(0)
                        .name("Линейка")
                        .build(),
                Product.builder()
                        .price(140.0)
                        .code(11111)
                        .amount(40)
                        .reservedAmount(0)
                        .name("Пенал")
                        .build()
        );

        List<Client> clients = List.of(Client.builder()
                        .name("Sanechka")
                        .phoneNumber("+375293621343")
                        .email("sasha987adamonis@gmail.com")
                        .dateOfRegistration(LocalDateTime.now())
                        .address("Радунский проспект д.21 кв 7")
                        .build(),
                Client.builder()
                        .name("Matthew")
                        .phoneNumber("+375299648938")
                        .email("stremousik@mail.ru")
                        .dateOfRegistration(LocalDateTime.now())
                        .address("Радунский проспект д.22 кв 8")
                        .build(),
                Client.builder()
                        .name("Ilyha")
                        .phoneNumber("+375297654321")
                        .email("neevels@mail.ru")
                        .dateOfRegistration(LocalDateTime.now())
                        .address("Радунский проспект д.20 кв 6")
                        .build());

        productRepository.saveAll(products);
        userRepository.saveAll(users);
        clientRepository.saveAll(clients);
    }
}
