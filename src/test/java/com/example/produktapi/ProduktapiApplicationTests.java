package com.example.produktapi;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.produktapi.model.Product;
import com.example.produktapi.repository.ProductRepository;

@DataJpaTest
class ProduktapiApplicationTests {
//something	
	Product product = new Product(
			"En dator", 
			25000.0, 
			"elektronik", 
			"Bra å ha", 
			"url"
			);
	Product product2 = new Product(
			"",
			2.0,
			"elektronik",
			"något",
			"url");
	
	@Autowired
	private ProductRepository underTest;
	
	@Test
	void simpleTest() {
		List<Product>products = underTest.findAll();
		Assertions.assertFalse(products.isEmpty(),"Meddlande woho tjoho hej hej hej");
	}
	@Test
	void whenSearchingForExisitingtitle_thenReturnThatProduct() {
		String title = "En dator";
		//given
		underTest.save(product);
		//When 
		Optional<Product> optionalProduct = underTest.findByTitle(title);
		//Then
		
		
		
		Assertions.assertAll(
				() -> 	Assertions.assertTrue(optionalProduct.isPresent()),
				() ->	Assertions.assertFalse(optionalProduct.isEmpty()),
				() ->	Assertions.assertEquals(title, optionalProduct.get().getTitle())
				);
	}
	
	@Test
	void searchForSomethingInThe_existing_SQLFILE() {
		String title = "DANVOUY Womens T Shirt Casual Cotton Short";
		
		Optional<Product> optionalProduct = underTest.findByTitle(title);
		
		Assertions.assertTrue(optionalProduct.isPresent());
	}
	
	
	@Test
	void whenSearchingForNonExisitingTitle_then_returningEmpyOptional() {
		String emptyString ="title";
		
		underTest.save(product2);
		
		Optional<Product> optionalProduct = underTest.findByTitle(emptyString);
				Assertions.assertFalse(optionalProduct.isPresent());
				Assertions.assertTrue(optionalProduct.isEmpty());
				
	}

	@Test
	void whenSearchingForNonExisitingTitle_then_returningExist() {
		
		String emptyString = "";
		underTest.save(product2);
		
		Optional<Product> optionalProduct = underTest.findByTitle(emptyString);
				Assertions.assertTrue(optionalProduct.isPresent());
				
	}
}
