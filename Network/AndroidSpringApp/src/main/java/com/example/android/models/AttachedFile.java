package com.example.android.models;

import com.example.android.attchedfile.FileType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "ATT_FILE", schema = "public", indexes = {@Index(name = "IDX_ATT_FILE_ORGIN_NM", columnList = "ORIGIN_NM")})
@SequenceGenerator(name = "SEQ_ATT_FILE_GENE", schema = "public", sequenceName = "SEQ_ATT_FILE", initialValue = 1, allocationSize = 10)
@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class AttachedFile extends FileEntity
{
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ATT_FILE_GENE")
    private Long id;

    @Column(length = 15)
    @Enumerated(EnumType.STRING)
    private FileType fileType;

    @Column(name = "ord")
    private Integer ord;

    @CreationTimestamp
    private Timestamp insDate;

    @UpdateTimestamp
    private Timestamp updDate;


}
