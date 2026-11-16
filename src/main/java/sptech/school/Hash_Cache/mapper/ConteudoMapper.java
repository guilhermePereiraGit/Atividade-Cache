package sptech.school.Hash_Cache.mapper;

import org.springframework.stereotype.Component;
import sptech.school.Hash_Cache.dto.ConteudoRequest;
import sptech.school.Hash_Cache.dto.ConteudoResponse;
import sptech.school.Hash_Cache.entity.Conteudo;

@Component
public class ConteudoMapper {

	public Conteudo paraEntidade(ConteudoRequest request) {
		Conteudo conteudo = new Conteudo();
		conteudo.setTitulo(request.getTitulo());
		conteudo.setCorpo(request.getCorpo());
		conteudo.setAutor(request.getAutor());
		return conteudo;
	}

	public ConteudoResponse paraResponse(Conteudo conteudo) {
		ConteudoResponse response = new ConteudoResponse();
		response.setId(conteudo.getId());
		response.setTitulo(conteudo.getTitulo());
		response.setCorpo(conteudo.getCorpo());
		response.setAutor(conteudo.getAutor());
		response.setAtualizadoEm(conteudo.getAtualizadoEm());
		return response;
	}
}
