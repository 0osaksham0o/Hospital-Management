package com.hospital.DepartmentTest;

import com.hospital.entity.Department;
import com.hospital.entity.Physician;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Department entity class.
 * Tests cover entity creation, getters, setters, and toString methods.
 */
public class DepartmentEntityTest {

    private Department department;
    private Physician physician;

    @BeforeEach
    void setUp() {
        // Initialize test data
        physician = new Physician();
        physician.setEmployeeId(1);
        physician.setName("Dr. John Smith");
        physician.setPosition("Cardiologist");
        physician.setSsn(123456789);

        department = new Department();
        department.setDepartmentId(1);
        department.setName("Cardiology");
        department.setHead(physician);
    }

    // ==================== Constructor Tests ====================

    @Test
    void testDefaultConstructor() {
        Department dept = new Department();
        assertNotNull(dept);
        assertNull(dept.getDepartmentId());
        assertNull(dept.getName());
        assertNull(dept.getHead());
    }

    @Test
    void testParameterizedConstructor() {
        Department dept = new Department(2, "Neurology", physician);

        assertNotNull(dept);
        assertEquals(2, dept.getDepartmentId());
        assertEquals("Neurology", dept.getName());
        assertNotNull(dept.getHead());
        assertEquals(1, dept.getHead().getEmployeeId());
        assertEquals("Dr. John Smith", dept.getHead().getName());
    }

    @Test
    void testParameterizedConstructorWithDifferentValues() {
        Physician surgeon = new Physician(2, "Dr. Jane Doe", "Surgeon", 987654321);
        Department dept = new Department(3, "Surgery", surgeon);

        assertEquals(3, dept.getDepartmentId());
        assertEquals("Surgery", dept.getName());
        assertEquals(2, dept.getHead().getEmployeeId());
        assertEquals("Dr. Jane Doe", dept.getHead().getName());
    }

    // ==================== Getter Tests ====================

    @Test
    void testGetDepartmentId() {
        assertEquals(1, department.getDepartmentId());
    }

    @Test
    void testGetName() {
        assertEquals("Cardiology", department.getName());
    }

    @Test
    void testGetHead() {
        assertNotNull(department.getHead());
        assertEquals(physician, department.getHead());
        assertEquals("Dr. John Smith", department.getHead().getName());
    }

    // ==================== Setter Tests ====================

    @Test
    void testSetDepartmentId() {
        department.setDepartmentId(10);
        assertEquals(10, department.getDepartmentId());
    }

    @Test
    void testSetDepartmentIdMultipleTimes() {
        department.setDepartmentId(5);
        assertEquals(5, department.getDepartmentId());

        department.setDepartmentId(15);
        assertEquals(15, department.getDepartmentId());
    }

    @Test
    void testSetName() {
        department.setName("Orthopedics");
        assertEquals("Orthopedics", department.getName());
    }

    @Test
    void testSetNameMultipleTimes() {
        department.setName("Pediatrics");
        assertEquals("Pediatrics", department.getName());

        department.setName("Emergency");
        assertEquals("Emergency", department.getName());
    }

    @Test
    void testSetHead() {
        Physician newPhysician = new Physician(3, "Dr. Bob Wilson", "Pediatrician", 555555555);
        department.setHead(newPhysician);

        assertNotNull(department.getHead());
        assertEquals(3, department.getHead().getEmployeeId());
        assertEquals("Dr. Bob Wilson", department.getHead().getName());
    }

    @Test
    void testSetHeadMultipleTimes() {
        Physician firstPhysician = new Physician(2, "Dr. Alice", "Orthopedist", 111111111);
        Physician secondPhysician = new Physician(3, "Dr. Bob", "Neurologist", 222222222);

        department.setHead(firstPhysician);
        assertEquals("Dr. Alice", department.getHead().getName());

        department.setHead(secondPhysician);
        assertEquals("Dr. Bob", department.getHead().getName());
    }

