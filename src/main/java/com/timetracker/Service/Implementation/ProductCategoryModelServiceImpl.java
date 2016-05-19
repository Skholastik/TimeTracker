/*
package com.timetracker.Service.Implementation;


import com.timetracker.DAO.Interfaces.ProductCategoryModelDao;
import com.timetracker.Entities.DTO.CharacteristicAttributeDTO;
import com.timetracker.Entities.DTO.ProductCategoryModelDTO;
import com.timetracker.Entities.DTO.ProductCategoryModelListDTO;
import com.timetracker.Entities.ProductCategoryModel;
import com.timetracker.Entities.ProductCategoryModelCharacteristics;
import com.timetracker.Service.AncillaryServices.ResponseMessage;
import com.timetracker.Service.Interfaces.ProductCategoryModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ProductCategoryModelServiceImpl implements ProductCategoryModelService {

    @Autowired
    private ProductCategoryModelDao productCategoryModelDao;

    @Override
    public ProductCategoryModelListDTO getProductCategoryModel(int categoryId, int maxResults, int currentPage, boolean needToReceiveAttributes) {

        ProductCategoryModelListDTO productCategoryModelListDTO = new ProductCategoryModelListDTO();
        int numberOfRecords = productCategoryModelDao.getNumberOfAllRecordsCurrentProductCategory(categoryId);

        if (numberOfRecords != 0) {
            productCategoryModelListDTO.setNumberOfPages((int) Math.ceil((float) numberOfRecords / (float) maxResults));
            productCategoryModelListDTO.addCharacteristicAttribute(new CharacteristicAttributeDTO("ID"));

            int resultsFromCurrentIndex = ((currentPage - 1) * maxResults);

            List<ProductCategoryModel> productCategoryModelList = productCategoryModelDao.getProductCategoryModelListByProductCategoryId(categoryId, maxResults, resultsFromCurrentIndex);

            for (int i = 0; i < productCategoryModelList.size(); i++) {
                if (needToReceiveAttributes && i == 0) {
                    for (ProductCategoryModelCharacteristics characteristics : productCategoryModelList.get(i)
                            .getCharacteristics())
                        productCategoryModelListDTO.addCharacteristicAttribute(new CharacteristicAttributeDTO(characteristics.getAttribute().getName()));
                }
                productCategoryModelListDTO.addProductCategoryModelDTO(new ProductCategoryModelDTO(productCategoryModelList.get(i)));
            }
        }

        return productCategoryModelListDTO;
    }

    @Override
    public ResponseMessage addProductCategoryModel(int categoryId, ProductCategoryModelDTO productCategoryModelDTO) {
        return null;
    }

}
*/
