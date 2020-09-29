package com.krootix.streamsex.stream;

import com.krootix.streamsex.creation.ObjectCreator;
import com.krootix.streamsex.model.UnitOfWork;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class StreamExample {

    private ObjectCreator objectCreator;


    public StreamExample(ObjectCreator objectCreator) {
        this.objectCreator = objectCreator;
    }

    public void run() {

        String matching = "S";
        String countryToFilter = "Switzerland";
        String filter = "house";
        int count = 12;

        List<String> countryList;

        System.out.println("list of titles of units of work:");
        map()
                .forEach(System.out::println);
        System.out.println("#--------------------------------------------------------------------------#");

        System.out.println("collecting units of work to Map:");
        collectToMap()
                .forEach((k, v) -> System.out.println("title: " + k + ", description: " + v));
        System.out.println("#--------------------------------------------------------------------------#");

        countryList = objectCreator.addRandomCountryAndRetrieveCountries(3);
        System.out.print("Countries: ");
        countryList
                .forEach(c -> System.out.print(c + " "));
        System.out.println();
        countCountries(countryList)
                .forEach((k, v) -> System.out.println("country: " + k + " present " + v + " times"));
        System.out.println("#--------------------------------------------------------------------------#");

        countryList = objectCreator.retrieveCountries();

        System.out.println("sorted countries:");
        sorted(countryList)
                .forEach(System.out::println);
        System.out.println("#--------------------------------------------------------------------------#");

        match(countryList, matching);
        System.out.println("#--------------------------------------------------------------------------#");

        System.out.println("total matched: " + count(countryList, matching));
        System.out.println("#--------------------------------------------------------------------------#");

        Set<Map<String, String>> countries = objectCreator.createCountriesWithCities();

        System.out.println("Cities in Switzerland:");
        findCitiesInCountry(countries, countryToFilter)
                .forEach(System.out::println);
        System.out.println("#--------------------------------------------------------------------------#");

        System.out.println("Cities in other countries:");
        CitiesToGet(countries, countryToFilter)
                .forEach(System.out::println);
        System.out.println("#--------------------------------------------------------------------------#");

        System.out.println("Units of work have been found by filter '" + filter + "' in description:");
        filter(filter)
                .forEach(System.out::println);
        System.out.println("#--------------------------------------------------------------------------#");

        System.out.println("flatMap example:");
        List<List<String>> nestedInListCountries = objectCreator.retrieveCountriesInListOfLists();
        flatMapCountriesFromList(nestedInListCountries)
                .forEach(System.out::println);
        System.out.println("#--------------------------------------------------------------------------#");

        System.out.println("some reduce example:");
        List<Integer> numbers = objectCreator.createIntegers(count);
        reduce(numbers);
        System.out.println("#--------------------------------------------------------------------------#");

    }

    // map
    // in this case we are mapping title from objects UnitOfWork to List of String
    private List<String> map() {
        return objectCreator.createStreamOfUnitOfWorks()
                .map(UnitOfWork::getTitle)
                .collect(Collectors.toList());
    }

    // collect to Set
    private Set<UnitOfWork> collectToSet() {
        return objectCreator.createStreamOfUnitOfWorks()
                .collect(toSet());
    }

    // collect to HashSet
    private Set<UnitOfWork> collectToHashSet() {
        return objectCreator.createStreamOfUnitOfWorks()
                .collect(Collectors.toCollection(HashSet::new));
    }

    // collect to UnmodifiableSet
    // you can covert to UnmodifiableList or UnmodifiableMap also
    private Set<UnitOfWork> collectToUnmodifiableSet() {
        return objectCreator.createStreamOfUnitOfWorks()
                .collect(Collectors.toUnmodifiableSet());
    }

    // collect to List
    private List<UnitOfWork> collectToList() {
        return objectCreator.createStreamOfUnitOfWorks()
                .collect(Collectors.toList());
    }

    // collect to Map with title and description
    private Map<String, String> collectToMap() {
        return objectCreator.createStreamOfUnitOfWorks()
                .collect(toMap(UnitOfWork::getTitle, UnitOfWork::getDescription));
    }

    // how often does each country occur
    private Map<String, Integer> countCountries(List<String> countryList) {
        return countryList
                .stream()
                .collect(HashMap::new, (s, value) -> s.merge(value, 1, Integer::sum), HashMap::putAll);
    }

    // sorted
    private List<String> sorted(List<String> countryList) {
        return countryList
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    // match
    private void match(List<String> countryList, String matching) {

        // you can yse yor own predicate, function, consumer, producer..
        // it's good choice, if you need to use lambdas multiple times
        Predicate<String> matchingPredicate = s -> s.startsWith(matching);

        boolean matchedResult = countryList.stream()
                .anyMatch(matchingPredicate);

        System.out.println("any match with filter " + matching + ": " + matchedResult);

        matchedResult = countryList.stream()
                .allMatch(matchingPredicate);
        System.out.println("all match with filter " + matching + ": " + matchedResult);

        matchedResult = countryList.stream()
                .noneMatch(matchingPredicate);
        System.out.println("none match with filter " + matching + ": " + matchedResult);
    }

    // count
    private long count(List<String> countryList, String matching) {
        return countryList.stream()
                .filter(s -> s.startsWith(matching))
                .count();
    }

    // Some example of using groupingBy
    private Map<String, List<String>> groupByCountry(Set<Map<String, String>> countries, String countryToFilter) {

        // grouping cities by country
        return countries
                .stream()
                .flatMap(m -> m.entrySet()
                        .stream()
                        .filter(e -> e.getKey().equals(countryToFilter)))
                .collect(groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
    }

    // using of flatMap. There we extract inner list from Map
    private Set<String> findCitiesInCountry(Set<Map<String, String>> countries, String countryToFilter) {
        return groupByCountry(countries, countryToFilter)
                .values()
                .stream()
                .flatMap(Collection::stream)
                .collect(toSet());
    }

    // using of flatMap. There we extract values from Map. We have one Map. In other way we got values from couple of Maps.
    private Set<String> CitiesToGet(Set<Map<String, String>> countries, String countryToFilter) {
        return filterCountriesExcludeFilterCountry(countries, countryToFilter)
                .stream()
                .flatMap(m -> m.values()
                        .stream())
                .collect(toSet());
    }

    private Set<Map<String, String>> filterCountriesExcludeFilterCountry(Set<Map<String, String>> countries, String countryToFilter) {
        return countries
                .stream()
                .map(m -> m.entrySet()
                        .stream()
                        .filter(e -> !e.getKey().equals(countryToFilter))
                        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue))
                )
                .collect(toSet());
    }


    // we can use Stream as output value from the method, if you want
    private Set<String> CitiesToGetUsingStream(Set<Map<String, String>> countries, String countryToFilter) {
        return filterCountriesExcludeFilterCountryStreamMethod(countries, countryToFilter)
                .flatMap(m -> m.values()
                        .stream())
                .collect(toSet());
    }

    private Stream<Map<String, String>> filterCountriesExcludeFilterCountryStreamMethod(Set<Map<String, String>> countries, String countryToFilter) {
        return countries
                .stream()
                .map(m -> m.entrySet()
                        .stream()
                        .filter(e -> !e.getKey().equals(countryToFilter))
                        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue))
                );
    }

    // another example of collect
    public List<String> collect() {
        return objectCreator.createStreamOfUnitOfWorks()
                .collect(ArrayList::new,
                        (list, item) -> list.add(item.getTitle()),
                        ArrayList::addAll);
    }

    // another example of collect
    public List<String> collectUsingExtractedYourOwnBiConsumer() {
        // another example of extracted lambda expression
        // you can use your own BiConsumer
        BiConsumer<ArrayList<String>, UnitOfWork> yourOwnBiConsumer = (list, item) -> list.add(item.getTitle());

        return objectCreator.createStreamOfUnitOfWorks()
                .collect(ArrayList::new,
                        yourOwnBiConsumer,
                        ArrayList::addAll);
    }

    // filter
    private Set<UnitOfWork> filter(String filter) {
        return objectCreator.createStreamOfUnitOfWorks()
                .filter(unitOfWork -> unitOfWork.getDescription().contains(filter))
                .collect(toSet());
    }

    // findFirst
    private UnitOfWork findFirst() {
        return objectCreator.createStreamOfUnitOfWorks()
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    // flatMap from List of Lists to List of String
    private List<String> flatMapCountriesFromList(List<List<String>> countriesNested) {
        return countriesNested.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    // peek
    // but I'd not recommend to use that in your code
    private void peek() {
        objectCreator.createStreamOfUnitOfWorks()
                .peek(UnitOfWork::getId)
                .peek(System.out::println)
                .collect(Collectors.toList());
    }

    // reduce
    private void reduce(List<Integer> numbers) {
        int result = numbers
                .stream()
                .reduce(0, Integer::sum);
        // or we can use it that way
        //      .reduce(0, (subtotal, element) -> subtotal + element);
        // the Integer value 0 is the identity
        // subtotal, element -> subtotal + element
        // it is the accumulator, since it takes the partial sum of
        // Integer values and the next element in the stream

        System.out.print("numbers:");
        numbers.forEach(n -> System.out.format(" %d", n));

        System.out.println("\nresult of reduce: " + result);

        divideListElements(numbers, 1);
    }

    private int divideListElements(List<Integer> values, int divider) {
        return values.stream().reduce(0, (a, b) -> divide(a, divider) + divide(b, divider));
    }

    private int divide(int value, int factor) {
        int result = 0;
        try {
            result = value / factor;
        } catch (ArithmeticException e) {
            System.out.format(" %s", e);
        }
        return result;
    }

    // simple example of reduce
    private void reduceCountries(List<String> countryList) {

        Optional<String> reduced = countryList.stream()
                .reduce((s1, s2) -> s1 + "#" + s2);
        reduced.ifPresent(System.out::println);
    }
}