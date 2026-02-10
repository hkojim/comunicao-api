package miyamoto.comunicaco_api.infraestructure.repositories;

import miyamoto.comunicaco_api.infraestructure.entities.ComunicacaoEntity;
import org.springframework.data.repository.CrudRepository;

public interface ComunicacaoRepository extends CrudRepository<ComunicacaoEntity, Long> {

    ComunicacaoEntity findByEmailDestinatario(String nomeDestinatario);
}
