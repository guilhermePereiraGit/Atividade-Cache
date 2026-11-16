package sptech.school.Hash_Cache.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConteudoRequest {

	@NotBlank(message = "O titulo e obrigatorio.")
	@Size(max = 255, message = "O titulo deve ter no maximo 255 caracteres.")
	private String titulo;

	@NotBlank(message = "O corpo e obrigatorio.")
	private String corpo;

	@NotBlank(message = "O autor e obrigatorio.")
	@Size(max = 255, message = "O autor deve ter no maximo 255 caracteres.")
	private String autor;
}
