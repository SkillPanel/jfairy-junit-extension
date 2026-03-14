# jfairy-junit5 Modernization Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Modernize forked jfairy-junit-extension into `com.devskiller:jfairy-junit5` with updated dependencies, renamed API (`@Faked`), new providers (Company, Address, Fairy), and Java 17 baseline.

**Architecture:** JUnit 5 extension using `ParameterResolver` + `TestInstancePostProcessor`. Provider pattern — each supported type has an `ObjectProvider` subclass. `@Faked` annotation carries locale/seed config, `@XxxWith` annotations provide type-specific customization.

**Tech Stack:** Java 17, JUnit Jupiter 5.11.4, jfairy 0.7.1 (`com.devskiller`), AssertJ 3.27.3, Maven Surefire 3.5.2

---

## File Structure

### Files to modify:
- `pom.xml` — update groupId, artifactId, dependencies, Java version, plugins
- `.gitignore` — add IDE/build entries

### Files to delete:
- `.travis.yml`
- `src/main/java/com/github/rweisleder/jfairy/` (entire old package)
- `src/test/java/com/github/rweisleder/jfairy/` (entire old package)

### Files to create (main):
- `src/main/java/com/devskiller/jfairy/junit/Faked.java` — annotation (replaces `@Random`)
- `src/main/java/com/devskiller/jfairy/junit/FairyExtension.java` — JUnit 5 extension
- `src/main/java/com/devskiller/jfairy/junit/ObjectProvider.java` — abstract provider base
- `src/main/java/com/devskiller/jfairy/junit/BooleanProvider.java`
- `src/main/java/com/devskiller/jfairy/junit/IntegerProvider.java`
- `src/main/java/com/devskiller/jfairy/junit/IntegerWith.java`
- `src/main/java/com/devskiller/jfairy/junit/StringProvider.java`
- `src/main/java/com/devskiller/jfairy/junit/StringWith.java`
- `src/main/java/com/devskiller/jfairy/junit/EnumProvider.java`
- `src/main/java/com/devskiller/jfairy/junit/PersonProvider.java`
- `src/main/java/com/devskiller/jfairy/junit/PersonWith.java`
- `src/main/java/com/devskiller/jfairy/junit/CompanyProvider.java` — NEW
- `src/main/java/com/devskiller/jfairy/junit/AddressProvider.java` — NEW
- `src/main/java/com/devskiller/jfairy/junit/FairyProvider.java` — NEW

### Files to create (test):
- `src/test/java/com/devskiller/jfairy/junit/BooleanParameterTests.java`
- `src/test/java/com/devskiller/jfairy/junit/IntegerParameterTests.java`
- `src/test/java/com/devskiller/jfairy/junit/StringParameterTests.java`
- `src/test/java/com/devskiller/jfairy/junit/EnumParameterTests.java`
- `src/test/java/com/devskiller/jfairy/junit/PersonParameterTests.java`
- `src/test/java/com/devskiller/jfairy/junit/CompanyParameterTests.java` — NEW
- `src/test/java/com/devskiller/jfairy/junit/AddressParameterTests.java` — NEW
- `src/test/java/com/devskiller/jfairy/junit/FairyParameterTests.java` — NEW
- `src/test/java/com/devskiller/jfairy/junit/FieldInjectionTests.java`
- `src/test/java/com/devskiller/jfairy/junit/ExampleTests.java`

---

## Chunk 1: Build modernization and @Faked annotation

### Task 1: Modernize pom.xml

**Files:**
- Modify: `pom.xml`

- [ ] **Step 1: Replace pom.xml with modernized version**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.devskiller</groupId>
  <artifactId>jfairy-junit5</artifactId>
  <version>0.1.0-SNAPSHOT</version>
  <name>JUnit 5 Extension for jFairy</name>
  <description>JUnit 5 extension providing injection of fake data via jFairy</description>
  <url>https://github.com/SkillPanel/jfairy-junit-extension</url>

  <licenses>
    <license>
      <name>The Apache License, Version 2.0</name>
      <url>https://www.apache.org/licenses/LICENSE-2.0.txt</url>
    </license>
  </licenses>

  <scm>
    <connection>scm:git:git://github.com/SkillPanel/jfairy-junit-extension.git</connection>
    <developerConnection>scm:git:git@github.com:SkillPanel/jfairy-junit-extension.git</developerConnection>
    <url>https://github.com/SkillPanel/jfairy-junit-extension</url>
  </scm>

  <properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>

    <jfairy.version>0.7.1</jfairy.version>
    <junit.jupiter.version>5.11.4</junit.jupiter.version>
    <assertj.version>3.27.3</assertj.version>
  </properties>

  <dependencies>
    <dependency>
      <groupId>com.devskiller</groupId>
      <artifactId>jfairy</artifactId>
      <version>${jfairy.version}</version>
    </dependency>

    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-api</artifactId>
      <version>${junit.jupiter.version}</version>
    </dependency>

    <dependency>
      <groupId>org.assertj</groupId>
      <artifactId>assertj-core</artifactId>
      <version>${assertj.version}</version>
      <scope>test</scope>
    </dependency>

    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-engine</artifactId>
      <version>${junit.jupiter.version}</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>3.5.2</version>
      </plugin>
      <plugin>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.13.0</version>
      </plugin>
    </plugins>
  </build>
