package com.thanhti.shop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import com.thanhti.shop.domain.Category;
import com.thanhti.shop.domain.Product;

public class CSVHelperProduct {

public static String TYPE = "text/csv";
	
	static String[] HEADERs = {"Name", "Category_ID", "Quantity", "Old Price", "New Price", "Image","Qescription","Note","Discount","Entered Date"};
	
    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }
    
    public static List<Product> readProductData(InputStream is) throws ParseException {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<Product> productEntityList = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Product product = new Product();
                
                product.setName(csvRecord.get(HEADERs[0]));
                Category category = new Category();
                category.setId(Long.parseLong(csvRecord.get(HEADERs[1])));
                product.setCategory(category);
                product.setQuantity(Integer.parseInt(csvRecord.get(HEADERs[2])));
                product.setOldPrice(Integer.parseInt(csvRecord.get(HEADERs[3])));
                product.setNewPrice(Integer.parseInt(csvRecord.get(HEADERs[4])));               
                product.setImage(csvRecord.get(HEADERs[5]));
                product.setDescription(csvRecord.get(HEADERs[6]));
                product.setNote(csvRecord.get(HEADERs[7]));
                product.setDiscount(Integer.parseInt(csvRecord.get(HEADERs[8])));
                product.setEnteredDate(new SimpleDateFormat("dd/MM/yyyy").parse(csvRecord.get(HEADERs[9])));
                productEntityList.add(product);
            }

            return productEntityList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
}
