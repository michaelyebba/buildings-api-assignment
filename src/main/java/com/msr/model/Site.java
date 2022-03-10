package com.msr.model;

import com.fasterxml.jackson.annotation.*;
import com.msr.model.projection.TotalSiteUseByType;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonTypeName(value = "site")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class Site {

    @Id
    @EqualsAndHashCode.Include
    private int id;

    private String name;

    private String address;

    private String city;

    private String state;

    private String zipcode;

    @ToString.Exclude
    @OneToMany(mappedBy = "site", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<SiteUse> siteUses;

    @Transient
    @JsonProperty("total_size")
    private long totalSize;

    @Transient
    @JsonProperty("primary_type")
    private UseType primaryUseType;
}
