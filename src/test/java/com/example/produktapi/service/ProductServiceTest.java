package com.example.produktapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.produktapi.exception.BadRequestException;
import com.example.produktapi.exception.EntityNotFoundException;
import com.example.produktapi.model.Product;
import com.example.produktapi.repository.ProductRepository;
import com.example.produktapi.service.ProductService;

import jakarta.persistence.Entity;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	


	@Mock
	private ProductRepository productRepository;
	
	@InjectMocks
	private ProductService underTest;
	
	@Captor
	ArgumentCaptor<Product> productCaptor;
	
	@Captor
	ArgumentCaptor<String> stringCaptor;
	
	@Captor
	ArgumentCaptor<Integer> intCaptor;
	
	
	@Test
	void tryUpdateProductByFindingItsId_thenChange() {
		//given
		Product product = new Product(
				"titel",
				200.0,
				"something",
				"desc",
				"url");

		given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
		//when(underTest.addProduct(product)).thenReturn(underTest.updateProduct(product, product.getId()));
		//when
		
		underTest.updateProduct(product, product.getId());
		//then
		
		verify(productRepository).save(product);
		

	}
	
	//findAllCategories
	
	@Test
	void findAllCategorys_thenReturnTrue() {
		
		//when
		underTest.getAllCategories();
		
		//then
		verify(productRepository, times(1)).findAllCategories();
		verifyNoMoreInteractions(productRepository);
		

		
	}
	
	//findByCategory
	@Test
	
	void findByCategory_test() {
		
		String category = "electronics";
		Product product = new Product(
				"Titel",
				200.0,
				category,
				"desc",
				"url");
		
		underTest.getProductsByCategory(category);
		
		verify(productRepository, times(1)).findByCategory(stringCaptor.capture());
		
		verifyNoMoreInteractions(productRepository);
		assertEquals(category, stringCaptor.getValue());
		
	}
	
	
	//can't find product by ID, return false.
	
	@Test
	void tryFindingAnIdThatDosntExist_thenThrowError() {
		
		//when
		int id = 2;
		when(productRepository.findById(id)).thenReturn(Optional.empty());
		
		//then
		EntityNotFoundException exception =
				assertThrows(EntityNotFoundException.class , () ->{
					underTest.getProductById(id);
				}
				);
		
        assertEquals("Produkt med id " + id + " hittades inte", exception.getMessage());
		
		
	}
	

	//Find product by ID.
	@Test
	void findproductBy_gettingTheID() {
		
		//given
		Product product = new Product(
				"Titel",
				200.0,
				"desc",
				"category",
				"url");
		
		
		given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
		//when
		underTest.getProductById(product.getId());
		
		//then
		verify(productRepository, times(1)).findById(intCaptor.capture());
		verifyNoMoreInteractions(productRepository);
		Assertions.assertTrue(productRepository.findById(product.getId()).isPresent());
	}
	
	@Test
	void deleteProductById_createProductThenDelete() {

		//given
		Product product = new Product(
				"Titel",
				200.0,
				"desc",
				"category",
				"url");
		
		
		given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
		
		//when
		underTest.deleteProduct(product.getId());
		
		
		//Then
		verify(productRepository).deleteById(product.getId());

		
		}
	

	
	
	//Are we calling the Method findAll()
	@Test
	void areWeCallingTheMethod_findAll() {
		//When
		underTest.getAllProducts();
		//Then
		verify(productRepository, times(1)).findAll();
		verifyNoMoreInteractions(productRepository);

	}
	
	//Are we calling the Method findAllCategories()
	@Test
	void areWeCallingTheMethod_findAllCategories() {
		//When
		underTest.getAllCategories();
		//Then
		verify(productRepository).findAllCategories();
	}
	
	
	//Write the findAllCategories TEST
	
	//Are we calling the Method get getAllCatefories()
	@Test
	void areWeCallingTheMethod_GetCateGorys_findall() {
		//When
		underTest.getAllCategories();
		//Then
		verify(productRepository).findAllCategories();
	}
	
	// Test to find product by it's category
	
	
	//SE ÖVER!!!!!
	// givenGetAllProducts_thenExactlyOneInteractionWithRepository ändrade om
	@Test
	void givenGetProducts_thenExactlyOneInteractionWithRepo() {
		//When
		String electronics = "electronics";
		List<Product> productsByCategory = underTest.getProductsByCategory(electronics);
		//Then
		verify(productRepository, times(1)).findByCategory(stringCaptor.capture());
		Assertions.assertTrue(productsByCategory.isEmpty());
		
	}

	
	@Test
	void whenAddingAprogram_thenSaveMethodShouldBeCalled() {
		//Given
		Product product = new Product(
				"Dator",
				4000.0,
				"",
				"",
				"");
		//When
		underTest.addProduct(product);
		//Then
		verify(productRepository).save(productCaptor.capture());
		assertEquals(product.getTitle(), productCaptor.getValue().getTitle());
		assertEquals(product.getPrice(), productCaptor.getValue().getPrice());
	}
	
	
	@Test
	void WhenAddingProductWithDuplicateTitle_thenThrowError() {
		String titel = "titel ye";
		//given
		Product product = new Product(
				titel,
				400.0,
				"something",
				"ello",
				"url"); 

		given(productRepository.findByTitle(titel)).willReturn(Optional.of(product));
		//Then
		BadRequestException exception = assertThrows(BadRequestException.class, 
				//When
				()-> underTest.addProduct(product) );
		verify(productRepository, times(1)).findByTitle(titel);
		verify(productRepository, never()).save(any());
		assertEquals("En produkt med titeln: "+ titel +" finns redan", exception.getMessage());
	}


}