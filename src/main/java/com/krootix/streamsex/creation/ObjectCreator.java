package com.krootix.streamsex.creation;

import com.krootix.streamsex.model.UnitOfWork;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.krootix.streamsex.model.UnitOfWork.createUnitOfWork;

public class ObjectCreator {

    private UnitOfWork[] unitOfWorks = {
            createUnitOfWork(1L, "take pictures", "take pictures of the landscape", 300),
            createUnitOfWork(2L, "broken tv", "Fix tv in my house", 300),
            createUnitOfWork(3L, "wipe the dust", "wipe the dust in my house", 250)
    };

    // how you can create a stream of elements
    public void create() {
        // 1
        Stream.of(unitOfWorks);
        // 2
        Arrays.stream(unitOfWorks);
        // 3
        Stream.of(unitOfWorks[0], unitOfWorks[1], unitOfWorks[2]);
        //4
        Stream.Builder<UnitOfWork> unitOfWorkBuilder = Stream.builder();
        unitOfWorkBuilder.accept(unitOfWorks[0]);
        unitOfWorkBuilder.accept(unitOfWorks[1]);
        unitOfWorkBuilder.accept(unitOfWorks[2]);
        Stream<UnitOfWork> unitOfWorkStream = unitOfWorkBuilder.build();

    }

    public Stream<UnitOfWork> createStreamOfUnitOfWorks() {
        return Stream.of(unitOfWorks);
    }

    public List<Integer> createIntegers(int count) {
        return IntStream.range(0, count).boxed().collect(Collectors.toList());
    }

    public List<String> retrieveCountries() {
        return new ArrayList<>(Arrays.asList("Luxembourg", "Switzerland", "Norway", "Singapore",
                "Australia", "Sweden", "Russia"));
    }

    public List<List<String>> retrieveCountriesInListOfLists() {
        return Arrays.asList(
                Arrays.asList("Luxembourg", "Switzerland"),
                Arrays.asList("Norway", "Singapore"),
                Arrays.asList("Australia", "Sweden"));
    }

    public Set<Map<String, String>> createCountriesWithCities() {
        Set<Map<String, String>> countries = new HashSet<>();
        Map<String, String> citiesMap1 = Map.of("Norway", "Oslo", "Australia", "Melbourne", "Switzerland", "Zürich");
        Map<String, String> citiesMap2 = Map.of("Australia", "Sydney", "Norway", "Bergen", "Switzerland", "Geneva");
        Map<String, String> citiesMap3 = Map.of("Switzerland", "Lucerne", "Australia", "Brisbane", "Norway", "Drammen");

        countries.add(citiesMap1);
        countries.add(citiesMap2);
        countries.add(citiesMap3);

        return countries;
    }

    public List<String> addRandomCountryAndRetrieveCountries(int count) {
        List<String> countryList = retrieveCountries();
        Random random = new Random();
        IntStream
                .iterate(0, n -> n + 1)
                .limit(count)
                .forEach(i -> {
                    int randomCountry = random.nextInt(countryList.size());
                    countryList.add(countryList.get(randomCountry));
                });
        return countryList;
    }
}