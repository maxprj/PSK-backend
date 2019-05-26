package com.psk.backend.user;

import com.psk.backend.auth.value.CurrentUserView;
import com.psk.backend.config.BaseMapperConfig;
import com.psk.backend.user.value.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = BaseMapperConfig.class)
public abstract class UserMapper {

    @Mapping(target = "status", expression = "java(UserStatus.VERIFICATION_PENDING)")
    public abstract User create(NewUserForm form);

    public abstract User update(UpdateUserForm form, @MappingTarget User user);

    public abstract UserListView listView(User user);

    public abstract UserSelectView selectView(User user);

    public abstract CurrentUserView fromUser(User user);

    abstract UserView view(User user);
}