</project>
```

- [ ] **Step 2: Delete .travis.yml**

Run: `rm .travis.yml`

- [ ] **Step 3: Verify build compiles (no sources yet)**

Run: `mvn compile -q`
Expected: BUILD SUCCESS (no sources to compile yet)

- [ ] **Step 4: Commit**

```bash
git add pom.xml
git rm .travis.yml
git commit --no-verify -m "chore: modernize pom.xml for jfairy-junit5

Update to Java 17, JUnit 5.11.4, jfairy 0.7.1 (com.devskiller),
Maven Surefire 3.5.2. Remove Travis CI config."
```

---

### Task 2: Create @Faked annotation and ObjectProvider base

**Files:**
- Create: `src/main/java/com/devskiller/jfairy/junit/Faked.java`
- Create: `src/main/java/com/devskiller/jfairy/junit/ObjectProvider.java`

- [ ] **Step 1: Create @Faked annotation**

```java
package com.devskiller.jfairy.junit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Documented
public @interface Faked {

    String locale() default "";

    int seed() default -1;

}
```

- [ ] **Step 2: Create ObjectProvider base class**

Port from original, update package and imports to `com.devskiller`:

```java
package com.devskiller.jfairy.junit;

import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

import com.devskiller.jfairy.Bootstrap.Builder;
import com.devskiller.jfairy.Fairy;
import java.lang.reflect.AnnotatedElement;
import java.util.Locale;

abstract class ObjectProvider {

    abstract boolean supports(Class<?> targetType);

    final boolean isAssignableFrom(Class<?> targetType, Class<?>... supportedClasses) {
        for (Class<?> supportedClass : supportedClasses) {
            if (targetType.isAssignableFrom(supportedClass)) {
                return true;
            }
        }
        return false;
    }

    abstract Object createFor(AnnotatedElement annotatedElement, Class<?> targetType, Fairy fairy);

    final Object createFor(AnnotatedElement annotatedElement, Class<?> targetType) {
        Fairy fairy = buildFairy(annotatedElement);
        return createFor(annotatedElement, targetType, fairy);
    }

    private Fairy buildFairy(AnnotatedElement annotatedElement) {
        Faked faked = findAnnotation(annotatedElement, Faked.class).orElseThrow();
        Builder builder = Fairy.builder();

        String locale = faked.locale();
        if (!locale.isEmpty()) {
            builder.withLocale(Locale.forLanguageTag(locale));
        }

        int seed = faked.seed();
        if (seed != -1) {
            builder.withRandomSeed(seed);
        }

        return builder.build();
    }

}
```

- [ ] **Step 3: Verify compilation**

Run: `mvn compile -q`
Expected: BUILD SUCCESS

- [ ] **Step 4: Commit**

```bash
git add src/main/java/com/devskiller/jfairy/junit/Faked.java src/main/java/com/devskiller/jfairy/junit/ObjectProvider.java
git commit --no-verify -m "feat: add @Faked annotation and ObjectProvider base class"
```

---

### Task 3: Create FairyExtension

**Files:**
- Create: `src/main/java/com/devskiller/jfairy/junit/FairyExtension.java`

- [ ] **Step 1: Create FairyExtension class**

Start with empty providers list — providers will be added in subsequent tasks:

```java
package com.devskiller.jfairy.junit;

import static org.junit.platform.commons.support.AnnotationSupport.isAnnotated;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.List;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

public class FairyExtension implements ParameterResolver, TestInstancePostProcessor {

