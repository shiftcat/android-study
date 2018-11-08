package com.example.android.attchedfile;

import com.example.android.models.AttachedFile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<AttachedFile, Long>, JpaSpecificationExecutor<AttachedFile>
{

    List<AttachedFile> findAllBy(Pageable pageable);

    List<AttachedFile> findByFileType(String fileType, Pageable pageable);

    List<AttachedFile> findByOriginalNameLike(String originalName, Pageable pageable);

    Optional<AttachedFile> findTop1ByOriginalNameAndSize(String originalName, Long size);

}
