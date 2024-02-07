package com.api.grafo.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rota {
	
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String source;
	private String target;
	private String distance;
}