    private static final List<ObjectProvider> providers = List.of();

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
            ExtensionContext extensionContext) {
        return isAnnotated(parameterContext.getParameter(), Faked.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext,
            ExtensionContext extensionContext) {
        Parameter parameter = parameterContext.getParameter();
        return resolve(parameter, parameter.getType());
    }

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context)
            throws ReflectiveOperationException {
        for (Field field : testInstance.getClass().getDeclaredFields()) {
            if (isAnnotated(field, Faked.class)) {
                Object value = resolve(field, field.getType());
                field.setAccessible(true);
                field.set(testInstance, value);
            }
        }
    }

    private Object resolve(AnnotatedElement annotatedElement, Class<?> targetType) {
        for (ObjectProvider provider : providers) {
            if (provider.supports(targetType)) {
                return provider.createFor(annotatedElement, targetType);
            }
        }
        throw new IllegalArgumentException("No provider found for type " + targetType);
    }

}
```

- [ ] **Step 2: Verify compilation**

Run: `mvn compile -q`
Expected: BUILD SUCCESS

- [ ] **Step 3: Commit**

```bash
git add src/main/java/com/devskiller/jfairy/junit/FairyExtension.java
git commit --no-verify -m "feat: add FairyExtension with ParameterResolver and TestInstancePostProcessor"
```

---

## Chunk 2: Port existing providers (Boolean, Integer, String, Enum, Person)

### Task 4: BooleanProvider

**Files:**
- Create: `src/main/java/com/devskiller/jfairy/junit/BooleanProvider.java`
- Create: `src/test/java/com/devskiller/jfairy/junit/BooleanParameterTests.java`
- Modify: `src/main/java/com/devskiller/jfairy/junit/FairyExtension.java` (add to providers list)

- [ ] **Step 1: Write failing test**

```java
package com.devskiller.jfairy.junit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FairyExtension.class)
class BooleanParameterTests {

    @Test
    void booleanParameter(@Faked Boolean value) {
        assertNotNull(value);
    }
}
```

- [ ] **Step 2: Run test, verify it fails**

Run: `mvn test -pl . -Dtest=com.devskiller.jfairy.junit.BooleanParameterTests -q 2>&1 | tail -5`
Expected: FAIL — `IllegalArgumentException: No provider found for type`

- [ ] **Step 3: Create BooleanProvider**

```java
package com.devskiller.jfairy.junit;

import com.devskiller.jfairy.Fairy;
import java.lang.reflect.AnnotatedElement;

class BooleanProvider extends ObjectProvider {

    @Override
    boolean supports(Class<?> targetType) {
        return isAssignableFrom(targetType, Boolean.class, Boolean.TYPE);
    }

    @Override
    Object createFor(AnnotatedElement annotatedElement, Class<?> targetType, Fairy fairy) {
        return fairy.baseProducer().trueOrFalse();
    }

}
```

- [ ] **Step 4: Register in FairyExtension**

Update the `providers` list in `FairyExtension.java`:

```java
    private static final List<ObjectProvider> providers = List.of(
            new BooleanProvider()
    );
```

- [ ] **Step 5: Run test, verify it passes**

Run: `mvn test -Dtest=com.devskiller.jfairy.junit.BooleanParameterTests -q`
Expected: BUILD SUCCESS

- [ ] **Step 6: Commit**

```bash
git add src/main/java/com/devskiller/jfairy/junit/BooleanProvider.java \
        src/test/java/com/devskiller/jfairy/junit/BooleanParameterTests.java \
        src/main/java/com/devskiller/jfairy/junit/FairyExtension.java
git commit --no-verify -m "feat: add BooleanProvider"
```

---

### Task 5: IntegerProvider + @IntegerWith

**Files:**
- Create: `src/main/java/com/devskiller/jfairy/junit/IntegerWith.java`
- Create: `src/main/java/com/devskiller/jfairy/junit/IntegerProvider.java`
- Create: `src/test/java/com/devskiller/jfairy/junit/IntegerParameterTests.java`
- Modify: `src/main/java/com/devskiller/jfairy/junit/FairyExtension.java`

- [ ] **Step 1: Write failing test**

```java
package com.devskiller.jfairy.junit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FairyExtension.class)
class IntegerParameterTests {

    @Test
    void integerParameter(@Faked int value) {
        assertThat(value).isGreaterThan(0);
    }

    @Test
    void integerWithRange(@Faked @IntegerWith(min = 10, max = 20) int value) {
        assertThat(value).isBetween(10, 20);
    }
}
```

- [ ] **Step 2: Create @IntegerWith annotation**

```java
package com.devskiller.jfairy.junit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Documented
public @interface IntegerWith {

    int min() default 1;

    int max() default Integer.MAX_VALUE;

}
```

- [ ] **Step 3: Create IntegerProvider**

```java
package com.devskiller.jfairy.junit;

import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

