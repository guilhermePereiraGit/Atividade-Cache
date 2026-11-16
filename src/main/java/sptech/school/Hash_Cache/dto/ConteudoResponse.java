package sptech.school.Hash_Cache.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ConteudoResponse {

	private Long id;
	private String titulo;
	private String corpo;
	private String autor;
	private LocalDateTime atualizadoEm;
}
