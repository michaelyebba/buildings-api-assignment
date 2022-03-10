package com.msr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.msr.model.projection.TotalSiteUseByType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class UseType {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "useType")
    @JsonIgnore
    private List<SiteUse> siteUses;

    /**
     * Constructor to use to convert a totalSiteUseByType projection to a useType object
     *
     * @param totalSiteUseByType
     */
    public UseType(TotalSiteUseByType totalSiteUseByType) {
        this.id = totalSiteUseByType.getUseTypeId();
        this.name = totalSiteUseByType.getUseTypeName();
    }
}
