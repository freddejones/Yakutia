package se.freddejones.yakutia.application.service.exceptions;

/**
 * User: Fredde
 * Date: 5/12/13 12:07 PM
 */
public class PlayerDoNotOwnLandAreaException extends Exception {

    public PlayerDoNotOwnLandAreaException(String message) {
        super(message);
    }
}
