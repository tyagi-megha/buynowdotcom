package com.dailycodework.buynowdotcom.service.image;

import com.dailycodework.buynowdotcom.dto.ImageDto;
import com.dailycodework.buynowdotcom.model.Image;
import com.dailycodework.buynowdotcom.model.Product;
import com.dailycodework.buynowdotcom.repository.ImageRepository;
import com.dailycodework.buynowdotcom.service.product.IProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final IProductService productService;

    @Override
    public Image getImageById(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Image with id " + imageId + " not found"));
    }

    @Override
    public void deleteImageById(Long imageId) {
        imageRepository.findById(imageId)
                .ifPresentOrElse(imageRepository::delete, () -> {
                    throw new EntityNotFoundException("Image not found");
                });
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try{
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        }
        catch(IOException | SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<ImageDto> saveImages(Long productId, List<MultipartFile> files) {
        Product product = productService.getProductById(productId);

        List<ImageDto> savedImages = new ArrayList<>();
        for (MultipartFile file : files) {
            try{
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl = "/api/v1/images/image/download/";
                //this id is from front end
                String downloadUrl = buildDownloadUrl + image.getId() ;
                image.setDownloadUrl(downloadUrl);
                Image savedImage = imageRepository.save(image);
                //this id is generated from SQL hence we set it again
                savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
                imageRepository.save(savedImage);

                //build info going to return to front end
                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setDownloadURL(savedImage.getDownloadUrl());
                savedImages.add(imageDto);
            }
            catch(IOException | SQLException e)
            {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImages;
    }
}
