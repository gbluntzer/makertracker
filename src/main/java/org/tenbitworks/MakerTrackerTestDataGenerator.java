package org.tenbitworks;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.tenbitworks.model.Asset;
import org.tenbitworks.model.AssetStatus;
import org.tenbitworks.model.Member;
import org.tenbitworks.model.MemberStatus;
import org.tenbitworks.model.PaymentMethod;
import org.tenbitworks.model.Roles;
import org.tenbitworks.repositories.AssetRepository;
import org.tenbitworks.repositories.MemberRepository;
import org.tenbitworks.repositories.UserRepository;

@Configuration
public class MakerTrackerTestDataGenerator {
	private static final String CLAZZ = MakertrackerApplication.class.getName();
	private static final Logger LOGGER = Logger.getLogger(CLAZZ);
	
	@Value("${makertracker.testdata.generate:false}")
	boolean generateTestData;
	
	@Value("${makertracker.testdata.count:10}")
	int testDataCount;
	
	@Autowired
	UserDetailsManager userDetailsManager;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	MemberRepository memberRepo;
	
	@Autowired
	AssetRepository assetRepo;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	Random random = new Random();
	
	@Bean
	public CommandLineRunner generateTestUsers() {
		return (args) -> {
			if (generateTestData && userRepo.count() <= 2) { //2 for admin and user defaults
				LOGGER.info("Generating Test Users");
				for (int i = 0; i < testDataCount; i++) {
					userDetailsManager.createUser(
							new User(
									"user" + i, 
									passwordEncoder.encode("user" + i), 
									Arrays.asList(new SimpleGrantedAuthority("ROLE_" + Roles.USER.toString()))));
				}
			}
		};
	}

	@Bean
	public CommandLineRunner generateMemberData() {
		return (args) -> {
			if (generateTestData && memberRepo.count() == 0) {
				LOGGER.info("Generating some members");
				
				for (int i = 0; i < testDataCount; i++) {
					LOGGER.info("Adding member " + i);
					Member newMember = new Member();
					newMember.setMemberName("member" + i);
					newMember.setAddress("address 1234 " + i);
					newMember.setDescription("member desc " + i);
					newMember.setEmail(i + "email@email.com");
					newMember.setPaymentMethod(PaymentMethod.PAYPAL);
					newMember.setPhoneNumber("210-123-4567");
					newMember.setRfid(UUID.randomUUID().toString());
					newMember.setStatus(MemberStatus.MEMBER);
					newMember.setZipCode("12345");
					newMember.setUser(userRepo.findOne("user" + i));
					
					memberRepo.save(newMember);
				}
			}
			
			LOGGER.info("Members found:");
			memberRepo.findAll().forEach(member -> LOGGER.info(member.toString()));
			LOGGER.info("-------------------------------");
		};
	}

	@Bean
	public CommandLineRunner generateAssetData() {
		return (args) -> {
			if (generateTestData && assetRepo.count() == 0) {
				LOGGER.info("Generating some assets");
				
				for (int i = 0; i < testDataCount; i++) {
					LOGGER.info("Adding asset " + i);
					Asset asset = new Asset();
					asset.setTenbitId("tenBitId" + i);
					asset.setBrand("brand" + i);
//					asset.setDateAcquired(dateAcquired);
					asset.setAccessControlled(random.nextBoolean());
					asset.setAccessControlTimeMS(random.nextInt(50000));
//					asset.setDateRemoved(dateRemoved);
					asset.setDescription("asset description " + i);
					asset.setDonor("donor " + i);
//					asset.setMembers(members);
					asset.setModelNumber("model number " + i);
					asset.setOperator("Operator " + i);
					asset.setRetailValue(new BigDecimal(random.nextInt(1000000)));
					asset.setSerialNumber("serialNumber " + i);
					asset.setStatus(AssetStatus.OWNED);
					asset.setTitle("title " + i);
					asset.setTrainingRequired(random.nextBoolean());
					asset.setWebLink("http://url" + i);
					
					assetRepo.save(asset);
				}
			}
			
			LOGGER.info("Assets found:");
			assetRepo.findAll().forEach(asset -> LOGGER.info(asset.toString()));
			LOGGER.info("-------------------------------");
		};
	}
}
