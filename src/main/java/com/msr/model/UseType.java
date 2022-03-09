package com.msr.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Entity
public class UseType {
    @Id
    private int id;

    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "useType")
    private List<SiteUse> siteUses;
}
