package com.innowise.rudkovskii.dto.user;


import com.innowise.rudkovskii.dto.user.request.UserCreateRequest;
import com.innowise.rudkovskii.dto.user.request.UserUpdateRequest;
import com.innowise.rudkovskii.dto.user.response.UserResponse;
import com.innowise.rudkovskii.dto.user.response.UserWithCardsResponse;
import com.innowise.rudkovskii.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse userToUserResponse(User user);
    UserWithCardsResponse userToUserWithCardsResponse(User user);

    User toUser(UserCreateRequest userCreateRequest);
    User toUser(UserUpdateRequest userUpdateRequest);

}
