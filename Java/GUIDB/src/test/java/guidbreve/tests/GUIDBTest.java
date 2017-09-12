/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guidbreve.tests;

import guidbreve.guidb.GUIDB;
import java.util.UUID;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author mainuser
 */
public class GUIDBTest
{
    guidbreve.guidb.GUIDB guidb;
    
    @BeforeClass
    public static void setUpClass()
    {
        
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp()
    {
    
    }
    
    @After
    public void tearDown()
    {

        
    }
    
    @Test
    public void guidbTest1()
    {
        for (int i = 0; i < 500; i++)
        {
            UUID uuid = UUID.randomUUID();
            String uuids = uuid.toString();
            

            String clearString = uuids.replaceAll("-", "");

            String guidb = ""+GUIDB.encode(clearString);
            
            String out = GUIDB.decode(GUIDB.encode(clearString));
            //log.info(out);
            String outs = out.replaceFirst("([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5");
            //log.info(outs);
            //log.info();
            if (!uuid.toString().equals(outs))
            {
                throw new AssertionError("Error 3YNHGD*A1EHV#VWWP1#18MWPG");
            }
        }
    }
    
    
}
