package sptech.school.Hash_Cache.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sptech.school.Hash_Cache.dto.ConteudoRequest;
import sptech.school.Hash_Cache.dto.ConteudoResponse;
import sptech.school.Hash_Cache.entity.Conteudo;
import sptech.school.Hash_Cache.mapper.ConteudoMapper;
import sptech.school.Hash_Cache.service.ConteudoService;

import java.net.URI;

@RestController
@RequestMapping("/conteudos")
public class ConteudoController {

	private final ConteudoService service;
	private final ConteudoMapper mapper;

	public ConteudoController(ConteudoService service, ConteudoMapper mapper) {
		this.service = service;
		this.mapper = mapper;
	}

	@PostMapping
	public ResponseEntity<ConteudoResponse> cadastrar(@Valid @RequestBody ConteudoRequest request) {
		Conteudo conteudo = mapper.paraEntidade(request);
		Conteudo salvo = service.cadastrar(conteudo);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(salvo.getId())
				.toUri();
		return ResponseEntity.created(location).body(mapper.paraResponse(salvo));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ConteudoResponse> buscarPorId(
			@PathVariable Long id, HttpServletResponse response) {
		Conteudo conteudo = service.buscarPorId(id,
				hit -> response.setHeader("X-Cache", hit ? "HIT" : "MISS"));
		return ResponseEntity.ok(mapper.paraResponse(conteudo));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ConteudoResponse> atualizar(
			@PathVariable Long id, @Valid @RequestBody ConteudoRequest request) {
		Conteudo novosDados = mapper.paraEntidade(request);
		Conteudo atualizado = service.atualizar(id, novosDados);
		return ResponseEntity.ok(mapper.paraResponse(atualizado));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		service.excluir(id);
		return ResponseEntity.noContent().build();
	}
}
