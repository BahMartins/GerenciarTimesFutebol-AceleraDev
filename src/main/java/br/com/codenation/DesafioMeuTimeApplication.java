package br.com.codenation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import br.com.codenation.desafio.annotation.Desafio;
import br.com.codenation.desafio.app.MeuTimeInterface;
import br.com.codenation.desafio.exceptions.CapitaoNaoInformadoException;
import br.com.codenation.desafio.exceptions.IdentificadorUtilizadoException;
import br.com.codenation.desafio.exceptions.JogadorNaoEncontradoException;
import br.com.codenation.desafio.exceptions.TimeNaoEncontradoException;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class DesafioMeuTimeApplication implements MeuTimeInterface {

    List<Time> listaTimes = new ArrayList<>();
    List<Jogador> listaJogadores = new ArrayList<>();

    @Desafio("incluirTime")
    public void incluirTime(Long id, String nome, LocalDate dataCriacao, String corUniformePrincipal, String corUniformeSecundario) {
        verificaTimeComMesmoId(id);

        Time time = new Time(id, nome, dataCriacao, corUniformePrincipal, corUniformeSecundario);

        listaTimes.add(time);

    }

    @Desafio("incluirJogador")
    public void incluirJogador(Long id, Long idTime, String nome, LocalDate dataNascimento, Integer nivelHabilidade, BigDecimal salario) {
        verificaJogadorComMesmoID(id);
        existeTimeComMesmoId(idTime);

        Time timeAtual = buscarTimePorId(idTime);

        if (nivelHabilidade < 0 || nivelHabilidade > 100) {
            throw new IllegalArgumentException("Nivel de habilidade não pode ser negativo ou maior que cem.");
        }

        Jogador novoJogado = new Jogador(id, idTime, nome, dataNascimento, nivelHabilidade, salario);
        timeAtual.addJogador(novoJogado);
        listaJogadores.add(novoJogado);

    }

    @Desafio("definirCapitao")
    public void definirCapitao(Long idJogador) {

        Jogador jogadorEncontrado = encontrarJogadorPeloId(idJogador);

        Time timeAtual = verificarTimeDoJogadorPeloId(jogadorEncontrado.getIdTime());

        timeAtual.setCapitao(jogadorEncontrado);

    }

    @Desafio("buscarCapitaoDoTime")
    public Long buscarCapitaoDoTime(Long idTime) {

        Time timeEncontrado = buscarTimePorId(idTime);

        Jogador capitaoEncontrado = timeEncontrado.getCapitao();

        if (Objects.isNull(capitaoEncontrado)) {
            throw new CapitaoNaoInformadoException("Capitão não encontrado");
        }

        return capitaoEncontrado.getId();
    }

    @Desafio("buscarNomeJogador")
    public String buscarNomeJogador(Long idJogador) {

        Jogador jogadorEncontrado = encontrarJogadorPeloId(idJogador);

        return jogadorEncontrado.getNome();
    }

    @Desafio("buscarNomeTime")
    public String buscarNomeTime(Long idTime) {

        Time nomeDoTimeEncontrado = buscarTimePorId(idTime);

        return nomeDoTimeEncontrado.getNome();
    }

    @Desafio("buscarJogadoresDoTime")
    public List<Long> buscarJogadoresDoTime(Long idTime) {

        Time timeEncontrado = buscarTimePorId(idTime);

        return timeEncontrado.getListaJogadores()
                .stream()
                .map(jogador -> jogador.getId())
                .sorted()
                .collect(toList());

    }

    @Desafio("buscarMelhorJogadorDoTime")
    public Long buscarMelhorJogadorDoTime(Long idTime) {

        Time timeEncontrado = listaTimes.stream().filter(time -> time.getId().equals(idTime))
                .findFirst()
                .orElseThrow(() -> new TimeNaoEncontradoException("Time não encontrado"));


        return timeEncontrado.getListaJogadores().stream()
                .min(Comparator.comparing(Jogador::getNivelHabilidade).reversed()
                    .thenComparing(Jogador::getId))
                .map(jogador -> jogador.getId())
                .orElseThrow(() -> new JogadorNaoEncontradoException("Jogador não encontrado"));
    }

    @Desafio("buscarJogadorMaisVelho")
    public Long buscarJogadorMaisVelho(Long idTime) {

        Time timeJogadorVelho = buscarTimePorId(idTime);

        return timeJogadorVelho.getListaJogadores().stream()
                .min(Comparator.comparing(Jogador::getDataNascimento).thenComparing(Jogador::getId))
                .map(jogador -> jogador.getId())
                .orElseThrow(() -> new JogadorNaoEncontradoException("Jogador não encontrado!"));

    }

    @Desafio("buscarTimes")
    public List<Long> buscarTimes() {

        return listaTimes.stream().sorted(Comparator.comparing(time -> time.getId()))
                .map(Time::getId)
                .collect(toList());
    }

    @Desafio("buscarJogadorMaiorSalario")
    public Long buscarJogadorMaiorSalario(Long idTime) {
        Time time = buscarTimePorId(idTime);


        Comparator<Jogador> jogadorComparator = Comparator.comparing(jogador -> jogador.getSalario());

        return time.getListaJogadores().stream()
                .max(jogadorComparator)
                .map(jogador -> jogador.getId())
                .orElseThrow(() -> new JogadorNaoEncontradoException("Jogador não encontrado"));
    }

    @Desafio("buscarSalarioDoJogador")
    public BigDecimal buscarSalarioDoJogador(Long idJogador) {

        return listaJogadores.stream()
                .filter(jogador -> jogador.getId().equals(idJogador))
                .findFirst()
                .map(Jogador::getSalario)
                .orElseThrow(() -> new JogadorNaoEncontradoException("Jogador não encontrado"));

    }

    @Desafio("buscarTopJogadores")
    public List<Long> buscarTopJogadores(Integer top) {

        List<Jogador> topJogador = listaJogadores.stream()
                .sorted(Comparator.comparingInt(Jogador::getNivelHabilidade).reversed())
                .limit(top)
                .collect(toList());

        return topJogador.stream()
                .sorted(Comparator.comparing(Jogador::getNivelHabilidade).reversed().thenComparing(Jogador::getId))
                .map(Jogador::getId)
                .collect(toList());
    }

    @Desafio("buscarCorCamisaTimeDeFora")
    public String buscarCorCamisaTimeDeFora(Long timeDaCasa, Long timeDeFora) {
        Time timeCasa = buscarTimePorId(timeDaCasa);
        Time timeFora = buscarTimePorId(timeDeFora);

        boolean compararUniforme = timeCasa.getCorUniformePrincipal().equalsIgnoreCase(timeFora.getCorUniformePrincipal());
        if (compararUniforme) {
            return timeFora.getCorUniformeSecundario();
        }
        return timeFora.getCorUniformePrincipal();

    }


    private void verificaTimeComMesmoId(Long id) {

        if (id == null) {
            throw new NullPointerException("ID obrigatório");
        }

        boolean existeTime = listaTimes.stream().anyMatch(time -> time.getId().equals(id));

        if (existeTime) {
            throw new IdentificadorUtilizadoException("Time como ID: " + id + " já está cadastrado");
        }

    }

    private void verificaJogadorComMesmoID(Long id) {
        boolean existeJogador = listaJogadores.stream().anyMatch(jogador -> jogador.getId().equals(id));

        if (existeJogador) {
            throw new IdentificadorUtilizadoException("O jogador com o ID: " + id + " já está cadastrado");
        }

    }

    private void existeTimeComMesmoId(Long idTime) {
        boolean timeInformadoExiste = listaTimes.stream().anyMatch(time -> time.getId().equals(idTime));

        if (!timeInformadoExiste) {
            throw new TimeNaoEncontradoException("O Time com o ID: " + idTime + " não existe");
        }

    }

    public Time buscarTimePorId(Long idTime) {

        Time timeEncontrado = listaTimes.stream()
                .filter(time -> time.getId().equals(idTime))
                .findFirst()
                .orElseThrow(() -> new TimeNaoEncontradoException("O time informmado não existe!"));

        return timeEncontrado;

    }

    public Jogador encontrarJogadorPeloId(Long idJogador) {

        Jogador jogadorEncontrado = listaJogadores.stream()
                .filter(jogador -> jogador.getId().equals(idJogador))
                .findFirst()
                .orElseThrow(() -> new JogadorNaoEncontradoException("O ID: " + idJogador + " do jogador não foi encontrado"));

        return jogadorEncontrado;
    }


    public Time verificarTimeDoJogadorPeloId(Long idTime){
        Time time = listaTimes.stream()
                .filter(time1 -> time1.getId().equals(idTime))
                .findFirst()
                .orElseThrow(() -> new TimeNaoEncontradoException("Time não existe!"));

        return time;
    }

}
