package pl.sggw.support.webservice.dao.exception;

import pl.sggw.support.webservice.dao.validator.violation.Violation;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Kamil on 2017-11-11.
 */
public class ConstraintViolationException extends RuntimeException {

    private final Set<Violation<?>> violations;

    public ConstraintViolationException(Set<? extends Violation<?>> violations) {
        this(null, violations);
    }

    public ConstraintViolationException(String message, Set<? extends Violation<?>> violations) {
        super(message);
        if(violations == null) {
            this.violations = null;
        } else {
            this.violations = new HashSet(violations);
        }
    }

    public Set<Violation<?>> getViolations() {
        return violations;
    }
}