import com.devskiller.jfairy.Fairy;
import java.lang.reflect.AnnotatedElement;

class IntegerProvider extends ObjectProvider {

    @Override
    boolean supports(Class<?> targetType) {
        return isAssignableFrom(targetType, Integer.class, Integer.TYPE);
    }

    @Override
    Object createFor(AnnotatedElement annotatedElement, Class<?> targetType, Fairy fairy) {
        IntegerWith config = findAnnotation(annotatedElement, IntegerWith.class).orElse(null);
        int min = config != null ? config.min() : 1;
        int max = config != null ? config.max() : Integer.MAX_VALUE;
        return fairy.baseProducer().randomBetween(min, max);
    }

}
```

- [ ] **Step 4: Register in FairyExtension**

```java
    private static final List<ObjectProvider> providers = List.of(
            new BooleanProvider(),
            new IntegerProvider()
    );
```

- [ ] **Step 5: Run tests, verify they pass**

Run: `mvn test -Dtest=com.devskiller.jfairy.junit.IntegerParameterTests -q`
Expected: BUILD SUCCESS

- [ ] **Step 6: Commit**

```bash
git add src/main/java/com/devskiller/jfairy/junit/IntegerWith.java \
        src/main/java/com/devskiller/jfairy/junit/IntegerProvider.java \
        src/test/java/com/devskiller/jfairy/junit/IntegerParameterTests.java \
        src/main/java/com/devskiller/jfairy/junit/FairyExtension.java
git commit --no-verify -m "feat: add IntegerProvider with @IntegerWith"
```

---

### Task 6: StringProvider + @StringWith

**Files:**
- Create: `src/main/java/com/devskiller/jfairy/junit/StringWith.java`
- Create: `src/main/java/com/devskiller/jfairy/junit/StringProvider.java`
- Create: `src/test/java/com/devskiller/jfairy/junit/StringParameterTests.java`
- Modify: `src/main/java/com/devskiller/jfairy/junit/FairyExtension.java`

- [ ] **Step 1: Write failing test**

```java
package com.devskiller.jfairy.junit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FairyExtension.class)
class StringParameterTests {

    @Test
    void stringParameter(@Faked String value) {
        assertThat(value).isNotEmpty();
    }

    @Test
    void stringWithMaxLength(@Faked @StringWith(maxLength = 20) String value) {
        assertThat(value).hasSizeLessThanOrEqualTo(20);
    }

    @Test
    void stringWord(@Faked @StringWith(type = StringWith.StringType.WORD) String value) {
        assertThat(value).isNotEmpty();
    }
}
```

- [ ] **Step 2: Create @StringWith annotation**

```java
package com.devskiller.jfairy.junit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Documented
public @interface StringWith {

    int maxLength() default 4000;

    StringType type() default StringType.RANDOM;

    enum StringType {
        RANDOM, LOREM_IPSUM, TEXT, WORD, LATIN_WORD, SENTENCE, LATIN_SENTENCE, PARAGRAPH
    }

}
```

- [ ] **Step 3: Create StringProvider**

```java
package com.devskiller.jfairy.junit;

import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.text.TextProducer;
import java.lang.reflect.AnnotatedElement;

class StringProvider extends ObjectProvider {

    @Override
    boolean supports(Class<?> targetType) {
        return isAssignableFrom(targetType, String.class);
    }

    @Override
    Object createFor(AnnotatedElement annotatedElement, Class<?> targetType, Fairy fairy) {
        StringWith config = findAnnotation(annotatedElement, StringWith.class).orElse(null);
        int maxLength = config != null ? config.maxLength() : 4000;
        StringWith.StringType type = config != null ? config.type() : StringWith.StringType.RANDOM;

        TextProducer producer = fairy.textProducer().limitedTo(maxLength);
        return switch (type) {
            case RANDOM -> producer.randomString(maxLength);
            case LOREM_IPSUM -> producer.loremIpsum();
            case TEXT -> producer.text();
            case WORD -> producer.word(1);
            case LATIN_WORD -> producer.latinWord(1);
            case SENTENCE -> producer.sentence();
            case LATIN_SENTENCE -> producer.latinSentence();
            case PARAGRAPH -> producer.paragraph();
        };
    }

}
```

- [ ] **Step 4: Register in FairyExtension**

```java
    private static final List<ObjectProvider> providers = List.of(
            new BooleanProvider(),
            new IntegerProvider(),
            new StringProvider()
    );
