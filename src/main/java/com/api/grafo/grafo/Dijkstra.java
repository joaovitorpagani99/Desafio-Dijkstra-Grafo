package com.api.grafo.grafo;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;

import com.api.grafo.model.Edge;
import com.api.grafo.model.dto.ResponsePayloadCaminhoMinimo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;

public class Dijkstra {
	private Map<String, List<AbstractMap.SimpleEntry<String, Integer>>> listaAdjacente;

	public Dijkstra() {
		this.listaAdjacente = new HashMap<>();
	}

	public boolean adicionarVertice(String vertice) {
		if (!this.listaAdjacente.containsKey(vertice)) {
			this.listaAdjacente.put(vertice, new ArrayList<>());
			return true;
		}
		return false;
	}

	public boolean adicionarAresta(String origem, String destino, int peso) {
		this.adicionarVertice(origem);
		this.adicionarVertice(destino);
		this.listaAdjacente.get(origem).add(new AbstractMap.SimpleEntry<>(destino, peso));
		return true;
	}

	public void adicionarRotas(List<Edge> edges) {
		for (Edge edge : edges) {
			this.adicionarAresta(edge.getSource(), edge.getTarget(), edge.getDistance());
		}
	}

	public boolean saoVizinhos(String vertice1, String vertice2) {
		if (this.listaAdjacente.containsKey(vertice1) && this.listaAdjacente.containsKey(vertice2)) {
			return this.listaAdjacente.get(vertice1).stream().anyMatch(entry -> entry.getKey().equals(vertice2));
		}
		return false;
	}

	public List<SimpleEntry<String, Integer>> listarVizinhos(String vertice) {
		if (this.listaAdjacente.containsKey(vertice)) {
			return this.listaAdjacente.get(vertice);
		}
		return null;
	}

	public ResponsePayloadCaminhoMinimo dijkstra(String origem, String destino) {
		Map<String, Integer> distancias = new HashMap<>();
		Map<String, String> anteriores = new HashMap<>();
		PriorityQueue<AbstractMap.SimpleEntry<String, Integer>> fila = new PriorityQueue<>(
				Comparator.comparingInt(AbstractMap.SimpleEntry::getValue));
		Set<String> visitados = new HashSet<>();

		for (String vertice : this.listaAdjacente.keySet()) {
			if (vertice.equals(origem)) {
				distancias.put(vertice, 0);
			} else {
				distancias.put(vertice, Integer.MAX_VALUE);
			}
			fila.add(new AbstractMap.SimpleEntry<>(vertice, distancias.get(vertice)));
		}

		while (!fila.isEmpty()) {
			String verticeAtual = fila.poll().getKey();
			if (visitados.contains(verticeAtual)) {
				continue;
			}
			visitados.add(verticeAtual);

			for (AbstractMap.SimpleEntry<String, Integer> vizinho : this.listaAdjacente.get(verticeAtual)) {
				int alternativaDistancia = distancias.get(verticeAtual) + vizinho.getValue();
				if (alternativaDistancia < distancias.get(vizinho.getKey())) {
					distancias.put(vizinho.getKey(), alternativaDistancia);
					anteriores.put(vizinho.getKey(), verticeAtual);

					fila = fila.stream()
							.filter(p -> !p.getKey().equals(vizinho.getKey()))
							.collect(Collectors.toCollection(() -> new PriorityQueue<>(
									Comparator.comparingInt(AbstractMap.SimpleEntry::getValue))));
					fila.add(new AbstractMap.SimpleEntry<>(vizinho.getKey(), distancias.get(vizinho.getKey())));
				}
			}
		}
		if (distancias.get(destino) == Integer.MAX_VALUE) {
			return new ResponsePayloadCaminhoMinimo(-1, new ArrayList<>());
		}

		List<String> caminho = new ArrayList<>();
		Set<String> visitadosSet = new HashSet<>();
		for (String vertice = destino; vertice != null; vertice = anteriores.get(vertice)) {
			if (!visitadosSet.add(vertice)) {
				return new ResponsePayloadCaminhoMinimo(-1, new ArrayList<>());
			}
			caminho.add(0, vertice);
		}

		int distancia = distancias.get(destino);
		return new ResponsePayloadCaminhoMinimo(distancia, caminho);
	}

	public void listarGrafo() {
		for (Map.Entry<String, List<AbstractMap.SimpleEntry<String, Integer>>> entry : this.listaAdjacente.entrySet()) {
			String vertice = entry.getKey();
			List<AbstractMap.SimpleEntry<String, Integer>> arestas = entry.getValue();
			System.out.println("Vértice: " + vertice);
			for (AbstractMap.SimpleEntry<String, Integer> aresta : arestas) {
				System.out.println("  Aresta: " + aresta.getKey() + ", Peso: " + aresta.getValue());
			}
		}
	}

	public List<String> encontrarCaminhosComMaximoParadas(String origem, String destino, int maximoParadas) {
		List<String> caminhos = new ArrayList<>();
		encontrarCaminhosDFS(origem, destino, maximoParadas, "", caminhos);
		return caminhos;
	}

	private void encontrarCaminhosDFS(String atual, String destino, int maximoParadas, String caminho,
			List<String> caminhos) {
		caminho += atual;
		if (caminho.length() - 1 > maximoParadas) {
			return;
		}
		if (atual.equals(destino) && caminho.length() - 1 <= maximoParadas) {
			caminhos.add(caminho);
		}
		if (this.listaAdjacente.containsKey(atual)) {
			for (AbstractMap.SimpleEntry<String, Integer> vizinho : this.listaAdjacente.get(atual)) {
				encontrarCaminhosDFS(vizinho.getKey(), destino, maximoParadas, caminho, caminhos);
			}
		}
	}

	public int calcularDistanciaCaminho(List<String> caminho, List<Edge> arestas) {
		if (caminho == null || caminho.size() <= 1) {
			return 0;
		}

		int distanciaTotal = 0;

		for (int i = 0; i < caminho.size() - 1; i++) {
			String source = caminho.get(i);
			String target = caminho.get(i + 1);

			Optional<Edge> optionalEdge = arestas.stream()
					.filter(a -> a.getSource().equals(source) && a.getTarget().equals(target))
					.findFirst();

			if (!optionalEdge.isPresent()) {
				return -1;
			}

			distanciaTotal += optionalEdge.get().getDistance();
		}

		return distanciaTotal;
	}

}
