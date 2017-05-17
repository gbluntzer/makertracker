package org.tenbitworks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.tenbitworks.model.Asset;
import org.tenbitworks.model.Member;
import org.tenbitworks.model.Training;
import org.tenbitworks.repositories.AssetRepository;
import org.tenbitworks.repositories.MemberRepository;
import org.tenbitworks.repositories.TrainingRepository;

@SpringBootApplication
@EnableGlobalMethodSecurity(securedEnabled = true)
public class MakertrackerApplication {

    private static final Logger log = LoggerFactory.getLogger(MakertrackerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MakertrackerApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(MemberRepository repository) {
        return (args) -> {
            // save a couple of customers




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
            } catch (Exception e) {
                e.printStackTrace();
            }


        };
    }

    @Bean
    public CommandLineRunner assetData(AssetRepository repository) {
        return (args) -> {
            // save a couple of Greeting

            // fetch all customers
            log.info("Asset found with findAll():");
            log.info("-------------------------------");
            for (Asset asset : repository.findAll()) {
                log.info(asset.toString());
            }
            log.info("");
        };
    }

    @Bean
    public CommandLineRunner trainingData(TrainingRepository repository) {
        return (args) -> {
            // save a couple of Greeting
            
            // fetch all customers
            log.info("Training found with findAll():");
            log.info("-------------------------------");
            for (Training training : repository.findAll()) {
                log.info(training.toString());
            }
            log.info("");
        };
    }
}
