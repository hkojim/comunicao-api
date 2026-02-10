package miyamoto.comunicaco_api.infraestructure.repositories;

import miyamoto.comunicaco_api.infraestructure.entities.ComunicacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComunicacaoRepository extends JpaRepository<ComunicacaoEntity, Long> {

    ComunicacaoEntity findByEmailDestinatario(String nomeDestinatario);
}
