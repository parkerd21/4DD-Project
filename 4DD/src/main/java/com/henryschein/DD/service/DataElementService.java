package com.henryschein.DD.service;

import com.henryschein.DD.TheCacheManager;
import com.henryschein.DD.dao.DataElementDAO;
import com.henryschein.DD.dto.DataElementDTO;
import com.henryschein.DD.entity.DataElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DataElementService {

    private final DataElementDAO dataElementDAO;
    private final TheCacheManager cacheManager;

    public DataElementService(DataElementDAO theDataElementDAO, TheCacheManager cacheManager) {
        this.dataElementDAO = theDataElementDAO;
        this.cacheManager = cacheManager;
    }

    public DataElement getDataElement(DataElementDTO dto) {
        DataElement dataElement = cacheManager.retrieveDataElement(dto);
        if (Objects.isNull(dataElement)) {
            dataElement = getDataElementByXY(dto);
            if (dataElement != null) {
                log.info("database: retrieved dataElement " + dataElement.toString());
            } else {
                log.info("database: dataElement not found " + dto.toString());
            }
        }
        return dataElement;
    }

    private DataElement getDataElementByXY(DataElementDTO dto) {
        List<DataElement> dataElementList = dataElementDAO.getDataElementListByXY(dto.getPageId(), dto.getXcoord(), dto.getYcoord());
        if (Objects.nonNull(dataElementList) && !dataElementList.isEmpty()) {
            log.info("database: retrieved list " + prettyPrintCoordsAndPageId(dto) + dataElementList.toString());
            cacheManager.saveDataElementListToDataElementCache(dataElementList);
            return getDataElementFromList(dataElementList, dto);
        } else {
            return null;
        }
    }

    public List<DataElement> getDataElementListByXY(DataElementDTO dto) {
        String key = "getByXY" + dto.getPageIdXY();
        List<DataElement> dataElementList = cacheManager.retrieveDataElementList(key);
        if (Objects.isNull(dataElementList) || dataElementList.isEmpty()) {
            dataElementList = dataElementDAO.getDataElementListByXY(dto.getPageId(), dto.getXcoord(), dto.getYcoord());
            if (!dataElementList.isEmpty()) {
                log.info("database: retrieved list " + prettyPrintCoordsAndPageId(dto) + dataElementList.toString());
                cacheManager.saveDataElementListToDataElementCache(dataElementList);
                cacheManager.saveDataElementListToListCache(key, dataElementList);
            } else {
                log.info("database: could not find dataElement " + dto.toString());
            }
        }
        return dataElementList;
    }

    public List<DataElement> getAll() {
        List<DataElement> dataElements = dataElementDAO.findAll();
        log.info("database: retrieving all dataElements");
        return dataElements;
    }

    public DataElement createNewDataElement(DataElementDTO dto) {
        DataElement dataElement = getDataElement(dto);
        if (dataElement == null) {
            dto.setZcoord(1);
            DataElement newElement = new DataElement(dto);
            try {
                newElement = dataElementDAO.saveAndFlush(newElement);
                log.info("database: added new dataElement " + newElement.toString());
                return newElement;
            } catch (Exception e) {
                log.error("database: failed to create dataElement in page " + dto.getPageId());
                return null;
            }
        } else {
            log.error("database: failed to create dataElement in page " + dto.getPageId());
            return null;
        }
    }

    public DataElement updateDataElement(DataElementDTO dto) {
        DataElement dataElement = getDataElement(dto);
        if (dataElement != null) {
            DataElement newElement = new DataElement(dto);
            newElement.setZcoord(dataElement.getZcoord() + 1);
            newElement = dataElementDAO.saveAndFlush(newElement);
            log.info("dataBase: updated/added dataElement " + newElement.toString());
            cacheManager.updateDataElementCacheRefresh(newElement, dto.getPageIdXY());
            return newElement;
        }
        else
            return null;
    }

    @Transactional
    public void deleteByXY(DataElementDTO dto) {
        DataElement dataElement = getDataElement(dto);
        if (dataElement != null) {
            dataElementDAO.deleteByXY(dto.getPageId(), dto.getXcoord(), dto.getYcoord());
            log.info("dataBase: deleted dataElements at  " + prettyPrintCoordsAndPageId(dto));
            cacheManager.invalidateDataElementCache(dto);
            cacheManager.invalidateDataElementListCache();
        }
    }

    public List<DataElement> getByRow(Integer pageId, Integer rowNumber) {
        String key = "getByRow" + pageId + rowNumber;
        List<DataElement> dataElementList = cacheManager.retrieveDataElementList(key);
        if (dataElementList == null || dataElementList.isEmpty()) {
            dataElementList = dataElementDAO.getByRow(pageId, rowNumber);
            if (dataElementList != null && !dataElementList.isEmpty()) {
                log.info("database: retrieved row: " + rowNumber + " from page " + pageId);
                cacheManager.saveDataElementListToDataElementCache(dataElementList);
                dataElementList = removeHistoryFromResult(dataElementList);
                cacheManager.saveDataElementListToListCache(key, dataElementList);
            } else {
                log.info("database: no dataElements to retrieve in row: " + rowNumber + " from page " + pageId);
            }
        }
        return dataElementList;
    }

    public List<DataElement> getByColumn(Integer pageId, Integer columnNumber) {
        String key = "getByColumn" + pageId + columnNumber;
        List<DataElement> dataElementList = cacheManager.retrieveDataElementList(key);
        if (dataElementList == null || dataElementList.isEmpty()) {
            dataElementList = dataElementDAO.getByColumn(pageId, columnNumber);
            if (dataElementList != null && !dataElementList.isEmpty()) {
                log.info("database: retrieved column: " + columnNumber + " from page " + pageId);
                cacheManager.saveDataElementListToDataElementCache(dataElementList);
                dataElementList = removeHistoryFromResult(dataElementList);
                cacheManager.saveDataElementListToListCache(key, dataElementList);
            } else {
                log.info("database: no dataElements to retrieve in column: " + columnNumber + " from page " + pageId);
            }
        }
        return dataElementList;
    }

    public List<DataElement> getByRange(Integer pageId, String range) {
        String key = "getByRange" + pageId + range;
        List<DataElement> dataElementList = cacheManager.retrieveDataElementList(key);
        if (dataElementList == null || dataElementList.isEmpty()) {
            String tempString = removeLeadingAndTrailingCharacterFromString(range, '[', ']');
            String[] stringArray = StringUtils.commaDelimitedListToStringArray(tempString);
            dataElementList = getDataElementListByRange(pageId, stringArray);
            if (dataElementList != null && !dataElementList.isEmpty()) {
                log.info("database: retrieved range of dataElements [" + stringArray[0] + ", " + stringArray[1] + "]");
                cacheManager.saveDataElementListToDataElementCache(dataElementList);
                dataElementList = removeHistoryFromResult(dataElementList);
                cacheManager.saveDataElementListToListCache(key, dataElementList);
            } else {
                log.info("database: no dataElements to retrieve in that range: [" + stringArray[0] + ", " + stringArray[1] + "]");
            }
        }
        return dataElementList;
    }

    private String removeLeadingAndTrailingCharacterFromString(String range, char leadingChar, char trailingChar) {
        String tempString = StringUtils.trimLeadingCharacter(range, leadingChar);
        return StringUtils.trimTrailingCharacter(tempString, trailingChar);
    }

    private List<Integer> convertStringToListOfIntegers(String range) {
        List<String> stringList = Arrays.asList(StringUtils.delimitedListToStringArray(range, ".."));
        List<Integer> integerList = new ArrayList<>();
        int lastValue = Integer.parseInt(stringList.get(stringList.size() - 1));
        int firstValue = Integer.parseInt(stringList.get(0));
        for (int i = firstValue; i <= lastValue; i++) {
            integerList.add(i);
        }
        return integerList;
    }
//     TODO:
//     "=[1,2] + [1,3]"
//     "=SUM([1..3,1..6])"
//     "=AVG([3,3])"
//     "=MIN(3,1..10])"
//     "=MAX(3,1..10])"

    //TODO: do some more testing
    public Double evaluateEquationString(Integer pageId, String equation) {
        equation = StringUtils.trimLeadingCharacter(equation, '=').toUpperCase();

        if (equation.startsWith("SUM")) {
            return evaluateSumEquation(pageId, equation);
        } else if (equation.startsWith("AVG")) {
            calculateAvg();
        } else if (equation.startsWith("MIN")) {
            calculateMin();
        }
        return null;
    }

    private Double evaluateSumEquation(Integer pageId, String equation) {
        equation = parseSumEquation(equation);
        List<DataElement> dataElementList = getByRange(pageId, equation);
        return calculateSum(dataElementList);
    }

    private List<DataElement> getDataElementListByRange(Integer pageId, String[] stringArray) {
        int x = 0;
        int y = 1;
        List<Integer> xValues = convertStringToListOfIntegers(stringArray[x]);
        List<Integer> yValues = convertStringToListOfIntegers(stringArray[y]);
        return dataElementDAO.getByRange(pageId, xValues.get(0), xValues.get(xValues.size() - 1), yValues.get(0), yValues.get(yValues.size() - 1));
    }

    private String parseSumEquation(String equation) {
        equation = StringUtils.delete(equation, "SUM");
        equation = removeLeadingAndTrailingCharacterFromString(equation, '(', ')');
        return equation;
    }

    private Double calculateSum(List<DataElement> dataElementList) {
        AtomicReference<Double> sum = new AtomicReference<>(0.0);
        dataElementList.forEach(dataElement -> {
            sum.set(sum.get() + Double.parseDouble(dataElement.getDataValue().getValue()));
        });
        return sum.get();
    }


    private Double calculateAvg() {
        return null;
    }

    private Double calculateMin() {
        return null;
    }

    private List<DataElement> removeHistoryFromResult(List<DataElement> rowWithHistory) {
        Map<String, DataElement> completedRow = new HashMap<>();
        for (DataElement dataElement : rowWithHistory) {
            String key = dataElement.getPageIdXY();
            if (completedRow.containsKey(key)) {
                if (completedRow.get(key).getZcoord() < dataElement.getZcoord())
                    completedRow.put(key, dataElement);
            } else
                completedRow.put(key, dataElement);
        }
        return new ArrayList<>(completedRow.values());
    }

    private DataElement getDataElementFromList(List<DataElement> dataElementList, DataElementDTO dto) {
        Map<Integer, DataElement> map = dataElementList.stream().collect(Collectors.toMap(DataElement::getZcoord, dataElement -> dataElement));
        if (Objects.isNull(dto.getZcoord())) {
            Integer maxZvalue = Collections.max(map.keySet());
            return map.get(maxZvalue);
        } else {
            return map.get(dto.getZcoord());
        }
    }

    public static String prettyPrintCoordsAndPageId(DataElementDTO dto) {
        return "" + dto.getPageId() + ": [" + dto.getXcoord() + "," + dto.getYcoord() + "]";
    }
}
