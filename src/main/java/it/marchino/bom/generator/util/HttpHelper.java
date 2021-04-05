package it.marchino.bom.generator.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import it.marchino.bom.generator.exception.ExecutionException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HttpHelper {
	
	private static final HttpClient HTTP_CLIENT = initHttpClient();

	private HttpClient initHttpClient() {
		 return HttpClient.newBuilder().build();
	}
	public static String httpInvocation(String uri) {
		return httpInvocation(URI.create(uri));
	}
	public static String httpInvocation(URI uri) {
		HttpRequest httpRequest = HttpRequest
			.newBuilder()
			.GET()
			.uri(uri)
			.build();
		HttpResponse<String> response;
		try {
			response = HTTP_CLIENT.send(httpRequest, BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			throw new ExecutionException("Exception in HTTP invocation: " + e.getMessage(), e);
		}
		return response.body();
	}
	public static Document httpInvocationDocument(String uri) {
		return httpInvocationDocument(URI.create(uri));
	}
	public static Document httpInvocationDocument(URI uri) {
		String body = httpInvocation(uri);
		return Jsoup.parse(body);
	}
	public static <T> T httpInvocationJson(String uri, Class<T> clazz) {
		return httpInvocationJson(URI.create(uri), clazz);
	}
	public static <T> T httpInvocationJson(URI uri, Class<T> clazz) {
		String body = httpInvocation(uri);
		return JsonHelper.GSON.fromJson(body, clazz);
	}
}
