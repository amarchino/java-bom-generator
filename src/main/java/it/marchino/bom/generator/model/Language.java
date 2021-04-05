package it.marchino.bom.generator.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Language {

	JAVA("java"),
	JAVASCRIPT("javascript"),
	;
	private final String description;
	
}
