/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package tools;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Gato Sagrado
 */
public class CriptografadorTest {
    
    public CriptografadorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of md5 method, of class Criptografador.
     */
    @Test
    public void testMd5() throws Exception {
        System.out.println("md5");
        String senha = "senhamuitolegal123";
        String expResult = "fab669bdf2c777a042cb71797bc9994a";
        String result = Criptografador.md5(senha);
        assertEquals(expResult, result);
        senha = "outrasenhalegal456";
        expResult = "09d4bc610b22b4d64a5ff771442bcc23";
        result = Criptografador.md5(senha);
        assertEquals(expResult, result);
    }
    
}
