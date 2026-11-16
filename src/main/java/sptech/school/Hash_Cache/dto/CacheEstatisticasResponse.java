package sptech.school.Hash_Cache.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CacheEstatisticasResponse {

	private int tamanhoAtual;
	private int limite;
	private long hits;
	private long misses;
	private long evictions;
	private double hitRatio;
}
