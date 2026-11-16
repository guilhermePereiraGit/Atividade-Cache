package sptech.school.Hash_Cache.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EstatisticasCache {

	private final int tamanhoAtual;
	private final int limite;
	private final long hits;
	private final long misses;
	private final long evictions;
	private final double hitRatio;
}
