package sptech.school.Hash_Cache.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Conteudo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String titulo;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String corpo;

	@Column(nullable = false)
	private String autor;

	@Column(nullable = false)
	private LocalDateTime atualizadoEm;

	@PrePersist
	@PreUpdate
	private void atualizarData() {
		this.atualizadoEm = LocalDateTime.now();
	}
}
