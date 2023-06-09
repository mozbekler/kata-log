package ChristmasLightsTests;

import ChristmasLights.LightMatrix;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ChristmasLights.Area;

import static org.junit.jupiter.api.Assertions.*;

class LightMatrixTest {

    private LightMatrix testee;

    @BeforeEach
    public void initializeLightMatrix() {
        testee = new LightMatrix();
    }

    @Test
    void initialBrightness() {
        int expectedBrightness = 0;
        int actualBrightness = testee.getTotalBrightness();

        assertEquals(expectedBrightness, actualBrightness);
    }

    @Test
    void turnOneLightOnAndOffAgain() {
        testee.turnOn(new Area(0, 0, 0, 0));
        testee.turnOff(new Area(0, 0, 0, 0));

        int expectedBrightness = 0;
        int actualBrightness = testee.getTotalBrightness();

        assertEquals(expectedBrightness, actualBrightness);
    }

    @Test
    void turnOffLightInTurnedOnArea() {
        testee.turnOn(new Area(0, 0, 1, 1));

        int expectedBrightness = 4;
        int actualBrightness = testee.getTotalBrightness();

        assertEquals(expectedBrightness, actualBrightness);

        testee.turnOff(new Area(0, 0, 0, 0));

        assertEquals(3, testee.getTotalBrightness());

    }

    @Test
    void turnOn3LightsThenToggle1000() {
        testee.turnOn(new Area(0, 0, 0, 0));
        testee.turnOn(new Area(2, 0, 2, 0));
        testee.turnOn(new Area(999, 0, 999, 0));

        assertEquals(3, testee.getTotalBrightness(), "precondition failed");

        testee.toggle(new Area(0, 0, 999, 0));

        assertEquals(2003, testee.getTotalBrightness());
    }

    @Test
    void turnOnSomeLightsThenToggleNotAllLights() {
        // Turn on three lights in row zero
        testee.turnOn(new Area(0, 0, 0, 0));
        testee.turnOn(new Area(2, 0, 2, 0));
        testee.turnOn(new Area(999, 0, 999, 0));

        // Turn on one light in row two
        testee.turnOn(new Area(2, 998, 2, 998));

        // Check that four lights turned on
        assertEquals(4, testee.getTotalBrightness(), "precondition failed");

        // Toggle 1000 lights in row 0
        testee.toggle(new Area(0, 0, 999, 0));

        assertEquals(2004, testee.getTotalBrightness());
    }

    @Test
    void testTurnOffActiveLightsTurnsOff() {
        Area area = new Area(499, 499, 500, 500);
        int expectedBrightness = 1_000_000 - 4;

        testee.turnOn(new Area(0, 0, 999, 999));
        testee.turnOff(area);

        assertEquals(expectedBrightness, testee.getTotalBrightness(), "Completely lit matrix, turn off from 499,499 to 500,500 => 1_000_000 - 4");
    }

    @Test
    void testTurnOffMixedLightsTurnsOff() {
        Area area = new Area(499, 499, 500, 500);
        int expectedBrightness = 500_000 - 2;

        testee.turnOn(new Area(0, 0, 999, 499));

        assertEquals(500_000, testee.getTotalBrightness(), "Precondition not met.");

        testee.turnOff(area);

        assertEquals(expectedBrightness, testee.getTotalBrightness(), "Partially lit matrix, turn off from 499,499 to 500,500 => 500_000 - 2");
    }

    @Test
    void testTurnOffDeactivatedLightsLeavesOff() {
        Area area = new Area(499, 499, 500, 500);
        int expectedBrightness = 0;

        testee.turnOff(area);

        assertEquals(expectedBrightness, testee.getTotalBrightness(), "Unlit matrix, turn off from 499,499 to 500,500 => 0");
    }

    @Test
    void toggleOverlappingAreas() {
        Area area = new Area(0, 0, 1, 1);

        //2      0 0 0
        //1      0 0 0
        //0      0 0 0
        //Y^/x>  0 1 2

        testee.toggle(area);

        //2  0 0 0
        //1  2 2 0
        //0  2 2 0
        //x  0 1 2

        testee.toggle(new Area(0, 1, 1, 2));

        //2  2 2 0
        //1  4 4 0
        //0  2 2 0
        //x  0 1 2

        assertTrue(testee.isLit(0, 0), "0,0");
        assertTrue(testee.isLit(1, 0), "1,0");
        assertFalse(testee.isLit(2, 0), "2,0");
        assertTrue(testee.isLit(0, 1), "0,1");
        assertTrue(testee.isLit(1, 1), "1,1");
        assertFalse(testee.isLit(2, 1), "2,1");
        assertTrue(testee.isLit(0, 2), "0,2");
        assertTrue(testee.isLit(1, 2), "1,2");
        assertFalse(testee.isLit(2, 2), "2,2");
        assertEquals(16, testee.getTotalBrightness());
    }

    @Test
    void turnOnAreaAndAssertMatrixStatus() {
        Area area = new Area(1, 1, 2, 1);

        // X: 0 1 2
        // Y
        // 0  0 0 0
        // 1  0 1 1
        // 2  0 0 0

        testee.turnOn(area);

        assertTrue(testee.isLit(2, 1), "2,1");
        assertTrue(testee.isLit(1, 1), "1,1");
    }

    @Test
    void turnOnTwoTimesAndTurnOff() {
        Area area = new Area(0, 0, 0, 0);
        testee.turnOn(area);
        testee.turnOn(area);

        testee.turnOff(area);
        assertEquals(1, testee.getTotalBrightness());
    }

    @Test
    void toggleOneMillionLights() {
        Area area = new Area(0, 0, 999, 999);
        testee.toggle(area);
        assertEquals(2_000_000, testee.getTotalBrightness());
    }


}