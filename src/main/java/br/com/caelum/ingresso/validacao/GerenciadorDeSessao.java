package br.com.caelum.ingresso.validacao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import br.com.caelum.ingresso.model.Sessao;

public class GerenciadorDeSessao {

	private List<Sessao> sessoesDaSala;
	
	public GerenciadorDeSessao(List<Sessao> sessoesDaSala) {
		this.sessoesDaSala = sessoesDaSala;
	}
	
	public boolean cabe(Sessao sessaoNova) {
		if (terminaAmanha(sessaoNova)) {
			return false;
		}
		
		return sessoesDaSala.stream().noneMatch(sessaoExistente -> 
				horarioIsConflitante(sessaoExistente,sessaoNova)						
				);
	}

	private boolean horarioIsConflitante(Sessao sessaoExistente, Sessao sessaoNova) {
		
		LocalDateTime inicioSessaoExistente = getInicioSessaoComDiaHoje(sessaoExistente);
		LocalDateTime terminoSessaoExistente = getTerminoSessaoComDiaDeHoje(sessaoExistente);
		LocalDateTime inicioSessaoNova = getInicioSessaoComDiaHoje(sessaoNova);
		LocalDateTime terminoSessaoNova = getTerminoSessaoComDiaDeHoje(sessaoNova);
		
		Boolean sessaoNovaTerminoAntesDaExistente = terminoSessaoNova.isBefore(inicioSessaoExistente);
		Boolean sessaoNovaComecaDepoisDaExistente = terminoSessaoExistente.isBefore(inicioSessaoNova);
		
		if (sessaoNovaTerminoAntesDaExistente || sessaoNovaComecaDepoisDaExistente) {
			return false;
		}
		return true;
	}

	private boolean terminaAmanha(Sessao sessao) {
		
		LocalDateTime terminoSessaoNova = getTerminoSessaoComDiaDeHoje(sessao);
		LocalDateTime ultimoSegundoDeHoje = LocalDateTime.of(LocalDate.now(),LocalTime.MAX);
		
		if (terminoSessaoNova.isAfter(ultimoSegundoDeHoje)) {
			return true;
		}
		return false;
	}

	private LocalDateTime getTerminoSessaoComDiaDeHoje(Sessao sessao) {
		LocalDateTime inicioSessaoNova = getInicioSessaoComDiaHoje(sessao);
		
		return inicioSessaoNova.plus(sessao.getFilme().getDuracao());
	}

	private LocalDateTime getInicioSessaoComDiaHoje(Sessao sessao) {
		LocalDate hoje = LocalDate.now();
		
		return sessao.getHorario().atDate(hoje);
	}
}
