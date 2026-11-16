package sptech.school.Hash_Cache.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sptech.school.Hash_Cache.entity.Conteudo;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
public class ConteudoCache {

	private static final Logger logger = LoggerFactory.getLogger(ConteudoCache.class);

	private final Map<Long, EntradaCache> entradas = new ConcurrentHashMap<>();

	public Conteudo buscar(Long id, Supplier<Conteudo> buscarNaFonte) {
		Objects.requireNonNull(id, "O id nao pode ser nulo.");
		Objects.requireNonNull(buscarNaFonte, "A funcao de busca nao pode ser nula.");

		long inicio = System.nanoTime();
		EntradaCache entrada = entradas.get(id);
		boolean hit = entrada != null;

		try {
			if (!hit) {
				entrada = entradas.computeIfAbsent(id,
						chave -> new EntradaCache(buscarNaFonte.get()));
			}
			entrada.registrarAcesso();
			return entrada.getConteudo();
		} finally {
			long duracao = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - inicio);
			logger.info("Cache {} - id={} - {} ms", hit ? "HIT" : "MISS", id, duracao);
		}
	}

	public void limpar() {
		entradas.clear();
	}
}
