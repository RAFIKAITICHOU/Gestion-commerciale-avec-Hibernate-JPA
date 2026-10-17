package org.example;

import org.example.entities.Client;
import org.example.entities.Fournisseur;
import org.example.entities.User;
import org.example.service.CrudService;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class UserInheritanceTest {

    private final CrudService<User, Long> userService = new CrudService<>(User.class);

    @Test
    public void testHeritageJoined() {
        Client c = new Client("Alice", LocalDate.of(2024, 1, 15));
        Fournisseur f = new Fournisseur("Bob SARL", "12 rue des Lilas");
        userService.create(c);
        userService.create(f);

        List<User> users = userService.getAll();
        assertTrue(users.stream().anyMatch(u -> u instanceof Client && u.getId().equals(c.getId())));
        assertTrue(users.stream().anyMatch(u -> u instanceof Fournisseur && u.getId().equals(f.getId())));

        User loaded = userService.getById(c.getId());
        assertTrue(loaded instanceof Client);
        assertEquals(LocalDate.of(2024, 1, 15), ((Client) loaded).getDateInscription());
    }
}
