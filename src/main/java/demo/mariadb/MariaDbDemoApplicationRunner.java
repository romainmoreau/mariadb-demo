package demo.mariadb;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MariaDbDemoApplicationRunner implements ApplicationRunner {
	private static final Logger LOGGER = LoggerFactory.getLogger(MariaDbDemoApplicationRunner.class);

	private static final int END_HOUR_EXCLUSIVE = 5;

	@Autowired
	private DemoService demoService;

	@Autowired
	private DemoRepository demoRepository;

	private long id;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		LOGGER.info("{} demos already inserted", demoRepository.count());
		id = demoRepository.findMaxId().map(id -> id + 1).orElse(0l);

		LOGGER.info("Generating demos");
		var l1 = getDemoList(LocalDate.parse("2022-09-01"));
		var l2 = getDemoList(LocalDate.parse("2022-09-02"));
		Stream.of(l1, l2).flatMap(Collection::stream)
				.forEach(d -> LOGGER.info("{}: {}", d.getId(), d.getEventDateTime()));

		LOGGER.info("Inserting demos");
		var inserted = Stream.of(l1, l2).parallel().mapToLong(l -> demoService.insertDemosInBatches(l)).sum();
		LOGGER.info("{} demos inserted", inserted);

		LOGGER.info("Finding all {} demos", demoRepository.count());
		demoRepository.findAll().stream().sorted(Comparator.comparing(Demo::getId))
				.forEach(d -> LOGGER.info("{}: {}", d.getId(), d.getEventDateTime()));
	}

	private List<Demo> getDemoList(LocalDate localDate) throws IOException {
		return IntStream.range(0, END_HOUR_EXCLUSIVE)
				.mapToObj(i -> ZonedDateTime.of(localDate, LocalTime.of(i, 0), ZoneOffset.UTC).toInstant())
				.map(i -> new Demo(id++, i)).collect(Collectors.toList());
	}
}
