package org.fipro.eclipse.migration.service;

import java.util.List;

import org.fipro.eclipse.migration.model.Person;

public interface PersonService {

	/**
	 * Creates a list of {@link Person}s. 
	 * @param numberOfPersons The number of {@link Person}s that should be generated.
	 * @return
	 */
	List<Person> getPersons(int numberOfPersons);

	/**
	 * Creates a random person out of names which are taken from "The Simpsons" 
	 * and enrich them with random generated married state and birthday date.
	 * @return
	 */
	Person createPerson(int id);

}