package miyamoto.comunicaco_api.business.mapper;

import miyamoto.comunicaco_api.api.dto.ComunicacaoInDTO;
import miyamoto.comunicaco_api.api.dto.ComunicacaoOutDTO;
import miyamoto.comunicaco_api.infraestructure.entities.ComunicacaoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComunicacaoConverterMapper {
    //@Mapping(source = "dataHoraEnvio", target="dataHoraEnvio")
    ComunicacaoEntity paraComunicacaoEntity(ComunicacaoInDTO dto);
    ComunicacaoOutDTO paraComunicacaoOutDTO(ComunicacaoEntity entity);



}
