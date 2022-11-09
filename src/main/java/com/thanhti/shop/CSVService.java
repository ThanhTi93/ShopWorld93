package com.thanhti.shop;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.thanhti.shop.domain.Category;
import com.thanhti.shop.repository.CategoryRepository;

@Service
public class CSVService {

	@Autowired
	CategoryRepository   categoryRepository;
	
	public void saveCategoryData(MultipartFile file) {
        try {
            List<Category> bookEntityList = CSVHelper.readCategoryData(file.getInputStream());
            categoryRepository.saveAll(bookEntityList);
        } catch (IOException  e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }
}
