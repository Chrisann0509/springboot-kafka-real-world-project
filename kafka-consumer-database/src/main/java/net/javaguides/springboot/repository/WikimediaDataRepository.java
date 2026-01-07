package net.javaguides.springboot.repository;

import net.javaguides.springboot.entity.Wikimedia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WikimediaDataRepository extends JpaRepository<Wikimedia, Long> {
}
