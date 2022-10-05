package demo.mariadb;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(indexes = { @Index(columnList = "eventDateTime") })
public class Demo {
	@Id
	private Long id;

	@Column(nullable = false)
	private Instant eventDateTime;

	public Demo() {
	}

	public Demo(Long id, Instant eventDateTime) {
		this.id = id;
		this.eventDateTime = eventDateTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Instant getEventDateTime() {
		return eventDateTime;
	}

	public void setEventDateTime(Instant eventDateTime) {
		this.eventDateTime = eventDateTime;
	}
}
