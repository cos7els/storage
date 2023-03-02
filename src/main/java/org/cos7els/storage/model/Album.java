package org.cos7els.storage.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "albums", schema = "public")
public class Album {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "albums_seq")
    @SequenceGenerator(name = "albums_seq", sequenceName = "albums_id_seq", allocationSize = 1)
    private long id;
    @JsonBackReference(value = "albumOwner")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "title")
    private String title;
    @JsonManagedReference(value = "photosOfAlbum")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "albums_photos",
            joinColumns = @JoinColumn(name = "album_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "photo_id", referencedColumnName = "id"))
    private List<Photo> photos;
}