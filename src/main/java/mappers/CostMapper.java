package mappers;

import com.etraveli.cardcostapi.dtos.CostDTO;
import com.etraveli.cardcostapi.dtos.CostRedisDTO;
import com.etraveli.cardcostapi.entity.Cost;
import com.etraveli.cardcostapi.entity.CostRedisEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CostMapper {
    CostDTO entityToDTO(Cost cost);
    CostRedisDTO redisEntityToDTO(CostRedisEntity costRedisEntity);
}