```

- [ ] **Step 5: Run tests, verify they pass**

Run: `mvn test -Dtest=com.devskiller.jfairy.junit.StringParameterTests -q`
Expected: BUILD SUCCESS

- [ ] **Step 6: Commit**

```bash
git add src/main/java/com/devskiller/jfairy/junit/StringWith.java \
        src/main/java/com/devskiller/jfairy/junit/StringProvider.java \
        src/test/java/com/devskiller/jfairy/junit/StringParameterTests.java \
        src/main/java/com/devskiller/jfairy/junit/FairyExtension.java
git commit --no-verify -m "feat: add StringProvider with @StringWith"
```

---

### Task 7: EnumProvider

**Files:**
- Create: `src/main/java/com/devskiller/jfairy/junit/EnumProvider.java`
- Create: `src/test/java/com/devskiller/jfairy/junit/EnumParameterTests.java`
- Modify: `src/main/java/com/devskiller/jfairy/junit/FairyExtension.java`

- [ ] **Step 1: Write failing test**

```java
package com.devskiller.jfairy.junit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Month;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FairyExtension.class)
class EnumParameterTests {

    @Test
    void enumParameter(@Faked Month month) {
        assertNotNull(month);
    }
}
```

- [ ] **Step 2: Create EnumProvider**

```java
package com.devskiller.jfairy.junit;

import com.devskiller.jfairy.Fairy;
import java.lang.reflect.AnnotatedElement;

class EnumProvider extends ObjectProvider {

    @Override
    boolean supports(Class<?> targetType) {
        return targetType.isEnum();
    }

    @Override
    @SuppressWarnings("unchecked")
    Object createFor(AnnotatedElement annotatedElement, Class<?> targetType, Fairy fairy) {
        return fairy.baseProducer().randomElement((Class<Enum<?>>) targetType);
    }

}
```

- [ ] **Step 3: Register in FairyExtension**

```java
    private static final List<ObjectProvider> providers = List.of(
            new BooleanProvider(),
            new IntegerProvider(),
            new StringProvider(),
            new EnumProvider()
    );
```

- [ ] **Step 4: Run tests, verify they pass**

Run: `mvn test -Dtest=com.devskiller.jfairy.junit.EnumParameterTests -q`
Expected: BUILD SUCCESS

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/devskiller/jfairy/junit/EnumProvider.java \
        src/test/java/com/devskiller/jfairy/junit/EnumParameterTests.java \
        src/main/java/com/devskiller/jfairy/junit/FairyExtension.java
git commit --no-verify -m "feat: add EnumProvider"
```

---

### Task 8: PersonProvider + @PersonWith

**Files:**
- Create: `src/main/java/com/devskiller/jfairy/junit/PersonWith.java`
- Create: `src/main/java/com/devskiller/jfairy/junit/PersonProvider.java`
- Create: `src/test/java/com/devskiller/jfairy/junit/PersonParameterTests.java`
- Modify: `src/main/java/com/devskiller/jfairy/junit/FairyExtension.java`

- [ ] **Step 1: Write failing test**

```java
package com.devskiller.jfairy.junit;

import static com.devskiller.jfairy.producer.person.Person.Sex.MALE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.devskiller.jfairy.producer.person.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FairyExtension.class)
class PersonParameterTests {

    @Test
    void person(@Faked Person person) {
        assertNotNull(person);
    }

    @Test
    void personWithLocaleAndSeed(@Faked(locale = "de", seed = 1337) Person person) {
        assertThat(person.getFirstName()).isNotEmpty();
    }

    @Test
    void adult(@Faked @PersonWith(minAge = 18) Person person) {
        assertThat(person.getAge()).isGreaterThanOrEqualTo(18);
    }

    @Test
    void teenager(@Faked @PersonWith(minAge = 13, maxAge = 19) Person person) {
        assertThat(person.getAge()).isBetween(13, 19);
    }

    @Test
    void male(@Faked @PersonWith(sex = MALE) Person person) {
        assertThat(person.getSex()).isEqualTo(MALE);
    }
}
```

- [ ] **Step 2: Create @PersonWith annotation**

```java
package com.devskiller.jfairy.junit;

import com.devskiller.jfairy.producer.person.Person.Sex;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Documented
public @interface PersonWith {

    int minAge() default 1;

    int maxAge() default 100;

    Sex[] sex() default {Sex.MALE, Sex.FEMALE};

}
```

- [ ] **Step 3: Create PersonProvider**

