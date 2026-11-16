Contexto

Imagine uma API que entrega conteúdos (artigos, posts, páginas) que vêm de uma fonte lenta: um banco sobrecarregado, um serviço externo ou um processamento pesado. Muitos usuários pedem os mesmos conteúdos várias vezes, e buscar tudo de novo a cada requisição desperdiça tempo e recursos.

É aí que entra o cache de conteúdo: guardar temporariamente o resultado de uma busca para responder as próximas requisições mais rápido. Mas memória não é infinita, então o cache precisa de limite e de uma regra para decidir o que sai quando ele enche.
Parte 1 — Pesquisa

Pesquise e responda, com suas palavras e citando as fontes consultadas:

    O que é cache e qual problema ele resolve? Dê dois exemplos do dia a dia de um sistema web (navegador, CDN, banco, aplicação).
    O que significam cache hit, cache miss e hit ratio?
    O que é TTL (Time To Live) e por que um dado em cache pode ficar "velho" (stale)?
    Explique as políticas de remoção (eviction) FIFO, LRU e LFU. Em que situação cada uma faz mais sentido?
    O que é invalidação de cache? Por que se diz que é um dos problemas mais difíceis da computação?
    Qual a diferença entre cache local (em memória da aplicação) e cache distribuído (ex.: Redis)? Quando o local deixa de ser suficiente?

Parte 2 — Implementação

Implemente uma API em Spring Boot 4.x com JDK 21+ que sirva conteúdos usando um cache próprio, construído com HashMap. Não utilize @Cacheable, Caffeine, Redis ou qualquer biblioteca de cache: a ideia é você construir a versão "light".
Cenário

    Crie uma entidade Conteudo (ex.: id, titulo, corpo, autor, atualizadoEm) persistida com JPA (H2 ou o banco que a turma já usa).
    Simule a lentidão da fonte: na busca pelo repositório/serviço, adicione um atraso artificial (ex.: Thread.sleep(2000)), para que a diferença entre HIT e MISS fique visível.

Requisitos do cache

    Crie um componente ConteudoCache (um @Component) que armazene os conteúdos em um Map<Long, EntradaCache>, em que EntradaCache guarda o conteúdo, o momento em que foi armazenado e o último acesso.
    Limite do cache: a quantidade máxima de itens deve ser configurável no application.properties, por exemplo:

    app.cache.max-itens=5
    app.cache.ttl-segundos=60

    Leia esses valores com @Value ou @ConfigurationProperties.
    Remoção ao atingir o limite: quando o cache estiver cheio e um novo item precisar entrar, remova o item usado há mais tempo (LRU), usando a informação de último acesso guardada na entrada.
    Expiração (TTL): um item com mais tempo que o TTL configurado deve ser tratado como MISS: busque de novo na fonte e atualize o cache.
    Invalidação: ao atualizar ou excluir um conteúdo, a entrada correspondente deve ser removida do cache.
    Estatísticas: o cache deve contar hits, misses e remoções por limite (evictions).
    Use ConcurrentHashMap no lugar do HashMap comum, para que o cache funcione com requisições simultâneas.
    Logs: cada busca deve gerar um log indicando se foi HIT ou MISS, o id do conteúdo e o tempo da operação em milissegundos. Use o logger do Spring (SLF4J), não System.out.println. Exemplo:

    INFO  ConteudoCache : Cache MISS - id=3 - 2015 ms
    INFO  ConteudoCache : Cache HIT  - id=3 - 1 ms

Endpoints esperados
Método 	Rota 	Comportamento
GET 	/conteudos/{id} 	Busca primeiro no cache; se não houver (ou estiver expirado), busca na fonte e armazena. Retorna o header X-Cache: HIT ou X-Cache: MISS.
POST 	/conteudos 	Cadastra um conteúdo.
PUT 	/conteudos/{id} 	Atualiza o conteúdo e invalida a entrada no cache.
DELETE 	/conteudos/{id} 	Exclui o conteúdo e invalida a entrada no cache.
GET 	/cache/estatisticas 	Retorna tamanho atual, limite, hits, misses, evictions e hit ratio.
DELETE 	/cache 	Limpa todo o cache.

Organização: siga o padrão da disciplina: a conversão para DTO acontece na controller (com Mapper), e o service recebe e retorna entidade. O cache é usado pelo service.
Demonstração

Com o limite configurado em 5 itens, cadastre pelo menos 7 conteúdos e mostre, com prints do Postman/Insomnia e do log da aplicação:

    a primeira busca de um conteúdo (MISS, lenta) e a segunda (HIT, rápida), comparando o tempo de resposta;
    o cache atingindo o limite e removendo o item menos usado recentemente;
    um item expirando pelo TTL;
    a invalidação após um PUT;
    o endpoint de estatísticas ao final dos testes.

Desafios extras (opcionais)

    Torne o cache genérico (CacheSimples<K, V>) para reaproveitá-lo em outras entidades.
    Permita trocar a política de remoção (LRU ou FIFO) por configuração.
    Crie uma rotina com @Scheduled que remova periodicamente os itens expirados.
    Escreva testes unitários do cache cobrindo limite, TTL e remoção.

Entrega

Cada integrante da dupla envia no Moodle:

    o documento da Parte 1 (PDF), com os nomes da dupla e as fontes consultadas;
    o link do repositório com o projeto da Parte 2 e um README explicando como rodar e como o cache funciona;
    os prints da demonstração (podem estar no README ou em um PDF).
