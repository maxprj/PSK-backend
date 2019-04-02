package com.psk.backend.user

import groovy.transform.builder.Builder
import groovy.transform.builder.ExternalStrategy

@Builder(builderStrategy = ExternalStrategy, forClass = User)
class UserBuilder {
    UserBuilder() {
        email('email')
        password('password')
        name('name')
        surname('surname')
        role(UserRole.ROLE_USER)
    }

    static User user() {
        new UserBuilder().build();
    }
}
