package com.example.android.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "THUMB_IMG", schema = "public", indexes = {@Index(name = "IDX_THUMB_IMG_ORGIN_NM", columnList = "ORIGIN_NM")})
@SequenceGenerator(name = "SEQ_THUMB_IMG_GENE", schema = "public", sequenceName = "SEQ_THUMB_IMG", initialValue = 1, allocationSize = 10)
@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ThumbnailImage extends FileEntity
{
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_THUMB_IMG_GENE")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORIGIN_FILE_ID", nullable = false)
    private AttachedFile originalFile;

    @CreationTimestamp
    private Timestamp insDate;

    @UpdateTimestamp
    private Timestamp updDate;
}
