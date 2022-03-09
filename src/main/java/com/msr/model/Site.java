package com.msr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Site {

    @Id
    private int id;

    private String name;

    private String address;

    private String city;

    private String state;

    private String zipcode;

    @ToString.Exclude
    @OneToMany(mappedBy = "site")
    @JsonIgnore
    private List<SiteUse> siteUses;

    @Transient
    @JsonProperty("total_size")
    private Long totalSize;

    @Transient
    @JsonProperty("primary_type")
    private UseType primaryUseType;
}