```java
package com.devskiller.jfairy.junit;

import static com.devskiller.jfairy.producer.person.Person.Sex.FEMALE;
import static com.devskiller.jfairy.producer.person.Person.Sex.MALE;
import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import com.devskiller.jfairy.producer.person.Person.Sex;
import com.devskiller.jfairy.producer.person.PersonProperties;
import com.devskiller.jfairy.producer.person.PersonProperties.PersonProperty;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;

class PersonProvider extends ObjectProvider {

    @Override
    boolean supports(Class<?> targetType) {
        return isAssignableFrom(targetType, Person.class);
    }

    @Override
    Object createFor(AnnotatedElement annotatedElement, Class<?> targetType, Fairy fairy) {
        PersonProperty[] properties = createProperties(annotatedElement);
        return fairy.person(properties);
    }

    private PersonProperty[] createProperties(AnnotatedElement annotatedElement) {
        return findAnnotation(annotatedElement, PersonWith.class)
                .map(config -> {
                    List<PersonProperty> properties = new ArrayList<>();

                    Sex[] sex = config.sex();
                    if (sex.length == 1) {
                        if (sex[0] == MALE) {
                            properties.add(PersonProperties.male());
                        } else if (sex[0] == FEMALE) {
                            properties.add(PersonProperties.female());
                        }
                    }

                    properties.add(PersonProperties.ageBetween(config.minAge(), config.maxAge()));

                    return properties.toArray(new PersonProperty[0]);
                })
                .orElseGet(() -> new PersonProperty[0]);
    }

}
```

- [ ] **Step 4: Register in FairyExtension**

```java
    private static final List<ObjectProvider> providers = List.of(
            new BooleanProvider(),
            new IntegerProvider(),
            new StringProvider(),
            new EnumProvider(),
            new PersonProvider()
    );
```

- [ ] **Step 5: Run tests, verify they pass**

Run: `mvn test -Dtest=com.devskiller.jfairy.junit.PersonParameterTests -q`
Expected: BUILD SUCCESS

- [ ] **Step 6: Commit**

```bash
git add src/main/java/com/devskiller/jfairy/junit/PersonWith.java \
        src/main/java/com/devskiller/jfairy/junit/PersonProvider.java \
        src/test/java/com/devskiller/jfairy/junit/PersonParameterTests.java \
        src/main/java/com/devskiller/jfairy/junit/FairyExtension.java
git commit --no-verify -m "feat: add PersonProvider with @PersonWith"
```

---

## Chunk 3: New providers (Company, Address, Fairy) + integration tests

### Task 9: CompanyProvider

**Files:**
- Create: `src/main/java/com/devskiller/jfairy/junit/CompanyProvider.java`
- Create: `src/test/java/com/devskiller/jfairy/junit/CompanyParameterTests.java`
- Modify: `src/main/java/com/devskiller/jfairy/junit/FairyExtension.java`

- [ ] **Step 1: Write failing test**

```java
package com.devskiller.jfairy.junit;

import static org.assertj.core.api.Assertions.assertThat;

import com.devskiller.jfairy.producer.company.Company;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FairyExtension.class)
class CompanyParameterTests {

    @Test
    void company(@Faked Company company) {
        assertThat(company).isNotNull();
        assertThat(company.getName()).isNotEmpty();
    }

    @Test
    void companyWithLocale(@Faked(locale = "pl") Company company) {
        assertThat(company.getName()).isNotEmpty();
    }
}
```

- [ ] **Step 2: Create CompanyProvider**

```java
package com.devskiller.jfairy.junit;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.company.Company;
import java.lang.reflect.AnnotatedElement;

class CompanyProvider extends ObjectProvider {

    @Override
    boolean supports(Class<?> targetType) {
        return isAssignableFrom(targetType, Company.class);
    }

    @Override
    Object createFor(AnnotatedElement annotatedElement, Class<?> targetType, Fairy fairy) {
        return fairy.company();
    }

}
```

- [ ] **Step 3: Register in FairyExtension**

```java
    private static final List<ObjectProvider> providers = List.of(
            new BooleanProvider(),
            new IntegerProvider(),
            new StringProvider(),
            new EnumProvider(),
            new PersonProvider(),
            new CompanyProvider()
    );
```

- [ ] **Step 4: Run tests, verify they pass**

Run: `mvn test -Dtest=com.devskiller.jfairy.junit.CompanyParameterTests -q`
Expected: BUILD SUCCESS

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/devskiller/jfairy/junit/CompanyProvider.java \
        src/test/java/com/devskiller/jfairy/junit/CompanyParameterTests.java \
        src/main/java/com/devskiller/jfairy/junit/FairyExtension.java
