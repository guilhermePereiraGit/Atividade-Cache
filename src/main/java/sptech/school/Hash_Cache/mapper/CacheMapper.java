package sptech.school.Hash_Cache.mapper;

import org.springframework.stereotype.Component;
import sptech.school.Hash_Cache.cache.EstatisticasCache;
import sptech.school.Hash_Cache.dto.CacheEstatisticasResponse;

@Component
public class CacheMapper {

	public CacheEstatisticasResponse paraResponse(EstatisticasCache estatisticas) {
		CacheEstatisticasResponse response = new CacheEstatisticasResponse();
		response.setTamanhoAtual(estatisticas.getTamanhoAtual());
		response.setLimite(estatisticas.getLimite());
		response.setHits(estatisticas.getHits());
		response.setMisses(estatisticas.getMisses());
		response.setEvictions(estatisticas.getEvictions());
		response.setHitRatio(estatisticas.getHitRatio());
		return response;
	}
}
