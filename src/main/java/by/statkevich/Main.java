package by.statkevich;

import by.statkevich.model.Animal;
import by.statkevich.model.Car;
import by.statkevich.model.Flower;
import by.statkevich.model.House;
import by.statkevich.model.Person;
import by.statkevich.util.Util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.reverseOrder;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.groupingBy;

public class Main {
    public static void main(String[] args) throws IOException {
        task1();
        task2();
        task3();
        task4();
        task5();
        task6();
        task7();
        task8();
        task9();
        task10();
        task11();
        task12();
        task13();
        task14();
        task15();
    }

    private static void task1() throws IOException {
        List<Animal> animals = Util.getAnimals();
        Stream<Animal> zoo = animals.stream()
                .filter(animal -> animal.getAge() > 10 && animal.getAge() < 20)
                .sorted(Comparator.comparingInt(Animal::getAge))
                .skip(14)
                .limit(21);
        System.out.println(zoo);
    }

    private static void task2() throws IOException {
        List<Animal> animals = Util.getAnimals();
        animals.stream()
                .filter(animal -> "Japanese".equals(animal.getOrigin()))
                .peek(animal -> animal.setBread(animal.getBread().toUpperCase()))
                .filter(animal -> "Female".equals(animal.getGender()))
                .map(Animal::getBread)
                .forEach(System.out::println);
    }

    private static void task3() throws IOException {
        List<Animal> animals = Util.getAnimals();
        animals.stream()
                .filter(animal -> animal.getAge() > 30)
                .map(Animal::getOrigin)
                .filter(origin -> origin.startsWith("A"))
                .distinct()
                .forEach(System.out::println);
    }


    private static void task4() throws IOException {
        List<Animal> animals = Util.getAnimals();
        long female = animals.stream()
                .map(Animal::getGender)
                .filter("Female"::equals)
                .count();
        System.out.println(female);
    }


    private static void task5() throws IOException {
        List<Animal> animals = Util.getAnimals();
        boolean isHungarian = animals.stream()
                .filter(animal -> animal.getAge() > 20 && animal.getAge() < 30)
                .map(Animal::getOrigin)
                .anyMatch("Hungarian"::equals);
        System.out.println(isHungarian);
    }

    private static void task6() throws IOException {
        List<Animal> animals = Util.getAnimals();
        boolean allMaleOrFemale = animals.stream()
                .map(Animal::getGender)
                .allMatch(gender -> "Female".equals(gender) || "Male".equals(gender));
        System.out.println(allMaleOrFemale);
    }


    private static void task7() throws IOException {
        List<Animal> animals = Util.getAnimals();
        boolean notFromOceania = animals.stream()
                .noneMatch(animal -> "Oceania".equals(animal.getOrigin()));
        System.out.println(notFromOceania);
    }


    private static void task8() throws IOException {
        List<Animal> animals = Util.getAnimals();
        Animal oldestAnimal = animals.stream()
                .sorted(Comparator.comparing(Animal::getBread))
                .limit(100)
                .max(Comparator.comparingInt(Animal::getAge))
                .orElseThrow();
        System.out.println(oldestAnimal);
    }


    private static void task9() throws IOException {
        List<Animal> animals = Util.getAnimals();
        Integer shortestArrayLength = animals.stream()
                .map(Animal::getBread)
                .map(String::toCharArray)
                .map(array -> array.length)
                .min(Integer::compareTo)
                .orElseThrow();
        System.out.println(shortestArrayLength);
    }


    private static void task10() throws IOException {
        List<Animal> animals = Util.getAnimals();
        int animalAgeSum = animals.stream()
                .mapToInt(Animal::getAge)
                .sum();
        System.out.println(animalAgeSum);
    }

    private static void task11() throws IOException {
        List<Animal> animals = Util.getAnimals();
        double avgAgeIndonesian = animals.stream()
                .filter(animal -> "Indonesian".equals(animal.getOrigin()))
                .mapToInt(Animal::getAge)
                .average().orElse(0);
        System.out.println(avgAgeIndonesian);
    }


    private static void task12() throws IOException {
        List<Person> people = Util.getPersons();
        people.stream()
                .filter(person -> "Male".equals(person.getGender()))
                .filter(person -> ChronoUnit.YEARS.between(person.getDateOfBirth(), LocalDate.now()) >= 18
                        && ChronoUnit.YEARS.between(person.getDateOfBirth(), LocalDate.now()) <= 27)
                .sorted(Comparator.comparing(Person::getRecruitmentGroup))
                .limit(200)
                .forEach(System.out::println);
    }


