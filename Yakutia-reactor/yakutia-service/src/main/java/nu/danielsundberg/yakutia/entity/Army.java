package nu.danielsundberg.yakutia.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;


@Entity
public class Army {

	@OneToMany(mappedBy ="id")
	public Set<Tank> getTanks() {
		return new HashSet<Tank>();
	}
	
}
