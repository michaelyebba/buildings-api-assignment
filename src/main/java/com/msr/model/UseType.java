package com.msr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UseType {

    @Id
    @EqualsAndHashCode.Include
    private int id;

    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "useType")
    @JsonIgnore
    private List<SiteUse> siteUses;
}
