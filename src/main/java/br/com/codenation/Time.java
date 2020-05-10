package br.com.codenation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Time {

    private Long id;
    private String nome;
    private LocalDate dataCriacao;
    private String corUniformePrincipal;
    private String corUniformeSecundario;
    private Jogador capitao;


    private List<Jogador> listaJogadores = new ArrayList<>();


     public Time(Long id, String nome, LocalDate dataCriacao, String corUniformePrincipal,
                   String corUniformeSecundario){
        this.id = id;
        this.nome = nome;
        this.dataCriacao = dataCriacao;
        this.corUniformePrincipal = corUniformePrincipal;
        this.corUniformeSecundario = corUniformeSecundario;
    }

    public void addJogador(Jogador jogador) {
        listaJogadores.add(jogador);

    }

    public Jogador getCapitao(){ return capitao;}

    public void setCapitao(Jogador capitao){ this.capitao = capitao; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getCorUniformePrincipal() {
        return corUniformePrincipal;
    }

    public void setCorUniformePrincipal(String corUniformePrincipal) {
        this.corUniformePrincipal = corUniformePrincipal;
    }

    public String getCorUniformeSecundario() {
        return corUniformeSecundario;
    }

    public void setCorUniformeSecundario(String corUniformeSecundario) {
        this.corUniformeSecundario = corUniformeSecundario;
    }


    public List<Jogador> getListaJogadores() {
        return listaJogadores;
    }

    public void setListaJogadores(List<Jogador> listaJogadores) {
        this.listaJogadores = listaJogadores;
    }


}
