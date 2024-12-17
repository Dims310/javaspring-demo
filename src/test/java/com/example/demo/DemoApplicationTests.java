package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.model.Payment;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.model.dto.ProductDTO;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;

@SpringBootTest
class DemoApplicationTests {

	// Cara 1
	// @Autowired // Dependecy Injection
	// private UserRepository userRepository;

	private UserRepository userRepository;
	private ProductRepository productRepository;
	private PaymentRepository paymentRepository;

	// Cara 2
	@Autowired
	public DemoApplicationTests(UserRepository userRepository, ProductRepository productRepository, PaymentRepository paymentRepository) {
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.paymentRepository = paymentRepository;
	}

	@Test
	void contextLoads() {
		// Arrange
		// int expectTotalUser = 10;
		// int expectProductStock = -1;
		int expectTotalUser = 1;

		// Act
		// List<User> actualUser = userRepository.findAll();
		// Integer actualProductStock = productRepository.findById(1).get().getStock();
		// List<Payment> actualPayment = paymentRepository.findAll();
		List<Integer> productStock = productRepository.getStock();
		// List<UserDTO> userDTOs = userRepository.getUser();
		// UserDTO userDTOByEmail = userRepository.getUserByEmail("argus1@mail.com");

		// ProductDTO productDto = productRepository.getProductByDTO();

		// Assert
		// assertEquals(expectTotalUser, actualUser.size());
		// assertNotEquals(expectProductStock, actualProductStock);
		// assertTrue(actualPayment.size() > 2);
		// assertInstanceOf(User.class, actualUser.get(0));

		assertNotEquals(-1, productStock);
		// assertEquals(expectTotalUser, userDTOs.size());
		// assertEquals(new UserDTO(3, "Hekate Argus", "Argus", "$2a$10$sca1tSUEJiPjTvLKJMMfS.STQWTNC7luphfcjmA3ZTuVNC/9HIhHa", "Admin Toko"), userDTOByEmail);
		// assertEquals("Rumah Tangga", productDto.getCategory().getName());
	}

}
