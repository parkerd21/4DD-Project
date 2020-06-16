package com.henryschein.DD;

import com.henryschein.DD.entity.DataElement;
import com.henryschein.DD.service.DataElementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
class DataElementTests {
	DataElement dataElement;

	@Autowired
	DataElementService dataElementService;

	@BeforeEach
	void before() {
		dataElement = new DataElement();
	}

	@Test
	void storeStringValue() {
		String value = "abc123";

		dataElement.setValue(value);

		assert dataElement.getValue().equals(value);
	}

	@Test
	void retrieveByXcoord() {
		// arrange
		int xCoord = 5;
		int yCoord = 2;

		// act
		//dataElement = dataElementService.findByCoords(xCoord, yCoord);

		// assert
		assert dataElement.getValue().equals("Goals");

	}

//	@Test
//	void storeIntValue() {
//		Integer value = 12345;
//
//		dataElement.setValue(value.toString());
//
//		assert dataElement.getValue().equals(value.toString());
//	}
//
//	@Test
//	void storeDoubleValue() {
//		double value = 12.3;
//
//		dataElement.setValue(value);
//
//		assert dataElement.getValue().equals(value);
//	}

//	@Test
//	void storeCoordinates_Int() {
//		String expectedResult = "[5,8]";
//		int x = 5;
//		int y = 8;
//
//		dataElement.setCoords(x, y);
//
//		assert dataElement.getCoords().equals(expectedResult);
//	}

//	@Test
//	void storeCoordinates_String() {
//		String expectedResult = "[5,8]";
//
//		dataElement.setCoords(expectedResult);
//
//		assert dataElement.getCoords().equals(expectedResult);
//	}



}