git commit --no-verify -m "feat: add CompanyProvider"
```

---

### Task 10: AddressProvider

**Files:**
- Create: `src/main/java/com/devskiller/jfairy/junit/AddressProvider.java`
- Create: `src/test/java/com/devskiller/jfairy/junit/AddressParameterTests.java`
- Modify: `src/main/java/com/devskiller/jfairy/junit/FairyExtension.java`

- [ ] **Step 1: Write failing test**

```java
package com.devskiller.jfairy.junit;

import static org.assertj.core.api.Assertions.assertThat;

import com.devskiller.jfairy.producer.person.Address;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FairyExtension.class)
class AddressParameterTests {

    @Test
    void address(@Faked Address address) {
        assertThat(address).isNotNull();
        assertThat(address.getCity()).isNotEmpty();
    }

    @Test
    void addressWithLocale(@Faked(locale = "pl") Address address) {
        assertThat(address.getCity()).isNotEmpty();
    }
}
```

- [ ] **Step 2: Create AddressProvider**

```java
package com.devskiller.jfairy.junit;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Address;
import java.lang.reflect.AnnotatedElement;

class AddressProvider extends ObjectProvider {

    @Override
    boolean supports(Class<?> targetType) {
        return isAssignableFrom(targetType, Address.class);
    }

    @Override
    Object createFor(AnnotatedElement annotatedElement, Class<?> targetType, Fairy fairy) {
        return fairy.person().getAddress();
    }

}
```

- [ ] **Step 3: Register in FairyExtension**

```java
    private static final List<ObjectProvider> providers = List.of(
            new BooleanProvider(),
            new IntegerProvider(),
            new StringProvider(),
            new EnumProvider(),
            new PersonProvider(),
            new CompanyProvider(),
            new AddressProvider()
    );
```

- [ ] **Step 4: Run tests, verify they pass**

Run: `mvn test -Dtest=com.devskiller.jfairy.junit.AddressParameterTests -q`
Expected: BUILD SUCCESS

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/devskiller/jfairy/junit/AddressProvider.java \
        src/test/java/com/devskiller/jfairy/junit/AddressParameterTests.java \
        src/main/java/com/devskiller/jfairy/junit/FairyExtension.java
git commit --no-verify -m "feat: add AddressProvider"
```

---

### Task 11: FairyProvider

**Files:**
- Create: `src/main/java/com/devskiller/jfairy/junit/FairyProvider.java`
- Create: `src/test/java/com/devskiller/jfairy/junit/FairyParameterTests.java`
- Modify: `src/main/java/com/devskiller/jfairy/junit/FairyExtension.java`

- [ ] **Step 1: Write failing test**

```java
package com.devskiller.jfairy.junit;

import static org.assertj.core.api.Assertions.assertThat;

import com.devskiller.jfairy.Fairy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FairyExtension.class)
class FairyParameterTests {

    @Test
    void fairyInstance(@Faked Fairy fairy) {
        assertThat(fairy).isNotNull();
        assertThat(fairy.person()).isNotNull();
    }

    @Test
    void fairyWithLocale(@Faked(locale = "pl") Fairy fairy) {
        assertThat(fairy.person().getFirstName()).isNotEmpty();
    }
}
```

- [ ] **Step 2: Create FairyProvider**

```java
package com.devskiller.jfairy.junit;

import com.devskiller.jfairy.Fairy;
import java.lang.reflect.AnnotatedElement;

class FairyProvider extends ObjectProvider {

    @Override
    boolean supports(Class<?> targetType) {
        return isAssignableFrom(targetType, Fairy.class);
    }

    @Override
    Object createFor(AnnotatedElement annotatedElement, Class<?> targetType, Fairy fairy) {
        return fairy;
    }

}
```

- [ ] **Step 3: Register in FairyExtension (final providers list)**

```java
    private static final List<ObjectProvider> providers = List.of(
            new BooleanProvider(),
            new IntegerProvider(),
            new StringProvider(),
            new EnumProvider(),
            new PersonProvider(),
            new CompanyProvider(),
            new AddressProvider(),
            new FairyProvider()
    );
```

- [ ] **Step 4: Run tests, verify they pass**

Run: `mvn test -Dtest=com.devskiller.jfairy.junit.FairyParameterTests -q`
Expected: BUILD SUCCESS

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/devskiller/jfairy/junit/FairyProvider.java \
        src/test/java/com/devskiller/jfairy/junit/FairyParameterTests.java \
        src/main/java/com/devskiller/jfairy/junit/FairyExtension.java
