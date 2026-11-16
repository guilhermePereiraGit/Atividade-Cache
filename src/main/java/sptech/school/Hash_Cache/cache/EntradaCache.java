package sptech.school.Hash_Cache.cache;

import lombok.Getter;
import sptech.school.Hash_Cache.entity.Conteudo;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@Getter
public class EntradaCache {

	private final Conteudo conteudo;
	private final Instant armazenadoEm;
	private volatile Instant ultimoAcesso;

	public EntradaCache(Conteudo conteudo) {
		this.conteudo = Objects.requireNonNull(conteudo, "O conteudo nao pode ser nulo.");
		this.armazenadoEm = Instant.now();
		this.ultimoAcesso = armazenadoEm;
	}

	public void registrarAcesso() {
		this.ultimoAcesso = Instant.now();
	}

	public boolean estaExpirada(Duration ttl) {
		Duration tempoArmazenado = Duration.between(armazenadoEm, Instant.now());
		return tempoArmazenado.compareTo(ttl) >= 0;
	}
}
