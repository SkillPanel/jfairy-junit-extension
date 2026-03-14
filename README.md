# jfairy-junit5

JUnit 5 extension for [jFairy](https://github.com/SkillPanel/jfairy) — inject fake test data into your tests.

## Usage

```java
@ExtendWith(FairyExtension.class)
class MyTest {

    @Test
    void testPerson(@Faked Person person) {
        assertNotNull(person.getFullName());
    }

    @Test
    void testCompany(@Faked Company company) {
        assertNotNull(company.getName());
    }

    @Test
    void testWithLocale(@Faked(locale = "pl") Person person) {
        assertNotNull(person.getFirstName());
    }

    @Test
    void testWithSeed(@Faked(seed = 42) Person person) {
        // deterministic — same seed produces same data
        assertNotNull(person.getFullName());
    }
}
```

## Supported types

| Type | Annotation | Customization |
|------|-----------|---------------|
| `Person` | `@Faked` | `@PersonWith(sex, minAge, maxAge)` |
| `Company` | `@Faked` | — |
| `Address` | `@Faked` | — |
| `Fairy` | `@Faked` | — |
| `String` | `@Faked` | `@StringWith(maxLength, type)` |
| `Integer` / `int` | `@Faked` | `@IntegerWith(min, max)` |
| `Boolean` / `boolean` | `@Faked` | — |
| Any `Enum` | `@Faked` | — |

## Field injection

```java
@ExtendWith(FairyExtension.class)
class MyTest {

    @Faked
    @PersonWith(sex = MALE, minAge = 18)
    private Person adultMale;

    @Faked
    @IntegerWith(min = 1, max = 100)
    private int score;

    @Test
    void test() {
        assertNotNull(adultMale);
        assertThat(score).isBetween(1, 100);
    }
}
```

## Maven

```xml
<dependency>
    <groupId>com.devskiller</groupId>
    <artifactId>jfairy-junit5</artifactId>
    <version>0.1.0</version>
    <scope>test</scope>
</dependency>
```

## License

Apache License 2.0
