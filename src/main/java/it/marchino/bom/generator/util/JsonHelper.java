package it.marchino.bom.generator.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonHelper {

	public static final Gson GSON = init();

	private static Gson init() {
		return new GsonBuilder().create();
	}
	
}
