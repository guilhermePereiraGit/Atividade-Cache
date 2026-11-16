package sptech.school.Hash_Cache.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import sptech.school.Hash_Cache.cache.ConteudoCache;
import sptech.school.Hash_Cache.entity.Conteudo;
import sptech.school.Hash_Cache.exception.ConteudoNaoEncontradoException;
import sptech.school.Hash_Cache.repository.ConteudoRepository;

@Service
public class ConteudoService {

	private final ConteudoRepository repository;
	private final ConteudoCache cache;
	private final long atrasoMilissegundos;

	public ConteudoService(
			ConteudoRepository repository,
			ConteudoCache cache,
			@Value("${app.fonte.atraso-milissegundos:2000}") long atrasoMilissegundos) {
		if (atrasoMilissegundos < 0) {
			throw new IllegalArgumentException("O atraso da fonte nao pode ser negativo.");
		}
		this.repository = repository;
		this.cache = cache;
		this.atrasoMilissegundos = atrasoMilissegundos;
	}

	@Transactional
	public Conteudo cadastrar(Conteudo conteudo) {
		return repository.save(conteudo);
	}

	@Transactional(readOnly = true)
	public Conteudo buscarPorId(Long id) {
		return cache.buscar(id, () -> buscarNaFonte(id));
	}

	private Conteudo buscarNaFonte(Long id) {
		simularLentidao();
		return repository.findById(id)
				.orElseThrow(() -> new ConteudoNaoEncontradoException(id));
	}

	private void simularLentidao() {
		try {
			Thread.sleep(atrasoMilissegundos);
		} catch (InterruptedException exception) {
			Thread.currentThread().interrupt();
			throw new ResponseStatusException(
					HttpStatus.SERVICE_UNAVAILABLE, "Busca interrompida.", exception);
		}
	}
}
