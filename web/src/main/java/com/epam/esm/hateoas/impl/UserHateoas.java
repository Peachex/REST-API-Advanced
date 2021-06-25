package com.epam.esm.hateoas.impl;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.User;
import com.epam.esm.hateoas.Hateoas;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserHateoas implements Hateoas<User> {
    @Override
    public void createHateoas(User user) {
        user.add(linkTo(methodOn(UserController.class).findAllUsers(0, 0)).withSelfRel());
        user.add(linkTo(methodOn(UserController.class).findUserById(String.valueOf(user.getId()))).withSelfRel());
        user.getGiftCertificates().forEach(t -> t.add(linkTo(methodOn(GiftCertificateController.class)
                .findCertificateById(String.valueOf(t.getId()))).withSelfRel()));
    }
}
