package com.example.android.thumbnail;

import com.example.android.models.ThumbnailImage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ThumbnailRepository extends JpaRepository<ThumbnailImage, Long>, JpaSpecificationExecutor<ThumbnailImage>
{
    List<ThumbnailImage> findAllBy(Pageable pageable);

    List<ThumbnailImage> findByOriginalNameLike(String originalName, Pageable pageable);
}
