package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    //public Optional<User> findById(Long id);

    //public User findByEmail(String email);

    //findAllByEmailContainingIgnoreCase("tr")
}
