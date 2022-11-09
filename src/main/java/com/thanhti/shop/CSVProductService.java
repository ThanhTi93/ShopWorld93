package com.thanhti.shop;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import com.thanhti.shop.domain.Product;
import com.thanhti.shop.repository.ProductRepository;

@Service
public class CSVProductService {

	@Autowired
	ProductRepository   productRepository;
	
	public void saveProductData(MultipartFile file) throws ParseException {
        try {
            List<Product> productEntityList = CSVHelperProduct.readProductData(file.getInputStream());
            productRepository.saveAll(productEntityList);
        } catch (IOException  e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }
}
