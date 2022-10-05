package demo.mariadb;

import java.util.List;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class DemoService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DemoService.class);

	private static final int BATCH_SIZE = 50;

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public int insertDemosInBatches(List<Demo> demoList) {
		LOGGER.info("Inserting {} demos", demoList.size());
		entityManager.unwrap(Session.class).setJdbcBatchSize(BATCH_SIZE);
		int i = 0;
		for (var demo : demoList) {
			if (i > 0 && i % BATCH_SIZE == 0) {
				entityManager.flush();
				entityManager.clear();
			}
			entityManager.persist(demo);
			i++;
		}
		return i;
	}
}