    private static void task13() throws IOException {
        List<House> houses = Util.getHouses();
        List<Person> evacuated = houses.stream()
                .collect(
                        Collectors.teeing(
                                Collectors.filtering(
                                        house -> "Hospital".equals(house.getBuildingType()),
                                        Collectors.flatMapping(
                                                house -> house.getPersonList().stream(),
                                                Collectors.toList()
                                        )
                                ),
                                Collectors.filtering(
                                        not(house -> "Hospital".equals(house.getBuildingType())),
                                        Collectors.flatMapping(
                                                house -> house.getPersonList().stream()
                                                        .sorted(
                                                                Comparator.comparing((Person person) ->
                                                                                ChronoUnit.YEARS.between(person.getDateOfBirth(), LocalDate.now()) < 18
                                                                                        || "Male".equals(person.getGender())
                                                                                        && ChronoUnit.YEARS.between(person.getDateOfBirth(), LocalDate.now()) > 63
                                                                                        || "Female".equals(person.getGender())
                                                                                        && ChronoUnit.YEARS.between(person.getDateOfBirth(), LocalDate.now()) > 58,
                                                                        Boolean::compareTo).reversed()
                                                        ),
                                                Collectors.toList()
                                        )
                                ),
                                (first, second) -> Stream.of(first, second)
                                        .flatMap(Collection::stream)
                                        .limit(500)
                                        .collect(Collectors.toList())
                        )
                );
        for (Person person : evacuated) {
            System.out.println(person);
        }
    }


    private static void task14() throws IOException {
        List<Car> cars = Util.getCars();
        Map.Entry<Map<String, Double>, Double> result = cars.stream()
                .filter(car -> groupingPredicate(car) != null)
                .collect(Collectors.teeing(
                        groupingBy(Main::groupingPredicate, LinkedHashMap::new,
                                Collectors.summingDouble(car -> (double) car.getMass() / 1000 * 7.14)
                        ), Collectors.summingDouble(car -> (double) car.getMass() / 1000 * 7.14),
                        Map::entry
                ));

        for (Map.Entry<String, Double> stringDoubleEntry : result.getKey().entrySet()) {
            String echelon = stringDoubleEntry.getKey();
            Double cost = stringDoubleEntry.getValue();
            System.out.println(echelon + " " + cost);
        }
        Double totalRevenue = result.getValue();
        System.out.println(totalRevenue);
    }

    private static String groupingPredicate(Car car) {

        if ("White".equals(car.getColor())
                || "Jaguar".equals(car.getCarMake())) {
            return "Turkmenistan";
        }
        if (car.getMass() < 1500
                && List.of("BMW", "Toyota", "Lexus", "Chrysler").contains(car.getCarMake())) {
            return "Uzbekistan";
        }
        if (("Black".equals(car.getColor()) && car.getMass() > 4000)
                || List.of("GMC", "Dodge").contains(car.getCarMake())) {
            return "Kazakhstan";
        }
        if (car.getReleaseYear() < 1982
                || List.of("Civic", "Cherokee").contains(car.getCarModel())) {
            return "Kyrgyzstan";
        }
        if (!List.of("Yellow", "Red", "Green", "Blue").contains(car.getColor())
                || car.getPrice() > 40000) {
            return "Russia";
        }
        if (car.getVin().contains("59")) {
            return "Mongolia";
        }
        return null;
    }


    private static void task15() throws IOException {
        List<String> materials = List.of("Glass", "Aluminum", "Steel");
        List<Flower> flowers = Util.getFlowers();
        double totalCost = flowers.stream()
                .sorted(Comparator
                        .comparing(Flower::getOrigin, reverseOrder())
                        .thenComparing(Flower::getPrice)
                        .thenComparing(Flower::getWaterConsumptionPerDay, reverseOrder()))
                .filter(flower -> flower.getCommonName().matches("[C-S].*"))
                .filter(Flower::isShadePreferred)
                .filter(flower -> flower.getFlowerVaseMaterial()
                        .stream()
                        .anyMatch(materials::contains))
                .mapToDouble(flower -> flower.getPrice() + flower.getWaterConsumptionPerDay() * 365 * 5 * 1.39)
                .sum();
        System.out.println(totalCost);
    }
}