package com.mnrclara.qb.ws.services;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import com.mnrclara.qb.ws.services.*;
import com.mnrclara.qb.ws.services.jxb.*;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;


@Component
public class CountryRepository {
	private static final Map<String, Country> countries = new HashMap<>();

	@PostConstruct
	public void initData() {
		Country spain = new Country();
		spain.setName("Spain");
		spain.setCapital("Madrid");
		spain.setPopulation(46704314);

		countries.put(spain.getName(), spain);

		Country poland = new Country();
		poland.setName("Poland");
		poland.setCapital("Warsaw");
		poland.setPopulation(38186860);

		countries.put(poland.getName(), poland);

		Country uk = new Country();
		uk.setName("United Kingdom");
		uk.setCapital("London");
		uk.setPopulation(63705000);

		countries.put(uk.getName(), uk);
	}

	public Country findCountry(String name) {
//		System.out.println("The country's name must not be null");
		return countries.get(name);
	}
}
