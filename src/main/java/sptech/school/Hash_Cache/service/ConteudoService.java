package sptech.school.Hash_Cache.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.server.ResponseStatusException;
import sptech.school.Hash_Cache.cache.ConteudoCache;
import sptech.school.Hash_Cache.entity.Conteudo;
import sptech.school.Hash_Cache.exception.ConteudoNaoEncontradoException;
import sptech.school.Hash_Cache.repository.ConteudoRepository;

import java.util.function.Consumer;

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

	@Transactional(readOnly = true)
	public Conteudo buscarPorId(Long id, Consumer<Boolean> informarHit) {
		return cache.buscar(id, () -> buscarNaFonte(id), informarHit);
	}

	@Transactional
	public Conteudo atualizar(Long id, Conteudo novosDados) {
		Conteudo existente = buscarNoRepositorio(id);
		existente.setTitulo(novosDados.getTitulo());
		existente.setCorpo(novosDados.getCorpo());
		existente.setAutor(novosDados.getAutor());

		Conteudo atualizado = repository.saveAndFlush(existente);
		invalidarAposCommit(id);
		return atualizado;
	}

	@Transactional
	public void excluir(Long id) {
		Conteudo existente = buscarNoRepositorio(id);
		repository.delete(existente);
		invalidarAposCommit(id);
	}

	private Conteudo buscarNaFonte(Long id) {
		simularLentidao();
		return buscarNoRepositorio(id);
	}

	private Conteudo buscarNoRepositorio(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ConteudoNaoEncontradoException(id));
	}

	private void invalidarAposCommit(Long id) {
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
			@Override
			public void afterCommit() {
				cache.invalidar(id);
			}
		});
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
