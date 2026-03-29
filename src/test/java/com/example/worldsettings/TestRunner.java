package com.example.worldsettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple test runner for ModdedItemsTest.
 * This class runs the test methods directly and reports pass/fail results.
 */
public class TestRunner {

    public static void main(String[] args) {
        ModdedItemsTest test = new ModdedItemsTest();
        List<String> failures = new ArrayList<>();

        runTest("happyPathReturnsExpectedDropChanceForKnownItem", () -> test.happyPathReturnsExpectedDropChanceForKnownItem(), failures);
        runTest("edgeCaseReturnsZeroForEmptyItemName", () -> test.edgeCaseReturnsZeroForEmptyItemName(), failures);
        runTest("integrationTestWorldSettingsAndDropChanceRegistryWorkTogether", () -> test.integrationTestWorldSettingsAndDropChanceRegistryWorkTogether(), failures);

        System.out.println();
        if (failures.isEmpty()) {
            System.out.println("ALL TESTS PASSED");
            System.exit(0);
        } else {
            System.out.println("TESTS FAILED: " + failures.size());
            failures.forEach(System.out::println);
            System.exit(1);
        }
    }

    private static void runTest(String name, Runnable test, List<String> failures) {
        try {
            test.run();
            System.out.println("PASS: " + name);
        } catch (Throwable thrown) {
            failures.add("FAIL: " + name + " - " + thrown.getClass().getSimpleName() + ": " + thrown.getMessage());
            System.out.println("FAIL: " + name);
            thrown.printStackTrace(System.out);
        }
    }
}
