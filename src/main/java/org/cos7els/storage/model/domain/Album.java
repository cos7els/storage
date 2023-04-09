package org.cos7els.storage.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "albums", schema = "public")
public class Album {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "albums_seq")
    @SequenceGenerator(name = "albums_seq", sequenceName = "albums_id_seq", allocationSize = 1)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "title")
    private String title;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinTable(name = "albums_photos", joinColumns = @JoinColumn(name = "album_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "photo_id", referencedColumnName = "id"))
    private List<Photo> photos;
}