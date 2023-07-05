package com.example.minor1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Minor1Application implements CommandLineRunner {

	/*@Autowired
	AdminService adminService;*/

	public static void main(String[] args) {

		SpringApplication.run(Minor1Application.class, args);
		/*
			Below code written in run method cannot be written here because, we cannot call
			not static variables(bookRepository) from static method.

			Also, bookRepository cannot be made static, if made static it takes value null

			So we implement CommandLineRunner's run method, that is internally called by spring
			to test functionalities without writing APIs

			so understand the need for non-static function like run method.
		 */

	}


	// run method is used for testing without calling APIs, can help in debugging
	@Override
	public void run(String... args) throws Exception {
		// This super admin can only create new admins
		/*Admin admin = Admin.builder()
				.name("Admin")
				.email("admin@google.com")
				.securedUser(
						SecuredUser.builder()
								.username("Admin")
								.password("admin123")
								.build()
				)
				.build();
		adminService.create(admin);*/

		/*Book book = Book.builder()
				.name("Intro to CS")
				.genre(Genre.PROGRAMMING)
				.build();

		bookRepository.save(book	);*/
	}
}
