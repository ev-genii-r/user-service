package com.innowise.rudkovskii.dto.user;

import com.innowise.rudkovskii.dto.user.request.UserCreateRequest;
import com.innowise.rudkovskii.dto.user.request.UserUpdateRequest;
import com.innowise.rudkovskii.dto.user.response.UserResponse;
import com.innowise.rudkovskii.dto.user.response.UserWithCardsResponse;
import com.innowise.rudkovskii.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-30T16:05:15+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponse userToUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse userResponse = new UserResponse();

        return userResponse;
    }

    @Override
    public UserWithCardsResponse userToUserWithCardsResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserWithCardsResponse userWithCardsResponse = new UserWithCardsResponse();

        return userWithCardsResponse;
    }

    @Override
    public User toUser(UserCreateRequest userCreateRequest) {
        if ( userCreateRequest == null ) {
            return null;
        }

        User user = new User();

        return user;
    }

    @Override
    public User toUser(UserUpdateRequest userUpdateRequest) {
        if ( userUpdateRequest == null ) {
            return null;
        }

        User user = new User();

        return user;
    }
}
