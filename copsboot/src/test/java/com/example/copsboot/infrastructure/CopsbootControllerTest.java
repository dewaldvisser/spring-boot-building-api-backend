package com.example.copsboot.infrastructure;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Custom annotation for all {@link org.springframework.stereotype.Controller Controller} tests on the project. By using
 * this single annotation, everything is configured properly to test a controller:
 * <ul>
 * <li>Import of {@link CopsbootControllerTestConfiguration}</li>
 * <li><code>test</code> profile active</li>
 * </ul>
 * <p>
 * Example usage:
 * <pre>
 * &#64;RunWith(SpringRunner.class)
 * &#64;CopsbootControllerTest(UserController.class)
 * public class UserControllerTest {
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@WebMvcTest
@ContextConfiguration(classes = CopsbootControllerTestConfiguration.class)
@ActiveProfiles(SpringProfiles.TEST)
public @interface CopsbootControllerTest {

    @AliasFor(annotation = WebMvcTest.class, attribute = "value")
    Class<?>[] value() default {};

    @AliasFor(annotation = WebMvcTest.class, attribute = "controllers")
    Class<?>[] controllers() default {};
}
