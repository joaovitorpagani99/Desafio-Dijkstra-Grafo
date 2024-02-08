package com.api.grafo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.api.grafo.grafo.Dijkstra;
import com.api.grafo.model.Grafo;
import com.api.grafo.model.PathData;
import com.api.grafo.model.Rotas;
import com.api.grafo.model.dto.ResponsePayloadCaminhoMinimo;
import com.api.grafo.model.dto.RotaDTO;
import com.api.grafo.model.dto.responseRotaDTO;
import com.api.grafo.repository.GrafoRepository;

@Service
public class GrafoService {

	@Autowired
	private GrafoRepository repository;

	public Rotas salvar(Rotas rotas) {
		Rotas grafo = this.repository.save(rotas);
		return grafo;
	}

	public Rotas buscar(Long graphId) {
		Rotas grafo = this.repository.findById(graphId).get();
		return grafo;
	}

	public List<responseRotaDTO> listarRotas(String town1, String town2, Integer maxStops, Rotas rotas) {
		Dijkstra dijkstra = new Dijkstra();
		List<Grafo> rotasAtual = rotas.getData();
		dijkstra.adicionarRotas(rotasAtual);
		List<String> caminhos = dijkstra.encontrarCaminhosComMaximoParadas(town1, town2, maxStops);
		List<responseRotaDTO> responses = new ArrayList<>();
		for (String caminho : caminhos) {
			RotaDTO rota = new RotaDTO();
			rota.setRoute(caminho);
			rota.setStops(caminho.length() - 1);
			responseRotaDTO response = new responseRotaDTO();
			response.setRotas(new ArrayList<>()); // Inicializa a lista de rotas
			response.getRotas().add(rota);
			responses.add(response);
		}
		return responses;
	}

	public List<responseRotaDTO> buscarGrafoEListarRotas(Long graphId, String town1, String town2, Integer maxStops) {
		Rotas rotas = this.buscar(graphId);
		return this.listarRotas(town1, town2, maxStops, rotas);
	}

	public Integer calcularDistancia(PathData pathData) {
		Dijkstra dijkstra = new Dijkstra();
		List<Grafo> rotas = pathData.getData();
		dijkstra.adicionarRotas(rotas);
		return dijkstra.calcularDistanciaCaminho(pathData.getPath(), pathData.getData());
	}

	public Integer calcularDistanciaComGrafoSalvo(Long graphId, PathData pathData) {
		Rotas rota = this.buscar(graphId);
		System.out.println(rota);
		Dijkstra dijkstra = new Dijkstra();
		pathData.setData(rota.getData());
		List<Grafo> rotas = pathData.getData();
		System.out.println(rotas);
		dijkstra.adicionarRotas(rotas);
		return dijkstra.calcularDistanciaCaminho(pathData.getPath(), pathData.getData());
	}

	public ResponsePayloadCaminhoMinimo calcularDistanciaMinimaEntreBairros(String town1, String town2, Rotas rotas) {
		Dijkstra dijkstra = new Dijkstra();
		dijkstra.adicionarRotas(rotas.getData());
		ResponsePayloadCaminhoMinimo response = dijkstra.dijkstra(town1, town2);
		return response;
	}
}
