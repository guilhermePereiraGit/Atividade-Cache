package sptech.school.Hash_Cache.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ConteudoNaoEncontradoException extends RuntimeException {

	public ConteudoNaoEncontradoException(Long id) {
		super("Conteudo nao encontrado: " + id);
	}
}