    // ==================== ToString Tests ====================

    @Test
    void testToString() {
        String result = department.toString();
        assertNotNull(result);
        assertTrue(result.contains("Department"));
        assertTrue(result.contains("departmentId=1"));
        assertTrue(result.contains("name=Cardiology"));
    }

    @Test
    void testToStringFormat() {
        String result = department.toString();
        assertEquals("Department{departmentId=1, name=Cardiology}", result);
    }

    @Test
    void testToStringWithDifferentValues() {
        Department dept = new Department(5, "Radiology", physician);
        String result = dept.toString();
        assertEquals("Department{departmentId=5, name=Radiology}", result);
    }

    // ==================== Null Handling Tests ====================

    @Test
    void testSetNameToNull() {
        department.setName(null);
        assertNull(department.getName());
    }

    @Test
    void testSetHeadToNull() {
        department.setHead(null);
        assertNull(department.getHead());
    }

    @Test
    void testSetDepartmentIdToNull() {
        department.setDepartmentId(null);
        assertNull(department.getDepartmentId());
    }

    // ==================== Equality and Identity Tests ====================

    @Test
    void testDifferentDepartmentsWithSameValues() {
        Department dept1 = new Department(1, "Cardiology", physician);
        Department dept2 = new Department(1, "Cardiology", physician);

        // Different objects but same values
        assertNotSame(dept1, dept2);
        assertEquals(dept1.getDepartmentId(), dept2.getDepartmentId());
        assertEquals(dept1.getName(), dept2.getName());
    }

    @Test
    void testDifferentDepartmentsWithDifferentIds() {
        Department dept1 = new Department(1, "Cardiology", physician);
        Department dept2 = new Department(2, "Cardiology", physician);

        assertNotEquals(dept1.getDepartmentId(), dept2.getDepartmentId());
    }

    @Test
    void testDifferentDepartmentsWithDifferentNames() {
        Department dept1 = new Department(1, "Cardiology", physician);
        Department dept2 = new Department(1, "Neurology", physician);

        assertNotEquals(dept1.getName(), dept2.getName());
    }

    // ==================== Edge Case Tests ====================

    @Test
    void testEmptyStringName() {
        department.setName("");
        assertEquals("", department.getName());
    }

    @Test
    void testLongStringName() {
        String longName = "A".repeat(32); // Max length is 32
        department.setName(longName);
        assertEquals(longName, department.getName());
    }

    @Test
    void testZeroDepartmentId() {
        department.setDepartmentId(0);
        assertEquals(0, department.getDepartmentId());
    }

    @Test
    void testNegativeDepartmentId() {
        department.setDepartmentId(-1);
        assertEquals(-1, department.getDepartmentId());
    }

    @Test
    void testLargeDepartmentId() {
        department.setDepartmentId(999999);
        assertEquals(999999, department.getDepartmentId());
    }

    // ==================== State Change Tests ====================

    @Test
    void testMultipleUpdatesToSameDepartment() {
        // Initial state
        assertEquals(1, department.getDepartmentId());
        assertEquals("Cardiology", department.getName());

        // First update
        department.setDepartmentId(2);
        department.setName("Neurology");
        assertEquals(2, department.getDepartmentId());
        assertEquals("Neurology", department.getName());

        // Second update
        department.setDepartmentId(3);
        department.setName("Surgery");
        assertEquals(3, department.getDepartmentId());
        assertEquals("Surgery", department.getName());
    }

    @Test
    void testHeadPhysicianDetailsPreserved() {
        Physician head = department.getHead();
        assertEquals(1, head.getEmployeeId());
        assertEquals("Dr. John Smith", head.getName());
        assertEquals("Cardiologist", head.getPosition());

        // Modify physician details
        head.setPosition("Chief Cardiologist");
        assertEquals("Chief Cardiologist", department.getHead().getPosition());
    }
}
