package sptech.school.Hash_Cache.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sptech.school.Hash_Cache.cache.EstatisticasCache;
import sptech.school.Hash_Cache.dto.CacheEstatisticasResponse;
import sptech.school.Hash_Cache.mapper.CacheMapper;
import sptech.school.Hash_Cache.service.CacheService;

@RestController
@RequestMapping("/cache")
public class CacheController {

	private final CacheService service;
	private final CacheMapper mapper;

	public CacheController(CacheService service, CacheMapper mapper) {
		this.service = service;
		this.mapper = mapper;
	}

	@GetMapping("/estatisticas")
	public ResponseEntity<CacheEstatisticasResponse> obterEstatisticas() {
		EstatisticasCache estatisticas = service.obterEstatisticas();
		return ResponseEntity.ok(mapper.paraResponse(estatisticas));
	}

	@DeleteMapping
	public ResponseEntity<Void> limpar() {
		service.limpar();
		return ResponseEntity.noContent().build();
	}
}
