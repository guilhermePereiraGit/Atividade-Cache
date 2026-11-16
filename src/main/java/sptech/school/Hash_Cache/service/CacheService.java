package sptech.school.Hash_Cache.service;

import org.springframework.stereotype.Service;
import sptech.school.Hash_Cache.cache.ConteudoCache;
import sptech.school.Hash_Cache.cache.EstatisticasCache;

@Service
public class CacheService {

	private final ConteudoCache cache;

	public CacheService(ConteudoCache cache) {
		this.cache = cache;
	}

	public EstatisticasCache obterEstatisticas() {
		return cache.obterEstatisticas();
	}

	public void limpar() {
		cache.limpar();
	}
}
