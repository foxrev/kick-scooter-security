package com.softServe.security;

import com.softServe.security.model.AppUser;
import com.softServe.security.model.Roles;
import com.softServe.security.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findUserShouldReturnUser() throws Exception {
        List<Roles> list = new ArrayList<>();
        list.add(Roles.ROLE_USER);
        new ArrayList<>().add(Roles.ROLE_USER);
        this.entityManager.persist(
                new AppUser(1l, "vchabanenko219@gmail.com",
                        "Vladimir", "Chabanenko",
                        "default_default", false,
                        LocalDateTime.of(LocalDate.of(2020, 02,27),
                                LocalTime.of(12,21,12, 311000)),
                        list));
        AppUser actual = this.userRepository.findByEmail("vchabanenko219@gmail.com").orElse(null);
        assertThat(actual).isNotNull();
    }
}
