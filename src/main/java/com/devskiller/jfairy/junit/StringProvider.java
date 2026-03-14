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
        int maxLength = findAnnotation(annotatedElement, StringWith.class).map(StringWith::maxLength).orElse(4000);
        StringWith.StringType type = findAnnotation(annotatedElement, StringWith.class).map(StringWith::type).orElse(StringWith.StringType.RANDOM);

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
