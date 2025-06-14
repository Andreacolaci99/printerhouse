package ph.id.printerhouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ph.id.printerhouse.model.Servizi;

public interface ServiziRepository extends JpaRepository<Servizi, Integer>{

    List<Servizi> findByNameContainingIgnoreCase(String name);
}
