package com.merge.hifi.merge;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by phillipjones on 11/19/14.
 */
public class PeopleCollection {
    private List<Person> collection;
    private Random randomGenerator;
    private Set<Person> peopleGenerated;
    private Person currentlyListeningTo;
    private Person currentlyLoggedIn;

    public PeopleCollection() {
        collection = new ArrayList<Person>();
        randomGenerator = new Random();
        peopleGenerated = new HashSet<Person>();
        currentlyListeningTo = null;
        currentlyLoggedIn = null;
    }

    public int getNumberOfPeople() {
        return collection.size();
    }

    public void addPerson(Person person) {
        collection.add(person);
    }

    public Person getPerson(int index) {
        return collection.get(index);
    }

    public void setCurrentlyLoggedIn(Person person) {
        currentlyLoggedIn = person;
        peopleGenerated.add(currentlyLoggedIn);
    }

    public Person getCurrentlyLoggedIn() {
        return currentlyLoggedIn;
    }

    public void setCurrentlyListeningTo(Person person) {
        currentlyListeningTo = person;
        peopleGenerated.add(currentlyListeningTo);
    }

    public Person getCurrentlyListeningTo() {
        return currentlyListeningTo;
    }

    public Person getRandomPerson() {
        Integer randomIndex = randomGenerator.nextInt(collection.size());
        Person randomPerson = collection.get(randomIndex);

        while (peopleGenerated.contains(randomPerson)) {
            randomIndex = randomGenerator.nextInt(collection.size());
            randomPerson = collection.get(randomIndex);
        }
        peopleGenerated.add(randomPerson);
        return randomPerson;
    }

    public void resetRandoms() {
        peopleGenerated.clear();
        if (currentlyLoggedIn != null) {
            peopleGenerated.add(currentlyLoggedIn);
        }
        if (currentlyListeningTo != null) {
            peopleGenerated.add(currentlyListeningTo);
        }
    }

    public Person findByName(String name) {
        for (Person p : collection) {
            if (p.getFullName().equals(name)) {
                return p;
            }
        }
        return null;
    }
}
