package sptech.school.Hash_Cache.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sptech.school.Hash_Cache.entity.Conteudo;

import java.time.Duration;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
public class ConteudoCache {

	private static final Logger logger = LoggerFactory.getLogger(ConteudoCache.class);

	private final Map<Long, EntradaCache> entradas = new ConcurrentHashMap<>();
	private final Object trava = new Object();
	private final int maxItens;
	private final Duration ttl;

	public ConteudoCache(
			@Value("${app.cache.max-itens}") int maxItens,
			@Value("${app.cache.ttl-segundos}") long ttlSegundos) {
		if (maxItens <= 0 || ttlSegundos <= 0) {
			throw new IllegalArgumentException("O limite e o TTL do cache devem ser positivos.");
		}
		this.maxItens = maxItens;
		this.ttl = Duration.ofSeconds(ttlSegundos);
	}

	public Conteudo buscar(Long id, Supplier<Conteudo> buscarNaFonte) {
		Objects.requireNonNull(id, "O id nao pode ser nulo.");
		Objects.requireNonNull(buscarNaFonte, "A funcao de busca nao pode ser nula.");

		long inicio = System.nanoTime();
		boolean hit = false;

		try {
			synchronized (trava) {
				EntradaCache entrada = entradas.get(id);
				if (entrada != null && !entrada.estaExpirada(ttl)) {
					hit = true;
					entrada.registrarAcesso();
					return entrada.getConteudo();
				}

				if (entrada != null) {
					entradas.remove(id);
					logger.info("Cache TTL - id={} - entrada expirada", id);
				}

				Conteudo conteudo = Objects.requireNonNull(
						buscarNaFonte.get(), "A fonte nao pode retornar conteudo nulo.");
				removerExpirados();
				if (entradas.size() >= maxItens) {
					removerMenosRecente();
				}
				entradas.put(id, new EntradaCache(conteudo));
				return conteudo;
			}
		} finally {
			long duracao = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - inicio);
			logger.info("Cache {} - id={} - {} ms", hit ? "HIT" : "MISS", id, duracao);
		}
	}

	public void limpar() {
		synchronized (trava) {
			entradas.clear();
		}
	}

	public void invalidar(Long id) {
		Objects.requireNonNull(id, "O id nao pode ser nulo.");
		synchronized (trava) {
			if (entradas.remove(id) != null) {
				logger.info("Cache INVALIDADO - id={}", id);
			}
		}
	}

	private void removerExpirados() {
		entradas.entrySet().removeIf(item -> {
			if (item.getValue().estaExpirada(ttl)) {
				logger.info("Cache TTL - id={} - entrada expirada", item.getKey());
				return true;
			}
			return false;
		});
	}

	private void removerMenosRecente() {
		Long idRemover = entradas.entrySet().stream()
				.min(Comparator.comparing(item -> item.getValue().getUltimoAcesso()))
				.map(Map.Entry::getKey)
				.orElseThrow();
		entradas.remove(idRemover);
		logger.info("Cache LRU - id={} - limite={}", idRemover, maxItens);
	}
}
