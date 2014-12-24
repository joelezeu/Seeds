package com.diadementi.seeds.util;

public interface JsonParser<D> {
	
	 abstract D parse(String response);

}