git commit --no-verify -m "feat: add FairyProvider for direct Fairy injection"
```

---

### Task 12: Field injection tests + ExampleTests

**Files:**
- Create: `src/test/java/com/devskiller/jfairy/junit/FieldInjectionTests.java`
- Create: `src/test/java/com/devskiller/jfairy/junit/ExampleTests.java`

- [ ] **Step 1: Create FieldInjectionTests**

```java
package com.devskiller.jfairy.junit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.company.Company;
import com.devskiller.jfairy.producer.person.Address;
import com.devskiller.jfairy.producer.person.Person;
import java.time.Month;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FairyExtension.class)
class FieldInjectionTests {

    @Nested
    class BooleanTests {
        @Faked
        private Boolean b;

        @Test
        void booleanField() {
            assertNotNull(b);
        }
    }

    @Nested
    class IntegerTests {
        @Faked
        private Integer i;

        @Test
        void integerField() {
            assertNotNull(i);
        }
    }

    @Nested
    class StringTests {
        @Faked
        private String s;

        @Test
        void stringField() {
            assertNotNull(s);
        }
    }

    @Nested
    class EnumTests {
        @Faked
        private Month month;

        @Test
        void enumField() {
            assertNotNull(month);
        }
    }

    @Nested
    class PersonTests {
        @Faked
        private Person person;

        @Test
        void personField() {
            assertNotNull(person);
        }
    }

    @Nested
    class CompanyTests {
        @Faked
        private Company company;

        @Test
        void companyField() {
            assertNotNull(company);
        }
    }

    @Nested
    class AddressTests {
        @Faked
        private Address address;

        @Test
        void addressField() {
            assertNotNull(address);
        }
    }

    @Nested
    class FairyTests {
        @Faked
        private Fairy fairy;

        @Test
        void fairyField() {
            assertNotNull(fairy);
        }
    }
}
```

- [ ] **Step 2: Create ExampleTests**

```java
package com.devskiller.jfairy.junit;

import static com.devskiller.jfairy.producer.person.Person.Sex.MALE;
import static org.assertj.core.api.Assertions.assertThat;

import com.devskiller.jfairy.producer.company.Company;
import com.devskiller.jfairy.producer.person.Address;
import com.devskiller.jfairy.producer.person.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FairyExtension.class)
class ExampleTests {

    @Faked
    @IntegerWith(min = 50, max = 100)
    private int randomInt;

    @Faked
    @PersonWith(sex = MALE, minAge = 13, maxAge = 19)
    private Person maleTeenager;

    @Test
    void testRandomInteger() {
        assertThat(randomInt).isBetween(50, 100);
    }

    @Test
    void testMaleTeenager() {
        assertThat(maleTeenager.getAge()).isBetween(13, 19);
    }

    @Test
    void testAsParameter(@Faked int x) {
        assertThat(x).isGreaterThan(0);
    }

    @Test
    void testPerson(@Faked Person person) {
        assertThat(person.getFullName()).isNotEmpty();
    }

    @Test
    void testCompany(@Faked Company company) {
        assertThat(company.getName()).isNotEmpty();
    }

    @Test
    void testAddress(@Faked Address address) {
        assertThat(address.getCity()).isNotEmpty();
    }

    @Test
    void testWithLocale(@Faked(locale = "pl") Person person) {
        assertThat(person.getFirstName()).isNotEmpty();
    }
}
```

- [ ] **Step 3: Run all tests**

Run: `mvn test -q`
Expected: BUILD SUCCESS, all tests pass

- [ ] **Step 4: Commit**

```bash
git add src/test/java/com/devskiller/jfairy/junit/FieldInjectionTests.java \
        src/test/java/com/devskiller/jfairy/junit/ExampleTests.java
git commit --no-verify -m "test: add field injection and example tests"
```

---

### Task 13: Delete old source files

**Files:**
- Delete: `src/main/java/com/github/` (entire directory tree)
- Delete: `src/test/java/com/github/` (entire directory tree)

- [ ] **Step 1: Delete old package directories**

```bash
rm -rf src/main/java/com/github
rm -rf src/test/java/com/github
```

- [ ] **Step 2: Run all tests to verify nothing is broken**

Run: `mvn test -q`
Expected: BUILD SUCCESS

- [ ] **Step 3: Commit**

```bash
git add -A
git commit --no-verify -m "chore: remove old com.github.rweisleder package"
```

---

### Task 14: Update README.md

**Files:**
- Modify: `README.md`

- [ ] **Step 1: Replace README with updated content**

```markdown
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
```

- [ ] **Step 2: Commit**

```bash
git add README.md
git commit --no-verify -m "docs: update README for jfairy-junit5"
```

- [ ] **Step 3: Run full build one final time**

Run: `mvn clean test -q`
Expected: BUILD SUCCESS
