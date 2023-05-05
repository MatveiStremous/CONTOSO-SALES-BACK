package com.example.contoso.service.impl;

import com.example.contoso.dto.mapper.UserMapper;
import com.example.contoso.dto.request.user.ChangePasswordRequest;
import com.example.contoso.dto.request.user.LoginRequest;
import com.example.contoso.dto.request.user.UserRequest;
import com.example.contoso.dto.response.user.UserResponse;
import com.example.contoso.entity.User;
import com.example.contoso.exception.type.BusinessException;
import com.example.contoso.repository.UserRepository;
import com.example.contoso.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 11:26 AM
 */
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private static final String ALREADY_EXIST = "Пользователь с логином %s уже существует.";
    private static final String NOT_FOUND = "Пользователь с логином %s не найден.";

    @Override
    public void registration(UserRequest userRequest) {
        Optional<User> userFromDb = userRepository.findByLogin(userRequest.getLogin());
        if (userFromDb.isEmpty()) {
            userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            User user = userMapper.toUser(userRequest);
            userRepository.save(user);
        } else {
            throw new BusinessException(String.format(ALREADY_EXIST, userRequest.getLogin()),
                    HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = getUser(changePasswordRequest.getUserId());
        String oldPassword = user.getPassword();
        System.out.println(user.getPassword());

        if (passwordEncoder.matches(changePasswordRequest.getNewPassword(), oldPassword) ||
                changePasswordRequest.getNewPassword()
                        .equals(changePasswordRequest.getOldPassword())
        ) {
            throw new BusinessException("Старый и новый паспорт совпадают. Пожалуйста, попробуйте снова!", HttpStatus.CONFLICT);
        }
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), oldPassword)) {
            throw new BusinessException("Старый пароль введен неверно. Пожалуйста, попробуйте снова!", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public UserResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByLogin(loginRequest.getLogin())
                .orElseThrow(() -> new BusinessException(String.format(NOT_FOUND, loginRequest.getLogin()), HttpStatus.NOT_FOUND));
        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return userMapper.toResponseDto(user);
        } else {
            throw new BusinessException("Пароль некорректный!", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<UserResponse> getAllManagers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> Objects.equals(user.getRole(), User.Role.MANAGER))
                .map(userMapper::toResponseDto)
                .toList();
    }

    @Override
    public void deleteById(Integer id) {
        User user = getUser(id);
        if (Objects.equals(user.getRole(), User.Role.MANAGER)) {
            userRepository.deleteById(user.getId());
        } else {
            throw new BusinessException("Вы не можете удалить админа!", HttpStatus.FORBIDDEN);
        }
    }


    private User getUser(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(String.format(NOT_FOUND, id), HttpStatus.NOT_FOUND));
    }
}
