package miyamoto.comunicaco_api.business.service;


import io.micrometer.common.util.StringUtils;
import miyamoto.comunicaco_api.api.dto.ComunicacaoInDTO;
import miyamoto.comunicaco_api.api.dto.ComunicacaoOutDTO;
import miyamoto.comunicaco_api.business.converter.ComunicacaoConverter;
import miyamoto.comunicaco_api.business.mapper.ComunicacaoConverterMapper;
import miyamoto.comunicaco_api.infraestructure.entities.ComunicacaoEntity;
import miyamoto.comunicaco_api.infraestructure.enums.StatusEnvioEnum;
import miyamoto.comunicaco_api.infraestructure.execptions.IllegalArgumentException;
import miyamoto.comunicaco_api.infraestructure.execptions.IllegalStateException;
import miyamoto.comunicaco_api.infraestructure.execptions.ResourceNotFoundException;
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
            throw new IllegalArgumentException("Dados de comunicação não podem ser nulos.");
        }
        dto.setStatusEnvio(StatusEnvioEnum.PENDENTE);
        ComunicacaoEntity entity = mapper.paraComunicacaoEntity(dto);
        repository.save(entity);
        ComunicacaoOutDTO outDTO = mapper.paraComunicacaoOutDTO(entity);
        return outDTO;
    }

    public ComunicacaoOutDTO buscarStatusComunicacao(String emailDestinatario) {
        // 1. Validação de argumento (Bad Request)
        if (StringUtils.isBlank(emailDestinatario)) {
            throw new IllegalArgumentException("O e-mail do destinatário deve ser informado.");
        }

        // 2. Validação de existência (Not Found)
        ComunicacaoEntity entity = repository.findByEmailDestinatario(emailDestinatario);
        if (Objects.isNull(entity)) {
            throw new ResourceNotFoundException("Comunicação não encontrada para o e-mail: " + emailDestinatario);
        }

        return mapper.paraComunicacaoOutDTO(entity);
    }

    public ComunicacaoOutDTO alterarStatusComunicacao(String emailDestinatario) {
        ComunicacaoEntity entity = repository.findByEmailDestinatario(emailDestinatario);

        if (Objects.isNull(entity)) {
            throw new ResourceNotFoundException("Não existe agendamento para o e-mail: " + emailDestinatario);
        }

        // 3. Regra de Negócio: Não cancelar o que já foi enviado
        if (StatusEnvioEnum.ENVIADO.equals(entity.getStatusEnvio())) {
            throw new IllegalStateException("Não é possível cancelar uma comunicação que já foi enviada.");
        }

        entity.setStatusEnvio(StatusEnvioEnum.CANCELADO);
        repository.save(entity);
        return mapper.paraComunicacaoOutDTO(entity);
    }

}
