package mar0602.tamz.project.utils;

import java.util.List;

/**
 * @author Ondrej
 * @since 2018-12-18
 */
public class EntityValidationFailedException extends RuntimeException {
    private List<String> errors;

    public EntityValidationFailedException(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
