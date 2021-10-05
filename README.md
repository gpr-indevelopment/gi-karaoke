# Karaokê da Gi 19

### Acesse o [karaokê](https://gi-karaoke.herokuapp.com/).

### Página de [administração](https://gi-karaoke.herokuapp.com/admin) do Karaokê.

---
## Motivação:

Minha irmã fez uma festa de aniversário no dia 02/10/2021. Ela me disse que na festa iria ter um karaokê onde os convidados cantariam em um microfone conectado a um amplificador, e as músicas e letras estariam passando no Youtube da televisão.

Com um grande número de convidados, certamente seria difícil organizar esse karaokê. Quem canta com quem? Quais músicas estão na lista? Qual a próxima música?

Esse problema pode ser resolvido de diversas maneiras, inclusive com fichas de papel preenchidas com as solicitações de músicas dos convidados. O problema é que essa abordagem não dá visibilidade da fila em tempo real, nem torna muito fácil o processo de pedir para cantar no karaokê.

Sendo assim, pensei em desenvolver uma aplicação web para resolver esse problema. Nela, os convidados poderiam acompanhar a fila em tempo real e pedir músicas direto dos browsers dos smartphones pessoais. Por conta da restrição de tempo, as implementações buscaram **resolver o problema com pouco esforço de desenvolvimento**.

## O que foi feito:

A aplicação é simples. Ela apresenta uma fila de solicitações de músicas no karaokê e a música que está tocando no momento. A estilização do site tenta lembrar os anos 2000, que era o tema da festa. Minha preocupação com o design foi lembrar a web 1.0 o máximo possível.

![Imagem inicial site](/assets/Fig1.jpg "Imagem inicial site")

Além disso, a aplicação disponibiliza uma página de administração onde minha irmã conseguiria passar para a próxima música e reverter a ação em caso de engano. Essa tela tem visual simples. 

Só quem conhecia essa tela era minha irmã. Isso foi fundamental já que a aplicação não tinha RBAC nem nada parecido, e qualquer um com conhecimento a esse link poderia alterar o karaokê. **Novamente, a ideia aqui foi manter simplicidade, e essa solução atendia a festa com pouco esforço**.

![Imagem admin](/assets/Fig2.jpg "Imagem admin")

## Como foi feito:

A lista de solicitações de músicas é mantida em uma estrutura de fila. As músicas em execução são mantidas em uma pilha. Isso permite que a função de reverter da tela de administração devolva músicas da pilha de volta para a fila.

1. Spring Boot: Framework selecionado para o backend. Essa é minha zona de conforto. JUnit 4 para os testes unitários.
2. **Não utilizei banco de dados**: Optei por manter os dados em memória por simplicidade. Ainda, a aplicação é suportada por uma única instância de servidor.
3. React: Framework selecionado para o frontend. Utilizei componentes da biblioteca [Ant Design](https://ant.design/components/overview/).
4. Comunicação: REST para as consultas e escritas das solicitações de músicas. Websockets foram usadas para atualizar os browsers dos convidados toda vez que a fila for atualizada.
5. Heroku: Plataforma utilizada para hospedar a aplicação. Selecionada por ser gratuita, e ter um hook que permite o redeploy toda vez que o código for atualizado na master.

## Conclusão

A festa foi um sucesso, e a aplicação foi muito util. O sentimento geral foi que a facilidade no cadastro das músicas estimulou as pessoas a cadastrar mais músicas (as vezes até por impulso). 

Não tivemos problemas técnicos nem bugs encontrados durante a festa. Mesmo com a limitação de tempo tive a preocupação de escrever vários testes unitários importantes.

**A melhor parte de ser desenvolvedor é poder colocar ideias em prática com pouco tempo e recursos.**


