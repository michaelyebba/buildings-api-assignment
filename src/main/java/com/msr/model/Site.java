package com.msr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.msr.model.projection.DecoratedSite;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
/*@JsonTypeName(value = "site")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)*/
public class Site {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

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

    /**
     * Constructor to transform a DecoratedSite into a Site object
     */
    public Site(DecoratedSite decoratedSite) {
        this.id = decoratedSite.getId();
        this.name = decoratedSite.getName();
        this.address = decoratedSite.getAddress();
        this.city = decoratedSite.getCity();
        this.state = decoratedSite.getState();
        this.zipcode = decoratedSite.getZipCode();
        this.totalSize = decoratedSite.getTotalSizeSqft();

        UseType primaryUt = new UseType();
        primaryUt.setId(decoratedSite.getUseTypeId());
        primaryUt.setName(decoratedSite.getUseTypeName());
        this.primaryUseType = primaryUt;
    }
}
