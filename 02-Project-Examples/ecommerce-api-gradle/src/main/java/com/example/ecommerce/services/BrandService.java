
package com.example.ecommerce.services;

import com.example.ecommerce.entities.Brand;
import com.example.ecommerce.repositories.BrandRepository;
import com.example.ecommerce.exceptions.NotFoundException;
import com.example.ecommerce.dtos.brand.BrandCreateDto;
import com.example.ecommerce.dtos.brand.BrandUpdateDto;
import com.example.ecommerce.dtos.brand.BrandResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;

    public Page<BrandResponseDto> getAllBrands(Pageable pageable) {
        return brandRepository.findAll(pageable)
                .map(this::toResponseDto);
    }

    public BrandResponseDto getBrandById(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Brand not found with id: " + id));
        return toResponseDto(brand);
    }

    public BrandResponseDto createBrand(BrandCreateDto dto) {
        Brand brand = new Brand();
        brand.setBrandName(dto.getName());
        brand.setBrandDescription(dto.getDescription());
        Brand saved = brandRepository.save(brand);
        return toResponseDto(saved);
    }

    public BrandResponseDto updateBrandById(Long id, BrandUpdateDto dto) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Brand not found with id: " + id));
        brand.setBrandName(dto.getName());
        brand.setBrandDescription(dto.getDescription());
        Brand saved = brandRepository.save(brand);
        return toResponseDto(saved);
    }


    private BrandResponseDto toResponseDto(Brand brand) {
        return new BrandResponseDto(
                brand.getId(),
                brand.getBrandName(),
                brand.getBrandDescription()
        );
    }
    public void deleteBrandById(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Brand not found with id: " + id));
        brandRepository.delete(brand);
    }
}
