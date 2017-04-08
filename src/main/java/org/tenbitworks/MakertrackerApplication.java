package org.tenbitworks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.tenbitworks.model.Member;
import org.tenbitworks.repositories.MemberRepository;

import java.util.Optional;


@SpringBootApplication
public class MakertrackerApplication {

	private static final Logger log = LoggerFactory.getLogger(MakertrackerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MakertrackerApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(MemberRepository repository) {
		return (args) -> {
			// save a couple of customers
			repository.save(new Member("email@email.com", "Greg", "Last"));
			repository.save(new Member("email@email.com", "Bob", "Last"));
			repository.save(new Member("email@email.com", "Fred", "Last"));


			// fetch all customers
			log.info("Customers found with findAll():");
			log.info("-------------------------------");
			for (Member member : repository.findAll()) {
				log.info(member.toString());
			}
			log.info("");


			try {
				// fetch an individual customer by ID
//				Optional<Member> member = repository.findOne(new Long("1"));
//				log.info("member found with findOne(1L):");
//				log.info("--------------------------------");
//				log.info(member.toString());
//				log.info("");
			} catch (Exception e){
				e.printStackTrace();
			}


		};
	}
}
