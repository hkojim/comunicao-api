package miyamoto.comunicaco_api.business.service;


import miyamoto.comunicaco_api.api.dto.ComunicacaoInDTO;
import miyamoto.comunicaco_api.api.dto.ComunicacaoOutDTO;
import miyamoto.comunicaco_api.business.converter.ComunicacaoConverter;
import miyamoto.comunicaco_api.business.mapper.ComunicacaoConverterMapper;
import miyamoto.comunicaco_api.infraestructure.entities.ComunicacaoEntity;
import miyamoto.comunicaco_api.infraestructure.enums.StatusEnvioEnum;
import miyamoto.comunicaco_api.infraestructure.repositories.ComunicacaoRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service

public class ComunicacaoService {

    private final ComunicacaoRepository repository;
    private final ComunicacaoConverter converter;
    private final ComunicacaoConverterMapper mapper;

    public ComunicacaoService(ComunicacaoRepository repository, ComunicacaoConverter converter, ComunicacaoConverterMapper mapper) {
        this.repository = repository;
        this.converter = converter;
        this.mapper = mapper;
    }

    public ComunicacaoOutDTO agendarComunicacao(ComunicacaoInDTO dto) {
        if (Objects.isNull(dto)) {
            throw new RuntimeException();
        }
        dto.setStatusEnvio(StatusEnvioEnum.PENDENTE);
        ComunicacaoEntity entity = mapper.paraComunicacaoEntity(dto);
        repository.save(entity);
        ComunicacaoOutDTO outDTO = mapper.paraComunicacaoOutDTO(entity);
        return outDTO;
    }

    public ComunicacaoOutDTO buscarStatusComunicacao(String emailDestinatario) {
        ComunicacaoEntity entity = repository.findByEmailDestinatario(emailDestinatario);
        if (Objects.isNull(entity)) {
            throw new RuntimeException();
        }
        return mapper.paraComunicacaoOutDTO(entity);
    }

    public ComunicacaoOutDTO alterarStatusComunicacao(String emailDestinatario) {
        ComunicacaoEntity entity = repository.findByEmailDestinatario(emailDestinatario);
        if (Objects.isNull(entity)) {
            throw new RuntimeException();
        }
        entity.setStatusEnvio(StatusEnvioEnum.CANCELADO);
        repository.save(entity);
        return (mapper.paraComunicacaoOutDTO(entity));
    }

}
