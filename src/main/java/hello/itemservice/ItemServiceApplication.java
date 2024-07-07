package hello.itemservice;

import com.zaxxer.hikari.util.DriverDataSource;
import hello.itemservice.config.*;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.mybatis.ItemMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


@Slf4j
//@Import(MemoryConfig.class)
//@Import(JdbcTemplateConfigV1.class)
//@Import(JdbcTemplateConfigV2.class)
//@Import(JdbcTemplateConfigV3.class)
//@Import(MyBatisConfig.class)
//@Import(JpaConfig.class)
@Import(SpringDataJpaConfig.class)
@SpringBootApplication(scanBasePackages = "hello.itemservice.web")
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

	/**
	 * 로컬 환경에서만 사용하는 빈 설정
	 * @param itemRepository : ItemRepository 빈 주입
	 * @return TestDataInit 빈
	 */
	@Bean
	@Profile("local")
	public TestDataInit testDataInit(ItemRepository itemRepository) {
		return new TestDataInit(itemRepository);
	}


	/**
	 * 테스트용 데이터베이스 설정
	 * @Profile("test") : test 프로파일이 활성화되어 있을 때 빈으로 등록
	 * @return
	 * 단,test  application.properties 파일에서 datasource 설정을 주석처리하면 아래 설정이 필요하지 않다.
	 */
/*

	@Bean
	@Profile("test")
	public DataSource dataSource() {
		log.info("메모리 데이터베이스 초기화");
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		*/
/**
		 *  jdbc:h2:mem:db;DB_CLOSE_DELAY=-1 : 메모리 모드로 데이터베이스를 생성하고, 애플리케이션이 종료될 때까지 데이터를 유지
		 *  DB_CLOSE_DELAY=-1 : 애플리케이션이 종료될 때까지 데이터를 유지
		 *  DB_CLOSE_ON_EXIT=FALSE : JVM이 종료될 때 데이터베이스를 유지
		 *  실무에서는 사용하지 않는 것이 좋다.
		 *  H2 데이터베이스는 애플리케이션을 재시작할 때마다 데이터가 초기화되기 때문에 테스트용도로만 사용
		 *  실무에서는 MySQL, PostgreSQL, Oracle, SQL Server 등을 사용
		 *//*

		dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1"); // H2 DB의 메모리 모드 URL
		dataSource.setUsername("sa");
		dataSource.setPassword("");
		return  dataSource;
	}

*/

}
