package com.market.ecommerce.user.service;

import com.market.ecommerce.exception.user.MultiUserException;
import com.market.ecommerce.exception.user.UserErrorCode;
import com.market.ecommerce.exception.user.UserException;
import com.market.ecommerce.user.domain.User;
import com.market.ecommerce.user.dto.SignUp;
import com.market.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.market.ecommerce.exception.user.UserErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public SignUp.SignUpResponse signUp(SignUp.SignUpRequest req) {
        List<Object[]> conflicts = userRepository.findConflictingUserInfo(
                req.getUserId(), req.getEmail(), req.getPhoneNumber()
        );

        List<UserErrorCode> errorCodes = extractConflictErrorCodes(conflicts, req);

        if (!errorCodes.isEmpty()) {
            throw new MultiUserException(errorCodes);
        }

        User user = User.from(req);
        userRepository.save(user);

        return SignUp.SignUpResponse.from(user);
    }

    private List<UserErrorCode> extractConflictErrorCodes(List<Object[]> conflicts, SignUp.SignUpRequest req) {
        List<UserErrorCode> errorCodes = new ArrayList<>();

        for (Object[] conflict : conflicts) {
            String existingUserId = (String) conflict[0];
            String existingEmail = (String) conflict[1];
            String existingPhoneNumber = (String) conflict[2];

            if (Objects.equals(existingUserId, req.getUserId())) {
                errorCodes.add(UserErrorCode.USER_ID_ALREADY_EXISTS);
            }
            if (Objects.equals(existingEmail, req.getEmail())) {
                errorCodes.add(UserErrorCode.EMAIL_ALREADY_EXISTS);
            }
            if (Objects.equals(existingPhoneNumber, req.getPhoneNumber())) {
                errorCodes.add(UserErrorCode.PHONE_NUMBER_ALREADY_EXISTS);
            }
        }

        return errorCodes;
    }
}
