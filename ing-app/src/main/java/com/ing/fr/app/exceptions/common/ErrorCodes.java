package com.ing.fr.app.exceptions.common;

/***
 * @author Gautam Sahoo
 * @version 1.0
 * @apiNote ErrorCodes is the class maintains all custom error codes which is important for UI for Localization purpose
 */
public class ErrorCodes {

    public static final String MINIMUM_DEPOSIT_AMOUNT_VALIDATION_FAILED = "E00001";
    public static final String OVERDRAFT_FACILITY_VALIDATION_FAILED = "E00002";
    public static final String ENTITY_NOT_FOUND = "E00003";
    public static final String ENTITY_ALREADY_PRESENT = "E00004";

    private ErrorCodes() {
    }
}
