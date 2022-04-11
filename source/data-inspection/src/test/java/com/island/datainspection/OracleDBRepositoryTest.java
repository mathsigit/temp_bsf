package com.island.datainspection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
public class OracleDBRepositoryTest {

    @Test
    public void contextLoads(){
        double oResult = Double.parseDouble("999");
        double hResult = Double.parseDouble("990");
        double aa = ( 1 + (hResult - oResult)/oResult) * 100;
        System.out.println(aa);
    }

    @Test
    public void compareStringValue() {
        Map<String,String> map = new HashMap<>();
        map.put("HIVE", "4096");
        map.put("ORACLE", "4096");
        Assert.assertTrue(map.get("HIVE").equals(map.get("ORACLE")));
    }

    @Test
    public void timestampTest() throws ParseException {
//        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("DD-Mon-RR HH:MI:SS.FF AM");
//        LocalDateTime dateTime = LocalDateTime.parse("14-MAR-22 04.52.53.583285000 PM",dateFormat);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
        System.out.println(dateTimeFormat.format(now).toString());

//        "14-MAR-22 04.52.53.583285000 PM")
//        System.out.println(dateTime.toString());
    }
}
