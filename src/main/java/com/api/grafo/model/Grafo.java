package com.api.grafo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity()
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Grafo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String source;
	private String target;
	private int distance;
}
