package com.msr.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
public class SiteUse {

    @Id
    private int id;

    private String description;

    @Column(name = "size_sqft")
    private long sizeSqft;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "use_type_id")//, insertable=false, updatable=false) // so we can keep useTypeId
    private UseType useType;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "site_id")//, insertable=false, updatable=false) // so we can keep siteId
    private Site site;
}
