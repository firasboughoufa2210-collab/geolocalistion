package com.example.microcapteur.Repository;

import com.example.microcapteur.Entities.Mesure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MesureRepository  extends JpaRepository<Mesure, Long> {
}
