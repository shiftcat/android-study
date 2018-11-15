package com.example.android.models;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Table(name = "BOARD", schema = "public", indexes = {@Index(name = "IDX_BOARD_SUBJECT", columnList = "SUBJECT")})
@SequenceGenerator(name = "SEQ_BOARD_GEN", schema = "public", sequenceName = "SEQ_BOARD", initialValue = 1, allocationSize = 10)
@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Board
{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BOARD_GEN")
    private Long id;

    @Column(name = "WRITER", length = 30, nullable = false)
    private String wirter;

    @Column(name = "SUBJECT", nullable = false, length = 150)
    private String subject;

    @Column(name = "CONTENT", nullable = false, length = 2000)
    private String content;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "BOARD_ATT_FILE", schema = "public",
            joinColumns = @JoinColumn(name = "BOARD_ID"),
            inverseJoinColumns = @JoinColumn(name = "FILE_ID"))
    @OrderBy(value = "ord asc")
    private List<AttachedFile> attachedFiles;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "THUMB_ID")
    private ThumbnailImage thumbnailImage;


    @Column(name = "FILE_CNT", nullable = false)
    private Integer fileCount;


    @CreationTimestamp
    private Timestamp insDate;

    @UpdateTimestamp
    private Timestamp updDate;
}
